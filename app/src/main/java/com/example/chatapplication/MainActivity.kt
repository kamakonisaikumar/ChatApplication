package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userlist :ArrayList<User>
    private lateinit var adapter: UserAdapter


    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()


        userlist = ArrayList()
        adapter = UserAdapter(this,userlist)

        userRecyclerView = findViewById(R.id.recycler)
        userRecyclerView .layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

     mDbRef.child("user").addValueEventListener(object: ValueEventListener{

         override fun onDataChange(snapshot: DataSnapshot) {

             userlist.clear()

            for (postSnapshot in snapshot.children){
                val currentUser = postSnapshot .getValue(User::class.java)

                if (mAuth.currentUser?.uid != currentUser?.uid){
                    userlist.add(currentUser!!)
                }



            }
             adapter.notifyDataSetChanged()
         }

         override fun onCancelled(error: DatabaseError) {

         }
     })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId ==R.id.logout){
            // write the login  for logout

            mAuth.signOut()
            val intent =  Intent(this@MainActivity,LogIn::class.java)
           finish()
            startActivity(intent)
            return true
        }
        return true

    }
}