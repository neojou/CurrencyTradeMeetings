# strategy-calculate-method.md — 停利/停損最佳化計算方法

> 本文件描述如何以 00706L 歷史資料，透過回放模擬（Backtest Simulation）
> 找出在「短打策略 + 月期望 ≥ 1,000 元 + 風險最低」目標下，
> 最合適的停利（TP）與停損（SL）百分比組合。

---

## 一、前置資料準備

### 1.1 價格資料來源
- **00706L 日線資料**（OHLC + 成交量）
  - 建議使用「反分割後」的價格（避免 2025/10 反分割造成的斷點影響績效計算）
  - 可從 Yahoo Finance（00706L.TW）、Stooq、元大投信官網、Yuanta ETFs 等取得 CSV
  - 必要欄位：`Date, Open, High, Low, Close, Volume`
- **USD/JPY 日線資料**（同期間）
  - 用來判斷進場訊號（MA20、RSI14）
  - 建議來源：Stooq（`usdjpy`）、Yahoo Finance（`JPY=X`）
  - 必要欄位：`Date, Open, High, Low, Close`

### 1.2 資料清洗
- 對齊日期：00706L 與 USD/JPY 只保留「兩者均有交易日」的日期（台灣休市日排除）
- 驗證無缺值（特別是 Close、High、Low）
- 確認反分割前後的價格連續性，必要時使用「調整後收盤價」

### 1.3 技術指標計算
```python
import pandas as pd

# USD/JPY
usdjpy['MA20'] = usdjpy['Close'].rolling(20).mean()
usdjpy['RSI14'] = compute_rsi(usdjpy['Close'], 14)  # 標準 Wilder RSI

# 進場訊號：USD/JPY 跌破 MA20 且 RSI < 30
usdjpy['signal'] = (
    (usdjpy['Close'] < usdjpy['MA20']) &
    (usdjpy['RSI14'] < 30)
)
```


---

## 二、交易成本設定（元大證券盤中零股）

| 項目 | 費率 |
| :-- | :-- |
| 買進手續費（電子交易 6 折） | 0.0855%（每筆最低 1 元） |
| 賣出手續費（電子交易 6 折） | 0.0855% |
| 賣出證券交易稅 | 0.300% |
| **來回合計成本** | **≈ 0.471%** |

> 注意：若改用整股交易或其他券商，`cost` 需重新計算並帶入所有公式。

```python
cost = 0.000855 + 0.000855 + 0.003  # = 0.00471
```


---

## 三、回放規則（Backtest Simulation）

### 3.1 進場條件

- 訊號出現日（USD/JPY 收盤同時滿足「跌破 MA20 + RSI < 30」）：
    - **進場價格** → 訊號當日收盤價（或隔日開盤價，兩者取一，需保持一致）

> **建議選「訊號當日收盤價」**：更接近你用 App 看到訊號後在 1340–1430 零股盤中掛單的實務情境。

### 3.2 出場判斷（逐日掃描，最長 7 個交易日）

對每一筆進場，在持倉期間的每個交易日：

```python
for day in range(1, max_hold_days + 1):
    # 當天最高價是否 >= 停利目標
    tp_triggered = (high_price >= entry_price * (1 + TP))
    # 當天最低價是否 <= 停損目標
    sl_triggered = (low_price <= entry_price * (1 - SL))

    if tp_triggered and sl_triggered:
        result = 'loss'   # 同日同時觸發 → 保守假設：先停損
    elif sl_triggered:
        result = 'loss'
    elif tp_triggered:
        result = 'win'
    elif day == max_hold_days:
        result = 'timeout'  # 第 7 日強制收盤出場
```


### 3.3 各種出場的淨報酬計算

```python
if result == 'win':
    net_return = TP - cost

elif result == 'loss':
    net_return = -SL - cost

elif result == 'timeout':
    exit_price = close_on_day_N  # N = 5 或 7（可分別測試）
    net_return = (exit_price / entry_price - 1) - cost
```


---

## 四、網格最佳化（TP/SL Grid Search）

### 4.1 掃描範圍

```python
TP_range = [round(x * 0.005, 3) for x in range(4, 25)]
# TP: 2.0% ~ 12.0%（每 0.5% 一格）

SL_range = [round(x * 0.005, 3) for x in range(2, 15)]
# SL: 1.0% ~ 7.0%（每 0.5% 一格）
```

> 注意：必須確保 `TP > cost`（否則贏了還虧本），即 TP > 0.471%；
> 同樣 `SL > 0`（不設停損不合策略精神）。

### 4.2 每組 TP/SL 計算的指標

| 指標 | 計算說明 |
| :-- | :-- |
| 勝率（Win Rate） | 出場為「win」的交易筆數 / 總交易筆數 |
| 損益兩平勝率 | `SL / (TP + SL)`（達到此勝率期望值 = 0，越低越安全） |
| 單筆期望值（EV） | `p × (TP - cost) - (1-p) × (SL + cost)` |
| 月期望報酬（700股） | `EV × 700 × 進場均價 × 每月平均訊號數`（約 2.2 筆） |
| 最大連敗次數 | 歷史回放中最長連續虧損交易筆數 |
| 最大回撤（MDD） | 以每筆 700 股計算的最大累計虧損金額 |
| 平均持倉天數 | 越短越符合短打精神（目標 ≤ 7 交易日） |

### 4.3 雙層篩選（選出「最佳」）

**第一層：風控門檻（必須同時通過）**

- 月期望報酬（700 股）≥ 1,000 元
- 最大連敗次數 ≤ 5 次（可接受）
- 平均持倉天數 ≤ 7 個交易日

**第二層：在通過第一層的 TP/SL 組合中，找最大的：**

```python
# 目標函數：Calmar Ratio 近似值（期望值 / 最大回撤）
score = monthly_ev / max_drawdown_twd
```


---

## 五、輸出：TP/SL 熱力表

計算完成後，建議輸出成「期望值熱力圖」：

- X 軸：TP（2%~12%）
- Y 軸：SL（1%~7%）
- 格子顏色：EV 大小（越深越高）
- 標記：通過「月期望 ≥ 1000 + 最大連敗 ≤ 5」雙門檻的格子

---

## 六、待確認的三個回放設定

在執行以上計算前，Owner 需確認：

1. **回放期間**
    - [ ] 使用「反分割後到最新」這段（較短但價格連續）
    - [ ] 使用更長期間並對價格做調整（較完整但需處理斷點）
2. **進場價格基準**
    - [ ] 訊號當日收盤價買入（較符合 App 實務）
    - [ ] 訊號隔日開盤價買入（較保守）
3. **同日同時觸發停利與停損（同一根 K 棒）**
    - [ ] 保守假設：先觸發停損（**建議選此項**）
    - [ ] 中性假設：算停利（樂觀偏誤）

> Owner 確認後，即可用此方法論跑出一份「TP/SL 最佳化報告」，並將結果更新至 `strategy.md`。

---

## 七、參考公式速查

損益兩平勝率：
$p_{BE} = \frac{SL + \text{cost}}{TP + SL}$

單筆期望值（以投入金額為基準）：
$EV = p \times (TP - \text{cost}) - (1-p) \times (SL + \text{cost})$

月期望報酬（N 股 / M 筆訊號）：
$E[\text{月獲利}] = EV \times N \times \text{均價} \times M$

Kelly 最佳下注比例（不建議直接用，僅供參考上限）：
$f^* = \frac{p}{SL + \text{cost}} - \frac{1-p}{TP - \text{cost}}$

---

*最後更新：2026-02-19 | 策略作者：Owner | 計算框架：ETF Strategy Analyst*


