package com.example.blogapp.ui.myinfo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoScreen(
    viewModel: MyInfoViewModel,
    onBack: () -> Unit,
) {

    //SnackbarHostState() 에서 상태를 유지한다.. 우리는 그 상태값을 변경만 하면 된다..
    val snackbarHostState = remember { SnackbarHostState() }

    //코루틴을 만들어서 구동시키려면 꼭 scope 가 먼저 선언되어야 하고.. 그 scope 소속으로 코루틴을 만들어 구동해야 한다..
    //CoroutineScope 는 여러 코루틴을 묶어서 한꺼번에 제어하는 역할..
    //Compose 에서 Scope 를 쉽게 사용하기 위해서.. rememberCoroutineScope()
    //자동으로.. 이 composable 제거시에..scope 내의 coroutine 종료..
    //SnackBar 띄우는 함수는 꼭..coroutine 내에서 호출하게 되어 있어서..
    //suspend 로 선언된 함수는 꼭 coroutine 이 준비되어야 한다..
    val scope = rememberCoroutineScope()

//    val savedEmail = viewModel.email
    val savedEmail by viewModel.email.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf(savedEmail) }

    val context = LocalContext.current//Context 객체 획득..

    fun onEditEmailChange(value: String){
        email = value
    }

    fun saveEmail(){
        scope.launch {
            //영속적 저장을 할 것이다. 시간이 오래 걸릴 수 있다. 코루틴으로..
            Log.d("BLOG", "이메일이 저장되었습니다.")
            viewModel.saveEmail(email)
            Toast.makeText(context, "이메일이 저장되었습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //화면의 기본 구조 제공..
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("내 정보") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        saveEmail()
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "저장")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "프로필 설정",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = ::onEditEmailChange,
                label = { Text("이메일") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("example@email.com") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "현재 저장된 이메일: ${if (email.isBlank()) "없음" else email}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    saveEmail()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("저장")
            }
        }
    }

}