package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {


    private  lateinit var  chatmessageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private  lateinit var sentButton : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var mDbRef : DatabaseReference

    var receiverRoom: String? = null
    var senderRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)



        val name =intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom =  receiverUid + senderUid
        receiverRoom =senderUid + receiverUid
        supportActionBar?.title = name

        chatmessageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.imageButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)


        chatmessageRecyclerView.layoutManager = LinearLayoutManager(this)
        chatmessageRecyclerView.adapter = messageAdapter



        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{


                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapChat in snapshot.children){
                        val message = postSnapChat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        // adding the message to database
        sentButton.setOnClickListener{
            val  message = messageBox.text.toString()
            val messageObject =Message(message,senderUid)


            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText(" ")
        }



    }
}