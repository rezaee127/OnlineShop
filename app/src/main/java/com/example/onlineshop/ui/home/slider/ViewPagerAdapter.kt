package com.example.onlineshop.ui.home.slider

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.onlineshop.R
import java.util.*


//https://www.geeksforgeeks.org/android-image-slider-using-viewpager-in-kotlin/
//https://stackoverflow.com/questions/23511045/add-timer-and-set-size-to-viewpager
val handler = Handler()
var swipeTimer=Timer()


class ViewPagerAdapter(val context: Context, private val imageList: ArrayList<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_row_item, container, false)
        itemView.rotationY = 180F

        val imageView: ImageView = itemView.findViewById<View>(R.id.iv_slider) as ImageView
        //imageView.setImageResource(imageList.get(position))

        try {
            Glide.with(context)
                .load(imageList[position])
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .centerCrop()
                .transform(RoundedCorners(50))
                .into(imageView)
        } catch (e: Exception) {
            imageView.setBackgroundResource(R.drawable.error)
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


    fun setTimer(myPager: ViewPager, time: Int) {
        val size: Int = imageList.size
        val update: Runnable = object : Runnable {
            var NUM_PAGES = size
            var currentPage = -1
            override fun run() {
                myPager.setCurrentItem(currentPage++, true)
                if (currentPage == NUM_PAGES) {
                    currentPage = 0
                }
            }
        }
        swipeTimer=Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 1000L, time * 1000L)
    }

    fun stopTimer() {
        swipeTimer.cancel()
    }

}