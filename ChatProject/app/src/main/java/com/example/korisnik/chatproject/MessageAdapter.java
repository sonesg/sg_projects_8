package com.example.korisnik.chatproject;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Korisnik on 2/2/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder>{

    private List<Messages> messagesList;
    private Context context;
    //context mi vise ne treba,ali mi je trebao kad sam inicjalizovao bazu u onCreateViewHolder-u
    //i kada sam tu uzimao linkove iz baze i tu uz pomoc picasa sam prikazivao te slike
    private DatabaseReference databaseReference;
    private String name;

    public MessageAdapter(List<Messages> messagesList,Context context,String name){
        this.messagesList = messagesList;
        this.context = context;
        this.name = name;
    }



    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessagesViewHolder(v);
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder{

        public  TextView messageText;
       // public CircleImageView profileImage;
        public TextView messageTime;
        public ImageView messageImage;

        public MessagesViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_single_text);
           // profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            messageImage = (ImageView) itemView.findViewById(R.id.imageViewChatMessaging);
        }

    }

    @Override
    public void onBindViewHolder(final MessagesViewHolder holder, int position) {
        //String current_user_id = auth.getCurrentUser().getUid();

        Messages c = messagesList.get(position);
        String message_type = c.getType();
        String from_user = c.getFrom();

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String imageProfile = dataSnapshot.child("thumb_image").getValue().toString();
//
//                Picasso.with(holder.profileImage.getContext()).load(imageProfile)
//                        .placeholder(R.drawable.img_avatar).into(holder.profileImage);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        holder.messageText.setText(c.getMessage());
        GetTimeAgo TimeAgo = new GetTimeAgo();
        long time = c.getTime();
        String time_m = TimeAgo.getTimeAgo(time, holder.messageTime.getContext());
        holder.messageTime.setText(time_m);

        if(message_type.equals("text")) {
            holder.messageImage.setVisibility(View.GONE);
            if (!from_user.equals(name)) {
                //ukoliko mi saljemo poruku
                holder.messageText.setBackgroundResource(R.drawable.message_background_);
                holder.messageText.setTextColor(Color.BLACK);
                //holder.profileImage.setVisibility(View.GONE);
            } else {
                //ukoliko neko nama salje,izgledace ovako
                holder.messageText.setBackgroundResource(R.drawable.message_background_r);
                holder.messageText.setTextColor(Color.WHITE);

                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                //NAMESTANJE GRAVITY-JA NA BUDE DESNO UZ RODITELJA
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.messageText.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            holder.messageText.setLayoutParams(lp);
//            ////////////////////////////////////////////////////////////////////////////////////////////////////////
////            holder.profileImage.setVisibility(View.GONE);
//            /////////////////////////////////////////////////////////////////////////////////////////////////////
//            RelativeLayout.LayoutParams lpTime = (RelativeLayout.LayoutParams) holder.messageTime.getLayoutParams();
//            lpTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            holder.messageTime.setLayoutParams(lpTime);
            }
        }else{
            holder.messageText.setVisibility(View.INVISIBLE);
            holder.messageImage.setVisibility(View.VISIBLE);
            Picasso.with(holder.messageImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.img_avatar).into(holder.messageImage);
        }
    }
//JAKO BITNA STVAR KOJU SAM URADIO
    //UBRZAO SAM DOSTA APLIKACIJU TAKO STO SAMO IZBRISAO INSTANCIRANJE BAZE PODATAKA DA BIH UZEO LINK OD SLIKE
    //MORAM DA SMISLIM BOLJI NACIN DA INSTANCIRAM BAZU,NEGE GDE CE INSTANCIRATI OBJEKAT SAMO JEDANPUT A NE 100PUTA I TAKO USPORAVATI PROGRAM

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    //UBACIO SAM DA MOGU DA SE SALJU SLIKE IAKO SAM SADA INICJALIZOVAO BAZU U ONBINDVIEWHOLDER A NE U ONCREATEVIEW HOLDER I DALJE
    //IMAM PROBLEMA SA VISESTRUKIM INICJALIZOVANJEM TIH OBJEKATA I ZBOG TOGA JE RAD APLIKACIJE MNOGO SPORIJI

}

