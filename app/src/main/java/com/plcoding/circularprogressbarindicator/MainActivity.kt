package com.plcoding.circularprogressbarindicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.plcoding.circularprogressbarindicator.ui.theme.CircularProgressBarIndicatorTheme
import com.plcoding.circularprogressbarindicator.ui.theme.gray
import com.plcoding.circularprogressbarindicator.ui.theme.nightDark
import com.plcoding.circularprogressbarindicator.ui.theme.redOrange


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircularProgressBarIndicatorTheme {
                  CustomCircularProgressIndicator(

                    modifier = Modifier
                        .fillMaxSize()
                        .background(nightDark),

                    percentage = 100f,
                    initialValue = 0,
                    primaryColor = redOrange,
                    secondaryColor = gray,
                    circleRadius = 230f,
                )
            }
        }
    }
}