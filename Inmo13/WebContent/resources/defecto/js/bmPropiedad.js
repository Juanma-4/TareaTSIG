﻿//////////********** VARIABLES **********//////////
var propiedad;
var saveStrategy;
var propiedades;
var growl;
var modifico = false;
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
 		
 	//	var fid = $("#formBMPropiedad\\:fid").html();
 		
 	/*	var filtro = new OpenLayers.Filter.Logical({
 		    type: OpenLayers.Filter.Logical.AND,
 		    filters: [
	 		        new OpenLayers.Filter.Comparison({
	 		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
	 		            property: "fid",
	 		            value: fid
	 		        })
 		        	]
 		});
 	*/	
 		/* "Layer Constructor" : Pide capa de porpiedades via WFS-T  */
 		 propiedades = new OpenLayers.Layer.Vector("Propiedad", {
 			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
 			styleMap: estiloProp,
 			displayInLayerSwitcher : false,
 		//	filter: filtro,
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
 		
 		/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
    			new OpenLayers.Projection(miEPSG),  map.getProjectionObject()), miZoom + 3);
		
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
     	 			feature.renderIntent = "select";
     				this.capa.drawFeature(feature);
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

     	var navegar = new OpenLayers.Control.Navigation({
     		title : "Navegación Mapa"
     	});

     	var del = new BorrarFeature(propiedades, {
     		title : "Borrar Propiedad"
     	});

     	 var modificar = new OpenLayers.Control.ModifyFeature(propiedades, {
         	selectControl: new OpenLayers.Control.SelectFeature([propiedades]),
         	mode: OpenLayers.Control.ModifyFeature.RESHAPE | OpenLayers.Control.ModifyFeature.DRAG,
         	title: "Modificar Propiedad",
             displayClass: 'olControlModifyFeature'
          });
     	 
     	save = new OpenLayers.Control.Button({
     		title : "Guardar",
     		trigger : function() {
     			if(modificar.feature){
     				modificar.selectControl.unselectAll();
     				modifico = true;
     			}
     	 		saveStrategy.save();
     		},
     		displayClass : "olControlSaveFeatures"
     	});
     	
     	/*
        propiedades.events.on({
            'beforefeaturemodified': function(evt) {
            	modificar.selectControl.select(evt.feature);
             },
             'afterfeaturemodified': function(evt) {
            	 modificar.selectControl.unselect(evt.feature);
             }
        });
        */

     	panel.addControls([ navegar, del, modificar, save]); 
     	panel.defaultControl = navegar;
     	map.addControl(panel);
 };
//////**** Funciones **** //////
 function exito(){
	 if(modifico){
		 growl.show([{summary:'Exito', detail: 'Se modificaron las propiedades', severity:'Info'}]); 
	 }else{
		 growl.show([{summary:'Exito', detail: 'Se borraron las propiedades', severity:'Info'}]); 
	 }
	 
 }
 
 function fallo(){
	 if(modifico){
		 growl.show([{summary:'Error', detail: 'Error al modificar las propiedades'}]); 
	 }else{
		 growl.show([{summary:'Error', detail: 'Error al borrar las propiedades'}]); 
	 }
 }

 /*********************** 
  * 
  * https://github.com/tmcw/OpenLayerer/blob/master/openlayers_src/OpenLayers-2.9.1/examples/wfs-snap-split.html
  * ***********************/
 
 
