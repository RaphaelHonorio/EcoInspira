package com.example.ecoinspira.views.screens.public

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.example.ecoinspira.views.components.eco_typography.EcoTypography


import androidx.lifecycle.viewmodel.compose.viewModel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class EcoLoginScreen : EcoActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppNavigation()

        }
    }
}



// Testes //

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToCamera = {
                    navController.navigate("camera")
                }
            )
        }
        composable("camera") {
            CameraScreen(
                onNavigateToResults = { imageUri ->
                    navController.navigate("results/${Uri.encode(imageUri)}")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            "results/{imageUri}",
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.decode(it) }
            if (imageUri != null) {
                ResultsScreen(
                    imageUri = Uri.parse(imageUri),
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigateToCamera: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Object Recognition App",

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToCamera) {
            Text("Take a Photo")
        }
    }
}



@Composable
fun CameraScreen(
    onNavigateToResults: (String) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission.value = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.imageUri?.let { uri ->
                onNavigateToResults(uri.toString())
            }
        }
    }

    LaunchedEffect(key1 = true) {
        if (!hasPermission.value) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Camera Screen",

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (hasPermission.value) {
                    // Create a temp file and get URI
                    viewModel.createImageUri(context)?.let { uri ->
                        cameraLauncher.launch(uri)
                    }
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        ) {
            Text("Take Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackPressed) {
            Text("Go Back")
        }
    }
}

@Composable
fun ResultsScreen(
    imageUri: Uri,
    onBackPressed: () -> Unit,
    viewModel: ImageAnalysisViewModel = viewModel()
) {
    val context = LocalContext.current
    val recognitionResults = viewModel.recognitionResults.collectAsState()
    val isAnalyzing = viewModel.isAnalyzing.collectAsState()

    LaunchedEffect(key1 = imageUri) {
        viewModel.analyzeImage(context, imageUri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Image Analysis Results",

            )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the captured image
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Captured Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isAnalyzing.value) {
            CircularProgressIndicator()
            Text(text = "Analyzing image...")
        } else {
            // Display the recognition results
            Text(
                text = "Objects Detected:",

                )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {


                items(recognitionResults.value) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = result.toString(),

                                )
                            Text(
                                text = "Confidence: ${(result.confidence * 100).toInt()}%",
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackPressed) {
            Text("Go Back")
        }
    }
}




class CameraViewModel : ViewModel() {
    var imageUri: Uri? = null
        private set

    fun createImageUri(context: Context): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("Pictures")
        val imageFile = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        imageUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )

        return imageUri
    }
}






data class RecognitionResult(
    val label: String,
    val confidence: Float
)

class ImageAnalysisViewModel : ViewModel() {
    private val _recognitionResults = MutableStateFlow<List<RecognitionResult>>(emptyList())
    val recognitionResults: StateFlow<List<RecognitionResult>> = _recognitionResults

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing

    fun analyzeImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _isAnalyzing.value = true

            try {
                val image = InputImage.fromFilePath(context, imageUri)
                val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        val results = labels.map { label ->
                            RecognitionResult(
                                label = label.text,
                                confidence = label.confidence
                            )
                        }
                        _recognitionResults.value = results.sortedByDescending { it.confidence }
                        _isAnalyzing.value = false
                    }
                    .addOnFailureListener { e ->
                        _recognitionResults.value = listOf(
                            RecognitionResult(
                                label = "Error analyzing image: ${e.message}",
                                confidence = 0f
                            )
                        )
                        _isAnalyzing.value = false
                    }
            } catch (e: IOException) {
                _recognitionResults.value = listOf(
                    RecognitionResult(
                        label = "Error loading image: ${e.message}",
                        confidence = 0f
                    )
                )
                _isAnalyzing.value = false
            }
        }
    }
}