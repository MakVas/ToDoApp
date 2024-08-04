package com.makvas.todoapp.presentation.ui.screens.add_task.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(initialValue = 1f) }
    val previousSelected = remember { mutableStateOf(selected) }

    LaunchedEffect(key1 = selected) {
        if (!previousSelected.value && selected) {
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(durationMillis = 50)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        previousSelected.value = selected
    }

    FilterChip(
        modifier = modifier
            .scale(scale.value),
        selected = selected,
        onClick = onClick,
        label = { Text(text = text) }
    )
}