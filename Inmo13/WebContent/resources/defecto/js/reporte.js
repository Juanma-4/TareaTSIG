var apreto = false;
var punto;
var gid;
var saveStrategy;

//Para popUp
var selectControl;
var selectedFeature;

var map;

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
	
		
		
////////Creacion de los estilos y reglas para las propiedades publicas, reservadas y privadas /////////

		var estiloPropPublica = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
							filter : new OpenLayers.Filter.Comparison({
								type : OpenLayers.Filter.Comparison.EQUAL_TO,
								property : "tipoestado" ,
								value : "Publica" ,
							}),
							symbolizer : {
								pointRadius : 7,
								fillColor : "green",
								fillOpacity : 0.5,
								strokeColor : "black"
							}
					})]
				})
			});
		
		var estiloPropPrivada = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "tipoestado" ,
					value : "Privada" ,
					}),
					symbolizer : {
						pointRadius : 7,
						fillColor : "red",
						fillOpacity : 0.5,
						strokeColor : "black"
					}
				}) ]
			})
		});
		
		var estiloPropReservadas = new OpenLayers.StyleMap({
			"default" : new OpenLayers.Style(null, {
				rules : [ new OpenLayers.Rule({
					filter : new OpenLayers.Filter.Comparison({
					type : OpenLayers.Filter.Comparison.EQUAL_TO,
					property : "tipoestado" ,
					value : "Reservada" ,
					}),
					symbolizer : {
						pointRadius : 7,
						fillColor : "blue",
						fillOpacity : 0.5,
						strokeColor : "black"
					}
				}) ]
			})
		});
		
		
		
		
/////////// Usuo de WFS para traer las propiedades con las reglas y estilos definidos arriba ////////
		
		
		var PropiedadesPublicas = new OpenLayers.Layer.Vector("Propiedades Publicas", {
			strategies : [ new OpenLayers.Strategy.Fixed() ],
			styleMap: estiloPropPublica,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
	
		var PropiedadesPrivadas = new OpenLayers.Layer.Vector("Propiedades Privadas", {
			strategies : [new OpenLayers.Strategy.Fixed() ],
			styleMap: estiloPropPrivada,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
			
		var PropiedadesReservadas = new OpenLayers.Layer.Vector("Propiedades Reservadas", {
			strategies : [new OpenLayers.Strategy.Fixed() ],
			styleMap: estiloPropReservadas,
			protocol : new OpenLayers.Protocol.WFS({
				version : "1.1.0",
				url : urlWFS,
				featureType : "propiedad",
				featureNS : urlGeoServer,
				geometryName : "geom",
				srsName: gEPSG,
				
			}),
		});	
		
	////// agregó las 3 capas de propiedades hechas antes ////////////////
	
		PropiedadesReservadas.setVisibility(false);
		PropiedadesPrivadas.setVisibility(false);
		
		map.addLayers([ google_maps, gphy,PropiedadesPublicas,PropiedadesPrivadas,PropiedadesReservadas]);

		/*	Control para la selección de popUps */
		selectControl = new OpenLayers.Control.SelectFeature([PropiedadesPublicas,PropiedadesPrivadas,PropiedadesReservadas],
				    {
				        onSelect: onPopupFeatureSelect,
				        onUnselect: onPopupFeatureUnselect,
				        //hover:true,
				        //highlightOnly: false // en true solo se agranda, es solo para eso
				    });
		
	    map.addControl(selectControl);
	    selectControl.activate();
		
	    
		map.zoomToExtent(limites);		
		
	
		
		/// PARA CENTRAR EN MONTEVIDEO
    	map.setCenter(new OpenLayers.LonLat(miLongitud, miLatitud).transform(
    			new OpenLayers.Projection(miEPSG),  map.getProjectionObject()), miZoom + 3);
		
  		
};


function onPopupClose(evt) {
    selectControl.unselect(selectedFeature);
}

function onPopupFeatureSelect(feature) {
    selectedFeature = feature;
    popup = new OpenLayers.Popup.FramedCloud(
    		"chicken",
	        feature.geometry.getBounds().getCenterLonLat(),
	        null,
	        
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
	    '</div>'+
	        '</div>'    
	       
	      //  '<Button value="Aceptar" action="#{usuarioMB.registroUsuario()}"' +'</Button>' //Muy loco ! jaja 
	      //  '<a  href="http://localhost:8080/Inmo13/faces/InfoPropiedad.xhtml?fid='+ feature.data.fid + '" "form-signin-heading" style="color: rgb(0,255,0)">Ver</a>'+
	       
	       
	      
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
