package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.screen.AdditionTimerScreenConfig
import ca.arnaud.hopsboilingtimer.app.view.TransparentTextField

@Composable
fun AddNewAddition(
    newAdditionHopsTextChanged: (String) -> Unit,
    newAdditionDurationTextChanged: (String) -> Unit,
    addAdditionClick: () -> Unit,
    title: () -> String,
    duration: () -> String,
    buttonEnabled: () -> Boolean,
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
            val buttonAction = {
                addAdditionClick()
                focusRequester.requestFocus()
            }

            TitleTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                title = title,
                onValueChange = newAdditionHopsTextChanged,
            )

            Spacer(modifier = Modifier.width(10.dp))

            DurationTextField(
                modifier = Modifier.width(80.dp),
                duration = duration,
                onValueChange = newAdditionDurationTextChanged,
                keyboardActions = KeyboardActions(
                    onDone = { buttonAction() }
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .height(40.dp),
                onClick = buttonAction,
                enabled = buttonEnabled(),
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
private fun TitleTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    title: () -> String,
) {
    TransparentTextField(
        modifier = modifier,
        value = title,
        label = { Text("Hops") },
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun DurationTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    duration: () -> String,
) {
    TransparentTextField(
        modifier = modifier,
        value = duration,
        label = { Text("Min") },
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
    )
}