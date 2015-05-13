var apreto = false;
var punto;
var gid;
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
				
		
		/* Estrategia que se va a utilizar para guardar el punto. */		
		saveStrategy = new OpenLayers.Strategy.Save();	
		saveStrategy.events.register("success", '', exito);
		saveStrategy.events.register("failure", '', fallo);	    
						
		/* "Layer Constructor" : Pide capa de porpiedades via WFS  */
		var wfs = new OpenLayers.Layer.Vector("Propiedad", {
			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedadGeom",
				featureNS : urlGeoServer,
				geometryName : "geom",
			  	
			}),
		});				    
								    
									    
		map.addLayers([ google_maps, gphy, wfs]);
		map.zoomToExtent(limites);		
		
	
		map.events.register("click", map, function(e) {	
			if(apreto){
				wfs.removeFeatures(punto);
			}
		    var posicion = map.getLonLatFromPixel(e.xy);	
							
			 punto = new OpenLayers.Feature.Vector( new OpenLayers.Geometry.Point(
					        (posicion.lon), (posicion.lat) ));			
								
		    wfs.addFeatures([punto]);	
		    gid = wfs.features.length; //Es el gid que se guardará en la tabla de info de las propiedades.
		    
		  //  alert(gid);
		    document.getElementById('formPropiedad:gid').value = gid;
		   // alert("input valor :"+ document.getElementById('formPropiedad:gid').value);
			//alert(wfs.features); 
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
	 punto.state = OpenLayers.State.INSERT;
	 saveStrategy.save();	
 }