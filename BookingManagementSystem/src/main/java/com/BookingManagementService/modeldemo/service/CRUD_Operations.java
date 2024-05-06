package com.BookingManagementService.modeldemo.service;

import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CRUD_Operations {
    private List<Book> list=new ArrayList<>();
    private List<ShoppingList> buylist=new ArrayList<>();
    private List<CustomerRequest> customerList=new ArrayList<>();
    private List<Account> accountList=new ArrayList<>();
    private List<Customer> customers=new ArrayList<>();

    private static final Logger logger= LoggerFactory.getLogger(BookManagementService.class);

    public List<Book> addNewBook(Book book){
        list.add(book);
        logger.info("AddNewBook Method Done Successfully");
        return list;
    }

    public List<Account> addNewAccount(Account account){
        accountList.add(account);
        logger.info("AddNewAccount Method Done Successfully");
        return accountList;
    }
    public List<ShoppingList> showAllShoppingList(){
        return buylist;
    }

    public List<Customer> addNewCustomer(Customer customer){
        customers.add(customer);
        logger.info("AddNewCustomer Method Done Successfully");
        return customers;
    }

    public String removeById(int bid){
        for (Book m : list) {
            if (m.getId() == bid) {
                list.remove(m);
                logger.info("Book with id: {} ,Removed",bid);
                return "Book with id: " + bid + " Removed";
            } }
        logger.error("No Book with id: {} ,Found",bid);
        return "No Book with id: " + bid + " Found";

    }

    public String removeByCustomerId(int pid){
        for (Customer n : customers) {
            if (n.getId() == pid) {
                customers.remove(n);
                logger.info("Customer with id: {} removed",pid);
                return  "Customer with id: " + pid + " Removed";

            }}
        logger.error("No customer with id: {} Found",pid);
        return "No customer with id: " + pid + " Found";
    }

    public String updateTitle(int id, String title){
        for (Book m:list){
            if (m.getId() == id) {
                m.setTitle(title);
                logger.info("Book title with id:{} ,updated",id);
                return "Book Title with id: " + id + " Updated";

            }
        }
        logger.error("No Book with id: {} Found to update title on",id);
        return "No Book with id: " + id + " Found";
    }

    public List<Book> showAllBooks(){
        logger.info("ShowAllBooks Method Done Successfully");
        return list;
    }

    public List<Account> showAllAccounts(){
        logger.info("showAllAccounts Method Done Successfully");
        return accountList;
    }

    public List<Customer> showAllCustomers(){
        return customers;
    }
    public List<CustomerRequest> getCustomerList() {
        return customerList;
    }

}
