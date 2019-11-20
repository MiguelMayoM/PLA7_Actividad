package pla7_actividad;

import java.util.List;
import java.util.Scanner;

import javax.persistence.Query;

import org.hibernate.Session;

import pla7_actividad.entidades.Alumno;
import pla7_actividad.entidades.Modulo;
import pla7_actividad.entidades.Profesor;

public class Main {
	static Scanner scnEntrada = new Scanner(System.in);
	
	public static void main (String[] args) {

		Session s;
		
		s = HibernateSesion.Nueva();
		/* Obviamente, podríamos haber recuperado material de la práctica anterior de
		 * CRUD, como el menú, y actualizarlo, pero en principio no aportaría nada
		 * nuevo, así que centraremos esfuerzos en trabajar otros aspectos específicos
		 * de este PLA dedicaddo a Hibernate */
		try {		
			/* Después de la carga de la BD (durante la cual, cuando se crea la BD la
			 * vez primera saldrán unos warnings de MySQL), muestro la información
			 * usando los métodos toString de las entidades */
			System.out.println("\nConfiguración inicial de la base de datos\n"
											 + "=========================================");
			imprimeEntidades(s);

			System.out.println("Presione Enter para continuar");
			String nada = scnEntrada.nextLine();
			
		/*+-----------------------+
		 	| INSERTAR 2 PROFESORES |
		 	+-----------------------+*/
			Profesor profesor1 = new Profesor("00000000T","Profesor A","profesorA@email.com");
			Profesor profesor2 = new Profesor("00000023T","Profesor B","profesorB@email.com");
			//s.beginTransaction();
			//s.save(profesor1);
			//s.save(profesor2);
			//s.getTransaction().commit();
		/*+--------------------------------------------+
	 		| INSERTAR 2 MÓDULOS, UNO PARA CADA PROFESOR |
	 		+--------------------------------------------+*/
			Modulo modulo1 = new Modulo("Física");
			Modulo modulo2 = new Modulo("Música");

			/* Implementamos la relación Modulo N:1 Profesor */
			profesor1.addModulo(modulo1);
			profesor2.addModulo(modulo2);
			//modulo2.setProfesor(profesor2);
			/* Si hubiera ejecutado la línea comentada anterior, no se actualizaría la
			 * otra parte de la relación y, al imprimir por pantalla, veríamos que el
			 * profesor2 no tenía ningún modulo asignado. Esto se arregla si cerramos
			 * y volvemos a conectar o de otra forma que luego expondré, con un "helper".
			 * Y no veo que cambie la cosa según las diferentes combinaciones que he
			 * probado de realizar los .save() o los .commit(), incluso leyendo la
			 * entidad de la BD ya sea con una query de JPL o con s.get(...)*/
			/* Pero bueno, al ir probando los ejercicios del pdf, esto mismo también
			 * ocurría en la pág.4 del "Relaciones en Hiberbate.pdf", cuando hace un
			 * s.get() de la categoria 2 ("Verdura")* y crea un Producto "tomate",
			 * setteándole la categoria de Verduras con "tomate.setCategoria(cat)"
			 * y no por el lado contrario de la relación con cat.add(tomate) */
			
			s.beginTransaction();
			s.save(profesor1);
			s.save(profesor2);
			s.save(modulo1);
			s.save(modulo2);
			s.getTransaction().commit();
			
			/*+------------------------------------------------+
	 			| INSERTAR 3 ALUMNOS, 2 EN MÓDULO1, 1 EN MÓDULO2 |
	 			+------------------------------------------------+*/
			Alumno alumno1 = new Alumno("alumno A", "alumnoA@email.com");
			Alumno alumno2 = new Alumno("alumno B", "alumnoB@email.com");
			Alumno alumno3 = new Alumno("alumno C", "alumnoC@email.com");

			/* Realizamos las operaciones de las dos maneras posibles, según el lado de
			 * la relación que escoja, cosa permitida por su bidireccionalidad. Aquí
			 * sí puedo hacer esto que no hice antes para la 1:N (Profesor:Modulo),
			 * sin miedo a que no se actualicen ambos lados de la relación, por la
			 * implementación que he usado con un "helper" en la parte @mappedBy, si
			 * bien en el pdf no se hace así, sino repitiendo el mismo código de toda
			 * la configuración en las dos partes, como se puede ver en las pp.8 y 9:
			 * @JoinTable( ..., joinColumns = @JoinColumn(... y  inverseJoinColumns = @JoinColumn(...,
			 * 
			 * Este "helper", no obstante, sí que es usado en el método .addProductos()
			 * de la p.3*/
			alumno1.addModulo(modulo1);
			modulo1.addAlumno(alumno2);
			alumno3.addModulo(modulo2);
			/* Así, sin esos "helpers", tendría que actualizar ambos lados de la
			 * relación con:*/
			//modulo1.addAlumno(alumno1);
			//alumno2.addModulo(modulo1);
			//modulo2.addAlumno(alumno3);

			s.beginTransaction();
			s.save(alumno1);
			s.save(alumno2);
			s.save(alumno3);
			s.getTransaction().commit();

			/* Y mostramos nuevamente la info de las entidades de la BD*/
			System.out.println("Datos requeridos en el PLA7 introducidos\n"
											 + "========================================");
			imprimeEntidades(s);

			System.out.println("Presione Enter para continuar");
			nada = scnEntrada.nextLine();
			
			/*+-------------------+
	 			| OTRAS PRUEBAS (1) |
	 			+-------------------+*/
			/* Voy a realizar sólo un par de pruebas más. La primera de ella será
			 * introducir un Profesor y Modulo nuevos para comentar los problemas que
			 * antes reseñé al implementar la relación 1:N desde el lado del Modulo
			 * sin realizar la implementación del lado contrario */
			Profesor profesor3 = new Profesor("00000001R", "Profesor M", "profesorM@email.com");
			Profesor profesor4 = new Profesor("00000024R", "Profesor P", "profesorP@email.com");
			Modulo modulo3 = new Modulo("Inglés");
			Modulo modulo4 = new Modulo("Alemán");
			
			/* Procedemos desde el lado de Modulo, lo cual va a provocar que el modulo
			 * no se vea al imprimir profesores */
			modulo3.setProfesor(profesor3);
			/* Sin embargo, por analogía a la parte @MappedBy de la relación N:N, he
			 * definido un método addProfesor() en la part 1:, aunque sólo haya un
			 * profesor, y que contiene un "helper" profesor.addModulo(this) que
			 * actualiza la parte contraria de la relación, mostrándose ahora sí el
			 * modulo asociado al profesor si creo la relación por el lado del modulo*/
			modulo4.addProfesor(profesor4);
			
			s.beginTransaction();
			s.save(profesor3);
			s.save(profesor4);
			s.save(modulo3);
			s.save(modulo4);
			s.getTransaction().commit();

			/* Y mostramos nuevamente la info de las entidades de la BD. Se puede
			 * observar una Entidad Modulo con un idmodulo = 16, la cual tiene asociado
			 * al profesor con nombre "profesor M". Sin embargo, en la Entidad Profesor,
			 * este último, con idprofesor = 9, no parece tener ningún Modulo asociado...*/
			System.out.println("Profesor añadido desde Modulo de 2 formas\n"
											 + "=========================================");
			imprimeEntidades(s);

			System.out.println("Presione Enter para continuar");
			nada = scnEntrada.nextLine();
			
			/*+-------------------+
 				| OTRAS PRUEBAS (2) |
 				+-------------------+*/
			/* Para acabar, vamos a eliminar una relación de un lado :N, ya que he creado
			 * unos métodos .remove(). Ojo que no borran la entidad de la BD, sino sólo
			 * rompen la relación. Para ello, elijo una entidad que tenga relacionadas
			 * varias de otro tipo... e.g. el profesor con idprofesor = 1 y ahora
			 * decido que ya no dé la clase de PHP (elijo esta porque sólo hay una,
			 * para no liar)*/
			/* El profesor que da esta asignatura, por su id */
			Profesor profe = s.get(Profesor.class, 1);			
			/* Para seleccionar la asignatura PHP, lo hago con JPQL. Aunque también, si
			 * sé que quiero quitarla de un profesor determinado, podría mirar su lista
			 * de modulos y elegir de allí... Como he dicho, he elegido una asignatura
			 * que no está repetida */
			Query q = s.createQuery("from Modulo where nombre =:nombre ")
								 .setParameter("nombre", "dd");
			Modulo dd = (Modulo) q.getSingleResult();
			profe.delModulo(dd);
			
			s.beginTransaction();
			s.save(dd);
			s.getTransaction().commit();
			
			System.out.println("Eliminado Modulo \"dd\" del profesor con idprofesor = 1\n"
					 						 + "=====================================================");
			imprimeEntidades(s);

			System.out.println("Fin del programa");			
			
			s.close();

		} finally {
			s.getSessionFactory().close();
		}
	}
	
	static void imprimeEntidades(Session s) {
		String strProfesores = "";
		String strModulos = "";
		String strAlumnos = "";
		
		/* Usamos JPQL */		
		List<Profesor> listaProfesores = s.createQuery("from Profesor p", Profesor.class).getResultList();
		for (Profesor o : listaProfesores) {
			strProfesores += o.toString() + "\n"; 
		}
		
		List<Modulo> listaModulos = s.createQuery("from Modulo m", Modulo.class).getResultList();
		for (Modulo o : listaModulos) {
			strModulos += o.toString() + "\n"; 
		}
		List<Alumno> listaAlumnos = s.createQuery("from Alumno a", Alumno.class).getResultList();
		for (Alumno o : listaAlumnos) {
			strAlumnos += o.toString() + "\n"; 
		}
		
		System.out.println("\n"
				+ "Profesor\n"
				+ "--------\n"
				+ strProfesores + "\n\n"	
				
				+ "Modulo\n"
				+ "------\n"
				+ strModulos + "\n\n"
				
				
				+ "Alumno\n"
				+ "------\n"
				+ strAlumnos + "\n"				

		);		
	}
}