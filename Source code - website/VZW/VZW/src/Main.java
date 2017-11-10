/* Copyright (C) 2017 Yuri Khechoyan
 * - All Rights Reserved -
 *
 * You may use this code under the terms of
 * the United States:
 * "computer programs" are literary works,
 * under the definition in the Copyright Act, 17 U.S.C. § 101.
 *
 * A copy of the Copyright Act, 17 U.S.C. § 101 is provided
 */

/*==========================================================================

	PROGRAM:  Verizon Customer Service Queue
    AUTHOR: Yuri Khechoyan
    PROJECT NUMBER: VZCSQ
    DATE CREATED: 12/17/2016
    LATEST UPDATE: 2/17/2017
	VERSION: 6.25.8
//##########################################################################

SUMMARY

	This program is designed to work as a Queue (ArrayList)
	Customer will register & add themselves to the queue
	by entering the following criteria:

		-First Name
		-Last Name
		-Mobile Phone Number
		-Reason For Visit (Drop Down Menu - ChoiceBox)

		*Program will keep track of time & when when customer registers
		* Also when they are removed from the Queue


	When initially launched, program will be on standby, waiting for customer input.

	Once customer information is entered, the customer will press the "Register" button
	When pressed, program will stay on Registration screen but it will display GREEN Confirmation text
	letting the customer know that the registration went through properly

	Otherwise, RED Failure Text will appear showing that something went wrong. This gives
	customer a chance to re-attempt registration

	Upon pressing the "REGISTER" button, a SMS text message will be sent to that customers'
	mobile phone number that they have previously entered.

		The initial Registration message will read:

		"Thank you for registering with Verizon Wireless Customer Service, [first name]!
		You are [x] in line. When you reach position (1) in line, please wait for your name to be called.
		Thank You for being patient with us. -Verizon Wireless"

	As more customers register through the system,
	the [x] number increases by 1 (x+1)

	When a Customer Service Representative (Verizon Wireless floor employee) is available,
	they will remove the first customer in the queue by entering a 4-digit pin*

	* - Only Verizon Wireless Management, Employees & the Product Developer will know the 4-digit pin


	After 'Delete' Button is pressed, 2 tasks will occur:

	1. Customer at the top of the list (at that time) is removed from the list.
	 	    A. They will not receive a SMS text message since their name has been called
	 	    and they have already been assigned to a Customer Service Representative


	2. Every other customer (customers 2 & below - after deletion has occurred), will receive a SMS text message
	which reads:

		    "[first name], you are [x-1]** in line for assistance. Please be patient with us. Thank You. -Verizon Wireless"

		    ** - The [x-1] is a place holder. If a customer has moved from 3rd to 2nd
		    in the list, customer will receive the text message (line 66) but [x-1] will
		    be changed to 2 (2nd place in list)


	3. Finally, program will output all of Customers' Information to a Microsoft Excel Document

//##########################################################################

INPUT

	-First Name
	-Last Name
	-Mobile Phone Number
	-Reason for Visit

SAFETY PROTOCOLS

    Program has several Safety Protocols in order to prevent unintentional (or intentional) actions from any user

    -- SEE BELOW --

//-----------------------------------------------------------------------------------------------

    1. On Main Registration Screen: Red Close Button 'X' has been somewhat disabled.

            A. When Red 'X' is pressed, instead of closing the program, Alert Box will appear
            stating that: this operation can not be executed. Meaning that when the subsequent Red 'X' or
            the 'OK' (inside Alert Box window) buttons are clicked, it closes the Alert Box and not the entire program.

            This makes sure that if the Red 'X' is either intentionally or unintentionally pressed,
            program still functions as normal.

            Reason: If Red 'X' executes as it should, everyone on the list (at that time)
            would be deleted and would require re-registration
//------------------------------------------------------------------------------------------------

    2. In View Queue Window: 'Delete' Button

            A. When 'Delete' Button is pressed, customer at the top of the list get removed
            and everyone behind them are shifted up (1) one spot.

            This makes sure that if 'Delete' is either intentionally or unintentionally pressed,
            customers aren't moved up without consent from the Customer Service Representative.

            Reason: If 'Delete' executed as it should, everyone on the list (at that time)
            would be moved up (1) one prematurely

            Requirements: To Delete someone off the list, requires a 4 digit Deletion Code
                a. If Deletion Code is Invalid, Error message will be shown.

//##########################################################################

OUTPUT

	1.	The initial Registration Confirmation SMS Text message will read:

	        "Thank you for registering with Verizon Wireless Customer Service, [first name]!
	        You are [x] in line for assistance. Please be patient with us. Thank You. -Verizon Wireless"

	2. Customer at the top of the list (at that time) is removed from the list.
	 	    A. They will NOT receive a SMS text message

	3. Every other customer them (customers 2 & below), will receive a SMS text message
	    which reads:

		    "[first name], you are [x-1]** in line for assistance. Please be patient with us. Thank You. -Verizon Wireless"

	4. Every time a Customer registers, their:
	        -First Name
	        -Last Name
	        -Reason for Visit
	        -Check-In Time (not shown to customer - printed directly to excel document) - for Management

	5. When the Customer is deleted off of the Queue:
	        -Check-Out Time (not shown to customer - printed directly to excel document) - for Management

//##########################################################################

ASSUMPTIONS

- Verizon Wireless location has a stable WiFi or Cellular Connection (3G/4G/4G LTE - CDMA)
- Customer Service Representative (Verizon floor employee) have memorized the 4-digit pin(s)
  	in order to delete the customer at the top of the list and shut down the system
-(Outside of source code scope): Developer will not be responsible for payment to Twilio
  	(3rd Party) company that provides API for sending SMS text Messages
*/
//##########################################################################

