var apreto = false;
var punto;
var gid;
var saveStrategy;
var map;
var opciones;
var limites;
var WGS84_google_mercator;
var WGS84;
var propiedadesFiltradas;
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
		var propiedades = new OpenLayers.Layer.Vector("Propiedades", {
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
		propiedadesFiltradas = new OpenLayers.Layer.Vector("PropiedadesFiltradas", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloProp,
			filter: filtro,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});				    
		
		
	
	
		
		map.addLayers([propiedadesFiltradas]);
		
       map.zoomToExtent(limites);		
		
   	
		
 	  
	  	/// PARA CENTRAR EN MONTEVIDEO
   	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
           	    WGS84,  map.getProjectionObject()), miZoom + 3);

	
	 /*
	propiedad.attributes.tipotransaccion =  document.getElementById('filtro-centros:tipoTransaccion').value;
	propiedad.attributes.tipomoneda =  document.getElementById('formPropiedad:moneda').value;
	propiedad.attributes.minimo = parseInt(document.getElementById('formPropiedad:minimo').value);
	propiedad.attributes.maximo = parseInt(document.getElementById('formPropiedad:maximo').value);
    propiedad.attributes.cantbanio = parseInt(document.getElementById('formPropiedad:cantBanio').value);
 	propiedad.attributes.cantdorm = parseInt(document.getElementById('formPropiedad:cantDormitorio').value);
 	propiedad.attributes.metroscuadrados = parseFloat(document.getElementById('formPropiedad:metrosCuadrados').value);
 	propiedad.attributes.barrio =  document.getElementById('formPropiedad:barrio').value;
 	propiedad.attributes.parrillero = document.getElementById('formPropiedad:parrillero').checked;
 	propiedad.attributes.garage = document.getElementById('formPropiedad:garage').checked; */
		
		
 	
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
 
