package com.sweng.customerqueue.service;

import com.sweng.customerqueue.model.Customer;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vikramh on 4/26/17.
 */
@Service
public class SmsMessage {
    //Set-up: accessing the Twilio account that will be used
    //Find your Account SID and Token at twilio.com/user/account
    private String ACCOUNT_SID= "AC25715ec0e85d62fdaf2f3f5d176f4f5a";
    private String AUTH_TOKEN = "d711dc1eb4af9bf60e514ae231be8bfd";


    @Autowired
    public SmsMessage() {
        //Initializing SID & Authentication token for usage
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(Customer customer, String messageText) {
        //Create message for the user - sent to their mobile numbers' (unformatted)
        Message message = Message.creator(new PhoneNumber("+1" + customer.getMobileNumber()),
                //Number from Twilio - from number (unformatted)
                new PhoneNumber("+13146268325"),
                messageText).create();
        System.out.println(message.getSid());
    }
}
