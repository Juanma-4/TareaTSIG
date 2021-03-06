var map;
var limites;
var WGS84_google_mercator;
var WGS84;
var propiedades;
var estiloProp;
var vectorLocalizador;
var propId;
var selectedFeature;

//var calles;
//var esquinas;

//////////// style para geolocalizacion
var style = {
	    fillColor: '#000',
	    fillOpacity: 0,
	    strokeWidth: 0
};

function init() {
	   
		 WGS84 = new OpenLayers.Projection(miEPSG);
		 WGS84_google_mercator = new OpenLayers.Projection(gEPSG);
		
		var opciones = {
			controls : [ new OpenLayers.Control.Navigation(),
				new OpenLayers.Control.PanZoom(),
				new OpenLayers.Control.LayerSwitcher(),
				new OpenLayers.Control.MousePosition({
					div : document.getElementById("coordenadas")
				}) ],
			projection: WGS84_google_mercator, // se agrega solo igual, se puede omitir.
			displayProjection: WGS84 
		};
	
	
		/* "Map Constructor" : Crea un mapa en el bloque con id "map"  */		
		 map = new OpenLayers.Map('map', opciones);	
			
		 var google_maps = new OpenLayers.Layer.Google(
	 	            "Google Streets", // the default
	 	            {numZoomLevels: 20, 'sphericalMercator': true }
	 	            );
	 			
//	 			new OpenLayers.Layer.Google("Google Maps", {
//	 			numZoomLevels : 20
//	 		});
		
		var gphy = new OpenLayers.Layer.Google("Google Physical", {
		type : google.maps.MapTypeId.TERRAIN
		});						

		
		 estiloProp = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					symbolizer : {
						"Point" : {
							pointRadius : 20,
							externalGraphic : "resources/defecto/img/localizacion.png",
							graphicOpacity : 1,
							graphicWidth : 50,
							graphicHeight : 36
	
						}
					}
				}) ]
			})
		});

		 var filter = new OpenLayers.Filter.Logical({
		        type: OpenLayers.Filter.Logical.OR,
		        filters: [
		        new OpenLayers.Filter.Comparison({
		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
		            property: "tipoestado",
		            value: "Publica"
		        }),
		        new OpenLayers.Filter.Comparison({
		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
		            property: "tipoestado",
		            value: "Reservada"
		        })
		        ]
		    });
		 filterStrategy = new OpenLayers.Strategy.Filter({filter: filter});

	    /////////////// GEOLOCALIZACION !!! //////////////////
	    vectorLocalizador = new OpenLayers.Layer.Vector('Localizado',{
	    	 displayInLayerSwitcher : false,
	    });
	    
	    var geolocate = new OpenLayers.Control.Geolocate({
	        bind: false,
	        geolocationOptions: {
	            enableHighAccuracy: false,
	            maximumAge: 0,
	            timeout: 7000
	        }
	    });
	    
	    geolocate.watch = true;
        var  firstGeolocation = true;
        geolocate.activate();
        
	    map.addControl(geolocate);
	    
	    geolocate.events.register("locationupdated",geolocate,function(e) {
	    	vectorLocalizador.removeAllFeatures();
	    	
	        var circle = new OpenLayers.Feature.Vector(
	            OpenLayers.Geometry.Polygon.createRegularPolygon(
	                new OpenLayers.Geometry.Point(e.point.x, e.point.y),
	                e.position.coords.accuracy/2,
	                40,
	                0
	            ),
	            {},
	            style
	        );
	        
	        vectorLocalizador.addFeatures([
	            new OpenLayers.Feature.Vector(
	                e.point,
	                {},
	                {  	externalGraphic : "resources/defecto/img/ubicacion.png",
						graphicWidth : 33,
						graphicHeight : 33
					}
	            ),
	            circle
	        ]);
	    });
	    
	    geolocate.events.register("locationfailed",this,function() {
		    OpenLayers.Console.log('Location detection failed');
		});
	    /////////////////////////Fin GeoLocalizacion //////////////////////////////
	    
	    // Para la escala
	    var scaleline = new OpenLayers.Control.ScaleLine();
	    map.addControl(scaleline);
	    
		/* "Layer Constructor" : Pide capa de porpiedades via WFS  */
		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
			strategies : [ new OpenLayers.Strategy.Fixed(),filterStrategy], //,filterStrategy BBOX Fixed()
			styleMap: estiloProp,
			protocol :  new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
			})
			
		});	
 	    
		propId = propiedades.id;

		map.addLayers([ google_maps, gphy,propiedades,vectorLocalizador]);
    	//map.zoomToExtent(limites);
	
		 /*	SelectControl, para popups */
		selectControl = new OpenLayers.Control.SelectFeature([propiedades],
				    {
				        onSelect: onPopupFeatureSelect,
				        onUnselect: onPopupFeatureUnselect,
				    }
				    );
		
	    map.addControl(selectControl);
	    selectControl.activate();
		
		/// PARA CENTRAR EN MONTEVIDEO
		map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
 	            new OpenLayers.Projection(miEPSG),
 	            map.getProjectionObject()
 	            ), 12);

		
