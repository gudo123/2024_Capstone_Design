package com.example.thenewsapp.ui

import android.os.Bundle
import android.view.View // 이 줄을 추가하여 View를 임포트합니다.
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thenewsapp.R
import com.example.thenewsapp.databinding.ActivityNewsBinding
import com.example.thenewsapp.db.ArticleDatabase
import com.example.thenewsapp.repository.NewsRepository
import com.example.thenewsapp.ui.fragments.NewPageFragment // NewPageFragment를 import

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        // FloatingActionButton의 동작 설정
        binding.fabBack.setOnClickListener {
            onBackPressed()
        }

        // 현재 Fragment에 따라 FloatingActionButton의 가시성을 제어
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.fabBack.visibility = if (destination.id == R.id.newPageFragment) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as? NewPageFragment
        if (currentFragment?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }
}
