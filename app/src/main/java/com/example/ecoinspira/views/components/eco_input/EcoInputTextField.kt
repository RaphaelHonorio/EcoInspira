package com.example.ecoinspira.views.components.eco_input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.views.components.eco_icons.EcoIcon
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


@Composable
fun EcoMinimalTextField(
    // --== Configurações do textField
    refValue: MutableState<String>,
    onValueChange: ((String) -> Unit) ?= { x -> refValue.value = x },
    visualTransformation: VisualTransformation?= VisualTransformation.None,
    maxLength: Int ?= 90,
    singleLine: Boolean ?= true,
    // --== Configuraçãoes de elementos do estilo
    margin: PaddingValues?= PaddingValues(0.dp),
    fullWidth: Boolean ?= true,
    width: Dp?= null,
    height: Dp?= 50.dp,
    borderSize: Dp?= 1.dp,
    borderRadius: Dp?= 8.dp,
    yTextLocation: Dp?= 16.dp,
    xTextLocation: Dp?= 17.dp,
    // --== Placeholder
    placeholder: String ?= "",
    placeholderSize : TextUnit?= 14.sp,
    paddingPlaceholder : Dp?= 17.dp,
    paddingPlaceholderBottom : Dp?= 0.dp,
    // --== Ação para trocar de um input para outro
    imeAction: ImeAction = ImeAction.Done,
    onDone: (KeyboardActionScope.() -> Unit) ?= null,
    type: KeyboardType? = KeyboardType.Password,
    // --== Ação para quando há um erro no TextField
    hasError: Boolean ?= false,
    errorMessage: String? = "",
    // --== Texto
    textSize: TextUnit?= 18.sp,
    textColor: Color ?= theme.colors.cinza02,
    textWeight: FontWeight? = FontWeight.Medium,
    letterSpacing: TextUnit?=  TextUnit.Unspecified,
    // --== Icone
    hasIcon: Boolean ?= false,
    icon: ImageVector ?= Icons.Default.Person,
    focusRequester: FocusRequester? = null,
    enable: Boolean ?= true,
    // --== Cores Provisorias mudar assim que tiver um guia de estilos
    borderColor: Color ?= theme.colors.cinza04,
    cursorColor: Color ?= theme.colors.transparent,
    backgroundColor : Color ?= theme.colors.white,
    wrapperBackground : Color ?= Color.Transparent,
    wrapperBorderRadius : Dp?= 8.dp
) {
    // -- Iniciando estilização
    var styledEcoInputTextField = Modifier
        .wrapContentSize()
        .padding(margin!!)
    // -- Verificando se deve ocupar toda a largura
    fullWidth?.let {
        if (fullWidth == true) styledEcoInputTextField =
            styledEcoInputTextField.fillMaxWidth()
    }
    // --== Adicionando demais estilos
    width?.let { styledEcoInputTextField = styledEcoInputTextField.width(width) }
    height?.let { styledEcoInputTextField = styledEcoInputTextField.height(height) }
    borderColor?.let { color ->
        borderSize?.let { size ->
            styledEcoInputTextField = styledEcoInputTextField.border(
                BorderStroke(size, color),
            )
        }
    }
    //--==Variaveis para definir valores como distancia e cor que não podem ser colocados diretamente no codigo
    val corBorda = if (hasError == false) borderColor else  theme.colors.statusErro
    val distanciaicone = if (hasIcon == true) xTextLocation?.plus(15.dp) else  xTextLocation

    Column(Modifier.background(wrapperBackground!!).clip(shape = RoundedCornerShape(wrapperBorderRadius!!))){
        //--== Box para fazer sobreposição de icone e placeholder
        Box {
            BasicTextField(
                //--== Definir o Valor que ele vai ter
                value = refValue.value,
                //--== OnValueChange Com o limitador de tamanho
                onValueChange = { x ->
                    if (x.length <= maxLength!!) {
                        refValue.value = x
                        onValueChange?.invoke(x)
                    }
                },
                //--== Visual Tranformation para definir "." "/" "-" etc
                visualTransformation = visualTransformation!!,
                //--== Modificadores Visuais Para deixar o input Bonito
                modifier = Modifier.padding(1.dp).fillMaxWidth().height(height!!).background(backgroundColor!!)
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                    //--== Define a Cor e expressura da borda
                    .border(border = BorderStroke(borderSize!!, SolidColor(corBorda!!)), shape = RoundedCornerShape(borderRadius!!))
                    .offset(y = yTextLocation!!, x = distanciaicone!!),
                //--== Modificadores Visuais Para deixar o texto interno Bonito
                textStyle = TextStyle(fontSize = textSize!!, color = textColor!!, textAlign = TextAlign.Start, fontWeight = textWeight!!,letterSpacing = letterSpacing!!),
                //--== Opções de Configuração do Teclado
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = type!!, imeAction = imeAction),
                keyboardActions = KeyboardActions(onDone = onDone),
                singleLine = singleLine!!,
                cursorBrush = Brush.verticalGradient(listOf(cursorColor!!, cursorColor)),
                enabled = enable!!
            )
            //--== Icone se Tiver e Placeholder se houver
            if (hasIcon == true) {
                Box(modifier = Modifier.offset(y = 16.dp, x = 16.dp)){
                    EcoIcon(icon = icon!!, size = 20.dp, iconColor = theme.colors.cinza06)
                }
                if (refValue.value.isEmpty()) {
                    Text(text = placeholder!!, color = theme.colors.cinza06,fontSize = placeholderSize!! ,
                        modifier = Modifier.padding(start = 45.dp).align(Alignment.CenterStart))
                }
            } else {
                if (refValue.value.isEmpty()) {
                    Text(text = placeholder!!, color = theme.colors.cinza06,fontSize = placeholderSize!!, letterSpacing = letterSpacing,
                        modifier = Modifier.padding(start = paddingPlaceholder!!, bottom = paddingPlaceholderBottom!! ).align(
                            Alignment.CenterStart)) }
            }
        }
        //--== Mensagem de Erro se houver
        if (hasError == true) {
            Row {
               EcoTypography(text = errorMessage!!, color = theme.colors.statusErro) }
        }
    }
}