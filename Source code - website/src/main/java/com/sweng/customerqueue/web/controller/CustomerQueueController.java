package com.sweng.customerqueue.web.controller;

import com.sweng.customerqueue.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by vikramh on 4/7/17.
 */
@Controller
public class CustomerQueueController {
    @RequestMapping("/")
    public String formRegisterCustomer(Model model)
    {
        model.addAttribute("customer", new Customer());
        model.addAttribute("action", "/add");
        return "index";
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String addCustomer(@Valid Customer customer, BindingResult result, RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors())
        {
            //Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            //Add customer if invalid data was received
            redirectAttributes.addFlashAttribute("customer",customer);
            //Redirect back to the form
            return "redirect:/";
        }

        return "redirect:/";
    }
}
