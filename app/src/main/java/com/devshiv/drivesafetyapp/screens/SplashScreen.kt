package com.devshiv.drivesafetyapp.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devshiv.drivesafetyapp.R
import com.devshiv.drivesafetyapp.ui.theme.PrimaryColor
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.viewmodels.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateRequired: (screen: String) -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.car_driving))

    val offsetX = remember { Animatable(initialValue = -1000f) }
    val offsetY = remember { Animatable(initialValue = 800f) }

    LaunchedEffect(Unit) {
        animateOffsetY(offsetY, targetOffsetY = 0f, 300)
        animateOffsetX(offsetX, targetOffsetX = 0f, 400)
        delay(1800L)
        viewModel.checkUserLoginStatus().collect {
            if (it) {
                onNavigateRequired(Constants.HOME_NAV)
            } else {
                onNavigateRequired(Constants.LOGIN_NAV + "/false")
            }
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
                .offset(y = with(LocalDensity.current) { offsetY.value.toDp() })
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter),
            alignment = Alignment.BottomCenter,
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .padding(bottom = 140.dp)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .offset(x = with(LocalDensity.current) { offsetX.value.toDp() })
                    .fillMaxWidth()
                    .height(200.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashPreview() {
    SplashScreen(onNavigateRequired = {})
}

private suspend fun animateOffsetX(
    offsetX: Animatable<Float, AnimationVector1D>,
    targetOffsetX: Float,
    time: Int
) {
    offsetX.animateTo(
        targetOffsetX,
        animationSpec = tween(durationMillis = time, easing = LinearEasing)
    )
}

private suspend fun animateOffsetY(
    offsetY: Animatable<Float, AnimationVector1D>,
    targetOffsetY: Float,
    time: Int
) {
    offsetY.animateTo(
        targetOffsetY,
        animationSpec = tween(durationMillis = time, easing = LinearEasing)
    )
}