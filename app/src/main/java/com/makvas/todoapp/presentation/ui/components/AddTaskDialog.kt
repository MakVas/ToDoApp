package com.makvas.todoapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makvas.todoapp.R
import com.makvas.todoapp.domain.repository.TaskEvent
import com.makvas.todoapp.domain.model.TaskState

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
                    fontWeight = FontWeight.Bold,
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
                    value = state.description,
                    onValueChange = { onEvent(TaskEvent.SetDescription(it)) },
                    placeholder = {
                        Text("Description")
                    }
                )

                Text(
                    text = "isImportant: ${state.isImportant}"
                )

                Text(
                    text = "Selected Date: ${state.date}"
                )

                Text(
                    text = "Selected Time: ${state.time}"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    Row {
                        Checkbox(
                            checked = state.isImportant,
                            onCheckedChange = { onEvent(TaskEvent.SetIsImportant(it)) },
                        )

                        CustomDatePicker(
                            onValueChange = { onEvent(TaskEvent.SetDate(it)) }
                        )

                        CustomTimePicker(
                            onValueChange = { onEvent(TaskEvent.SetTime(it)) }
                        )
                    }

                    IconButton(
                        onClick = { onEvent(TaskEvent.SaveTask) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save"
                        )
                    }
                }
            }
        }
    )
}