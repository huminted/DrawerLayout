package cn.iwakeup.slidedrawer.example.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.iwakeup.slidedrawer.example.R

class HorizontalAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView =
            view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_horizontal,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.textView.text = items[position]
    }

    override fun getItemCount() = items.size
}