package com.example.androidlab

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidlab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    lateinit var button: Button
//    lateinit var editView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //액티비티의 영역이 시스템 영역(status bar, navigation bar까지 차지하라..)
        enableEdgeToEdge()

        //화면 출력 명령.. inflate + 출력
//        setContentView(R.layout.activity_main)
//        //필요한 뷰 객체 획득..
//        button = findViewById(R.id.button)
//        editView = findViewById(R.id.editView)
//        //뷰에 이벤트 등록..
//        button.setOnClickListener {
//            val data = editView.text.toString()
//            Log.d("kkang", data)
//        }

        //ViewBinding...............................
        //자동으로 만들어진 Binding 클래스에 inflate 명령은 내려야 한다..
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //XXXBinding 클래스내에. 뷰 변수 이미 선언.. 이름은 id 로..
        binding.button.setOnClickListener {
            val data = binding.editView.text.toString()
            Log.d("kkang", data)
        }

        //시스템 영역의 컨텐츠에서 최대한 앱의 컨텐츠를 보호해서 출력..
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}