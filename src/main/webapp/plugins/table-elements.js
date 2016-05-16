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
		"ajax": $.fn.dataTable.pipeline({
			url: '../../restful/api/features',
		}),
		"columns": [
						{"data": "id", "title":'ID'},
						{"data": "renderingengine", "title":"Rendering Engine"},
						{"data": "browser", "title":"Browser"},
						{"data": "platforms", "title":"Platform(s)"},
						{"data": "engineversion", "title":"Engine version"},
						{"data": "cssgrade", "title":"	CSS grade"},
						{"data": null, "defaultContent":"<a id='edit-feature' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp&nbsp&nbsp<a id='delete-feature'class='btn btn-sm btn-danger'> <i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}
		  			],

		
	} );	

	var save_method;
	var data;
	//
	//Edit features
	//
    $('#full-features tbody').on( 'click', '#edit-feature', function () {
          	data = table.row( $(this).parents('tr') ).data();
         	$('[name="id"]').val(data["id"]);
            $('[name="renderingengine"]').val(data["renderingengine"]);
            $('[name="browser"]').val(data["browser"]);
            $('[name="platforms"]').val(data["platforms"]);
            $('[name="engineversion"]').val(data["engineversion"]);
            $('[name="cssgrade"]').val(data["cssgrade"]);
       	 	$('#modal_form').modal('show');
       	 	$('.modal-title').text('Edit Features');
       	 	save_method = 'update';

    });
    //
    //Add features
    //
	$('#add_features').click(function(){
		$('[name="id"]').val("");
        $('[name="renderingengine"]').val("");
        $('[name="browser"]').val("");
        $('[name="platforms"]').val("");
        $('[name="engineversion"]').val("");
        $('[name="cssgrade"]').val("");
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

		if($('[name="engineversion"]').val()==""){
    			$('[name="engineversion"]').val(0);
    	}

		request_data={
			    "renderingengine": $('[name="renderingengine"]').val(),
				"browser": $('[name="browser"]').val(),
				"platforms": $('[name="platforms"]').val(),
				"engineversion": $('[name="engineversion"]').val(),
				"cssgrade": $('[name="cssgrade"]').val()
		}
		request_data = JSON.stringify(request_data);
		$('#btnSave').text('saving...'); //change button text
    	$('#btnSave').attr('disabled',true); //set button disable
		$.ajax({
	        url : url,
	        contentType: "application/json",
	        type: dataType,
	        data: request_data,
	        success: function()
	        {
	    		$('#btnSave').text('save'); //change button text
	        	$('#btnSave').attr('disabled',false); //set button enable
	            $('#modal_form').modal('hide');
	          	table.clearPipeline().draw();

	        },
	        error: function (jqXHR, textStatus, errorThrown)
	        {
	            alert('Error get data from ajax');
	            $('#btnSave').text('save'); //change button text
	            $('#btnSave').attr('disabled',false); //set button enable
	        }
   		});

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
		            table.clearPipeline().draw();
			    },
		        error: function (jqXHR, textStatus, errorThrown)
		        {
		            alert('Error get data from ajax');
		        }
		    })
		}	
    });

});
//
// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
//
$.fn.dataTable.pipeline = function ( opts ) {
	// Configuration options
	var conf = $.extend( {
		url: '',      // script url
		data: null,   // function or object with parameters to send to the server
		              // matching how `ajax.data` works in DataTables
		method: 'GET' // Ajax HTTP method
	}, opts );


	return function ( request, drawCallback, settings ) {
			settings.jqXHR = $.ajax( {
				"type":     conf.method,
				"url":      conf.url,
				"data":     requestMapper(request),
				"dataType": "json",
				"cache":    false,
				"success":  function ( json,textStatus, requestj) {
					recordsTotal = requestj.getResponseHeader('recordsTotal');
					recordsFiltered = requestj.getResponseHeader('recordsFiltered');
					drawCallback({data: json,
						recordsTotal: recordsTotal,
						recordsFiltered: recordsFiltered,
					});
				}
			} );
	};
}

	// Register an API method that will empty the pipelined data, forcing an Ajax
	// fetch on the next draw (i.e. `table.clearPipeline().draw()`)
	$.fn.dataTable.Api.register( 'clearPipeline()', function () {
		return this.iterator( 'table', function ( settings ) {
			settings.clearCache = true;
		} );
	});
