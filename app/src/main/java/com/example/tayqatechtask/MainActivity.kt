package com.example.tayqatechtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tayqatechtask.databinding.ActivityMainBinding
import com.example.tayqatechtask.ui.PersonsListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, PersonsListFragment())
            .commit()

    }
}