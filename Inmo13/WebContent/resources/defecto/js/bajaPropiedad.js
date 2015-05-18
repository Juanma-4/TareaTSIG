//////////********** VARIABLES **********//////////
var propiedad;
var saveStrategy;
var propiedades;
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
 			projection: WGS84_google_mercator,
 			displayProjection: WGS84 
 		};
 		
 		var map = new OpenLayers.Map('map', opciones);	
 		
 		var google_maps = new OpenLayers.Layer.Google("Google Maps", {
 			numZoomLevels : 20
 		});
 		
 		var google_fisico = new OpenLayers.Layer.Google("Google Fisico", {
 		type : google.maps.MapTypeId.TERRAIN
 		});		
 				
 		saveStrategy = new OpenLayers.Strategy.Save();	
 		saveStrategy.events.register("success", '', exito);
 		saveStrategy.events.register("failure", '', fallo);	    
 					
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
 			displayInLayerSwitcher : false,
 			protocol : new OpenLayers.Protocol.WFS({
 				version : "1.1.0",
 				url : urlWFS,
 				featureType : "propiedad",
 				featureNS : urlGeoServer,
 				geometryName : "geom",
 				srsName: gEPSG,
 			  	
 			}),
 		});	
 		 
 		map.addLayers([ google_maps, google_fisico, propiedades]);
 		map.zoomToExtent(limites);	
 		
 		/* PARA CENTRAR EN MONTEVIDEO */
     	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
             	    WGS84,  map.getProjectionObject()), miZoom + 3);
 		
     	
     	/**OpenLayers.Handler.Feature
     	 * Handler to respond to mouse events related to a drawn feature. Callbacks with the following keys will be 
     	 * notified of the following events associated with features: click, clickout, over, out, and dblclick.
     	 */
     	
     	/* Creo clase OpenLayers de tipo OpenLayers.Control.DeleteFeature con mi programación. */
     	 var BorrarFeature = OpenLayers.Class(OpenLayers.Control, {
     	 	initialize : function(capa, opciones) {
     	 		OpenLayers.Control.prototype.initialize.apply(this, [ opciones ]);
     	 		this.capa = capa;
     	 		this.handler = new OpenLayers.Handler.Feature(this, capa, {
     	 			click : this.clickFeature
     	 		});
     	 	},
     	 	clickFeature : function(feature) {
     	    	 		
     	 		// if feature doesn't have a fid, destroy it
     	 		if (feature.fid == undefined) {
     	 			this.capa.destroyFeatures([ feature ]);
     	 		} else {     	 		
     	 		
     	 			feature.state = OpenLayers.State.DELETE;
     	 			this.capa.events.triggerEvent("afterfeaturemodified", {
     	 				feature : feature
     	 			});
     	 		propiedades.removeFeatures(feature);
     	 		}
     	 	},
     	 	setMap : function(map) {
     	 		this.handler.setMap(map);
     	 		OpenLayers.Control.prototype.setMap.apply(this, arguments);
     	 	},
     	 	CLASS_NAME : "OpenLayers.Control.DeleteFeature"
     	 });
     	
     	
     	// Creo el panel para los controles.
     	var panel = new OpenLayers.Control.Panel({
     		displayClass : 'customEditingToolbar'
     	});

     	var navigate = new OpenLayers.Control.Navigation({
     		title : "Navegación Mapa"
     	});

     	var del = new BorrarFeature(propiedades, {
     		title : "Borrar Propiedad"
     	});

     	save = new OpenLayers.Control.Button({
     		title : "Guardar",
     		trigger : function() {
     	 		saveStrategy.save();
     		},
     		displayClass : "olControlSaveFeatures"
     	});

     	panel.addControls([ navigate, del, save]);
     	panel.defaultControl = navigate;
     	map.addControl(panel);
 };
 
 function exito(){
	 growl.show([{summary:'Exito', detail: 'Se borraron las propiedades', severity:'Info'}]); 
 }
 
 function fallo(){
	 growl.show([{summary:'Error', detail: 'Error al borrar la propiedad'}]); 
 }
 
 /*********************** 
  * 
  * https://github.com/tmcw/OpenLayerer/blob/master/openlayers_src/OpenLayers-2.9.1/examples/wfs-snap-split.html
  * ***********************/
 
 
