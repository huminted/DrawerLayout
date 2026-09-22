package cn.iwakeup.slidedrawer.example.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.iwakeup.slidedrawer.example.R

class PageFragment : Fragment(R.layout.fragment_page) {

    override fun onViewCreated(
        view:
        View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val position = requireArguments().getInt("position")

        view.findViewById<TextView>(R.id.textView).text =
            "Page ${position}"


    }

    companion object {
        fun newInstance(position: Int): PageFragment {
            return PageFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }
    }
}