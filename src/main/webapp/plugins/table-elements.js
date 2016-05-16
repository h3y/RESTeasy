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
			pages: 5 // number of pages to cache
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


///////////////////////////

//
// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
//
$.fn.dataTable.pipeline = function ( opts ) {
	// Configuration options
	var conf = $.extend( {
		pages: 5,     // number of pages to cache
		url: '',      // script url
		data: null,   // function or object with parameters to send to the server
		              // matching how `ajax.data` works in DataTables
		method: 'GET' // Ajax HTTP method
	}, opts );

	// Private variables for storing the cache
	var cacheLower = -1;
	var cacheUpper = null;
	var cacheLastRequest = null;
	var cacheLastJson = null;

	return function ( request, drawCallback, settings ) {
		var ajax          = false;
		var requestStart  = request.start;
		var drawStart     = request.start;
		var requestLength = request.length;
		var requestEnd    = requestStart + requestLength;
		
		if ( settings.clearCache ) {
			// API requested that the cache be cleared
			ajax = true;
			settings.clearCache = false;
		}
		else if ( cacheLower < 0 || requestStart < cacheLower || requestEnd > cacheUpper ) {
			// outside cached data - need to make a request
			ajax = true;
		}
		else if ( JSON.stringify( request.order )   !== JSON.stringify( cacheLastRequest.order ) ||
		          JSON.stringify( request.columns ) !== JSON.stringify( cacheLastRequest.columns ) ||
		          JSON.stringify( request.search )  !== JSON.stringify( cacheLastRequest.search )
		) {
			// properties changed (ordering, columns, searching)
			ajax = true;
		}
		
		// Store the request for checking next time around
		cacheLastRequest = $.extend( true, {}, request );

		if ( ajax ) {
			// Need data from the server
			if ( requestStart < cacheLower ) {
				requestStart = requestStart - (requestLength*(conf.pages-1));

				if ( requestStart < 0 ) {
					requestStart = 0;
				}
			}
			
			cacheLower = requestStart;
			cacheUpper = requestStart + (requestLength * conf.pages);

			request.start = requestStart;
			request.length = requestLength*conf.pages;

			// Provide the same `data` options as DataTables.
			if ( $.isFunction ( conf.data ) ) {
				// As a function it is executed with the data object as an arg
				// for manipulation. If an object is returned, it is used as the
				// data object to submit
				var d = conf.data( request );
				if ( d ) {
					$.extend( request, d );
				}
			}
			else if ( $.isPlainObject( conf.data ) ) {
				// As an object, the data given extends the default
				$.extend( request, conf.data );
			}
			settings.jqXHR = $.ajax( {
				"type":     conf.method,
				"url":      conf.url,
				"data":     requestMapper(request),
				"dataType": "json",
				"cache":    false,
				"success":  function ( json,textStatus, request) {
					cacheLastJson = $.extend(true, {}, json);
					if ( cacheLower != drawStart ) {
						json.splice( 0, drawStart-cacheLower );
					}
					json.splice( requestLength, json.length );
					
					drawCallback({data: json,
						recordsTotal: request.getResponseHeader('recordsTotal'),
						recordsFiltered: request.getResponseHeader('recordsFiltered'),}
					);

				}
			} );
		}
		else {
			json = $.extend( true, {}, cacheLastJson );
; // Update the echo for each response
			json.splice( 0, requestStart-cacheLower );
			json.splice( requestLength, json.length );
			drawCallback({draw: request.draw,
						  data: json,
						  recordsTotal: request.getResponseHeader('recordsTotal'),
						  recordsFiltered: request.getResponseHeader('recordsFiltered'),
			});
		}
	}
};

// Register an API method that will empty the pipelined data, forcing an Ajax
// fetch on the next draw (i.e. `table.clearPipeline().draw()`)
$.fn.dataTable.Api.register( 'clearPipeline()', function () {
	return this.iterator( 'table', function ( settings ) {
		settings.clearCache = true;
	} );
} );
