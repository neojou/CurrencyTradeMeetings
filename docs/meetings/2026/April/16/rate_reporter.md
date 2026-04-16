## 📊 Rate Reporter ｜ 2026/04/16 晚間覆盤


***

### 【前次待辦確認】

上次會議（3/2）待辦：DCI 路徑A結算確認 ✅（今日已確認比價不轉換，本輪 DCI 路徑A成立）

***

### 1. USD/JPY 即時匯率報告

| 項目 | 數值 | 說明 |
| :-- | :-- | :-- |
| **當前匯率（S0）** | **159.0555** | 10:23 UTC（台灣時間 18:23）[^1] |
| 今日漲跌 | **+0.2375（+0.15%）** | [^2] |
| 日內低點 | ~158.22 | 凌晨 4:00 UTC（附圖） |
| 日內高點 | ~159.06+ | 10:23 UTC |
| 前收 | 158.818 | 附圖標示 |
| 近週高點 | **~159.85**（4/13） | [^3][^2] |
| 近週低點 | ~158.22（今日） | 附圖 |

**⚠️ 警戒：USD/JPY = 159.06，處於 158–160 Rate Check 警戒區（§3.6 觸發）**

***

### 2. JGB 市場快照

| 項目 | 數值 |
| :-- | :-- |
| 日本 10Y JGB 殖利率（今日） | **2.40%**（穩定）[^4] |
| 昨日（4/14） | 2.42%（下降 2 bps）[^5] |
| 4/13 | 2.47%（本週高點）[^5] |
| 近月漲幅 | **+14 bps**（過去 4 週 +23.98 bps）[^4] |
| 歷史水位 | 接近 **27 年高位**（1997 年以來最高區域）[^6] |

**解讀**：JGB 殖利率持續走高 → BoJ 升息預期強化 → 理論上利好日圓（有利 00706L），但美日利差仍寬，USD 維持強勢。

***

### 3. DCI 合約狀態

| 項目 | 狀態 |
| :-- | :-- |
| 比價日 | **2026/4/16（今日）✅ 已比價** |
| 結果 | **路徑A：不轉換（USD/JPY ≥ 158.4）** |
| 到期日 | 2026/4/20（週一） |
| 預計返還 | **1,602,386 JPY**（稅前） |
| 新合約機會 | 需另行評估 |


***

### 4. P(路徑A) 計算（strategy.md §9）

**使用參數**：S0 = 159.0555，K1 = 154.7，σ = 0.09，T = 9/365

$d_2 = \frac{\ln(159.0555/154.7) + (0.09^2/2) \times \frac{9}{365}}{0.09\sqrt{\frac{9}{365}}} - 0.09\sqrt{\frac{9}{365}} = 1.9576$


| 指標 | 數值 |
| :-- | :-- |
| **P(路徑A)** | **97.49%** |
| 門檻 71.3% | ✅ 超越 **+26.19 pp** |
| P(日圓升，模型) | **2.51%**（遠低於 52.5%） |

> ⚠️ 但 S0 = 159.06 ≥ 158 → **§3.6 Rate Check 框架適用**（取代標準 P(日圓升) 模型）

***

### 5. §3.6 Rate Check 框架（S0=159.06）

| 情境 | 條件 | 00706L 預估報酬 |
| :-- | :-- | :-- |
| **情境X**（RC觸發，日圓升≥3円） | 財務省介入 | **+3.43%** |
| **情境Y**（RC未觸發，USD續強+1.5円） | 無干預 | **−2.23%** |
| **EV 翻正門檻 P(RC)** | **≥ 39.34%** | — |


***

### 6. 一週後匯率分布推估

> **若無 DCI 合約，一週後 USD/JPY > X 的機率 ≥ 70%，X 約為多少？**

使用中性漂移（μ=0）、σ=0.09、T=7/365，求 30th percentile：

$X = 159.0555 \times e^{-\frac{0.09^2}{2} \times \frac{7}{365} + 0.09\sqrt{\frac{7}{365}} \times (-0.5244)} = \mathbf{158.01}$

