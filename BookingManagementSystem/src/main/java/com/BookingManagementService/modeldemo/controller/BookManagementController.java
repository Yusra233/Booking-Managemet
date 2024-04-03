package com.BookingManagementService.modeldemo.controller;

import com.BookingManagementService.modeldemo.model.BookManagementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/BookManage")
public class BookManagementController {

    private static final Logger logger= LoggerFactory.getLogger(BookManagementController.class);

    private BookManagementService bookManagementService;
    @Autowired
    public BookManagementController(BookManagementService bookManagementService){
        this.bookManagementService=bookManagementService;
    }

    @PostMapping("/addBook")
    @Operation(tags = "Book Operations",operationId = "post")
    public String addNewBookToList(@RequestBody Book book){
        bookManagementService.addNewBook(book);
        return "Book Added Successfully";
    }

    @PostMapping("/addPerson")
    @Operation(tags = "Customer Operations",operationId = "post")
    public String addNewCustomerToList(@RequestBody Customer customer){
        bookManagementService.addNewCustomer(customer);
        return "Person Added Successfully";
    }

    @PostMapping("/addAccount")
    @Operation(tags = "Account Operations",operationId = "post")
    public String addNewAccount(@RequestBody Account account){
        bookManagementService.addNewAccount(account);
        return "Account Added Successfully";
    }

    @PostMapping("/addCustomer")
    @Operation(tags = "Customer Operations",operationId = "post")
    public String addNewCustomer(@RequestBody CustomerRequest customerRequest){
        return bookManagementService.getResponse(customerRequest);
    }

    @PostMapping("/addShoppingList")
    @Operation(tags = "ShoppingList Operations",operationId = "post")
    public String buyNewBook(@RequestBody ShoppingList shoppingList){
        logger.info("Buy New Book Operation Done Successfully");
        return bookManagementService.buyBooks(shoppingList);
    }

    @GetMapping("/displayShoppingList")
    @Operation(tags = "ShoppingList Operations",operationId = "get")
    public List<ShoppingList> showAllShoppingList() {
        logger.info("Displaying the shopping list done successfully");
        return bookManagementService.showAllShoppingList();
    }
    @GetMapping("/displayBookList")
    @Operation(tags = "Book Operations",operationId = "get")
    public List<Book> showAllList() {
        return bookManagementService.showAllBooks();
    }

    @GetMapping("/displayAccountList")
    @Operation(tags = "Account Operations",operationId = "get")
    public List<Account> showAllAccounts() {
        logger.info("displaying the account list done successfully");
        return bookManagementService.showAllAccounts();
    }

    @GetMapping("/displayPersonList")
    @Operation(tags = "Customer Operations",operationId = "get")
    public List<Customer> showAllCustomerList() {
        logger.info("displaying the person list done successfully");
        return bookManagementService.showAllCustomers();
    }

    @GetMapping("/displayCustomerList")
    @Operation(tags = "Customer Operations",operationId = "get")
    public List<CustomerRequest> showAllCustomersRequests() {
        logger.info("displaying the CustomerRequest list done successfully");
        return bookManagementService.getCustomerList();
    }

    @DeleteMapping("{bid}")
    @Operation(tags = "Book Operations",operationId = "delete")
    public String deleteById(@PathVariable("bid") int bid){
        return bookManagementService.removeById(bid);
    }

    @DeleteMapping("/deletePerson/{pid}")
    @Operation(tags = "Customer Operations",operationId = "delete")
    public String deleteCustomerById(@PathVariable("pid") int pid){
        return bookManagementService.removeByCustomerId(pid);
    }

    @PutMapping("{id}")
    @Operation(tags = "Book Operations",operationId = "put")
    public String updateBookTitle(@PathVariable("id") int id,@RequestParam String title){
        return bookManagementService.updateTitle(id,title);
    }

    @PostMapping("/pay")
    @Operation(tags = "Payment Operations",operationId = "post")
    public PaymentRequest pay(@RequestBody PaymentRequest paymentRequest){
        logger.info("payment details gotten successfully");
        return bookManagementService.getDetails(paymentRequest);
    }

    @GetMapping("/getExcelInDB")
    public String readExcelInDB(){
         return bookManagementService.readExcelInDB();
    }

    @GetMapping("/getExcelSheet")
    public List<Book> readFromExcel() {
        return bookManagementService.readExcel();
    }

    @PostMapping("/post")
    public String create(@RequestParam Long bookId, @RequestParam Long customerId,@RequestBody CustomerRequest customerRequest){
        return bookManagementService.buyBook(bookId,customerId,customerRequest);
    }
}