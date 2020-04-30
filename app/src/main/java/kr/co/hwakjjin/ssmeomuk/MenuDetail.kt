package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import kotlinx.android.synthetic.main.activity_diner_list.*
import kotlinx.android.synthetic.main.activity_menu_detail.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MenuDetail : AppCompatActivity() {
    private var rList = arrayListOf<ReviewData>()
    var context= this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_detail)


        val moneyComma: DecimalFormat = DecimalFormat("###,###");
        val intent = intent /*데이터 수신*/
        val menuCode = intent.extras!!.getString("code")
        val dinerName =  intent.extras!!.getString("txt_diner")
        intent.extras!!.getString("title")
        txt_menu_name.text = intent.extras!!.getString("txt_menu")
        menuPic?.setImageResource( intent.extras!!.getInt("foodPic"))
        rating?.rating = intent.extras!!.getFloat("rate")
        txt_rating?.text = intent.extras!!.getFloat("rate").toString() + "/ 5.0"
        txt_menu_price?.text = moneyComma.format(intent.extras!!.getInt("price")) + "원"
        txt_store_name?.text = dinerName
        txt_bestReview?.text = intent.extras!!.getString("bestReviewer")+" : "+ intent.extras!!.getString("bestReview")

        btn_review.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialog  = builder.create()

            val dialogView = layoutInflater.inflate(R.layout.dialog_review, null)
            val dialogEditNick = dialogView.findViewById<EditText>(R.id.edit_nick)
            val dialogRate = dialogView.findViewById<RatingBar>(R.id.review_rate)
            val dialogEditReview = dialogView.findViewById<EditText>(R.id.edit_review)
            val dialogTxtRate =  dialogView.findViewById<TextView>(R.id.txt_rate)
            val dialogBtnEnroll = dialogView.findViewById<Button>(R.id.btn_enroll)

            val database = FirebaseDatabase.getInstance()
            val reviewPath = "0/review/"+menuCode
            val menuDataPath = "0/menu/"+menuCode
            val reviewRef = database.getReference(reviewPath)
            val menuDataRef = database.getReference(menuDataPath)

            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)

            val reviewHistory = pref.getString(menuCode,"")
            if(reviewHistory != ""){ // 이미 작성글이 있다.
                for(rData in rList){ // 수정하도록 변경
                    if(rData.getCode() == reviewHistory){
                        dialogEditNick.setText(rData.getID())
                        dialogRate.rating= rData.getRate()!!
                        dialogTxtRate.text = rData.getRate().toString() + "/ 5.0"
                        dialogBtnEnroll.text = "수정"
                        dialogEditReview.setText(rData.getReview())
                    }
                }
            }

            dialogRate.setOnRatingBarChangeListener { ratingBar, fl, b ->
                dialogTxtRate.text = fl.toString() + "/ 5.0"
            }
            dialogBtnEnroll.setOnClickListener {


                val date = Date(System.currentTimeMillis())
                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss");
                val strDate = sdfNow.format(date);
                val randKey = Random().nextInt()
                val myReview: ReviewData

                if(reviewHistory != ""){// 이미 작성글이 있다.
                    myReview = ReviewData(reviewHistory!!,dialogEditNick.text.toString(),dialogEditReview.text.toString(),dialogRate.rating,0,strDate)
                    var map = mutableMapOf<String,ReviewData>()
                    map[reviewHistory] = myReview
                    reviewRef.updateChildren(map as Map<String, Any>)
                }else{
                    myReview = ReviewData(strDate+randKey.toString(),dialogEditNick.text.toString(),dialogEditReview.text.toString(),dialogRate.rating,0,strDate)
                    reviewRef.child(strDate+randKey.toString()).setValue(myReview)
                    val ed = pref.edit()
                    ed.putString(menuCode,strDate+randKey.toString()) //내가쓴 리뷰의 리뷰코드를 기기내에 가지고있는다.
                    ed.apply()
                }



                dialog.dismiss()
                reviewList.adapter?.notifyDataSetChanged()
            }
            dialog.setView(dialogView)
            dialog.show()

        }

        val database = FirebaseDatabase.getInstance()
        val dinerPath = "0/diner/"+dinerName
        val myRef = database.getReference(dinerPath)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value

                value as HashMap <*,*>
                txt_store_location.text  = value["location"].toString()
                txt_store_rate.text = value["rate"].toString()+ "/ 5.0"
                rating_store.rating = value["rate"].toString().toFloat()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        val reviewPath = "0/review/"+menuCode
        val reviewRef = database.getReference(reviewPath)

        reviewRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                rList.clear()
                if(dataSnapshot.exists()){
                    val value = dataSnapshot.value
                    value as HashMap <*,*>
                    for (hash in value) {
                        val content = hash.value;
                        if (content is HashMap<*, *>) {
                            val pStr = content["price"].toString()

                            rList.add(ReviewData(content["code"].toString(),content["id"].toString(),content["review"].toString(),
                                content["rate"].toString().toFloat(),content["up"].toString().toInt(),content["date"].toString()))
                        }
                    }
                    val adapter = ReviewListAdapter(context, rList){ ReviewData->

                    }
                    reviewList.adapter = adapter
                    reviewList.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                    val lm = LinearLayoutManager(context)
                    reviewList.layoutManager = lm
                    reviewList.setHasFixedSize(true)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


        fun refreshBestReview( list: ArrayList<ReviewData>){
            for(rData in list){

            }
        }


    }
}
