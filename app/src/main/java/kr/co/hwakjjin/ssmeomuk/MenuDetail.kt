package kr.co.hwakjjin.ssmeomuk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MenuDetail : AppCompatActivity() {

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

    }
}
