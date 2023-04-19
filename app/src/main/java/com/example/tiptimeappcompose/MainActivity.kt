package com.example.tiptimeappcompose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptimeappcompose.ui.theme.TipTimeAppComPoseTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeAppComPoseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   TipTimeAppScreen()
                }
            }
        }
    }
}
@Composable
fun TipTimeAppScreen(){
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember {  mutableStateOf(false) }
       

    val focusManager = LocalFocusManager.current

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent  = tipInput.toDoubleOrNull() ?: 0.0
    val tip = CaculateTip(amount,tipPercent,roundUp)
Column(
    modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
){

Text(text = stringResource(id = R.string.calculate_tip),
fontSize = 24.sp,
modifier = Modifier.align(Alignment.CenterHorizontally))

Spacer( Modifier.height(16.dp))

    EditNumberField(label = R.string.bill_amount,
        keyboardOptions =
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
             focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        value = amountInput,
    onValueChange = {amountInput =it })

    EditNumberField(label = R.string.how_was_the_service,
        keyboardOptions =
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done

        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        value = tipInput, onValueChange = {tipInput =it } )

    Spacer( Modifier.height(24.dp))

    Text(text = stringResource(id = R.string.tip_amount,tip),
     modifier = Modifier.align(Alignment.CenterHorizontally),
         fontSize = 20.sp,
        fontWeight = FontWeight.Bold
           )
    RoundTheTipRow(roundup = roundUp, onRoundupChanged = {
        roundUp =it
    } )
}
}
@Composable
fun EditNumberField(
    @StringRes label :Int,
    value:String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
onValueChange:(String)->Unit
)
{
    TextField(
        value = value,
        onValueChange =  onValueChange ,
        label  ={ Text(stringResource(label))} ,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions ,
        keyboardActions = keyboardActions
    )
}
@Composable
fun RoundTheTipRow(modifier:Modifier = Modifier,
roundup:Boolean,
onRoundupChanged:(Boolean)-> Unit){
Row(modifier = modifier
    .fillMaxWidth()
    .size(48.dp),
verticalAlignment = Alignment.CenterVertically) {
Text(text = stringResource(id = R.string.round_up_tip))
    Switch(checked = roundup, onCheckedChange = onRoundupChanged,
    modifier = modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.End),
    colors = SwitchDefaults.colors(
        uncheckedThumbColor = Color.DarkGray
    ))
}
}
private fun CaculateTip(
    amount:Double,
    percent:Double,
    roundUp: Boolean
) : String{
    var tip = percent/100 * amount
    if(roundUp){
        tip = kotlin.math.ceil(tip)
    }

    return NumberFormat.getNumberInstance().format(tip)
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeAppComPoseTheme {
      TipTimeAppScreen()
    }
}