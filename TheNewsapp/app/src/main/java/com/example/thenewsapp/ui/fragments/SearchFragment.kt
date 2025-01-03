package com.example.thenewsapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsapp.R
import com.example.thenewsapp.databinding.FragmentSearchBinding
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import android.util.Log

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemSearchError: CardView
    lateinit var binding: FragmentSearchBinding

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
        applySettings()

        itemSearchError = view.findViewById(R.id.itemSearchError)

        // 재시도 버튼과 에러 텍스트 초기화
        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupSearchRecycler()

        // 뉴스 아이템 클릭 리스너 설정
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle)
        }

        var job: Job? = null
        binding.searchEdit.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay()
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        // 엔터 키를 눌렀을 때 검색이 실행되도록 설정
        binding.searchEdit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                // 검색 실행
                val query = binding.searchEdit.text.toString()
                if (query.isNotEmpty()) {
                    // 검색어 디코딩 및 로그 출력
                    val decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8.toString())
                    Log.d("SearchQuery_DEBUG", "Decoded query: $decodedQuery")

                    // 실제 API 호출
                    newsViewModel.searchNews(query)
                }
                true // 엔터 이벤트를 처리했음을 알림
            } else {
                false
            }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                // 에러나 성공에 따른 UI 업데이트
                else -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.results.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
                        }
                    }
                }
            }
        })

        // 재시도 버튼 클릭 리스너 설정
        retryButton.setOnClickListener {
            if (binding.searchEdit.text.toString().isNotEmpty()) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    private fun delay() {
        // 딜레이 로직 구현
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemSearchError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupSearchRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    // 설정 값을 불러와 UI에 반영하는 메서드
    private fun applySettings() {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // 저장된 설정 값 불러오기
        val fontSize = sharedPreferences.getString("fontSize", "Medium")
        val textColor = sharedPreferences.getString("textColor", "Black")
        val backgroundColor = sharedPreferences.getString("backgroundColor", "White")

        // EditText (검색창)의 폰트 크기 및 텍스트 색상 적용
        binding.searchEdit.textSize = when (fontSize) {
            "Small" -> 14f
            "Medium" -> 18f
            "Large" -> 22f
            else -> 18f
        }

        binding.searchEdit.setTextColor(
            when (textColor) {
                "Black" -> Color.BLACK
                "Blue" -> Color.BLUE
                "Red" -> Color.RED
                else -> Color.BLACK
            }
        )

        // 전체 배경 색상 적용
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
}
