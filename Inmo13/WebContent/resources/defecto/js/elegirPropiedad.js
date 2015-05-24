//////////********** VARIABLES **********//////////
var apreto = false;
var saveStrategy;
var selectControl;
var selectedFeature;
var map;
//////////********** CREACIÓN DE MAPA, CAPAS Y CONTROLES **********//////////
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
			projection: WGS84_google_mercator, 
			displayProjection: WGS84 
		};
	
	
		/* "Map Constructor" : Crea un mapa en el bloque con id "map"  */		
		map = new OpenLayers.Map('map', opciones);	
		
		var google_maps = new OpenLayers.Layer.Google("Google Maps", {
			numZoomLevels : 20
		});
		
		var gphy = new OpenLayers.Layer.Google("Google Físico", {
		type : google.maps.MapTypeId.TERRAIN
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
		
		 propiedades = new OpenLayers.Layer.Vector("Propiedad", {
	 			strategies : [new OpenLayers.Strategy.BBOX()],
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
		
		map.addLayers([ google_maps, gphy,propiedades]);

		/*	Control para la selección de popUps */
		selectControl = new OpenLayers.Control.SelectFeature([propiedades],
				    {
				        onSelect: onPopupFeatureSelect,
				        onUnselect: onPopupFeatureUnselect,
				    });
		
	    map.addControl(selectControl);
	    selectControl.activate();
		
		map.zoomToExtent(limites);		
		
	  	/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
    			new OpenLayers.Projection(miEPSG),  map.getProjectionObject()), miZoom + 3);
		
  		
};

//////**** Funciones **** //////
function onPopupClose(evt) {
    selectControl.unselect(selectedFeature);
}

function onPopupFeatureSelect(feature) {
	
    selectedFeature = feature;
    popup = new OpenLayers.Popup.FramedCloud(
    		"",
	        feature.geometry.getBounds().getCenterLonLat(),
	        null,//new OpenLayers.Size(150,200), 
	        
	        '<div>'+
	        '<div style="color:#FF0000;text-align:center">'+
	        feature.data.calle +
	        '</br>' + 
	        feature.data.numeropuerta +
	        '</div>'+
	      
   
	        '</br>'+ '</br>'+
	  '<div style="color:#000000">'+
	        '<label for="usr"style="color:#000000" >Propiedad: </label>' + feature.data.tipopropiedad +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Se: </label>' + feature.data.tipotransaccion +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Precio: </label>' + feature.data.tipomoneda +'<label>  </label>'+ feature.data.precio +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Piso: </label>' + feature.data.piso +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Dormitorios: </label>' + feature.data.cantdorm +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Baños: </label>' + feature.data.cantbanio +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Metros Cuadrados: </label>' + feature.data.metroscuadrados +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Parrillero: </label>' + feature.data.parrillero +
	        '</br>'+
	        '<label for="usr"style="color:#000000" >Garage: </label>' + feature.data.garage +
	        '</br>'+
	       
        '</div>' +
        	'</br>' +
	        '<div style="text-align:center">'+
	        	'<a class="linkMB" onclick="enviarDatos()" href="http://localhost:8080/Inmo13/faces/ModificarDatosPropiedad.xhtml">Modificar Datos</a>'+
	        	 // 	'<p:commandLink id="irMBPropiedad" onclick="enviarDatos()" immediate="true">Modificar/Borrar</p:commandLink>'+
	        '</div>'+
	        
	        '<div style="text-align:center">'+
	        	' <img src="http://hogartotal.imujer.com/sites/default/files/hogartotal/Fotos-de-fachadas-de-casas-modernas-3.jpg">' +
	        '</div>',
	        null,  
	        true, 
	        onPopupClose
	);
    popup.panMapIfOutOfView = true;
    popup.autoSize = true;
//    popup.autoSize = false;
    feature.popup = popup;
    map.addPopup(popup);
}
function onPopupFeatureUnselect(feature) {
    map.removePopup(feature.popup);
    feature.popup.destroy();
    feature.popup = null;
}
function enviarDatos(){
	var calle = selectedFeature.data.calle;
	var precio = selectedFeature.data.precio;
	var numPuerta = selectedFeature.data.numeropuerta; 
	var tipoPropiedad = selectedFeature.data.tipopropiedad; 
	var tipotransaccion = selectedFeature.data.tipotransaccion; 
	var tipoMoneda = selectedFeature.data.tipomoneda; 
	var tipoEstado = selectedFeature.data.tipoEstado; 
	var piso = selectedFeature.data.piso; 
	var cantDorm = selectedFeature.data.cantdorm; 
	var cantBanio = selectedFeature.data.cantbanio; 
	var metrosCuadrados = selectedFeature.data.metroscuadrados; 
	var parrillero = selectedFeature.data.parrillero;
	var garage = selectedFeature.data.garage;
	var usuario = selectedFeature.data.usuario;
	var fid = selectedFeature.data.fid;	
	remoteBMPropiedad([{name:'calle', value:calle},{name:'precio', value:precio},{name:'numPuerta', value:numPuerta},{name:'tipoPropiedad', value:tipoPropiedad},
					   {name:'tipotransaccion', value:tipotransaccion},{name:'tipoMoneda', value:tipoMoneda},{name:'tipoEstado', value:tipoEstado},
					   {name:'piso', value:piso},{name:'cantDorm', value:cantDorm},{name:'cantBanio', value:cantBanio},{name:'metrosCuadrados', value:metrosCuadrados},
					   {name:'parrillero', value:parrillero},{name:'garage', value:garage},{name:'usuario', value:usuario},{name:'fid', value:fid}]);
}
