package com.example.thenewsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thenewsapp.databinding.FragmentNewPageBinding
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.ui.fragments.NewPageFragmentArgs

class NewPageFragment : Fragment() {

    // ViewModel 선언
    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentNewPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding을 통해 레이아웃 초기화
        binding = FragmentNewPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NewsActivity에서 ViewModel 가져오기
        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = arguments?.let { NewPageFragmentArgs.fromBundle(it).article }

        // 전달된 article 객체를 로그로 출력하여 확인
        Log.d("NewPageFragment", "Article received: ${article?.title}")

        article?.let {
            setupWebView(it.link  ?: "https://www.example.com")

            // FloatingActionButton 클릭 리스너 설정
            binding.fab.setOnClickListener {
                newsViewModel.addToFavourites(article)  // 기사를 즐겨찾기에 추가
                Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // WebView 설정
    private fun setupWebView(url: String) {
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)

                    activity?.let {
                        Toast.makeText(it, "Failed to load page", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Log.e("NewPageFragment", "Activity is null, cannot show Toast")
                    }
                }
            }
            settings.javaScriptEnabled = true // 필요 시 자바스크립트 활성화
            loadUrl(url)
        }
    }

    // Fragment 내에서 백 버튼 처리 (Activity에서 호출 필요)
    fun onBackPressed(): Boolean {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return false
    }
}
