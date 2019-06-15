package com.example.newsly.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsly.ArticleActivity
import com.example.newsly.R
import com.example.newsly.model.results
import kotlinx.android.synthetic.main.recycler_row.view.*

class TopStoriesAdapter(val data: results) : RecyclerView.Adapter<TopStoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopStoriesViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return TopStoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.results.size
    }

    override fun onBindViewHolder(holder: TopStoriesViewHolder, position: Int) {
        holder.item.headline.text = data.results[position].title
        holder.item.byline.text = data.results[position].byline
        holder.item.summary.text = data.results[position].abstract
        if (data.results[position].multimedia.isNotEmpty())
            Glide.with(holder.item.context)
                .load(data.results[position].multimedia[0].url)
                .override(100, 140)
                .into(holder.item.image)

        holder.item.setOnClickListener { view ->
            var url = data.results[position].url
            var intent = Intent(view?.context, ArticleActivity::class.java)
            intent.putExtra("url", url)
            view?.context?.startActivity(intent)
        }
    }

}

class TopStoriesViewHolder(var item: View) : RecyclerView.ViewHolder(item)