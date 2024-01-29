package com.devshiv.drivesafetyapp.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.devshiv.drivesafetyapp.App
import com.devshiv.drivesafetyapp.MainActivity
import com.devshiv.drivesafetyapp.R
import com.devshiv.drivesafetyapp.model.ItemModel
import com.devshiv.drivesafetyapp.ui.theme.AccentColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryDarkColor
import com.devshiv.drivesafetyapp.ui.theme.PrimaryDarkLightColor
import com.devshiv.drivesafetyapp.utils.AutoResizeText
import com.devshiv.drivesafetyapp.utils.Constants
import com.devshiv.drivesafetyapp.utils.FontSizeRange
import com.devshiv.drivesafetyapp.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateRequired: (screen: String) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val context = LocalContext.current as MainActivity

    var selectedTab by remember {
        mutableIntStateOf(0)
    }
    val userData by viewModel.userData.collectAsState()

    val carRoadSafetyTips = listOf(
        "1. Regular Vehicle Maintenance:\n- Ensure your car undergoes regular maintenance checks, including brakes, tires, lights, and fluid levels.\n- Follow the manufacturer's recommended service schedule.",
        "2. Tire Safety:\n- Check tire pressure regularly and maintain it at the recommended levels.\n- Inspect tires for signs of wear and replace them when necessary.\n- Rotate tires as recommended by the vehicle's manual.",
        "3. Brake Maintenance:\n- Have your brakes inspected regularly and replace brake pads as needed.\n- Listen for unusual sounds when applying the brakes and address any issues promptly.",
        "4. Follow Traffic Rules:\n- Obey speed limits and traffic signals.\n- Always use turn signals when changing lanes or making turns.\n- Come to a complete stop at stop signs and red lights.",
        "5. Avoid Distractions:\n- Keep your focus on the road and avoid distractions like texting, talking on the phone, or adjusting the radio while driving.\n- If you need to use your phone, pull over to a safe location.",
        "6. Buckle Up:\n- Always wear your seatbelt, and ensure all passengers do the same.\n- Secure children in appropriate car seats or booster seats.",
        "7. Defensive Driving:\n- Be aware of your surroundings and anticipate the actions of other drivers.\n- Maintain a safe following distance to allow for reaction time.",
        "8. Weather Awareness:\n- Adjust your driving to suit the weather conditions (rain, snow, fog).\n- Increase following distances during adverse weather.",
        "9. Night Driving Precautions:\n- Ensure your headlights, brake lights, and turn signals are functioning properly.\n- Reduce speed and use high beams responsibly to improve visibility.",
        "10. Avoid Driving Under the Influence:\n- Never drive under the influence of alcohol or drugs.\n- If you're impaired, arrange for alternative transportation.",
    )

    val bikeSafetyTips = listOf(
        "1. Helmet Usage:\n- Always wear a helmet to protect your head in case of accidents or falls.",
        "2. Visibility Matters:\n- Use reflective gear and ensure your bike has proper reflectors to enhance visibility, especially at night.",
        "3. Defensive Riding:\n- Anticipate potential risks and ride defensively. Be aware of your surroundings and other road users.",
        "4. Follow Traffic Rules:\n- Obey traffic signals, signs, and rules. Respect traffic laws to ensure a safe ride.",
        "5. Regular Bike Maintenance:\n- Keep your bike in good condition by checking brakes, tires, lights, and other crucial components regularly.",
        "6. Be Predictable:\n- Signal your intentions clearly. Use hand signals to indicate turns or lane changes.",
        "7. Avoid Distractions:\n- Stay focused on the road. Avoid using your phone or engaging in distracting activities while riding.",
        "8. Ride in the Right Direction:\n- Always ride in the same direction as traffic to reduce the risk of accidents.",
        "9. Watch for Road Hazards:\n- Be alert to potential hazards such as potholes, gravel, or wet surfaces. Adjust your speed accordingly.",
        "10. Respect Pedestrians:\n- Yield to pedestrians at crosswalks and intersections. Give them the right of way.",
    )

    val truckSafetyTips = listOf(
        "1. Vehicle Inspection:\n- Conduct thorough pre-trip inspections to check brakes, tires, lights, and overall vehicle condition.",
        "2. Load Securement:\n- Ensure proper load securement to prevent shifting during transit. Use appropriate restraints and tie-downs.",
        "3. Maintain Safe Following Distance:\n- Keep a safe distance from the vehicle in front of you, allowing for ample braking distance.",
        "4. Stay in Designated Lanes:\n- Stick to the designated lanes for trucks, especially on highways. Avoid sudden lane changes.",
        "5. Obey Weight Limits:\n- Adhere to weight limits set for your truck. Overloading can affect stability and braking.",
        "6. Be Mindful of Blind Spots:\n- Trucks have large blind spots. Be aware of other vehicles around you, and use mirrors to check blind spots.",
        "7. Use Turn Signals Early:\n- Signal your intentions well in advance of making turns or changing lanes to give other drivers ample warning.",
        "8. Weather Preparedness:\n- Adjust your driving in adverse weather conditions. Slow down and maintain control of your vehicle.",
        "9. Fatigue Management:\n- Manage fatigue by adhering to driving hour regulations. Take breaks and rest when needed.",
        "10. Emergency Preparedness:\n- Carry emergency equipment, such as a first aid kit and warning triangles. Be prepared for unexpected situations.",
    )

    BackHandler(enabled = true) {
        context.finish()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryDarkColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(PrimaryDarkLightColor),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(40.dp)),
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.6f)
                        .padding(top = 4.dp, start = 6.dp, end = 10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Name : ${userData.name}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Age : ${userData.age}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vehicle : ${userData.vehicle}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.main_font)),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            onLogout()
                        },
                        modifier = Modifier
                            .padding(top = 4.dp, start = 20.dp, end = 20.dp, bottom = 4.dp)
                            .height(30.dp)
                            .align(Alignment.End),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(),
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.main_font)),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }

            }

            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PrimaryDarkLightColor),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable { selectedTab = 0 }
                        .background(
                            if (selectedTab == 0) AccentColor else PrimaryDarkLightColor
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = "Car",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = if (selectedTab == 0) 19.sp else 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(PrimaryDarkColor)
                )

                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable { selectedTab = 1 }
                        .background(
                            if (selectedTab == 1) AccentColor else PrimaryDarkLightColor
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = "Motorbike",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = if (selectedTab == 1) 19.sp else 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(PrimaryDarkColor)
                )

                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable { selectedTab = 2 }
                        .background(
                            if (selectedTab == 2) AccentColor else PrimaryDarkLightColor
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = "Truck",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = if (selectedTab == 2) 19.sp else 16.sp,
                    fontFamily = FontFamily(Font(R.font.main_font)),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                Image(
                    painter = painterResource(
                        id =
                        when (selectedTab) {
                            1 -> R.drawable.bike
                            2 -> R.drawable.truck
                            else -> R.drawable.ic_car
                        }
                    ),
                    contentDescription = "Image",
                    modifier = Modifier
                        .height(110.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.FillHeight
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    when (selectedTab) {
                        1 ->
                            bikeSafetyTips.forEach { tip ->
                                RoadSafetyTip(tip)
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        2 ->
                            truckSafetyTips.forEach { tip ->
                                RoadSafetyTip(tip)
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        else ->
                            carRoadSafetyTips.forEach { tip ->
                                RoadSafetyTip(tip)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                    }
                }

            }

        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("https://colab.research.google.com/drive/13PwfUe5M5P7pYlCpBwaY0YYux0Dtde3U?usp=sharing"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ViewInAr,
                    contentDescription = "View Button",
                    tint = Color.White
                )
            }

            FloatingActionButton(
                onClick = {
                    onNavigateRequired("${Constants.REPORT_ACCIDENT_NAV}/${Constants.HOME_NAV}/${App.curUser}")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Report An Accident",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun RoadSafetyTip(tip: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = tip,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = FontFamily(Font(R.font.main_font)),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = TextUnit.Unspecified,
                color = Color.White,
                textAlign = TextAlign.Left
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    HomeScreen({}, {})
}