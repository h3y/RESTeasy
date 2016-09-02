$(document).ready(function(){	
		
	//
	// DataTables initialisation
	//
	var table = $('#full-features').DataTable( {
		"processing": true,
		"serverSide": true,
		"dataSrc": "tableData",
		 "ordering": true,
		 "paging": true,
	     "lengthChange": true,
	     "searching": true,
	     "info": true,
	     "autoWidth": false,
		"ajax": getContent(),
		"columns": [
						{"data": "id", "title":'ID'},
						{"data": "rendering_engine", "title":"Rendering Engine"},
						{"data": "browser", "title":"Browser"},
						{"data": "platform", "title":"Platform(s)"},
						{"data": "engine_version", "title":"Engine version"},
						{"data": "css_grade", "title":"	CSS grade"},
						{"data": null, "defaultContent":"<a id='edit-feature' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp&nbsp&nbsp<a id='delete-feature'class='btn btn-sm btn-danger'> <i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}
		  			],

		
	});	


	function checkInput(name){
		if($('[name="'+name+'"]').val().trim() == ""){
			$('[name="'+name+'"]').parent().children()[1].textContent = "Invalid value";
			return false;
		}
		$('[name="'+name+'"]').parent().children()[1].textContent = "";
		return true;
	}

	var save_method;

	//
	//Edit features
	//
    $('#full-features tbody').on( 'click', '#edit-feature', function () {
      	var data = table.row( $(this).parents('tr') ).data();
     	$('[name="id"]').val(data["id"]);
        $('[name="rendering_engine"]').val(data["rendering_engine"]);
        $('[name="browser"]').val(data["browser"]);
        $('[name="platform"]').val(data["platform"]);
        $('[name="engine_version"]').val(data["engine_version"]);
        $('[name="css_grade"]').val(data["css_grade"]);
   	 	$('#modal_form').modal('show');
   	 	$('.modal-title').text('Edit Features');
   	 	save_method = 'update';
    });
    //
    //Add features
    //
	$('#add_features').click(function(){
		$('[name="id"]').val("");
        $('[name="rendering_engine"]').val("");
        $('[name="browser"]').val("");
        $('[name="platform"]').val("");
        $('[name="engine_version"]').val("");
        $('[name="css_grade"]').val("");
		$('#modal_form').modal('show');
		$('.modal-title').text('Add Features');
		save_method = 'add';
	});

    //Save or Edit features
    $('#btnSave').click(function(){
    	var dataType;
    	var request_data;
    	var url;
    	//change url depends of request
    	if(save_method =='add'){
    		$('.modal-title').text('Add Features');
	    	url = "../../restful/api/feature";	
    		dataType = "POST";
    	}else{
	    	$('.modal-title').text('Edit Features');
    		url = "../../restful/api/feature/"+$('[name="id"]').val();
			dataType = "PUT";
		}
		request_data={
			    "rendering_engine": $('[name="rendering_engine"]').val(),
				"browser": $('[name="browser"]').val(),
				"platform": $('[name="platform"]').val(),
				"engine_version": $('[name="engine_version"]').val(),
				"css_grade": $('[name="css_grade"]').val()
		}		
    	if(checkInput("rendering_engine")&checkInput("browser")&checkInput("platform")&checkInput("engine_version")&checkInput("css_grade")){
			$.ajax({
		        url : url,
		        contentType: "application/json",
		        type: dataType,
		        data: JSON.stringify(request_data),
		        success: function()
		        {
		            $('#modal_form').modal('hide');
		          	table.draw();

		        },
		        error: function (jqXHR, textStatus, errorThrown)
		        {
		            alert('Error get data from ajax');
		        }
	   		});
		}
    });

 //delete features
    $('#full-features tbody').on( 'click', '#delete-feature', function () {
         var data = table.row( $(this).parents('tr') ).data();
         if (window.confirm("you really want to delete the entry from the id = "+data["id"]))
		{
			 var url = "../../restful/api/feature/"+data["id"];
			 $.ajax({
			    url : url,
			    type: "DELETE",
			    success: function()
			    {
		            table.draw();
			    },
		        error: function (jqXHR, textStatus, errorThrown)
		        {
		            alert('Error get data from ajax');
		        }
		    })
		}	
    });

});
//get content
function getContent() {
	return function ( request, drawCallback, settings ) {
			settings.jqXHR = $.ajax( {
				"type":     'GET',
				"url":      '../../restful/api/features',
				"data":     requestMapper(request),
				"dataType": "json",
				"cache":    false,
				"success":  function ( json,textStatus, requestj) {
					drawCallback(json);
				}
			} );
	};
}