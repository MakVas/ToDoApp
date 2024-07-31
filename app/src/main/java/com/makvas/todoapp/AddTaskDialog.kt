package com.makvas.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TaskEvent.HideDialog) },
        content = {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Add task"
                )

                TextField(
                    value = state.title,
                    onValueChange = { onEvent(TaskEvent.SetTitle(it)) },
                    placeholder = {
                        Text("Title")
                    }
                )

                TextField(
                    value = state.date,
                    onValueChange = { onEvent(TaskEvent.SetDate(it)) },
                    placeholder = {
                        Text("Date")
                    }
                )

                TextField(
                    value = state.time,
                    onValueChange = { onEvent(TaskEvent.SetTime(it)) },
                    placeholder = {
                        Text("Time")
                    }
                )

                TextField(
                    value = state.description,
                    onValueChange = { onEvent(TaskEvent.SetDescription(it)) },
                    placeholder = {
                        Text("Description")
                    }
                )

                Row{
                    Checkbox(
                        checked = state.isImportant,
                        onCheckedChange = { onEvent(TaskEvent.SetIsImportant(it)) },
                    )
                    Text("Important")
                }


                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onEvent(TaskEvent.SaveTask)
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}