//		traerCalles();
//		traerEsquinas();
		
};					

//function cargarCalles(xhr,status,args){
//	calles = JSON.parse(args.Calles);	
//}
//
//function cargarEsquinas(xhr,status,args){
//	esquinas = JSON.parse(args.Esquinas);
//}

function verificarTerreno(){		
	
	if(document.getElementById('filtro-centros:tipoPropiedad').value == 'Terreno'){
		 document.getElementById('filtro-centros:cantDormitorio').disabled = true;
		 document.getElementById('filtro-centros:cantBanio').disabled = true;
		 document.getElementById('filtro-centros:parrillero').disabled = true;
		 document.getElementById('filtro-centros:garage').disabled = true;
		 
		 document.getElementById('filtro-centros:cantDormitorio').value= "";
		 document.getElementById('filtro-centros:cantBanio').value = "";
		 document.getElementById('filtro-centros:parrillero').value = false;
		 document.getElementById('filtro-centros:garage').value = false;
		 
		 
	}else{
		 document.getElementById('filtro-centros:cantDormitorio').disabled = false;
		 document.getElementById('filtro-centros:cantBanio').disabled = false;
		 document.getElementById('filtro-centros:parrillero').disabled = false;
		 document.getElementById('filtro-centros:garage').disabled = false;
		 
		 document.getElementById('filtro-centros:cantDormitorio').value= "1";
		 document.getElementById('filtro-centros:cantBanio').value = "1";
	}
}

