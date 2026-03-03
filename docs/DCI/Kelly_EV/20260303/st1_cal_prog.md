import math

def N(x):
    return 0.5 * (1 + math.erf(x / math.sqrt(2)))

# ================================================================
# 基本設定
# ================================================================
S0 = 157.4          # 今日 USD/JPY
principal = 2_000_000  # JPY
T_days = 9
T = T_days / 365
sigma = 0.09        # 隱含波動率

# 路徑B損失假設：觸發後日圓升至何處
# 條件期望值 E[S | S < K1]
def E_S_given_trigger(S0, K1, sigma, T):
    d1 = (math.log(S0/K1) + (sigma**2/2)*T) / (sigma*math.sqrt(T))
    d2 = d1 - sigma*math.sqrt(T)
    return S0 * (N(-d1) / N(-d2))

def calc_pA(S0, K1, sigma, T):
    d2 = (math.log(S0/K1) + (sigma**2/2)*T) / (sigma*math.sqrt(T)) - sigma*math.sqrt(T)
    return N(d2)

# Owner 提供的履約價 - 利率對照表
options = [
    (157.0, 11.34),
    (156.5,  7.39),
    (156.0,  4.51),
    (155.5,  2.36),
]

# 主觀機率設定
# JPY Bull 修正版：P(USD維持) = 50-55%
# 但今日 S0=157.4，比昨日 157.0 更高
# USD Bull：P(不觸發) 對應各K1，主觀上修
# 用兩組機率計算：
# (A) 風險中性（銀行模型）
# (B) USD Bull 主觀（P(不觸發) += 上移修正）
# (C) JPY Bull 主觀（P(不觸發) -= 下移修正）

# 主觀修正邏輯（S0=157.4）
# USD Bull：上修 +12 pp（關稅、利差、技術面）
# JPY Bull：下修 -5 pp（BoJ、春鬥、干預風險）
# 中性：用風險中性

print("=" * 78)
print(f"DCI Kelly EV 分析（S0={S0}，本金={principal:,} JPY，投資{T_days}天）")
print("=" * 78)

print(f"\n{'履約價':>8} {'緩衝':>7} {'年利率':>8} {'9天利息':>10} {'r_win':>8}")
print("  " + "─"*50)
for K1, rate in options:
    buf = S0 - K1
    interest = principal * (rate/100) * (T_days/360)
    r_win = interest / principal * 100
    print(f"  K={K1:>5} {buf:>6.1f}円 {rate:>7.2f}% {interest:>9.0f}JPY {r_win:>7.4f}%")

# ================================================================
# 完整計算：各履約價 × 三種機率假設
# ================================================================
print(f"\n{'='*78}")
print("完整 Kelly EV 計算")
print(f"{'='*78}")

# 損失假設：觸發後日圓升至的條件期望值
loss_scenarios = {
    "保守（S_end=153）": 153.0,
    "中性（S_end=154）": 154.0,
    "樂觀（S_end=155）": 155.0,
}

for K1, rate in options:
    buf = S0 - K1
    interest = principal * (rate/100) * (T_days/360)
    r_win = interest / principal * 100

    # 風險中性 P(不觸發)
    pA_neutral = calc_pA(S0, K1, sigma, T)
    # USD Bull 主觀 P（上修）
    pA_usdbull = min(pA_neutral + 0.12, 0.97)
    # JPY Bull 主觀 P（下修）
    pA_jpybull = max(pA_neutral - 0.05, 0.01)

    # 條件期望值（觸發後均值）
    E_S_trig = E_S_given_trigger(S0, K1, sigma, T)

    usd_if_trigger = (principal + interest) / K1
    
    print(f"\n【K1 = {K1}，年利率 {rate}%，緩衝 {buf:.1f}円】")
    print(f"  9天利息 = {interest:,.0f} JPY（r_win = +{r_win:.4f}%）")
    print(f"  觸發後換得 USD = {usd_if_trigger:,.2f} USD")
    print(f"  觸發後條件期望匯率 E[S|觸發] = {E_S_trig:.3f}")
    print(f"\n  {'機率假設':>12} {'P(不觸發)':>12} {'P(觸發)':>10}", end="")
    for s_label in loss_scenarios:
        print(f" {'EV('+s_label[-5:-1]+')':>12}", end="")
    print(f" {'Kelly f*':>10}")
    print("  " + "─"*90)

    for p_label, pA in [("風險中性", pA_neutral), 
                         ("USD Bull主觀", pA_usdbull), 
                         ("JPY Bull主觀", pA_jpybull)]:
        pTrig = 1 - pA
        line = f"  {p_label:>12} {pA*100:>11.2f}% {pTrig*100:>9.2f}%"
        
        evs = []
        for s_label, S_end in loss_scenarios.items():
            jpy_back = usd_if_trigger * S_end
            r_lose = (jpy_back - principal) / principal * 100
            ev = pA * r_win + pTrig * r_lose
            evs.append(ev)
            ok = "✅" if ev > 0 else "❌"
            line += f" {ev:>+11.4f}%{ok}"
        
        # Kelly f*（用中性損失）
        S_end_mid = 154.0
        jpy_back_mid = usd_if_trigger * S_end_mid
        r_lose_mid = (jpy_back_mid - principal) / principal * 100
        b = r_win / abs(r_lose_mid)
        kelly = pA * b - (1-pA)
        kelly_pct = kelly / b * 100 if b > 0 else 0
        line += f" {kelly_pct:>+9.2f}%"
        print(line)

