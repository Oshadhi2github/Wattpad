package com.opk.pos.controller;

import com.opk.pos.db.Database;
import com.opk.pos.model.Customer;
import com.opk.pos.model.Product;
import com.opk.pos.view.tm.CartTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class PlaceOrderFormController {
    public AnchorPane placeOrderContext;
    public ComboBox<String> cmbCustomerCodes;
    public ComboBox<String> cmbItemCodes;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtOrderId;
    public TextField txtOrderDate;
    public TextField txtOrderTotal;
    public TextField txtItemCount;
    public TextField txtQty;
    public TableView<CartTM> tblCart;
    public TableColumn colCode;
    public TableColumn colDesc;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadCustomerIds();
        loadItemCodes();
        setDate();

        //===========Listeners===========
        cmbCustomerCodes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerData(newValue);
        });

        cmbItemCodes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setProductData(newValue);
        });
        //===========Listeners===========
    }

    private void setDate() {
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void setProductData(String code) {
        Product product = Database.productTable.stream().filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        if (product!=null){
            txtDescription.setText(product.getDescription());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(product.getQtyOnHand()));
        }
    }

    private void setCustomerData(String id) {
        Stream<Customer> customerList = Database.customerTable.stream().filter(e -> e.getId().equals(id));
        System.out.println(customerList);
        Optional<Customer> first = customerList.findFirst();
        if (first.isPresent()){
            Customer customer = first.get();
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtSalary.setText(String.valueOf(customer.getSalary()));
        }
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
        cmbItemCodes.setItems(ObList);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }
    private void setUi(String location, String title) throws IOException {
        Stage window = (Stage) placeOrderContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
    }
    ObservableList<CartTM> tmList = FXCollections.observableArrayList();
    public void addToCart(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double total = qty* unitPrice;

        Button btn = new Button("Remove");

        CartTM existTm = isExists(cmbItemCodes.getValue());
        if (existTm!=null) {
            existTm.setQty(existTm.getQty() + qty);
            existTm.setTotal(existTm.getTotal() + total);
        }else{
            CartTM tm = new CartTM(cmbItemCodes.getValue(),
                    txtDescription.getText(),unitPrice,qty,total,btn);

            tmList.add(tm);
        }



        tblCart.setItems(tmList);
        tblCart.refresh();

    }
    private  CartTM isExists(String id){
        return tmList.stream().filter(e->e.getCode().equals(id)).findFirst().orElse(null);
    }
}
