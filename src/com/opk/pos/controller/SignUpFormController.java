package com.opk.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.opk.pos.db.Database;
import com.opk.pos.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpFormController {
    public JFXTextField txtEmail;
    public JFXPasswordField txtRePassword;
    public JFXTextField txtFullName;
    public JFXTextField txtContact;
    public JFXPasswordField txtPassword;
    public AnchorPane signupFormContext;

    public void signUpOnAction(ActionEvent actionEvent) throws InterruptedException, IOException {
        //check password match?
        String realPwd=txtPassword.getText().trim(); //trim => __N_ after trim => N
        String matchPwd=txtRePassword.getText().trim();
        if (!realPwd.equals(matchPwd)){
            new Alert(Alert.AlertType.WARNING, "Both passwords should match").show();
            return; // we will stop the JVM
        }
        User u = new User(txtEmail.getText().trim(),
                txtFullName.getText(),txtContact.getText(),realPwd);
        if (saveUser(u)) {
            new Alert(Alert.AlertType.CONFIRMATION, "User Registered!").show();
            clearFields();
            Thread.sleep(2000); // wait 2 seconds
            setUi("DashBoardForm");
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();

        }

    }
    private void clearFields(){
        txtEmail.clear();txtFullName.clear();txtContact.clear();txtPassword.clear();txtRePassword.clear();
    }

    private boolean saveUser(User u){
        return Database.userTable.add(u);//inbuilt method => java.util
    }


    private void setUi(String location) throws IOException {
        Stage window = (Stage)signupFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }
}