//Imported JavaFX Libraries
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//Imports for Java File IO
import java.io.*;

//Imported Twilio Libraries (Dependency version)
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


//Imported ArrayList Libraries
import java.util.ArrayList;

public class Main extends Application {

    //Set-up: accessing the Twilio account that will be used
    //Find your Account SID and Token at twilio.com/user/account
    private static final String ACCOUNT_SID = "AC25715ec0e85d62fdaf2f3f5d176f4f5a";
    private static final String AUTH_TOKEN = "d711dc1eb4af9bf60e514ae231be8bfd";

    //Create ArrayList for Customers
    ArrayList<Customer> CustomerList = new ArrayList<Customer>();

    //******************
    // MAIN METHOD
    //******************

    public static void main(String[] args) {
        //Initializing SID & Authentication token for usage
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        //launch goes into Application, then calls start
        launch(args);
    }

    //If someone Accidentally (or intentionally) tries to close the program
    //It will throw an alert - line 279
    private void accidentalShutDown(Stage alertWindow) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Well this is embarrassing for me... "
                + ("I can't do that. Please take me back to the login screen. Thank you"), ButtonType.OK);
        if (alert.showAndWait().equals(ButtonType.OK)) {
            // you may need to close other windows or replace this with Platform.exit();
            alertWindow.close();
        }
    }

    //***********************************
    //  Entire Functionality of Program
    //***********************************
    @Override
    //Primary Window
    //start method
    public void start(Stage primaryStage) {
        //Entire Window = 'Stage' - Max, Min, Close, etc.
        //Content inside Window = 'Scene'
        //Layout = where everything is placed
        //Hierarchy Structure = Stage, Scene, Layout
//-----------------------------------------------------------------------------------------------

        //***********************************************
        // Creating an empty file for keeping records
        // New File is created each new Work Day
        //***********************************************

        FileManage file = new FileManage();
        file.openFile();

        //---------------------------------------------------------------------------------------

        //##############################
        //  INITIAL PROGRAM WINDOW/STAGE
        //##############################

        //Setting title on top of Window
        primaryStage.setTitle("Verizon Wireless Corporate Retail Store - Kirkwood, MO");
        //Make Window (Stage) non-resizable
        primaryStage.setResizable(false);
        //Creates the structure of the layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Creates a content inside Stage
        Scene scene = new Scene(grid, 730, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Creates short description for user to understand program (static text)
        Text SceneTitle = new Text("-- Welcome to the Verizon Wireless Corporate Store in Kirkwood, MO --\n"
                + "To be assisted by a Customer Service Representative: Enter your First Name,\n"
                + "Last Name, and your (10) ten-digit Mobile Phone Number. (i.e. 0123456789).\n"
                + "And a Reason for your Visit today. The phone number will be used to send you\n"
                + "SMS Text Messages. When done, click the ‘Register’ Button to confirm.");

        //Styling of the description
        SceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        SceneTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(SceneTitle, 0, 0, 2, 1);

        //-------------------------------------------------------------------------------

        //*******************************
        // Static Labels - what user sees
        //*******************************

        //Creates "First Name" Label (non-editable) - static text
        Label FName = new Label("First Name:");
        grid.add(FName, 1, 2);

        //Creates "Last Name" Label (non-editable) - static text
        Label LName = new Label("Last Name:");
        grid.add(LName, 1, 3);

        //Creates "Mobile Phone Number" Label (non-editable) - static text
        Label phNumber = new Label("Mobile Phone Number:");
        grid.add(phNumber, 1, 4);

        //Creates "Reason For Visit" Label (non-editable) - static text
        Label tv_reasonForVisit = new Label("Reason For Visit:");
        grid.add(tv_reasonForVisit, 1, 5);

        //Creates Copyright Label (non-editable) - static text
        Label Copyright = new Label("Copyright (C) 2017 Yuri Khechoyan | "
                +"All Rights Reserved");
        Copyright.setTextFill(Color.BLACK);
        Copyright.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(Copyright, 2, 30);

        //---------------------------------------------------------------------------------

        //******************************
        // Text Fields for User Input
        //******************************

        //Creates a TextField in order for user input - First name
        TextField userFName = new TextField();
        grid.add(userFName, 2, 2);

        //Creates a TextField in order for user input - Last name
        TextField userLName = new TextField();
        grid.add(userLName, 2, 3);

        //Creates a TextField in order for user input - Phone number
        TextField userPhNumber = new TextField();
        grid.add(userPhNumber, 2, 4);

        //Creates a ChoiceBox - Reason for Visit
        ChoiceBox<String> reasonForVisit = new ChoiceBox<>();
        grid.add(reasonForVisit, 2, 5);

        //Adds items to reasonForVisit ChoiceBox - why Customer is registering
        reasonForVisit.getItems().addAll("Account Issues", "Billing/Payment", "Device Upgrade",
                "New Customer", "Technical Support", "Other");

        //----------------------------------------------------------------------------------

        //***************************
        //Verizon Wireless Logo
        //***************************

        //Add VZW Logo in the bottom right corner of window
        Image image = new Image("imgs/vzwLogo.png");
        ImageView vzLogo = new ImageView(image);
        vzLogo.setFitHeight(45);
        vzLogo.setFitWidth(145);
        vzLogo.setPreserveRatio(true);
        grid.add(vzLogo, 3, 30);

        //If red 'X' is pressed on primaryStage,
        //go to accidentalShutDown method: line 193
        primaryStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
            // execute with accidentalShutDown procedure
            accidentalShutDown(primaryStage);
        });

        //------------------------------------------------------------------------------------------

        //***********************************************
        // BUTTONS - Reg, Shut Down, View Queue
        //***********************************************

        //Creates Registration Button
        Button btnReg = new Button("Register");
        HBox hbBtnReg = new HBox(0);
        hbBtnReg.setAlignment(Pos.CENTER);
        hbBtnReg.getChildren().add(btnReg);
        grid.add(hbBtnReg, 2, 7);

        //Creates View Button - viewing people on list
        Button btnView = new Button("View Queue");
        HBox hbBtnView = new HBox(10);
        hbBtnView.setAlignment(Pos.BOTTOM_CENTER);
        hbBtnView.getChildren().add(btnView);
        grid.add(hbBtnView, 3, 5);

        //Creates Shut Down Button
        Button btnPWD = new Button("Shut Down");
        HBox hbBtnPWD = new HBox(10);
        hbBtnPWD.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnPWD.getChildren().add(btnPWD);
        grid.add(hbBtnPWD, 0, 30);

        //Creates Small sub text near Register button (Reg. Success)
        final Text RegComplete = new Text();
        grid.add(RegComplete, 2, 9);

        //Creates Small sub text near Register button (Reg. Fail)
        final Text RegFail = new Text();
        grid.add(RegFail, 2, 9);
