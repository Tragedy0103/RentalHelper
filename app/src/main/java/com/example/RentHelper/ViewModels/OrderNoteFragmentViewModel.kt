package com.example.RentHelper.ViewModels

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.RentHelper.R
import com.example.RentHelper.Tool.*
import com.example.RentHelper.ViewModels.IType.MessageBodyViewModel
import com.example.RentHelper.models.AccountModel
import com.example.RentHelper.models.NoteModel

class OrderNoteFragmentViewModel(val orderId: String) : Observed<OrderNoteFragmentViewModel>() {

    val adapter = CommonAdapter.Builder(ArrayList())
        .addFooter {
            val view =
                LayoutInflater.from(it.context).inflate(R.layout.item_list_note_footer, it, false)
            val imgSend = view.findViewById<ImageView>(R.id.imgSend)
            val etMessage = view.findViewById<EditText>(R.id.etMessage)
            imgSend.setOnClickListener {
                sendMessage(etMessage.text.toString(), { str ->
                    etMessage.setText("")
                }, { str ->
                    Toast.makeText(it.context, str, Toast.LENGTH_SHORT).show()
                })
            }
            object : BaseViewHolder<IType>(view) {
                override fun bind(item: IType) {
                }
            }
        }
        .addType(
            {
                val view =
                    LayoutInflater.from(it.context)
                        .inflate(R.layout.item_list_note_body, it, false)
                object : BaseViewHolder<MessageBodyViewModel>(view) {
                    override fun bind(item: MessageBodyViewModel) {
                        itemView.findViewById<TextView>(R.id.tvName).text = item.name + "："
                        itemView.findViewById<TextView>(R.id.tvDate).text = item.date.toString()
                        itemView.findViewById<TextView>(R.id.tvTime).text = item.time.toString()
                        itemView.findViewById<TextView>(R.id.tvMessage).text = item.message
                    }
                }
            }, ViewType.MessageBodyViewModel
        )
        .build()

    init {
        NoteModel.getNoteList(AccountModel.INSTANCE.token!!, orderId) {
            it.forEach { noteModel ->
                adapter.add(
                    MessageBodyViewModel(
                        noteModel.senderName!!,
                        noteModel.date,
                        noteModel.time,
                        noteModel.message!!
                    )
                )
            }
        }

    }


    fun sendMessage(
        text: String,
        onSuccess: (str: String) -> Unit?,
        onFail: (str: String) -> Unit?
    ) {
        if (text == "") {
            return
        }
        val noteModel = NoteModel()
        AccountModel.INSTANCE.token?.let {
            noteModel.addNote(it, orderId, text, { str ->
                onSuccess.invoke(str)
                adapter.add(
                    MessageBodyViewModel(
                        noteModel.senderName!!,
                        noteModel.date,
                        noteModel.time,
                        noteModel.message!!
                    )
                )
            },
                { str ->
                    onFail.invoke(str)
                })
        }
    }


    override fun onRegister() {

    }

    override fun onUnRegister() {

    }

    override fun onCloseObserved() {

    }
}