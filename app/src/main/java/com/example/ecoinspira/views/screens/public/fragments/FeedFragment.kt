package com.example.ecoinspira.views.screens.public.fragments

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecoainspira.config.theme.theme
import com.example.ecoinspira.R
import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.post.EcoPostModel
import com.example.ecoinspira.services.post.IEcoPostService
import com.example.ecoinspira.viewmodel.eco_fragment.EcoFragmentsViewModel
import com.example.ecoinspira.viewmodel.fragment.EcoFragmentSlider
import com.example.ecoinspira.views.components.eco_icons.EcoIcon
import com.example.ecoinspira.views.components.eco_typography.EcoTypography
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun FeedFragment(
    fragmentMainViewModel: EcoFragmentsViewModel
) {

    val context = LocalContext.current

    val posts = remember { mutableStateOf<List<EcoPostModel>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    val postService: IEcoPostService = get()

    LaunchedEffect(Unit) {
        postService.getAllPosts(
            context = context,
            options = EcoAPICallback(
                onSucess = { response ->
                    // response agora é do tipo GetPostResponse
                    posts.value = response.post
                    isLoading.value = false
                },
                onFailure = { error ->
                    isLoading.value = false
                    println("Erro ao buscar posts: $error")
                }
            )
        )
    }

    EcoFragmentSlider(form = fragmentMainViewModel.feedFragmentView.observeAsState()) {

        EcoFeedScreen(posts)

    }
}


@Composable
fun EcoFeedScreen(posts: MutableState<List<EcoPostModel>>) {

    val pagerState = rememberPagerState { posts.value.size }

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(theme.colors.white)
    ) { page ->

        EcoPostItem(post = posts.value[page])
    }
}

@Composable
fun EcoPostItem(post: EcoPostModel) {
    var showComments by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        post.imageUrl?.let { EcoFeedImage(url = it) }

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            EcoTypography(
                text = "@${post.userName}",
                size = 16.sp,
                color = theme.colors.white,
                weight = FontWeight.SemiBold,
                padding = PaddingValues(12.dp)
            )

            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxSize()) {


                EcoTypography(
                    text = "@${post.title}",
                    size = 16.sp,
                    color = theme.colors.black01,
                    weight = FontWeight.SemiBold,
                    padding = PaddingValues(12.dp)
                )
            }


            EcoSideActions(
                modifier = Modifier.align(Alignment.BottomEnd),
                likes = post.likesCount.toInt(),
                comments = post.commentsCount.toInt(),
                avatarRes = R.drawable.avatarmota,
                onCommentsClick = { showComments = true }
            )
        }
    }

    if (showComments) {
        CommentsBottomSheet(
            comments = demoComments(), // função que retorna lista fake
            onDismiss = { showComments = false }
        )
    }
}


@Composable
fun EcoFeedImage(url: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            // painter = painterResource(id = imageRes),
            painter = rememberAsyncImagePainter(url),
            contentDescription = "Feed Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


    }
}


@Composable
fun EcoSideActions(
    modifier: Modifier = Modifier,
    likes: Int,
    comments: Int,
    avatarRes: Int,
    onCommentsClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(end = 8.dp, bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )

        EcoActionButton(icon = Icons.Default.Favorite, count = likes, color = Color.Red)
        EcoActionButton(icon = Icons.Default.Comment, count = comments, onClick = onCommentsClick)
    }
}


@Composable
fun EcoActionButton(
    icon: ImageVector,
    count: Int? = null,
    color: Color = theme.colors.white,
    onClick: () -> Unit = {} // parâmetro extra, opcional
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        EcoIcon(
            icon = icon,
            size = 36.dp,
            iconColor = color
        )
        count?.let {
            EcoTypography(
                text = it.toString(),
                size = 14.sp,
                color = theme.colors.white,
                weight = FontWeight.Medium
            )
        }
    }
}


data class EcoPost(
    val imageRes: Int,
    val likes: Int,
    val comments: Int,
    val favorites: Int,
    val username: String,
    val userAvatar: Int // poderia ser URL se fosse com Coil
)


data class EcoComment(
    val username: String,
    val avatarRes: Int,
    val text: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    comments: List<EcoComment>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (LocalConfiguration.current.screenHeightDp.dp * 0.7f)) // ocupa no máx 70%
                .padding(16.dp)
        ) {
            EcoTypography(
                text = "${comments.size} Comentários",
                size = 16.sp,
                weight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false) // ocupa só o espaço dos comentários
            ) {
                items(comments) { comment ->
                    CommentItem(comment = comment)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Adicionar comentário") }
                )
                IconButton(onClick = { /* enviar comentário */ }) {
                    Icon(Icons.Default.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: EcoComment) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = comment.avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column {
            EcoTypography(
                text = comment.username,
                size = 14.sp,
                weight = FontWeight.SemiBold
            )
            EcoTypography(
                text = comment.text,
                size = 14.sp,
                color = Color.Gray
            )
        }
    }
}


fun demoComments() = listOf(
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Matheus Mota Caldas",
        R.drawable.avatarmota,
        "Caraca mano ficou muito legal esse vaso de flor"
    ),
    EcoComment(
        "Vinícius Gimines Sapienza",
        R.drawable.avatarvinicius,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ),
    EcoComment("Raphael", R.drawable.avatarraphael, "Gostei muito dessa ideia, parabéns!"),
    EcoComment(
        "Marcos Ribeiro",
        R.drawable.avatarmarcos,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    )

)