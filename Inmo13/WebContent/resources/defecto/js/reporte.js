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
	
	/*	
		reglaPublica = new OpenLayers.Rule({
			filter : new OpenLayers.Filter.Comparison({
				type : OpenLayers.Filter.Comparison.LIKE,
				property : "gid",
				value : 8,
			}),
			symbolizer : {
				pointRadius : 10,
				fillColor : "green",
				fillOpacity : 1,
				strokeColor : "black"
			}
		});
		
		
		
		var estiloPropPublica = new OpenLayers.Style();
		estiloPropPublica.addRules([ reglaPublica ]);
		
		
		var mapaEstilo = new OpenLayers.StyleMap({
			"default" : estiloPropPublica,
			
		});
		
	*/	
		var estiloProp = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "id" ,
					value : 9 ,
					}),
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
		

	
		/* "Layer Constructor" : Pide capa de porpiedadesGeom via WFS  */
		var propiedadesGeom = new OpenLayers.Layer.Vector("PropiedadesGeom", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloProp,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "deportes",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
		
		
	/*	
		/* "Layer Constructor" : Pide capa de porpiedadesGeom via WFS  
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
				*/				    
			
		//Para que no traiga esa capa cuando cargue los overlay (capa encima de la base) 
	
		map.addLayers([ google_maps, gphy,propiedadesGeom]);
		
		
		map.zoomToExtent(limites);		
		
	
		
	  
	  	/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
            	    WGS84,  map.getProjectionObject()), miZoom + 3);
		
							
};

	
