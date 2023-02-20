package com.projectit3048c.ss23

import MainViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.projectit3048c.ss23.ui.theme.ProjectIT3048CTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectIT3048CTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                    CalorieFacts("Android")
                }
            }
        }
    }
}

@Composable
fun CalorieFacts(name:String) {
    var foodItem by remember { mutableStateOf("")}
    var itemCalorie by remember {mutableStateOf("")}
    var itemServing by remember {mutableStateOf("")}
    val context = LocalContext.current
    Column {
        OutlinedTextField(
            value = foodItem,
            onValueChange = { foodItem = it },
            label = { Text(stringResource(R.string.foodItem)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = itemCalorie,
            onValueChange = { itemCalorie = it },
            label = { Text(stringResource(R.string.itemCalorie)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = itemServing,
            onValueChange = { itemServing = it },
            label = { Text(stringResource(R.string.itemServing)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                Toast.makeText(context, "$foodItem $itemCalorie $itemServing", Toast.LENGTH_LONG).show()
            }
        ){
            Text(text = "Add")
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