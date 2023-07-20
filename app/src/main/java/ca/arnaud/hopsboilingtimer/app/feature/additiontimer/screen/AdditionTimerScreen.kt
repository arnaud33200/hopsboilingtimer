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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.RowModel
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
import ca.arnaud.hopsboilingtimer.app.theme.LocalAppColors
import ca.arnaud.hopsboilingtimer.app.theme.LocalAppTypography
import ca.arnaud.hopsboilingtimer.app.view.NotificationPermissionDialog
import ca.arnaud.hopsboilingtimer.app.view.TransparentTextField

interface AdditionTimerScreenActionListener {

    fun newAdditionHopsTextChanged(text: String)
    fun newAdditionDurationTextChanged(text: String)
    fun addAdditionClick()
    fun onOptionClick(rowModel: RowModel, optionType: AdditionOptionType)
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
    model: MainScreenModel,
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

        }
    ) { paddingValues ->
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 10.dp)
                    .padding(paddingValues),
                content = {
                    model.additionRows.forEach { rowModel ->
                        when (rowModel) {
                            is RowModel.AdditionRowModel -> {
                                AdditionRow(model = rowModel, onOptionClick = actionListener::onOptionClick)
                            }
                            is RowModel.AlertRowModel -> {
                                AlertRow(
                                    model = rowModel,
                                    onAlertRowCheckChanged = actionListener::onAlertRowCheckChanged
                                )
                            }
                        }
                    }

                    model.newAdditionRow?.let { newAdditionRow ->
                        if (model.additionRows.isNotEmpty()) {
                            TitleRow(
                                modifier = Modifier.padding(top = 10.dp),
                                text = "New Addition"
                            )
                        }
                        AddNewAddition(
                            actionListener::newAdditionHopsTextChanged,
                            actionListener::newAdditionDurationTextChanged,
                            actionListener::addAdditionClick,
                            newAdditionRow
                        )
                    }
                })
            BottomBar(actionListener::startTimerButtonClick, actionListener::onSubButtonClick, model.bottomBarModel)
        }
    }

    if (showRequestPermissionDialog) {
        NotificationPermissionDialog(onPermissionResult = actionListener::onPermissionResult)
    }
}

@Composable
private fun TitleRow(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(modifier = modifier) {
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(horizontal = AdditionTimerScreenConfig.contentPadding),
            text = text, // TODO - hardcoded string
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
            TransparentTextField(
                modifier = Modifier.weight(1f),
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
    onOptionClick: (RowModel, AdditionOptionType) -> Unit,
    model: RowModel.AdditionRowModel,
) {
    Row(
        // TODO - setup dimension in theme
        modifier = Modifier.padding(horizontal = AdditionTimerScreenConfig.contentPadding, vertical = 10.dp),
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
fun AlertRow(
    onAlertRowCheckChanged: (Boolean, String) -> Unit,
    model: RowModel.AlertRowModel,
) {
    Row(
        // TODO - setup dimension in theme
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO - setup typography
        val textAlpha = if (model.disabled) 0.3f else 1.0f
        val textColor = LocalContentColor.current.copy(alpha = textAlpha)
        val fontStyle = if (model.disabled) FontStyle.Italic else null
        val fontWeight = if (model.highlighted) FontWeight.Bold else null
        val textDecoration = if (model.addChecked == true) TextDecoration.LineThrough else null

        Text(
            modifier = Modifier.weight(1f),
            text = model.title,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            textDecoration = textDecoration,
            color = textColor,
            style = LocalAppTypography.current.body2
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = model.duration,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            textDecoration = textDecoration,
            color = textColor,
            style = LocalAppTypography.current.body2
        )

        model.addChecked?.let { checked ->
            Spacer(modifier = Modifier.width(10.dp))
            Checkbox(
                checked = checked,
                onCheckedChange = { newChecked ->
                    onAlertRowCheckChanged(newChecked, model.id)
                }
            )
        }
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
        val backgroundColor = when (model.buttonStyle) {
            ButtonStyle.Start -> LocalAppColors.current.primary
            ButtonStyle.Stop -> LocalAppColors.current.secondary
        }

        Button(
            // TODO - setup dimension
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            enabled = model.buttonEnable,
            onClick = startTimerButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            )
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = model.buttonTitle,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = model.buttonTime,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HopsAppTheme(darkTheme = true) {
        AdditionTimerScreen(
            model = MainScreenModel(
                additionRows = listOf(
                    RowModel.AdditionRowModel("", "Amarillo", "60"),
                    RowModel.AdditionRowModel("", "Mozaic", "45"),
                    RowModel.AdditionRowModel("", "Saaz", "5"),
                    RowModel.AdditionRowModel("", "El Dorado", "10"),
                ),
                newAdditionRow = NewAdditionModel(
                    title = "new addition",
                    duration = "30",
                    buttonEnabled = true
                ),
                bottomBarModel = BottomBarModel(
                    buttonTitle = "Start Timer",
                    buttonTime = "60 Min",
                    buttonStyle = ButtonStyle.Start,
                    buttonEnable = true,
                    subButtonTitle = "Options"
                )
            ),
            showRequestPermissionDialog = false,
            actionListener = object : AdditionTimerScreenActionListener {

            override fun newAdditionHopsTextChanged(text: String) {

            }

            override fun newAdditionDurationTextChanged(text: String) {

            }
            override fun addAdditionClick() { }
            override fun onOptionClick(
                rowModel: RowModel, optionType: AdditionOptionType,
            ) { }
            override fun onAlertRowCheckChanged(checked: Boolean, alertId: String) { }
            override fun onThemeIconClick(isSystemInDarkTheme: Boolean) { }
            override fun startTimerButtonClick() {

            }

            override fun onSubButtonClick() {

            }

            override fun onPermissionResult(granted: Boolean) {

            }

        })
    }
}
