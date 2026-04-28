# App Architecture: Vedic Multiplication Engine

## 1. Input Logic (MainActivity)
The app first checks the size of the multiplier:
- **Single Digits (2-9):** Handled locally via `CalculationUtils.kt`.
- **Double Digits (11-99):** Launched via `Intent` to `Fractenberg.kt`.

## 2. The Analysis Engine (Fractenberg.kt)
Before calculating, the `analyzeTwoDigitNumber` function runs:
- **Step A:** Checks for base ratios (e.g., 24 is 1:2).
- **Step B (The Clutch):** If no ratio is found, it performs a "carry shift" ($a-1, b+10$).
- **Step C:** If still no ratio, it defaults to standard Vedic digit-by-digit multiplication.

## 3. The Calculation Loop
Depending on if the ratio is **Ascending** ($Left < Right$) or **Descending** ($Left > Right$), the program determines whether to calculate the "Prefix" or "Suffix" first to maintain mathematical flow.