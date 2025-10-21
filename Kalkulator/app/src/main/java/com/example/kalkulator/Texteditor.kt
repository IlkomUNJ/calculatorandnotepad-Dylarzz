package com.example.kalkulator

import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kalkulator.ui.theme.KalkulatorTheme
import kotlin.math.max
import kotlin.math.min

fun applyStyle(text: TextFieldValue, style: SpanStyle): TextFieldValue {
    val annotatedString = text.annotatedString
    val selected = text.selection

    if (selected.collapsed){
        return text
    }

    val newText = buildAnnotatedString {
        // Don't just append and add - you need to preserve existing styles
        append(annotatedString.text)

        // Copy over existing span styles
        annotatedString.spanStyles.forEach { spanStyle ->
            addStyle(spanStyle.item, spanStyle.start, spanStyle.end)
        }

        // Add the new style
        addStyle(style, selected.start, selected.end)
    }

    return text.copy(
        annotatedString = newText,
        selection = TextRange(selected.end)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditor(navController: NavController)
{
    var text by remember { mutableStateOf(
        TextFieldValue(
            annotatedString = buildAnnotatedString { append("") }
        )
    ) }
    var fontSize by remember { mutableIntStateOf(16) }
    var isBoldActive by remember { mutableStateOf(false) }
    var isItalicActive by remember { mutableStateOf(false) }

    // Update active flags based on cursor position
    LaunchedEffect(text.selection) {
        val cursorPosition = text.selection.start

        // Check if any span at cursor position has bold
        isBoldActive = text.annotatedString.spanStyles.any { spanStyle ->
            cursorPosition >= spanStyle.start &&
                    cursorPosition <= spanStyle.end &&
                    spanStyle.item.fontWeight == FontWeight.Bold
        }

        // Check if any span at cursor position has italic
        isItalicActive = text.annotatedString.spanStyles.any { spanStyle ->
            cursorPosition >= spanStyle.start &&
                    cursorPosition <= spanStyle.end &&
                    spanStyle.item.fontStyle == FontStyle.Italic
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Text Editor",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("main_screen") }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        // Font Size Controls
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            IconButton(
                                onClick = { if (fontSize > 10) fontSize -= 1 }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RemoveCircleOutline,
                                    contentDescription = "decrease font size",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = fontSize.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            IconButton(
                                onClick = { fontSize += 1}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircleOutline,
                                    contentDescription = "increase font size",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Bold Button
                        FilledIconToggleButton(
                            checked = isBoldActive,
                            onCheckedChange = {
                                if (!text.selection.collapsed) {
                                    if (!isBoldActive) {
                                        text = applyStyle(
                                            text = text,
                                            style = SpanStyle(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    else {
                                        text = applyStyle(
                                            text = text,
                                            style = SpanStyle(fontWeight = FontWeight.Normal)
                                        )
                                    }
                                }
                                isBoldActive = !isBoldActive
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 4.dp),
                            colors = IconButtonDefaults.filledIconToggleButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(
                                text = "B",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        // Italic Button
                        FilledIconToggleButton(
                            checked = isItalicActive,
                            onCheckedChange = {
                                if (!text.selection.collapsed) {
                                    if (!isItalicActive) {
                                        text = applyStyle(
                                            text = text,
                                            style = SpanStyle(fontStyle = FontStyle.Italic)
                                        )
                                    }
                                    else{
                                        text = applyStyle(
                                            text = text,
                                            style = SpanStyle(fontStyle = FontStyle.Normal)
                                        )
                                    }
                                }
                                isItalicActive = !isItalicActive
                            },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.filledIconToggleButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(
                                text = "I",
                                style = TextStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { text = TextFieldValue(
                    annotatedString = buildAnnotatedString { append("") }
                ) },
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "increase font size"
                    )
                }
            )
        },
        content = { paddingValues ->
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black)
                        .padding(paddingValues)
                )
                BasicTextField(
                    value = text,
                    onValueChange = { newValue ->
                        val oldLength = text.text.length
                        val newLength = newValue.text.length

                        val newAnnotatedString = buildAnnotatedString {
                            append(newValue.text)

                            // Preserve existing styles
                            text.annotatedString.spanStyles.forEach { spanStyle ->
                                val start = minOf(spanStyle.start, newValue.text.length)
                                val end = minOf(spanStyle.end, newValue.text.length)
                                if (start < end) {
                                    addStyle(spanStyle.item, start, end)
                                }
                            }

                            // Apply active styles to newly typed text
                            if (newLength > oldLength) {
                                val insertPosition = newValue.selection.start - (newLength - oldLength)
                                val insertedLength = newLength - oldLength

                                if (isBoldActive) {
                                    addStyle(
                                        SpanStyle(fontWeight = FontWeight.Bold),
                                        insertPosition,
                                        insertPosition + insertedLength
                                    )
                                }

                                if (isItalicActive) {
                                    addStyle(
                                        SpanStyle(fontStyle = FontStyle.Italic),
                                        insertPosition,
                                        insertPosition + insertedLength
                                    )
                                }
                            }
                        }

                        text = newValue.copy(annotatedString = newAnnotatedString)
                    },
                    textStyle = TextStyle(fontSize = fontSize.sp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.White),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                        ) {
                            if(text.text.isEmpty()){
                                Text(
                                    text = "Type here...",
                                    color = Color.Gray,
                                    fontSize = fontSize.sp
                                )
                            }
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(Color.Black)
                )
            }
        }
    )
}

@Preview
@Composable
fun TextEditorPreview()
{
    TextEditor(navController = rememberNavController())
}
