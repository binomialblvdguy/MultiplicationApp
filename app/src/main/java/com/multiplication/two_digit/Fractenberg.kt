package com.multiplication.two_digit

data class VedicResult(
    val finalAnswer: Long,
    val methodLabel: String,
    val vedicLine: String,
    val gradeSchoolLines: List<String>,
    val note: String = ""
)

enum class SolveMode {
    AUTO,
    STANDARD
}

object VedicLogic {

    // Normalized digit ratios the app will treat as shortcut-friendly.
    // This covers:
    // 11..19  -> 1:1 through 1:9
    // 21..91  -> also normalize to 1:2 through 1:9
    // plus a few other easy pairs like 23, 34, 45, etc.
    private val supportedRatios = setOf(
        "1:1",
        "1:2", "1:3", "1:4", "1:5", "1:6", "1:7", "1:8", "1:9",
        "2:3", "3:4", "4:5"
    )

    fun calculate(n1: Long, n2: Long, mode: SolveMode = SolveMode.AUTO): VedicResult {
        if (n1 == 0L || n2 == 0L) {
            return VedicResult(
                finalAnswer = 0L,
                methodLabel = "ZERO",
                vedicLine = "0 = 0",
                gradeSchoolLines = listOf("0", "-", "0"),
                note = "Anything times 0 is 0."
            )
        }

        val actual = n1 * n2

        return when (mode) {
            SolveMode.STANDARD -> solveVerticalCrosswise(
                base = n1,
                multiplier = n2,
                actual = actual,
                choiceNote = "Standard selected. Using Vertical & Crosswise."
            )

            SolveMode.AUTO -> {
                val choice = chooseNumbers(n1, n2)

                if (isShortcutFriendly(choice.multiplier)) {
                    solveShortcut(
                        multiplier = choice.multiplier,
                        base = choice.multiplicand,
                        actual = actual,
                        choiceNote = choice.note
                    )
                } else {
                    solveVerticalCrosswise(
                        base = n1,
                        multiplier = n2,
                        actual = actual,
                        choiceNote = "Auto found no simple shortcut. Using Vertical & Crosswise."
                    )
                }
            }
        }
    }

    private data class NumberChoice(
        val multiplicand: Long,
        val multiplier: Long,
        val note: String
    )

    private fun chooseNumbers(n1: Long, n2: Long): NumberChoice {
        val score1 = score(n1)
        val score2 = score(n2)

        return when {
            score1 > score2 -> NumberChoice(
                multiplicand = n2,
                multiplier = n1,
                note = "Auto picked First number for the shortcut."
            )

            score2 > score1 -> NumberChoice(
                multiplicand = n1,
                multiplier = n2,
                note = "Auto picked Second number for the shortcut."
            )

            n1 <= n2 -> NumberChoice(
                multiplicand = n2,
                multiplier = n1,
                note = "Both work. Auto picked the smaller number."
            )

            else -> NumberChoice(
                multiplicand = n1,
                multiplier = n2,
                note = "Both work. Auto picked the smaller number."
            )
        }
    }

    private fun score(n: Long): Int {
        return when {
            isShortcutFriendly(n) -> 100
            else -> 50 - digitSum(n).coerceAtMost(30)
        }
    }

    private fun isShortcutFriendly(n: Long): Boolean {
        val ratioKey = normalizedDigitRatio(n)
        return ratioKey != null && ratioKey in supportedRatios
    }

    private fun normalizedDigitRatio(n: Long): String? {
        if (n !in 10L..99L) return null

        val tens = (n / 10).toInt()
        val ones = (n % 10).toInt()

        if (tens == 0 || ones == 0) return null

        val small = minOf(tens, ones)
        val large = maxOf(tens, ones)
        val d = gcd(small, large)

        return "${small / d}:${large / d}"
    }

    private fun gcd(a: Int, b: Int): Int {
        var x = a
        var y = b
        while (y != 0) {
            val t = x % y
            x = y
            y = t
        }
        return x
    }

    private fun digitSum(n: Long): Int = n.toString().sumOf { it.digitToInt() }

    private fun solveShortcut(
        multiplier: Long,
        base: Long,
        actual: Long,
        choiceNote: String
    ): VedicResult {
        val tens = multiplier / 10
        val ones = multiplier % 10

        // Keep display order the same as the original number.
        val parts = listOf(tens * base, ones * base).filter { it != 0L }

        val ratioKey = normalizedDigitRatio(multiplier).orEmpty()

        return VedicResult(
            finalAnswer = actual,
            methodLabel = "SHORTCUT RULE",
            vedicLine = formatStandardLine(parts, actual),
            gradeSchoolLines = formatGradeSchool(multiplierRows(base, multiplier), actual),
            note = mergeNotes(choiceNote, "Shortcut ratio recognized: $ratioKey.")
        )
    }

    private fun solveVerticalCrosswise(
        base: Long,
        multiplier: Long,
        actual: Long,
        choiceNote: String
    ): VedicResult {
        return solveVc2x2(base, multiplier, actual, choiceNote)
    }

    private fun solveVc2x2(
        base: Long,
        multiplier: Long,
        actual: Long,
        choiceNote: String
    ): VedicResult {
        val a = base / 10
        val b = base % 10
        val c = multiplier / 10
        val d = multiplier % 10

        val left = a * c
        val middle = (a * d) + (b * c)
        val right = b * d

        val vcParts = listOf(
            left.toString(),
            middle.toString(),
            right.toString().padStart(2, '0')
        )

        val vcRows = listOf(
            left * 100,
            middle * 10,
            right
        )

        return VedicResult(
            finalAnswer = actual,
            methodLabel = "VERTICAL & CROSSWISE",
            vedicLine = formatVcLine(vcParts, actual),
            gradeSchoolLines = formatGradeSchool(vcRows, actual),
            note = mergeNotes(choiceNote, "True V&C for 2 digits by 2 digits.")
        )
    }

    private fun multiplierRows(base: Long, multiplier: Long): List<Long> {
        val tens = multiplier / 10
        val ones = multiplier % 10

        val rows = mutableListOf<Long>()
        if (tens != 0L) rows.add(tens * base * 10)
        if (ones != 0L) rows.add(ones * base)

        return if (rows.isEmpty()) listOf(0L) else rows
    }

    private fun formatStandardLine(parts: List<Long>, actual: Long): String {
        return parts.joinToString(" | ") + " = $actual"
    }

    private fun formatVcLine(parts: List<String>, actual: Long): String {
        return parts.joinToString(" / ") + " = $actual"
    }

    private fun formatGradeSchool(rows: List<Long>, actual: Long): List<String> {
        val width = maxOf(
            actual.toString().length,
            rows.maxOfOrNull { it.toString().length } ?: 1
        )

        val lines = rows.map { it.toString().padStart(width) }.toMutableList()
        lines.add("-".repeat(width))
        lines.add(actual.toString().padStart(width))
        return lines
    }

    private fun mergeNotes(first: String, second: String): String {
        return listOf(first, second)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }
}