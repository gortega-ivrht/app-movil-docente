package com.example.sesion01.ui.theme
import com.example.sesion01.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val fuentes = FontFamily(
    Font(R.font.leaguespartanbold, FontWeight.Bold),
    Font(R.font.leaguespartanlight,FontWeight.Light),
    Font(R.font.leaguespartanregular,FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = fuentes,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fuentes,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fuentes,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp
    ),
    /* bodyLarge = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Normal,
       fontSize = 16.sp,
       lineHeight = 24.sp,
       letterSpacing = 0.5.sp
   )
  Othefault text styles to override
   titleLarge = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Normal,
       fontSize = 22.sp,
       lineHeight = 28.sp,
       letterSpacing = 0.sp
   ),
   labelSmall = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Medium,
       fontSize = 11.sp,
       lineHeight = 16.sp,
       letterSpacing = 0.5.sp
   )
   */
)