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

    Spesa(String name, String amount, String year, String month, String day) {
        this.name = name;
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
