package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.EntityViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.ListableEntity
import com.github.antoni0sali3ri.doyouevenscale.ui.activity.EditorActivity
import com.github.antoni0sali3ri.doyouevenscale.ui.adapter.ListableEntityRecyclerViewAdapter
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor.EntityEditorFragment

abstract class EntityListFragment<T : ListableEntity>(private val clazz: Class<T>) : Fragment() {

    protected abstract val viewModel: EntityViewModel<T>
    protected lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListableEntityRecyclerViewAdapter<T>

    protected open fun getAdapter(items: List<T>): RecyclerView.Adapter<*> {
        val adapter = ListableEntityRecyclerViewAdapter(items, onEditItem, onDeleteItem)
        this.adapter = adapter
        return adapter
    }

    protected open fun setItems(items: List<T>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    protected open fun setUpRecyclerView(recyclerView: RecyclerView, items: List<T>) {
        recyclerView.adapter = getAdapter(items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entity_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            with(view) {
                layoutManager = LinearLayoutManager(context)
                val deco = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                //deco.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)!!)
                addItemDecoration(deco)
            }
        }
        setUpRecyclerView(recyclerView, emptyList())
        viewModel.items.observe(viewLifecycleOwner) { items ->
            lifecycleScope.launchWhenStarted {
                setItems(items)
            }
        }
        return view
    }

    fun createItem() {
        launchEditor(requireContext(), clazz)
    }

    protected val onEditItem = { id: Long ->
        startActivity(Intent(requireContext(), EditorActivity::class.java).apply {
            putExtra(EditorActivity.ARG_CLASS, clazz)
            putExtra(EntityEditorFragment.ARG_ITEM_ID, id)
        })
    }

    protected val onDeleteItem: (Long) -> Unit = { id: Long ->
        viewModel.delete(id)
    }

    companion object {
        fun launchEditor(context: Context, forClass: Class<out ListableEntity>) {
            ContextCompat.startActivity(
                context,
                Intent(context, EditorActivity::class.java).apply {
                    putExtra(EditorActivity.ARG_CLASS, forClass)
                    putExtra(EntityEditorFragment.ARG_ITEM_ID, 0L)
                },
                null
            )

        }
    }
}