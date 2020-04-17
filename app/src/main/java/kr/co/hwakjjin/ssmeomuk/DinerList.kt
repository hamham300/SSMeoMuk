package kr.co.hwakjjin.ssmeomuk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_diner_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class DinerList : AppCompatActivity() {

    lateinit var mAdView : AdView
    private var mList = arrayListOf<MenuData>()
    var context= this
    lateinit var title : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diner_list)

        val intent = intent /*데이터 수신*/
        title = intent.extras!!.getString("title").toString()
        txt_title.text = title

        setListData()




        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        if (title == "~ 5000") {
            btnContainer.visibility = View.VISIBLE
            btn_haksik.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://m.soongguri.com/"))
                startActivity(intent)
            }
            btn_gyosik.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://m.soongguri.com/"))
                startActivity(intent)
            }
            btn_gisik.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://ssudorm.ssu.ac.kr:444/SShostel/mall_main.php?viewform=B0001_foodboard_list&board_no=1")
                )
                startActivity(intent)
            }
        } else {
            btn_gisik.visibility = View.INVISIBLE
            btn_gyosik.visibility = View.INVISIBLE
            btn_haksik.visibility = View.INVISIBLE
            btnContainer.visibility = View.GONE
        }

        btn_back.setOnClickListener{
            finish()
        }


    }


    private fun setListData() {

        mList = ArrayList<MenuData>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("0/menu")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value
                if(value is ArrayList<*>){
                    for (hash in value){
                        if(hash is HashMap<*, *>){
                            val pStr = hash["price"].toString()
                            val price = Integer.parseInt(pStr)

                            if(price <= 5000 && title == "~ 5000")  mList.add(MenuData(R.drawable.sausagerice,  hash["txt_menu"].toString(),hash["txt_diner"].toString(), hash["rate"].toString().toFloat(), price,  hash["bestReviewer"].toString(), hash["bestReview"].toString(), hash["bestReviewUp"].toString().toInt()))
                            else if(price in 5000..6000 && title == "5000 ~ 6000")  mList.add(MenuData(R.drawable.sausagerice,  hash["txt_menu"].toString(),hash["txt_diner"].toString(), hash["rate"].toString().toFloat(), price,  hash["bestReviewer"].toString(), hash["bestReview"].toString(), hash["bestReviewUp"].toString().toInt()))
                            else if(price in 6000..7000 && title == "6000 ~ 7000") mList.add(MenuData(R.drawable.sausagerice,  hash["txt_menu"].toString(),hash["txt_diner"].toString(), hash["rate"].toString().toFloat(), price,  hash["bestReviewer"].toString(), hash["bestReview"].toString(), hash["bestReviewUp"].toString().toInt()))
                            else if(price >= 7000 && title == "7000 ~") mList.add(MenuData(R.drawable.sausagerice,  hash["txt_menu"].toString(),hash["txt_diner"].toString(), hash["rate"].toString().toFloat(), price,  hash["bestReviewer"].toString(), hash["bestReview"].toString(), hash["bestReviewUp"].toString().toInt()))
                        }
                    }
                }

                val adapter = DinerListAdapter(context, mList){ MenuData ->
                    val intent = Intent(applicationContext, MenuDetail::class.java)
                    //intent.putExtra("data", menuData)
                    startActivity(intent)
                }
                recyclerview.adapter = adapter
                recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                val lm = LinearLayoutManager(context)
                recyclerview.layoutManager = lm
                recyclerview.setHasFixedSize(true)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

       // mList.add(MenuData(R.drawable.sausagerice, "소시지필라프 ","3pop", 4.0f, 5000, "jeon", "맛있노", 5))
       // mList.add(MenuData(R.drawable.galbimandu, "갈비만두", "3pop",4.3f, 4000, "kim", "gglglaslaslfasdgj", 3))
       // mList.add(MenuData(R.drawable.cheesestick, "치즈스틱", "3pop",4.0f, 5000, "park", "asdasdasdas", 5))
       // mList.add(MenuData(R.drawable.gyojamandu, "교자만두시발새기ㅏ듬ㄴㄹㄴㅇ", "조온나긴이름이다시발롬들아",4.0f, 5000, "john", "afdffds", 13))
       // mList.add(MenuData(R.drawable.kimchirice, "김치두루치기 덮밥", "3pop",4.0f, 5000, "igi", "00asdf8ags", 45))
    }
}
