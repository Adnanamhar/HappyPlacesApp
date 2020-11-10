package com.adnanamhar.happyplace.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adnanamhar.happyplace.R
import com.adnanamhar.happyplace.adapter.HappyPlacesAdapter
import com.adnanamhar.happyplace.database.DatabaseHandler
import com.adnanamhar.happyplace.model.HappyPlacesModel
import com.adnanamhar.happyplace.util.SwipeToDeleteCallback
import com.adnanamhar.happyplace.util.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var ADD_HAPPY_ACTIVITY_REQUEST_CODE = 1
        var HAPPY_PLACE_DETAILS = "extra_place_details"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener {
            val intent = Intent(this, AddHappyPlacesActivity::class.java)
            startActivityForResult(intent, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }
//      function untuk get data dari database
        getHappyPlacesFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HAPPY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesFromLocalDB()
            } else {
                Log.e("Activity", "Called or Back Pressed")
            }
        }
    }
    private fun getHappyPlacesFromLocalDB() {
//      variable supaya databasenya bisa kita gunakan di MainActivity atau layar utama
        val dbHandler = DatabaseHandler(this)
//      digunakan untuk menjalankan aksi get data yang berasal dari database Handdler
        val getHappyPlaceList: ArrayList<HappyPlacesModel> = dbHandler.getHappyPlaceList()

//      sebuah kondisi jika data itu ada
        if (getHappyPlaceList.size > 0) {
            rv_places.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE
            setUpHappyPlacesRv(getHappyPlaceList)
//       sebuah kondisi jika data itu tidak ada
        } else {
            rv_places.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
        }
    }

//  function ini digunakan untuk create recycler view didalam main activity
    private fun setUpHappyPlacesRv(happyPlaceList: java.util.ArrayList<HappyPlacesModel>) {
//  untuk mendeteksi data ketika ada perubahan
    rv_places.layoutManager = LinearLayoutManager(this)
//  buat trigger kalau ada data baru
    rv_places.setHasFixedSize(true)

    val adapter = HappyPlacesAdapter(this,happyPlaceList)
    rv_places.adapter = adapter

    adapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
        override fun onClick(position: Int, model: HappyPlacesModel) {
            val intent = Intent(this@MainActivity, HappyDetailActivity::class.java)
            intent.putExtra(HAPPY_PLACE_DETAILS, model)
            startActivity(intent)
        }
    })

    val editSwipeHandler = object : SwipeToEditCallback(this) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val adapter = rv_places.adapter as HappyPlacesAdapter
            adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }
    }

    val editItemTouch = ItemTouchHelper(editSwipeHandler)
    editItemTouch.attachToRecyclerView(rv_places)

    val deleteSwipe = object : SwipeToDeleteCallback(this){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val adapter = rv_places.adapter as HappyPlacesAdapter
            adapter.removeAt(viewHolder.adapterPosition)

            getHappyPlacesFromLocalDB()
        }
    }
    val deleteItem = ItemTouchHelper(deleteSwipe)
    deleteItem.attachToRecyclerView(rv_places)

    }
}