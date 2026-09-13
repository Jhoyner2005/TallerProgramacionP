package com.example.tallerpp.composables
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsHockey
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tallerpp.R



@Composable
fun HomeScreen(
    onClickGame: () -> Unit
) {

    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E3A8A),
            Color(0xFF2563EB)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Icon(Icon top of tittle)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.home_title),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ready for the next match?",
                    color = Color.White.copy(alpha = .8f),
                    fontSize = 16.sp
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = .12f)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.home_score),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }

            Button(
                onClick = onClickGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(10.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00AEFF)
                )
            ) {

                Text(
                    text = stringResource(R.string.home_btn_play),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Components 301•2026 • Hockey Game",
                color = Color.White.copy(alpha = .6f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}