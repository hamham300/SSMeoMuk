package kr.co.hwakjjin.ssmeomuk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.grid_item.view.*

class GridviewAdapter(val context: Context, val img_list : Array<Int>, val text_list : Array<String>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val view : View = LayoutInflater.from(context).inflate(R.layout.grid_item, null)

        view.gridview_text.text = text_list[position]
        view.gridview_img.setImageResource(img_list[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return img_list.size
    }

}