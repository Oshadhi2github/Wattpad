package com.opk.pos.controller;

import com.opk.pos.db.Database;
import com.opk.pos.model.Customer;
import com.opk.pos.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PlaceOrderFormController {
    public AnchorPane placeOrderContext;
    public ComboBox<String> cmbCustomerCodes;
    public ComboBox<String> cmbItemCodes;

    public void initialize(){
        loadCustomerIds();
        loadItemCodes();
    }

    private void loadCustomerIds() {
        ObservableList<String> ObList = FXCollections.observableArrayList();
        for (Customer c: Database.customerTable
             ) {
            ObList.add(c.getId());
        }
        cmbCustomerCodes.setItems(ObList);
    }

    private void loadItemCodes() {
        ObservableList<String> ObList = FXCollections.observableArrayList();
        for (Product p: Database.productTable
        ) {
            ObList.add(p.getCode());
        }
        cmbCustomerCodes.setItems(ObList);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }
    private void setUi(String location, String title) throws IOException {
        Stage window = (Stage) placeOrderContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
}
