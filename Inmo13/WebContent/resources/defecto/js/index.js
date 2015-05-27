var apreto = false;
var punto;
var gid;
var saveStrategy;
var map;
var opciones;
var limites;
var WGS84_google_mercator;
var WGS84;
var propiedades;
var estiloProp;
var propiedadesFiltradas;
var estiloFiltrado;
var filterStrategy;

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
			
		 filterStrategy = new OpenLayers.Strategy.Filter({filter: filter});		
		
		/* "Layer Constructor" : Pide capa de porpiedades via WFS  */
		 propiedades = new OpenLayers.Layer.Vector("Propiedades", {
			strategies : [ new OpenLayers.Strategy.Fixed()], //,filterStrategy 
			styleMap: estiloProp,
			protocol : 
//			 new OpenLayers.Protocol.WFS({
//				version : "1.1.0",
//				url : urlWFS,
//				featureType : "propiedad",
//				featureNS : urlGeoServer,
//				geometryName : "geom",
//				srsName: gEPSG,
				new OpenLayers.Protocol.Script({
				url : urlWFS,
				callbackKey: 'format_options',
				callbackPrefix: 'callback:',
				params: {
					service: 'WFS',
					version: '1.1.0',
					srsName: miEPSG,
					request: 'GetFeature',
					typeName: 'sige:propiedad',
					outputFormat: 'json',
				    CQL_FILTER: "tipoestado='"+"Publica"+"'",
				}
			})
			
		});	
		 	 
		
	   
		map.addLayers([ google_maps, gphy, propiedades]);
		
		
		map.zoomToExtent(limites);		
	
		/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
    			new OpenLayers.Projection(miEPSG),  map.getProjectionObject()), miZoom + 3);
		
};					

//////////////////////funcion que hace la busqueda en si misma 

$(function() { 
	$("#botonBusqueda").click(function(){
		
	 
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
	 
     var distanciaMar = parseInt($("#MarSliderVal").text());
     var distanciaParada = parseInt($("#BusSliderVal").text());
     var distanciaPuntoInteres = parseInt($("#PInteresSliderVal").text());

     
     var publica =  new OpenLayers.Filter.Comparison({
         type: OpenLayers.Filter.Comparison.EQUAL_TO,
         property: "tipoestado",
         value: "Publica"
     });
     var reservada = new OpenLayers.Filter.Comparison({
         type: OpenLayers.Filter.Comparison.EQUAL_TO,
         property: "tipoestado",
         value: "Reservada"
     });
     
	 var filter = new OpenLayers.Filter.Logical({
	        type: OpenLayers.Filter.Logical.OR,
	        filters: [
                  //publica,reservada,
	        new OpenLayers.Filter.Comparison({
	            type: OpenLayers.Filter.Comparison.EQUAL_TO,
	            property: "tipopropiedad",
	            value: tipopropiedad
	        }),
	        new OpenLayers.Filter.Comparison({
	            type: OpenLayers.Filter.Comparison.EQUAL_TO,
	            property: "tipotransaccion",
	            value: tipotransaccion
	        })
	        ]
	    });

	    filterStrategy.setFilter(filter);
		
	});
});


 
 $( document ).ready(function() {

	 $(function(){
	    
		   $("#sliderMar").slider();
		   $("#sliderMar").on("slide", function(slideEvt) {
		   	$("#MarSliderVal").text(slideEvt.value);
		   });
		   
	 });
	 
	 
	 $(function(){
		      
		   $("#sliderBus").slider();
		   $("#sliderBus").on("slide", function(slideEvt) {
		   	$("#BusSliderVal").text(slideEvt.value);
		   });
		   
	 });
	 
	 
	 $(function(){
		      
		   $("#sliderPInteres").slider();
		   $("#sliderPInteres").on("slide", function(slideEvt) {
		   	$("#PInteresSliderVal").text(slideEvt.value);
		   });
		   
	 });
 });
