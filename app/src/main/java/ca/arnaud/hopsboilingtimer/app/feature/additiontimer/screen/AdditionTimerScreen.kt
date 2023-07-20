package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionTimerScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AlertRowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonStyle
import ca.arnaud.hopsboilingtimer.app.feature.common.view.TimeButton
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
import ca.arnaud.hopsboilingtimer.app.theme.LocalAppTypography
import ca.arnaud.hopsboilingtimer.app.view.NotificationPermissionDialog
import ca.arnaud.hopsboilingtimer.app.view.TransparentTextField

interface AdditionTimerScreenActionListener {

    fun newAdditionHopsTextChanged(text: String)
    fun newAdditionDurationTextChanged(text: String)
    fun addAdditionClick()
    fun onAdditionRowOptionClick(rowModel: AdditionRowModel, optionType: AdditionOptionType)
    fun onAlertRowCheckChanged(checked: Boolean, alertId: String)
    fun onThemeIconClick(isSystemInDarkTheme: Boolean)

    fun startTimerButtonClick()
    fun onSubButtonClick()

    fun onPermissionResult(granted: Boolean)
}

object AdditionTimerScreenConfig {
    val contentPadding = 15.dp
}

@Composable
fun AdditionTimerScreen(
    model: AdditionTimerScreenModel,
    showRequestPermissionDialog: Boolean,
    actionListener: AdditionTimerScreenActionListener,
) {
    val currentDarkTheme = isSystemInDarkTheme()
    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Hops Boiling Timer", // TODO - put in strings.xml
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Icon(
                        modifier = Modifier
                            .clickable { actionListener.onThemeIconClick(currentDarkTheme) }
                            .padding(5.dp),
                        imageVector = Icons.Filled.InvertColors,
                        contentDescription = null
                    )
                }
                Divider()
            }

        },
        bottomBar = {
            val bottomBarModel = when (model) {
                is AdditionTimerScreenModel.Edit -> model.bottomBarModel
                is AdditionTimerScreenModel.Schedule -> model.bottomBarModel
            }
            BottomBar(
                actionListener::startTimerButtonClick,
                actionListener::onSubButtonClick,
                bottomBarModel
            )
        },
        content = { paddingValues ->
            when (model) {
                is AdditionTimerScreenModel.Edit -> EditContent(
                    modifier = Modifier.padding(paddingValues),
                    model = model,
                    actionListener = actionListener,
                )

                is AdditionTimerScreenModel.Schedule -> {
                    ScheduleContent(
                        modifier = Modifier.padding(paddingValues),
                        model = model,
                        actionListener = actionListener,
                    )
                }
            }
        }
    )

    if (showRequestPermissionDialog) {
        NotificationPermissionDialog(onPermissionResult = actionListener::onPermissionResult)
    }
}

@Composable
private fun EditContent(
    modifier: Modifier = Modifier,
    model: AdditionTimerScreenModel.Edit,
    actionListener: AdditionTimerScreenActionListener,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 10.dp),
        content = {
            model.additionRows.forEach { rowModel ->
                AdditionRow(
                    model = rowModel,
                    onOptionClick = actionListener::onAdditionRowOptionClick
                )
            }

            if (model.additionRows.isNotEmpty()) {
                TitleRow(text = "New Addition")
            }
            AddNewAddition(
                actionListener::newAdditionHopsTextChanged,
                actionListener::newAdditionDurationTextChanged,
                actionListener::addAdditionClick,
                model.newAdditionRow
            )

        })
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    model: AdditionTimerScreenModel.Schedule,
    actionListener: AdditionTimerScreenActionListener,
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 10.dp),
        content = {
            items(
                items = model.nextRows,
                key = { rowItem -> rowItem.id }
            ) { rowModel ->
                AlertNextRow(
                    modifier = Modifier.animateItemPlacement(),
                    model = rowModel,
                )
            }

            item(key = "added") {
                TitleRow(text = "Added")
            }

            items(
                items = model.addedRows,
                key = { rowItem -> rowItem.id }
            ) { rowModel ->
                AlertAddedRow(
                    modifier = Modifier.animateItemPlacement(),
                    model = rowModel,
                    onAlertRowCheckChanged = actionListener::onAlertRowCheckChanged,
                )
            }
        })
}

@Composable
private fun TitleRow(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(modifier = modifier.padding(top = 20.dp)) {
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(horizontal = AdditionTimerScreenConfig.contentPadding),
            text = text,
            style = LocalAppTypography.current.h2
        )
    }
}

