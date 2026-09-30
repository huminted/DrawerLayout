package cn.iwakeup.slidedrawer.example

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.iwakeup.slidedrawer.DrawerLayout
import cn.iwakeup.slidedrawer.example.list.VerticalAdapter

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.setMainContent(getMainContent())
        drawer.setDrawerContent(getDrawerContent())
        drawer.setDrawerWidth(250)


    }


    fun getDrawerContent(): View {

        return LayoutInflater.from(this).inflate(R.layout.layout_drawer_content, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }


    fun getMainContent(): View {
        val mainContentView =
            LayoutInflater.from(this).inflate(R.layout.layout_main_content, null).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

        val data = List(10) { row ->

            List(20) { column ->
                "R${row + 1} - ${column + 1}"
            }
        }
        mainContentView.findViewById<RecyclerView>(R.id.list).apply {
            adapter = VerticalAdapter(data)
            layoutManager = LinearLayoutManager(context)
        }

        return mainContentView
    }
}



