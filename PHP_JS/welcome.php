<?php
   include('session.php');
?>
<html">
   <head>
      <title>Burger Design Page</title>
   </head>
   
   <body>
      <h1>Design your Burger, <?php echo $login_session; ?>!</h1>
	  <form action="confirmation.php" method="POST">
		<div>
		  <?php
			  $db = mysqli_connect('localhost', 'root', '','homework_2');
			  echo "Bun:   ";
			  $sql = "SELECT itemName FROM category WHERE isRequired=1 AND onlyOne=1";
			  $result = mysqli_query($db,$sql);
			  echo "<select name='ingredient[bun][itemName]'>";
			  while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
				  echo "<option value='" . $row['itemName'] ."'>" . $row['itemName'] ."</option>";
			  }
			  echo "</select>";
			  echo "<input type='hidden' name='ingredient[bun][quantity]' min='1' value='1'>";
			  echo "<br />";
		   ?>
		   <p class="ingredient-warning"></p>
		</div>
		<div>
		   <?php
			  $db = mysqli_connect('localhost', 'root', '','homework_2');
			  echo "Patty: ";
			  $sql = "SELECT itemName FROM category WHERE isRequired=1 AND onlyOne=0";
			  $result = mysqli_query($db,$sql);
			  echo "<select name='ingredient[patty][itemName]'>";
			  while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
				  echo "<option value='" . $row['itemName'] ."'>" . $row['itemName'] ."</option>";
			  }
			  echo "</select>";
			  echo "<input type='number' class='exclude-from-change' name='ingredient[patty][quantity]' min='1' placeholder='Quantity'>";
			  echo "<br />";
		   ?>
		   <p class="ingredient-warning"></p>
		</div>
		
		  <?php
			  $db = mysqli_connect('localhost', 'root', '','homework_2');
			  echo "Extras: ";
			  $sql = "SELECT itemName FROM category WHERE isRequired=0 AND onlyOne=0";
			  $result = mysqli_query($db,$sql);
			  while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
			  echo "<div>";
			  echo "<label>".$row['itemName']."</label>";
			  echo "<input type='number' data-item='".$row['itemName']."' name='ingredient[extras][".$row['itemName']."]' min='0' placeholder='Quantity'>";
			  echo '<p class="ingredient-warning"></p>';
			  echo "</div>";
			  }
		  ?>
		  
		  <?php
			  $db = mysqli_connect('localhost', 'root', '','homework_2');
			  echo "List of items: ";
			  $sql01 = "SELECT itemName,cost,description FROM ingredients";
			  $result01 = mysqli_query($db,$sql01);
			  while ($row = mysqli_fetch_array($result01,MYSQLI_ASSOC)) {
				  echo "<p>--".$row['itemName'] . "<pre>" . "  Cost: $" . $row['cost'] . "</pre>" . "<pre>" . "  Description: " . $row['description'] . "</pre>" . "</p>";
			  }
		  ?>
			
	  <input type = "submit" name = "SubmitOrder" class = "box" /><br/><br />
	</form>
	  
	  
	  <?php
		   if($_SERVER["REQUEST_METHOD"] == "POST") {
			   print_r($_POST);
		   }
	?>
	  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js">
	  </script>
	  <script src="js/main.js">
	  </script>
   </body>
   
</html>