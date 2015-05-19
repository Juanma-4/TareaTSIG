//////////********** VARIABLES **********//////////
var apreto = false;
var propiedad;
var saveStrategy;
var propiedades;
var nuevaPropiedad;
var growl;

//////////********** CREACIÓN DE MAPA, CAPAS Y CONTROLES **********//////////
window.onload = function() {
		growl = PF('growl');
		
 		var WGS84 = new OpenLayers.Projection(miEPSG);
 		var WGS84_google_mercator = new OpenLayers.Projection(gEPSG);		
 		
 		var limites = new OpenLayers.Bounds(
 			366582.290141166, 6127927.10038269,
 			858252.0151745,6671738.21181725
 		).transform(WGS84, WGS84_google_mercator);
 		
 		var opciones = {
 			controls : [ new OpenLayers.Control.Navigation(),
 				new OpenLayers.Control.PanZoom(),
 				new OpenLayers.Control.LayerSwitcher(),
 				new OpenLayers.Control.MousePosition({
 					div : document.getElementById("coordenadas")
 				}) ],
 			maxExtent: limites,
 			projection: WGS84_google_mercator, // se agrega solo igual, se puede omitir.
 			displayProjection: WGS84 
 		};
 	
 	
 		/* "Map Constructor" : Crea un mapa en el bloque con id "map"  */		
 		var map = new OpenLayers.Map('map', opciones);	
 		
 		var google_maps = new OpenLayers.Layer.Google("Google Maps", {
 			numZoomLevels : 20
 		});
 		
 		var google_fisico = new OpenLayers.Layer.Google("Google Fisico", {
 		type : google.maps.MapTypeId.TERRAIN
 		});		
 				
 		
 		/* Estrategia que se va a utilizar para guardar la propiedad. */		
 		saveStrategy = new OpenLayers.Strategy.Save();	
 		saveStrategy.events.register("success", '', exito);
 		saveStrategy.events.register("failure", '', fallo);	    


 		/* 	Estilo de la propiedad que se va a ingresar */
 		var estiloProp = new OpenLayers.StyleMap({
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
 		
 		/* "Layer Constructor" : Pide capa de porpiedades via WFS-T  */
 		 propiedades = new OpenLayers.Layer.Vector("Propiedad", {
 			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
 			styleMap: estiloProp,
 			protocol : new OpenLayers.Protocol.WFS({
 				version : "1.1.0",
 				url : urlWFS,
 				featureType : "propiedad",
 				featureNS : urlGeoServer,
 				geometryName : "geom",
 				srsName: gEPSG,
 			  	
 			}),
 		});	
 		 
 		
 		 nuevaPropiedad = new OpenLayers.Layer.Vector("Nueva Propiedad", {
             styleMap: estiloProp,
             geometryType : OpenLayers.Geometry.Point,
             displayInLayerSwitcher : false,
         });

 		/// para dibujar
 		dibujar = new OpenLayers.Control.DrawFeature(nuevaPropiedad,
 													 OpenLayers.Handler.Point, {
 															featureAdded : onFeatureAdded,
 	    });
 		
 		map.addControl(dibujar);		
 		dibujar.activate();
 				    
 		map.addLayers([ google_maps, google_fisico, propiedades, nuevaPropiedad]);
 		map.zoomToExtent(limites);		
 	
 	  	/// PARA CENTRAR EN MONTEVIDEO
     	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
             	    WGS84,  map.getProjectionObject()), miZoom + 3);
 		
 							
 };

//////////********** CONTROLES AL INGRESAR PROPIEDAD **********//////////
 function exito(){
	 growl.show([{summary:'Exito', detail: 'Se guardo la propiedad', severity:'Info'}]); 
 }
 
 function fallo(){
	 growl.show([{summary:'Error', detail: 'Error al ingresar la propiedad'}]); 
 }
 
 function onFeatureAdded(feature) {
 	if(apreto){
 		nuevaPropiedad.removeFeatures(propiedad);
 	} 	
 	propiedad = feature;
 	//propiedades.addFeatures([propiedad]);	//No es necesario porque el dibujar ya lo agrega
 	apreto = true;
 }
 	
  
function darAltaGeom(){	
	  
	
	 //Agrego la nueva feature a la capa de propiedades
	 propiedades.addFeatures([nuevaPropiedad.getFeatureById(propiedad.id)]);
 	 
 	 // Preparo los datos	 
 	 propiedad.attributes.calle =  document.getElementById('formPropiedad:calle').value;
 	 propiedad.attributes.cantbanio = parseInt(document.getElementById('formPropiedad:cantBanio').value);
 	 propiedad.attributes.cantdorm = parseInt(document.getElementById('formPropiedad:cantDormitorio').value);
 	 propiedad.attributes.garage = document.getElementById('formPropiedad:garage').checked;
 	 propiedad.attributes.metroscuadrados = parseFloat(document.getElementById('formPropiedad:metrosCuadrados').value);
 	 propiedad.attributes.numeropuerta = parseInt(document.getElementById('formPropiedad:numeroPuerta').value);
 	 propiedad.attributes.parrillero = document.getElementById('formPropiedad:parrillero').checked;
 	 propiedad.attributes.precio = parseFloat(document.getElementById('formPropiedad:precio').value);
 	 propiedad.attributes.tipoestado = document.getElementById('formPropiedad:tipoEstado').value;
 	 propiedad.attributes.tipopropiedad = document.getElementById('formPropiedad:tipoPropiedad').value;
 	 propiedad.attributes.tipotransaccion =  document.getElementById('formPropiedad:tipoTransaccion').value;
 	 propiedad.attributes.usuario = document.getElementById('formPropiedad:usuario').value; // "admin@gmail.com" para probar
 	 propiedad.attributes.fid =  propiedad.id;//document.getElementById('formPropiedad:fid').value;
 	 propiedad.attributes.piso =  document.getElementById('formPropiedad:piso').value;
 	 propiedad.attributes.tipomoneda =  document.getElementById('formPropiedad:moneda').value;

 	// propiedad.state = OpenLayers.State.INSERT; //No es necesario porque el dibujar ya le cambia el estado.
 	 saveStrategy.save();	
}

/////////////*************** FUNCIONES DE CONTROL ***************/////////////

$(function() {  
	$("#formPropiedad\\:botonFormPropiedad").click(function(){ 
	  if (apreto){			  
		  if(controlarInputs()){
			  darAltaGeom();
		  }else
			  return false;
	  }else{
		  growl.show([{summary:'Error', detail: 'Debe ingresar una propiedad'}]); //, severity: 'info severity'}]) warn
		  return false;
	  }
	});
	
}); 

function controlarInputs(){
	
	if(!$.trim($("#formPropiedad\\:calle").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar una calle!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:numeroPuerta").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar un numero de puerta!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:precio").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar un precio!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:piso").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar un Nº de piso!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:cantBanio").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar una cantidad de baños!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:cantDormitorio").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar una cantidad de dormitorios!'}]); 
		return false;
	}else if(!$.trim($("#formPropiedad\\:metrosCuadrados").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar una cantidad de metros cuadrados!'}]); 
		return false;
	}else{
		return true;
	}
	
}