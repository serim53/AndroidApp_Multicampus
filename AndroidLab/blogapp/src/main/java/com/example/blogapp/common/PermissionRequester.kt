package com.example.blogapp.common

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

//앱의 퍼미션이 많다. 퍼미션 체크, 다이얼로그 띄우는 코드 중복..
@Composable
fun permissionRequester(
    permission: String, //퍼미션 이름..
    onPermissionGranted: () -> Unit = {},//허락상태에서 실행될 함수..
    onPermissionDenied: () -> Unit = {}//거부상태에서 실행될 함수..
){
    val context = LocalContext.current
    //퍼미션 조정 다이얼로그를 띄우는 런처. 콜백 등록해서..
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()//요청 실행자..퍼미션 조정 다이얼로그..
    ) { isGranted -> //다이얼로그가 닫긴 후 콜백..
        if(isGranted){
            onPermissionGranted()
        }else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(permission) {
        //현 상태 체크..
        val isGranted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
        if(isGranted){
            onPermissionGranted()
        }else {
            //거부 상태라면 조정 다이얼로그 띄운다..
            permissionLauncher.launch(permission)
        }
    }
}