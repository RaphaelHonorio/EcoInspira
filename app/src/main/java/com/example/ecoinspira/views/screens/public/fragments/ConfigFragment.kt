package com.example.ecoinspira.views.screens.public.fragments

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.ecoinspira.config.keys.EcoMemoChunks
import com.example.ecoinspira.config.keys.EcoMemoKeys
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.services.memo.IEcoMemo
import com.example.ecoinspira.services.post.IEcoPostService
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.viewmodel.user.EcoUserViewModel
import com.example.ecoinspira.views.components.eco_buttons.EcoSimpleButton
import com.example.ecoinspira.views.components.eco_input.EcoMinimalTextField
import com.example.ecoinspira.views.components.eco_paper.EcoMargin
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConfigFragment (
    userViewModel: EcoUserViewModel,
    fragmentMainViewModel: EcoFragmentsViewModel,
    _memoService: IEcoMemo
) {
    val context = LocalContext.current

    val postService: IEcoPostService = get()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val Titulo = remember { mutableStateOf("") }

    val Descricao = remember { mutableStateOf("") }


    val post = EcoPostModel(
        title = Titulo.value,
        description = Descricao.value,
        likesCount = 20,
        commentsCount = 20,
        userName = _memoService.find(context, EcoMemoKeys.nome,EcoMemoChunks().identidade)
    )

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


    // --- Função que chama o serviço de upload
    fun handleUploadPost(file: File) {

        GlobalScope.launch {
            postService.uploadPost(
                context = context,
                post = post,
                imageFile = file,
                options = EcoAPICallback(
                    onSucess = { response ->
                    },
                    onFailure = { error ->
                    }
                )
            )
        }
    }


    // --- URI da imagem capturada
    val imageUri = remember { mutableStateOf<Uri?>(null) }


    // --- Launcher para capturar foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri.value?.let { uri ->
                // converte Uri para File
                val file = uriToFile(uri, context)
                // envia para o backend
                handleUploadPost(file)
            }
        }
    }

    // --- Botão para abrir câmera
    fun takePhoto() {
        val photoFile = createTempFile()
        imageUri.value = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        launcher.launch(imageUri.value)
    }



    EcoFragmentSlider(form = fragmentMainViewModel.configFragmentView.observeAsState()) {
        
        
        EcoMargin(marginTop = 24.dp) {
            
        EcoTypography(text = "Teste de Postagem de Imagem")
        
        EcoMinimalTextField(
            refValue = Titulo,
            placeholder = "Digite o Titulo",
            imeAction = ImeAction.Done,
            onValueChange = {
                Titulo.value = it

            }
        )
        EcoMinimalTextField(
            refValue = Descricao,
            placeholder = "Digite a descricao",
            imeAction = ImeAction.Done,
            onValueChange = {
                Descricao.value = it

            }
        )

        


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EcoSimpleButton("Tirar Foto e Enviar", onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        takePhoto()
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    } })

            }

        }
    }
}