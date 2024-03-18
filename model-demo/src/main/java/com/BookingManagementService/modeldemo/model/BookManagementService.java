package com.BookingManagementService.modeldemo.model;

import com.BookingManagementService.modeldemo.feignclient.Payable;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.*;
import org.example.model.CustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookManagementService {
    private List<Book> list=new ArrayList<>();
    private List<Person> plist=new ArrayList<>();
    private List<ShoppingList> buylist=new ArrayList<>();
    private List<CustomerRequest> customerList=new ArrayList<>();
    private List<Account> accountList=new ArrayList<>();

    @Autowired
    Payable payable;
    Book book;

    private static final Logger logger= LoggerFactory.getLogger(BookManagementService.class);

    public List<ShoppingList> showAllShoppingList(){
        return buylist;
    }

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

    public List<Person> addNewPerson(Person person){
        plist.add(person);
        logger.info("AddNewPerson Method Done Successfully");
        return plist;
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

    public String removeByPersonId(int pid){
        for (Person n : plist) {
            if (n.getId() == pid) {
                plist.remove(n);
                logger.info("person with id: {} removed",pid);
                return  "Person with id: " + pid + " Removed";

            }}
        logger.error("No Person with id: {} Found",pid);
        return "No Person with id: " + pid + " Found";
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

    public List<Person> showAllPerson(){
        return plist;
    }

    public String buyBooks(ShoppingList shoppingList){
        for(Book c:list){
            if(c.getId()==shoppingList.getBookId()){
                shoppingList.setBookId(shoppingList.getBookId());
                shoppingList.setPersonId(shoppingList.getPersonId());
                buylist.add(shoppingList);
                logger.info("Buying Done Successfully");
                return "Buying Done Successfully";
            }
        }
        logger.error("Fail to Buy, Book Not Found");
        return "Fail to Buy, Book Not Found";
    }

    public PaymentRequest getDetails(PaymentRequest paymentRequest){
        logger.info("method to get payment details");
        return payable.pay(paymentRequest);
    }
    public String getInfo(CustomerRequest customerRequest) {
        LocalTime currentTime = LocalTime.now();
        CustomerResponse customerResponse=new CustomerResponse();
        customerResponse.setTime(currentTime);
        customerList.add(customerRequest);
        customerResponse.setAmount(customerRequest);
        if (customerRequest.getAmount() < 0 || customerRequest.getAmount() == 0) {
            customerRequest.getAmount();
            logger.error("Customer cannot added, amount value not as required!!");
            return "Status: "+ CustomerResponse.Status.FAIL + "\nAmount: " + String.valueOf(customerResponse.getAmount()) + "\nTime: " + customerResponse.getTime();

        } else {
            logger.info("Customer Added Successfully");
            return "Status: "+CustomerResponse.Status.SUCCESS + "\nAmount: " + String.valueOf(customerResponse.getAmount()) + "\nTime: " + customerResponse.getTime();
        }
    }

    public List<CustomerRequest> getCustomerList() {
        return customerList;
    }

    public List<Book> readExcel() throws IOException, InvalidFormatException {
        FileInputStream file = new FileInputStream(new File("C:\\\\Users\\\\hp\\\\Desktop\\\\Training\\\\Library Excel Sheet\\\\Library.xlsx"));
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
            Row row = sheet.getRow(n);
            Book book1 = new Book();
            int i = row.getFirstCellNum();
            book1.setTitle(dataFormatter.formatCellValue(row.getCell(++i)));
            book1.setAuthor(dataFormatter.formatCellValue(row.getCell(++i)));
            String typeString = dataFormatter.formatCellValue(row.getCell(++i));
            try {
                book1.setType(Book.Type.valueOf(typeString));
            } catch (IllegalArgumentException e) {
                System.out.println("DEFAULT");
            }
            list.add(book1);
        }
        return list;
    }

    }
