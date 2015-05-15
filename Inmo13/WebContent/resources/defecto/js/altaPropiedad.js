var apreto = false;
var propiedad;
var saveStrategy;
window.onload = function() {
	   
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
		
		var gphy = new OpenLayers.Layer.Google("Google Physical", {
		type : google.maps.MapTypeId.TERRAIN
		});		
				
		
		/* Estrategia que se va a utilizar para guardar la propiedad. */		
		saveStrategy = new OpenLayers.Strategy.Save();	
		saveStrategy.events.register("success", '', exito);
		saveStrategy.events.register("failure", '', fallo);	    
						
		/* "Layer Constructor" : Pide capa de porpiedades via WFS-T  */
		var propiedades = new OpenLayers.Layer.Vector("Propiedad", {
			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
			  	
			}),
		});		
		
		
		
		/*	PARA EL FUTURO... a propiedades se le saca saveStrategy
		var nuevaPropiedad = new OpenLayers.Layer.Vector("Nueva Propiedad", {
			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
			  	
			}),
		});	
		*/
		
		//propiedades.setVisibility(false);					    
		map.addLayers([ google_maps, gphy, propiedades]);
		map.zoomToExtent(limites);		
		
	
		map.events.register("click", map, function(e) {	
			if(apreto){
				propiedades.removeFeatures(propiedad);
			}
		    var posicion = map.getLonLatFromPixel(e.xy);	
							
		    propiedad = new OpenLayers.Feature.Vector( new OpenLayers.Geometry.Point(
					        (posicion.lon), (posicion.lat) ));			
								
			 propiedades.addFeatures([propiedad]);	
			 document.getElementById('formPropiedad:fid').value = punto.id;
		   // alert("Punto id:"+ punto.id + "Punto fid: " + punto.fid + "Puntos almacenados: " + propiedades.features.length);
		   // alert("input valor :"+ document.getElementById('formPropiedad:fid').value);
		    
		    apreto = true;
		});
	  
	  	/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
            	    WGS84,  map.getProjectionObject()), miZoom + 3);
		
							
};

	
 function exito(){
 	alert("Se guardo con éxito");
 }
 
 function fallo(){
 	alert("Error al guardar"); 
 }
 
 function darAltaGeom(){	 
	 // Preparo los datos	 
	 propiedad.attributes.calle =  document.getElementById('formPropiedad:calle').value;
	 propiedad.attributes.cantbanio = parseInt(document.getElementById('formPropiedad:cantBanio').value);
	 propiedad.attributes.cantdorm = parseInt(document.getElementById('formPropiedad:cantDormitorio').value);
	 propiedad.attributes.garage = document.getElementById('formPropiedad:garage').checked;
	 propiedad.attributes.metroscuadrados = parseFloat(document.getElementById('formPropiedad:metrosCuadrados').value);
	 propiedad.attributes.numeroPuerta = parseInt(document.getElementById('formPropiedad:numeroPuerta').value);
	 propiedad.attributes.parrillero = document.getElementById('formPropiedad:parrillero').checked;
	 propiedad.attributes.precio = parseFloat(document.getElementById('formPropiedad:precio').value);
	 propiedad.attributes.tipoestado = document.getElementById('formPropiedad:tipoEstado').value;
	 propiedad.attributes.tipopropiedad = document.getElementById('formPropiedad:tipoPropiedad').value;
	 propiedad.attributes.tipotransaccion =  document.getElementById('formPropiedad:tipoTransaccion').value;
	 propiedad.attributes.usuario =  "admin@gmail.com"//document.getElementById('formPropiedad:numeroPuerta').value;
	 propiedad.attributes.fid =  propiedad.id;//document.getElementById('formPropiedad:fid').value;
	 
	 propiedad.state = OpenLayers.State.INSERT;
	 saveStrategy.save();	
	// alert(punto.attributes);
	// alert("Punto id:"+ punto.id + "Punto fid: " + punto.fid);
	// document.getElementById('formPropiedad:fid').value = punto.id;
 }