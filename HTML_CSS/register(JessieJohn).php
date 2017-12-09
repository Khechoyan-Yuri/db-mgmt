<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Qu·eu·ed - Register</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="styles.css">
  <link href="https://fonts.googleapis.com/css?family=Muli%7CRoboto:400,300,500,700,900" rel="stylesheet"></head>
  <body>

    <div class="main-nav">
        <ul class="nav">
          <li class="name"> 
		  <img src="Images/queued.jpg" alt="Queued Logo" width="150" height="75"></li>
		  <li><a href="index.html">Home</a></li>
          <li><a href="queue.html">Queue</a></li>
		  <li><a href="register.html">Register</a></li>
          <li><a href="admin-login.html">Admin</a></li>
        </ul>
    </div>

	<div class="Login" style="text-align: center;"></div>
	<div class="Login" style="text-align: center;"></div>

    <!-- Start of Content on Page -->
	<main class="flex">
      <div class="card">
		<style="text-align: center;">
		
			<!-- Center Alignment for Greeting -->
	<h1 style="text-align: center;">
		<!-- Welcome Text Object --> 
		<span style="color: #000000;">Registration</span></h1>
	
	<div>
		<!-- Queued Logo Object --> 
		<span style="color: #000000;"> 
		<img style="margin-right: auto; margin-left: auto; display: block;" 
		src="Images/queued.jpg" alt="Queued Logo" /><br /></span></div>
	
		<!-- Center Alignment for Username fields -->
		<form method="POST">
			<div class="Login" 
				style="text-align: center;">
				<!-- Username Object --> 
				<label><strong>First Name</strong></label> 
				<input name="fname" required="" type="text" value="fname" align="" placeholder="i.e. John" />
			</div>
		
			<!-- Center Alignment for spacing -->
			<div class="Login" 
			<style="text-align: center;"><br/>
			</div>
		
		
			<div class="Login" 
			style="text-align: center;">
			<label><strong>Last Name</strong></label> 
			<input name="lname" required="" type="text" value="lname" placeholder="i.e. Smith" /></div>
		
			<div class="Login" style="text-align: center;"></div>
			<div class="Login" style="text-align: center;">
		
		<!-- Center Alignment for spacing -->
			<div class="Login" 
			<style="text-align: center;"><br/>
			</div>
		
		<!-- Phone Number -->
		
			<div class="Login" 
			style="text-align: center;">
			<label><strong>Phone Number</strong></label> 
			<input name="phonenumber" required="" type="number" value="phonenumber" placeholder="1235550186" /></div>
		
			<div class="Login" style="text-align: center;"></div>
			<div class="Login" style="text-align: center;">
		
		<!-- Center Alignment for spacing -->
			<div class="Login" 
			<style="text-align: center;"><br/>
			</div>
			
			<!-- Reason For Visit -->
			<div class="Login" 
			<style="text-align: center;">
			<label><strong>Reason For Visit</strong></label> 
			
		<!-- Drop Down Menu -->
			<select>
				<option selected="null">
				<!--<option value="device">New Customer</option>-->
				<option value="device">Device</option>
				<option value="billing">Billing/Payment</option>
				<option value="account">Plan/Account</option>
				<option value="services">Services/Apps</option>
			</select>
	
		<!-- Center Alignment for spacing -->
			<div class="Login" 
			<style="text-align: center;"><br/>
			</div>
		
			<!-- Login Button Object --> 
			<button type="submit">Login</button></div>
			<div></div>
			
				<div class="Login" style="text-align: center;"></div>
				<div class="Login" style="text-align: center;">
				</div>
		</form>
    </main>
    <footer>
	
      <ul>
        <li><a href="https://github.com/Khechoyan-Yuri/db-mgmt" class="social github">Github</a></li>
      </ul>

      <p class="copyright">© 2017, Yuri Khechoyan, Steven Olsen, Jessie Wilkins, & John McQuaide</p>
	  <div class="Login" style="text-align: center;"></div>
	  <p class="copyright">- ® All Rights Reserved -</p>
    </footer>
  </body>
  </html>
  <?php
	if(isset($_POST["fname"]) && isset($_POST["lname"]) && isset($_POST["phonenumber"]) && (isset($_POST["device"]) || isset($_POST["billing"]) || isset($_POST["account"]) || isset($_POST["services"]))) {
		$servername = "localhost";
		$username = "root";
		$password = "";
		$dbname = "project_db";
		
		try {
			$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			$stmt = $conn->prepare("INSERT INTO customer(Fname, Lname, CellNum, ReasonForVisit) VALUES(?,?,?,?)");
			//Values to input
			$fname = $_POST["fname"];
			$lname= $_POST["lname"];
			$phonenumber= $_POST["phonenumber"];
			if(isset($_POST["device"])) {
				$reason= $_POST["device"];
			}
			
			else if(isset($_POST["billing"])) {
				$reason= $_POST["billing"];
			}
			
			else if(isset($_POST["account"])) {
				$reason= $_POST["account"];
			}
			
			else if(isset($_POST["services"])) {
				$reason= $_POST["services"];
			}
			
			$stmt->execute(array($fname, $lname, $phonenumber, $reason));
			echo "$name has been added<br>";
		}
		catch(PDOException $e){
			echo "Connection failed: " . $e->getMessage();
		}
	}

  ?>