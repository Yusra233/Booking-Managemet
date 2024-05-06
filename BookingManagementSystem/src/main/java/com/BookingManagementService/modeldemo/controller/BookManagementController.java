package com.BookingManagementService.modeldemo.controller;

import com.BookingManagementService.modeldemo.service.BookManagementService;
import com.BookingManagementService.modeldemo.service.CRUD_Operations;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/BookManage")
public class BookManagementController {

    private static final Logger logger= LoggerFactory.getLogger(BookManagementController.class);

    private BookManagementService bookManagementService;
    private CRUD_Operations crud_operations;
    @Autowired
    public BookManagementController(BookManagementService bookManagementService, CRUD_Operations crud_operations){
        this.bookManagementService=bookManagementService;
        this.crud_operations=crud_operations;
    }

    @PostMapping("/addBook")
    @Operation(tags = "Book Operations",operationId = "post")
    public String addNewBookToList(@RequestBody Book book){
        crud_operations.addNewBook(book);
        return "Book Added Successfully";
    }

    @PostMapping("/addPerson")
    @Operation(tags = "Customer Operations",operationId = "post")
    public String addNewCustomerToList(@RequestBody Customer customer){
        crud_operations.addNewCustomer(customer);
        return "Person Added Successfully";
    }

    @PostMapping("/addAccount")
    @Operation(tags = "Account Operations",operationId = "post")
    public String addNewAccount(@RequestBody Account account){
        crud_operations.addNewAccount(account);
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
        return crud_operations.showAllShoppingList();
    }
    @GetMapping("/displayBookList")
    @Operation(tags = "Book Operations",operationId = "get")
    public List<Book> showAllList() {
        return crud_operations.showAllBooks();
    }

    @GetMapping("/displayAccountList")
    @Operation(tags = "Account Operations",operationId = "get")
    public List<Account> showAllAccounts() {
        logger.info("displaying the account list done successfully");
        return crud_operations.showAllAccounts();
    }

    @GetMapping("/displayPersonList")
    @Operation(tags = "Customer Operations",operationId = "get")
    public List<Customer> showAllCustomerList() {
        logger.info("displaying the person list done successfully");
        return crud_operations.showAllCustomers();
    }

    @GetMapping("/displayCustomerList")
    @Operation(tags = "Customer Operations",operationId = "get")
    public List<CustomerRequest> showAllCustomersRequests() {
        logger.info("displaying the CustomerRequest list done successfully");
        return crud_operations.getCustomerList();
    }

    @DeleteMapping("{bid}")
    @Operation(tags = "Book Operations",operationId = "delete")
    public String deleteById(@PathVariable("bid") int bid){
        return crud_operations.removeById(bid);
    }

    @DeleteMapping("/deletePerson/{pid}")
    @Operation(tags = "Customer Operations",operationId = "delete")
    public String deleteCustomerById(@PathVariable("pid") int pid){
        return crud_operations.removeByCustomerId(pid);
    }

    @PutMapping("{id}")
    @Operation(tags = "Book Operations",operationId = "put")
    public String updateBookTitle(@PathVariable("id") int id,@RequestParam String title){
        return crud_operations.updateTitle(id,title);
    }

    @PostMapping("/pay")
    @Operation(tags = "Payment Operations",operationId = "post")
    public PaymentRequest pay(@RequestBody PaymentRequest paymentRequest){
        logger.info("payment details gotten successfully");
        return bookManagementService.getDetails(paymentRequest);
    }

    @GetMapping("/getExcelSheet")
    public HashSet<Book> readExcelSheet() {
        return bookManagementService.readExcelSheet();
    }

    @GetMapping("/getListSorted")
    public List<Book> sort() {
       return  bookManagementService.sortList();
    }

    @GetMapping("/addSortedListToDb")
    public String addSortedListToDb() {
        return bookManagementService.addSortedListToDb();
    }

    @PostMapping("/post")
    public String create(@RequestParam Long bookId, @RequestParam Long customerId,@RequestBody CustomerRequest customerRequest){
        return bookManagementService.buyBook(bookId,customerId,customerRequest);
    }
}