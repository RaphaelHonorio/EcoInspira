package com.example.ecoinspira.views.screens.public

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.ecoinspira.extensions.activity.EcoActivity
import com.example.ecoinspira.services.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import com.example.ecoinspira.services.hugging_face.HuggingFaceRequest
import com.example.ecoinspira.services.hugging_face.Prediction
import java.io.File


class EcoTesteScreen : EcoActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ObjectDetectionScreen()
        }
    }
}

@Composable
fun ObjectDetectionScreen() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var predictions by remember { mutableStateOf<List<Prediction>?>(null) }

    // Cria um arquivo temporário para armazenar a foto capturada
    val photoFile = remember { File.createTempFile("photo", ".jpg", context.cacheDir) }
    val photoUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)

    // Launcher para capturar a foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUri // Atualiza a URI da imagem capturada
            imageUri?.let { uri ->
                // Converte a imagem para base64
                val inputStream = context.contentResolver.openInputStream(uri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                val resizedBitmap = resizeImage(originalBitmap, 800, 800) // Redimensiona para 800x800 pixels

                val byteArrayOutputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream) // 80% de qualidade
                val imageBytes = byteArrayOutputStream.toByteArray()
                val imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                // Envia a imagem para a API
                val request = HuggingFaceRequest(imageBase64)
                val call = RetrofitClient.instance.analyzeImage("Bearer secret kkkk", request)
                call.enqueue(object : Callback<List<Prediction>> {
                    override fun onResponse(
                        call: Call<List<Prediction>>,
                        response: Response<List<Prediction>>
                    ) {
                        if (response.isSuccessful) {
                            predictions = response.body()
                        } else {
                            println("Erro na requisição: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Prediction>>, t: Throwable) {
                        println("Erro: ${t.message}")
                    }
                })
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch(photoUri) }) {
            Text("Tirar Foto")
        }

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        predictions?.let { predictions ->
            Column {
                predictions.forEach { prediction ->
                    Text("${prediction.label}: ${prediction.score}")
                }
            }
        }
    }
}
// Função para converter imagem em base64 (não é mais necessária, pois fazemos isso diretamente no launcher)
// fun convertImageToBase64(imagePath: String): String {
//     val bitmap = BitmapFactory.decodeFile(imagePath)
//     val byteArrayOutputStream = ByteArrayOutputStream()
//     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//     val imageBytes = byteArrayOutputStream.toByteArray()
//     return Base64.encodeToString(imageBytes, Base64.DEFAULT)
// }


fun resizeImage(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val ratio = width.toFloat() / height.toFloat()

    val newWidth: Int
    val newHeight: Int

    if (width > height) {
        newWidth = maxWidth
        newHeight = (newWidth / ratio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (newHeight * ratio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}