package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // sharedPreference 초기화
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val ed = pref.edit()
        ed.clear()
        ed.apply()

        btn_price.setOnClickListener(View.OnClickListener {
            price_select_layout.visibility = View.VISIBLE
            btn_back.visibility = View.VISIBLE
            btn_menu.visibility = View.INVISIBLE
            btn_price.visibility = View.INVISIBLE
        })

        btn_menu.setOnClickListener(View.OnClickListener {
            menu_select_layout.visibility = View.VISIBLE
            btn_back.visibility = View.VISIBLE
            btn_menu.visibility = View.INVISIBLE
            btn_price.visibility = View.INVISIBLE
        })

        btn_back.setOnClickListener(View.OnClickListener {
            menu_select_layout.visibility = View.INVISIBLE
            price_select_layout.visibility = View.INVISIBLE
            btn_back.visibility = View.INVISIBLE
            btn_menu.visibility = View.VISIBLE
            btn_price.visibility = View.VISIBLE
        })

        btn_under5.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, DinerList::class.java)
            intent.putExtra("title", "~ 5000")
            startActivity(intent)
        })
        btn_5to6.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, DinerList::class.java)
            intent.putExtra("title", "5000 ~ 6000")
            startActivity(intent)
        })
        btn_6to7.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, DinerList::class.java)
            intent.putExtra("title", "6000 ~ 7000")
            startActivity(intent)
        })
        btn_over7.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, DinerList::class.java)
            intent.putExtra("title", "7000 ~")
            startActivity(intent)
        })

        test_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}
