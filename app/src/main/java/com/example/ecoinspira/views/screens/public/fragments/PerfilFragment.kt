package com.example.ecoinspira.views.screens.public.fragments

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.config.keys.EcoMemoChunks
import com.example.ecoinspira.config.keys.EcoMemoKeys
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.services.memo.IEcoMemo
import com.example.ecoinspira.services.post.IEcoPostService
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun PerfilFragment(
    userViewModel: EcoUserViewModel,
    fragmentMainViewModel: EcoFragmentsViewModel,
    _memoService: IEcoMemo
) {

    EcoFragmentSlider(form = fragmentMainViewModel.perfilFragmentView.observeAsState()) {

        val context = LocalContext.current
        val postService: IEcoPostService = get()

        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        // --- URI da imagem (tanto câmera quanto galeria)
        val imageUri = remember { mutableStateOf<Uri?>(null) }

        // --- Cria arquivo temporário para câmera
        fun createTempFile(): File {
            return File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        }

        // --- Converte Uri para File
        fun uriToFile(uri: Uri, context: Context): File {
            val inputStream = context.contentResolver.openInputStream(uri)!!
            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            return file
        }

        // --- Launcher para capturar foto com câmera
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                imageUri.value?.let { uri ->
                    val file = uriToFile(uri, context)
                    GlobalScope.launch {
                        postService.analyzeImage(
                            context = context,
                            imageFile = file,
                            options = EcoAPICallback(
                                onSucess = { detectedObject ->
                                    Log.d("AI_ANALYZE", "Objeto detectado: $detectedObject")
                                },
                                onFailure = { error ->
                                    Log.e("AI_ANALYZE", "Erro na análise da imagem: $error")
                                }
                            )
                        )
                    }
                }
            }
        }

        // --- Launcher para escolher imagem da galeria
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                imageUri.value = it
                val file = uriToFile(it, context)
                GlobalScope.launch {
                    postService.analyzeImage(
                        context = context,
                        imageFile = file,
                        options = EcoAPICallback(
                            onSucess = { detectedObject ->
                                Log.d("AI_ANALYZE", "Objeto detectado: $detectedObject")
                            },
                            onFailure = { error ->
                                Log.e("AI_ANALYZE", "Erro na análise da imagem: $error")
                            }
                        )
                    )
                }
            }
        }

        // --- Função para tirar foto
        fun takePhoto() {
            val photoFile = createTempFile()
            imageUri.value = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            cameraLauncher.launch(imageUri.value)
        }

        // --- Interface
        EcoMargin(marginTop = 24.dp) {

            EcoTypography(text = "Teste de Análise de Imagem")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EcoSimpleButton("Tirar Foto", onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        takePhoto()
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                })


            }

            EcoSimpleButton("Escolher da Galeria", onClick = {
                galleryLauncher.launch("image/*")
            })

            // --- Exibir imagem abaixo com borda redonda
            imageUri.value?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = uri,
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, theme.colors.cinza02, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