//Se envian los datos para filtrar las propiedades.
function buscarPropiedades(){
	var calleDestino = document.getElementById('filtro-centros:calleDestino').value;
	var esquinaDestino =  document.getElementById('filtro-centros:esquinaDestino').value;
	
	if((calleDestino != "")&&(esquinaDestino == "")){	
		$.growl.error({ message: "Debe ingresar una esquina de destino" });


	}else if((esquinaDestino != "")&&(calleDestino == "")){
				
		$.growl.error({ message: "Debe ingresar una calle de destino" });

	}else{
		
		var tipopropiedad = document.getElementById('filtro-centros:tipoPropiedad').value;
		var tipotransaccion =  document.getElementById('filtro-centros:tipoTransaccion').value;
		var minimo = parseInt(document.getElementById('filtro-centros:minimo').value);
		var maximo = parseInt(document.getElementById('filtro-centros:maximo').value);
		var cantbanio = parseInt(document.getElementById('filtro-centros:cantBanio').value);
		var cantdorm = parseInt(document.getElementById('filtro-centros:cantDormitorio').value);
		var metroscuadrados = parseFloat(document.getElementById('filtro-centros:metrosCuadrados').value);
		var barrio =  document.getElementById('filtro-centros:barrio').value;
		var parrillero = document.getElementById('filtro-centros:parrillero').checked;
		var garage = document.getElementById('filtro-centros:garage').checked;
		
		
		var distanciaMar = parseInt($("#MarSliderVal").text());
	    var distanciaParada = parseInt($("#BusSliderVal").text());
	    var distanciaPInteres = parseInt($("#PInteresSliderVal").text());
		
	    
		remoteListar([{name:'tipopropiedad', value:tipopropiedad},{name:'tipotransaccion', value:tipotransaccion},
		              {name:'minimo', value:minimo},{name:'maximo', value:maximo},
		              {name:'cantbanio', value:cantbanio},{name:'cantdorm', value:cantdorm},
		              {name:'metroscuadrados', value:metroscuadrados},{name:'barrio', value:barrio},
		              {name:'parrillero', value:parrillero},{name:'garage', value:garage},{name:'distanciaMar', value:distanciaMar},
		              {name:'distanciaParada', value:distanciaParada},{name:'distanciaPInteres', value:distanciaPInteres},
		              {name:'calleDestino', value:calleDestino},{name:'esquinaDestino', value:esquinaDestino}]);
		
	}
}
//Funcion onstart
function load(){
	$body = $("body");
	$body.addClass("loading");   	  
}
// Funcion oncomplete
function handleConfirm(xhr,status,args)
{
  var propiedades = map.getLayer(propId);
  
  // Manipulo el json del callback
  var propiedadesJSON = JSON.parse(args.PropiedaesFiltradas);
 
	  if(propiedadesJSON.length != 0){
		  var propArr = [];
		  for(var i=0; i<propiedadesJSON.length;i++){
			  
			  var prop = propiedadesJSON[i];
			  
			  var propiedadFiltrada = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(prop.latitud, prop.longitud));
			  propiedadFiltrada.data.calle = prop.calle;
			  propiedadFiltrada.data.cantbanio = prop.cantBanio;
			  propiedadFiltrada.data.cantdorm = prop.cantDorm;
			  propiedadFiltrada.data.fid = prop.fid;
			  propiedadFiltrada.data.garage = prop.garage;
			  propiedadFiltrada.data.metroscuadrados = prop.metrosCuadrados;
			  propiedadFiltrada.data.numeropuerta = prop.numeroPuerta;
			  propiedadFiltrada.data.parrillero = prop.parrillero;
			  propiedadFiltrada.data.precio = prop.precio;
			  propiedadFiltrada.data.tipoestado = prop.tipoEstado;
			  propiedadFiltrada.data.tipopropiedad = prop.tipoPropiedad;
			  propiedadFiltrada.data.tipotransaccion = prop.tipotransaccion;
			  propiedadFiltrada.data.usuario = prop.usuario;
			  propiedadFiltrada.data.imagen = prop.imagen;
			  
		      propiedadFiltrada.attributes.calle = prop.calle;
		      propiedadFiltrada.attributes.cantbanio = prop.cantBanio;
			  propiedadFiltrada.attributes.cantdorm = prop.cantDorm;
			  propiedadFiltrada.attributes.fid = prop.fid;
			  propiedadFiltrada.attributes.garage = prop.garage;
			  propiedadFiltrada.attributes.metroscuadrados = prop.metrosCuadrados;
			  propiedadFiltrada.attributes.numeropuerta = prop.numeroPuerta;
			  propiedadFiltrada.attributes.parrillero = prop.parrillero;
			  propiedadFiltrada.attributes.precio = prop.precio;
			  propiedadFiltrada.attributes.tipoestado = prop.tipoEstado;
			  propiedadFiltrada.attributes.tipopropiedad = prop.tipoPropiedad;
			  propiedadFiltrada.attributes.tipotransaccion = prop.tipotransaccion;
			  propiedadFiltrada.attributes.usuario = prop.usuario;
			  propiedadFiltrada.attributes.imagen = prop.imagen;
			  
		      propiedadFiltrada.renderIntent = "default";
		      
		      propArr.push(propiedadFiltrada);
			 
		 }
		  
			  setTimeout(function finalizarBusqueda(){
				  		  //Borro las propiedades que se ven
						  propiedades.removeAllFeatures();
						  //Agrego las nuevas si es que hay.
						  for(var j=0; j<propArr.length;j++){
							  propiedades.addFeatures([propArr[j]]);
						  }
						  $body = $("body");
						  $body.removeClass("loading");
		  			}, 0) //500) // Duerme por medio segundo y luego ejecuta la función.
		 
	  		
	}else{	
		propiedades.removeAllFeatures();
		$.growl.warning({ message: "No se encontraron propiedades con las características que usted desea" });
		
		$body = $("body");
	    $body.removeClass("loading");
	}
  		
}

