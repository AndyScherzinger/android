/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2024 Alper Ozturk <alper.ozturk@nextcloud.com>
 * SPDX-License-Identifier: AGPL-3.0-or-later OR GPL-2.0-only
 */
package com.nextcloud.client.assistant.task

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nextcloud.client.assistant.task.model.TaskViewBottomSheetType
import com.nextcloud.client.assistant.taskDetail.TaskDetailBottomSheet
import com.owncloud.android.lib.resources.assistant.model.Task
import com.owncloud.android.lib.resources.assistant.model.TaskInput
import com.owncloud.android.lib.resources.assistant.model.TaskOutput

@Suppress("LongMethod", "MagicNumber")
@Composable
fun TaskView(task: Task, showTaskActions: () -> Unit) {
    var bottomSheetType by remember { mutableStateOf<TaskViewBottomSheetType?>(null) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    bottomSheetType = TaskViewBottomSheetType.Detail
                }
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            task.input?.input?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(300.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            task.output?.output?.let {
                val output = if (it.length >= 100) {
                    it.take(100) + "..."
                } else {
                    it
                }

                Text(
                    text = output,
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                )
            }

            TaskStatusView(task, foregroundColor = Color.White)

            bottomSheetType?.let {
                TaskViewBottomSheet(it, task, dismiss = {
                    bottomSheetType = null
                }, showTaskActions = {
                    showTaskActions()
                })
            }
        }

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = showTaskActions
        )
        {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More button",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun TaskViewBottomSheet(
    bottomSheetType: TaskViewBottomSheetType,
    task: Task,
    showTaskActions: () -> Unit,
    dismiss: () -> Unit
) {
    when (bottomSheetType) {
        TaskViewBottomSheetType.Detail -> {
            TaskDetailBottomSheet(task, showTaskActions = {
                dismiss()
                showTaskActions()
            }) {
                dismiss()
            }
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun TaskViewPreview() {
    TaskView(
        task = Task(
            1,
            "Free Prompt",
            "STATUS_COMPLETED",
            "1",
            "1",
            TaskInput("What about other promising tokens like"),
            TaskOutput("Several tokens show promise for future growth in the" +
                "cryptocurrency market"),
            1707692337,
            1707692337,
            1707692337,
            1707692337,
            1707692337,
        ), showTaskActions = {

        }
    )
}
