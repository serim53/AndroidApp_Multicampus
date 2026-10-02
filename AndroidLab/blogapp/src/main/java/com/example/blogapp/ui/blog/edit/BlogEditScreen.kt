package com.example.blogapp.ui.blog.edit

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blogapp.ui.blog.BlogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogEditScreen(
    viewModel: BlogViewModel,
    postId: Long?,
    onSaved: () -> Unit,
    onBack: () -> Unit,
) {

    //유저 입력 요소.. 유저 입력 데이터 유지해야 하고. 유저 입력에 의한 화면 재구성되어야 한다.
    //==>상태 유지해야 한다.
    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
//    val post = viewModel.selectedPost
    val posts by viewModel.posts.collectAsStateWithLifecycle()

    //수정이라고 하면 초기에 이전 데이터가 준비되어 있어야 한다.
    //이곳에 상태 많다.. 이전 데이터 준비가 매번 될 필요가 있는가?
    LaunchedEffect(postId) {
        if(postId != null){
//            val post = posts.find { it.id == postId }
//            viewModel.loadPost(postId)
//            post?.let { //not null
//                title = it.title
//                content = it.content
//                imageUri = it.imageUri
//            }
            val post = posts.find { it.id == postId }
            if(post != null){
                imageUri = post.imageUri
                title = post.title
                content = post.content
            }else {
                viewModel.loadPost(postId)
            }
        }
    }

    //유저 입력 순간의 이벤트 콜백..
    fun onTitleChange(value: String){
        //유저 입력 이벤트가 발생하면 입력 데이터를 저장하는 상태를 변경해서 화면에 반영되게..
        title = value
    }
    fun onContentChange(value: String){
        content = value
    }

    fun savePost(){
        Log.d("BLOG", "title : $title, content:$content, image: $imageUri")
        Toast.makeText(context, "저장 되었습니다.", Toast.LENGTH_SHORT)
            .show()
        if(postId != null){
            viewModel.savePost(
                id = postId,
                title = title,
                content = content,
                imageUri = imageUri
            )
        }else {
            viewModel.savePost(
                id = 0,
                title = title,
                content = content,
                imageUri = imageUri
            )
        }
        onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("새 게시물") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        savePost()
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "저장")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
    ) { innerPadding -> //Scaffold 화면의 기본 구조를 설정, 화면단위 컴포저블에서 대부분 이용
        //항상 매개변수가 제공.. 여백 사이즈.. 시스템 영역과 겹치지 않게 하기 위한 여백 사이즈를 알려준다..
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                //동일 Modifier 에 padding 중복선언하면.. + 개념..
                //innerPadding 으로.. 시스템 영역과 겹치지 않을 만큼 패팅 설정하고..
                //개발자 의도에 의한 패딩 추가..
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "탭하여 이미지 선택",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = title,//초기값이자.. 유저가 입력한 상태값..
                //이벤트 콜백 함수 지정..
                //onValueChange = { value -> onTitleChange(value) },
                //코틀린에서 람다함수내에서만 예약어.  it
                //매개변수 하나를 가지는 람다함수를 선언할 때 매개변수 선언없이 it 으로 매개변수 값 대체 가능..
                //onValueChange = { onTitleChange(it) },
                //아래처럼 함수 참조정보로 지정도 가능하고..
                //자바에서는 A.class ==> 코틀린에서는 A::class
                onValueChange = ::onTitleChange,
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank(),
                supportingText = if(title.isBlank()){
                    { Text("제목을 입력해 주세요.")}
                }else null
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = ::onContentChange,
                label = { Text("본문") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                minLines = 6,
                maxLines = 15
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { savePost() },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if(postId != null) "수정" else "저장")
            }
        }

    }
}