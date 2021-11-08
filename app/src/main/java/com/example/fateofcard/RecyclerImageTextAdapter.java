package com.example.fateofcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerImageTextAdapter extends RecyclerView.Adapter<RecyclerImageTextAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> dt = null;

    RecyclerImageTextAdapter(ArrayList<RecyclerItem> list) {
        dt = list;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener Listener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.Listener = listener;
    }

    @Override
    public RecyclerImageTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        RecyclerImageTextAdapter.ViewHolder vh = new RecyclerImageTextAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerImageTextAdapter.ViewHolder holder, int position) {
        RecyclerItem item = dt.get(position);
        holder.icon.setForeground(item.getSelected());
        holder.icon.setImageDrawable(item.getIconD());
        holder.tx.setText(item.getCardno());
    }

    @Override
    public int getItemCount() {
        return dt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView tx;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.rc_icon);
            tx = itemView.findViewById(R.id.cardno);

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
