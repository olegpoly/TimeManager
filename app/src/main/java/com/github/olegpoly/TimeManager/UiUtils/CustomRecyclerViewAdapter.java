package com.github.olegpoly.TimeManager.UiUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.R;

import java.util.Collections;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyVeiwHolder> {
    private final LayoutInflater inflator;

    List<Information> data = Collections.emptyList();
    Context context;

    public CustomRecyclerViewAdapter(Context context, List<Information> data) {
        inflator = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void doSmth(int position) {
        Toast.makeText(context, "smth " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyVeiwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_recycler_row, parent, false);

        MyVeiwHolder holder = new MyVeiwHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyVeiwHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
        /*holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyVeiwHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyVeiwHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

            icon.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            doSmth(getPosition());
        }
    }
}