//-------------------------------------------------------------------------------

        //*******************************************************************************
        // EVENT HANDLER FOR REGISTRATION BUTTON - HANDLES USER INPUT INSIDE TEXT FIELDS
        //*******************************************************************************

        btnReg.setOnAction((ActionEvent e) -> {

            //If text Fields are empty, throw Error
            if (userFName.getText().isEmpty() || userLName.getText().isEmpty() || userPhNumber.getText().isEmpty()) {

                //Adds Red Text left of button telling user that Registration Failed
                RegFail.setFill(Color.FIREBRICK);
                RegFail.setText("Registration has failed. Please re-enter your credential(s)");
                RegFail.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
                RegFail.setVisible(true);
                RegComplete.setVisible(false);
            }
            //If Fields are not empty, throw Success message & sends SMS message to confirm
            else {
                RegFail.setVisible(false);
                RegComplete.setVisible(true);

                //Adds Green Text below 'Register' button telling user that Registration Succeeded
                RegComplete.setFill(Color.GREEN);
                RegComplete.setText("Registration complete! An SMS Text Message is being sent.");
                RegComplete.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));

                //Assigning firstname variable to user input
                String firstname = userFName.getText();

                //Assigning lastname variable to user input
                String lastname = userLName.getText();

                //Assigning MobileNumber to user input
                String mobileNumber = userPhNumber.getText();

                //Add customer to the ArrayList: CustomerList
                Customer customer = new Customer(firstname, lastname, mobileNumber);
                CustomerList.add(customer);

                //Variable for position in line: (how many people are in front of person x)
                int position = CustomerList.size();

                //Retrieve the Reason for Customer's Visit
                String Reason = reasonForVisit.getValue();


                //Add All credentials to File (fn, ln, reason, checkInTime)
                try {
                    file.addRecords(firstname, lastname, mobileNumber, Reason);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //Create message for the user - sent to their mobile numbers' (unformatted)
                Message message = Message.creator(new PhoneNumber("+1" + userPhNumber.getText()),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        "Thank you for registering with Verizon Wireless, " + userFName.getText() + "! You are "
                                + "currently (" + (position) + ")" + " in line. When you reach position (1) in line, "
                                + "please wait for your name to be called. "
                                + "Thank you for being patient with us!\n\n"
                                + " -Verizon Wireless").create();
                System.out.println(message.getSid());

                //Clear Text Fields when Registration is Complete
                userFName.clear();
                userLName.clear();
                userPhNumber.clear();

                //Setting Default Value
                reasonForVisit.setValue("Account Issues");

            }
        });
