# DCI Dual Currency Investment Contract Record

## Basic Information

| Field | Value |
| :-- | :-- |
| Product Type | DCI Dual Currency Investment |
| Currency Pair | JPY / USD |
| Trade Date | 2026/8/06 |
| Effective Date | 2026/8/06 |
| Principal | JPY 2,000,000 |
| Linked Asset | USD/JPY Exchange Rate |
| Fixing Date | 2026/8/13 |
| Fixing Price Basis | USD/JPY closing rate on Fixing Date |
| Fixing Result / Fixing Price | —（尚未比價） |
| Maturity Date | 2026/8/17 |
| Strike Price K1 | 156.5 |
| Strike Price K2 | 109.55 (= K1 × 70% = 156.5 × 70%) |
| Yield (Annual Rate) | 5.28% |
| Investment Days | 11 days（生效日 8/06 → 到期日 8/17） |
| Day Count Convention | 360 days/year |
| Reference Market Value | 99.77%（as of 2026/8/07） |


***

## Interest Calculation

```
Interest = Principal x Annual Rate x (Days / 360)
         = 2,000,000 x 5.28% x (11/360)
         = 3,226.666... → 3,227 JPY (pre-tax, rounded half-up)
```


***

## Scenarios at Fixing Date

### Scenario 1: Fixing Price >= K1 (156.5)

- Currency stays in JPY
- Pre-tax redemption:

```
2,000,000 + 3,227 = 2,003,227 JPY
```


***

### Scenario 2: K2 (109.55) <= Fixing Price < K1 (156.5)

- Principal + Interest converted to USD at Strike K1
- Pre-tax redemption:

```
(2,000,000 + 3,227) / 156.5 = 12,800.1725... → 12,800.17 USD
```


***

### Scenario 3: Fixing Price < K2 (109.55)

- Only 70% of principal returned, plus interest, in JPY
- Pre-tax redemption:

```
2,000,000 x 70% + 3,227 = 1,400,000 + 3,227 = 1,403,227 JPY
```


***

## Scenario Summary

| Scenario | Condition | Currency | Pre-tax Amount |
| :--: | :-- | :--: | :--: |
| 1 | Fixing Price >= 156.5 (K1) | JPY | 2,003,227 JPY |
| 2 | 109.55 (K2) <= Fixing Price < 156.5 (K1) | USD | 12,800.17 USD |
| 3 | Fixing Price < 109.55 (K2) | JPY | 1,403,227 JPY |


***

*This record is for reference only. Actual amounts are subject to official bank statements. All figures are pre-tax. Interest rounded half-up to whole JPY; USD amount rounded half-up to 2 decimal places.*
