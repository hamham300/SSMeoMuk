package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_menu_detail.*
import org.json.JSONArray
import org.json.JSONException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MenuDetail : AppCompatActivity(), OnMapReadyCallback{
    private var rList = arrayListOf<ReviewData>()
    private var UpList = arrayListOf<String>()
    var context= this
    var location : LatLng = LatLng(37.487936, 126.825071)
    lateinit var naver_map : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_detail)

        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)

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
        bestReviewUp?.text = intent.extras!!.getInt("bestReviewUp").toString()
        btn_up?.isChecked
        btn_up?.setChecked(true,true)
        btn_up.isClickable = false
        val jsonUp = pref.getString(menuCode+"Up",null)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naver_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id. naver_map, it).commit()
            }
        mapFragment.getMapAsync(this)

        if (jsonUp != null) {
            try {
                val a = JSONArray(jsonUp)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    UpList.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

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

                var rateAvg = 0.0f
                for(rv in rList){
                    rateAvg += rv.getRate()!!
                }
                rateAvg += myReview.getRate()!!
                rating.rating = rateAvg/(rList.size+1)
                txt_rating.text =  ( rateAvg/(rList.size+1)).toString() + "/ 5.0"

                menuDataRef.child("rate").setValue( rateAvg/(rList.size+1))

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
                location = LatLng(value["pointX"].toString().toDouble(), value["pointY"].toString().toDouble())
                if(::naver_map.isInitialized){
                    val marker = Marker()
                    marker.position = location
                    marker.map = naver_map
                    marker.icon = MarkerIcons.PINK
                    naver_map.cameraPosition = CameraPosition(location, 18.0)
                }
                txt_store_rate.text = value["rate"].toString()+ "/ 5.0"
                rating_store.rating = value["rate"].toString().toFloat()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })


        val reviewPath = "0/review/"+menuCode
        val reviewRef = database.getReference(reviewPath)
        val menuDataPath = "0/menu/"+menuCode
        val menuDataRef = database.getReference(menuDataPath)

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
                    val adapter = ReviewListAdapter(context, rList, UpList){ ReviewData->
                        var isClicked = false
                        val thisUp = ReviewData.getUp()
                        if(!UpList.isEmpty()) {
                            var delIndex = -1
                            UpList.forEachIndexed { index, s ->
                                if (ReviewData.getCode() == s) {// 눌렀는데 이미 누른거야
                                    isClicked =true
                                    delIndex = index
                                    //UpList.removeAt(index)
                                }
                            }
                            if(delIndex > -1)
                            UpList.removeAt(delIndex)
                            if (thisUp != null) {
                                reviewRef.child(ReviewData.getCode().toString()).child("up").setValue(thisUp-1)

                                if( (ReviewData.getID()+" : "+ ReviewData.getReview() )== txt_bestReview?.text){ //최고리뷰 따봉 취소 // 최고리뷰인지 알아내는 코드 위험성 존재
                                    menuDataRef.child("bestReviewUp").setValue(thisUp-1)
                                    for(rv in rList){
                                        if(rv.getUp()!! > (thisUp-1)){ // 만약 리뷰들중에 지금 이거 따봉 취소하면 최고리뷰가 바뀐다
                                            menuDataRef.child("bestReview").setValue(rv.getReview())
                                            menuDataRef.child("bestReviewer").setValue(rv.getID())
                                            menuDataRef.child("bestReviewUp").setValue(rv.getUp())
                                            txt_bestReview?.text = rv.getID()+" : "+ rv.getReview()
                                            txt_best_rate?.text = ReviewData.getRate().toString()
                                            bestReviewUp?.text = rv.getUp().toString()
                                            break
                                        }
                                    }
                                }
                            }
                        }

                        if(!isClicked){ // 이번에 새로 누름 -> 저장
                            UpList.add( ReviewData.getCode().toString()) // 해당 리뷰의 코드를 SharedPreferences에 저장후
                            if (thisUp != null) {
                                reviewRef.child(ReviewData.getCode().toString()).child("up").setValue(thisUp+1)
                                if ((thisUp+1) > ((bestReviewUp.text as String).toInt())){ // 따봉을 줘가지고 준게 최고 따봉이 되었다.
                                    menuDataRef.child("bestReview").setValue(ReviewData.getReview())
                                    menuDataRef.child("bestReviewer").setValue(ReviewData.getID())
                                    menuDataRef.child("bestReviewUp").setValue(thisUp+1)
                                    //menuDataRef.child("bestReviewCode").setValue(ReviewData.getCode())
                                    txt_bestReview?.text = ReviewData.getID()+" : "+ ReviewData.getReview()
                                    txt_best_rate?.text = ReviewData.getRate().toString()
                                    bestReviewUp?.text = (thisUp+1).toString()
                                }
                            }
                        }

                        val json = JSONArray()
                        for(i in UpList){
                            json.put(i)
                        }

                        val ed = pref.edit()
                        ed.putString(menuCode+"Up",json.toString()) //내가쓴 리뷰의 리뷰코드를 기기내에 가지고있는다.
                        ed.apply()

                       reviewList.adapter?.notifyDataSetChanged()


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


        fun refreshBestReview( list: ArrayList<ReviewData>) {
            for (rData in list) {

            }
        }
    }

    @UiThread
    @Override
    override fun onMapReady(naverMap: NaverMap) {
        naver_map = naverMap
        val marker = Marker()
        marker.position = location
        marker.map = naverMap
        marker.icon = MarkerIcons.PINK
        naverMap.cameraPosition = CameraPosition(location, 18.0)

    }
}
