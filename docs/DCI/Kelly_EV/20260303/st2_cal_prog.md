import math

def N(x):
    return 0.5 * (1 + math.erf(x / math.sqrt(2)))

def calc_d2(S0, K, sigma, T):
    return (math.log(S0/K) + (sigma**2/2)*T) / (sigma*math.sqrt(T)) - sigma*math.sqrt(T)

def calc_d1(S0, K, sigma, T):
    return calc_d2(S0, K, sigma, T) + sigma*math.sqrt(T)

def calc_pA(S0, K, sigma, T):
    return N(calc_d2(S0, K, sigma, T))

def E_S_given_trigger(S0, K, sigma, T):
    """條件期望值 E[S | S < K]"""
    d1 = calc_d1(S0, K, sigma, T)
    d2 = calc_d2(S0, K, sigma, T)
    return S0 * (N(-d1) / N(-d2))

# ================================================================
# 基本設定
# ================================================================
S0 = 157.4
sigma = 0.09
T = 9/365
principal_jpy = 2_000_000

# 利率對照表（第一輪，JPY→USD）
rate_table = {157.0: 11.34, 156.5: 7.39, 156.0: 4.51, 155.5: 2.36}

# ================================================================
# 第二輪利率估算
# 第二輪：USD→JPY，觸發後 S0_R2 = E[S|觸發] < K1
# 第二輪 K2 = K1（相同履約價）
# 第二輪的「緩衝」= K2 - S0_R2（反向，日圓便宜時買回）
# 利率公式：用同樣的利率表插值
# 但第二輪是 USD→JPY，到期若 USD/JPY ≥ K2 則換回JPY（路徑C）
# 
# 利率插值：Owner 提供的表是 S0=157.4 時的報價
# 第二輪 S0_R2 ≈ 154-156，K2=K1
# 緩衝R2 = K2 - S0_R2（因為是日圓升值方向）
# 利率估算：與第一輪同邏輯，緩衝越小利率越高
# 用線性插值：rate(緩衝) = f(緩衝)
# 從第一輪數據：
#   緩衝0.4 → 11.34%, 緩衝0.9 → 7.39%, 緩衝1.4 → 4.51%, 緩衝1.9 → 2.36%
# 擬合：近似指數衰減

# 擬合利率 vs 緩衝關係
buffers_fit = [0.4, 0.9, 1.4, 1.9]
rates_fit   = [11.34, 7.39, 4.51, 2.36]

# 用線性插值（在給定區間內）
def interp_rate(buf):
    """根據緩衝距離插值利率"""
    if buf <= 0.4:
        return 11.34 + (0.4 - buf) * (11.34 - 7.39) / 0.5  # 外插
    elif buf >= 1.9:
        return max(2.36 - (buf - 1.9) * 1.5, 0.5)  # 外插下限0.5%
    else:
        # 線性插值
        for i in range(len(buffers_fit)-1):
            if buffers_fit[i] <= buf <= buffers_fit[i+1]:
                t = (buf - buffers_fit[i]) / (buffers_fit[i+1] - buffers_fit[i])
                return rates_fit[i] + t * (rates_fit[i+1] - rates_fit[i])
    return 2.0

print("=" * 78)
print(f"兩輪 DCI 組合 Kelly EV（情境二）")
print(f"S0={S0}，本金={principal_jpy:,} JPY，各輪9天")
print("=" * 78)

print("\n【利率插值函數驗證（第二輪用）】")
for buf_test in [0.3, 0.5, 0.9, 1.0, 1.4, 1.5, 1.9, 2.0, 2.5]:
    print(f"  緩衝={buf_test:.1f}円 → 估算利率={interp_rate(buf_test):.2f}%")

print(f"\n{'='*78}")
print("各履約價兩輪 DCI 完整計算")
print(f"{'='*78}")

results_all = []

