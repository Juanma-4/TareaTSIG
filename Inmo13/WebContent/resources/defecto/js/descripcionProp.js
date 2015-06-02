var map;
var gmapLayer;

        function init() {
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
            map = new OpenLayers.Map('map', opciones);

            gmapLayer = new OpenLayers.Layer.Google(
            "Google Streets", // the default
            {numZoomLevels: 20, 'sphericalMercator': true }
            );
 

//            map.addLayer(gmapLayer);
//            map.events.register("click", map, function(e) {
//            	  
//            	var position = map.getLonLatFromPixel(e.xy);
//            	alert(map.getProjectionObject());
//            	  OpenLayers.Util.getElement("coordenadas").innerHTML = 
//            	        position.lon.toFixed(3) + ', ' + position.lat.toFixed(3);
//
//            	});

   		 estiloProp = new OpenLayers.StyleMap({
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
   		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
   			strategies : [ new OpenLayers.Strategy.Fixed()], //,filterStrategy 
   			styleMap: estiloProp,
   			protocol : 
   			 new OpenLayers.Protocol.WFS({
   				version : "1.1.0",
   				url : urlWFS,
   				featureType : "propiedad",
   				featureNS : urlGeoServer,
   				geometryName : "geom",
   				srsName: gEPSG,
//   				new OpenLayers.Protocol.Script({
//   				url : urlWFS,
//   				callbackKey: 'format_options',
//   				callbackPrefix: 'callback:',
//   				params: {
//   					service: 'WFS',
//   					version: '1.1.0',
//   					srsName: miEPSG,
//   					request: 'GetFeature',
//   					typeName: 'sige:propiedad',
//   					outputFormat: 'json',
//   				    CQL_FILTER: "tipoestado='"+"Publica"+"'",
//   				}
   			})
   			
   		});	
   		map.addLayers([gmapLayer,propiedades]);
            // Google.v3 uses EPSG:900913 as projection, so we have to
            // transform our coordinates -6252433.174, -4150065.807
            map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
            new OpenLayers.Projection(miEPSG),
            map.getProjectionObject()
            ), 12);

}