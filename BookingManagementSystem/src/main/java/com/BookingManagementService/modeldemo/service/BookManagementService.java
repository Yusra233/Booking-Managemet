package com.BookingManagementService.modeldemo.service;

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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookManagementService {
    private List<Book> list=new ArrayList<>();
    private List<ShoppingList> buylist=new ArrayList<>();
    private List<CustomerRequest> customerList=new ArrayList<>();

    private Payable payable;
    private BookRepo bookRepo;
    private CustomerRepo customerRepo;
    private MailService mailService;

    @Autowired
    public BookManagementService(Payable payable, BookRepo bookRepo,CustomerRepo customerRepo,MailService mailService) {
        this.payable = payable;
        this.bookRepo = bookRepo;
        this.customerRepo=customerRepo;
        this.mailService=mailService;
    }

    private static final Logger logger= LoggerFactory.getLogger(BookManagementService.class);

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

    public HashSet<Book> readExcelSheet() {
        FileInputStream file = null;
        HashSet<Book> uniqueList=new HashSet<>();
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
                    Book book = new Book();
                    int i = row.getFirstCellNum();
                    book.setTitle(dataFormatter.formatCellValue(row.getCell(i++)));
                    book.setAuthor(dataFormatter.formatCellValue(row.getCell(i++)));
                    String typeString = dataFormatter.formatCellValue(row.getCell(i++));
                    book.setType(Book.Type.valueOf(typeString));
                    double price = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(i++)));
                    book.setPrice(price);
                    uniqueList.add(book);
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
        return uniqueList;
    }

    public String addSortedListToDb(){
        bookRepo.saveAll(sortList());
        logger.info("Sorted list added to the database successfully.");
        return "Sorted list added to the database successfully.";
    }

    public List<Book> sortList(){
        List<Book> bookList=new ArrayList<>();
        bookList.addAll(readExcelSheet());
        Collections.sort(bookList,new BookComp());
        logger.info("Sorting the database list done successfully.");
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

            mailService.sendEmail(customer.getEmail(), subject, text);
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

}
