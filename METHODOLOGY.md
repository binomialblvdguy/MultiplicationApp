ad# Methodology: The Gear Ratio Theory
### Multiplication as a Mechanical System

In standard arithmetic, multiplication is often treated as a rote memory task. This application reimagines the process as a **Mechanical Gear System**, where numbers are analyzed for their internal "ratios" to find the most efficient path to a solution.

---

## 1. Choosing the "Drive Wheel"
Before any calculation begins, the app scans both numbers to identify which one will act as the **Drive Wheel** (the Multiplier). A Drive Wheel is selected based on how "smooth" its internal ratio is.

* **The Ratio Scan:** The app looks for simple ratios like **1:1, 1:2, 1:3, 1:4, 1:5** or their inverses (e.g., **2:1, 3:1**).
* **Selection Priority:** 1. If the second number (**B**) has a clean ratio, it is chosen as the Drive Wheel.
    2. If only the first number (**A**) has a clean ratio, it becomes the Drive Wheel.
    3. If neither has a clean ratio, the app defaults to a manual digit-by-digit calculation.

> **Example:** In $15 \times 24$, both have ratios ($15$ is $1:5$ and $24$ is $1:2$). The app selects **24** as the Drive Wheel because the $1:2$ ratio is mathematically "smoother" to calculate.

---

## 2. Gear Shifting (The Carry Flip)
Sometimes a number doesn't immediately show its "gear." For example, the number **32** (a $3:2$ ratio) isn't in our list of simple ratios.

The app performs a **Gear Shift** (the "Clutch") by carrying one unit over:
* **Original:** 3 : 2
* **Shifted:** (3 - 1) : (2 + 10) = **2 : 12**
* **Reduced:** 2:12 reduces to a **1:6** ratio, which the engine can then process.

---

## 3. Drive Direction: Ascending vs. Descending
The direction of the calculation depends on the "slope" of the gear ratio.

### Ascending Gears (e.g., 1:2, 1:3)
When the first part of the ratio is smaller than the second, the gear **increases** the output.
1. **Calculate Prefix:** Multiplicand × first ratio part.
2. **Calculate Suffix:** Prefix × ratio factor.

### Descending Gears (e.g., 2:1, 3:1)
When the first part is larger, we start with the smaller component to maintain stability.
1. **Calculate Suffix:** Multiplicand × second ratio part.
2. **Calculate Prefix:** Suffix × ratio value.

---

## 4. Final Assembly (Vedic One-Line)
Once the **Prefix** and **Suffix** "gears" are calculated, they are meshed into the final answer using the Vedic one-line method:
1. **Drop the Units:** Bring down the last digit of the Suffix.
2. **The Carry:** Add the remaining digits of the Suffix to the Prefix.
3. **The Result:** The combined total is your final product.

---
*Documentation for the MultiplicationApp Engine v1.0*ded to the prefix.The suffix stays.The rest of the suffix is carried over and added to the prefix.