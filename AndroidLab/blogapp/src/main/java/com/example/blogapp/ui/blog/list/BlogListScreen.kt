package com.example.blogapp.ui.blog.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.blogapp.R
import com.example.blogapp.domain.model.BlogPost
import com.example.blogapp.ui.blog.BlogViewModel
import kotlinx.coroutines.launch


//테스트를 위한 가상의 데이터..
//val posts: List<BlogPost> = listOf(
//    BlogPost(id = 1, title = "title1", content = "content1", author = "author1", imageUri = null),
//    BlogPost(id = 2, title = "title2", content = "content1", author = "author1", imageUri = null),
//    BlogPost(id = 3, title = "title3", content = "content1", author = "author1", imageUri = null),
//    BlogPost(id = 4, title = "title4", content = "content1", author = "author1", imageUri = null),
//    BlogPost(id = 5, title = "title5", content = "content1", author = "author1", imageUri = null),
//)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogListScreen(
    viewModel: BlogViewModel,
    onBack: () -> Unit,
    onNavigateToBlogDetail: (Long) -> Unit,
    onNavigateToBlogEdit: () -> Unit,
) {

//    val posts = viewModel.posts
    val posts by viewModel.posts.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("블로그 목록") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    Button(
                        onClick = onNavigateToBlogEdit,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("새 게시물")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
    ) { innerPadding ->

            if(posts.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "아직 게시물이 없어요",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "새 게시물을 작성해 보세요!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    //posts : 갯수 만큼 항목 추가..
                    //key : optional 이다.. 만들어지는 항목의 내부적으로 이용하는 식별자 값을 지정
                    //key 를 지정하지 않으면.. index 로 key 값 설정..
                    //목록에 항목이 나온 후 변화가 없거나..마지막에 신규 항목이 추가되거나. 마지막 항목이 제거되거나. 설정하지 않아도 문제 없다.
                    //항목이 중간 위치에 추가되거나, 중간위치의 항목이 제거되거나, 항목의 순서가 변경되거나..  key 안주면 이상반응..
                    //키는 유일성이 확보되는 값으로 주야하고.. 데이터에 유일성 확보 데이터가 있다면 그 값을 지정하라고 권장..
                    items(posts, key = { it.id }){ post -> //항목 갯수 만큼 반복 호출..
                        BlogPostCard(
                            post=post,
                            onClick = { onNavigateToBlogDetail(post.id) }
                        )
                    }
                }
            }
        }
//    }


}


@Composable
fun BlogPostCard(
    onClick: () -> Unit,
    post: BlogPost,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.main_swiss)
                        .crossfade(true)
                        .build(),
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            // 제목
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            )
        }
    }
}