**結論：一週後有 70% 機率 USD/JPY 仍高於 ~158.01**
（即：模型顯示短期 USD 維持強勢概率高，日圓大幅升值空間有限）

***

### 7. 00706L 浮損預估

| 項目 | 數值 |
| :-- | :-- |
| 持倉均價 | 19.85 元 |
| 4/13 已知收盤 | ~19.32 元 [^7] |
| 今日估算收盤 | ~**19.47–19.55 元**（基於 USD/JPY 近週走勢估算） |
| **估計浮損%** | 約 **−1.5% ~ −2.0%** |
| 9 張估計總浮損 | 約 **−2,700 ~ −3,600 TWD** |
| 停損門檻（−2.5%） | 對應 00706L ≈ 19.355 元 |

> ⚠️ **持倉天數待確認**：請 Owner 告知 00706L 進場日期，以確認是否觸及時間停損節點（第 5/7 天）

***

### 8. 策略切換訊號

| 策略 | 狀態 |
| :-- | :-- |
| **策略一（DCI）** | P(路徑A)=97.49% ≥ 71.3% ✅，若進新合約 EV 為正（需確認K1/利率） |
| **策略二（00706L）** | 標準模型 P(日圓升)=2.51% ❌；**轉用 §3.6 Rate Check 框架評估** |
| **Rate Check 框架** | S0=159.06 進入 158–160 區間，需 Owner 主觀判斷 P(RC反彈) ≥ 39.34% |
| 離桌線警戒 | USD/JPY 距 160 僅差 **0.94 円**，⚠️ 高度警戒 |


***

### 9. 反饋前述看法

（首位發言，無前述報告需反饋）

***

### 10. 個人新增看法

1. **Rate Check 壓力正在累積**：USD/JPY 在 159 高位盤整，JGB 殖利率創 27 年高，財務省口頭干預風險顯著上升，P(RC反彈) 主觀估計可能已進入 35–45% 區間，§3.6 框架值得認真討論。
2. **00706L 浮損接近關鍵區**：估計浮損約 −1.5%~−2.0%，距標準停損 −2.5% 僅剩 0.5–1.0% 緩衝；加上 USD/JPY 收盤後繼續走強至 159.06，後市須謹慎。**請 Owner 確認進場日期以判斷時間停損節點。**

***

*（等待 News Analyst 報告）*
<span style="display:none">[^10][^11][^12][^13][^14][^15][^16][^8][^9]</span>

<div align="center">⁂</div>

[^1]: https://robinhood.com/us/en/prediction-markets/fx/events/us-dollar-to-japanese-yen-exchange-rate-april-16-2026-apr-16-2026/

[^2]: https://finance.yahoo.com/quote/USDJPY=X/history/

[^3]: https://www.poundsterlinglive.com/history/USD-JPY-2026

[^4]: https://tradingeconomics.com/japan/government-bond-yield

[^5]: https://ycharts.com/indicators/japan_10_year_government_bond_interest_rate

[^6]: https://www.japantimes.co.jp/business/2026/04/07/economy/bonds-27-years/

[^7]: https://www.cnyes.com/twstock/00706L

[^8]: Jie-Tu-2026-04-16-Xia-Wu-6.23.34.jpg

[^9]: https://www.federalreserve.gov/releases/h10/hist/dat00_ja.htm

[^10]: https://www.investing.com/currencies/usd-jpy-historical-data

[^11]: https://www.exchangerates.org.uk/USD-JPY-spot-exchange-rates-history-2026.html

[^12]: https://forex24.pro/usdjpy-forecast/usd-jpy-forecast-japanese-yen-for-april-16-2026/

[^13]: https://tw.stock.yahoo.com/quote/00706L.TW

[^14]: https://www.investing.com/rates-bonds/japan-10-year-bond-yield

[^15]: https://www.wantgoo.com/stock/etf/00706l/discount-premium

[^16]: https://www.exchange-rates.org/exchange-rate-history/usd-jpy-2026

