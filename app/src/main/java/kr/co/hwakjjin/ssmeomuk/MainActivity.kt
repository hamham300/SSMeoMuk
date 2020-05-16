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

        val img = arrayOf(
            R.drawable.btn_korean,
            R.drawable.btn_japanese,
            R.drawable.btn_chinese,
            R.drawable.btn_western,
            R.drawable.btn_night_snack,
            R.drawable.btn_franchise,
            R.drawable.btn_cafe,
            R.drawable.btn_drink,
            R.drawable.btn_ranking
        )

        val text = arrayOf(
            "한식",
            "일식",
            "중식",
            "양식",
            "야식",
            "프랜차이즈",
            "카페",
            "술집",
            "랭킹"
        )

        val gridviewAdapter = GridviewAdapter(this, img, text)
        menu_select_gridView.adapter = gridviewAdapter


        // sharedPreference 초기화
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val ed = pref.edit()
        ed.clear()
        ed.apply()
        btn_back.visibility = View.INVISIBLE

        btn_price.setOnClickListener(View.OnClickListener {
            price_select_layout.visibility = View.VISIBLE
            btn_back.visibility = View.VISIBLE
            btn_menu.visibility = View.INVISIBLE
            btn_price.visibility = View.INVISIBLE
        })

        btn_menu.setOnClickListener(View.OnClickListener {
            menu_select_gridView.visibility = View.VISIBLE
            btn_back.visibility = View.VISIBLE
            btn_menu.visibility = View.INVISIBLE
            btn_price.visibility = View.INVISIBLE
        })

        btn_back.setOnClickListener(View.OnClickListener {
            menu_select_gridView.visibility = View.INVISIBLE
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
