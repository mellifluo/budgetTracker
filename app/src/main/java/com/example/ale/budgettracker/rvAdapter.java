package com.example.ale.budgettracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.SpesaViewHolder> {

    public static class SpesaViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView nameSpesa;
        public TextView amountSpesa;
        public TextView dateSpesa;
        public TextView catSpesa;
        public String idExtra;
        public String pos;
        public String catExtra;
        public String categoria;
        public String Fday;
        public String Fmonth;
        public String Fyear;
        public ImageView ifSpesa;
        private DBHelper dbh;


        SpesaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nameSpesa = (TextView)itemView.findViewById(R.id.name_expanse);
            amountSpesa = (TextView)itemView.findViewById(R.id.amount_expanse);
            dateSpesa = (TextView)itemView.findViewById(R.id.date_expanse);
            catSpesa = (TextView)itemView.findViewById(R.id.card_category);
            ifSpesa = (ImageView) itemView.findViewById(R.id.if_spesa);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nome = nameSpesa.getText().toString();
                    if (!nome.equals("Budget mensile")) {
                        Intent modifySpesa = new Intent(v.getContext(), DetailsActivity.class);
                        modifySpesa.putExtra("day", Fday);
                        modifySpesa.putExtra("month", Fmonth);
                        modifySpesa.putExtra("year", Fyear);
                        modifySpesa.putExtra("id", idExtra);
                        modifySpesa.putExtra("cat", categoria);
                        modifySpesa.putExtra("pos", pos);
                        modifySpesa.putExtra("category", catExtra);
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
        pvh.dbh = new DBHelper(viewGroup.getContext());
        if (pvh.dbh.getTheme()==0) {
            v.setBackgroundColor(ContextCompat.getColor(viewGroup.getContext(), R.color.colorPrimaryLight2));
        }
        return pvh;
    }

    @Override
    public void onBindViewHolder(SpesaViewHolder SpesaViewHolder, int i) {
        String amount = Spese.get(i).amount + "€";
        String date = Spese.get(i).day + "/" + Spese.get(i).month + "/" + Spese.get(i).year;
        String nome = Spese.get(i).name;
        SpesaViewHolder.idExtra = Spese.get(i).id;
        SpesaViewHolder.Fyear = Spese.get(i).year;
        SpesaViewHolder.Fmonth = Spese.get(i).month;
        SpesaViewHolder.Fday = Spese.get(i).day;
        SpesaViewHolder.pos = Spese.get(i).pos;
        SpesaViewHolder.catExtra = Spese.get(i).planned;
        SpesaViewHolder.categoria = Spese.get(i).category;
        SpesaViewHolder.nameSpesa.setText(nome);
        SpesaViewHolder.amountSpesa.setText(amount);
        SpesaViewHolder.catSpesa.setText(Spese.get(i).category);
        SpesaViewHolder.dateSpesa.setText(date);
        int color1 = Color.parseColor("#62727b");
        int color2 = Color.parseColor("#BCC1C3");
        if (SpesaViewHolder.dbh.getTheme()==0) {
            color1 = Color.parseColor("#BBDEFB");
            color2 = Color.parseColor("#FFFFFF");
        }
        if (nome.equals("Budget mensile")){
            SpesaViewHolder.cv.setCardBackgroundColor(color1);
            SpesaViewHolder.dateSpesa.setText("");
            SpesaViewHolder.ifSpesa.setVisibility(View.GONE);
        }
        else {
            SpesaViewHolder.cv.setCardBackgroundColor(color2);
            if (Float.valueOf(Spese.get(i).amount) < 0) {
                SpesaViewHolder.ifSpesa.setImageResource(R.drawable.ic_action_minus);
            }
            else
                SpesaViewHolder.ifSpesa.setImageResource(R.drawable.ic_action_plusgreen);
        }
    }

    @Override
    public int getItemCount() {
        return Spese.size();
    }

}