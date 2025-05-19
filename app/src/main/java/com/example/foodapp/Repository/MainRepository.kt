package com.example.foodapp.Repository

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.Model.CategoryModel
import com.example.foodapp.Model.ItemsModel
import com.example.foodapp.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private  val firebaseDatabase = FirebaseDatabase.getInstance();


    fun loadBanner(): LiveData<MutableList<SliderModel>>{
        val listData = MutableLiveData<MutableList<SliderModel>>()
        val ref = firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for(childSnapshot in snapshot.children){
                    val item =  childSnapshot.getValue(SliderModel ::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value=  lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>>{
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for(childSnapshot in snapshot.children){
                    val item =  childSnapshot.getValue(CategoryModel ::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value=  lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }

    fun loadBestSeller(): LiveData<MutableList<ItemsModel>>{
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        ref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for(childSnapshot in snapshot.children){
                    val item =  childSnapshot.getValue(ItemsModel ::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value=  lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }
    fun loadFiltered(id : String) : LiveData<MutableList<ItemsModel>>{
         val listData= MutableLiveData<MutableList<ItemsModel>>()
         val ref = firebaseDatabase.getReference("Items")
         val query: Query = ref.orderByChild("categoryId").equalTo(id)

         query.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val lists = mutableListOf<ItemsModel>()
                 for (childSnapshot in snapshot.children) {
                     val list = childSnapshot.getValue(ItemsModel::class.java)
                     if (list != null) {
                         lists.add(list)
                     }
                 }
                 listData.value= lists
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }
         })

         return listData
     }

    fun searchItems(query: String): LiveData<List<ItemsModel>> {
        val result = MutableLiveData<List<ItemsModel>>()

        val ref = firebaseDatabase.getReference("Items")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (child in snapshot.children) {
                    val item = child.getValue(ItemsModel::class.java)
                    if (item != null && item.title.contains(query, ignoreCase = true)) {
                        list.add(item)
                    }
                }
                result.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                result.value = emptyList()
            }
        })

        return result
    }

}