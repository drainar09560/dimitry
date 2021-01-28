package com.example.litvyaksavlibayevtask5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    public void setSelectElementListener(SelectElementListener selectElementListener) {
        this.selectElementListener = selectElementListener;
    }

    SelectElementListener selectElementListener;
    public abstract static class SelectElementListener{
        public abstract void selectElement(User user);
    }
    private LayoutInflater inflater;
    private List<User> users;

    DataAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.markView.setText(user.getMark());
        holder.modelView.setText(user.getModel());
        holder.typeView.setText(user.getType());
        holder.vintageView.setText(String.valueOf(user.getVintage()));
        holder.counterView.setText(String.valueOf(user.getCounter()));
        holder.regView.setText(String.valueOf(user.getReg()));
        holder.itenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectElementListener!=null)
                selectElementListener.selectElement(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView markView;
        final TextView modelView;
        final TextView typeView;
        final TextView vintageView;
        final TextView counterView;
        final TextView regView;
        final View itenView;
        ViewHolder(View view){
            super(view);
            itenView = view;
            markView = view.findViewById(R.id.markTextView);
            modelView = (TextView) view.findViewById(R.id.modelTextView);
            typeView = view.findViewById(R.id.typeTextView);
            vintageView = view.findViewById(R.id.vintageTextView);
            counterView = view.findViewById(R.id.counterTextView);
            regView = view.findViewById(R.id.regTextView);
        }
    }
}
