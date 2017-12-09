
<style>
/* The container */
.container {
    display: block;
    position: relative;
    padding-left: 35px;
    margin-bottom: 12px;
    cursor: pointer;
    font-size: 18px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

/* Hide the browser's default radio button */
.container input {
    position: absolute;
    opacity: 0;
}

/* Create a custom radio button */
.checkmark {
    position: absolute;
    top: 0;
    left: 0;
    height: 14px;
    width: 14px;
    background-color: #eee;
    border-radius: 50%;
}

/* On mouse-over, add a grey background color */
.container:hover input ~ .checkmark {
    background-color: #ccc;
}

/* When the radio button is checked, add a blue background */
.container input:checked ~ .checkmark {
    background-color: #2196F3;
}

/* Create the indicator (the dot/circle - hidden when not checked) */
.checkmark:after {
    content: "";
    position: absolute;
    display: none;
}

/* Show the indicator (dot/circle) when checked */
.container input:checked ~ .checkmark:after {
    display: block;
}

/* Style the indicator (dot/circle) */
.container .checkmark:after {
 	top: 4px;
	left: 4px;
	width: 6px;
	height: 6px;
	border-radius: 50%;
	background: white;
}
</style>


	
	<!-- Center Alignment for Username fields -->
	<form method="POST">
			
			<!-- Username Object --> 
			<label><strong>First Name <input name="fname" required="" type="text" value="fname" align="" placeholder="i.e. John" /></strong></label> 
				
		
			<label><strong>Last Name <input name="lname" required="" type="text" value="lname" placeholder="i.e. Smith" /></strong></label> 
			
		
		
		
		<!-- Phone Number -->
		
			<label><strong>Phone Number <input name="phonenumber" required="" type="number" value="phonenumber" placeholder="1235550186" /></strong></label> 
			
		
		
		<!-- Email -->
		
		
			<label><strong>Email <input name="email" required="" type="text" value="email" placeholder="johndoe@email.com" /></strong></label> 
			
		
		
		<!-- Notification Frequency -->
		
			<label><strong>Notification Frequency <input name="notfreq" required="" type="text" value="notfreq" placeholder="eg. 5" /></strong></label> 
			
			
					
		<!-- Preference -->
		
		
			<label><strong>Preference <input name="preference" required="" type="number" value="preference" placeholder="eg. 0" /></strong></label> 
			
		
			
			<!-- Reason For Visit -->
			
			<label><strong>Reason For Visit <!-- Drop Down Menu -->
			<select name="reason">
				<option selected="null">
				<!--<option value="device">New Customer</option>-->
				<option value="device">Device</option>
				<option value="billing">Billing/Payment</option>
				<option value="account">Plan/Account</option>
				<option value="services">Services/Apps</option>
			</select></strong></label> 
			
		
			<!-- Login Button Object --> 
			<button type="submit" value="Submit">Login</button>
			
				
	</form>
   
  <?php
	if(isset($_POST["fname"]) && isset($_POST["lname"]) && isset($_POST["phonenumber"]) && isset($_POST["preference"]) && isset($_POST["notfreq"]) &&isset($_POST["email"]) && isset($_POST["reason"])) {
		$servername = "localhost";
		$username = "root";
		$password = "";
		$dbname = "project_db";
		
		try {
			$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			$stmt = $conn->prepare("INSERT INTO customer(Fname, Lname, CellNum, Email, NotifyFreq, ReasonForVisit, TicketID, Preference) VALUES(?,?,?,?,?,?,?,?)");
			//Values to input
			$fname = $_POST["fname"];
			$lname= $_POST["lname"];
			$phonenumber= $_POST["phonenumber"];
			$email= $_POST["email"];
			$preference = $_POST["preference"];
			$notfreq = $_POST["notfreq"];
			$reason = $_POST["reason"];
			
			$stmt->execute(array($fname, $lname, $phonenumber, $email, $notfreq, $reason, "e", $preference));
			echo "Added<br>";
		}
		catch(PDOException $e){
			echo "Connection failed: " . $e->getMessage();
		}
	}
	
	else{
		echo "WTF?!?";
	}

  ?>