//////////////////// Para Pop Ups //////////////////////////


function onPopupClose(evt) {
    selectControl.unselect(selectedFeature);
}

function onPopupFeatureSelect(feature) {
	    selectedFeature = feature;
	    
	    var popUpHtml = 
	           	'<div style="text-align:center;" class="font-style">'
	        			+ feature.data.calle + '  ' + feature.data.numeropuerta + 
	        	'</div>'+
	
        '</br>'; 
        
if(feature.data.tipopropiedad != "Terreno"){
        	
        	popUpHtml +=
  '<div class="my-container">'+
 
        '<font-style2>Propiedad: </font-style2> <font-style3>	'+ feature.data.tipopropiedad +
        '</font-style3> </br>'+
        '<font-style2>Para: </font-style2> <font-style3>		'+ feature.data.tipotransaccion +
        '</font-style3> </br>'+
        '<font-style2>Precio: </font-style2> <font-style2>$ </font-style2><font-style3>		'+ feature.data.precio +
        '</font-style3> </br>'+
        '<font-style2>Piso: </font-style2> <font-style3>		'+ feature.data.piso +
        '</font-style3> </br>'+
        '<font-style2>Dormitorios: </font-style2> <font-style3>	'+feature.data.cantdorm +
        '</font-style3> </br>'+
        '<font-style2>Baños: </font-style2> <font-style3>		'+feature.data.cantbanio +
        '</font-style3> </br>'+
        '<font-style2>Metros Cuadrados: </font-style2> <font-style3>	'+feature.data.metroscuadrados +
        '</font-style3> </br>';
    if(feature.data.parrillero == "true"){
    	popUpHtml += '<font-style2>Parrillero: </font-style2> <font-style3>	Si</font-style3>' +
			        '</br>';
			        
    }else{
    	popUpHtml += '<font-style2>Parrillero: </font-style2> <font-style3>	No</font-style3>' +
        '</br>';
    }
    if(feature.data.garage == "true"){
    	popUpHtml += '<font-style2>Garage: </font-style2> <font-style3>		Si</font-style3> '+
					        '</br>'+ '</br>'+
					         '<div class="div_radius" >'+

//					            '<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu_Shk6FE0rfzy4mOsdZPDj3SdrqJn6nLaSeKLhWSZ3G557S7JyV50_XB0" "width="380" height="150" class="div_radius">'+
					            	' <img src="'+feature.data.imagen+'" "width="280" height="130" class="div_radius">' +

					        

					            '</div>'+'</br>' +
				        '</div>' +
				        '<div style="text-align:center">'+
				       	'<a class="linkMB" onclick="enviarDatos()" href="javascript:void(0);">Ver Información</a>'+
				       '</div>';
			        
    }else{
    	popUpHtml += '<font-style2>Garage: </font-style2> <font-style3>		No</font-style3> '+
				         '</br>'+ '</br>'+
				         '<div class="div_radius" >'+

//				            '<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu_Shk6FE0rfzy4mOsdZPDj3SdrqJn6nLaSeKLhWSZ3G557S7JyV50_XB0" "width="380" height="150" class="div_radius">'+
				            	' <img src="'+feature.data.imagen+'" "width="280" height="130" class="div_radius">' +

				        

				            '</div>'+'</br>' +
			         '</div>' +
			         '<div style="text-align:center">'+
		            	'<a class="linkMB" onclick="enviarDatos()" href="javascript:void(0);">Ver Información</a>'+
		            '</div>';
    
    	 
    }	
}else{
	
	popUpHtml +=
	
		  '<div class="my-container">'+
		  
	        '<font-style2>Propiedad: </font-style2> <font-style3>   	'+ feature.data.tipopropiedad +
	        '</font-style3> </br>'+
	        '<font-style2>Para: </font-style2> <font-style3>		'+ feature.data.tipotransaccion +
	        '</font-style3> </br>'+
	        '<font-style2>Precio: </font-style2> <font-style2>$ </font-style2><font-style3>		'+ feature.data.precio +
	        '</font-style3> </br>'+
    
	        '<font-style2>Metros Cuadrados: </font-style2> <font-style3> '+feature.data.metroscuadrados +
	        '</font-style3> </br>' +
	        '</br>'+ 
	        '<div class="div_radius" >'+

//	        '<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu_Shk6FE0rfzy4mOsdZPDj3SdrqJn6nLaSeKLhWSZ3G557S7JyV50_XB0" "width="380" height="150" class="div_radius">'+
	      ' <img src="'+feature.data.imagen+'" "width="280" height="130" class="div_radius">' +

	       

	        '</div>'+'</br>' +
  '</div>' +
  '<div style="text-align:center">'+
  '<a class="linkMB" onclick="enviarDatos()" href="javascript:void(0);">Ver Información</a>'+
   '</div>';
	
	
}    
	    popup = new OpenLayers.Popup.FramedCloud(

	    		"",
	        feature.geometry.getBounds().getCenterLonLat(),
	        null,//new OpenLayers.Size(150,200), 
	        popUpHtml,
	        null, 
	        true, 
	        onPopupClose
	);
    popup.panMapIfOutOfView = true;
    popup.autoSize = true;
    feature.popup = popup;
    map.addPopup(popup);
}

