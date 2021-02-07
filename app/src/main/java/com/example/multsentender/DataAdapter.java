package com.example.multsentender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.nameView.setText(user.getName());
        holder.surnameView.setText(user.getSurname());
        holder.middleView.setText(user.getMiddle());
        holder.sexView.setText(user.getSex());
        holder.smenaView.setText(user.getSmena());
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
        final TextView nameView;
        final TextView surnameView;
        final TextView middleView;
        final TextView sexView;
        final TextView smenaView;
        final View itenView;
        ViewHolder(View view){
            super(view);
            itenView = view;
            nameView = view.findViewById(R.id.nameTextView);
            surnameView = (TextView) view.findViewById(R.id.surnameTextView);
            middleView = view.findViewById(R.id.middleNameTextView);
            sexView = view.findViewById(R.id.sexTextView);
            smenaView = view.findViewById(R.id.smenaTextView);
        }
    }
}