# ================================================================
# EV 翻正門檻分析
# ================================================================
print(f"\n{'='*78}")
print("EV 翻正所需主觀 P(不觸發) 門檻（S_end=154 假設）")
print(f"{'='*78}")
print(f"\n  {'K1':>6} {'利率':>7} {'緩衝':>7} {'p_be門檻':>12} {'風險中性':>12} {'USD Bull':>12} {'JPY Bull':>12}")
print("  " + "─"*72)

S_end_base = 154.0
for K1, rate in options:
    buf = S0 - K1
    interest = principal * (rate/100) * (T_days/360)
    r_win = interest / principal * 100
    usd_trig = (principal + interest) / K1
    jpy_back = usd_trig * S_end_base
    r_lose = (jpy_back - principal) / principal * 100
    
    p_be = abs(r_lose) / (r_win + abs(r_lose))
    
    pA_n = calc_pA(S0, K1, sigma, T)
    pA_u = min(pA_n + 0.12, 0.97)
    pA_j = max(pA_n - 0.05, 0.01)
    
    def mark(p, p_be):
        if p >= p_be:
            return f"{p*100:.2f}%✅"
        else:
            diff = (p_be - p)*100
            return f"{p*100:.2f}%❌({diff:.1f}pp不足)"
    
    print(f"  {K1:>6} {rate:>6.2f}% {buf:>6.1f}円 {p_be*100:>11.2f}% "
          f"{mark(pA_n, p_be):>20} {mark(pA_u, p_be):>20} {mark(pA_j, p_be):>20}")

# ================================================================
# 最終建議
# ================================================================
print(f"\n{'='*78}")
print("最終建議（S0=157.4，主觀機率 USD Bull P=+12pp / JPY Bull P=-5pp）")
print(f"{'='*78}")

print(f"""
S_end 假設說明：
  保守（153）：日圓急升 4.4円，如 2024/10 干預幅度
  中性（154）：日圓升 3.4円，歷史均值
  樂觀（155）：日圓只升 2.4円，輕微回調

結論摘要：
""")

recommendations = []
for K1, rate in options:
    buf = S0 - K1
    interest = principal * (rate/100) * (T_days/360)
    r_win = interest / principal * 100
    usd_trig = (principal + interest) / K1
    
    pA_n = calc_pA(S0, K1, sigma, T)
    pA_u = min(pA_n + 0.12, 0.97)
    pA_j = max(pA_n - 0.05, 0.01)
    
    # 中性損失
    for S_end, slabel in [(153, "保守"), (154, "中性"), (155, "樂觀")]:
        jpy_b = usd_trig * S_end
        rl = (jpy_b - principal) / principal * 100
        ev_u = pA_u * r_win + (1-pA_u) * rl
        ev_j = pA_j * r_win + (1-pA_j) * rl
    
    # 用中性假設
    S_end = 154.0
    jpy_b = usd_trig * S_end
    rl = (jpy_b - principal) / principal * 100
    ev_u = pA_u * r_win + (1-pA_u) * rl
    ev_j = pA_j * r_win + (1-pA_j) * rl
    ev_n = pA_n * r_win + (1-pA_n) * rl
    
    b = r_win / abs(rl)
    kelly_u = max((pA_u * b - (1-pA_u)) / b * 100, 0)
    
    if ev_u > 0 and ev_j > 0:
        verdict = "✅✅ 強烈建議（兩方均正）"
    elif ev_u > 0 and ev_j <= 0:
        verdict = "⚠️ 條件建議（USD Bull正，JPY Bull負）"
    else:
        verdict = "❌ 不建議（兩方均負）"
    
    print(f"  K={K1}（{rate}%，緩衝{buf:.1f}円）：")
    print(f"    EV：中性={ev_n:+.4f}% | USD Bull={ev_u:+.4f}% | JPY Bull={ev_j:+.4f}%")
    print(f"    Kelly f*（USD Bull）= {kelly_u:.1f}%")
    print(f"    {verdict}")
    print()

