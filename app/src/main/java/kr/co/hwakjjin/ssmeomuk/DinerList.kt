package kr.co.hwakjjin.ssmeomuk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class DinerList : AppCompatActivity() {

    lateinit var mAdView : AdView
    private var mList = ArrayList<MenuData>()
    var context= this
    lateinit var title : String
    var adapter = DinerListAdapter(context,mList){}

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



        btn_sort.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialog  = builder.create()

            val dialogView = layoutInflater.inflate(R.layout.dialog_sort, null)
            val dialogBtnRate = dialogView.findViewById<Button>(R.id.btn_rate)
            val dialogBtnHighPrice = dialogView.findViewById<Button>(R.id.btn_low_price)
            val dialogBtnLowPrice = dialogView.findViewById<Button>(R.id.btn_high_price)
            val dialogBtnRateNum = dialogView.findViewById<Button>(R.id.btn_rate_num)
            dialogBtnRate.setOnClickListener{
                val a = ArrayList(mList.sortedByDescending {it.getRate() })
                mList.clear()
                mList.addAll(a)
                recyclerview.adapter?.notifyDataSetChanged()
                btn_sort.text = "평점순"
                dialog.dismiss()
            }
            dialogBtnHighPrice.setOnClickListener{
                val a =  ArrayList(mList.sortedBy {it.getPrice() })
                mList.clear()
                mList.addAll(a)
                recyclerview.adapter?.notifyDataSetChanged()
                btn_sort.text = "가격낮은순";
                dialog.dismiss()
            }
            dialogBtnLowPrice.setOnClickListener{
                val a =  ArrayList(mList.sortedByDescending {it.getPrice() })
                mList.clear()
                mList.addAll(a)
                recyclerview.adapter?.notifyDataSetChanged()
                btn_sort.text = "가격높은순";
                dialog.dismiss()
            }
            dialogBtnRateNum.setOnClickListener{
                dialog.dismiss()
            }

            dialog.setView(dialogView)
            dialog.show()
        }


    }

    override fun onResume() {
        super.onResume()
       // setListData()
    }

    private fun setListData() {

       // mList = ArrayList<MenuData>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("0/menu")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val value = dataSnapshot.value
                if(value is HashMap<*,*>){
                    for (hash in value){
                        val code = hash.key;
                        val content = hash.value;
                        if(content is HashMap<*,*>) {
                            Log.d("Test", "@drawable/$code")
                            var menuPic = context.resources.getIdentifier("@drawable/$code","id", context.packageName)
                            if(menuPic == 0) menuPic = R.drawable.defaultpic
                            Log.d("Test",menuPic.toString())
                            if(title == "~ 5000" ||  title == "5000 ~ 6000" || title == "6000 ~ 7000" || title == "7000 ~") {
                                val pStr = content["price"].toString()
                                val price = Integer.parseInt(pStr)
                                Log.d("Test",code.toString())
                                if (price <= 5000 && title == "~ 5000") mList.add(
                                    MenuData(
                                        code.toString(),
                                        menuPic,
                                        content["txt_menu"].toString(),
                                        content["txt_diner"].toString(),
                                        content["rate"].toString().toFloat(),
                                        price,
                                        content["bestReviewer"].toString(),
                                        content["bestReview"].toString(),
                                        content["bestReviewUp"].toString().toInt()
                                    )
                                )
                                else if (price in 5000..6000 && title == "5000 ~ 6000") mList.add(
                                    MenuData(
                                        code.toString(),
                                        menuPic,
                                        content["txt_menu"].toString(),
                                        content["txt_diner"].toString(),
                                        content["rate"].toString().toFloat(),
                                        price,
                                        content["bestReviewer"].toString(),
                                        content["bestReview"].toString(),
                                        content["bestReviewUp"].toString().toInt()
                                    )
                                )
                                else if (price in 6000..7000 && title == "6000 ~ 7000") mList.add(
                                    MenuData(
                                        code.toString(),
                                        menuPic,
                                        content["txt_menu"].toString(),
                                        content["txt_diner"].toString(),
                                        content["rate"].toString().toFloat(),
                                        price,
                                        content["bestReviewer"].toString(),
                                        content["bestReview"].toString(),
                                        content["bestReviewUp"].toString().toInt()
                                    )
                                )
                                else if (price >= 7000 && title == "7000 ~") mList.add(
                                    MenuData(
                                        code.toString(),
                                        menuPic,
                                        content["txt_menu"].toString(),
                                        content["txt_diner"].toString(),
                                        content["rate"].toString().toFloat(),
                                        price,
                                        content["bestReviewer"].toString(),
                                        content["bestReview"].toString(),
                                        content["bestReviewUp"].toString().toInt()
                                    )
                                )
                            }
                            else {
                                val typeStr = content["type"].toString()
                                if (typeStr == title ) mList.add(
                                    MenuData(
                                        code.toString(),
                                        menuPic,
                                        content["txt_menu"].toString(),
                                        content["txt_diner"].toString(),
                                        content["rate"].toString().toFloat(),
                                        content["price"].toString().toInt(),
                                        content["bestReviewer"].toString(),
                                        content["bestReview"].toString(),
                                        content["bestReviewUp"].toString().toInt()
                                    )
                                )
                            }
                        }
                    }
                }

                 adapter = DinerListAdapter(context, mList){ MenuData ->
                    val intent = Intent(applicationContext, MenuDetail::class.java)
                    intent.putExtra("code", MenuData.getCode())
                    intent.putExtra("foodPic", MenuData.getFoodPic())
                    intent.putExtra("txt_menu", MenuData.getMenuTxt())
                    intent.putExtra("txt_diner", MenuData.getDiner())
                    intent.putExtra("rate", MenuData.getRate())
                    intent.putExtra("price", MenuData.getPrice())
                    intent.putExtra("bestReviewer", MenuData.getBestReviewer())
                    intent.putExtra("bestReview", MenuData.getBestReview())
                    intent.putExtra("bestReviewUp", MenuData.getBestReviewUp())

                    startActivity(intent)
                }
                recyclerview.adapter = adapter
                recyclerview.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                

                val lm = LinearLayoutManager(context)
                recyclerview.layoutManager = lm
                recyclerview.setHasFixedSize(true)

                btn_sort.visibility = View.VISIBLE

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

    }
}
