package ca.arnaud.hopsboilingtimer.app.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.arnaud.hopsboilingtimer.app.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
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
        model
    )
}

@Composable
private fun MainScreen(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    startTimerButtonClick: () -> Unit,
    model: MainScreenModel
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
        },
        bottomBar = {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    // TODO - setup dimension
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    onClick = startTimerButtonClick
                ) {
                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Start Timer"
                        )
                        Text("60 Min")
                    }
                }
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            content = {
                model.additionRows.forEach { rowModel ->
                    AdditionRow(model = rowModel)
                }

                AddNewAddition(
                    newAdditionHopsTextChanged,
                    newAdditionDurationTextChanged,
                    addAdditionClick,
                    model.newAdditionRow
                )
            })
    }
}

@Composable
fun AddNewAddition(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    model: AdditionRowModel
) {
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
                    .width(40.dp),
                onClick = addAdditionClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun AdditionRow(model: AdditionRowModel) {
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

//        Spacer(modifier = Modifier.width(10.dp))
//        Icon(Icons.Filled.Delete, "")
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
                            AdditionRowModel("Amarillo", "60"),
                            AdditionRowModel("Mozaic", "45"),
                            AdditionRowModel("Saaz", "5"),
                            AdditionRowModel("El Dorado", "10"),
                        )
                    )
                )

            override fun newAdditionHopsTextChanged(text: String) {

            }

            override fun newAdditionDurationTextChanged(text: String) {

            }

            override fun addAdditionClick() {

            }

            override fun startTimerButtonClick() {

            }

        })
    }
}
