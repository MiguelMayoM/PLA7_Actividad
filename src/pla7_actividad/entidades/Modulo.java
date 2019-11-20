package pla7_actividad.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/* RELACIONES
	 Esta entidad está relacionada con las otras dos mediante relaciones:
 + Modulo N:1 Profesor
 + Modulo N:N Alumnos

 	El SQL de la tabla, incluído el Join de la relación PROFESOR 1:N MODULO):
	CREATE TABLE `modulos` (
	  `idmodulo` int(11) NOT NULL AUTO_INCREMENT,
	  `idprofesor` int(11) DEFAULT NULL, 			¡¡¡¡¡OJO QUE ESTA COLUMNA ES LA JOINED!!!!! 
	  `nombre` varchar(45) DEFAULT NULL,
	  PRIMARY KEY (`idmodulo`),
	  KEY `fk_profesor_idx` (`idprofesor`),
	  CONSTRAINT `fk_profesor` FOREIGN KEY (`idprofesor`) REFERENCES `profesores` (`idprofesor`) ON UPDATE CASCADE
	) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
*/

/*Seguimos el criterio de clase/objeto en singular y tabla en plural*/
@Entity
@Table(name = "modulos")
public class Modulo {
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idmodulo", length = 11, nullable = false)
	public int getIdmodulo() {return idmodulo;}
	public void setIdmodulo(int idmodulo) {this.idmodulo = idmodulo;}
	private int idmodulo;
	
	@Column(name = "nombre", length = 45)
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	private String nombre;
	
