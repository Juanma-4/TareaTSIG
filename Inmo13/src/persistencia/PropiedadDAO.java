package persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import wrappers.WrapperPropiedadFiltrada;
import wrappers.WrapperPuntoInteres;
import dominio.Propiedad;
import dominio.Usuario;

@Stateless
public class PropiedadDAO implements IPropiedadDAO{

	@PersistenceContext(unitName="Inmo")
    private EntityManager em;	
	
	public List<String> listarIds(String fid) {
		List<String> ids = null;
		try {
			ids = em.createQuery("Select p.fid From Propiedad p",String.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ids;
	}
	
	public boolean guardarPropiedad(Propiedad propiedad) {

		boolean guardo = false;
		
		try {
			em.persist(propiedad);
			guardo = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guardo;

	}


	public boolean modificarPropiedad(String calle, double precio,
			Integer cantDorm, Integer cantBanio, double metrosCuadrados,
			boolean parrillero, boolean garage, String tipoPropiedad,
			String tipotransaccion, String tipoEstado, Integer numeroPuerta,
			String fid, String imagen, String piso, String usuario) {
		
		boolean modifico = false;
		try {
			Propiedad propiedad = (Propiedad) em.createQuery(
							"Select p From Propiedad p Where p.fid = '" + fid + "'").getSingleResult();
			
			propiedad.setCalle(calle);
			propiedad.setPrecio(precio);
			propiedad.setCantDorm(cantDorm);
			propiedad.setCantBanio(cantBanio);
			propiedad.setMetrosCuadrados(metrosCuadrados);
			propiedad.setParrillero(parrillero);
			propiedad.setGarage(garage);
			propiedad.setTipoPropiedad(tipoPropiedad);
			propiedad.setTipotransaccion(tipotransaccion);
			propiedad.setTipoEstado(tipoEstado);
			propiedad.setNumeroPuerta(numeroPuerta);
			propiedad.setImagen(imagen);
			propiedad.setPiso(piso);
			//propiedad.setUsuario(usuario);
			//propiedad.setFid(fid);
			em.persist(propiedad);
//			em.refresh(propiedad);
			modifico = true;
		} catch (Exception e) {

			e.printStackTrace();

		}
		return modifico;
	}
	

	/**
	 * 0, tipopropiedad
	 * 2, minimo
	 * 3, maximo
	 * 4, cantbanio
	 * 5, cantdorm
	 * 6, metroscuadrados
	 * 7, barrio
	 * 8, parrillero
	 * 9, garage
	 * 10, distanciaMar
	 * 11, distanciaParada
	 * 12, distanciaPInteres
	 */
	@SuppressWarnings("unchecked")
	public List<WrapperPropiedadFiltrada> listarPropiedades(ArrayList<String> filtros) {
			
		System.out.println(filtros);
		
		// Filtros 
		String tipoPropiedad = filtros.get(0);
		String tipoTransaccion = filtros.get(1);
		Integer cantBanio = 0;
		Integer cantDorm = 0;
		
		
		if(!filtros.get(4).equals("NaN")){
		cantBanio = Integer.parseInt(filtros.get(4));
		}
		if(!filtros.get(5).equals("NaN")){
			cantDorm = Integer.parseInt(filtros.get(5));
		}
			
			
	    String barrio = filtros.get(7);
		Boolean parrillero = Boolean.parseBoolean(filtros.get(8));
		Boolean garage = Boolean.parseBoolean(filtros.get(9));
				
		Integer distanciaMar = Integer.parseInt(filtros.get(10));
		Integer distanciaParada = Integer.parseInt(filtros.get(11));
		Integer distanciaPuntoInteres = Integer.parseInt(filtros.get(12));
		
		
		String select = null;
		String from = null;
		String where = null;
		String sql = null;
		
		List<WrapperPropiedadFiltrada> comunes = null;
		List<WrapperPropiedadFiltrada> conBarrios = null;
		List<WrapperPropiedadFiltrada> conDistanciaMar = null;
		List<WrapperPropiedadFiltrada> conDistanciaParadas = null;
		List<WrapperPropiedadFiltrada> conDistanciaParadasDestino = null;
		List<WrapperPropiedadFiltrada> resultado = null;
		
		//// Para los puntos de Interes ////
		
		List<WrapperPropiedadFiltrada> puntoInteresComercial = null;
		List<WrapperPropiedadFiltrada> puntoInteresEscuela = null;
		List<WrapperPropiedadFiltrada> puntoInteresLiceo = null;
		List<WrapperPropiedadFiltrada> puntoInteresPlaza = null;
		
		try {		
						
			// Primero hago los filtros básicos.
			select = "SELECT DISTINCT propiedad.precio,propiedad.cantDorm,propiedad.cantBanio,propiedad.metrosCuadrados,propiedad.parrillero, "
					+ "propiedad.garage,propiedad.tipoPropiedad,propiedad.tipotransaccion,propiedad.tipoestado,propiedad.numeroPuerta, "
					+ "propiedad.calle,propiedad.fid,propiedad.imagen,propiedad.piso, propiedad.usuario, "
					+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud ";
			
			from = "FROM propiedad ";
			
			if(!tipoPropiedad.equals("Terreno")){
			where = "WHERE propiedad.tipopropiedad = '"+tipoPropiedad+"' AND propiedad.tipotransaccion = '"+tipoTransaccion+"'"
					+ " AND propiedad.cantbanio = "+cantBanio+" AND propiedad.cantdorm = "+cantDorm+" AND propiedad.parrillero = "+parrillero 
					+ " AND propiedad.garage = "+garage;
			}else{
				where = "WHERE propiedad.tipopropiedad = '"+tipoPropiedad+"' AND propiedad.tipotransaccion = '"+tipoTransaccion+"'";	
			}
			
			if(!filtros.get(2).equalsIgnoreCase("NaN")){
				Double precioMinimo = Double.parseDouble(filtros.get(2));
				where += " AND propiedad.precio >= "+precioMinimo;
			}
			
			if(!filtros.get(3).equalsIgnoreCase("NaN")){
				Double precioMaximo = Double.parseDouble(filtros.get(3));
				where += " AND propiedad.precio <= "+precioMaximo;
			}
			
			if(!filtros.get(6).equalsIgnoreCase("NaN")){
				Double metrosCuadrados = Double.parseDouble(filtros.get(6));
				where += " AND propiedad.metroscuadrados = "+metrosCuadrados;
			}
			
			where += " AND propiedad.tipoestado IN ('Publica','Reservada')";
			
			
			sql = select+from+where;
			comunes = em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList();
			resultado = new ArrayList<WrapperPropiedadFiltrada>(comunes);
			
			// Si me trajo algo los filtros comunes, se sigue filtrando con los filtros espaciales.
			if(resultado.size() != 0){
			
				
				if(!barrio.equalsIgnoreCase("TODOS")){					
					sql = armarCondicionBarrios(select,from,where,barrio,resultado);
					conBarrios = em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList();
					
					resultado = new ArrayList<WrapperPropiedadFiltrada>(conBarrios);				
					
				}						
				
			}
			
			// si devuelve algo con los filtros comunes y quiso filtrar por barrio
			if(resultado.size() != 0){
				
				if(distanciaMar != 0){
					
					distanciaMar = (distanciaMar*1000); // para expresarlo en km.			
					sql = armarCondicionDistanciaMar(select,from,where,distanciaMar,resultado);	
					conDistanciaMar = em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList();
					
					resultado = new ArrayList<WrapperPropiedadFiltrada>(conDistanciaMar);
						
				}
			}
				
			if(resultado.size() != 0){	
				if(distanciaPuntoInteres!= 0){
					
					sql= armarCondicionPuntosInteres(select,from,where,"serv_comerciales",distanciaPuntoInteres,resultado);
					puntoInteresComercial = new ArrayList<WrapperPropiedadFiltrada>(em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList());
					
					sql= armarCondicionPuntosInteres(select,from,where,"deportes",distanciaPuntoInteres,resultado);
					puntoInteresPlaza = new ArrayList<WrapperPropiedadFiltrada>(em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList());
					
					sql= armarCondicionPuntosInteres(select,from,where,"edu_primaria",distanciaPuntoInteres,resultado);
					puntoInteresEscuela = new ArrayList<WrapperPropiedadFiltrada>(em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList());
					
					sql= armarCondicionPuntosInteres(select,from,where,"edu_secundaria",distanciaPuntoInteres,resultado);
					puntoInteresLiceo = new ArrayList<WrapperPropiedadFiltrada>(em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList());
							
					//limpio lista
					resultado.clear();
					
					resultado = new ArrayList<WrapperPropiedadFiltrada>(puntoInteresComercial);
										
					if(puntoInteresPlaza.size() != 0){
						
						if(resultado.size() != 0){
						
							for(WrapperPropiedadFiltrada plaza : puntoInteresPlaza){
								
								if(!contieneWrapperPuntoInteres(resultado,plaza))
									resultado.add(plaza);
								
									
							}
							
						}else{
								resultado = puntoInteresPlaza;
						}
					}
					


					if(puntoInteresEscuela.size() != 0){
											
						if(resultado.size() != 0){
						
							for(WrapperPropiedadFiltrada escuela : puntoInteresEscuela){
								
								if(!contieneWrapperPuntoInteres(resultado,escuela))
									resultado.add(escuela);								
									
							}
							
						}else{
								resultado = puntoInteresEscuela;
						}
					}
					
					if(puntoInteresLiceo.size() != 0){
						
						if(resultado.size() != 0){
						
							for(WrapperPropiedadFiltrada liceo : puntoInteresLiceo){
								
								if(!contieneWrapperPuntoInteres(resultado,liceo))
									resultado.add(liceo);
								
									
							}
							
						}else{
								resultado = puntoInteresLiceo;
						}
					}
					
						
				}
			}
			
			
			if(resultado.size() != 0){
				
				if(distanciaParada!= 0){
					conDistanciaParadasDestino = new ArrayList<WrapperPropiedadFiltrada>();
					
					if((!filtros.get(13).equalsIgnoreCase("")) && (!filtros.get(14).equalsIgnoreCase(""))){ //Si quiso ver filtro mas avanzado
						String calleDestino = filtros.get(13);
						String esquinaDestino = filtros.get(14);
						
						sql = armarCondicionLineas(select,from,where,calleDestino,esquinaDestino);
						List<String> paradas = em.createNativeQuery(sql,String.class).getResultList();
						
						if(paradas.size() != 0){
							sql = armarCondicionDistanciaParadaConDestino(select,from,where,paradas,distanciaParada,resultado);
							conDistanciaParadasDestino = em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList();

							resultado = new ArrayList<WrapperPropiedadFiltrada>(conDistanciaParadasDestino);
							
						}else{							
							resultado = conDistanciaParadasDestino;
						}
						
						
					}else{ 	// Filtro normal de parada				
						
							sql = armarCondicionDistanciaParada(select, from, where,distanciaParada, resultado);
							conDistanciaParadas =  em.createNativeQuery(sql,WrapperPropiedadFiltrada.class).getResultList();
							
							resultado = new ArrayList<WrapperPropiedadFiltrada>(conDistanciaParadas);
							
					}
			    }
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	return resultado;
		
		
	}		
	
	
	private String armarCondicionBarrios(String select, String from, String where, String barrio, List<WrapperPropiedadFiltrada> comunes) {
	

		from += ",barrios ";
		where = armarCondicionesIn(comunes);
		where += " AND ST_CONTAINS(barrios.geom,propiedad.geom) AND barrios.barrio = '"+barrio+"'"; 
		
		return (select+from+where);
	}


	private String armarCondicionesIn(List<WrapperPropiedadFiltrada> filtradas) {
		String resultado = "WHERE propiedad.fid IN ('"+filtradas.get(0).getFid()+"'";
						
		for(int i = 1; i<filtradas.size() ;i++){
			resultado += ",'"+filtradas.get(i).getFid()+"'";
		}	
		resultado += ")";
		
		return resultado;
	}

	private String armarCondicionDistanciaMar(String select, String from, String where, Integer distanciaMar, List<WrapperPropiedadFiltrada> filtradas) {

		from = " FROM propiedad, borde_rambla ";
		
		where = armarCondicionesIn(filtradas);							
		where += " AND ST_Intersects(ST_Buffer(borde_rambla.geom,"+distanciaMar+"),propiedad.geom)";
		
		return (select+from+where);
	}
	
	private String armarCondicionPuntosInteres(String select, String from,String where,String pi, Integer distanciaPuntoInteres, List<WrapperPropiedadFiltrada> resultado) {
		
		select = "SELECT DISTINCT propiedad.precio,propiedad.cantDorm,propiedad.cantBanio, "
				+ "propiedad.metrosCuadrados,propiedad.parrillero, propiedad.garage,propiedad.tipoPropiedad, "
				+ "propiedad.tipotransaccion,propiedad.tipoestado,propiedad.numeroPuerta, propiedad.calle, "
				+ "propiedad.fid,propiedad.imagen,propiedad.piso, propiedad.usuario, ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , "
				+ "ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud ";
		
		from = " FROM propiedad, "+pi+" ";
		
		where = armarCondicionesIn(resultado);
		where += " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"), "+pi+".geom)";

		return (select+from+where);
		
	}
	
	public boolean contieneWrapperPuntoInteres(List<WrapperPropiedadFiltrada> lista,WrapperPropiedadFiltrada prop) {
		   boolean contiene = false;
		   
			for(WrapperPropiedadFiltrada p : lista){
				
				if(p.getFid().equals(prop.getFid()))
					contiene = true;
			}
			
			return contiene;
			
    }
		
//	private String armarCondicionPuntosInteres(String select, String from,	String where, Integer distanciaPuntoInteres,List<WrapperPropiedadFiltrada> filtradas) {
//		select = "SELECT DISTINCT propiedad.precio,propiedad.cantDorm,propiedad.cantBanio,propiedad.metrosCuadrados,propiedad.parrillero, "
//				+ "propiedad.garage,propiedad.tipoPropiedad,propiedad.tipotransaccion,propiedad.tipoestado,propiedad.numeroPuerta, "
//				+ "propiedad.calle,propiedad.fid,propiedad.imagen,propiedad.piso, propiedad.usuario, "
//				+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud "; 
//		from = " FROM propiedad, serv_comerciales,edu_primaria, edu_secundaria, deportes ";		
//		where = armarCondicionesIn(filtradas);							
//		where += " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),serv_comerciales.geom)" 
//					 + " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),edu_primaria.geom)"
//					 + " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),edu_secundaria.geom)"
//					 + " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),deportes.geom)";
//		return (select+from+where);
//	}

	private String armarCondicionDistanciaParada(String select, String from,String where, Integer distanciaParada, List<WrapperPropiedadFiltrada> resultado) {
			
			select = "SELECT DISTINCT propiedad.precio,propiedad.cantDorm,propiedad.cantBanio,propiedad.metrosCuadrados,propiedad.parrillero, "
					+ "propiedad.garage,propiedad.tipoPropiedad,propiedad.tipotransaccion,propiedad.tipoestado,propiedad.numeroPuerta, "
					+ "propiedad.calle,propiedad.fid,propiedad.imagen,propiedad.piso, propiedad.usuario, "
					+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud "; 
			from = " FROM propiedad, paradas ";		
			where = armarCondicionesIn(resultado);							
			where += " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaParada+"),paradas.geom)";
			
			return (select+from+where);
			
	}
	
	private String armarCondicionLineas(String select, String from,String where, String calleDestino, String esquinaDestino) {
			select = "SELECT DISTINCT paradas.desc_linea "; 
			from = " FROM propiedad, paradas ";		
			where += " AND paradas.calle = '"+ calleDestino +"' AND paradas.esquina = '" + esquinaDestino + "' OR "
					+ "paradas.calle = '"+ esquinaDestino +"' AND paradas.esquina = '" + calleDestino + "'";		
			
			return (select+from+where);
	}
	private String armarCondicionDistanciaParadaConDestino(String select,
			String from, String where, List<String> paradas,
			Integer distanciaParada, List<WrapperPropiedadFiltrada> resultado) {

	
		select = "SELECT DISTINCT propiedad.precio,propiedad.cantDorm,propiedad.cantBanio,propiedad.metrosCuadrados,propiedad.parrillero, "
				+ "propiedad.garage,propiedad.tipoPropiedad,propiedad.tipotransaccion,propiedad.tipoestado,propiedad.numeroPuerta, "
				+ "propiedad.calle,propiedad.fid,propiedad.imagen,propiedad.piso, propiedad.usuario, "
				+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud ";
		from = " FROM propiedad, paradas ";		
		where = armarCondicionesIn(resultado);	
		where += armarCondicionesInParadas(paradas);
		where += " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaParada+"),paradas.geom)";
		
