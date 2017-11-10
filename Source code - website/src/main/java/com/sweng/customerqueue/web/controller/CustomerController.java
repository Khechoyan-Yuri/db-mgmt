package com.sweng.customerqueue.web.controller;

import com.sweng.customerqueue.model.Customer;
import com.sweng.customerqueue.service.CustomerService;
import com.sweng.customerqueue.web.FlashMessage;
import com.sweng.customerqueue.web.Reason;
import com.sweng.customerqueue.service.SmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vikramh on 4/7/17.
 */
@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SmsMessage smsMessage;

    @RequestMapping("/")
    public String displayQueue(Model model)
    {
        List<Customer> customers = customerService.findNotHandled();

        List<Customer> checkedOutCustomers = customers.stream()
                .filter(p -> p.getCheckOutTime() != null).collect(Collectors.toList());

        long waittime = 0;

        if(checkedOutCustomers.size() > 0) {

            // Get total wait time of last 10 customers who checked out
            // (or, instead of 10) list size, if list size is less than 10)
            for (Customer cust : checkedOutCustomers
                    .subList(0, 9 > checkedOutCustomers.size() - 1 ? checkedOutCustomers.size() - 1 : 9)) {
                waittime += cust.getCheckOutTime().getTime() - cust.getCheckInTime().getTime();
            }

            // Get average wait time of last ten customers
            waittime = waittime / 10 / 1000;
        }

        model.addAttribute("waittime", waittime);
        model.addAttribute("count", customers.size());
        model.addAttribute("customers", customers);

        System.out.println("In /");

        return "index";
    }

    @RequestMapping("/customer")
    public String formRegisterCustomer(Model model)
    {
        model.addAttribute("reasons", Reason.values());
        model.addAttribute("customer", new Customer());
        model.addAttribute("action", "/add");
        model.addAttribute("purpose", "register");
        System.out.println("In /customer");
        return "customer";
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String addCustomer(@Valid Customer customer,
                              BindingResult result,
                              RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors())
        {
            //Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customer", result);
            //Add customer if invalid data was received
            redirectAttributes.addFlashAttribute("customer",customer);
            System.out.println("Has errors (/add) \n" + result.toString());
            //Redirect back to the form
            return "redirect:/customer";
        }

        // Strip out non-numerical characters for storing in database
        customer.setMobileNumber(customer.getMobileNumber().replaceAll("[^\\d.]", ""));

        // Set check-in time for customer
        customer.setCheckInTime(Timestamp.valueOf(LocalDateTime.now()));

        // Save customer in database
        customerService.save(customer);
        System.out.println("Saved");

        long waittime = 0;

        List<Customer> customers = customerService.findNotHandled();

        List<Customer> checkedOutCustomers = customers.stream()
                .filter(p -> p.getCheckOutTime() != null).collect(Collectors.toList());

        // Get total wait time of last 10 customers who checked out
        // (or list size, if list size is less than 10)

        if(checkedOutCustomers.size() > 0) {

            // Get total wait time of last 10 customers who checked out
            // (or list size, if list size is less than 10)
            for (Customer cust : checkedOutCustomers
                    .subList(0, 9 > checkedOutCustomers.size() - 1 ? checkedOutCustomers.size() - 1 : 9)) {
                waittime += cust.getCheckOutTime().getTime() - cust.getCheckInTime().getTime();
            }

            // Get average wait time of last ten customers
            waittime = waittime / 10 / 1000;


        }

        redirectAttributes.addFlashAttribute("waittime", waittime);

        redirectAttributes.addFlashAttribute("count",customerService.findNotHandled().size());

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("You are now in the queue.",FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping("/admin")
    public String displayAdminPage(Model model)
    {
        // Get a list of today's customers
        List<Customer> customers = customerService.findAll().stream()
                .filter(p -> !(p.getCheckInTime().before(java.sql.Date
                        .valueOf(LocalDate.now())))).collect(Collectors.toList());
        model.addAttribute("customers", customers);

        return "admin";
    }

    @RequestMapping(value="/edit/{customerId}")
    public String editCustomer(@PathVariable Long customerId, Model model)
    {
        Customer customer = customerService.findById(customerId);

        model.addAttribute("customer", customer);
        model.addAttribute("purpose","edit");
        model.addAttribute("action","/add");

        return "customer";
    }

    @RequestMapping("/handle")
    public String handleCustomer(Model model)
    {

        List<Customer> customers = customerService.findNotHandled();
        if(customers.size()>0)
        {
            Customer customer = customers.get(0);

            // Find oldest customer in queue
            for (int i = 0; i < customers.size(); ++i) {
                if (customer.getCheckInTime().getTime() > customers.get(i).getCheckInTime().getTime()) {
                    customer = customers.get(i);
                }
            }

            // Checkout and save checkout time
            customer.setCheckOutTime(Timestamp.valueOf(LocalDateTime.now()));
            customerService.save(customer);

            // Send out text messages to first 5 customers in
            // queue as well as customer 9 in queue
            for (int i = 0; i < customers.size(); ++i) {
                if(i == 0) {
                    String message = "Thank you for registering with us, " + customer.getFirstName() + "! We " +
                            "are now ready to serve you.  "
                            + "Thank you for being patient with us!\n\n"
                            + "Customer Queue";

                    // smsMessage.sendSms(customer, message);
                }
                else if ((i >= 1 && i <= 4) || i == 9) {
                    customer = customers.get(i);
                    String message = "Thank you for registering with us, " + customer.getFirstName() + "! You are "
                            + "currently" + (i == 1 ? " next " : " (" + i + ") ") + "in line. When you reach position (1) in line, "
                            + "please be in the store and ready for your name to be called. "
                            + "Thank you for being patient with us!\n\n"
                            + "Customer Queue";

                    // smsMessage.sendSms(customer, message);
                }
            }
            // Remove oldest customer
            customers.remove(customers.get(0));
        }

        model.addAttribute("customers", customers);

        return "index";
    }
}
