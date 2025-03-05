package com.example.ecoainspira.config.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.ecoinspira.R

// --== Tema de cores do EcoInspira
data class EcoThemeColors(
    // --== Cores neutras
    val black00: Color = Color(0xFF000000),
    val black01 : Color = Color(0xFF171717),
    val blackTransparent : Color = Color(0xF2171717),
    val blacksemiTransparente: Color = Color(0xA41E1E1E),

    val white: Color = Color(0xFFFFFFFF),
    val paper: Color = Color(0xFFF7F7F7),
    val transparent: Color = Color (0x00000000),

    val decription: Color = Color(0xFF838383),
    val statusErro: Color = Color(0xE0E00000),
    val vermelho00: Color = Color(0xEDAD0101),
    val vermelho01: Color = Color(0xEDD76B6B),
    val rosa00: Color = Color(0xFFFBE4E4),

    // --== Cor primária
    val primary01: Color = Color(0xFFD3F41F),
    val primary02: Color = Color(0xFFB1D100),
    val primary03: Color = Color(0xFF92AD10),
    val darkPrimary: Color = Color(0xFF4D5B00),

    // --== Tons de vier
    val verdeConfirmar: Color = Color(0xFF346E26),
    val verdeLivre: Color = Color(0xFFC9E7CB),

    // --== Tons de cinza
    val cinza00: Color = Color(0xFF888888),
    val cinza02: Color = Color(0xFF8B8FA8),
    val cinza03: Color = Color(0xFFE5E6E9),
    val cinza04: Color = Color(0xFFADB5BD),
    val cinza05: Color = Color(0xFFEDEDED),
    val cinza06: Color = Color(0xFFD9D9D9),
    val cinza07: Color = Color(0xFFDEE2E6),
    val cinza08: Color = Color(0xFF797979),
    val cinza09: Color = Color(0xFF565656),
    val cinza10: Color = Color(0xFF33303E),
    val cinza11: Color = Color(0xFF3D3D3D),
    val cinza12: Color = Color(0xFF2D2D2D),
    val cinza13: Color = Color(0xFF868AA5),
    val cinza14: Color = Color(0xFF495057),
    val cinza16: Color = Color(0xFF868E96),
)

// --== Tema geral do EcoAInspira
data class EcoTheme(
    val colors: EcoThemeColors = EcoThemeColors(),

)

// -- Variável de acesso
val theme = EcoTheme()


val EcoFontNunitoSans = FontFamily(
    Font(R.font.nunitosans_regular, FontWeight.Normal),
    Font(R.font.nunitosans_bold, FontWeight.Bold),
    Font(R.font.nunitosans_semibold, FontWeight.SemiBold),
    Font(R.font.nunitosans_medium, FontWeight.Medium),
    Font(R.font.nunitosan_light, FontWeight.Light),
    Font(R.font.nunitosans_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunitosans_extralight, FontWeight.ExtraLight),
    Font(R.font.nunitosans_blackitalic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.nunitosans_blackitalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.nunitosans_semiboliditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.nunitosans_semiboliditalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.nunitosans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.nunitosans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.nunitosans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.nunitosans_italic, FontWeight.Normal, FontStyle.Italic)
)