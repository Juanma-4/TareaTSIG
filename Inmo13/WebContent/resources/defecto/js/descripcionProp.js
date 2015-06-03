var map;
var gmapLayer;
var propId;

////////////style para geolocalizacion
var style = {
	    fillColor: '#000',
	    fillOpacity: 0,
	    strokeWidth: 0
};

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
   		 
   		 var fid= document.getElementById('formDescripcion:fid').value;
   		 
   		 var filter = new OpenLayers.Filter.Logical({
		        type: OpenLayers.Filter.Logical.OR,
		        filters: [
		        new OpenLayers.Filter.Comparison({
		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
		            property: "fid",
		            value: fid
		        })
		        ]
		    });
   		 
		 filterStrategy = new OpenLayers.Strategy.Filter({filter: filter});
		 
		 	/////////////// GEOLOCALIZACION !!! //////////////////
		    vectorLocalizador = new OpenLayers.Layer.Vector('Localizado',{
		    	 displayInLayerSwitcher : false,
		    });
		    
		    var geolocate = new OpenLayers.Control.Geolocate({
		        bind: false,
		        geolocationOptions: {
		            enableHighAccuracy: false,
		            maximumAge: 0,
		            timeout: 7000
		        }
		    });
		    
		    geolocate.watch = true;
	        var  firstGeolocation = true;
	        geolocate.activate();
	        
		    map.addControl(geolocate);
		    
		    geolocate.events.register("locationupdated",geolocate,function(e) {
		    	vectorLocalizador.removeAllFeatures();
		    	
		        var circle = new OpenLayers.Feature.Vector(
		            OpenLayers.Geometry.Polygon.createRegularPolygon(
		                new OpenLayers.Geometry.Point(e.point.x, e.point.y),
		                e.position.coords.accuracy/2,
		                40,
		                0
		            ),
		            {},
		            style
		        );
		        
		        vectorLocalizador.addFeatures([
		            new OpenLayers.Feature.Vector(
		                e.point,
		                {},
		                {  	externalGraphic : "resources/defecto/img/ubicacion.png",
							graphicWidth : 33,
							graphicHeight : 33
						}
		            ),
		            circle
		        ]);
		    });
		    
		    geolocate.events.register("locationfailed",this,function() {
			    OpenLayers.Console.log('Location detection failed');
			});
		    /////////////////////////Fin GeoLocalizacion //////////////////////////////
		    
		    // Para la escala
		    var scaleline = new OpenLayers.Control.ScaleLine();
		    map.addControl(scaleline);		 
		 
            /* "Layer Constructor" : Pide capa de porpiedades via WFS  */
   		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
   			strategies : [ new OpenLayers.Strategy.Fixed(),filterStrategy],
   			styleMap: estiloProp,
   			protocol : 
   			 new OpenLayers.Protocol.WFS({
   				version : "1.1.0",
   				url : urlWFS,
   				featureType : "propiedad",
   				featureNS : urlGeoServer,
   				geometryName : "geom",
   				srsName: gEPSG,
   			})
   			
   		});	
   		map.addLayers([gmapLayer,propiedades,vectorLocalizador]);
   		
        map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
        new OpenLayers.Projection(miEPSG),
        map.getProjectionObject()
        ), 12);

        remoteListarPuntosInteres();
}
        
function handleConfirm(xhr,status,args){
	
	
	 //Estilos
	 estiloPuntoInteres = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ 
						new OpenLayers.Rule({
						    filter: filter = new OpenLayers.Filter.Logical({
						        type: OpenLayers.Filter.Logical.AND,
						        filters: [
						            new OpenLayers.Filter.Comparison({
						                type: OpenLayers.Filter.Comparison.EQUAL_TO,
						                property: "tipo",
						                value: "comercial"
						            }),
						            new OpenLayers.Filter.Comparison({
						                type: OpenLayers.Filter.Comparison.EQUAL_TO,
						                property: "nombre",
						                value: "TA TA"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/pelota.png",
						        graphicOpacity : 1
						    }
						})
						,
						new OpenLayers.Rule({
						    filter: filter = new OpenLayers.Filter.Logical({
						        type: OpenLayers.Filter.Logical.AND,
						        filters: [
						            new OpenLayers.Filter.Comparison({
						                type: OpenLayers.Filter.Comparison.EQUAL_TO,
						                property: "tipo",
						                value: "comercial"
						            }),
						            new OpenLayers.Filter.Comparison({
						                type: OpenLayers.Filter.Comparison.EQUAL_TO,
						                property: "nombre",
						                value: "MULTI AHORR"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/buses.png",
						        graphicOpacity : 1
						    }
						})
				 ]
			})
		});
	
	 
  //Creo la capa de los puntos de interes.
  puntosInteres = new OpenLayers.Layer.Vector("Puntos Interes",
		 {styleMap : estiloPuntoInteres});
  
  // Manipulo el json del callback
  var puntosInteresJSON = JSON.parse(args.PuntosInteres);
	
  for(var i=0; i<puntosInteresJSON.length;i++){
	 
	  var pi = puntosInteresJSON[i];
	  
	  var puntoInteres = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(pi.x, pi.y));
	  puntoInteres.data.nombre = pi.nombre;
	  puntoInteres.data.tipo = pi.tipo;
	  
	  puntoInteres.attributes.nombre = pi.nombre;
	  puntoInteres.attributes.tipo = pi.tipo;
	  
	  puntoInteres.renderIntent = "default";
      
	  puntosInteres.addFeatures([puntoInteres]);
	  
  }
 
	map.addLayer(puntosInteres);
	 
	 
}