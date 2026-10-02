package com.example.blogapp.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.blogapp.model.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
    ) { innerPadding ->

        when (val state = uiState) {
            is NewsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is NewsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = "오류: ${state.message}")
                }
            }

            is NewsUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    items(state.newsList) { news ->
                        NewsItem(news = news)
                        HorizontalDivider(thickness = 0.2.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: News) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .background(androidx.compose.ui.graphics.Color.White)
            .padding(top = 10.dp, bottom = 15.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = news.title ?: "",
            color = androidx.compose.ui.graphics.Color(0xFF212121),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "${news.author} At ${news.publishedAt}",
            color = androidx.compose.ui.graphics.Color(0xFF727272),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = news.description ?: "",
            color = androidx.compose.ui.graphics.Color(0xFF212121),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        news.urlToImage?.let { imageUrl ->
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = news.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}