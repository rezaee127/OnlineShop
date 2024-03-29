package com.example.onlineshop.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment:NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)

        if(savedInstanceState!= null){
            binding.fragmentContainerView.visibility = savedInstanceState.getInt("container")
            binding.splashIcon.visibility = savedInstanceState.getInt("splash")
        }else{
            supportActionBar?.hide()
            binding.bottomNavigation.visibility = View.GONE
        }

        splash()
        setBottomNavigationVisibility()

    }

    private fun setBottomNavigationVisibility() {
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if(binding.splashIcon.isVisible)
                binding.bottomNavigation.visibility = View.GONE
            else if(destination.id == R.id.homeFragment||
                destination.id == R.id.categoriesFragment
                ||  destination.id == R.id.searchFragment ) {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
            else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    private fun splash() {

        binding.splashIcon.alpha=0f
        binding.splashIcon.animate().setDuration(3000).alpha(1f).withEndAction {
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            binding.fragmentContainerView.visibility= View.VISIBLE
            binding.splashIcon.visibility= View.GONE
            setBottomNavigationVisibility()
            supportActionBar?.show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("container" , binding.fragmentContainerView.visibility)
        outState.putInt("splash" ,  binding.splashIcon.visibility)
        super.onSaveInstanceState(outState)
    }
}