<?php
include ('session.php');
if ($_POST){
	$total001 = 0;
	$OrderBuilder = "";
		//print '<pre>'; print_r( $_POST ); print '</pre>'; exit;
		foreach($_POST['ingredient'] as $name => $ingredient) {
			if($name == "extras"){
				echo "<h2>". ucfirst($name) ."</h2>";
					foreach($ingredient as $extraName => $extraQuantity){
						echo "<p>Name: " . $extraName . "</p>";
						echo "<p>Quantity: " . $extraQuantity . "</p>";
						$OrderBuilder = $OrderBuilder . $extraName . "(" . $extraQuantity . "),";
					}
			}
			else{
				echo "<h2>". ucfirst($name) ."</h2>";
				echo "<p>Name: " . $ingredient['itemName'] . "</p>";
				echo "<p>Quantity: " . $ingredient['quantity'] . "</p>";
				$OrderBuilder = $OrderBuilder . $ingredient['itemName'] . "(" . $ingredient['quantity'] . "),";				
			}
		}
		
		foreach($_POST['ingredient'] as $name => $ingredient) { //this for loop runs and calculates the total price of the order.
			
			if($name == "extras"){ //this is because we get the array in this file in a much different orientation than bun and patties.
				foreach($ingredient as $extraName => $extraQuantity){
						$db001 = mysqli_connect('localhost', 'root', '','homework_2');
						$sql001 = "SELECT cost FROM ingredients WHERE itemName='".$extraName."'";
						$result001 = mysqli_query($db001,$sql001);
						$row001 = mysqli_fetch_array($result001,MYSQLI_ASSOC);
						$total001 += $row001['cost'] * $extraQuantity;
				}
			}
			else{
			  $db001 = mysqli_connect('localhost', 'root', '','homework_2');
			  $sql001 = "SELECT cost FROM ingredients WHERE itemName='".$ingredient['itemName']."'";
			  $result001 = mysqli_query($db001,$sql001);
			  $row001 = mysqli_fetch_array($result001,MYSQLI_ASSOC);
			  $total001 += $row001['cost'] * $ingredient['quantity'];				
			} 
		}
		echo "<h1> Your total is: $<u>" .$total001 . "</u></h1>";
		//GENERATE THE ORDER
		 $OrderNumOutput = substr(str_shuffle(str_repeat("0123456789", 6)), 0, 6);
		 $db002 = mysqli_connect('localhost', 'root', '','homework_2');
		 $sql002 = "SELECT OrderNum FROM orders WHERE OrderNum='".$OrderNumOutput."'";
		 $result002 = mysqli_query($db002,$sql002);
		 //scan through database to make sure the order number (by chance) doesnt already exist.
		 if (mysqli_num_rows($result002) == 0) {
			 //echo "<p>No duplicate OrderID's</p>";
		 } 
		 else {
			 $OrderNumOutput = substr(str_shuffle(str_repeat("0123456789", 6)), 0, 6);
		 }
		/*WEAKNESS: only generates a new order ID once. Chance is microscopic that you'll get 2 of the 
					same keys twice but if this was like McDonalds' database, it would be a concern.*/
		echo "<p>Your order ID is: " .$OrderNumOutput . "</p>";
		
		//"Your order ticket is : above generated number"
		//"Your order will be delivered to *address, state city zip*"
		
		 $db003 = mysqli_connect('localhost', 'root', '','homework_2');
		 $sql003 = "SELECT AddressesID,phoneNum FROM customers WHERE Username='".$login_session."'";  //OKAY GET READY FOR A MIND-WRECKER: AddressesID is in customers, but AddressID is in addresses. 
		 $result003 = mysqli_query($db003,$sql003);
		 $row003 = mysqli_fetch_array($result003,MYSQLI_ASSOC);
		 $LinkerAddress = $row003['AddressesID'];
		 $LinkerPhone = $row003['phoneNum'];
		 $db004 = mysqli_connect('localhost', 'root', '','homework_2');
		 $sql004= "SELECT street,city,state,zip FROM addresses WHERE AddressID='".$LinkerAddress."'";
		 $result004 = mysqli_query($db004,$sql004);
		 $row004 = mysqli_fetch_array($result004,MYSQLI_ASSOC);
		 
		 echo "<p>Burger will be delivered to: " .$row004['street'] . "," . $row004['city'] . " " . $row004['state'] . " " . $row004['zip'] . "</p>";
		 date_default_timezone_set('America/Chicago');
		 $date = date('m/d/Y h:i', time());
		 
		  $db006 = mysqli_connect('localhost', 'root', '','homework_2');
		  $sql006 = "INSERT INTO burgers" . "(OrderNum,total,itemName) VALUES (\"" . $OrderNumOutput . "\",\"" . $total001 . "\",\"" . $OrderBuilder . "\");";
		  mysqli_query($db006,$sql006);
		  
		 
		  $db005 = mysqli_connect('localhost', 'root', '','homework_2');
		  $sql005 = "INSERT INTO orders" . "(OrderNum,phoneNum,time) VALUES (\"" . $OrderNumOutput . "\",\"" . $LinkerPhone . "\",\"" . $date . "\");";
		  mysqli_query($db005,$sql005);
		  
		 
		//output each ingredient and quantity to the burgers table
		//get phoneNum (orders) to customers (phonenum) and AddressesID(customers) to AddressesID (addresses)
		
		echo "<p><b><u>Your order has been placed!</u></b></p>";
		echo "<h2><a href = 'logout.php'>Sign Out</a></h2>";
		
		
		/*
		A few notes:
			-My category system works with 1's and 0's instead of another mechanism. In this way, I have 
			3 consise categories: buns, patties, and extras. Extras is cheese and extras.
			-I don't use caregory name because it's a key and doesn't want to work well with the website
			-In a real database, you wouldnt want to connect as many times as I do in this php script.
			-The login isn't super secure, however a create account system with caesar cipher, SHA1, or MD5
			would be easy enough to implement on a larger scale in a more serious website.
			-A big weakness comes when you don't secure the SQL calls. If a malicious individual appended
			a drop all tables statement to an unsecure SQL call, you'd loose your database.
			-Allergens are displayed dynamically via a Jquery function that uses google's hosted AJEX
			functionality.
			-Although it doesn't automatically take them back to the homepage after displaying the summary,
			they can manually log out. Maybe an "on window.close -> logout.php" would solve the alternate case
			where they just absolutely don't log out and close the window.
			-allergens->percentAffected isn't used.
			-2 new columns were added for the login system:  Pass and Username
			-customer can't order 2 types of patties. if I were to allow them to do this, I'd do it in the same way I did the extras system with a few
			 more constraints and conditions.
			
			
		---INSTRUCTIONS---
		Supplied will be my sql database dump file, my username and password, and basic instructions on how my whole website works:
		
		
		-To access the website on localhost:
		  +start XAMPP with MySQL and Apache running
		  + go to:   http://localhost/DatabaseHomework4/HomePage.html
		  + make sure the DatabaseHomework4 directory is stored in c:/xampp/htdocs/--------
		---!!!SQL DUMP FILE IS ATTACHED IN THE .zip FILE!!!---
		
		Username: "wowguy233"
		password: "plaintextPassword"
		
		1)  So the website starts up on localhost. you click the button on the homepage, it takes you to the login page. 
		2)  you enter the supplied username and password and the php script checks it against the homework_2 customers database
		3) successful: log in and start ordering. The drop down menus are build by looping the conditions in the SQL query until it has populated
		   a category. rinse and repeat 3 times for the 3 combinations of 1's and 0's I have for categories.
		4) dynamically, the page updates allergies via ajex and jquery event processing.
		5) statically, the page displays all ingredient descriptions, costs, and the associated item name for each ingredient and just kinda displays
		   them on the page in a sorta boring way. Also the customer has to do the math in their head for the price of each item as a dynamically displayed and updated
		   running total variable would be very complex and time consuming to produce.
		6) hit the submit button to post all of the data to a confirmation file where all information is processed.
		7) the confirmation page works like this:  
				a) display the category type along with the quantity and name of the customer's order
				b) display extras in a different way (since this array is different from the patty and bun array)
				c) generate the order that the chef will see by appending to a string called OrderBuilder (swiss cheese(1),buffalo(3),...)
				d) display total 
				e) generate random order number, check against database for existing order number by the same number (very small chance but not impossible)
				f) get customer's information from the database to basically tell them "this is where the burger is being delivered".
				g) send all relevant information to the database, like OrderBuilder, total, orderID, etc...
				h) sign out button that takes them to the HomePage   :)
		8) done!
		
		ALSO ATTACHED: screenshots showing how it looks on my computer.
		*/
}