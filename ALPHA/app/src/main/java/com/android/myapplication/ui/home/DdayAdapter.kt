package com.android.myapplication.ui.home


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class DdayAdapter(var pageNum: ArrayList<Int>) :
    RecyclerView.Adapter<DdayAdapter.PagerViewHolder>() {
    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (
        LayoutInflater.from(parent.context).inflate(R.layout.viewpager_slide_dday, parent, false)
    ) {
        val ddayback = itemView.findViewById<LinearLayout>(R.id.dday_back)
        val ddaytitle = itemView.findViewById<TextView>(R.id.dday_title)
        val ddaynum1 = itemView.findViewById<TextView>(R.id.dday_num1)
        val ddaynum2 = itemView.findViewById<TextView>(R.id.dday_num2)
        val ddaynum3 = itemView.findViewById<TextView>(R.id.dday_num3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = pageNum.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        when(position){
            0 -> {
                holder.ddaytitle.text = "수능"
                holder.ddaytitle.setBackgroundResource(R.drawable.bg_main_dday_title_blue)
                holder.ddayback.setBackgroundResource(R.drawable.bg_main_dday_blue)
                holder.ddaynum1.setTextColor(Color.parseColor("#0057FF"))
                holder.ddaynum2.setTextColor(Color.parseColor("#0057FF"))
                holder.ddaynum3.setTextColor(Color.parseColor("#0057FF"))
                // dday
                val start = "2024-11-14"
                val dday = DdayCalculate(start)
                holder.ddaynum1.text = dday[0].toString()
                holder.ddaynum2.text = dday[1].toString()
                holder.ddaynum3.text = dday[2].toString()

            }
            1 -> {
                holder.ddaytitle.text = "6월 모의고사"
                holder.ddaytitle.setBackgroundResource(R.drawable.bg_main_dday_title_red)
                holder.ddayback.setBackgroundResource(R.drawable.bg_main_dday_red)
                holder.ddaynum1.setTextColor(Color.parseColor("#FF4848"))
                holder.ddaynum2.setTextColor(Color.parseColor("#FF4848"))
                holder.ddaynum3.setTextColor(Color.parseColor("#FF4848"))
                val start = "2024-06-04"
                val dday = DdayCalculate(start)
                holder.ddaynum1.text = dday[0].toString()
                holder.ddaynum2.text = dday[1].toString()
                holder.ddaynum3.text = dday[2].toString()
            }
            else -> {
                holder.ddaytitle.text = "9월 모의고사"
                holder.ddaytitle.setBackgroundResource(R.drawable.bg_main_dday_title_green)
                holder.ddayback.setBackgroundResource(R.drawable.bg_main_dday_green)
                holder.ddaynum1.setTextColor(Color.parseColor("#98F690"))
                holder.ddaynum2.setTextColor(Color.parseColor("#98F690"))
                holder.ddaynum3.setTextColor(Color.parseColor("#98F690"))
                val start = "2024-09-04"
                val dday = DdayCalculate(start)
                holder.ddaynum1.text = dday[0].toString()
                holder.ddaynum2.text = dday[1].toString()
                holder.ddaynum3.text = dday[2].toString()
            }
        }
    }
    fun DdayCalculate(start : String) : String {
        val now = LocalDate.now()
        val startDay = LocalDate.parse(start)
        val dday = ChronoUnit.DAYS.between(now,startDay)
        var readlDday = ""
        if (dday < 10){
            readlDday = "00$dday"
        } else if (dday < 100)
            readlDday = "0$dday"
        else {
            readlDday = dday.toString()
        }
        return readlDday
    }
}