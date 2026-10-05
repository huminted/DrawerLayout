package cn.iwakeup.slidedrawer.example

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat


import cn.iwakeup.slidedrawer.DrawerLayout
import cn.iwakeup.slidedrawer.example.fragment.ViewPagerFragment
import cn.iwakeup.slidedrawer.example.list.getBlankFrameLayout

import cn.iwakeup.slidedrawer.example.list.getDrawerContent

class MainActivity : AppCompatActivity() {
    private var open = false
    private var enable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        applyEdgeToEdge()


        val viewPagerFragment = ViewPagerFragment()

        val mainContent = getBlankFrameLayout(this)
        mainContent.id = View.generateViewId()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(mainContent.id, viewPagerFragment)
        transaction.commit()


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.setMainContent(mainContent)
        drawer.setDrawerContent(getDrawerContent(this))
        drawer.setDrawerWidth(300)

        val openOrCloseBtn = findViewById<Button>(R.id.open_or_close_drawer_btn)
        val openOrCloseText = findViewById<TextView>(R.id.open_or_close_drawer_text)
        changeStatusText(openOrCloseText, open, "Opened", "Closed")


        val enableOrDisableBtn = findViewById<Button>(R.id.enable_or_disable_drawer_btn)
        val enableOrDisableTextView = findViewById<TextView>(R.id.enable_or_disable_text)
        changeStatusText(enableOrDisableTextView, enable, "Enabled", "Disabled")

        val progressText = findViewById<TextView>(R.id.drawer_progress_text)


        openOrCloseBtn.setOnClickListener {
            if (open) {
                drawer.closeDrawer()
            } else {
                drawer.openDrawer()
            }
            open = !open
            changeStatusText(openOrCloseText, open, "Opened", "Closed")

        }
        enableOrDisableBtn.setOnClickListener {
            drawer.setDrawerSwipeable(!enable)
            enable = !enable
            changeStatusText(enableOrDisableTextView, enable, "Enabled", "Disabled")
        }

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onProgress(progress: Float) {
                progressText.text = "Open Progress:\n$progress"
            }

            override fun onStart() {

            }

            override fun onEnd() {
            }

        })

    }

    fun changeStatusText(view: TextView, status: Boolean, trueText: String, falseText: String) {
        view.text = if (status) trueText else falseText
    }

    private fun applyEdgeToEdge() {
        val buttonPanel = findViewById<ViewGroup>(R.id.button_panel)
        ViewCompat.setOnApplyWindowInsetsListener(buttonPanel) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            buttonPanel.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        val window = this.window

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
    }
}