//----------------------------------------------------------------------------------

        //**************************************
        // EVENT HANDLER FOR SHUT DOWN BUTTON
        //**************************************

        btnPWD.setOnAction((ActionEvent s) -> {

            //###############################
            // SHUT DOWN PROGRAM WINDOW
            //###############################

            Stage pwdStage = new Stage();
            //Setting title on top of Window
            pwdStage.setTitle("System Shut Down");
            //Make Window (Stage) non-resizable
            pwdStage.setResizable(false);

            //Created Grid for storing objects inside pwdGrid
            GridPane pwdGrid = new GridPane();
            pwdGrid.setAlignment(Pos.TOP_CENTER);

            pwdGrid.setHgap(10);
            pwdGrid.setVgap(10);
            pwdGrid.setPadding(new Insets(25, 25, 25, 25));

            Scene pwdScene = new Scene(pwdGrid, 350, 350);

            pwdStage.setScene(pwdScene);
            pwdStage.show();

            //Creates short description so that the user can read & find out what this is used for
            Text pwdSceneTitle = new Text("To shut down the system, please enter" +
                    "\nthe System Shut Down Code below\n then click the Shut Down Button");

            //Styling of the description
            pwdSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            pwdGrid.add(pwdSceneTitle, 0, 0, 2, 1);

            //Creates a TextField in order for user input - Shut Down Code input
            TextField userPWD = new TextField();
            pwdGrid.add(userPWD, 1, 2);

            //Creates Shut Down Button
            Button pwdBtn = new Button("Shut Down");
            HBox hbPwdBtn = new HBox(10);
            hbPwdBtn.setAlignment(Pos.BOTTOM_CENTER);
            hbPwdBtn.getChildren().add(pwdBtn);
            pwdGrid.add(hbPwdBtn, 1, 5);

            //Add VZW Logo to the bottom of the window
            Image imagePWD = new Image("imgs/vzwLogo.png");
            ImageView vzLogoPWD = new ImageView(imagePWD);
            vzLogoPWD.setFitHeight(45);
            vzLogoPWD.setFitWidth(145);
            vzLogoPWD.setPreserveRatio(true);
            pwdGrid.add(vzLogoPWD, 2, 15);

            //Creates Small sub text near Shut Down button (Fail) - invalid
            final Text pwdFailInval = new Text();
            pwdGrid.add(pwdFailInval, 1, 7);

//*************************************************************************************

            //***********************************************************************
            // EVENT HANDLER FOR OFFICIAL SHUT DOWN BUTTON - AFTER CODE IS SUCCESSFUL
            //***********************************************************************

            pwdBtn.setOnAction((ActionEvent p) -> {
                final String shutDown;
                shutDown = "1111";
                //if user inputted code does not match the actual code, throw error message
                if (!userPWD.getText().matches(shutDown)) {
                    pwdFailInval.setFill(Color.FIREBRICK);
                    pwdFailInval.setText("An Invalid Code was Entered.");
                    pwdFailInval.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 12));
                    pwdFailInval.setVisible(true);
                }
                //if inputted code matches the actual coe, program shuts down
                //CustomerList information is permanently erased
                if (userPWD.getText().matches(shutDown)) {
                    pwdBtn.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent pwd) {
                            Platform.exit();
                            System.exit(1);
                            CustomerList.clear();
                        }
                    });
                }
            });
        });
        //Creates text that is seen when queue is empty
        Text viewEmpty = new Text("The Queue is Empty!");
        //Styling of Empty Queue text
        viewEmpty.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));

