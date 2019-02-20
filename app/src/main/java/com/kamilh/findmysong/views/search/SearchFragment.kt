package com.kamilh.findmysong.views.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamilh.findmysong.R
import com.kamilh.findmysong.base.BaseFragment
import com.kamilh.findmysong.extensions.observeNotNull
import com.kamilh.findmysong.extensions.setShowing
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SongAdapter

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        setUpView()
        setUpObservables()
    }

    private fun setUpView() {
        adapter = SongAdapter(viewModel::itemClicked)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
        chipGroup.sourceChipCheckedListener = viewModel::onSource

        setHasOptionsMenu(true)
    }

    private fun setUpObservables() {
        observeNotNull(viewModel.list) { adapter.submitList(it) }
        observeNotNull(viewModel.isLoading) { progressBar.setShowing(it) }
        observeNotNull(viewModel.isEmptyView) { emptyView.isVisible = it }
        observeNotNull(viewModel.chipConfigs) { chipGroup.set(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)

        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView
        viewModel.queryObservable(Observable.create { source ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { source.onNext(newText) }
                    return true
                }
            })
        })
    }
}
