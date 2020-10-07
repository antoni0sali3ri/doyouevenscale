package de.theopensourceguy.doyouevenscale.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.theopensourceguy.doyouevenscale.MyApp
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.ListableEntity
import de.theopensourceguy.doyouevenscale.ui.activity.EditorActivity
import de.theopensourceguy.doyouevenscale.ui.main.ListableEntityRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 */
class EntityListViewModel<T : ListableEntity>(val items: LiveData<out List<T>>) : ViewModel() {
}

class EntityListFragment<T : ListableEntity>(private val clazz: Class<T>, ) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EntityListViewModel<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = EntityListViewModel(
            MyApp.getDatabase(context).getDaoForClass(clazz).getAll()
        )
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
            }
        }
        viewModel.items.observe(viewLifecycleOwner) {
            recyclerView.adapter = ListableEntityRecyclerViewAdapter(it, onEditItem, {})
        }
        return view
    }

    val onEditItem = { id: Long ->
        startActivity(Intent(requireContext(), EditorActivity::class.java).apply {
            putExtra(EditorActivity.ARG_CLASS, clazz)
            putExtra(EntityEditorFragment.ARG_ITEM_ID, id)
        })
    }
}