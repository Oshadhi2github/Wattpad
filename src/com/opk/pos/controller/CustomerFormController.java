package com.opk.pos.controller;

import com.opk.pos.db.Database;
import com.opk.pos.model.Customer;
import com.opk.pos.view.tm.CustomerTM;
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
import java.util.ArrayList;

public class CustomerFormController {
    public AnchorPane customerFormContext;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOption;
    public TableView<CustomerTM> tblCustomer;

    public void initialize(){
        setTableData();
        setColCustomerId();
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm","Dashboard");
    }

    private void setUi(String location, String title) throws IOException {
        Stage window = (Stage)customerFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        Customer customer = new Customer(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        );
        if (Database.customerTable.add(customer)){
            new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
            setTableData();
            setColCustomerId();
        }else{
            new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
        }
    }

    private void setTableData(){
        ArrayList<Customer> customerList=Database.customerTable;
        ObservableList<CustomerTM> obList= FXCollections.observableArrayList();
        for (Customer c:customerList
             ) {
            Button btn = new Button("Delete");
            CustomerTM tm = new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
            obList.add(tm);
        }
        tblCustomer.setItems(obList);
    }

    private void setColCustomerId(){
        //get last saved customer
        //catch the id(C-001)
        //separate the numbers from the character
        //increment the separated number
        //concat the character again to the incremented number(C-002)
        //set CustomerId
        if(!Database.customerTable.isEmpty()){
            Customer c=Database.customerTable.get(Database.customerTable.size()-1);// [10,20] size=2 => last => size-1 = 1 [10,20]
            String id = c.getId();//c-002
            String dataArray[] = id.split("-");// =>["C","002"];//java string class => split
            id=dataArray[1]; //002
            int oldNumber= Integer.parseInt(id); // 2 => 00 remove
            oldNumber++; // 3
            if (oldNumber<9){
                txtId.setText("C-00"+oldNumber);
            }else if (oldNumber<99){
                txtId.setText("C-0"+oldNumber);
            }else{
                txtId.setText("C-"+oldNumber);
            }
        }else{
            txtId.setText("C-001");
        }
    }

}
