package com.makvas.todoapp.presentation.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makvas.todoapp.data.Task
import com.makvas.todoapp.presentation.ui.screens.home.MainScreenEvent

@Composable
fun TaskItem(
    task: Task,
    onEvent: (MainScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(colorScheme.surfaceContainer)
            .padding(start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isCompleted ->
                onEvent(MainScreenEvent.OnCompletedClick(task, isCompleted))
            }
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                fontSize = 20.sp
            )
            task.description?.let {
                Text(
                    text = task.description,
                    fontSize = 14.sp
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            task.time?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
            }

            if (task.isImportant) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(colorScheme.error),
                    contentAlignment = Alignment.Center,
                    content = {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "!",
                            color = colorScheme.onError
                        )
                    },
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                    content = {
                        Text(
                            text = " "
                        )
                    },
                )
            }
        }
    }
}