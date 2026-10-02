package com.example.blogapp.ui.blog.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.blogapp.R
import com.example.blogapp.ui.blog.BlogViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN)
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(
    viewModel: BlogViewModel,
    onBack: () -> Unit,
    postId: Long,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    //자신의 화면이 나오면서..  postId를 이용해 출력할 데이터 획득
    //상태가 있다. 매번 할 필요가 있겠는가?
    //어떤 특정 순간 한번만 실행되어 발생하는 값을 유지하고 싶은 것이다.
//    val post = remember(postId) {
//        posts.find { it.id == postId }
//    }

//    val post = viewModel.selectedPost
    val selectedPost by viewModel.selectedPost.collectAsStateWithLifecycle()

    LaunchedEffect(postId) {
        viewModel.loadPost(postId)
    }

    //다이얼로그를 띄울지 상태..
    var showDeleteDialog by remember { mutableStateOf(false) }

    if(showDeleteDialog){
        AlertDialog(
            //다이알로그 밖을 터치했을 때.
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("게시물 삭제")},
            text = { Text("게시물을 삭제하시겠습니까?")},
            //필수 버튼..
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deletePost(postId)
                        onDelete()
                    }
                ) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
            },
            //optional
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("취소")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시물 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onEdit()
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "수정")
                    }
                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "삭제")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
    ) { innerPadding ->

        val post = selectedPost

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 60.dp)
                //재구성시에. 스크롤 정보 유지되어야한다.
                .verticalScroll(rememberScrollState())
        ) {
            //식별자 변수 선언..
            val (image, title, authorRow, updateText, divider, content) = createRefs()

            //이미지를 리소스에서 로딩.. Coil 을 이용해서..
            //가장 기본 컴포저블은 AsyncImage
            //SubcomposeAsyncImage - 이미지 로딩, 에러 이미지등 설정 가능..
            if(post?.imageUri != null) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.imageUri.toUri())
                        .crossfade(true)//이미지 로딩 완료시 이미지 출력 애니메이션 효과 적용하라..
                        .build(),
                    contentDescription = post.title,
                    //화면 출력 사이즈와 이미지 사이즈가 맞지 않은 경우 어떻게 할것인가?
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                        },
                    loading = {//이미지 로딩 전에 출력..
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }

            Text(
                text = post?.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                    .constrainAs(title){
                        top.linkTo(if(post?.imageUri != null) image.bottom else parent.top)
                    }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .constrainAs(authorRow){
                        top.linkTo(title.bottom)
                    }
            ) {
                Text(
                    text = "작성자 : ${post?.author?.ifBlank { "Unknown"}}",
                    style= MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = " . ",
                    style= MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDate(System.currentTimeMillis()),
                    style= MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if(post?.updatedAt != post?.createdAt) {
                Text(
                    text = "수정됨 : ${formatDate(post?.updatedAt ?: System.currentTimeMillis())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .constrainAs(updateText) {
                            top.linkTo(authorRow.bottom)
                        }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .constrainAs(divider){
                        top.linkTo(if(post?.updatedAt != post?.createdAt) updateText.bottom else authorRow.bottom)
                    }
            )

            Text(
                text = post?.content?.ifBlank {"(내용 없음)"} ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .constrainAs(content){
                        top.linkTo(divider.bottom)
                    }
            )
        }
    }


}