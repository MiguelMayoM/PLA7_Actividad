package pla7_actividad;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSesion {
	static final String nombrePaquete = "pla7_actividad.entidades";
	
	public static Session Nueva() {
		Session sesion = null;
		try {
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
			//.addAnnotatedClass(Alumno.class).addAnnotatedClass(Modulo.class).addAnnotatedClass(Profesor.class);
			/* Añadir las clases así sustituye el hacerlo con los xml/hbm como se 
			 * hacía antes. No obstante, lo bueno de hacerlo con XMLs es que puedes
			 * modificarlos sin tener que volver a compilar/build */
			/* Voy a leer las clases del paquete y cargarlas automáticamente. Luego,
			 * en el Main, voy a tener que importarlas y voy a nombrarlas al utilizarlas,
			 * pero bueno... */
			for (Class<?> clase : ClasesPaquete(nombrePaquete)) {
			  configuration.addAnnotatedClass(clase);
			}
			
			StandardServiceRegistryBuilder builder= new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties());
		
			SessionFactory factory = configuration.buildSessionFactory(builder.build());
			/* Parece que .getCurrentSession se cierra después de hacer un commit() y
			 * yo quiero que siga abierta para ir haciendo transacciones y mirando o
			 * mostrando los cambios parciales en la BD para ver la operatividad de los
			 * mismos*/
			sesion = factory.openSession(); //.getCurrentSession();
		} catch(Exception e) {
			System.err.println("\nNo se ha podido crear la sesión de Hibernate:");
			e.printStackTrace();
		}
		return sesion;
	}
	
	private static List<Class<?>> ClasesPaquete(String nombrePaquete) throws Exception {
		List<Class<?>> clases = new ArrayList<Class<?>>();
		try{
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			/* Sé que sólo estarán en esta carpeta "entidades"; es decir, que sólo
			 * habrá un recurso*/
			URL recursoURL = cl.getResource(nombrePaquete.replace(".","/"));
			File directorio = new File(recursoURL.getFile());
			/* Sé que todo lo que hay en este directorio son clases, no he de buscar más */
			System.out.println("Anotando entidades:");
			for (File archivo : directorio.listFiles()) {
				/* He de tomar todo el nombre, paquete incluído. En el addAnnotatedClass
				 * sólo se pone el nombre porque el path está en el import*/
				String nombreClase = nombrePaquete + "." + archivo.getName().replace(".class", "");
				System.out.println(nombreClase);
				clases.add(Class.forName(nombreClase));
			}
			
		}catch(Exception e){
			System.err.println("\nHubo un error obteniendo las entidades.");
			e.printStackTrace();
			throw e;
			
		}
		return clases;	
	}	
}