package com.example.androidlab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidlab.databinding.ActivityLayoutTestBinding

class LayoutTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityLayoutTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // binding.mainEvent.setOnClickListener(MyHandler())

        //익명 클래스로 선언...
        //코틀린에서는 익명 클래스 꼭 object 예약어로 선언..
        //object : A() {} : A 를 상속받은..
        //object : A(), B { } : A를 상속받고, B를 구현한..
//        binding.mainEvent.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//
//            }
//        })

        //추상함수 1개를 가지는 인터페이스를 구현한 익명 클래스를 선언할 때..
        //위처럼 선언해도 되는데.. 인터페이스의 추상함수 내부 부분만 { } 로 줄여서 선언..
        //SAM : Single Abstract Method 기법..
//        binding.mainEvent.setOnClickListener({
//
//        })

        //어떤 함수를 호출할 때, 매개변수를 함수로..
        //마지막 매개변수에 한해서.. 함수를 전달한다면 () 밖에 선언이 가능하다.
//        binding.mainEvent.setOnClickListener(){
//
//        }
        binding.mainEvent.setOnClickListener {

        }
    }
}

class MyHandler: View.OnClickListener {
    override fun onClick(v: View?) {
        Log.d("kkang", "event...")
    }
}








