var map;
var gmapLayer;
var propId;
var propiedades;
////////////style para geolocalizacion
var style = {
	    fillColor: '#000',
	    fillOpacity: 0,
	    strokeWidth: 0
};

window.onload = function() {
	
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
         			displayProjection: WGS84, 
         			
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
   							graphicWidth : 54,
   							graphicHeight : 40
   	
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
   		
   		propId = propiedades.id;
   		
        // Asi es como anda 
   	/*	    map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
        new OpenLayers.Projection(miEPSG),
        map.getProjectionObject()
        ), 12);
           
   	*/
           
        
   	/*
   		var dataExtent = propiedades.getDataExtent();
   		map.zoomToExtent(dataExtent);
   		
   			
   	 map.zoomToExtent(propiedades.getDataExtent());
   	 
   	 map.setCenter(new OpenLayers.LonLat(, ).transform(
   	        new OpenLayers.Projection(miEPSG),
   	        map.getProjectionObject()
   	        ), 12);
   	        
   	        
   	    //	map.setCenter(new OpenLayers.LonLat(fid.geometry.getBounds(), fid.geometry.getBounds()), 16);    
	*/	
   	
   		
   		 propiedades.events.register("loadend", propiedades, function() {
   		console.log("termino");
   		terminar();
   		});
   		
   		
   		
        remoteListarPuntosInteres();
}
function terminar() {
	//alert("asdasd");
var propiedades = map.getLayer(propId);


map.zoomToExtent(propiedades.getDataExtent()); 
map.zoomTo(17);
}



function handleConfirm(xhr,status,args){
	
	
	 //Estilos
	 estiloPuntoInteres = new OpenLayers.Style();
			
	 
	 
					var tata =	new OpenLayers.Rule({
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
						        externalGraphic: "resources/defecto/img/tata.png",
						        graphicOpacity : 1,
						        graphicWidth : 25,
								graphicHeight : 25
						    }
						});
						
				 var macro = new OpenLayers.Rule({
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
						                value: "MACRO MERCA"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/macro.png",
						        graphicOpacity : 1,
						        graphicWidth : 35,
								graphicHeight : 35
						    }
						});
						
						
					var devoto =	new OpenLayers.Rule({
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
						                value: "DEVOTO"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/devoto.png",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
						    }
						});
						
					var inglesa =	new OpenLayers.Rule({
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
						                value: "TIENDA INGL"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/tiendaInglesa.jpg",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
						    }
						});
						
					var carrito =	new OpenLayers.Rule({
							filter : new OpenLayers.Filter.Comparison({
								type : OpenLayers.Filter.Comparison.EQUAL_TO,
								property : "tipo" ,
								value : "comercial" ,
							}),
							symbolizer : {
								externalGraphic: "resources/defecto/img/carro.png",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
							}
					});
						
					var plaza =	new OpenLayers.Rule({
							filter : new OpenLayers.Filter.Comparison({
								type : OpenLayers.Filter.Comparison.EQUAL_TO,
								property : "tipo" ,
								value : "plaza" ,
							}),
							symbolizer : {
								externalGraphic: "resources/defecto/img/pelota.png",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
							}
					});
						
						var escuela = new OpenLayers.Rule({
							filter : new OpenLayers.Filter.Comparison({
								type : OpenLayers.Filter.Comparison.EQUAL_TO,
								property : "tipo" ,
								value : "escuela" ,
							}),
							symbolizer : {
								externalGraphic: "resources/defecto/img/escuela.jpg",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
								
							}
					});
						
					var secundaria =	new OpenLayers.Rule({
							filter : new OpenLayers.Filter.Comparison({
								type : OpenLayers.Filter.Comparison.EQUAL_TO,
								property : "tipo" ,
								value : "liceo" ,
							}),
							symbolizer : {
								externalGraphic: "resources/defecto/img/secundaria.png",
						        graphicOpacity : 1,
						        graphicWidth : 20,
								graphicHeight : 20
							}
					});
						
					var multi =	new OpenLayers.Rule({
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
						        externalGraphic: "resources/defecto/img/multiahorro.png",
						        graphicOpacity : 1,
						        graphicWidth : 23,
								graphicHeight : 23
						    }
						});
						var disco = new OpenLayers.Rule({
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
						                value: "DISCO"
						            })  
						        ]
						    }),
						    symbolizer: {
						        externalGraphic: "resources/defecto/img/disco.png",
						        graphicOpacity : 1,
						        graphicWidth : 23,
								graphicHeight : 23
								
						    }
						});
						
			
					
					
	estiloPuntoInteres.addRules([escuela, plaza, carrito, secundaria,tata, multi, macro, disco, inglesa, devoto ]);
	 
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
	  puntoInteres.data.distancia = pi.distancia;
	  
	  puntoInteres.attributes.nombre = pi.nombre;
	  puntoInteres.attributes.tipo = pi.tipo;
	  puntoInteres.attributes.distancia = pi.distancia;
	  
	  puntoInteres.renderIntent = "default";
      
	  puntosInteres.addFeatures([puntoInteres]);
	  
  }
 
	map.addLayer(puntosInteres);
	 
	selectControl = new OpenLayers.Control.SelectFeature([puntosInteres],
		    {
		        onSelect: onPopupFeatureSelect,
		        onUnselect: onPopupFeatureUnselect,
		        hover:true,
		        //highlightOnly: false // en true solo se agranda, es solo para eso
		    });

map.addControl(selectControl); 
selectControl.activate();
}

function onPopupClose(evt) {
    selectControl.unselect(selectedFeature);
}

function onPopupFeatureSelect(feature) {
    selectedFeature = feature;
       
    var popUpHtml = 
        '<div>'+
        '<div style="text-align:center" class="font-style">'+
        feature.data.nombre +
        '</br>' +
        '<div class="my-container">'+
        '<font-style2>Distancia: </font-style2> <font-style3>	'+ feature.data.distancia + ' m' +
        '</font-style3> </br>'+
//        '<label for="usr"style="color:#000000" >Distancia: </label>'+ feature.data.distancia + '<label for="usr"style="color:#000000" >m </label>'+
        '</div>'+
        '</div>' 
        

    
    popup = new OpenLayers.Popup.FramedCloud(
    		"chicken",
	        feature.geometry.getBounds().getCenterLonLat(),
	        null,	
	        popUpHtml
	        ,
	        null, 
	        true, 
	        onPopupClose
	);
    popup.panMapIfOutOfView = true;
    popup.autoSize = true;
    feature.popup = popup;
    map.addPopup(popup);
}
function onPopupFeatureUnselect(feature) {
    map.removePopup(feature.popup);
    feature.popup.destroy();
    feature.popup = null;
}