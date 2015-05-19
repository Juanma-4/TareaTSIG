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
 		

     	// Creo el panel para los controles.
     	var panel = new OpenLayers.Control.Panel({
     		displayClass : 'customEditingToolbar'
     	});
     	
 		
 		var navegar = new OpenLayers.Control.Navigation({
     		title : "Navegación Mapa",
     		displayClass: 'olControlNavigation'
     	});
     	
    
        var modificar = new OpenLayers.Control.ModifyFeature(propiedades, {
        	mode: OpenLayers.Control.ModifyFeature.RESHAPE | OpenLayers.Control.ModifyFeature.DRAG,
        	title: "Modificar Propiedad",
            displayClass: 'olControlModifyFeature'
         });
        
    	save = new OpenLayers.Control.Button({
     		title : "Guardar",
     		trigger : function() {
     			if(modificar.feature) {
     				modificar.selectControl.unselectAll();
               }
     	 		saveStrategy.save();
     		},
     		displayClass : "olControlSaveFeatures"
     	});
        
        panel.addControls([ navegar, modificar, save]);
        panel.defaultControl = navegar;
        map.addControl(panel);
  
    
     	
 };
 
 function exito(){
	 growl.show([{summary:'Exito', detail: 'Se modificaron las propiedades', severity:'Info'}]); 
 }
 
 function fallo(){
	 growl.show([{summary:'Error', detail: 'Error al modificar las propiedades'}]); 
 }
 
  	