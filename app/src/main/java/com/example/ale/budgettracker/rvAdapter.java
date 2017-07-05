package com.example.ale.budgettracker;

import android.content.Intent;
import android.graphics.Color;
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
        public TextView nameSpesa;
        public TextView amountSpesa;
        public TextView dateSpesa;

        SpesaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nameSpesa = (TextView)itemView.findViewById(R.id.name_expanse);
            amountSpesa = (TextView)itemView.findViewById(R.id.amount_expanse);
            dateSpesa = (TextView)itemView.findViewById(R.id.date_expanse);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nome = nameSpesa.getText().toString();
                    if (!nome.equals("Budget iniziale")) {
                        Intent modifySpesa = new Intent(v.getContext(), SpesaActivity.class);
                        modifySpesa.putExtra("nameSpesa", nome);
                        modifySpesa.putExtra("amountSpesa", amountSpesa.getText().toString());
                        v.getContext().startActivity(modifySpesa);
                    }
                }
            });
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
        String amount = Spese.get(i).amount + "€";
        String date = Spese.get(i).day + "/" + Spese.get(i).month + "/" + Spese.get(i).year;
        String nome = Spese.get(i).name;
        SpesaViewHolder.nameSpesa.setText(nome);
        SpesaViewHolder.amountSpesa.setText(amount);
        SpesaViewHolder.dateSpesa.setText(date);
        if (Integer.valueOf(Spese.get(i).amount) < 0) {
            int color = Color.parseColor("#FF4081");
            SpesaViewHolder.cv.setCardBackgroundColor(color);
        }
        else SpesaViewHolder.cv.setCardBackgroundColor(Color.GREEN);
        if (nome.equals("Budget mensile")){
            int color = Color.parseColor("#3F51B5");
            SpesaViewHolder.cv.setCardBackgroundColor(color);
            SpesaViewHolder.dateSpesa.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return Spese.size();
    }

}