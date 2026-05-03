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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.multiplication.two_digit.R
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

    val howItWorks = remember {
        val assetText = loadAsset(context, "legend_rules.md")
        if (assetText == "File legend_rules.md not found.") {
            context.getString(R.string.dialog_how_it_works_message)
        } else {
            assetText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.screen_title),
                color = Color(0xFFFFD700),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            HeaderLink(
                text = stringResource(R.string.button_how_it_works),
                fontSize = 13.sp
            ) {
                popupTitle = context.getString(R.string.dialog_how_it_works_title)
                popupContent = howItWorks
                showPopup = true
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CompactVedicField(
                value = num1,
                label = stringResource(R.string.label_first_number),
                modifier = Modifier.weight(1f),
                onValueChange = {
                    num1 = it
                    result = null
                    statusText = "Ready!"
                }
            )

            CompactVedicField(
                value = num2,
                label = stringResource(R.string.label_second_number),
                modifier = Modifier.weight(1f),
                onValueChange = {
                    num2 = it
                    result = null
                    statusText = "Ready!"
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.helper_input_range),
            color = Color(0xFFFFD700),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Method:",
                color = Color(0xFFFFD700),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(6.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SolveChip(
                    text = stringResource(R.string.button_auto),
                    selected = solveMode == SolveMode.AUTO,
                    fontSize = 11.sp
                ) {
                    solveMode = SolveMode.AUTO
                }

                SolveChip(
                    text = stringResource(R.string.button_standard),
                    selected = solveMode == SolveMode.STANDARD,
                    fontSize = 11.sp
                ) {
                    solveMode = SolveMode.STANDARD
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.helper_method_choice),
            color = Color(0xFFFFD700),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = {
                    val a = num1.toIntOrNull()
                    val b = num2.toIntOrNull()

                    if (a == null || b == null || a !in 10..99 || b !in 10..99) {
                        result = null
                        statusText = context.getString(R.string.error_invalid_input)
                    } else {
                        result = VedicLogic.calculate(a.toLong(), b.toLong(), solveMode)
                        statusText = ""
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_calculate),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
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
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_clear),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF111111))
                .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.35f))
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "${stringResource(R.string.label_answer)}: ${result?.finalAnswer ?: 0}",
                    color = Color(0xFFFFD700),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(6.dp))

                val methodLabel = result?.methodLabel ?: when (solveMode) {
                    SolveMode.AUTO -> stringResource(R.string.method_auto)
                    SolveMode.STANDARD -> stringResource(R.string.method_standard)
                }

                Text(
                    text = stringResource(R.string.result_method_used, methodLabel),
                    color = Color(0xFFFFD700),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (result == null) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    Text(
                        text = result?.vedicLine.orEmpty(),
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.20f)
                    )

                    Text(
                        text = result?.gradeSchoolLines?.joinToString("\n").orEmpty(),
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
        ) {
            Text(
                text = "RETURN",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }

    if (showPopup) {
        Dialog(onDismissRequest = { showPopup = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0A0A0A),
                border = BorderStroke(1.dp, Color(0xFFFFD700))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = popupTitle,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = popupContent,
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showPopup = false },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.button_close),
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderLink(
    text: String,
    fontSize: TextUnit = 13.sp,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = Color(0xFFFFD700),
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 3.dp, vertical = 2.dp)
    )
}

@Composable
fun SolveChip(
    text: String,
    selected: Boolean,
    fontSize: TextUnit = 11.sp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFFFFD700) else Color(0xFF555555),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (selected) Color(0x33FFD700) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFFFFD700) else Color.White,
            fontSize = fontSize,
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
            if (input.all { it.isDigit() } && input.length <= 2) {
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
        modifier = modifier.height(68.dp),
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color(0xFF444444),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFFFFD700),
            unfocusedLabelColor = Color(0xFFFFD700)
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