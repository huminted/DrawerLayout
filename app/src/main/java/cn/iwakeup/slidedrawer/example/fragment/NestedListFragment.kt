package cn.iwakeup.slidedrawer.example.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.iwakeup.slidedrawer.example.R
import cn.iwakeup.slidedrawer.example.list.VerticalAdapter

class NestedListFragment : Fragment(R.layout.layout_main_content) {

    override fun onViewCreated(
        view:
        View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        val data = List(10) { row ->

            List(20) { column ->
                "Row ${row + 1} - Item ${column + 1}"
            }
        }
        view.findViewById<RecyclerView>(R.id.list).apply {
            adapter = VerticalAdapter(data)
            layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        fun newInstance(position: Int): NestedListFragment {
            return NestedListFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }
    }
}