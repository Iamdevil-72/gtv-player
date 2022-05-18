package com.example.gtvplayer;

import android.content.Context;
import android.content.Intent;
import android.icu.number.CompactNotation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gtvplayer.db.Geovid;

import org.w3c.dom.Text;

import java.util.List;

public class GeovidListAdapter extends RecyclerView.Adapter<GeovidListAdapter.MyViewHolder> {

    private Context context;
    private List<Geovid> geovidList;
    private OnGeovidListener onGeovidListener;

    public GeovidListAdapter(Context context){
        this.context = context;
    }

    public void setGeovidList(List<Geovid> geovidList, OnGeovidListener onGeovidListener){
        this.onGeovidListener = onGeovidListener;
        this.geovidList = geovidList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GeovidListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(view, onGeovidListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GeovidListAdapter.MyViewHolder holder, int position) {
        holder.docname.setText(this.geovidList.get(position).docid);
//        holder.vidloc.setText(this.geovidList.get(position).videoLink);
    }

    @Override
    public int getItemCount() {
        return this.geovidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView docname;
        TextView vidloc;
        OnGeovidListener onGeovidListener;
        public MyViewHolder(View view, OnGeovidListener onGeovidListener){
            super(view);
            docname = view.findViewById(R.id.docname);
//            vidloc = view.findViewById(R.id.vidloc);

            this.onGeovidListener = onGeovidListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGeovidListener.onGeovidClick(getAdapterPosition());
        }
    }

    public interface OnGeovidListener{
        void onGeovidClick(int position);
    }


}
