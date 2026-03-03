## 📊 Rate Reporter ｜ 2026/03/02 晚間覆盤

### (1) 前次待辦

上次會議（2/25）待辦：監控 00706L 浮虧至第5天（3/2），方向不明即出。今日正是第5天節點，將重點標注。

***

### (2) 即時市場數據

**USD/JPY 今日走勢**


| 項目 | 數值 |
| :-- | :-- |
| 最新報價（UTC 08:03） | **157.032** |
| 今日漲跌 | **+0.3865（+0.25%）** |
| 上次收盤 | 156.6455 |
| 今日低點 | 約 156.19（見圖，午夜開盤後觸底） |
| 今日高點 | 約 157.03（UTC 08:03 附近） |
| 近週收盤參考 | 2/27: 155.86，2/26: 156.14，3/2: 157.03↑ [^1] |

走勢特徵：今日亞洲盤午夜開盤後先跌至 ~156.19，隨後持續拉升，UTC 8 時前後觸及今日高點 157.03，呈現**強勢V形反彈、USD 持續強勢**。[^1]

***

**10Y JGB 殖利率**


| 項目 | 數值 |
| :-- | :-- |
| 3/2 最新 | **2.09%**（Trading Economics）[^2] |
| 2/27 前收 | 2.13% [^3] |
| 當日變化 | **−4 bps** |
| 近一週走勢 | 2/24: 2.12% → 2/25: 2.15% → 2/26: 2.17% → 2/27: 2.13% → 3/2: 2.09% |
| 一年前 | 1.40% [^3] |

JGB 殖利率今日下行 −4 bps，短線 BoJ 升息預期略降溫，偏利 DCI（JPY 稍弱）。[^2]

***

**P(路徑A) 即時計算**

依 `strategy.md §9` 決策樹，S0 = 157.032 代入：

$$
d_2 = \frac{\ln(157.032/154.7) + (0.09^2/2)(9/365)}{0.09\sqrt{9/365}} - 0.09\sqrt{9/365} = 1.0516
$$


| 指標 | 數值 |
| :-- | :-- |
| **P(路徑A)** | **85.35%** |
| 門檻 | 71.3% |
| **超門檻** | ✅ **+14.05 pp** |
| P(日圓升) | **14.65%** |
| 比價日 | **明日（3/3）** ⚠️ |

**DCI 合約狀態**：P(路徑A) = 85.35%，明日比價日 USD/JPY 需跌破 154.7 才會觸發，目前距履約價還有 **+2.33 點**緩衝，路徑 A 主導機率極高。

***

**若無 DCI 合約情境**（參考）：
一週後 P(USD/JPY > X) = 70%，X = **156.00**（即匯率需維持在 156.00 以上，才有七成把握）。

***

### (3) 策略切換建議

| 策略 | 狀態 | 說明 |
| :-- | :-- | :-- |
| **策略一 DCI** | ✅ **強力維持** | P(路徑A)=85.35%，比價日明日，路徑A主導 |
| **策略二 00706L** | ❌ **EV 負** | P(日圓升)=14.65%，遠低於52.5%門檻 |


***

### (4) 反饋前述看法

- **前次 P(路徑A)=80.0%（2/25）**：今日繼續升至 85.35%，方向正確，USD 強勢如預期延續。✅
- **上次警示第5天時間停損（3/2）**：今日正是第5天，00706L 持倉 1 張 @ 20.36，需對照現價立即評估。⚠️

***

### (5) 個人新增看法

1. **DCI 明日比價日風險極低**：S0=157.03 距 K1=154.7 有 2.33 點，即使今晚日圓急升，路徑 A 結算機率仍非常高。
2. **00706L 時間停損警示**：P(日圓升)=14.65% 遠低於門檻，今日為第5天節點，除非有重大 JPY 強勢訊號，否則理應出場。

***

> 📌 **Rate Reporter 小結**：USD/JPY = 157.03，P(路徑A) = 85.35%（✅ 超門檻 +14 pp），DCI 明日比價安全；00706L 第5天節點，P(日圓升)僅14.65%，EV 深度為負。等待 News Analyst 補充基本面訊號。
<span style="display:none">[^10][^11][^12][^13][^14][^15][^16][^4][^5][^6][^7][^8][^9]</span>

<div align="center">⁂</div>

[^1]: https://finance.yahoo.com/quote/JPY=X/history/

[^2]: https://tradingeconomics.com/japan/government-bond-yield

[^3]: https://ycharts.com/indicators/japan_10_year_government_bond_interest_rate

[^4]: Jie-Tu-2026-03-02-Xia-Wu-4.04.21.jpg

[^5]: https://wise.com/gb/currency-converter/usd-to-jpy-rate/history

[^6]: https://robinhood.com/us/en/prediction-markets/fx/events/us-dollar-to-japanese-yen-exchange-rate-march-2-2026-mar-02-2026/

[^7]: https://www.poundsterlinglive.com/history/USD-JPY-2026

[^8]: https://alanchand.com/en/exchange-rates/usd-jpy

[^9]: https://tw.stock.yahoo.com/quote/00706L.TW

[^10]: https://www.exchange-rates.org/exchange-rate-history/usd-jpy-2026

[^11]: https://hk.finance.yahoo.com/quote/00706L.TW/history/

[^12]: https://fxds-hcc.oanda.com

[^13]: https://etf.masterlink.com.tw/Details.html?id=00706L

[^14]: https://www.investing.com/rates-bonds/japan-10-year-bond-yield

[^15]: https://www.investing.com/currencies/usd-jpy

[^16]: https://www.yuantaetfs.com/tradeInfo/pcf/00706L

