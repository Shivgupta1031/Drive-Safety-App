package com.devshiv.drivesafetyapp.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devshiv.drivesafetyapp.MainActivity
import com.devshiv.drivesafetyapp.R
import com.devshiv.drivesafetyapp.ui.theme.AccentColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryLightColor
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.Dialog
import com.devshiv.drivesafetyapp.utils.LoadingDialog
import com.devshiv.drivesafetyapp.viewmodels.EditProfileViewModel
import com.devshiv.drivesafetyapp.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    prevPage: String,
    number: String,
    onNavigateRequired: (screen: String) -> Unit
) {

    val viewModel: EditProfileViewModel = hiltViewModel()

    var nameError by remember {
        mutableStateOf("")
    }
    var ageError by remember {
        mutableStateOf("")
    }
    var vehicleError by remember {
        mutableStateOf("")
    }

    var nameET by remember {
        mutableStateOf("")
    }
    var ageET by remember {
        mutableStateOf("")
    }
    var vehicleET by remember {
        mutableStateOf("")
    }

    val loadingState by viewModel.loadingState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    val responseState by viewModel.responseState.collectAsState()

    val context = LocalContext.current as MainActivity

    LaunchedEffect(key1 = responseState, block = {
        if (responseState) {
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

        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.BottomCenter,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, top = 18.dp),
                text = "CREATE ACCOUNT!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(40.dp))

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
                    value = nameET,
                    onValueChange = { newText ->
                        nameET = newText
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
                            text = "Full Name",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    isError = nameError.isNotEmpty(),
                    supportingText = {
                        if (nameError.isNotEmpty()) {
                            Text(
                                text = nameError,
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
                    value = ageET,
                    onValueChange = { newText ->
                        ageET = newText
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
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PrimaryLightColor,
                        textColor = Color.White,
                    ),

                    label = {
                        Text(
                            text = "Age",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    isError = ageError.isNotEmpty(),
                    supportingText = {
                        if (ageError.isNotEmpty()) {
                            Text(
                                text = ageError,
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
                    value = vehicleET,
                    onValueChange = { newText ->
                        vehicleET = newText
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
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PrimaryLightColor,
                        textColor = Color.White,
                    ),

                    label = {
                        Text(
                            text = "Vehicle In Use",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    isError = vehicleError.isNotEmpty(),
                    supportingText = {
                        if (vehicleError.isNotEmpty()) {
                            Text(
                                text = vehicleError,
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
                    if (!viewModel.validate(nameET)) {
                        nameError = "* Required"
                    } else if (!viewModel.validate(ageET)) {
                        ageError = "* Required"
                    } else if (!viewModel.validate(vehicleET)) {
                        vehicleError = "* Required"
                    } else {
                        nameError = ""
                        ageError = ""
                        vehicleError = ""
                        viewModel.createAccount(number, nameET, ageET, vehicleET)
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
                    text = "Continue",
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