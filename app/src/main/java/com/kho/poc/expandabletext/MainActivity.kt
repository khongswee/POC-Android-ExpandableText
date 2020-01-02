package com.kho.poc.expandabletext

import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_text_epx.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvExp.adapter = ExpandTextListAdapter()
        rvExp.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    class ExpandTextListAdapter : RecyclerView.Adapter<ExpandViewHolder> {
        lateinit var items: HashMap<Int, ExpandableTextView.State2>

        constructor() {
            items = HashMap<Int, ExpandableTextView.State2>()
            items[1] = ExpandableTextView.State2.Static
            items[2] = ExpandableTextView.State2.Static
            items[3] = ExpandableTextView.State2.Static
            items[4] = ExpandableTextView.State2.Static
            items[5] = ExpandableTextView.State2.Static
            items[6] = ExpandableTextView.State2.Static
            items[7] = ExpandableTextView.State2.Static
            items[8] = ExpandableTextView.State2.Static
            items[9] = ExpandableTextView.State2.Static
            items[10] = ExpandableTextView.State2.Static
            items[11] = ExpandableTextView.State2.Static
            items[12] = ExpandableTextView.State2.Static
            items[13] = ExpandableTextView.State2.Static
            items[14] = ExpandableTextView.State2.Static
            items[154] = ExpandableTextView.State2.Static
            items[152] = ExpandableTextView.State2.Static
            items[155] = ExpandableTextView.State2.Static
            items[157] = ExpandableTextView.State2.Static
            items[151] = ExpandableTextView.State2.Static

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandViewHolder {
            return ExpandViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_text_epx,
                        parent,
                        false
                    )
            )
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ExpandViewHolder, position: Int) {
            items[position]?.let {
                Log.d("ExpandableTextView",position.toString())
                holder.bindPosition(it)
            }
        }

    }

    class ExpandViewHolder(private val iv: View) : RecyclerView.ViewHolder(iv) {

        val expText = iv.expText

        fun bindPosition(state2: ExpandableTextView.State2) {
//            expText.text = iv.context.getString(R.string.lorem_text)
//            expText.state = state2
        }
    }
}
