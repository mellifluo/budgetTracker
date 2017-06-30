package com.example.ale.budgettracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.SpesaViewHolder> {

    public static class SpesaViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView SpesaName;
        public TextView SpesaAge;

        SpesaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            SpesaName = (TextView)itemView.findViewById(R.id.name_expanse);
            SpesaAge = (TextView)itemView.findViewById(R.id.amount_expanse);
        }
    }

    private List<Spesa> Spese;

    rvAdapter(List<Spesa> Spese){
        this.Spese = Spese;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SpesaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main, viewGroup, false);
        SpesaViewHolder pvh = new SpesaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SpesaViewHolder SpesaViewHolder, int i) {
        SpesaViewHolder.SpesaName.setText(Spese.get(i).name);
        SpesaViewHolder.SpesaAge.setText(Spese.get(i).amount);
    }

    @Override
    public int getItemCount() {
        return Spese.size();
    }

}