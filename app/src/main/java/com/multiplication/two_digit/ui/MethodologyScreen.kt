package com.multiplication.two_digit.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.multiplication.two_digit.SolveMode
import com.multiplication.two_digit.VedicLogic
import com.multiplication.two_digit.VedicResult

@Composable
fun MethodologyScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<VedicResult?>(null) }
    var statusText by remember { mutableStateOf("Ready!") }
    var solveMode by remember { mutableStateOf(SolveMode.AUTO) }

    var showPopup by remember { mutableStateOf(false) }
    var popupTitle by remember { mutableStateOf("") }
    var popupContent by remember { mutableStateOf("") }

    val lesson = remember { loadAsset(context, "lesson_notes.md") }
    val rules = remember { loadAsset(context, "legend_rules.md") }
    val ratios = remember { loadAsset(context, "2x3.md") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vedic Math",
                color = Color(0xFFFFD700),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                HeaderLink("LESSON") {
                    popupTitle = "LESSON"
                    popupContent = lesson
                    showPopup = true
                }
                HeaderLink("RULES") {
                    popupTitle = "RULES"
                    popupContent = rules
                    showPopup = true
                }
                HeaderLink("2x3") {
                    popupTitle = "2x3 RATIOS"
                    popupContent = ratios
                    showPopup = true
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompactVedicField(
                value = num1,
                label = "Number 1",
                modifier = Modifier.weight(1f),
                onValueChange = { num1 = it }
            )

            CompactVedicField(
                value = num2,
                label = "Number 2",
                modifier = Modifier.weight(1f),
                onValueChange = { num2 = it }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Choose Method",
            color = Color(0xFFFFD700),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SolveChip(
                text = "AUTO",
                selected = solveMode == SolveMode.AUTO
            ) { solveMode = SolveMode.AUTO }

            SolveChip(
                text = "USE #1",
                selected = solveMode == SolveMode.USE_NUMBER_1
            ) { solveMode = SolveMode.USE_NUMBER_1 }

            SolveChip(
                text = "USE #2",
                selected = solveMode == SolveMode.USE_NUMBER_2
            ) { solveMode = SolveMode.USE_NUMBER_2 }

            SolveChip(
                text = "V&C",
                selected = solveMode == SolveMode.VERTICAL_CROSSWISE
            ) { solveMode = SolveMode.VERTICAL_CROSSWISE }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AUTO suggests a split. USE #1 and USE #2 let students compare.",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val a = num1.toLongOrNull()
                    val b = num2.toLongOrNull()

                    if (a == null || b == null) {
                        result = null
                        statusText = "Enter two whole numbers."
                    } else {
                        result = VedicLogic.calculate(a, b, solveMode)
                        statusText = ""
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text(
                    text = "CALCULATE",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    num1 = ""
                    num2 = ""
                    result = null
                    statusText = "Ready!"
                    solveMode = SolveMode.AUTO
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text(
                    text = "CLEAR",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF111111))
                .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.25f))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ANSWER: ${result?.finalAnswer ?: 0}",
                    color = Color(0xFFFFD700),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (result == null) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    Text(
                        text = "Method: ${result?.methodLabel.orEmpty()}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    if (!result?.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result?.note.orEmpty(),
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.20f)
                    )

                    Text(
                        text = "Vedic One-Line",
                        color = Color(0xFFFFD700),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = result?.vedicLine.orEmpty(),
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.20f)
                    )

                    Text(
                        text = "Grade School",
                        color = Color(0xFFFFD700),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = result?.gradeSchoolLines?.joinToString("\n").orEmpty(),
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
        ) {
            Text(
                text = "RETURN",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showPopup) {
        Dialog(onDismissRequest = { showPopup = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0A0A0A),
                border = BorderStroke(1.dp, Color(0xFFFFD700))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = popupTitle,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = popupContent,
                            color = Color.White,
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showPopup = false },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text(
                            text = "CLOSE",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderLink(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color(0xFFFFD700),
        fontSize = 14.sp,
        modifier = Modifier
            .clickable { onClick() }
            .padding(6.dp)
    )
}

@Composable
fun SolveChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFFFFD700) else Color(0xFF555555),
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = if (selected) Color(0x33FFD700) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFFFFD700) else Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompactVedicField(
    value: String,
    label: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        label = {
            Text(
                text = label,
                color = Color(0xFFFFD700),
                fontSize = 11.sp
            )
        },
        modifier = modifier.height(64.dp),
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color(0xFF444444)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

fun loadAsset(context: Context, fileName: String): String {
    return try {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        "File $fileName not found."
    }
}