function onPopupFeatureUnselect(feature) {
    map.removePopup(feature.popup);
    feature.popup.destroy();
    feature.popup = null;
}	

function enviarDatos(){
	  $body = $("body");
	  $body.addClass("loading-puntosInteres"); 	
	
	var calle = selectedFeature.data.calle;
	var precio = selectedFeature.data.precio;
	var numPuerta = selectedFeature.data.numeropuerta; 
	var tipoPropiedad = selectedFeature.data.tipopropiedad; 
	var tipotransaccion = selectedFeature.data.tipotransaccion; 
	var imagen = selectedFeature.data.imagen; 
	var tipoEstado = selectedFeature.data.tipoestado; 
	var piso = selectedFeature.data.piso; 
	var cantDorm = selectedFeature.data.cantdorm; 
	var cantBanio = selectedFeature.data.cantbanio; 
	var metrosCuadrados = selectedFeature.data.metroscuadrados; 
	var parrillero = selectedFeature.data.parrillero;
	var garage = selectedFeature.data.garage;
	var usuario = selectedFeature.data.usuario;
	var fid = selectedFeature.data.fid;	
	remoteVerInfoProp([{name:'calle', value:calle},{name:'precio', value:precio},{name:'numPuerta', value:numPuerta},{name:'tipoPropiedad', value:tipoPropiedad},
					   {name:'tipotransaccion', value:tipotransaccion},{name:'imagen', value:imagen},{name:'tipoEstado', value:tipoEstado},
					   {name:'piso', value:piso},{name:'cantDorm', value:cantDorm},{name:'cantBanio', value:cantBanio},{name:'metrosCuadrados', value:metrosCuadrados},
					   {name:'parrillero', value:parrillero},{name:'garage', value:garage},{name:'usuario', value:usuario},{name:'fid', value:fid}]);

}

$(function(){
    
	   $("#sliderMar").slider();
	   $("#sliderMar").on("slide", function(slideEvt) {
	   	$("#MarSliderVal").text(slideEvt.value);
	   });
	   
});


$(function(){
	      
	   $("#sliderBus").slider();
	   $("#sliderBus").on("slide", function(slideEvt) {
	   	$("#BusSliderVal").text(slideEvt.value);
	   });
	   
});


$(function(){
	      
	   $("#sliderPInteres").slider();
	   $("#sliderPInteres").on("slide", function(slideEvt) {
	   	$("#PInteresSliderVal").text(slideEvt.value);
	   });
	   
});

//
//$(function(){
//	
//	$(document.getElementById('tags')).autocomplete({
//	    source: calles
//	  });
//	
//	$(document.getElementById('filtro-centros:calleDestino')).autocomplete({
//	    source: calles
//	  });
//});
