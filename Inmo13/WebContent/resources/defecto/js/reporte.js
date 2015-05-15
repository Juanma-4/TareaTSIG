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
	
		
		
////////Creacion de los estilos y reglas para las propiedades publicas, reservadas y privadas /////////

		var estiloPropPublica = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "tipoestado" ,
					value : "Publica" ,
					}),
					symbolizer : {
						pointRadius : 7,
						fillColor : "green",
						fillOpacity : 0.5,
						strokeColor : "black"
					}
				}) ]
			})
		});
		
		var estiloPropPrivada = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "tipoestado" ,
					value : "Privada" ,
					}),
					symbolizer : {
						pointRadius : 7,
						fillColor : "red",
						fillOpacity : 0.5,
						strokeColor : "black"
					}
				}) ]
			})
		});
		
		var estiloPropReservadas = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "tipoestado" ,
					value : "Reservada" ,
					}),
					symbolizer : {
						pointRadius : 7,
						fillColor : "blue",
						fillOpacity : 0.5,
						strokeColor : "black"
					}
				}) ]
			})
		});
		
		
		
		
/////////// Usuo de WFS para traer las propiedades con las reglas y estilos definidos arriba ////////
		
		
		var PropiedadesPublicas = new OpenLayers.Layer.Vector("Propiedades Publicas", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloPropPublica,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
	
		var PropiedadesPrivadas = new OpenLayers.Layer.Vector("Propiedades Privadas", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloPropPrivada,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
			
		var PropiedadesReservadas = new OpenLayers.Layer.Vector("Propiedades Reservadas", {
			strategies : [ new OpenLayers.Strategy.BBOX() ],
			styleMap: estiloPropReservadas,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
		
	////// agregó las 3 capas de propiedades hechas antes ////////////////
	
		PropiedadesReservadas.setVisibility(false);
		PropiedadesPrivadas.setVisibility(false);
		
		
		var PopUp = new OpenLayers.Layer.WMS("Informacion",
		        "http://localhost:8080/geoserver/wms", 
		        {'layers': 'sige:propiedad', transparent: true, format: 'image/gif'},
		        {isBaseLayer: false}
		 
		    );
		
		highlightLayer = new OpenLayers.Layer.Vector("Highlighted Features", {
            displayInLayerSwitcher: false, 
            isBaseLayer: false 
            }
        );
		
		map.addLayers([ google_maps, gphy,PropiedadesPublicas,PropiedadesPrivadas,PropiedadesReservadas, PopUp,highlightLayer]);

		
		infoControls = new OpenLayers.Control.WMSGetFeatureInfo({
            url: "http://localhost:8080/geoserver/wms", 
            title: 'Identify features by clicking',
            queryVisible: true,
            eventListeners: {
                getfeatureinfo: function(event) {
                    map.addPopup(new OpenLayers.Popup.FramedCloud(
                        "popup", 
                        map.getLonLatFromPixel(event.xy),
                        null,          
                        event.text,
                        null,
                        true
                    ));
                }
            }
        });
		
		  
		
		
		map.addControl(infoControls);
		infoControls.activate();
        map.addControl(new OpenLayers.Control.LayerSwitcher());
		
		map.zoomToExtent(limites);		
		
	
		
	  	/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
            	    WGS84,  map.getProjectionObject()), miZoom + 3);
		
  		
};

	