	public Modulo() {}
	public Modulo(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	/****************************************************************************/
	/* 													RELACIÓN N:1 																		*/
	/****************************************************************************/
	/* Para implementar la relación Modulo N:1 Profesor, se hace un JOIN en esta
	 * tabla "modulos" (parte N:), que será la "propietaria" de la relación, con
	 * la columna que contiene la clave primaria de la tabla "profesor", que
	 * representa la parte :1 de la relación. Como la columna que se une, 
	 * "idprofesor", puede ser null, fijamos el parámetro optional a true. Aunque
	 * no sé si es redundante respecto al nullable del @JoinColumn*/
	@ManyToOne(optional = true,
						 cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			 									CascadeType.DETACH, CascadeType.REFRESH},
						 fetch = FetchType.LAZY
	)
	@JoinColumn(name = "idprofesor", columnDefinition = "int(11)", nullable = true,
							referencedColumnName = "idprofesor", /*Creo que no hace falta al usar foreignKey*/
							foreignKey = @ForeignKey(
								name = "fk_profesor",
								value=ConstraintMode.CONSTRAINT,
								foreignKeyDefinition="FOREIGN KEY (idprofesor) REFERENCES profesores(idprofesor)"))
	public Profesor getProfesor() {return profesor;}
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
		/* No puedo hacer esto porque desde allí se vuelve a llamar a setProfesor y
		 * entra en bucle infinito*/
		//profesor.addModulo(this);
	}
	private Profesor profesor;
	
	/* MUCHO CUIDADO PORQUE ESTAMOS DEL LADO :1 DE UNA RELACIÓN BIDIRECCIONAL Y,
	 * por lo tanto, no hay método add ni remove y en su lugar se usa:
	 * 	add 		-> set "loquesea"
	 *  remove 	-> set null
	 * Pero el problema que he detectado es que no estoy actualizando el otro lado
	 * de la relación si hago un modulo.setProfesor(profesor), pues me estoy
	 * dejando de poner profesor.add(modulo). Entonces, después de persistir me he
	 * encontrado que no podía recuperar la información en el toString de profesor.
	 * La solución pasaría por implementar un "helper", pero no estamos usando ni
	 * addProfesor ni removeProfesor al haber sólo un profesor que "poner o quitar".
	 * Entonces, o bien creo estos métodos también en el lado 1: de esta relación,
	 * cosa que no voy a hacer por si me salto instrucciones, o implemento el
	 * "helper" en el propio setter. Yo haría lo anterior, pero por no saltarme
	 * instrucciones haré esto último*/
	/* Puede parecer contradictorio que el nombre de la columna sea "idprofesor"
	 * pero que la propiedad sea el objeto profesor "entero". Una cosa es la
	 * información que representamos en la columna de la tabla (idprofesor) y otra
	 * la que se encuentra implícitamente relacionada, la que es objeto de la
	 * relación, i.e. el objeto profesor con todas sus propiedades*/
	/* Si queremos que esta relación sea bidireccional (se podría dejar así e
	 * Hibernate ya lo entendería), se ha de "replicar" esta relación en la parte
	 * contraria de la misma, la parte :1, correspondiente a la entidad Profesor,
	 * adaptando la etiqueta a este caso :1 con @OneToMany*/
	public void addProfesor(Profesor profesor) {
		this.setProfesor(profesor);
		profesor.addModulo(this);
	}
	
	/****************************************************************************/
	/* 													RELACIÓN N:N 																		*/
	/****************************************************************************/
	/* Elegimos esta parte "Modulo" como la propietaria (owner) de la relación N:N,
	 * de forma que será aquí donde la configuraremos. La otra parte (target), la
	 * parte "Alumnos", simplemente la referenciará.
	 * Darse cuenta que no estamos generando un índice primary key auto_increment
	 * para la propia tabla `mod_alu`, si bien en el schema inicial si viene así...
	 * Bueno, se podría hacer pero en el curso no lo hemos hecho y ahora tengo otra
	 * faena más urgente*/
	/* El SQL proporcionado para la tabla `mod_alu` de la BD `instituto` es:
	CREATE TABLE `mod_alu` (
	  `idmod_alu` int(11) NOT NULL AUTO_INCREMENT,
	  `idmodulo` int(11) DEFAULT NULL,
	  `idalumno` int(11) DEFAULT NULL,
	  PRIMARY KEY (`idmod_alu`),
	  KEY `fk_modulo_idx` (`idmodulo`),
	  KEY `fk_alumno_idx` (`idalumno`),
	  CONSTRAINT `fk_alumno` FOREIGN KEY (`idalumno`) REFERENCES `alumnos` (`idalumno`) ON UPDATE CASCADE,
	  CONSTRAINT `fk_modulo` FOREIGN KEY (`idmodulo`) REFERENCES `modulos` (`idmodulo`) ON UPDATE CASCADE
	) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
	*/

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
												 CascadeType.DETACH, CascadeType.REFRESH},
							fetch = FetchType.LAZY /*:N ya lo es por defecto, pero por si acaso*/
	)
	@JoinTable(name = "mod_alu",
	 					 joinColumns = @JoinColumn(
	 						 name = "idmodulo", columnDefinition = "int(11)", nullable = true,
	 						 referencedColumnName = "idmodulo",
	 						 foreignKey = @ForeignKey(
 							 name = "fk_modulo",
 							 value=ConstraintMode.CONSTRAINT,
 							 foreignKeyDefinition="FOREIGN KEY (idmodulo) REFERENCES modulos(idmodulo)"
 					 )),
 					 inverseJoinColumns = @JoinColumn(
 						 name = "idalumno", columnDefinition = "int(11)", nullable = true,
 						 referencedColumnName = "idalumno",
 						 foreignKey = @ForeignKey(
 							 name = "fk_alumno",
 							 value=ConstraintMode.CONSTRAINT,
 							 foreignKeyDefinition="FOREIGN KEY (idalumno) REFERENCES alumnos(idalumno)"
 					 ))										 
	)
	public List<Alumno> getAlumnos() {return alumnos;}
	public void setAlumnos(List<Alumno> alumnos) {this.alumnos = alumnos;}
	private List<Alumno> alumnos = new ArrayList<Alumno>();
	/* He leído que se recomienda usar un Set que una List cuando es ToMany, que 
	 * es más óptimo para borrar registros*/	
	
	public void addAlumno(Alumno alumno) {
		//if (alumnos == null) {
		//	alumnos = new ArrayList<Alumno>();
		//}
		this.alumnos.add(alumno);
		alumno.getModulos().add(this);
	}
	public void delAlumno(Alumno alumno) {
		this.alumnos.remove(alumno);
		alumno.getModulos().remove(this);
	}
	
	/****************************************************************************/
	@Override
	public String toString() {
		/* Hay que ir con cuidado porque profesor y alumnos pueden ser nulos!!*/
		String strAlumnos = alumnos
			.stream().map(a -> !a.equals(null) ? a.getNombre() + "|": "")
			.reduce("", String::concat);				
		
		String strProfesor = "";
		if(profesor != null) {
			strProfesor = profesor.getNombre();
		}
		
		return ""
			+ "  idmodulo=\"" + idmodulo + "\", nombre=\"" + nombre
			+ "\", [nombre/s profesor/es=\"" + strProfesor
			+ "\"], [nombre/s alumno/s=\"" + strAlumnos.replaceAll("^(.*).{1}$", "$1")
			+ "\"]";
	}
}
