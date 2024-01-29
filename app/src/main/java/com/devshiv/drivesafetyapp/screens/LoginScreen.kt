package com.devshiv.drivesafetyapp.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.platform.LocalDensity
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
import com.devshiv.drivesafetyapp.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(logout: Boolean, onNavigateRequired: (screen: String) -> Unit) {

    val viewModel: LoginViewModel = hiltViewModel()

    var numberError by remember {
        mutableStateOf("")
    }

    var otpError by remember {
        mutableStateOf("")
    }

    var numberET by remember {
        mutableStateOf("")
    }
    var otpET by remember {
        mutableStateOf("")
    }

    val loadingState by viewModel.loadingState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    val otpSent by viewModel.otpSentState.collectAsState()
    val userFoundState by viewModel.userFoundState.collectAsState()

    val context = LocalContext.current as MainActivity
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.login_anim))

    BackHandler(enabled = true) {
        context.finish()
    }

    LaunchedEffect(key1 = true, block = {
        if (logout) {
            viewModel.deleteAllData()
        }
    })

    LaunchedEffect(key1 = userFoundState, block = {
        if (userFoundState == 1) {
            onNavigateRequired(Constants.HOME_NAV)
        } else if (userFoundState == 2) {
            onNavigateRequired("${Constants.EDIT_PROFILE_NAV}/${Constants.LOGIN_NAV}/$numberET")
        }
    })

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
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 34.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(40.dp))

            LottieAnimation(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally),
                composition = composition,
                iterations = LottieConstants.IterateForever,
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
                    value = numberET,
                    onValueChange = { newText ->
                        numberET = newText
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
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PrimaryLightColor,
                        textColor = Color.White,
                    ),

                    label = {
                        Text(
                            text = "Phone Number",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            color = Color.White
                        )
                    },
                    enabled = !otpSent,
                    isError = numberError.isNotEmpty(),
                    supportingText = {
                        if (numberError.isNotEmpty()) {
                            Text(
                                text = numberError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily(Font(R.font.main_font)),
                                modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 2.dp)
                            )
                        }
                    },
                )
            }

            if (otpSent) {
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
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing
                            )
                        )
                ) {
                    TextField(
                        value = otpET,
                        onValueChange = { newText ->
                            otpET = newText
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
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = PrimaryLightColor,
                            textColor = Color.White,
                        ),

                        label = {
                            Text(
                                text = "Enter OTP",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.main_font)),
                                color = Color.White
                            )
                        },
                        enabled = otpSent,
                        isError = otpError.isNotEmpty(),
                        supportingText = {
                            if (otpError.isNotEmpty()) {
                                Text(
                                    text = otpError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily(Font(R.font.main_font)),
                                    modifier = Modifier.padding(
                                        start = 6.dp,
                                        top = 0.dp,
                                        bottom = 2.dp
                                    )
                                )
                            }
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!otpSent) {
                Button(
                    onClick = {
                        if (viewModel.validateNumber(numberET)) {
                            numberError = ""
                            viewModel.requestOtp(context, numberET)
                        } else {
                            numberError = "Invalid Number"
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
                        text = "Get OTP",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .wrapContentSize(Alignment.Center)
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing
                            )
                        )
                ) {

                    IconButton(
                        onClick = {
                            viewModel.resetOtp()
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .background(color = AccentColor, shape = CircleShape),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = Color.White,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (viewModel.validateOTP(otpET)) {
                                otpError = ""
                                viewModel.verifyOtp(otpET)
                            } else {
                                otpError = "* Required"
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .width(180.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(),
                    ) {
                        Text(
                            text = "Verify",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
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