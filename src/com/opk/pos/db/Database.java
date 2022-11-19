package com.opk.pos.db;

import com.opk.pos.model.Customer;
import com.opk.pos.model.Order;
import com.opk.pos.model.Product;
import com.opk.pos.model.User;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList<User>();
    public static ArrayList<Customer> customerTable = new ArrayList<Customer>();
    public static ArrayList<Product> productTable = new ArrayList<Product>();
    public static ArrayList<Order> orderTable = new ArrayList<Order>();

    static {
        customerTable.add(new Customer("C-001","Anne","Colombo",45000));
        customerTable.add(new Customer("C-002","Fathima","Kegalle",50000));
        customerTable.add(new Customer("C-003","Krish","Kandy",40000));

        productTable.add(new Product("D-001","Description 1",25,4500));
        productTable.add(new Product("D-002","Description 2",90,450));
        productTable.add(new Product("D-003","Description 3",15,750));
        productTable.add(new Product("D-004","Description 4",50,500));
        productTable.add(new Product("D-005","Description 5",45,600));
    }
}
