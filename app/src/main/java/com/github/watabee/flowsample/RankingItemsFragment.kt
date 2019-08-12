package com.github.watabee.flowsample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter

class RankingItemsFragment : Fragment(R.layout.fragment_ranking_items) {

    private val viewModel: RankingItemsViewModel by viewModels()
    private val adapter = GroupAdapter<ItemViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var snackbar: Snackbar? = null

        fun retry() {
            snackbar?.dismiss()
            snackbar = null

            viewModel.findRankingItems()
        }

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener { retry() }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        )
        recyclerView.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state: RankingViewState ->

            adapter.update(
                state.itemStates.map { Item(it) { state -> viewModel.toggleFavorite(state) } }
            )
            swipeRefreshLayout.isRefreshing = state.isLoading

            if (state.isError) {
                snackbar = Snackbar.make(view, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { retry() }
                    .apply { show() }
            }
        }
    }

    companion object {
        fun newInstance(): RankingItemsFragment = RankingItemsFragment()
    }
}

data class RankingViewState(
    val itemStates: List<ItemState> = emptyList(),
    val isError: Boolean = false,
    val isLoading: Boolean = false
)