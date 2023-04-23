package com.annguyenhoang.annotes.common.margin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun MarginVertical(value: Dp) {
    Spacer(modifier = Modifier.padding(vertical = value))
}

@Composable
fun MarginHorizontal(value: Dp) {
    Spacer(modifier = Modifier.padding(horizontal = value))
}


@Composable
fun MarginTop(value: Dp) {
    Spacer(modifier = Modifier.padding(top = value))
}

@Composable
fun MarginBottom(value: Dp) {
    Spacer(modifier = Modifier.padding(bottom = value))
}

@Composable
fun MarginStart(value: Dp) {
    Spacer(modifier = Modifier.padding(start = value))
}

@Composable
fun MarginEnd(value: Dp) {
    Spacer(modifier = Modifier.padding(end = value))
}