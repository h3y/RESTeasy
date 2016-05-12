 $(document).ready(function () {

var table = $("#full-features").DataTable({
			"ajax": {
            "url": '../../restful/api/get_features',
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
						{"data": "cssgrade", "title":"	CSS grade"}
			  ],
		"ordering": true,
		   "paging": true,
	      "lengthChange": false,
	      "searching": true,
	      "info": true,
	      "autoWidth": false
		}); 

});
