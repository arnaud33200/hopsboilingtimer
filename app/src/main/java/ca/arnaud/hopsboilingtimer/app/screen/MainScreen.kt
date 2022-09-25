package ca.arnaud.hopsboilingtimer.app.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.arnaud.hopsboilingtimer.app.model.*
import ca.arnaud.hopsboilingtimer.app.theme.HopsBoilingTimerTheme
import ca.arnaud.hopsboilingtimer.app.theme.Typography
import ca.arnaud.hopsboilingtimer.app.view.TransparentTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MainScreenViewModel {
    val screenModel: StateFlow<MainScreenModel>

    fun newAdditionHopsTextChanged(text: String)
    fun newAdditionDurationTextChanged(text: String)
    fun addAdditionClick()
    fun onOptionClick(rowModel: RowModel, optionType: AdditionOptionType)
    fun onAlertRowCheckChanged(checked: Boolean, alertId: String)

    fun startTimerButtonClick()
}

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val model = viewModel.screenModel.collectAsState().value
    MainScreen(
        viewModel::newAdditionHopsTextChanged,
        viewModel::newAdditionDurationTextChanged,
        viewModel::addAdditionClick,
        viewModel::startTimerButtonClick,
        viewModel::onOptionClick,
        viewModel::onAlertRowCheckChanged,
        model
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    startTimerButtonClick: () -> Unit,
    onOptionClick: (RowModel, AdditionOptionType) -> Unit,
    onAlertRowCheckChanged: (Boolean, String) -> Unit,
    model: MainScreenModel,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    text = "Hops Boiling Timer",
                    style = Typography.h5
                )
            }
        }
    ) { _ ->
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                content = {
                    model.additionRows.forEach { rowModel ->
                        when (rowModel) {
                            is RowModel.AdditionRowModel -> {
                                AdditionRow(model = rowModel, onOptionClick = onOptionClick)
                            }
                            is RowModel.AlertRowModel -> {
                                AlertRow(
                                    model = rowModel,
                                    onAlertRowCheckChanged = onAlertRowCheckChanged
                                )
                            }
                        }

                    }

                    AddNewAddition(
                        newAdditionHopsTextChanged,
                        newAdditionDurationTextChanged,
                        addAdditionClick,
                        model.newAdditionRow
                    )
                })
            BottomBar(startTimerButtonClick, model.bottomBarModel)
        }
    }
}

@Composable
private fun BottomBar(
    startTimerButtonClick: () -> Unit,
    model: BottomBarModel,
) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val backgroundColor = when (model.buttonStyle) {
            ButtonStyle.Start -> MaterialTheme.colors.primary
            ButtonStyle.Stop -> MaterialTheme.colors.secondary
        }

        Button(
            // TODO - setup dimension
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            onClick = startTimerButtonClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor
            )
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = model.buttonTitle
                )
                Text(model.buttonTime)
            }
        }
    }
}

@Composable
fun AddNewAddition(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    model: NewAdditionModel?,
) {
    if (model == null) {
        return
    }
    Column(
        Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
    ) {
        Text(
            "New Addition",
            style = Typography.h6
        )
        Row(
            // TODO - setup dimension in theme
            modifier = Modifier.padding(vertical = 10.dp),
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
                    .height(40.dp)
                    .width(50.dp),
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
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
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
        val textColor = if (model.disabled) Color.Gray else Color.Black
        val fontStyle = if (model.disabled) FontStyle.Italic else null
        val fontWeight = if (model.highlighted) FontWeight.Bold else null
        val textDecoration = if (model.addChecked == true) TextDecoration.LineThrough else null

        Text(
            modifier = Modifier.weight(1f),
            text = model.title,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            textDecoration = textDecoration,
            color = textColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = model.duration,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            textDecoration = textDecoration,
            color = textColor
        )

        model.addChecked?.let { checked ->
            // TODO - will remove the extra paddind
//            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HopsBoilingTimerTheme {
        MainScreen(viewModel = object : MainScreenViewModel {
            override val screenModel: StateFlow<MainScreenModel>
                get() = MutableStateFlow(
                    MainScreenModel(
                        listOf(
                            RowModel.AdditionRowModel("Amarillo", "60"),
                            RowModel.AdditionRowModel("Mozaic", "45"),
                            RowModel.AdditionRowModel("Saaz", "5"),
                            RowModel.AdditionRowModel("El Dorado", "10"),
                        )
                    )
                )

            override fun newAdditionHopsTextChanged(text: String) {

            }

            override fun newAdditionDurationTextChanged(text: String) {

            }

            override fun addAdditionClick() {

            }

            override fun onOptionClick(
                rowModel: RowModel,
                optionType: AdditionOptionType,
            ) {

            }

            override fun onAlertRowCheckChanged(checked: Boolean, alertId: String) {

            }

            override fun startTimerButtonClick() {

            }

        })
    }
}
