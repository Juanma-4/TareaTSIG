var map = null;
var mapModal = null;
var marca = null;
var geocoder = new google.maps.Geocoder();
var location;
var auxForm;
var auxLat;
var auxLng;
var circulo = null;
var circulos = new Array();


function mapa(idmap, latitud,longitud,zoom)
{
	var miLatlng = new google.maps.LatLng(latitud, longitud);
    var misOpciones = {
        zoom: zoom,
        center: miLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    map = new google.maps.Map(document.getElementById(idmap), misOpciones);
}

function mapaModal(idmap, latitud,longitud,zoom)
{
	var miLatlng = new google.maps.LatLng(latitud, longitud);
    var misOpciones = {
        zoom: zoom,
        center: miLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    mapModal = new google.maps.Map(document.getElementById(idmap), misOpciones);
}

function mapaConRadio(idmap, idlat, idlng, idradio, latitud, longitud, zoom) {
    var miLatlng = new google.maps.LatLng(latitud, longitud);
    var misOpciones = {
        zoom: zoom,
        center: miLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    map = new google.maps.Map(document.getElementById(idmap), misOpciones);
    
    marca = new google.maps.Marker({
        position: miLatlng,
        draggable: true,
        map: map,
        title: "Centro del evento"
    });
    
    google.maps.event.addListener(marca, 'dragstart', function (evt) {
        document.getElementById(idlat).value = evt.latLng.lat();
        document.getElementById(idlng).value = evt.latLng.lng();
    });

    google.maps.event.addListener(marca, 'dragend', function (evt) {
        document.getElementById(idlat).value = evt.latLng.lat();
        document.getElementById(idlng).value = evt.latLng.lng();
        agregarCirculo(idlat,idlng,document.getElementById(idradio).value);
    });
}

function agregarCirculo(idlat, idlng, radio)
{

	if(circulo != null)
		circulo.setMap(null);
	var miLatlng = new google.maps.LatLng(document.getElementById(idlat).value, document.getElementById(idlng).value);
	var opciones = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      center: miLatlng,
      radius: radio * 1000
    };
    circulo = new google.maps.Circle(opciones);
}

function agregarCirculos(marcador,radio)
{
	var miLatlng = marcador.getPosition();
	var opciones = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      center: miLatlng,
      radius: radio * 1000
    };
    circulos.push(new google.maps.Circle(opciones));
}

function buscarDir(lat, lng, busqueda) {
    auxLat = lat;
    auxLng = lng;
    // Inicia el proceso de georreferenciación (asíncrono)  
    processGeocoding(busqueda, addMarkers);
    //alert("xD");
}

function processGeocoding(location, callback) {
    // Propiedades de la georreferenciación  
    var request = {
        address: location
    };
    // Invocación a la georreferenciación (proceso asíncrono)
    geocoder.geocode(request, function (results, status) {
        //alert("xD");
        /* 
        * OK 
        * ZERO_RESULTS 
        * OVER_QUERY_LIMIT 
        * REQUEST_DENIED 
        * INVALID_REQUEST 
        */
        // Notifica al usuario el resultado obtenido  
        //document.getElementById('message').innerHTML = "Respuesta obtenida: " + status;
        // En caso de terminarse exitosamente el proyecto ...  
        if (status == google.maps.GeocoderStatus.OK) {
            // Invoca la función de callback  
            callback(results);
            // Retorna los resultados obtenidos  
            return results;
        }
        // En caso de error retorna el estado  
        return status;
    });
}

function addMarkers(geocodes) {
    for (var i = 0; i < geocodes.length; i++) {
        // Centra el mapa en la nueva ubicación  
        map.setCenter(geocodes[i].geometry.location);

        // Establece la ubicación del marcador  
        marca.setPosition(geocodes[i].geometry.location);

        auxLat.value = geocodes[i].geometry.location.lat();
        auxLng.value = geocodes[i].geometry.location.lng();

        // Asocia el evento de clic sobre el marcador con el despliegue  
        // de la ventana de información  
        google.maps.event.addListener(marker, 'click', function (event) {
            //infoWindow.open(map, marca);
            formulario.latitud.value = evt.latLng.lat();
            formulario.longitud.value = evt.latLng.lng();
        });
    }
}

function agregarMarca(latitud, longitud, texto) {
    return new google.maps.Marker({
        position: new google.maps.LatLng(latitud, longitud),
        draggable: false,
        map: map,
        title: texto
    });
}

function agregarVentana(contenido, marca)
{
	var infowindow = new google.maps.InfoWindow({
	    content: contenido
	});
	
	google.maps.event.addListener(marca, 'click', function() {
		infowindow.open(map,marca);
	});
}


// devuelve metros, METROS
function calcularDistancia(latitudA, longitudA, latitudB, longitudB) {
    var latLngA = new google.maps.LatLng(latitudA, longitudA);
    var latLngB = new google.maps.LatLng(latitudB, longitudB);
    var metros = google.maps.geometry.spherical.computeDistanceBetween(latLngA, latLngB);
    return metros;
}