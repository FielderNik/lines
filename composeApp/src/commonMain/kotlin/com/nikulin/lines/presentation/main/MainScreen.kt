package com.nikulin.lines.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nikulin.lines.domain.repositories.languages
import com.nikulin.lines.domain.repositories.mock
import com.nikulin.lines.domain.repositories.mock1

@Composable
fun MainRoute() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text("MAIN SCREEN")

        TableScreen()
    }
}

@Composable
private fun MainScreen() {
    val tableData = (1..100).mapIndexed { index, item ->
        index to "Item $index"
    }
    val column1Weight = .3f // 30%
    val column2Weight = .6f // 70%
    LazyVerticalGrid(
        columns = GridCells.Fixed(1)
    ) {
        item {
            Row(Modifier.background(Color.Gray), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TableCell(text = "Column 1", weight = column1Weight)
                VerticalDivider(modifier = Modifier, color = Color.Black, thickness = 2.dp)
                TableCell(text = "Column 2", weight = column2Weight)
            }
            HorizontalDivider()
        }
        itemsIndexed(items = tableData) { index, item ->
            val (id, text) = item
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TableCell(text = id.toString(), weight = column1Weight)

                VerticalDivider(modifier = Modifier, color = Color.Black, thickness = 2.dp)

                TableCell(text = text, weight = column2Weight)
            }
            if (index < tableData.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun TableScreen() {

    val tableData = (1..100).mapIndexed { index, item ->
        index to "Item $index"
    }

    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {

        item {
            Row(Modifier.background(Color.Gray)/*, horizontalArrangement = Arrangement.spacedBy(4.dp)*/) {

                TableCellRightBorder(text = "Ключ", weight = column1Weight)

                languages.forEachIndexed { index, language ->
                    if (index != languages.lastIndex) {

                        TableCellRightBorder(text = language.value, weight = column2Weight)
                    } else {

                        TableCell(text = language.value, weight = column2Weight)
                    }

                }
            }
            HorizontalDivider()
        }

        itemsIndexed(items = mock1) { index, item ->
//            val (id, text) = item
            val (key, translations) = item
            Row(modifier = Modifier.fillMaxWidth(),/* horizontalArrangement = Arrangement.spacedBy(4.dp)*/) {
                TableCellRightBorder(text = key, weight = column1Weight)

                languages.forEachIndexed { index, language ->
                    val tr = translations[language]
                    if (index != languages.lastIndex) {
                        TableCellRightBorder(text = tr?.value.orEmpty(), weight = column2Weight)
                    } else {
                        TableCell(text = tr?.value.orEmpty(), weight = column2Weight)
                    }

                }

            }

            if (index != mock1.lastIndex) {
                HorizontalDivider()
            }

        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .background(color = if (text.isNotEmpty()) Color.Transparent else Color.Red)
            .padding(8.dp)
    )
}

@Composable
fun RowScope.TableCellRightBorder(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .rightBorder(1.dp, Color.Black)
            .weight(weight)
            .background(color = if (text.isNotEmpty()) Color.Transparent else Color.Red)
            .padding(8.dp)

    )
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

enum class BorderPosition {
    BOTTOM, RIGHT
}
