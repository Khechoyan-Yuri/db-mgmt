<?php
include ('config.php');
		$sentence = "The above ingredient contains ";
		$sql = "SELECT allergenName FROM contains WHERE itemName='".$_POST['ingredient']."'";
		$result = mysqli_query($db,$sql);
		$allergens = [];

		if ($result->num_rows >0){
			while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
				array_push($allergens, $row['allergenName']);
				//$sentence .= ',' . $row['allergenName'] . ' ';
			}
			echo $sentence . implode(', ',$allergens) . '.';
		}
		else {
			echo '';
		}
?>