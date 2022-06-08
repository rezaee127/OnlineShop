package com.example.onlineshop.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.onlineshop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState!= null){
            binding.fragmentContainerView.visibility = savedInstanceState.getInt("container")
            binding.splashIcon.visibility = savedInstanceState.getInt("splash")
        }else
            supportActionBar?.hide()

        splash()

    }

    private fun splash() {

        binding.splashIcon.alpha=0f
        binding.splashIcon.animate().setDuration(3000).alpha(1f).withEndAction {
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            binding.fragmentContainerView.visibility= View.VISIBLE
            binding.splashIcon.visibility= View.GONE
            supportActionBar?.show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("container" , binding.fragmentContainerView.visibility)
        outState.putInt("splash" ,  binding.splashIcon.visibility)
        super.onSaveInstanceState(outState)
    }
}