for K1, rate_R1 in sorted(rate_table.items()):
    buf_R1 = S0 - K1
    interest_R1 = principal_jpy * (rate_R1/100) * (9/360)
    r_win_R1 = interest_R1 / principal_jpy * 100

    # 第一輪機率
    pA_R1_neutral = calc_pA(S0, K1, sigma, T)
    pTrig_R1 = 1 - pA_R1_neutral

    # USD Bull / JPY Bull 主觀修正
    pA_R1_usdbull = min(pA_R1_neutral + 0.12, 0.97)
    pA_R1_jpybull = max(pA_R1_neutral - 0.05, 0.01)

    # 觸發後換得 USD
    usd_principal = (principal_jpy + interest_R1) / K1

    # 第二輪：條件期望值 S0_R2 = E[S | S < K1]
    S0_R2 = E_S_given_trigger(S0, K1, sigma, T)
    K2 = K1  # 相同履約價

    # 第二輪緩衝（USD→JPY，反向：K2 > S0_R2）
    buf_R2 = K2 - S0_R2
    rate_R2 = interp_rate(buf_R2)
    interest_R2_usd = usd_principal * (rate_R2/100) * (9/360)
    usd_total = usd_principal + interest_R2_usd
    r_win_R2 = interest_R2_usd / usd_principal * 100

    # 第二輪機率（條件：已觸發，S0_R2 < K2）
    pC_given_trig = calc_pA(S0_R2, K2, sigma, T)  # USD/JPY升回≥K2，換回JPY
    pB_given_trig = 1 - pC_given_trig              # 繼續跌，留USD

    # USD Bull / JPY Bull 主觀修正第二輪
    # 第二輪：日圓已升，第二輪是「USD→JPY」，方向對JPY Bull更有利
    # 但第二輪的 S0_R2 < K2，代表日圓已升，再繼續升的機率取決於情況
    pC_R2_usdbull = min(pC_given_trig + 0.10, 0.95)  # USD Bull認為日圓升夠了，會貶回
    pC_R2_jpybull = max(pC_given_trig - 0.05, 0.05)  # JPY Bull認為日圓持續升

    # 三條路徑損益
    # 路徑A：第一輪不觸發，留JPY+利息
    profit_A = interest_R1
    r_A = profit_A / principal_jpy * 100

    # 路徑C：第一輪觸發→USD，第二輪換回JPY @ K2
    jpy_back_C = usd_total * K2
    profit_C = jpy_back_C - principal_jpy
    r_C = profit_C / principal_jpy * 100

    # 路徑B：兩輪均觸發，持有USD，以不同S_end估算
    results_by_send = {}
    for S_end in [153.0, 154.0, 155.0]:
        jpy_equiv_B = usd_total * S_end
        profit_B = jpy_equiv_B - principal_jpy
        r_B = profit_B / principal_jpy * 100
        results_by_send[S_end] = (profit_B, r_B)

    print(f"\n{'─'*78}")
    print(f"【K1 = {K1}，第一輪利率 {rate_R1}%，緩衝 {buf_R1:.1f}円】")
    print(f"\n  第一輪（JPY→USD）：")
    print(f"    利息={interest_R1:,.0f}JPY，r_win={r_win_R1:.4f}%")
    print(f"    P(A，不觸發) 風險中性={pA_R1_neutral*100:.2f}%")
    print(f"    P(觸發) 風險中性={pTrig_R1*100:.2f}%")
    print(f"    觸發後換得 USD={usd_principal:,.2f}")

    print(f"\n  第二輪（USD→JPY）：")
    print(f"    S0_R2（條件期望值）= {S0_R2:.3f}，K2={K2}，緩衝={buf_R2:.3f}円")
    print(f"    第二輪估算利率={rate_R2:.2f}%，利息={interest_R2_usd:.2f}USD")
    print(f"    USD合計={usd_total:.2f}，r_win_R2={r_win_R2:.4f}%")
    print(f"    P(C，換回JPY) 風險中性={pC_given_trig*100:.2f}%")
    print(f"    P(B，留USD)  風險中性={pB_given_trig*100:.2f}%")
    
    print(f"\n  路徑損益：")
    print(f"    路徑A：+{profit_A:,.0f}JPY（+{r_A:.4f}%）")
    jpy_back_C_val = usd_total * K2
    print(f"    路徑C：USD{usd_total:.2f}×{K2}={jpy_back_C_val:,.0f}JPY → {profit_C:+,.0f}JPY（{r_C:+.4f}%）")
    for S_end, (pB_profit, pB_r) in results_by_send.items():
        print(f"    路徑B（S_end={S_end}）：{pB_profit:+,.0f}JPY（{pB_r:+.4f}%）")

    # EV 計算（三種機率 × 三種S_end）
    print(f"\n  {'─'*72}")
    print(f"  EV 計算（三種機率假設 × 三種損失假設）")
    print(f"  {'─'*72}")
    print(f"  {'機率假設':>14} | {'路徑機率':>28} | {'EV(S=153)':>11} | {'EV(S=154)':>11} | {'EV(S=155)':>11}")
    print(f"  {'':>14} | {'P(A) P(C) P(B)':>28} |")
    print(f"  {'─'*72}")

    for p_label, pA_R1, pC_R2 in [
        ("風險中性",    pA_R1_neutral, pC_given_trig),
        ("USD Bull主觀", pA_R1_usdbull, pC_R2_usdbull),
        ("JPY Bull主觀", pA_R1_jpybull, pC_R2_jpybull),
    ]:
        pTrig = 1 - pA_R1
        pC_total = pTrig * pC_R2
        pB_total = pTrig * (1 - pC_R2)

        evs = []
        for S_end, (pB_profit, pB_r) in results_by_send.items():
            ev = pA_R1 * r_A + pC_total * r_C + pB_total * pB_r
            evs.append(ev)
            ok = "✅" if ev > 0 else "❌"

        prob_str = f"P(A)={pA_R1*100:.1f}% P(C)={pC_total*100:.1f}% P(B)={pB_total*100:.1f}%"
        ev_strs = " | ".join([f"{e:>+10.4f}%{'✅' if e>0 else '❌'}" for e in evs])
        print(f"  {p_label:>14} | {prob_str:>28} | {ev_strs}")

    # 與單輪比較
    print(f"\n  【vs 單輪 EV（S_end=154，風險中性）】")
    S_end_mid = 154.0
    usd_single = (principal_jpy + interest_R1) / K1
    jpy_single = usd_single * S_end_mid
    r_lose_single = (jpy_single - principal_jpy) / principal_jpy * 100
    ev_single_neutral = pA_R1_neutral * r_win_R1 + pTrig_R1 * r_lose_single
    
    pC_t = pTrig_R1 * pC_given_trig
    pB_t = pTrig_R1 * pB_given_trig
    pB_profit_mid, pB_r_mid = results_by_send[154.0]
    ev_double_neutral = pA_R1_neutral * r_A + pC_t * r_C + pB_t * pB_r_mid

    improvement = ev_double_neutral - ev_single_neutral
    print(f"    單輪EV={ev_single_neutral:+.4f}% | 兩輪EV={ev_double_neutral:+.4f}% | 改善={improvement:+.4f}pp")

    results_all.append({
        'K1': K1, 'rate_R1': rate_R1, 'buf_R1': buf_R1,
        'S0_R2': S0_R2, 'buf_R2': buf_R2, 'rate_R2': rate_R2,
        'pA_neutral': pA_R1_neutral, 'pA_usdbull': pA_R1_usdbull,
        'ev_single_neutral': ev_single_neutral,
        'ev_double_neutral': ev_double_neutral,
        'improvement': improvement,
        'pC_total_neutral': pTrig_R1 * pC_given_trig,
        'pB_total_neutral': pTrig_R1 * pB_given_trig,
        'r_A': r_A, 'r_C': r_C,
    })

