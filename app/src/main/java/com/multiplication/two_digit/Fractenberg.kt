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
    USE_NUMBER_1,
    USE_NUMBER_2,
    VERTICAL_CROSSWISE
}

object VedicLogic {

    private val rootPairs = setOf<Long>(101, 204, 309, 416, 525, 636, 749, 864, 981)
    private val powerPairs = setOf<Long>(402, 903, 164, 255, 366, 497, 648, 819)

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
        val choice = chooseNumbers(n1, n2, mode)

        return when (mode) {
            SolveMode.VERTICAL_CROSSWISE -> solveVerticalCrosswise(
                base = choice.multiplicand,
                multiplier = choice.multiplier,
                actual = actual,
                choiceNote = choice.note
            )

            else -> when {
                choice.multiplier in rootPairs || choice.multiplier in powerPairs ->
                    solveGolden(choice.multiplier, choice.multiplicand, actual, choice.note)

                isMidOne(choice.multiplier) ->
                    solveMidOne(choice.multiplier, choice.multiplicand, actual, choice.note)

                isSkeletonRatio(choice.multiplier) ->
                    solveSkeleton(choice.multiplier, choice.multiplicand, actual, choice.note)

                else ->
                    solveStandard(choice.multiplier, choice.multiplicand, actual, choice.note)
            }
        }
    }

    private data class NumberChoice(
        val multiplicand: Long,
        val multiplier: Long,
        val note: String
    )

    private fun chooseNumbers(n1: Long, n2: Long, mode: SolveMode): NumberChoice {
        return when (mode) {
            SolveMode.USE_NUMBER_1 -> NumberChoice(
                multiplicand = n2,
                multiplier = n1,
                note = "Using Number 1 as the split number."
            )

            SolveMode.USE_NUMBER_2 -> NumberChoice(
                multiplicand = n1,
                multiplier = n2,
                note = "Using Number 2 as the split number."
            )

            SolveMode.VERTICAL_CROSSWISE -> {
                if (digitCount(n1) >= digitCount(n2)) {
                    NumberChoice(
                        multiplicand = n1,
                        multiplier = n2,
                        note = "Using the shorter number for V&C."
                    )
                } else {
                    NumberChoice(
                        multiplicand = n2,
                        multiplier = n1,
                        note = "Using the shorter number for V&C."
                    )
                }
            }

            SolveMode.AUTO -> {
                val score1 = score(n1)
                val score2 = score(n2)

                when {
                    score1 > score2 -> NumberChoice(
                        multiplicand = n2,
                        multiplier = n1,
                        note = "Auto picked Number 1 as the easier split."
                    )

                    score2 > score1 -> NumberChoice(
                        multiplicand = n1,
                        multiplier = n2,
                        note = "Auto picked Number 2 as the easier split."
                    )

                    digitCount(n1) < digitCount(n2) -> NumberChoice(
                        multiplicand = n2,
                        multiplier = n1,
                        note = "Both work. Auto picked the shorter split."
                    )

                    digitCount(n2) < digitCount(n1) -> NumberChoice(
                        multiplicand = n1,
                        multiplier = n2,
                        note = "Both work. Auto picked the shorter split."
                    )

                    n1 <= n2 -> NumberChoice(
                        multiplicand = n2,
                        multiplier = n1,
                        note = "Both work. Auto picked the smaller split."
                    )

                    else -> NumberChoice(
                        multiplicand = n1,
                        multiplier = n2,
                        note = "Both work. Auto picked the smaller split."
                    )
                }
            }
        }
    }

    private fun score(n: Long): Int {
        return when {
            n in rootPairs || n in powerPairs -> 100
            isMidOne(n) -> 95
            isSkeletonRatio(n) -> 90
            n == 25L -> 85
            n % 10 == 5L -> 80
            hasAnchorOne(n) -> 75
            else -> 50 - digitSum(n).coerceAtMost(30)
        }
    }

    private fun digitCount(n: Long): Int = n.toString().length

    private fun digitSum(n: Long): Int = n.toString().sumOf { it.digitToInt() }

    private fun hasAnchorOne(n: Long): Boolean {
        val s = n.toString()
        return s.startsWith("1") || s.endsWith("1")
    }

    private fun isMidOne(n: Long): Boolean {
        return n in 101L..999L && ((n / 10) % 10 == 1L)
    }

    private fun isSkeletonRatio(n: Long): Boolean {
        if (n !in 100L..999L) return false
        val p = n / 100
        val s = n % 10
        return p != 0L && s != 0L && (p * 2 == s || s * 2 == p)
    }

    private fun solveMidOne(m: Long, base: Long, actual: Long, choiceNote: String): VedicResult {
        val a = m / 100
        val c = m % 10
        val vedicParts = listOf(a * base, base, c * base).filter { it != 0L }

        return VedicResult(
            finalAnswer = actual,
            methodLabel = "MIDDLE-1 GIFT",
            vedicLine = formatStandardLine(vedicParts, actual),
            gradeSchoolLines = formatGradeSchool(multiplierRows(base, m), actual),
            note = mergeNotes(choiceNote, "Middle digit 1 gives the middle chunk directly.")
        )
    }

    private fun solveSkeleton(m: Long, base: Long, actual: Long, choiceNote: String): VedicResult {
        val p = m / 100
        val mid = (m / 10) % 10
        val s = m % 10

        val pRes = p * base
        val midRes = mid * base
        val sRes = if (p * 2 == s) pRes * 2 else pRes / 2

        val vedicParts = listOf(pRes, midRes, sRes).filter { it != 0L }

        return VedicResult(
            finalAnswer = actual,
            methodLabel = "SKELETON RATIO",
            vedicLine = formatStandardLine(vedicParts, actual),
            gradeSchoolLines = formatGradeSchool(multiplierRows(base, m), actual),
            note = mergeNotes(choiceNote, "Ratio $p:$s recognized.")
        )
    }

    private fun solveGolden(g: Long, base: Long, actual: Long, choiceNote: String): VedicResult {
        val p = g / 100
        val root = g % 10

        val first: Long
        val second: Long
        val label: String
        val extraNote: String

        if (g in rootPairs) {
            first = p * base
            second = first * p
            label = "GOLDEN ROOT"
            extraNote = "Root pair recognized."
        } else {
            second = root * base
            first = second * root
            label = "GOLDEN POWER"
            extraNote = "Power pair recognized."
        }

        return VedicResult(
            finalAnswer = actual,
            methodLabel = label,
            vedicLine = formatStandardLine(listOf(first, second), actual),
            gradeSchoolLines = formatGradeSchool(multiplierRows(base, g), actual),
            note = mergeNotes(choiceNote, extraNote)
        )
    }

    private fun solveStandard(m: Long, base: Long, actual: Long, choiceNote: String): VedicResult {
        val label = when {
            m == 25L -> "×25 SHORTCUT"
            hasAnchorOne(m) -> "1-ANCHOR SPLIT"
            m % 10 == 5L -> "ENDS-IN-5 SPLIT"
            else -> "STANDARD SPLIT"
        }

        val extra = when {
            m == 25L -> "Friendly ×25 split."
            hasAnchorOne(m) -> "1-anchor pattern."
            m % 10 == 5L -> "Ends-in-5 split."
            else -> ""
        }

        val vedicParts = multiplierParts(base, m)
        val gradeRows = multiplierRows(base, m)

        return VedicResult(
            finalAnswer = actual,
            methodLabel = label,
            vedicLine = formatStandardLine(vedicParts, actual),
            gradeSchoolLines = formatGradeSchool(gradeRows, actual),
            note = mergeNotes(choiceNote, extra)
        )
    }

    private fun solveVerticalCrosswise(
        base: Long,
        multiplier: Long,
        actual: Long,
        choiceNote: String
    ): VedicResult {
        return when {
            digitCount(base) == 2 && digitCount(multiplier) == 2 ->
                solveVc2x2(base, multiplier, actual, choiceNote)

            digitCount(base) == 3 && digitCount(multiplier) == 2 ->
                solveVc3x2(base, multiplier, actual, choiceNote)

            else ->
                VedicResult(
                    finalAnswer = actual,
                    methodLabel = "VERTICAL & CROSSWISE",
                    vedicLine = "V&C ready for 2x2 and 3x2.",
                    gradeSchoolLines = formatGradeSchool(multiplierRows(base, multiplier), actual),
                    note = mergeNotes(choiceNote, "This V&C version is set up for 2x2 and 3x2.")
                )
        }
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

    private fun solveVc3x2(
        base: Long,
        multiplier: Long,
        actual: Long,
        choiceNote: String
    ): VedicResult {
        val tensDigit = multiplier / 10
        val onesDigit = multiplier % 10

        val topPartial = base * tensDigit
        val bottomPartial = base * onesDigit

        val left = (topPartial / 100) * 10
        val middle = (topPartial % 100) + (bottomPartial / 10)
        val right = bottomPartial % 10

        val vcParts = listOf(
            left.toString(),
            middle.toString().padStart(2, '0'),
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
            note = mergeNotes(choiceNote, "True V&C for 3 digits by 2 digits.")
        )
    }

    private fun multiplierParts(base: Long, multiplier: Long): List<Long> {
        return when {
            multiplier < 10L -> listOf(base * multiplier)

            multiplier < 100L -> {
                val tens = multiplier / 10
                val ones = multiplier % 10
                listOf(tens * base, ones * base).filter { it != 0L }
            }

            multiplier < 1000L -> {
                val hundreds = multiplier / 100
                val tens = (multiplier / 10) % 10
                val ones = multiplier % 10
                listOf(hundreds * base, tens * base, ones * base).filter { it != 0L }
            }

            else -> {
                multiplier.toString()
                    .map { it.digitToInt().toLong() * base }
                    .filter { it != 0L }
            }
        }
    }

    private fun multiplierRows(base: Long, multiplier: Long): List<Long> {
        val digits = multiplier.toString().map { it.digitToInt().toLong() }
        val size = digits.size

        val rows = digits.mapIndexedNotNull { index, digit ->
            if (digit == 0L) {
                null
            } else {
                val shift = size - index - 1
                digit * base * pow10(shift)
            }
        }

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

    private fun pow10(exp: Int): Long {
        var result = 1L
        repeat(exp) { result *= 10L }
        return result
    }
}