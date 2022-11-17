package com.opk.pos.controller;

import com.jfoenix.controls.JFXButton;
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
    public JFXButton addToCartButton;

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
        txtQty.requestFocus();
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

        if (!checkQty( qty)){
            new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
            return;
        }

        Button btn = new Button("Remove");
        CartTM tm = new CartTM(cmbItemCodes.getValue(),
                txtDescription.getText(),unitPrice,qty,total,btn);

        CartTM existTm = isExists(cmbItemCodes.getValue());

        if (existTm!=null) {
            if (!checkQty(existTm.getQty() + qty)){
                new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
                return;
            }
            existTm.setQty(existTm.getQty() + qty);
            existTm.setTotal(existTm.getTotal() + total);
        }else{


            tmList.add(tm);
        }

        btn.setOnAction(e->{
            tmList.remove(tm);
            tblCart.refresh();
            setTotalAndCount();
        });


        tblCart.setItems(tmList);
        tblCart.refresh();
        setTotalAndCount();
        clearFields();

    }
    private boolean checkQty(int qty){
        /*if (Integer.parseInt(txtQtyOnHand.getText())<qty){
            new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
            return false; //can't add
        }
        return true;//add more*/
        return Integer.parseInt(txtQtyOnHand.getText())<qty?false:true;
    }

    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        cmbItemCodes.requestFocus();
    }

    private  CartTM isExists(String id){
        return tmList.stream().filter(e->e.getCode().equals(id)).findFirst().orElse(null);
    }

    private void setTotalAndCount(){
        double cost=0;
        /*txtOrderTotal.setText(String.valueOf(0));
        tmList.forEach(e->{
            //txtOrderTotal.setText((Double.parseDouble(txtOrderTotal.getText())+e.getTotal())+"");
            txtOrderTotal.setText(String.valueOf(Double.parseDouble(txtOrderTotal.getText())+e.getTotal()));

        });*/
        for (CartTM tm:tmList
             ) {
            cost+= tm.getTotal();
        }
        txtOrderTotal.setText(String.valueOf(cost));
        txtItemCount.setText(String.valueOf(tmList.size()));
    }

    public void addToCartData(ActionEvent actionEvent) {
        addToCartButton.fire();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm","Customer Management");
    }
}
