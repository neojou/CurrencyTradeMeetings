# DCI Dual Currency Investment Contract Record

## Basic Information

| Field | Value                               |
| :-- |:------------------------------------|
| Product Type | DCI Dual Currency Investment        |
| Currency Pair | JPY / USD                           |
| Trade Date | 2026/3/05                           |
| Principal | JPY 1,600,000                       |
| Linked Asset | USD/JPY Exchange Rate               |
| Fixing Date | 2026/3/12                           |
| Fixing Price Basis | USD/JPY closing rate on Fixing Date |
| Maturity Date | 2026/3/16                           |
| Strike Price K1 | 156.4                               |
| Strike Price K2 | 108.29 (= K1 × 70% = 154.7 × 70%)   |
| Yield (Annual Rate) | 10.10%                              |
| Investment Days | 11 days                             |
| Day Count Convention | 360 days/year                       |


***

## Interest Calculation

```
Interest = Principal x Annual Rate x (Days / 360)
         = 1,600,000 x 10.10% x (11/360)
         = 4,938 JPY (pre-tax)
```


***

## Scenarios at Fixing Date

### Scenario 1: Fixing Price >= K1 (156.4)

- Currency stays in JPY
- Pre-tax redemption:

```
1,600,000 + 4,938 = 1,604,938 JPY
```


***

### Scenario 2: K2 (109.48) <= Fixing Price < K1 (156.4)

- Principal + Interest converted to USD at Strike K1
- Pre-tax redemption:

```
(1,600,000 + 4,938) / 156.4 = 10261.752... -> 10261.75 USD
```


***

### Scenario 3: Fixing Price < K2 (109.48)

- Only 70% of principal returned, plus interest, in JPY
- Pre-tax redemption:

```
1,600,000 x 70% + 4,938 = 1,1200,000 + 4,938 = 1,124,938 JPY
```


***

## Scenario Summary

| Scenario | Condition                                | Currency | Pre-tax Amount |
| :--: |:-----------------------------------------| :--: |:--------------:|
| 1 | Fixing Price >= 156.4 (K1)               | JPY | 1,604,938 JPY  |
| 2 | 109.48 (K2) <= Fixing Price < 156.4 (K1) | USD | 10,261.75 USD  |
| 3 | Fixing Price < 109.48 (K2)               | JPY | 1,124,938 JPY  |


***

*This record is for reference only. Actual amounts are subject to official bank statements. All figures are pre-tax.*

