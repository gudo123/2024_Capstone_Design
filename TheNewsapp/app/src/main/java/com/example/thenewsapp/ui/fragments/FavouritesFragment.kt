package com.example.thenewsapp.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsapp.R
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.databinding.FragmentFavouritesBinding
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentFavouritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        // 설정 값 적용
        applySettings()

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavouritesRecycler()

        // 아이템 클릭 리스너 설정
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_articleFragment, bundle)
        }

        // 스와이프하여 아이템 삭제 설정
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed from favourites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.addToFavourites(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        // 즐겨찾기된 기사 리스트를 옵저빙하여 리사이클러뷰에 업데이트
        newsViewModel.getFavouriteNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    // 즐겨찾기 리사이클러뷰 설정
    private fun setupFavouritesRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    // 설정 값을 불러와 UI에 반영하는 메서드
    private fun applySettings() {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // 저장된 설정 값 불러오기
        val fontSize = sharedPreferences.getString("fontSize", "Medium")
        val textColor = sharedPreferences.getString("textColor", "Black")
        val backgroundColor = sharedPreferences.getString("backgroundColor", "White")

        // TextView의 폰트 크기 및 색상 적용 (예시: 리스트 아이템에 적용 가능)
        binding.recyclerFavourites.apply {
            // 아이템의 폰트 크기 및 텍스트 색상 등을 적용하려면 해당 뷰에 접근
            adapter?.let {
                // 설정을 적용할 수 있음
            }
        }

        // 배경 색상 적용
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
