package com.github.watabee.flowsample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    private val adapter = GroupAdapter<RankingViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var snackbar: Snackbar? = null

        fun retry() {
            snackbar?.dismiss()
            snackbar = null

            viewModel.findRankingItems()
        }

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener { retry() }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        recyclerView.adapter = adapter

        viewModel.state.observe(this) { state: MainViewState ->

            adapter.update(state.rankingItemStates.map(::RankingItem))
            swipeRefreshLayout.isRefreshing = state.isLoading

            if (state.isError) {
                snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.error_message,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) { retry() }
                    .apply { show() }
            }
        }
    }
}
