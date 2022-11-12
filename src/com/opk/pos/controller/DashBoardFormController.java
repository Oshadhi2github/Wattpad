package com.opk.pos.controller;

import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashBoardFormController {
    public Label lblDate;
    public Label lblTime;

    public void initialize(){
        setDateAndTime();
    }

    private void setDateAndTime() {
        //set date
       /* Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String formattedDate = dateFormat.format(date);
        lblDate.setText(formattedDate);*/

        lblDate.setText(new SimpleDateFormat("YYYY-MM-dd").format(new Date()));


    }
}