//*******************************************************************************

        //**************************************
        // EVENT HANDLER FOR VIEW QUEUE BUTTON
        //**************************************

        btnView.setOnAction((ActionEvent v) -> {
            Stage viewStage = new Stage();
            //Setting title on top of Window
            viewStage.setTitle("Customer Queue");
            //Make Window (Stage) non-resizable
            viewStage.setResizable(true);

            GridPane viewGrid = new GridPane();
            viewGrid.setAlignment(Pos.TOP_CENTER);

            //Padding
            viewGrid.setHgap(10);
            viewGrid.setVgap(10);
            viewGrid.setPadding(new Insets(25, 25, 25, 25));

            //Create Default size for View Queue Window
            Scene viewScene = new Scene(viewGrid, 500, 550);

            viewStage.setScene(viewScene);
            viewStage.show();

            //Creates sub text so that the user can read what they are looking at
            Text viewSceneTitle = new Text("Customers currently waiting for assistance:");

            //Styling of the description
            viewSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
            viewGrid.add(viewSceneTitle, 2, 0, 2, 1);

//*********************************************************************************************

            //Condition in which 'View Queue' is pressed, but no one registered
            if (CustomerList.isEmpty()) {
                viewGrid.add(viewEmpty, 3, 15, 2, 1);
            }
            //Creates View Button - Delete button that launches the Deletion sequence
            Button btnDel = new Button("Delete");
            HBox hbBtnDel = new HBox(10);
            hbBtnDel.setAlignment(Pos.BOTTOM_CENTER);
            hbBtnDel.getChildren().add(btnDel);
            viewGrid.add(hbBtnDel, 3, 10);

            //Text above the names - helps identify customers' first & last name
            Text viewFName = new Text("FIRST NAME");
            Text viewLName = new Text("LAST NAME");

            //Styling of the FNames and LNames
            viewFName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
            viewGrid.add(viewFName, 1, 2, 2, 1);

            viewFName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
            viewGrid.add(viewLName, 4, 2, 2, 1);

            //Condition in which 'View Queue' is pressed & info is already entered from Registration
            if (CustomerList.isEmpty()) {
                viewEmpty.setVisible(true);
                btnDel.setVisible(false);
            } else {
                //Display List of people currently in queue
                for (int place = 0; place < CustomerList.size(); place++) {
                    //Displays customer's first & last name
                    Text viewActFName = new Text(CustomerList.get(place).firstname);
                    Text viewActLName = new Text(CustomerList.get(place).lastname);

                    //Styling of the FNames
                    viewActFName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    viewGrid.add(viewActFName, 1, place + 4, 2, 1);

                    //Styling of the LNames
                    viewActLName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    viewGrid.add(viewActLName, 4, place + 4, 2, 1);

                    //Prints out entire list to console when View Queue is pressed
                   //System.out.println(place + "-" + CustomerList.get(place).firstname + " " + CustomerList.get(place).lastname);
                }
            }
            //Add VZW Logo to the bottom of the viewQueue window
            Image imageVQ = new Image("imgs/vzwLogo.png");
            ImageView vzLogoVQ = new ImageView(imageVQ);
            vzLogoVQ.setFitHeight(45);
            vzLogoVQ.setFitWidth(145);
            vzLogoVQ.setPreserveRatio(true);
            //VZW Logo will need to be positioned at columnindex: 50, rowindex: 45
            viewGrid.add(vzLogoVQ, 4, 38);

            //Notify that customer has been deleted - only works with 2+ people in queue
            //If there is only 1 person in queue, it will not show
            Text viewUserDel = new Text("Customer: Deleted.\nClose window to update list.");
            viewUserDel.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
            viewGrid.add(viewUserDel, 3, 15, 2, 1);
            viewUserDel.setVisible(false);

//**********************************************************************************************

            //***************************************************************************
            // EVENT HANDLER FOR DELETE CUSTOMER BUTTON - Deletion Code window
            //***************************************************************************

            btnDel.setOnAction((ActionEvent d) -> {

                Stage delStage = new Stage();
                //Setting title on top of Window
                delStage.setTitle("Delete Customer");
                //Make Window (Stage) non-resizable
                delStage.setResizable(false);

                GridPane delGrid = new GridPane();
                delGrid.setAlignment(Pos.TOP_CENTER);

                delGrid.setHgap(10);
                delGrid.setVgap(10);
                delGrid.setPadding(new Insets(25, 25, 25, 25));

                Scene delScene = new Scene(delGrid, 380, 350);

                delStage.setScene(delScene);
                delStage.show();

                //Creates short description so that the user knows what this part of program does
                Text delSceneTitle = new Text("To delete a customer, enter the Deletion Code below" +
                        "\nthen click the 'Delete Customer' Button");

                //Styling of the description
                delSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                delGrid.add(delSceneTitle, 0, 0, 2, 1);

                //Creates a TextField in order for user input - Delete Customer Code
                TextField userDel = new TextField();
                delGrid.add(userDel, 1, 2);

                //Creates OFFICIAL Delete Customer Button
                Button btnDelCustomerOff = new Button("Delete Customer");
                HBox hbDelBtn = new HBox(10);
                hbDelBtn.setAlignment(Pos.BOTTOM_CENTER);
                hbDelBtn.getChildren().add(btnDelCustomerOff);
                delGrid.add(hbDelBtn, 1, 5);

                //Add VZW Logo to the bottom of the window
                Image imageDel = new Image("imgs/vzwLogo.png");
                ImageView vzLogoDel = new ImageView(imageDel);
                vzLogoDel.setFitHeight(45);
                vzLogoDel.setFitWidth(145);
                vzLogoDel.setPreserveRatio(true);
                delGrid.add(vzLogoDel, 2, 15);

                //Creates Small sub text near Shut Down button (Fail) - invalid
                final Text delFailError = new Text();
                delGrid.add(delFailError, 1, 7);

 //*********************************************************************************************

                //*******************************************
                // EVENT HANDLER FOR DELETE CUSTOMER BUTTON
                //******************************************

                btnDelCustomerOff.setOnAction((ActionEvent delCusOfficial) -> {
                    final String delCustomer;
                    delCustomer = "2222";
                    //if inputted deletion code does not match actual code, throw error
                    if (!userDel.getText().matches(delCustomer)){
                        delFailError.setFill(Color.FIREBRICK);
                        delFailError.setText("An Invalid Code was Entered.");
                        delFailError.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 12));
                        delFailError.setVisible(true);
                    }
                    //if inputted code matches actual code: delete first customer off list
                    //move everyone else up in the list
                    //send people in 1st, 2nd, 3rd, 4th, 5th, & 10th place a text letting them know their position
                    //after deletion
                    if (userDel.getText().matches(delCustomer)) {

                        //Closes Deletion Window - goes mack to Queue Window
                        delStage.close();

                        //Deletes first customer off of the List
                        CustomerList.remove(0);


                        try {
                            file.delRecords();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                //Incrementally find 1/2/3/4/5/10 and send them an SMS Text update
                    for (int place = 0; place < CustomerList.size(); place++) {
                    viewUserDel.setVisible(true);

                //List Position that will receive updated status SMS Text Messages
                    int placeZero = 0;// 1st  - in line after deletion
                    int placeOne = 1;// 2nd  - in line after deletion
                    int placeTwo = 2;// 3rd  - in line after deletion
                    int placeThree = 3;// 4th  - in line after deletion
                    int placeFour = 4;// 5th  - in line after deletion
                    int placeNine = 9;// 10th  - in line after deletion


                        if (place == placeZero) {
                        //Sends SMS message to 1st customer in line after deletion is complete
                        Message posInLine0 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),

                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                             + ") in line for assistance. Please wait for your name to be called. "
                             +"Thank You.\n\n -Verizon Wireless").create();
                                System.out.println(posInLine0.getSid());
                        }

                        if (place == placeOne) {
                        //Sends SMS message to 2nd customer in line after deletion is complete
                         Message posInLine1 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                           + ") in line for assistance. Please be patient with us. "
                           +"Thank You.\n\n -Verizon Wireless").create();
                            System.out.println(posInLine1.getSid());
                        }

                        if (place == placeTwo) {
                         //Sends SMS message to 3rd customer in line after deletion is complete
                        Message posInLine2 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                            + ") in line for assistance. Please be patient with us. "
                            +"Thank You.\n\n -Verizon Wireless").create();
                            System.out.println(posInLine2.getSid());
                        }

                        if (place == placeThree) {
                            //Sends SMS message to 4th customer in line after deletion is complete
                        Message posInLine3 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                        + ") in line for assistance. Please be patient with us. "
                        +"Thank You.\n\n -Verizon Wireless").create();
                            System.out.println(posInLine3.getSid());
                        }

                        if (place == placeFour) {
                         //Sends SMS message to 5th customer in line after deletion is complete
                         Message posInLine4 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                           + ") in line for assistance. Please be patient with us. "
                           +"Thank You.\n\n -Verizon Wireless").create();
                           System.out.println(posInLine4.getSid());
                        }

                        if (place == placeNine) {
                        //Sends SMS message to 10th customer in line after deletion is complete
                        Message posInLine9 = Message.creator(new PhoneNumber("+1" + CustomerList.get(place).mobileNumber),
                        //Number from Twilio - from number (unformatted)
                        new PhoneNumber("+13146268325"),
                        CustomerList.get(place).firstname + ", you are now (" + (place+1)
                            + ") in line for assistance. Please be patient with us. "
                            +"Thank You.\n\n -Verizon Wireless").create();
                            System.out.println(posInLine9.getSid());
                        }

                      }
                    }
                });

            });
        });
    }
}

        //--------------------------------------------------------------------------------------
        //                      END OF CODE RIGHT HERE
        //-----------------------------------------------------------------------------------
        //Google JavaFX Form creation for the basics of a Registration program