@Composable
fun AddNewAddition(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    model: NewAdditionModel,
) {
    Column(
        Modifier.padding(horizontal = AdditionTimerScreenConfig.contentPadding, vertical = 10.dp),
    ) {
        Row(
            // TODO - setup dimension in theme
            modifier = Modifier.padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val focusRequester = remember { FocusRequester() }
            TransparentTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = model.title,
                label = { Text("Hops") },
                singleLine = true,
                onValueChange = newAdditionHopsTextChanged,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            TransparentTextField(
                modifier = Modifier.width(80.dp),
                value = model.duration,
                label = { Text("Min") },
                singleLine = true,
                onValueChange = newAdditionDurationTextChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        addAdditionClick()
                        focusRequester.requestFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .height(40.dp),
                onClick = addAdditionClick,
                enabled = model.buttonEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun AdditionRow(
    onOptionClick: (AdditionRowModel, AdditionOptionType) -> Unit,
    model: AdditionRowModel,
) {
    Row(
        // TODO - setup dimension in theme
        modifier = Modifier.padding(
            horizontal = AdditionTimerScreenConfig.contentPadding,
            vertical = 10.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO - setup typography
        Text(
            modifier = Modifier.weight(1f),
            text = model.title
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = model.duration
        )

        Spacer(modifier = Modifier.width(10.dp))

        AdditionOptions(
            onClick = { type -> onOptionClick(model, type) },
            types = model.options
        )
    }
}

@Composable
fun AlertNextRow(
    modifier: Modifier = Modifier,
    model: AlertRowModel.Next,
) {
    Row(
        // TODO - setup dimension in theme
        modifier = modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fontWeight = if (model.highlighted) FontWeight.Bold else null

        Text(
            modifier = Modifier.weight(1f),
            text = model.title,
            fontWeight = fontWeight,
            style = LocalAppTypography.current.body2
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = model.time,
            fontWeight = fontWeight,
            style = LocalAppTypography.current.body2
        )
    }
}

@Composable
fun AlertAddedRow(
    modifier: Modifier = Modifier,
    onAlertRowCheckChanged: (Boolean, String) -> Unit,
    model: AlertRowModel.Added,
) {
    Row(
        // TODO - setup dimension in theme
        modifier = modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO - setup typography
        val textAlpha = 0.3f
        val textColor = LocalContentColor.current.copy(alpha = textAlpha)
        val fontStyle = FontStyle.Italic
        val textDecoration = if (model.checked) TextDecoration.LineThrough else null

        Text(
            modifier = Modifier.weight(1f),
            text = model.title,
            fontStyle = fontStyle,
            textDecoration = textDecoration,
            color = textColor,
            style = LocalAppTypography.current.body2
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = model.time,
            fontStyle = fontStyle,
            textDecoration = textDecoration,
            color = textColor,
            style = LocalAppTypography.current.body2
        )

        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(
            checked = model.checked,
            onCheckedChange = { newChecked ->
                onAlertRowCheckChanged(newChecked, model.id)
            }
        )
    }
}

@Composable
private fun AdditionOptions(
    onClick: (AdditionOptionType) -> Unit,
    types: List<AdditionOptionType>,
) {
    types.forEach { type ->
        val imageVector = when (type) {
            AdditionOptionType.Delete -> Icons.Filled.Delete
        }
        Icon(
            modifier = Modifier
                .clickable { onClick(type) }
                .padding(5.dp),
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

@Composable
private fun BottomBar(
    startTimerButtonClick: () -> Unit,
    onSubButtonClick: () -> Unit,
    model: BottomBarModel,
) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TimeButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = startTimerButtonClick,
            model = model.timeButton
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HopsAppTheme(darkTheme = true) {
        AdditionTimerScreen(
            model = AdditionTimerScreenModel.Edit(
                additionRows = listOf(
                    AdditionRowModel("", "Amarillo", "60"),
                    AdditionRowModel("", "Mozaic", "45"),
                    AdditionRowModel("", "Saaz", "5"),
                    AdditionRowModel("", "El Dorado", "10"),
                ),
                newAdditionRow = NewAdditionModel(
                    title = "new addition",
                    duration = "30",
                    buttonEnabled = true
                ),
                bottomBarModel = BottomBarModel(
                    timeButton = TimeButtonModel(
                        title = "Start Timer",
                        time = "60 Min",
                        style = TimeButtonStyle.Start,
                        enabled = true,
                    ),
                )
            ),
            showRequestPermissionDialog = false,
            actionListener = object : AdditionTimerScreenActionListener {

                override fun newAdditionHopsTextChanged(text: String) {

                }

                override fun newAdditionDurationTextChanged(text: String) {

                }

                override fun addAdditionClick() {}
                override fun onAdditionRowOptionClick(
                    rowModel: AdditionRowModel, optionType: AdditionOptionType,
                ) {
                }

                override fun onAlertRowCheckChanged(checked: Boolean, alertId: String) {}
                override fun onThemeIconClick(isSystemInDarkTheme: Boolean) {}
                override fun startTimerButtonClick() {

                }

                override fun onSubButtonClick() {

                }

                override fun onPermissionResult(granted: Boolean) {

                }

            })
    }
}
