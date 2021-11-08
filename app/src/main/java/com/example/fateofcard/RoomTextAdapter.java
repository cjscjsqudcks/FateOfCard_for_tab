package com.example.fateofcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoomTextAdapter extends RecyclerView.Adapter<RoomTextAdapter.Viewholder> {
    private ArrayList<RoomItem> data = null;

    RoomTextAdapter(ArrayList<RoomItem> list) {
        data = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private RecyclerImageTextAdapter.OnItemClickListener Listener = null;

    public void setOnItemClickListener(RecyclerImageTextAdapter.OnItemClickListener listener) {
        this.Listener = listener;
    }

    @NonNull
    @Override
    public RoomTextAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_item, parent, false);
        RoomTextAdapter.Viewholder Roomvh = new RoomTextAdapter.Viewholder(view);
        return Roomvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTextAdapter.Viewholder holder, int position) {
        RoomItem item = data.get(position);
        holder.tx.setText(item.getRoomname());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tx;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tx = itemView.findViewById(R.id.user_item_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (Listener != null) {
                            Listener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
