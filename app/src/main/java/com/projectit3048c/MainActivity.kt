package com.projectit3048c

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.projectit3048c.dto.*
import com.projectit3048c.ss23.R
import com.projectit3048c.ss23.ui.theme.Orange
import com.projectit3048c.ss23.ui.theme.ProjectIT3048CTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : FragmentActivity(){

    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var selectedFood: Food? = null
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var inFoodName: String = ""
    private var strUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { viewModel.fetchFoods()
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToFoodSpecimens()
            }
            val foods by viewModel.foods.observeAsState(initial = emptyList())
            val foodAmounts by viewModel.foodAmounts.observeAsState(initial = emptyList())
            ProjectIT3048CTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                    CalorieFacts("Android", foods, foodAmounts, viewModel.selectedFoodAmount)
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Food>, label : String = "", selectedFoodAmount: FoodAmount = FoodAmount()) {
        val dropDownOptions = remember { mutableStateOf(listOf<Food>()) }
        val textFieldValue = remember(selectedFoodAmount.foodId) { mutableStateOf(TextFieldValue(selectedFoodAmount.foodName)) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inFoodName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(3)
        }

        TextFieldWithDropdown(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
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
    fun FoodAmountSpinner(foodAmountList: List<FoodAmount>) {
        var specimenText by remember { mutableStateOf("Logged Foods") }
        var expanded by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier
                .padding(15.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = specimenText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    foodAmountList.forEach {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            if (it.foodName == viewModel.NEW_FOODAMOUNT) {
                                specimenText = ""
                                it.foodName = ""
                                it.foodAmount = "0"
                            } else {
                                specimenText = it.toString()
                                selectedFood = Food(name = "", description = "", calories = "")
                                inFoodName = it.foodName
                            }
                            viewModel.selectedFoodAmount = it
                            if(it.foodName != "") {
                                viewModel.selectedFoodAmount = it
                                viewModel.fetchPhotos()
                            } else {
                                viewModel.selectedFoodAmount = it
                                viewModel.deletPhotosForNewFood()
                            }
                        }) {
                            Text(text = it.toString())
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CircleProgressBar(
        percentage: Int,
        number: Int,
        fontSize: TextUnit = 20.sp,
        radius: Dp = 80.dp,
        animDuration: Int = 1000,
        animDelay: Int = 0
    ) {
        var animationPlayed by remember {
            mutableStateOf(false)
        }
        val curPercentage = animateFloatAsState(
            targetValue = (if (animationPlayed) percentage else 0).toFloat(),
            animationSpec = tween(
                durationMillis = animDuration,
                delayMillis = animDelay
            )
        )
        LaunchedEffect(key1 = true) {
            animationPlayed = true
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(radius * 2f)
        ) {
            Canvas(modifier = Modifier.size(radius * 2f)
            ) {
                drawArc(
                    color = Color.LightGray,
                    -90f,
                    360 * (viewModel.totalCalories / 2000.toFloat()),
                    useCenter = false,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
            }
            Text(
                text = "${viewModel.totalCalories} / $percentage",
                color = Color.DarkGray,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun CalorieFacts(
        name: String,
        foods: List<Food> = ArrayList<Food>(),
        loggedFoods: List<FoodAmount> = ArrayList<FoodAmount>(),
        selectedFoodAmount: FoodAmount = FoodAmount(),
        selectedFood: Food = Food(),
    ) {
        var inIntake by remember(selectedFoodAmount.foodId) { mutableStateOf(selectedFoodAmount.foodIntake) }
        var inAmount by remember(selectedFoodAmount.foodId) { mutableStateOf(selectedFoodAmount.foodAmount) }
        var pickedDate by remember { mutableStateOf(LocalDate.now()) }
        val context = LocalContext.current

        Column {
            DatePickerCalendar()
            FoodAmountSpinner(foodAmountList = loggedFoods)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleProgressBar(2400, viewModel.totalCalories)
            }
            TextFieldWithDropdownUsage(
                dataIn = foods,
                label = stringResource(R.string.foodName),
                selectedFoodAmount = selectedFoodAmount
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = inIntake,
                        onValueChange = { inIntake = it },
                        label = { Text(stringResource(R.string.intake)) },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TextField(
                        value = inAmount,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty()) {
                                inAmount = "0"
                            } else {
                                inAmount = newValue.toString()
                            }
                        },
                        label = { Text(stringResource(R.string.Calories)) },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(8.dp)
                            .weight(0.5f),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
               if(firebaseUser != null ) {
                   Button(
                       onClick = {
                           if (inFoodName != "") {
                               selectedFoodAmount.apply {
                                   foodName = inFoodName
                                   internalFoodID = selectedFood?.id ?: 0
                                   foodAmount = inAmount
                                   foodIntake = inIntake
                                   foodDate = pickedDate.toString()
                               }
                               viewModel.saveFoodAmount()
                               firebaseUser?.let {
                                   Toast.makeText(
                                       context,
                                       "$inFoodName $inAmount $inIntake",
                                       Toast.LENGTH_LONG
                                   ).show()
                               } ?: Toast.makeText(
                                   context,
                                   "Please Login",
                                   Toast.LENGTH_LONG
                               ).show()
                           } else {
                               Toast.makeText(
                                   context,
                                   "Empty fields",
                                   Toast.LENGTH_LONG
                               ).show()
                           }
                       },
                       modifier = Modifier.weight(1f)
                   ) {
                       Text(text = "Add")
                   }
                   Spacer(modifier = Modifier.width(14.dp))
                   Button(
                       onClick = {
                           takePhoto()
                       },
                       modifier = Modifier.weight(1f)
                   ) {
                       Text(text = "Photo")
                   }
                   Spacer(modifier = Modifier.width(14.dp))
                   Button(
                       onClick = {
                           AuthUI.getInstance().signOut(context)
                               .addOnCompleteListener {
                               }
                           Toast.makeText(
                               context,
                               "You are Logged Out",
                               Toast.LENGTH_LONG
                           ).show()
                       },
                       modifier = Modifier.weight(1f)
                   ) {
                       Text(text = "Logout")
                   }
               } else {
                   Spacer(modifier = Modifier.width(14.dp).padding(8.dp))
                   Button(
                       onClick = {
                           signIn()
                       },
                       modifier = Modifier.weight(1f)
                   ) {
                       Text(text = "Login")
                   }
               }
            }
            Events()
        }
    }

    private @Composable
    fun DatePickerCalendar() {
        var pickedDate by remember { mutableStateOf(LocalDate.now()) }
        val formattedDate by remember { derivedStateOf { DateTimeFormatter.ofPattern("MMM dd YYYY").format(pickedDate) }}
        val dateDialogState = rememberMaterialDialogState()
        Button(
            onClick = {
                dateDialogState.show()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.DarkGray
            ),
            modifier = Modifier
                .border(
                    BorderStroke(width = 2.dp, color = Color.LightGray),
                )
                .height(50.dp)
                .fillMaxWidth()
        )
        {
            Text(text = formattedDate)
        }
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(
                        applicationContext,
                        "CLicked ok",
                        Toast.LENGTH_LONG
                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
                colors = DatePickerDefaults.colors(Orange),
            ) {
                pickedDate = it
                viewModel.selectedDate = it
                viewModel.listenToFoodSpecimens()
            }
        }
    }

    @Composable
    private fun Events () {
        val photos by viewModel.eventPhotos.observeAsState(initial = emptyList())
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items (
                items = photos,
                itemContent = {
                    EventListItem(photo = it)
                }
            )
        }
    }

    @Composable
    fun EventListItem(photo: Photo){
        var inDescription by remember(photo.id) {mutableStateOf(photo.description)}
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = contentColorFor(backgroundColor),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        )
        {
            Row {
                Column(Modifier.weight(2f)) {
                    AsyncImage(
                        model = photo.localUri, contentDescription = "Event Image",
                        Modifier
                            .width(120.dp)
                            .height(130.dp)
                            .padding(15.dp)
                    )
                }
                Column(Modifier.weight(3.5f).padding(5.dp)) {
                    Text(text = inFoodName, style = typography.h6)
                    Text(text = photo.dateTaken.toString(), style = typography.caption)
                    OutlinedTextField(
                        value = inDescription,
                        onValueChange = { inDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(Modifier.weight(1.1f).padding(5.dp)) {
                    Button(
                        onClick = {
                            photo.description = inDescription
                            save(photo)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save",
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                    Button(
                        onClick = {
                            delete(photo)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            }
        }
    }

    private fun delete(photo: Photo) {
        viewModel.delete(photo)
    }

    private fun save(photo: Photo) {
        viewModel.updatePhotoDatabase(photo)
    }

    private fun takePhoto() {
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
            invokeCamera()
        } else {
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        } else {
            Toast.makeText(this, "Unable to load camera without permission.", Toast.LENGTH_LONG).show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.projectit3048c.ss23.fileprovider", file)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "loggedFood_${timestamp}",
            ".jpg",
                imageDirectory
        ).apply{
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        success ->
        if (success) {
            Log.i(TAG, "Image Location: $uri")
            strUri = uri.toString()
            val photo = Photo(localUri = uri.toString())
            viewModel.photos.add(photo)
        } else {
            Log.e(TAG, "Image not saved. $uri")
        }
    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signinIntent)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                viewModel.listenToFoodSpecimens()
            }
        } else {
            Log.e("MainActivity.ky", "Error logging in" + response?.error?.errorCode)
        }
    }

    @Preview(name = "Light Mode", showBackground = true)
    @Composable
    fun DefaultPreview() {
        ProjectIT3048CTheme {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                CalorieFacts("Android")
            }
        }
    }
}