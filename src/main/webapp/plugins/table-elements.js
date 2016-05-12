$(document).ready(function() {

//
// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
//
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
				"data":     request,
				"dataType": "json",
				"cache":    false,
				"success":  function ( json ) {
					cacheLastJson = $.extend(true, {}, json);

					if ( cacheLower != drawStart ) {
						json.data.splice( 0, drawStart-cacheLower );
					}
					json.data.splice( requestLength, json.data.length );
					
					drawCallback( json );
				}
			} );
		}
		else {
			json = $.extend( true, {}, cacheLastJson );
			json.draw = request.draw; // Update the echo for each response
			json.data.splice( 0, requestStart-cacheLower );
			json.data.splice( requestLength, json.data.length );

			drawCallback(json);
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


//
// DataTables initialisation
//
$(document).ready(function() {
	$('#full-features').DataTable( {
		"processing": true,
		"serverSide": true,
		"ajax": $.fn.dataTable.pipeline( {
			url: '../../restful/api/features',
			pages: 5 // number of pages to cache
		} ),
        "columns": [
						{"data": "id", 'id':'id', "title":'ID'},
						{"data": "renderingengine", "title":"Rendering Engine"},
						{"data": "browser", "title":"Browser"},
						{"data": "platforms", "title":"Platform(s)"},
						{"data": "engineversion", "title":"Engine version"},
						{"data": "cssgrade", "title":"	CSS grade"},
						{"targets": -1,"data": null, "defaultContent":"<a id='edit-features' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp&nbsp&nbsp<a id='delete-features'class='btn btn-sm btn-danger'> <i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}
			  ]
	} );
} );



	/**/

/*
var table = $("#full-features").DataTable({
			"ajax": {
            "url": '../../restful/api/features',
            "deferRender": true,
            "dataSrc": "",
            "type": "GET"
        },
        "columns": [
						{"data": "id", 'id':'id', "title":'ID'},
						{"data": "renderingengine", "title":"Rendering Engine"},
						{"data": "browser", "title":"Browser"},
						{"data": "platforms", "title":"Platform(s)"},
						{"data": "engineversion", "title":"Engine version"},
						{"data": "cssgrade", "title":"	CSS grade"},
						{"targets": -1,"data": null, "defaultContent":"<a id='edit-features' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp&nbsp&nbsp<a id='delete-features'class='btn btn-sm btn-danger'> <i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}
			  ],
		  "ordering": true,
		  "paging": true,
	      "lengthChange": true,
	      "searching": true,
	      "info": true,
	      "autoWidth": false
		}); 

	var save_method;
	 //edit features
    $('#full-features tbody').on( 'click', '#edit-features', function () {
         var data = table.row( $(this).parents('tr') ).data();
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
    //add features
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

    //save edit-features
    $('#btnSave').click(function(){
    	var dataType;
    	console.log(save_method);
    	if(save_method =='add'){
    		$('.modal-title').text('Add Features');
    		if($('[name="engineversion"]').val()==""){
    			$('[name="engineversion"]').val(0);
    		}
	    	var url = "../../restful/api/add_features/"+$('[name="renderingengine"]').val()+"/"+$('[name="browser"]').val()+"/"+$('[name="platforms"]').val()+"/"+parseFloat($('[name="engineversion"]').val())+"/"+$('[name="cssgrade"]').val();	
    		dataType = "POST";
    	}else{
	    	$('.modal-title').text('Edit Features');
	    	if($('[name="engineversion"]').val()==""){
    			$('[name="engineversion"]').val(0);
    		}
	    	var url = "../../restful/api/update_features/"+$('[name="id"]').val()+"/"+$('[name="renderingengine"]').val()+"/"+$('[name="browser"]').val()+"/"+$('[name="platforms"]').val()+"/"+parseFloat($('[name="engineversion"]').val())+"/"+$('[name="cssgrade"]').val();
			dataType = "";
		}PUT

		$('#btnSave').text('saving...'); //change button text
    	$('#btnSave').attr('disabled',true); //set button disable
		$.ajax({
        url : url,
        type: dataType,
        success: function()
        {
    		$('#btnSave').text('save'); //change button text
        	$('#btnSave').attr('disabled',false); //set button enable
            $('#modal_form').modal('hide');
            table.ajax.reload(null,false); 

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
    $('#full-features tbody').on( 'click', '#delete-features', function () {
         var data = table.row( $(this).parents('tr') ).data();
         if (window.confirm("you really want to delete the entry from the id = "+data["id"]))
		{
			 var url = "../../restful/api/delete_features/"+data["id"];
			 $.ajax({
			    url : url,
			    type: "DELETE",
			    success: function()
			    {
		            table.ajax.reload(null,false); 
			    },
		        error: function (jqXHR, textStatus, errorThrown)
		        {
		            alert('Error get data from ajax');
		        }
		    })
		}	
    });*/
} );