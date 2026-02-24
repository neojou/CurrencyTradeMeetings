# Strategy Analyst : 在這設定下, 因為春鬥大概三月底, 若 00706L 留倉 30 天, 這邊的 Kelly EV 值會有變化嗎?

在「持有拉長到 30 天、撐到春鬥」這個新設定下，**Kelly EV 的方向（正 or 負）不會因為時間變長自動改變，但「有效 EV」會被槓桿 ETF 的時間衰減打折，實際能拿到的期望報酬會下降。**[^1][^2]

***

## 1. Kelly 理論上：持有天數不會改變「數學 EV」

在純理論的 Kelly 模型裡，只要：

- 同一筆交易的勝率 $p$、
- 多頭賺幅（例如 +5% 或 +8%）、
- 停損虧幅（約 −4.7%）

這三個條件不變，無論你持有 7 天、30 天或 60 天，**這一筆「賭局」的 EV 公式本身是相同的**，也就是你之前算出的：

- 標準停利（+5%）時，EV 約 **+2.57%**
- 春鬥停利（+8%）時，EV 約 **+3.92%**

這個是「路徑 A/B/C + 各自機率」那個 EV，算的是整筆部位從開倉到平倉的期望報酬，不直接看持有幾天。

***

## 2. 但對 2 倍 ETF 來說，拉長到 30 天會有「時間衰減」

關鍵是：00706L 是**日內重置的 2 倍槓桿 ETF**，30 天意味著：

- 每天都做一次槓桿重置
- 每天都在承受「**波動拖累（volatility decay）**」[^2][^1]
- 同一個最終匯率結果，**走一條很抖的路** vs **走一條平滑的趨勢線**，00706L 的最終報酬會不一樣（路徑依賴）

實證與機構研究的共識：

- 槓桿 ETF 的負面時間效應，與：
    - 槓桿倍數（這裡是 2x）
    - 標的波動度（USD/JPY 若這段時間「先大漲再大跌再大漲」，拖累會變大）
成正比。[^3][^1][^2]
- 持有期越長，「實際報酬」越容易低於你直覺想像的 2 倍結果。[^4][^1]

用白話講：

> 同樣是「USD/JPY 最後從 158 掉到 154」，
> 如果是 3 天內一路滑下去，00706L 表現接近你算的 +5～8%。
> 如果是 30 天內「158→160→157→159→155→157→154」這樣亂震，
> 00706L 的最終報酬會比直覺的 2 倍小一截，甚至被波動吃掉一些。

所以：

- **數學上那個 EV（+2.57% / +3.92%）是「理想無衰減」版本**
- 實務上持有 30 天，得打個折扣（看這 30 天實際的波動度）

***

## 3. 在你這單的具體情境下，EV 大致會怎麼變？

你的情境有兩個特別點，會「減少」長期衰減的毒性：

1. **是單一來回的中線賭局，不是長年反覆進出同一檔 2x ETF**
2. 停損設在 USD/JPY 160（約 −4.7%），**不是無限死守**，所以「很長時間又大震盪」的最壞情況，會被截斷

合理的保守處理方式可以這樣看：

- 保留原本 EV **方向與大致量級是對的（正 EV）**
- 但預期報酬要打折，例如：
    - 標準版本 +2.57% → 實務「有效 EV」可能變成 **+1.5～2% 左右**
    - 春鬥版本 +3.92% → 實務可能變成 **+2.3～3% 左右**

這個折扣是拿來補：

- 每日 2x 重置的複利拖累
- 一段時間內 USD/JPY 上上下下造成的路徑依賴差異[^1][^2][^3]

**重點：EV 還是正的，只是「沒有理論算的那麼漂亮」。**

***

## 4. 拉到 30 天，最大差別在「風控」，不是 EV 公式本身

綜合：

- 春鬥 + BoJ 升息預期，使得 **1–2 個月內日圓升值的中線勝率提高**[^5][^6]
- 但 2x 槓桿 + 日內重置，讓「**持有時間越長，對走錯路徑的懲罰越重**」[^2][^1]

所以在 30 天設定下，會建議你多加兩個風險控管點：

1. **中途技術面完全失控時，可考慮不等 160 先下車一次重算 EV**
例如：USD/JPY 長時間盤在 159 附近，但 BoJ / 春鬥的升值故事遲遲沒發生。
2. **春鬥結果落地後，要「重算一次新的 Kelly EV」**
    - 如果春鬥很強（加薪 > 市場預期），新的 EV 可能更好，甚至可以延長持有或移高停利
    - 如果春鬥失望，P(日圓升) 會大幅下修，EV 可能轉負，應比原計畫更早出場

***

## 5. 直接回答你的問題

在現在這個設定下：

- **持有時間拉長到 30 天，本質上不會讓這筆交易從「正 EV」變成「負 EV」**
（前提是你對「春鬥前後 1–2 個月日圓大致會升」的信念沒有變）
- 但因為 00706L 是 2 倍日內重置 ETF，**實際拿到的報酬會被波動與時間衰減吃掉一部分**，[^1][^2]
所以可以把原本計出的 +2.57% / +3.92%，心裡先折掉大約三分之一左右，當作更保守的 EV 估值。

如果你要一句話的結論：

> **Kelly EV 方向仍然是正的，但 30 天持有會讓「實際有效 EV」比理論值低，算帳時要自己心裡再打折，不要照 3～4% 當真，以 1.5～3% 的區間來看會比較保守。**
<span style="display:none">[^10][^7][^8][^9]</span>

<div align="center">⁂</div>

[^1]: https://www.barchart.com/story/news/35838311/daily-reset-long-term-risk-why-leveraged-etfs-arent-built-for-buy-and-hold

[^2]: https://www.ainvest.com/news/leveraged-etf-decay-dynamics-navigating-risk-adjusted-returns-holding-period-sensitivity-2510/

[^3]: https://securitiesexamsmastery.ca/dfol/17-derivative-based-etfs/risks-daily-resets-leveraged-etfs-path-dependency/

[^4]: https://keystocks.com/investment-strategy/daily-reset-long-term-risk-why-leveraged-etfs-arent-built-for-buy-and-hold/

[^5]: https://www.reuters.com/world/asia-pacific/boj-may-raise-rates-march-if-yen-resumes-slide-says-ex-policymaker-2026-02-23/

[^6]: https://investinglive.com/centralbank/boj-could-hike-in-march-if-yen-weakens-says-ex-policymaker-sakurai-20260223/

[^7]: https://www.equiti.com/sc-en/news/market-insights/the-boj-hits-the-brakes-on-rate-hikes/

[^8]: https://leverageshares.com/us/insights/leveraged-etfs-explained-how-they-work-risks-and-benefits/

[^9]: https://www.nomuraholdings.com/doc/en/investor/presentation/2026_0108_prem.pdf

[^10]: https://www.cbre.co.jp/en/insights/reports/japan-market-outlook-2026

