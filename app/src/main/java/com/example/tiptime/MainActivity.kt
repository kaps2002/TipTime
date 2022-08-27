package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import androidx.compose.material.TextField as TextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen(){
    var amountinput by remember{mutableStateOf("")}
    val amount =amountinput.toDoubleOrNull()?:0.0
    var tipinput by remember{ mutableStateOf("")}
    val tipercent = tipinput.toDoubleOrNull()?:0.0
    var focusM= LocalFocusManager.current
    var roundUp by remember { mutableStateOf(false) }
    val tip= calculatetip(amount,tipercent, roundUp)
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(R.string.calculate_tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            label= R.string.Bill_amount,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value=amountinput,
            onvaluechange={amountinput=it},
            keyboardOptions=KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardAction = KeyboardActions(onNext = {focusM.moveFocus(FocusDirection.Down)})


        )
        EditNumberField(
            label= R.string.Tipc,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value=tipinput,
            onvaluechange={tipinput=it},
            keyboardOptions=KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardAction = KeyboardActions(onDone = {focusM.clearFocus()})

            )

        RoundTip(roundUp = roundUp, onRoundUpChange = {roundUp=it})
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(id = R.string.tip_amount,tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun EditNumberField(modifier: Modifier=Modifier,
                    @StringRes label:Int,
                    value: String,
                    onvaluechange:(String)-> Unit,
                    keyboardOptions: KeyboardOptions,
                    keyboardAction:  KeyboardActions
                    ){

                        TextField(value = value,
                            onValueChange = onvaluechange,
                            label={
                                  Text(
                                      text = stringResource(id= label),
                                      modifier = Modifier.fillMaxWidth()
                                  )
                            },
                            keyboardOptions = keyboardOptions,
                            keyboardActions = keyboardAction,

                            singleLine= true
                        )
}
@Composable
fun RoundTip(
    modifier: Modifier=Modifier,
    roundUp:Boolean,
    onRoundUpChange: (Boolean) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = stringResource(R.string.Round))
        Switch(checked = roundUp,
            onCheckedChange = onRoundUpChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
        )
    }
}
private fun calculatetip(amount: Double, tip:Double=15.0 ,roundUp: Boolean): String {
    var result = tip/100 *amount
    if(roundUp){
        result =kotlin.math.ceil(result)
    }
    return NumberFormat.getCurrencyInstance().format(result)
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}