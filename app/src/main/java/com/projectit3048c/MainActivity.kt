package com.projectit3048c

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodItems
import com.projectit3048c.dto.FoodAmount
import com.projectit3048c.ss23.R
import com.projectit3048c.ss23.ui.theme.ProjectIT3048CTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private var selectedFoodAmount: FoodAmount? = null
    private var selectedFood: Food? = null
    private val viewModel : MainViewModel by viewModel<MainViewModel>()
    private var inFoodName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchFoods()
            val foods by viewModel.foods.observeAsState(initial = emptyList())
            ProjectIT3048CTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.background) {
                    CalorieFacts("Android", foods)
                }
                var foo = foods
                var i = 1 + 1
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Food>?, label : String = "", take :Int = 3) {

        val dropDownOptions = remember { mutableStateOf(listOf<Food>()) }
        val textFieldValue = remember {mutableStateOf(TextFieldValue()) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inFoodName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn?.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }!!.take(take)
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }


    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Food>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                        selectedFood = text
                    }) {
                        Text(text = text.toString())
                    }
                }
            }
        }
    }

    @Composable
    fun CalorieFacts(name:String, foods: List<Food> = ArrayList<Food>(), loggedFoods: List<FoodAmount> = ArrayList<FoodAmount>()) {
        var inIntake by remember { mutableStateOf("") }
        var inLogged by remember { mutableStateOf("") }
        var inAmount by remember { mutableStateOf("") }
        val context = LocalContext.current
        Column {
            FoodAmountSpinner(loggedFoods = loggedFoods)
            TextFieldWithDropdownUsage(dataIn = foods, stringResource(R.string.foodName))
            OutlinedTextField(
                value = inIntake,
                onValueChange = { inIntake = it },
                label = { Text(stringResource(R.string.foodIntake)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inLogged,
                onValueChange = { inLogged = it },
                label = { Text(stringResource(R.string.foodLoged)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inAmount,
                onValueChange = { inAmount = it },
                label = { Text(stringResource(R.string.foodAmount)) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    var specimen = FoodAmount().apply {
                        foodName = inFoodName
                        foodId = selectedFood?.let(){
                            it.id
                        } ?: 0
                        foodAmount = inAmount
                        foodIntake = inIntake
                        foodLogged = inLogged
                    }
                    Toast.makeText(
                        context,
                        "$inFoodName $inAmount $inIntake $inLogged",
                        Toast.LENGTH_LONG)
                        .show()
                }
            ) {
                Text(text = "Add")
            }
        }

        fun delete(foodItems: FoodItems) {
            //  ViewModel.deleteSavedFoodDatabase(foodItems)
        }

        @Composable
        fun EventListItem(foodItems: FoodItems){
            Row {
                Column(Modifier.weight(6f)) {
                    Text(text = foodItems.fdcId, style=typography.h6)
                    Text(text = foodItems.description, style=typography.caption)
                }
                Column(Modifier.weight(1f)) {
                    Button (
                        onClick = {delete(foodItems)}
                    ){
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
    @Preview(name="Light Mode", showBackground = true)
    @Composable
    fun DefaultPreview() {
        ProjectIT3048CTheme {
            Surface(color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()) {
                CalorieFacts("Android")
            }
        }
    }

    @Composable
    fun FoodAmountSpinner (loggedFoods: List<FoodAmount>) {
        var loggedFoodText by remember { mutableStateOf("Logged Food Collection") }
        var expanded by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = loggedFoodText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                    loggedFoods.forEach {
                        loggedFood -> DropdownMenuItem(onClick = {
                            expanded = false
                        loggedFoodText = loggedFood.toString()
                        selectedFoodAmount = loggedFood
                    }) {
                            Text(text = loggedFood.toString())
                    }
                    }
                }
            }
        }
    }
}

