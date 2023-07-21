package ca.arnaud.hopsboilingtimer.app.feature.common.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonStyle
import ca.arnaud.hopsboilingtimer.app.theme.LocalAppColors

@Composable
fun TimeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    model: TimeButtonModel,
    time: () -> String?,
) {
    val backgroundColor = when (model.style) {
        TimeButtonStyle.Start -> LocalAppColors.current.primary
        TimeButtonStyle.Stop -> LocalAppColors.current.secondary
    }

    Button(
        modifier = modifier,
        enabled = model.enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = model.title,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )

            TimeText(
                text = model.time,
                time = time,
            )
        }
    }
}

@Composable
private fun TimeText(text: String, time: () -> String?) {
    Text(
        text = time() ?: text,
        maxLines = 1,
        style = MaterialTheme.typography.bodyMedium
    )
}