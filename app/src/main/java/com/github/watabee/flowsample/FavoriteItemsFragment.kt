package com.github.watabee.flowsample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter

class FavoriteItemsFragment : Fragment(R.layout.fragment_favorite_items) {

    private val viewModel: FavoriteItemsViewModel by viewModels()
    private val adapter = GroupAdapter<ItemViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        )
        recyclerView.adapter = adapter

        val noItemsTextView: TextView = view.findViewById(R.id.text_no_items)

        viewModel.state.observe(viewLifecycleOwner) { state: FavoriteViewState ->
            if (state.itemStates.isEmpty()) {
                recyclerView.visibility = View.GONE
                noItemsTextView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                noItemsTextView.visibility = View.GONE
                adapter.update(
                    state.itemStates.map { Item(it) { state -> viewModel.toggleFavorite(state) } }
                )
            }

            if (state.isError) {
                Snackbar.make(view, R.string.error_message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun newInstance(): FavoriteItemsFragment = FavoriteItemsFragment()
    }
}

data class FavoriteViewState(
    val itemStates: List<ItemState> = emptyList(),
    val isError: Boolean = false
)