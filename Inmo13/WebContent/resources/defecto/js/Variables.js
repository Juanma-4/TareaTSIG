
/*
//Para la transformación
Proj4js.defs["EPSG:32721"] = "+proj=utm +zone=21 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
 */

var miEPSG="EPSG:32721";
var gEPSG="EPSG:900913";

/*
var origen = new Proj4js.Proj(gEPSG);
var destino = new Proj4js.Proj(miEPSG);	
*/

var miLongitud="-6257927.540382689";
var miLatitud="-4147927.540382689";

var miZoom = 10;

var urlWMS="http://localhost:8080/geoserver/wms";
var urlWFS="http://localhost:8080/geoserver/wfs";
var urlGeoServer = "http://www.opengeospatial.net/sige";
   