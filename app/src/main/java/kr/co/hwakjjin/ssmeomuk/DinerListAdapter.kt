package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.util.*

class DinerListAdapter( val context: Context,  val data: ArrayList<MenuData>, val itemClick: (MenuData)-> Unit) : RecyclerView.Adapter<DinerListAdapter.Holder>() {



    override fun getItemCount(): Int {
        return data.size
    }


    inner class Holder(view: View?, itemClick: (MenuData) -> Unit) : RecyclerView.ViewHolder(view!!){
        val foodImage  = view?.findViewById<ImageView>(R.id.img_food)
        val menuText = view?.findViewById<TextView>(R.id.txt_menu)
        val rating = view?.findViewById<RatingBar>(R.id.rating)
        val rate = view?.findViewById<TextView>(R.id.txt_rate)
        val price = view?.findViewById<TextView>(R.id.txt_price)
        val bestReview = view?.findViewById<TextView>(R.id.txt_bestReview)
        val reviewUp = view?.findViewById<TextView>(R.id.txt_reviewup)
        val count = view?.findViewById<TextView>(R.id.txt_number)
        val moneyComma: DecimalFormat = DecimalFormat("###,###");

        fun bind(menu: MenuData, context: Context , itemCount: Int){
            count?.text = (itemCount+1).toString()+"."
            foodImage?.setImageResource(menu.getFoodPic())
            menuText?.text = menu.getMenuTxt() + " - " + menu.getDiner()
            rating?.rating = menu.getRate()
            rate?.text = menu.getRate().toString() + "/ 5.0"
            price?.text = moneyComma.format(menu.getPrice()) + "원"
            bestReview?.text = menu.getBestReviewer()+" : "+menu.getBestReview()
            reviewUp?.text = menu.getBestReviewUp().toString()

            itemView.setOnClickListener { itemClick(menu) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.listitem_price, parent, false)
        return Holder(view, itemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position], context, position)
    }

/* 리사이클러뷰가아닌 리스트뷰로 변경시
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val holder: ViewHolder
        val moneyComma: DecimalFormat = DecimalFormat("###,###");


        if(convertView == null){
            view =  LayoutInflater.from(context).inflate(R.layout.listitem_price, null)
            holder = ViewHolder()
            holder.foodImage  = view.findViewById(R.id.img_food)
            holder.menu = view.findViewById(R.id.txt_menu)
            holder.rating = view.findViewById(R.id.rating)
            holder.rate = view.findViewById(R.id.txt_rate)
            holder.price = view.findViewById(R.id.txt_price)
            holder.bestReview = view.findViewById(R.id.txt_bestReview)
            holder.reviewUp = view.findViewById(R.id.txt_reviewup)
            view.tag = holder

        }else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val menu = data[position]
        holder.foodImage?.setImageResource(menu.getFoodPic())
        holder.menu?.text = menu.getMenuTxt() + " - " + menu.getDiner()
        holder.rating?.rating = menu.getRate()
        holder.rate?.text = menu.getRate().toString() + "/ 5.0"
        holder.price?.text = moneyComma.format(menu.getPrice()) + "원"
        holder.bestReview?.text = menu.getBestReviewer()+" : "+menu.getBestReview()
        holder.reviewUp?.text = menu.getBestReviewUp().toString()



        return view
    }

    override fun getItem(p0: Int): Any {
       return data[p0]
    }

    override fun getItemId(p0: Int): Long {
       return 0
    }

    override fun getCount(): Int {
       return data.size
    }

    private class ViewHolder {
        var foodImage : ImageView? = null
        var menu: TextView? = null
        var rating: RatingBar? = null
        var rate: TextView? = null
        var price: TextView? = null
        var bestReview: TextView? = null
        var reviewUp: TextView? = null
    }
*/
}