# DCI Dual Currency Investment Contract Record

## Basic Information

| Field | Value                               |
| :-- |:------------------------------------|
| Product Type | DCI Dual Currency Investment        |
| Currency Pair | JPY / USD                           |
| Trade Date | 2026/4/09                           |
| Principal | JPY 1,600,000                       |
| Linked Asset | USD/JPY Exchange Rate               |
| Fixing Date | 2026/4/16                           |
| Fixing Price Basis | USD/JPY closing rate on Fixing Date |
| Maturity Date | 2026/4/20                           |
| Strike Price K1 | 158.4                               |
| Strike Price K2 | 110.88 (= K1 × 70% = 158.4 × 70%)   |
| Yield (Annual Rate) | 4.88%                               |
| Investment Days | 11 days                             |
| Day Count Convention | 360 days/year                       |


***

## Interest Calculation

```
Interest = Principal x Annual Rate x (Days / 360)
         = 1,600,000 x 4.88% x (11/360)
         = 2,386 JPY (pre-tax)
```


***

## Scenarios at Fixing Date

### Scenario 1: Fixing Price >= K1 (158.4)

- Currency stays in JPY
- Pre-tax redemption:

```
1,600,000 + 2,386 = 1,602,386 JPY
```


***

### Scenario 2: K2 (110.88) <= Fixing Price < K1 (158.4)

- Principal + Interest converted to USD at Strike K1
- Pre-tax redemption:

```
(1,600,000 + 2,386) / 158.4 = 10116.073... -> 10116.07 USD
```


***

### Scenario 3: Fixing Price < K2 (110.88)

- Only 70% of principal returned, plus interest, in JPY
- Pre-tax redemption:

```
1,600,000 x 70% + 4,938 = 1,1200,000 + 2,386 = 1,1223,86 JPY
```


***

## Scenario Summary

| Scenario | Condition                                | Currency | Pre-tax Amount |
| :--: |:-----------------------------------------| :--: |:--------------:|
| 1 | Fixing Price >= 158.4 (K1)               | JPY | 1,602,386 JPY  |
| 2 | 110.88 (K2) <= Fixing Price < 158.4 (K1) | USD | 10,116.07 USD  |
| 3 | Fixing Price < 110.88 (K2)               | JPY | 1,1223,86 JPY  |


***

*This record is for reference only. Actual amounts are subject to official bank statements. All figures are pre-tax.*

