//////////********** VARIABLES **********//////////
var apreto = false;
var zona;
var saveStrategy;
var zonas;
var nuevaZona;
var growl;
var map;

//////////********** CREACIÓN DE MAPA, CAPAS Y CONTROLES **********//////////
window.onload = function() {
		growl = PF('growl');
		var WGS84_google_mercator = new OpenLayers.Projection(gEPSG);	
 		var WGS84 = new OpenLayers.Projection(miEPSG);		
 		
 		var opciones = {
 			controls : [ new OpenLayers.Control.Navigation(),
 				new OpenLayers.Control.PanZoom(),
 				new OpenLayers.Control.LayerSwitcher(),
 				new OpenLayers.Control.MousePosition({
 					div : document.getElementById("coordenadas")
 				}) ],
 			projection: WGS84_google_mercator, // se agrega solo igual, se puede omitir.
 			displayProjection: WGS84 
 		};
 	
 	
 		/* "Map Constructor" : Crea un mapa en el bloque con id "map"  */		
 		map = new OpenLayers.Map('map', opciones);	
 		
 		var google_maps = new OpenLayers.Layer.Google(
 	            "Google Streets", // the default
 	            {numZoomLevels: 20, 'sphericalMercator': true }
 	            );
 			
// 			new OpenLayers.Layer.Google("Google Maps", {
// 			numZoomLevels : 20
// 		});
 		
 		var google_fisico = new OpenLayers.Layer.Google("Google Fisico", {
 		type : google.maps.MapTypeId.TERRAIN
 		});		
 				
 		
 		/* Estrategia que se va a utilizar para guardar la zona. */		
 		saveStrategy = new OpenLayers.Strategy.Save();	
 		saveStrategy.events.register("success", '', exito);
 		saveStrategy.events.register("failure", '', fallo);	    


 		/* 	Estilo de la zona que se va a ingresar */
// 		var estiloZone = new OpenLayers.StyleMap({
// 				"default" : new OpenLayers.Style(null, {
// 					rules : [ new OpenLayers.Rule({
// 						symbolizer : {
// 							"Polygon" : {
// 								pointRadius : 20,
// 								graphicOpacity : 1,
// 								graphicWidth : 50,
// 								graphicHeight : 36
//
// 							}
// 						}
// 					}) ]
// 				})
// 			});
 		
 		/* "Layer Constructor" : Pide capa de porpiedades via WFS-T  */
 		 zonas = new OpenLayers.Layer.Vector("Propiedad", {
 			strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
// 			styleMap: estiloZone,
 			protocol : new OpenLayers.Protocol.WFS({
 				version : "1.1.0",
 				url : urlWFS,
 				featureType : "zonas",
 				featureNS : urlGeoServer,
 				geometryName : "geom",
 				srsName: gEPSG,
 			  	
 			}),
 		});	
 		 
 		
 		 nuevaZona = new OpenLayers.Layer.Vector("Nueva zona", {
//             styleMap: estiloZone,
             geometryType : OpenLayers.Geometry.Polygon,
             displayInLayerSwitcher : false,
         });
 		 

 		
 		/// para dibujar
 		dibujar = new OpenLayers.Control.DrawFeature(nuevaZona,
 													 OpenLayers.Handler.Polygon, {
 															featureAdded : onFeatureAdded,
 	    });
 		
 		map.addControl(dibujar);		
 		dibujar.activate();
 				    
 		map.addLayers([ google_maps, google_fisico, zonas, nuevaZona]);	
 	
 	// Control para Zonas "pegajosas"
 		var snap = new OpenLayers.Control.Snapping({
 			layer : nuevaZona
 		});
 		map.addControl(snap);
 		snap.activate();

 		// configure split agent
 		var split = new OpenLayers.Control.Split({
 			layer : nuevaZona,
 			source : nuevaZona,
 			tolerance : 1000,
 			deferDelete : true,
 			eventListeners : {
 				aftersplit : function(event) {
 					// var msg = "Split resulted in " + event.features.length + "
 					// features.";
 					flashFeatures(event.features);
 				}
 			}
 		});
 		map.addControl(split);
 		split.activate();

 	  	/// PARA CENTRAR EN MONTEVIDEO
 		map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
 	            new OpenLayers.Projection(miEPSG),
 	            map.getProjectionObject()
 	            ), 12);
 							
 };

//////////********** CONTROLES AL INGRESAR zona **********//////////
 function exito(){
	 growl.show([{summary:'Exito', detail: 'Se guardo la zona', severity:'Info'}]); 
 }
 
 function fallo(){
	 growl.show([{summary:'Error', detail: 'Error al ingresar la zona'}]); 
 }
 
 function onFeatureAdded(feature) {
 	if(apreto){
 		nuevaZona.removeFeatures(zona);
 	} 	
 	zona = feature;
 	//propiedades.addFeatures([propiedad]);	//No es necesario porque el dibujar ya lo agrega
 	apreto = true;
	zonaid = zona.id;
	if (encimado(zona, zonas)) { // si esta encimado
		zonas.destroyFeatures([ feature ]);
		growl.show([{summary:'Error', detail: 'La zona creada se superpone, vuelva a intentarlo'}]);
		dibujar.cancel();
	}
 }
 	
  
function darAltaGeom(){	
	  
	 zonas.addFeatures([nuevaZona.getFeatureById(zona.id)]);
 	  
 	 zona.attributes.nombre =  document.getElementById('formZona:nombre').value;
 	zona.attributes.descripcion =  document.getElementById('formZona:desc').value;
 	 zona.attributes.fid =  zona.id;
 	 saveStrategy.save();	
}

/////////////*************** FUNCIONES DE CONTROL ***************/////////////

$(function() {  
	$("#formZona\\:botonFormZona").click(function(){ 
	  if (apreto){			  
		  if(controlarInputs()){
			  darAltaGeom();
		  }else
			  return false;
	  }else{
		  growl.show([{summary:'Error', detail: 'Debe dar de alta una zona.'}]); //, severity: 'info severity'}]) warn
		  return false;
	  }
	});
	
}); 

function controlarInputs(){
	
	if(!$.trim($("#formZona\\:nombre").val()).length) {
		growl.show([{summary:'Error', detail: 'Debe ingresar un nombre!'}]); 
		return false;
	}else{
		return true;
	}
	
}

function encimado(miFeature, capa) {
	var openLayersParser = new jsts.io.OpenLayersParser(); // interprete
	// openlayers a jsts framework
	var miFeatureGeometry = openLayersParser.read(miFeature.geometry); // hacerla compatible con el frame jsts

	var cont = capa.features.length;

	// recorrer cada feature y preguntar si se intersecta
	for ( var i = 0; i < cont; i++) {

		var featureActual = capa.features[i]; // elemento actual

		if (featureActual != miFeature) { // si no es el mismo elemento

			var geoActual = openLayersParser.read(featureActual.geometry);

			if (geoActual.overlaps(miFeatureGeometry)) {
				return true;
			}
		}

	}
	return false;
}