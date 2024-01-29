package com.devshiv.drivesafetyapp.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.devshiv.drivesafetyapp.MainActivity
import com.devshiv.drivesafetyapp.R
import com.devshiv.drivesafetyapp.ui.theme.AccentColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryDarkLightColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryLightColor
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.Dialog
import com.devshiv.drivesafetyapp.utils.LoadingDialog
import com.devshiv.drivesafetyapp.viewmodels.RAViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    prevPage: String,
    number: String,
    onNavigateRequired: (screen: String) -> Unit
) {

    val viewModel: RAViewModel = hiltViewModel()

    var locationError by remember {
        mutableStateOf("")
    }
    var detailsError by remember {
        mutableStateOf("")
    }
    var locationET by remember {
        mutableStateOf("")
    }
    var detailsET by remember {
        mutableStateOf("")
    }

    val loadingState by viewModel.loadingState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    val responseState by viewModel.response.collectAsState()

    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            pickedImageUri = uri
        }
    )

    val context = LocalContext.current as MainActivity

    LaunchedEffect(key1 = responseState, block = {
        if (responseState) {
            Toast.makeText(context, "Accident Reported Successfully", Toast.LENGTH_SHORT).show()
            onNavigateRequired(Constants.HOME_NAV)
        }
    })

    BackHandler(enabled = true) {
        if (prevPage == Constants.LOGIN_NAV) {
            onNavigateRequired("$prevPage/false")
        } else {
            onNavigateRequired(prevPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, top = 18.dp),
                text = "Report An Accident!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.car_accident),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = PrimaryLightColor)
                    .shadow(10.dp, ambientColor = Color.Black)
                    .align(Alignment.CenterHorizontally)
                    .animateContentSize(
                        tween(500)
                    )
            ) {
                TextField(
                    value = locationET,
                    onValueChange = { newText ->
                        locationET = newText
                    },
                    modifier = Modifier
                        .background(color = PrimaryLightColor)
                        .fillMaxWidth(),

                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        color = Color.White
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PrimaryLightColor,
                        textColor = Color.White,
                    ),

                    label = {
                        Text(
                            text = "Accident Location",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    isError = locationError.isNotEmpty(),
                    supportingText = {
                        if (locationError.isNotEmpty()) {
                            Text(
                                text = locationError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily(Font(R.font.main_font)),
                                modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                            )
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = PrimaryLightColor)
                    .shadow(10.dp, ambientColor = Color.Black)
                    .align(Alignment.CenterHorizontally)
                    .animateContentSize(
                        tween(500)
                    )
            ) {
                TextField(
                    value = detailsET,
                    onValueChange = { newText ->
                        detailsET = newText
                    },
                    modifier = Modifier
                        .background(color = PrimaryLightColor)
                        .fillMaxWidth()
                        .height(100.dp),

                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        color = Color.White
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PrimaryLightColor,
                        textColor = Color.White,
                    ),

                    label = {
                        Text(
                            text = "More Details",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    isError = detailsError.isNotEmpty(),
                    supportingText = {
                        if (detailsError.isNotEmpty()) {
                            Text(
                                text = detailsError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily(Font(R.font.main_font)),
                                modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                            )
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    pickImageLauncher.launch("image/*")
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryLightColor
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues()
            ) {
                Row {
                    Text(
                        text = "Pick Image",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Image",
                        tint = Color.White
                    )

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            pickedImageUri?.let { uri ->
                val painter = rememberImagePainter(uri)

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (!viewModel.validate(locationET)) {
                        locationError = "* Required"
                    } else if (!viewModel.validate(detailsET)) {
                        detailsError = "* Required"
                    } else if (pickedImageUri == null) {
                        Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show()
                    } else {
                        locationError = ""
                        detailsError = ""
                        viewModel.reportAccident(number, detailsET, locationET, pickedImageUri!!)
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = "Send",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }

    LoadingDialog(showLoading = loadingState)

    if (dialogState.showToast) {
        Toast.makeText(context, dialogState.description, Toast.LENGTH_SHORT).show()
        viewModel.dismissDialog()
    } else {
        Dialog(showDialog = dialogState.showDialog,
            title = dialogState.title,
            description = dialogState.description,
            onClick = {
                if (dialogState.success) {
                    onNavigateRequired(Constants.HOME_NAV)
                }
                viewModel.dismissDialog()
            })
    }
}