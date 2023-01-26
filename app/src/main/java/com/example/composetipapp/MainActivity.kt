package com.example.composetipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composetipapp.ui.theme.ComposeTipAppTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTipAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    TipAppScreen()
                }
            }
        }
    }
}

@Composable
fun EditNumberField(
    @StringRes labelStringResourceId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions
) {

    TextField(
        value = value, onValueChange = onValueChange, label = {
            Text(
                text = stringResource(id = labelStringResourceId), Modifier.fillMaxWidth()
            )
        }, keyboardOptions = keyboardOptions, keyboardActions = keyboardActions, singleLine = true
    )
}


@Composable
fun TipAppScreen() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var roundUp by remember { mutableStateOf(false) }
    var tip = calculateTip(amount, tipPercent,roundUp)
    val focusManager = LocalFocusManager.current
    Column(
        Modifier.padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            value = amountInput,
            labelStringResourceId = R.string.bill_amount,
            onValueChange = { amountInput = it },
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            value = tipInput,
            labelStringResourceId = R.string.tip_percent,
            onValueChange = {
                tipInput = it
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.tip_amount, tip),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        RoundTheTipRow(roundUp = roundUp, onRoundUpChange = { roundUp = it })

    }
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChange,
            modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.Blue,
                uncheckedTrackColor = Color.Red,
                checkedThumbColor = Color.Red,
                checkedTrackColor = Color.Blue
            )
        )
    }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp){
        tip = ceil(tip)
    }

    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ComposeTipAppTheme {
        TipAppScreen()
    }
}