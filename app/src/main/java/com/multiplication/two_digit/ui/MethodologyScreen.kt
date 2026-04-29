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
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vedic Math",
                color = Color(0xFFFFD700),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
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

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Choose:",
                color = Color(0xFFFFD700),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(4.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                SolveChip(
                    text = "AUTO",
                    selected = solveMode == SolveMode.AUTO
                ) { solveMode = SolveMode.AUTO }

                SolveChip(
                    text = "1",
                    selected = solveMode == SolveMode.USE_NUMBER_1
                ) { solveMode = SolveMode.USE_NUMBER_1 }

                SolveChip(
                    text = "2",
                    selected = solveMode == SolveMode.USE_NUMBER_2
                ) { solveMode = SolveMode.USE_NUMBER_2 }

                SolveChip(
                    text = "V&C",
                    selected = solveMode == SolveMode.VERTICAL_CROSSWISE
                ) { solveMode = SolveMode.VERTICAL_CROSSWISE }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
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
                    .height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "CALC",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
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
                    .height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "CLR",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF111111))
                .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.25f))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ANSWER: ${result?.finalAnswer ?: 0}",
                    color = Color(0xFFFFD700),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (result == null) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    Text(
                        text = result?.vedicLine.orEmpty(),
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.20f)
                    )

                    Text(
                        text = result?.gradeSchoolLines?.joinToString("\n").orEmpty(),
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
        ) {
            Text(
                text = "RETURN",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
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
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = popupTitle,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = popupContent,
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { showPopup = false },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "CLOSE",
                            color = Color.Black,
                            fontSize = 10.sp
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
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 2.dp, vertical = 1.dp)
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
                shape = RoundedCornerShape(14.dp)
            )
            .background(
                color = if (selected) Color(0x33FFD700) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFFFFD700) else Color.White,
            fontSize = 9.sp,
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
                fontSize = 9.sp
            )
        },
        modifier = modifier.height(58.dp),
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 13.sp,
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