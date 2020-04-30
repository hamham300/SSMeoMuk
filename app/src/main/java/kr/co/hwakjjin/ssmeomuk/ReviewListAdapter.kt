package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sackcentury.shinebuttonlib.ShineButton
import java.util.ArrayList

class ReviewListAdapter (val context: Context, val data: ArrayList<ReviewData>, val itemClick: (ReviewData)-> Unit) : RecyclerView.Adapter<ReviewListAdapter.Holder>() {

    override fun getItemCount(): Int {
        return data.size
    }


    inner class Holder(view: View?, itemClick: (ReviewData) -> Unit) : RecyclerView.ViewHolder(view!!){
        val reviewer  = view?.findViewById<TextView>(R.id.reviewer)
        val reviewTxt =  view?.findViewById<TextView>(R.id.txt_review)
        val reviewUp = view?.findViewById<TextView>(R.id.reviewUp)
        val rate = view?.findViewById<RatingBar>(R.id.review_rate)
        val rateText = view?.findViewById<TextView>(R.id.txt_review_rate)
        val btnUp =  view?.findViewById<ShineButton>(R.id.btn_up)

        fun bind(review: ReviewData, context: Context , itemCount: Int){

            reviewer?.text = review.getID()
            reviewTxt?.text = review.getReview()
            reviewUp?.text = review.getUp().toString()
            rate?.rating = review.getRate()!!.toFloat()
            rateText?.text =  review.getRate()!!.toFloat().toString()

            itemView.setOnClickListener { itemClick(review) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.listitem_review, parent, false)
        return Holder(view, itemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position], context, position)
    }
}