package com.example.ecoinspira.views.components.eco_buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.models.generate.EcoGenerateModel
import com.example.ecoinspira.models.generate.FormaDeReciclar
import com.example.ecoinspira.views.components.eco_typography.EcoTypography

@Composable
fun EcoMaterialSelectionButton(
    resultado: EcoGenerateModel?,
    selectedOption: MutableState<String?>,
    onAvancar: () -> Unit
) {
    resultado?.let { res ->
        EcoTypography(text = res.title ?: "Análise do Material")

        if (!res.motivo.isNullOrEmpty()) {
            EcoTypography(text = "Motivo: ${res.motivo}")
        }

        res.formasDeReciclar?.takeIf { it.isNotEmpty() }?.let { formas ->
            Spacer(modifier = Modifier.height(8.dp))
            EcoTypography(text = "Formas de Reciclar:")
            FormaDeReciclarSelector(formas, selectedOption)
        }

        selectedOption.value?.let {
            Spacer(modifier = Modifier.height(8.dp))
            EcoTypography(text = "Opção selecionada: $it")

            // ✅ botão para ir para a próxima etapa (passos)
            Spacer(modifier = Modifier.height(12.dp))
            EcoSimpleButton("Ver Passos", onClick = onAvancar)
        }
    }
}


@Composable
fun FormaDeReciclarSelector(
    formas: List<FormaDeReciclar>,
    selectedOption: MutableState<String?> // <- variável externa
) {
    val selected = remember { mutableStateOf<FormaDeReciclar?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        formas.forEach { forma ->
            val isSelected = forma == selected.value

            EcoSimpleButton(
                onClick = {
                    selected.value = forma
                    selectedOption.value = forma.opcao // ✅ salva o texto da opção
                },
                text = forma.opcao,

                color = if (isSelected) theme.colors.white else theme.colors.black01,
                backgroundColor = if (isSelected) theme.colors.verdeConfirmar else theme.colors.logo,
                backgroundColorDegrade = if (isSelected) theme.colors.verdeConfirmar else theme.colors.logo,
            )
        }
    }
}