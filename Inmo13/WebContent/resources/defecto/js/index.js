var map;
var opciones;
var limites;
var WGS84_google_mercator;
var WGS84;
var propiedades;
var estiloProp;
var vectorLocalizador;
var propId;
//////////// style para geolocalizacion
var style = {
	    fillColor: '#000',
	    fillOpacity: 0,
	    strokeWidth: 0
};

function init() {
	   
		 WGS84 = new OpenLayers.Projection(miEPSG);
		 WGS84_google_mercator = new OpenLayers.Projection(gEPSG);		
		
		limites = new OpenLayers.Bounds(
			366582.290141166, 6127927.10038269,
			858252.0151745,6671738.21181725
		).transform(WGS84, WGS84_google_mercator);
		
		opciones = {
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
		 map = new OpenLayers.Map('map', opciones);	
		
		var google_maps = new OpenLayers.Layer.Google("Google Maps", {
			numZoomLevels : 20
		});
		
		var gphy = new OpenLayers.Layer.Google("Google Physical", {
		type : google.maps.MapTypeId.TERRAIN
		});						

		
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

		 var filter = new OpenLayers.Filter.Logical({
		        type: OpenLayers.Filter.Logical.OR,
		        filters: [
		        new OpenLayers.Filter.Comparison({
		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
		            property: "tipoestado",
		            value: "Publica"
		        }),
		        new OpenLayers.Filter.Comparison({
		            type: OpenLayers.Filter.Comparison.EQUAL_TO,
		            property: "tipoestado",
		            value: "Reservada"
		        })
		        ]
		    });
			

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
	    
	    
		/* "Layer Constructor" : Pide capa de porpiedades via WFS  */
		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
			strategies : [ new OpenLayers.Strategy.Fixed()], //,filterStrategy BBOX Fixed()
			styleMap: estiloProp,
			protocol :  new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
							 
			})
			
		});	
 	    
		propId = propiedades.id;

		map.addLayers([ google_maps, gphy, propiedades,vectorLocalizador]);
    	map.zoomToExtent(limites);		
	
		/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
    			new OpenLayers.Projection(miEPSG),  map.getProjectionObject()), miZoom + 3);
	
    	
//    	map.events.register("click", map, function(e) {	
// 			var posicion = map.getLonLatFromPixel(e.xy);
// 			alert("LATITUD : "+posicion.lat + "LONGITUD : " + posicion.lon  );
// 			
//    	});
    	
};					

// Se envian los datos para filtrar las propiedades.
function buscarPropiedades(){

var tipopropiedad = document.getElementById('filtro-centros:tipoPropiedad').value;
var tipotransaccion =  document.getElementById('filtro-centros:tipoTransaccion').value;
var tipomoneda =  document.getElementById('filtro-centros:moneda').value;
var minimo = parseInt(document.getElementById('filtro-centros:minimo').value);
var maximo = parseInt(document.getElementById('filtro-centros:maximo').value);
var cantbanio = parseInt(document.getElementById('filtro-centros:cantBanio').value);
var cantdorm = parseInt(document.getElementById('filtro-centros:cantDormitorio').value);
var metroscuadrados = parseFloat(document.getElementById('filtro-centros:metrosCuadrados').value);
var barrio =  document.getElementById('filtro-centros:barrio').value;
var parrillero = document.getElementById('filtro-centros:parrillero').checked;
var garage = document.getElementById('filtro-centros:garage').checked; 

//var distanciaMar = parseInt($("#MarSliderVal").text());
//var distanciaParada = parseInt($("#BusSliderVal").text());
//var distanciaPuntoInteres = parseInt($("#PInteresSliderVal").text());


//remoteListarPropiedades([{name:'tipopropiedad', value:tipopropiedad},{name:'tipotransaccion', value:tipotransaccion},{name:'tipomoneda', value:tipomoneda},
//       {name:'minimo', value:minimo},{name:'maximo', value:maximo},{name:'cantbanio', value:cantbanio},{name:'cantdorm', value:cantdorm},
//		  {name:'metroscuadrados', value:metroscuadrados},{name:'barrio', value:barrio},{name:'parrillero', value:parrillero},{name:'garage', value:garage},
//		  {name:'distanciaMar', value:distanciaMar},{name:'distanciaParada', value:distanciaParada},{name:'distanciaPuntoInteres', value:distanciaPuntoInteres}]);


remoteListar([{name:'tipopropiedad', value:tipopropiedad},{name:'tipotransaccion', value:tipotransaccion},{name:'tipomoneda', value:tipomoneda},{name:'minimo', value:minimo},
              {name:'maximo', value:maximo}, {name:'cantbanio', value:cantbanio},{name:'cantdorm', value:cantdorm},{name:'metroscuadrados', value:metroscuadrados},{name:'barrio', value:barrio},{name:'parrillero', value:parrillero},{name:'garage', value:garage}]);


}
// Funcion onComplete
function handleConfirm(xhr,status,args)
{
//  alert(propId);
//  alert(args.PropiedaesFiltradas);
  var propiedades = map.getLayer(propId);
  //Borro las propiedades que se ven
  propiedades.removeAllFeatures();  
  //propiedades.destroyFeatures();
  // Manipulo el json del callback
  var propiedadesJSON = JSON.parse(args.PropiedaesFiltradas);
 

  for(var i=0; i<propiedadesJSON.length;i++){
	  
	  var prop = propiedadesJSON[i];
	  
	  var propiedadFiltrada = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(prop.latitud, prop.longitud));
	  propiedadFiltrada.data.calle = prop.calle;
      propiedadFiltrada.attributes.calle = prop.calle;
      propiedadFiltrada.renderIntent = "default";
	  propiedades.addFeatures([propiedadFiltrada]);
  }
  
//  propiedades.refresh({force:true});
  
}

   