# DCI Dual Currency Investment Contract Record

## Basic Information

| Field | Value |
| :-- | :-- |
| Product Type | DCI Dual Currency Investment（70%保值型） |
| Trade ID | X20260819F1022 |
| Currency Pair | JPY / USD |
| Trade Date | 2026/8/19 |
| Effective Date | 2026/8/19 |
| Principal | JPY 2,000,000 |
| Linked Asset | USD/JPY Exchange Rate |
| Spot Price | 159.3300 |
| Fixing Date | 2026/8/26 |
| Fixing Price Basis | USD/JPY closing rate on Fixing Date |
| Fixing Result / Fixing Price | —（尚未比價） |
| Maturity Date | 2026/8/28 |
| Strike Price K1 | 159.3 |
| Strike Price K2 | 111.51 (= K1 × 70% = 159.3 × 70%) |
| Yield (Annual Rate) | 7.80% |
| Investment Days | 9 days（生效日 8/19 → 到期日 8/28） |
| Day Count Convention | 360 days/year |


***

## Interest Calculation

```
Interest = Principal x Annual Rate x (Days / 360)
         = 2,000,000 x 7.80% x (9/360)
         = 3,900 JPY (pre-tax)
```


***

## Scenarios at Fixing Date

### Scenario 1: Fixing Price >= K1 (159.3)

- Currency stays in JPY
- Pre-tax redemption:

```
2,000,000 + 3,900 = 2,003,900 JPY
```


***

### Scenario 2: K2 (111.51) <= Fixing Price < K1 (159.3)

- Principal + Interest converted to USD at Strike K1
- Pre-tax redemption:

```
(2,000,000 + 3,900) / 159.3 = 12,579.4099... → 12,579.41 USD
```


***

### Scenario 3: Fixing Price < K2 (111.51)

- Only 70% of principal returned, plus interest, in JPY
- Pre-tax redemption:

```
2,000,000 x 70% + 3,900 = 1,400,000 + 3,900 = 1,403,900 JPY
```


***

## Scenario Summary

| Scenario | Condition | Currency | Pre-tax Amount |
| :--: | :-- | :--: | :--: |
| 1 | Fixing Price >= 159.3 (K1) | JPY | 2,003,900 JPY |
| 2 | 111.51 (K2) <= Fixing Price < 159.3 (K1) | USD | 12,579.41 USD |
| 3 | Fixing Price < 111.51 (K2) | JPY | 1,403,900 JPY |


***

*This record is for reference only. Actual amounts are subject to official bank statements. All figures are pre-tax. Interest is exact (no rounding); USD amount rounded half-up to 2 decimal places.*
