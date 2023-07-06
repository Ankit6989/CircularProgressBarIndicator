package com.plcoding.circularprogressbarindicator


import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.circularprogressbarindicator.ui.theme.white
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    initialValue: Int,
    primaryColor: Color,
    secondaryColor: Color,
    minValue:Int = 0,
    maxValue:Int = 100,
    circleRadius:Float,
    animDelay: Int = 0,
    animDuration: Int = 5000,
    percentage : Float,


    //onPositionChange:(Int)->Unit //This lambda function is only needed if you want to change the circular indicator in to circular slider.
){
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

   /* var positionValue by remember {
        mutableStateOf(initialValue)
    }*/

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val curPercentage = animateFloatAsState(
        targetValue = if(animationPlayed) percentage else 0f, //
        animationSpec = tween( // animation specification is given
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true){
        animationPlayed = true
    }

    Box(
        modifier = modifier
    ){
        Canvas(modifier = Modifier
            .fillMaxSize()
        ){
            val width = size.width
            val height = size.height
            val circleThickness = width / 35f
            circleCenter = Offset(x = width/2f, y = height/2f)

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        primaryColor.copy(0.45f),
                        secondaryColor.copy(0.15f)
                    )
                ),
                radius = circleRadius,
                center = circleCenter

            )

            drawCircle(
                style = Stroke(
                    width = circleThickness
                ),
                color = secondaryColor,
                radius = circleRadius,
                center = circleCenter
            )


            drawArc(
                color = primaryColor,
                startAngle = 90f,   // this represent the outer circular arc from where to start
                //sweepAngle = (368f/maxValue) * positionValue.toFloat(),  // total area it covered in encircling the inner circle
                sweepAngle = 360 * curPercentage.value,
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = circleRadius * 2f,
                    height = circleRadius * 2f
            ),
                topLeft = Offset(
                    (width - circleRadius * 2f)/2f,
                    (height - circleRadius * 2f)/2f
                )

            )
            val outerRadius = circleRadius + circleThickness/2f
            val gap = 15f //gap between outer circle and circle lines

            for (i in 0 .. (maxValue-minValue)){
                val color = if(i < percentage-minValue) primaryColor else primaryColor.copy(alpha = 0.3f)
                val angleInDegrees = i*360f/(maxValue-minValue).toFloat()
                val angleInRad = angleInDegrees * PI / 180f + PI/2f

                val yGapAdjustment = cos(angleInDegrees * PI / 180f)*gap
                val xGapAdjustment = -sin(angleInDegrees * PI / 180f)*gap

                val start = Offset(
                    x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat()
                )

                val end = Offset(
                    x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRad) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
                )

                rotate(
                    angleInDegrees,
                    pivot = start
                ){
                    drawLine(
                        color = color,
                        start = start,
                        end = end,
                        strokeWidth = 1.dp.toPx()
                    )
                }

            }
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "${(percentage * 1).toInt()}%",
                        circleCenter.x,
                        circleCenter.y + 45.dp.toPx()/3f,
                        Paint().apply {
                            textSize = 38.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = white.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }

        }
    }
}

