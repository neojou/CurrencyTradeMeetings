# DCI Dual Currency Investment Contract Record

## Basic Information

| Field | Value |
| :-- | :-- |
| Product Type | DCI Dual Currency Investment |
| Currency Pair | JPY / USD |
| Trade Date | 2026/2/24 |
| Principal | JPY 2,000,000 |
| Linked Asset | USD/JPY Exchange Rate |
| Fixing Date | 2026/3/3 |
| Fixing Price Basis | USD/JPY closing rate on Fixing Date |
| Maturity Date | 2026/3/5 |
| Strike Price K1 | 154.7 |
| Strike Price K2 | 108.29 (= K1 × 70% = 154.7 × 70%) |
| Yield (Annual Rate) | 9.87% |
| Investment Days | 9 days |
| Day Count Convention | 360 days/year |


***

## Interest Calculation

```
Interest = Principal x Annual Rate x (Days / 360)
         = 2,000,000 x 9.87% x (9/360)
         = 4,935 JPY (pre-tax)
```


***

## Scenarios at Fixing Date

### Scenario 1: Fixing Price >= K1 (154.7)

- Currency stays in JPY
- Pre-tax redemption:

```
2,000,000 + 4,935 = 2,004,935 JPY
```


***

### Scenario 2: K2 (108.29) <= Fixing Price < K1 (154.7)

- Principal + Interest converted to USD at Strike K1
- Pre-tax redemption:

```
(2,000,000 + 4,935) / 154.7 = 12,960.149... -> 12,960.15 USD
```


***

### Scenario 3: Fixing Price < K2 (108.29)

- Only 70% of principal returned, plus interest, in JPY
- Pre-tax redemption:

```
2,000,000 x 70% + 4,935 = 1,400,000 + 4,935 = 1,404,935 JPY
```


***

## Scenario Summary

| Scenario | Condition | Currency | Pre-tax Amount |
| :--: | :-- | :--: | :--: |
| 1 | Fixing Price >= 154.7 (K1) | JPY | 2,004,935 JPY |
| 2 | 108.29 (K2) <= Fixing Price < 154.7 (K1) | USD | 12,960.15 USD |
| 3 | Fixing Price < 108.29 (K2) | JPY | 1,404,935 JPY |


***

*This record is for reference only. Actual amounts are subject to official bank statements. All figures are pre-tax.*