		return (select+from+where);
	}
	
	private String armarCondicionesInParadas(List<String> paradas) {
		String resultado = " AND paradas.desc_linea IN ('"+paradas.get(0)+"'";
		
		for(int i = 1; i<paradas.size() ;i++){
			resultado += ",'"+paradas.get(i)+"'";
		}	
		resultado += ")";
		
		return resultado;
	}	

	@SuppressWarnings("unchecked")
	@Override
	public List<WrapperPuntoInteres> listarPuntosInteres(String fid) {
		String sql = null;
		
		List<WrapperPuntoInteres> comerciales = null;
		List<WrapperPuntoInteres> plazas = null;
		List<WrapperPuntoInteres> escuelas = null;
		List<WrapperPuntoInteres> liceos = null;
		List<WrapperPuntoInteres> puntosInteres = new ArrayList<WrapperPuntoInteres>();
		try{
			
			System.out.println("ESTOY EN PROPIEDAD DAO, ANTES CONSULTA, FID ="+fid);
			
			sql = "SELECT serv_comerciales.nbre AS nombre, ST_X(ST_Transform(serv_comerciales.geom, 900913)) AS x , "
						+ "ST_Y(ST_Transform(serv_comerciales.geom, 900913)) AS y ,"  
						+ "trunc(ST_DISTANCE(propiedad.geom,serv_comerciales.geom)) AS distancia, 'comercial'::character varying(20) AS tipo"
						+ " FROM propiedad, serv_comerciales"
						+ " WHERE propiedad.fid = '"+fid.trim()+"' AND ST_INTERSECTS(ST_BUFFER(propiedad.geom,300),"
						+ " serv_comerciales.geom)";
			comerciales = em.createNativeQuery(sql,WrapperPuntoInteres.class).getResultList();
	
			sql= "SELECT deportes.nombre , ST_X(ST_Transform(deportes.geom, 900913)) AS x , ST_Y(ST_Transform(deportes.geom, 900913)) AS y ," 
				+ "trunc(ST_DISTANCE(propiedad.geom,deportes.geom)) AS distancia, 'plaza'::character varying(20) AS tipo"
				+ " FROM propiedad, deportes"
				+ " WHERE propiedad.fid = '"+fid.trim()+"' AND ST_INTERSECTS(ST_BUFFER(propiedad.geom,300),deportes.geom)";
			
			plazas = em.createNativeQuery(sql,WrapperPuntoInteres.class).getResultList();
			
			sql= "SELECT edu_primaria.nombre , ST_X(ST_Transform(edu_primaria.geom, 900913)) AS x , "
					+ "ST_Y(ST_Transform(edu_primaria.geom, 900913)) AS y ," 
					+ "trunc(ST_DISTANCE(propiedad.geom,edu_primaria.geom)) AS distancia, 'escuela'::character varying(20) AS tipo"
					+ " FROM propiedad, edu_primaria"
					+ " WHERE propiedad.fid = '"+fid.trim()+"' AND ST_INTERSECTS(ST_BUFFER(propiedad.geom,300),edu_primaria.geom)";
			
			escuelas = em.createNativeQuery(sql,WrapperPuntoInteres.class).getResultList();
			
			sql= "SELECT edu_secundaria.nombre , ST_X(ST_Transform(edu_secundaria.geom, 900913)) AS x , "
					+ "ST_Y(ST_Transform(edu_secundaria.geom, 900913)) AS y ," 
					+ "trunc(ST_DISTANCE(propiedad.geom,edu_secundaria.geom)) AS distancia, 'liceo'::character varying(20) AS tipo"
					+ " FROM propiedad, edu_secundaria"
					+ " WHERE propiedad.fid = '"+fid.trim()+"' AND ST_INTERSECTS(ST_BUFFER(propiedad.geom,300),edu_secundaria.geom)";
			
			liceos = em.createNativeQuery(sql,WrapperPuntoInteres.class).getResultList();
			
			
			for(WrapperPuntoInteres pi : comerciales){
				puntosInteres.add(pi);
			}
			for(WrapperPuntoInteres pi : plazas){
				puntosInteres.add(pi);
			}
			for(WrapperPuntoInteres pi : escuelas){
				puntosInteres.add(pi);
			}
			for(WrapperPuntoInteres pi : liceos){
				puntosInteres.add(pi);
			}
										
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return puntosInteres;
	}

	public ArrayList<String> listarCalles() {
		ArrayList<String> calles = null;
		
		try{	

			calles = new ArrayList<String>(em.createNativeQuery("SELECT DISTINCT calle FROM paradas ").getResultList());
		}catch(Exception e){
			e.printStackTrace();
		}
		return calles;
	}

	@Override
	public ArrayList<String> listarEsquinas() {
		ArrayList<String> esquinas = null;
		
		try{	

			esquinas = new ArrayList<String>(em.createNativeQuery("SELECT DISTINCT esquina FROM paradas ").getResultList());
		}catch(Exception e){
			e.printStackTrace();
		}
		return esquinas;
	}
}