# ================================================================
# 總結比較表
# ================================================================
print(f"\n{'='*78}")
print("總結比較表（S_end=154 中性假設）")
print(f"{'='*78}")
print(f"\n  {'K1':>6} {'R1利率':>8} {'R2利率':>8} {'P(A)':>8} {'P(C)':>8} {'P(B)':>8} {'單輪EV':>10} {'兩輪EV':>10} {'改善':>9} {'結論':>6}")
print("  " + "─"*86)

for r in results_all:
    ok_s = "✅" if r['ev_single_neutral'] > 0 else "❌"
    ok_d = "✅" if r['ev_double_neutral'] > 0 else "❌"
    print(f"  {r['K1']:>6} {r['rate_R1']:>7.2f}% {r['rate_R2']:>7.2f}% "
          f"{r['pA_neutral']*100:>7.1f}% {r['pC_total_neutral']*100:>7.1f}% {r['pB_total_neutral']*100:>7.1f}% "
          f"{r['ev_single_neutral']:>+9.4f}%{ok_s} {r['ev_double_neutral']:>+9.4f}%{ok_d} "
          f"{r['improvement']:>+8.4f}pp")

print(f"\n  ⭐ 兩輪比單輪平均改善：{sum(r['improvement'] for r in results_all)/len(results_all):+.4f}pp")
print(f"\n  EV翻正需要（S_end=154，風險中性）：")
for r in results_all:
    pbe = abs(r['ev_single_neutral']) / (abs(r['ev_single_neutral']) + r['r_A'])
    print(f"    K={r['K1']}：P(不觸發) 需達 {(r['pA_neutral']+(-r['ev_double_neutral']/(r['r_A']-(-1.35))))*100:.1f}%，目前風險中性={r['pA_neutral']*100:.1f}%，USD Bull={r['pA_usdbull']*100:.1f}%")

