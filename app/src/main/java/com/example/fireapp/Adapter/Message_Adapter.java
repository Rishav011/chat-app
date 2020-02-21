package com.example.fireapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fireapp.R;
import com.example.fireapp.model.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.List;

public class Message_Adapter  extends RecyclerView.Adapter<Message_Adapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=1;
    public static final int MSG_TYPE_RIGHT=0;
    private Context context;
    private List<message> messages;
    FirebaseUser fUser;

    public Message_Adapter(Context context, List<message> messages) {


        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public Message_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false);
            return new Message_Adapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recieved_message, parent, false);
            return new Message_Adapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.ViewHolder holder, final int position) {
        message chat =messages.get(position);
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        long time = chat.getTime();
        String newTime=formatTime.format(time);
        String newDate=formatDate.format(time);
        holder.dateText.setText(newDate);
        if(position>0)
        {
            message message1= messages.get(position);
            message message2 = messages.get(position-1);
            String newDate1=formatDate.format(message1.getTime());
            String newDate2=formatDate.format(message2.getTime());
            if(newDate1.equals(newDate2))
            {
                holder.dateText.setVisibility(View.GONE);
            }else{
                holder.dateText.setVisibility(View.VISIBLE);
            }
        }
        holder.timeText.setText(newTime);
        holder.show_message.setText(chat.getMessage());
        }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public TextView timeText;
        public TextView dateText;

        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            timeText=itemView.findViewById(R.id.text_message_time);
            dateText = itemView.findViewById(R.id.text_message_date);
            mView = itemView;
        }

    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
