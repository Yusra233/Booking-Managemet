package com.BookingManagementService.modeldemo.model;

import com.BookingManagementService.modeldemo.exception.NotFoundException;
import com.BookingManagementService.modeldemo.repository.BookRepo;
import com.BookingManagementService.modeldemo.repository.CustomerRepo;
import com.BookingManagementService.modeldemo.feignclient.Payable;

import org.apache.poi.ss.usermodel.*;
import org.example.model.*;
import org.example.model.CustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookManagementService {
    private List<Book> list=new ArrayList<>();
    private List<Book> bookList=new ArrayList<>();
    private List<ShoppingList> buylist=new ArrayList<>();
    private List<CustomerRequest> customerList=new ArrayList<>();
    private List<Account> accountList=new ArrayList<>();
    private List<Customer> customers=new ArrayList<>();

    private Payable payable;
    private BookRepo bookRepo;
    private CustomerRepo customerRepo;
    private JavaMailSender javaMailSender;

    @Autowired
    public BookManagementService(Payable payable, BookRepo bookRepo,CustomerRepo customerRepo,JavaMailSender javaMailSender) {
        this.payable = payable;
        this.bookRepo = bookRepo;
        this.customerRepo=customerRepo;
        this.javaMailSender=javaMailSender;
    }

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

    public String buyBooks(ShoppingList shoppingList){
        for(Book c:list){
            if(c.getId() == shoppingList.getBookId()){
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

    public String getResponse(CustomerRequest customerRequest) {
        LocalTime currentTime = LocalTime.now();
        CustomerResponse customerResponse=new CustomerResponse();
        customerResponse.setTime(currentTime);
        customerList.add(customerRequest);
        customerResponse.setAmount(customerRequest.getAccount());
        if (customerRequest.getAccount().getBalance() < 0 || customerRequest.getAccount().getBalance() == 0) {
            customerRequest.getAccount().getBalance();
            logger.error("Customer cannot added, amount value not as required!!");
            return "Status: "+ CustomerResponse.Status.FAIL + "\nAmount: " + String.valueOf(customerResponse.getAmount()) + "\nTime: " + customerResponse.getTime();
        } else {
            logger.info("Customer Added Successfully");
           return "Status: "+CustomerResponse.Status.SUCCESS + "\nAmount: " + String.valueOf(customerResponse.getAmount()) + "\nTime: " + customerResponse.getTime();
        }}


    public List<CustomerRequest> getCustomerList() {
        return customerList;
    }

    public String readExcelInDB() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("D:\\\\Users\\\\hp\\\\Desktop\\\\Training\\\\Library Excel Sheet\\\\Library.xlsx"));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
                Row row = sheet.getRow(n);
                boolean isEmpty = true;
                for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell.getCellType() != CellType.BLANK) {
                        isEmpty = false;
                        break;
                    }
                }
                if (!isEmpty) {
                    Book book1 = new Book();
                    int i = row.getFirstCellNum();
                    book1.setTitle(dataFormatter.formatCellValue(row.getCell(++i)));
                    book1.setAuthor(dataFormatter.formatCellValue(row.getCell(++i)));
                    String typeString = dataFormatter.formatCellValue(row.getCell(++i));
                    book1.setType(Book.Type.valueOf(typeString));
                    double price = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(++i)));
                    book1.setPrice(price);
                    bookRepo.save(book1);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File Not Fount!!");
            throw new RuntimeException(e);
        } catch (IOException ex) {
            logger.error("IOException");
            throw new RuntimeException(ex);
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                logger.error("IOException");
                System.out.println("IOException found : " + e.getMessage());
            }
        }return "Added to DB successfully";
    }

    public List<Book> readExcel() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("D:\\\\Users\\\\hp\\\\Desktop\\\\Training\\\\Library Excel Sheet\\\\Library.xlsx"));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
                Row row = sheet.getRow(n);
                boolean isEmpty = true;
                for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell.getCellType() != CellType.BLANK) {
                        isEmpty = false;
                        break;
                    }
                }
                if (!isEmpty) {
                    Book book1 = new Book();
                    int i = row.getFirstCellNum();
                    book1.setTitle(dataFormatter.formatCellValue(row.getCell(++i)));
                    book1.setAuthor(dataFormatter.formatCellValue(row.getCell(++i)));
                    String typeString = dataFormatter.formatCellValue(row.getCell(++i));
                    try {
                        book1.setType(Book.Type.valueOf(typeString));
                    } catch (IllegalArgumentException e) {
                        logger.error("Illegal Argument Exception");
                        System.out.println("DEFAULT");
                    }
                    double price = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(++i)));
                    book1.setPrice(price);
                    bookList.add(book1);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File Not Fount!!");
        } catch (IOException ex) {
            logger.error("IOException");
            System.out.println("IOException found : " + ex.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                logger.error("IOException");
                System.out.println("IOException found : " + e.getMessage());
            }
        }
        return bookList;
    }

    public String buyBook(Long bookId, Long customerId,CustomerRequest customerRequest) {
        Optional<Book> optionalBook = bookRepo.findById(bookId);
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);

        if (optionalBook.isPresent() && optionalCustomer.isPresent()) {
            Book book = optionalBook.get();
            Customer customer = optionalCustomer.get();
            double bookPrice = book.getPrice();
            System.out.println(customer.getId());

            double balance = payable.get(customerRequest);
            System.out.println(balance);
            if (balance >= bookPrice) {
                balance -= bookPrice;
                customerRequest.getAccount().setBalance(balance);
                System.out.println(balance);
            customer.addBook(book);
            customerRepo.save(customer);

            String subject = "Book Registration Confirmation";
            String text = "Dear " + customer.getName() + ",\n\n"
                    + "Thank you for registering the book \"" + book.getTitle() + "\" from our store.\n\n"+"The amount of "+ bookPrice +" JD has been deducted from your account.\n\n"
                    + "Best regards,\nBookstore";

            sendEmail(customer.getEmail(), subject, text);
            logger.info("Book Registration Done");
            return "Book reserved successfully.";
        } else {
                logger.error("No balance enough");
                return "No balance enough to buy a book";
        }
    }
        else if(optionalBook.isEmpty() && optionalCustomer.isEmpty()){
            logger.error("Book and Customer Not Found");
            throw new NotFoundException("Book and Customer Not Found, Try Again!!!");
        }
        else if (optionalBook.isEmpty()) {
            logger.error("Book Not Found");
            throw new NotFoundException("Book Not Found, Try Again !!");
        }
        else {
            logger.error("Customer Not Found");
            throw new NotFoundException("Customer Not Found, Try Again !!");
        }
    }

    public void sendEmail(String toEmail, String subject, String body){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Bookstore <y.kanakri310@gmail.com>");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            logger.info("Sending email done");

            javaMailSender.send(message);
    }
}
