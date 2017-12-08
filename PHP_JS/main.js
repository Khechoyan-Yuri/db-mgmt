$(document).ready(function(){
	//alert("working!")
	$('select').change(function(){
		//get the ingredient that was changed too
		//console.log($(this).val())
		//get the allergen information from the database. then display on tooltip hover-over <<SCRAPPED>>
		var ingredient = $(this).val()
		var context    = $(this)
		
			$.ajax({
				url: "process-allergen.php",
				type: 'POST',
				data: {
					ingredient: ingredient
				},
				success: function(data){
					//console.log(data);
					//console.log(context);
					context.parents('div').find('.ingredient-warning').text(data)
				}
			});
			//display results of the check
	})

	$('input[type="number"]').not('.exclude-from-change').change(function(){
		//get the ingredient that was changed too
		//console.log($(this).val())
		//get the allergen information from the database. then display on tooltip hover-over
		var ingredient = $(this).attr('data-item')
		var context    = $(this)
		
		if($(this).val() > 0){
		
			$.ajax({
				url: "process-allergen.php",
				type: 'POST',
				data: {
					ingredient: ingredient
				},
				success: function(data){
					//console.log(data);
					//console.log(context);
					context.parents('div').find('.ingredient-warning').text(data)
				}
			});
			//display results of the check
		}else{
			context.parents('div').find('.ingredient-warning').text('')
		}
	})
})