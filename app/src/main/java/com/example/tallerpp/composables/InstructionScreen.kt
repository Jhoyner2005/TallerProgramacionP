package com.example.tallerpp.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tallerpp.R

@Composable
fun InstructionScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {

        // clouds
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // left cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 28f,
                center = Offset(
                    size.width * 0.15f,
                    size.height * 0.12f
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 38f,
                center = Offset(
                    size.width * 0.22f,
                    size.height * 0.11f
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 26f,
                center = Offset(
                    size.width * 0.29f,
                    size.height * 0.13f
                )
            )

            // right cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 26f,
                center = Offset(
                    size.width * 0.72f,
                    size.height * 0.09f
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 36f,
                center = Offset(
                    size.width * 0.79f,
                    size.height * 0.08f
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 25f,
                center = Offset(
                    size.width * 0.86f,
                    size.height * 0.11f
                )
            )
        }

        // golf course
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 190.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 70.dp,
                        topEnd = 70.dp
                    )
                )
                .background(Color(0xFF4CAF50))
        )

        //  tittle
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 65.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "GOLF",
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "MASTER",
                color = Color(0xFFFFD54F),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // instructions box
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .shadow(
                    10.dp,
                    RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF87CEEB)
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "¿How do you play?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "¡point!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Tilt your phone to choose the shot direction.",
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "¡lance!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Make a forward motion with the phone to throw the ball",
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "¡Make a hole in one!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Try to get the ball in the hole using the fewest shots.",
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Be careful of obstacles.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "¡Good luck!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
            }
        }

        // back button
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-100).dp)
                .padding(18.dp)
                .width(120.dp)
                .height(54.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Text(
                text = stringResource(R.string.home_btn_back),
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

