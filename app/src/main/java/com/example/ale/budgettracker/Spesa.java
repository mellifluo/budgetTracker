package com.example.ale.budgettracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ale on 26/06/17.
 */

class Spesa {
    String name;
    String amount;
    String year;
    String month;
    String day;
    String category;
    String id;

    Spesa(String name, String amount, String year, String month, String day, String category, String id) {
        this.name = name;
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = category;
        this.id = id;
    }
}
