package com.example.thenewsapp.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thenewsapp.R
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.databinding.FragmentHeadlineBinding
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.util.Resource

class HeadlineFragment : Fragment(R.layout.fragment_headline) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentHeadlineBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로그 추가: Fragment가 생성되었는지 확인
        Log.d("HeadlineFragment", "onViewCreated: Fragment Created")

        binding = FragmentHeadlineBinding.bind(view)
        newsViewModel = (activity as NewsActivity).newsViewModel

        setupHeadlinesRecycler()

        // 환경설정 적용
        applySettings()

        // 뉴스 데이터 옵저빙
        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            Log.d("HeadlineFragment", "onViewCreated: Headlines response received")

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    Log.d("HeadlineFragment", "onViewCreated: Data loaded successfully")
                    response.data?.let { newsResponse ->
                        Log.d("HeadlineFragment", "Articles: ${newsResponse.results}")
                        newsAdapter.differ.submitList(newsResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("HeadlineFragment", "onViewCreated: Error: $message")
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                    Log.d("HeadlineFragment", "onViewCreated: Loading data")
                }
            }
        })

        // 기본적으로 헤드라인 뉴스 가져오기
        newsViewModel.getHeadlines()

        // 뉴스 클릭 시 WebView로 이동 설정
        newsAdapter.setOnItemClickListener { article ->
            article.link?.let {
                val action = HeadlineFragmentDirections.actionHeadlineFragmentToNewPageFragment(article)
                findNavController().navigate(action)
            } ?: run {
                Toast.makeText(activity, "No link available for this article", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // RecyclerView 설정
    private fun setupHeadlinesRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    // 환경설정 적용 메서드
    private fun applySettings() {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val fontSize = sharedPreferences.getString("fontSize", "Medium")
        val textColor = sharedPreferences.getString("textColor", "Black")
        val backgroundColor = sharedPreferences.getString("backgroundColor", "White")

        binding.sampleTextView.textSize = when (fontSize) {
            "Small" -> 14f
            "Medium" -> 18f
            "Large" -> 22f
            else -> 18f
        }

        binding.root.setBackgroundColor(
            when (backgroundColor) {
                "White" -> Color.WHITE
                "Black" -> Color.BLACK
                "Blue" -> Color.BLUE
                "Red" -> Color.RED
                else -> Color.WHITE
            }
        )
    }

    // ProgressBar 숨기기
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    // ProgressBar 표시
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}
