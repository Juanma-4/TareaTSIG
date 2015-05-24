var apreto = false;
var punto;
var gid;
var saveStrategy;
var map;
var opciones;
var limites;
var WGS84_google_mercator;
var WGS84;
var propiedades
window.onload = function() {
	   
		 WGS84 = new OpenLayers.Projection(miEPSG);
		 WGS84_google_mercator = new OpenLayers.Projection(gEPSG);		
		
		limites = new OpenLayers.Bounds(
			366582.290141166, 6127927.10038269,
			858252.0151745,6671738.21181725
		).transform(WGS84, WGS84_google_mercator);
		
		opciones = {
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
		 map = new OpenLayers.Map('map', opciones);	
		
		var google_maps = new OpenLayers.Layer.Google("Google Maps", {
			numZoomLevels : 20
		});
		
		var gphy = new OpenLayers.Layer.Google("Google Physical", {
		type : google.maps.MapTypeId.TERRAIN
		});						
		
		var styleMap = new OpenLayers.StyleMap({
			  pointRadius: 5,
			  strokeColor: '#ff0000',
			  fillColor: '#ff0000',
			  fillOpacity: 0.6
			});
		
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
		
		
		
		/* "Layer Constructor" : Pide capa de porpiedades via WFS  */
		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
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
								  
				
		map.addLayers([ google_maps, gphy, propiedades]);
		
		
		map.zoomToExtent(limites);		
	
	  
	  	/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
            	    WGS84,  map.getProjectionObject()), miZoom + 3);
		
};					

//////////////////////funcion que hace la busqueda en si misma 

function hacerBusqueda(){	
	
	
	
	 
	 
	 var tipopropiedad = document.getElementById('filtro-centros:tipoPropiedad').value;
	 
	 var tipotransaccion =  document.getElementById('filtro-centros:tipoTransaccion').value;
	 var tipomoneda =  document.getElementById('filtro-centros:moneda').value;
	 var minimo = parseInt(document.getElementById('filtro-centros:minimo').value);
	 var maximo = parseInt(document.getElementById('filtro-centros:maximo').value);
     var cantbanio = parseInt(document.getElementById('filtro-centros:cantBanio').value);
 	 var cantdorm = parseInt(document.getElementById('filtro-centros:cantDormitorio').value);
     var metroscuadrados = parseFloat(document.getElementById('filtro-centros:metrosCuadrados').value);
 	 var barrio =  document.getElementById('filtro-centros:barrio').value;
     var parrillero = document.getElementById('filtro-centros:parrillero').checked;
     var garage = document.getElementById('filtro-centros:garage').checked; 
	 
   //  var distanciaMar = parseInt(document.getElementById('formMar:sliderMar').getAttribute('data-slider-value').value); no funciona !!

	 
	 var filtro = new OpenLayers.Filter.Logical({
		    type: OpenLayers.Filter.Logical.AND,
		    filters: [
	 		        new OpenLayers.Filter.Comparison({
	 		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
	 		            property: "tipopropiedad",
	 		            value: tipopropiedad
	 		        })
		        	]
		});
	 
		
	// var search_name = document.getElementById("search_text").value;
	    var features = propiedades.features;
	    propiedades.removeAllFeatures();
	    propiedades.destroyFeatures();
	 //   propiedades.filter=filtro;
	    propiedades.refresh({force: true});
	    
	 /*  
	    for(var i=0; i< features.length; i++) {
	    	
	    	if(features[i].filters==filtro){
	    	   propiedades.addFeatures([features[i]]);
	    	}
	    	
	    	
	    }*/
	    	
	//    propiedades.redraw();
	    	
	     //features[i].attributes.name. you have the attribute field "name"
/*
	      if(features[i].attributes.filters = filters) {
	        propiedades.addFeatures([features[i]])
	      }*/
	    

	    		
	 
	 
	 
	
	/*	
	 propiedades.filters = filtro; 
	 propiedades.refresh({force: true});
	 
	// map.addLayers([propiedades]);	    
		
	/*	
	 
	
	 var clone = map.getLayersByName( 'propiedades' ).features.clone();
	 propiedades.addFeatures(clone);
		
		
		
       

	
	 /*
	
		
		*/
 	
}

		







$(function() { 
	var a = "holaaaaaaaaassssss"; 
	$("#filtro-centros\\:botonBusqueda").click(function(){ 
		var a = "hola";
		hacerBusqueda();
		var b = "holabbbbb";
		var c = "holabbbbbccc";
	});
});





 function exito(){
 	alert("Se guardo con éxito");
 }
 
 function fallo(){
 	alert("Error al guardar"); 
 }
 
