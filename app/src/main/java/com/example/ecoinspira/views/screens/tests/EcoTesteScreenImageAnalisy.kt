package com.example.ecoinspira.views.screens.tests

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class EcoTesteScreenImageAnalisy : EcoActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ObjectRecognitionApp()


        }
    }
}
@Composable
private fun ObjectRecognitionApp() {
    var screenState by remember { mutableStateOf<RecognitionScreenState>(RecognitionScreenState.Camera) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Mostrar mensagem que a permissão é necessária
        }
    }

    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    when (screenState) {
        RecognitionScreenState.Camera -> {
            CameraScreen(
                onImageCaptured = { uri ->
                    capturedImageUri = uri
                    screenState = RecognitionScreenState.Analysis
                },
                onError = { error ->
                    Log.e("CameraError", "Erro na câmera", error)
                }
            )
        }

        RecognitionScreenState.Analysis -> {
            capturedImageUri?.let { uri ->
                Column(modifier = Modifier.fillMaxSize()) {
                    ImageAnalysisScreen(imageUri = uri)

                    Button(
                        onClick = { screenState = RecognitionScreenState.Camera },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text("Tirar outra foto")
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraScreen(
    onImageCaptured: (Uri) -> Unit,
    onError: (Throwable) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        onError(e)
                    }
                }, executor)

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                val outputFile = File(context.cacheDir, "temp_photo.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val uri = Uri.fromFile(outputFile)
                            onImageCaptured(uri)
                        }

                        override fun onError(exc: ImageCaptureException) {
                            onError(exc)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Capturar Imagem")
        }
    }
}

@Composable
private fun ImageAnalysisScreen(imageUri: Uri) {
    val context = LocalContext.current
    val labels = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageUri) {
        analyzeImage(context, imageUri) { detectedLabels ->
            labels.clear()
            labels.addAll(detectedLabels)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            Text("Analisando imagem...", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(
                "Objetos identificados:",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (labels.isEmpty()) {
                Text("Nenhum objeto identificado")
            } else {
                LazyColumn {
                    items(labels.size) { index ->
                        Text("- ${labels[index]}")
                    }
                }
            }
        }
    }
}

private suspend fun analyzeImage(
    context: Context,
    imageUri: Uri,
    onLabelsDetected: (List<String>) -> Unit
) = withContext(Dispatchers.IO) {
    try {
        val image = InputImage.fromFilePath(context, imageUri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val formattedLabels = labels.map { label ->
                    "${label.text} (${(label.confidence * 100).toInt()}% de confiança)"
                }
                onLabelsDetected(formattedLabels)
            }
            .addOnFailureListener { e ->
                Log.e("ImageAnalysis", "Erro na análise", e)
                onLabelsDetected(emptyList())
            }
    } catch (e: Exception) {
        Log.e("ImageAnalysis", "Erro ao criar InputImage", e)
        onLabelsDetected(emptyList())
    }
}

private sealed class RecognitionScreenState {
    object Camera : RecognitionScreenState()
    object Analysis : RecognitionScreenState()
}

// Extensão para AndroidView (se não estiver disponível)
@Composable
private fun AndroidView(
    factory: (Context) -> android.view.View,
    modifier: Modifier = Modifier,
    update: (android.view.View) -> Unit = {}
) {
    androidx.compose.ui.viewinterop.AndroidView(
        factory = factory,
        modifier = modifier,
        update = update
    )
}