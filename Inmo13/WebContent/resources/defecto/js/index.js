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
				featureType : "propiedadGeom",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});				    
								  
		
		var estiloBuses = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					symbolizer : {
						"Point" : {
							pointRadius : 20,
							externalGraphic : "resources/defecto/img/buses.png",
							graphicOpacity : 1,
							graphicWidth : 10,
							graphicHeight : 10
	
						}
					}
				}) ]
			})
		});
		
		
		var paradas = new OpenLayers.Layer.Vector("Paradas de Omnibus", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloBuses,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "v_uptu_paradas",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});		
		
		
		
		var estiloPlazaDeportes = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					symbolizer : {
						"Point" : {
							pointRadius : 20,
							externalGraphic : "resources/defecto/img/pelota.png",
							graphicOpacity : 1,
							graphicWidth : 15,
							graphicHeight : 15
	
						}
					}
				}) ]
			})
		});
		
		
		
		var deportes = new OpenLayers.Layer.Vector("Plazas de Deportes", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloPlazaDeportes,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "deportes",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
		//Para que no traiga esa capa cuando cargue los overlay (capa encima de la base) 
		paradas.setVisibility(false);
		deportes.setVisibility(false);
		map.addLayers([ google_maps, gphy,propiedades,paradas,deportes]);
		
		
		map.zoomToExtent(limites);		
		
	
		
	  
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