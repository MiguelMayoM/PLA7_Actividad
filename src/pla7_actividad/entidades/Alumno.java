package pla7_actividad.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/* RELACIONES
	 Esta entidad sólo está relacionada con Modulo mediante la relación:
 + Alumno N:N Modulo

	El SQL proporcionado para la tabla `alumnos` de la BD `instituto` es:
	CREATE TABLE `alumnos` (
	  `idalumno` int(11) NOT NULL AUTO_INCREMENT,
	  `nombre` varchar(45) DEFAULT NULL,
	  `email` varchar(45) DEFAULT NULL,
	  PRIMARY KEY (`idalumno`)
	) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
 */

/*Seguimos el criterio de clase/objeto en singular y tabla en plural*/
@Entity
@Table(name = "alumnos")
public class Alumno {
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idalumno", length = 11, nullable = false)
	public int getIdalumno() {return idalumno;}
	public void setIdalumno(int idalumno) {this.idalumno = idalumno;}
	private int idalumno;
	
	@Column(name = "nombre", length = 45)
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	private String nombre;
	
	@Column(name = "email", length = 45)
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	private String email;
	
	public Alumno() {}
	public Alumno(String nombre, String email) {
		super();
		this.nombre = nombre;
		this.email = email;		
	}
		
	/****************************************************************************/
	/* 													RELACIÓN N:N 																		*/
	/****************************************************************************/
	/* La relación N:N no existe físicamente en la BD, de la misma forma que en la
	 * 1:N entre Profesor y Modulo, cuando hacíamos el JOIN sólo estábamos copiando
	 * la columna de la clave primaria de cada entidad "profesor" a la tabla de
	 * la entidad "Modulo" que tocase. Así, en el caso de N:N, se copian las claves
	 * primarias de las entidades relacionadas a una tabla intermedia, desde la
	 * cual se podrá recuperar toda la información que sea necesaria*/
	/* Elegimos la parte "Modulo" como la propietaria (owner) de la relación N:N,
	 * siendo allí donde se configurarán las columnas que generarán la tabla
	 * `mod_alu` intermedia; esta parte, "Alumno", será la parte "target" de la
	 * relación, donde sólo se habrá de referenciar a la parte propietaria, donde
	 * ya se ha realizado toda la configuración necesaria de la relación, sin
	 * necesidad de tener que volver a repetir el mismo código, que he dejado
	 * comentado. Y dicha referencia a la configuración en la parte propietaria se
	 * realiza con el atributo mappedBy*/
	//	@JoinTable(name = "mod_alu",
	//		joinColumns = @JoinColumn(
	//			name = "idalumno", columnDefinition = "int(11)", nullable = true,
	//			referencedColumnName = "idalumno",
	//			foreignKey = @ForeignKey(
	//				name = "fk_alumno",
	//				value=ConstraintMode.CONSTRAINT,
	//				foreignKeyDefinition="FOREIGN KEY (idalumno) REFERENCES alumnos(idalumno)"
	//		)),
	//		inverseJoinColumns = @JoinColumn(
	//			name = "idmodulo", columnDefinition = "int(11)", nullable = true,
	//			referencedColumnName = "idmodulo",
	//			foreignKey = @ForeignKey(
	//				name = "fk_modulo",
	//				value=ConstraintMode.CONSTRAINT,
	//				foreignKeyDefinition="FOREIGN KEY (idmodulo) REFERENCES modulos(idmodulo)"
	//		))								 
	//)
	/* Con mappedBy se referencia la relación ya definida en Modulo, no hay que
	 * volver a definir el código arriba comentado */
	@ManyToMany(mappedBy = "alumnos",
							cascade = {CascadeType.PERSIST, CascadeType.MERGE,
												 CascadeType.DETACH, CascadeType.REFRESH},
							fetch = FetchType.LAZY /*:N ya lo es por defecto, pero por si acaso*/
	)
	public List<Modulo> getModulos() {return modulos;}
	public void setModulos(List<Modulo> modulos) {this.modulos = modulos;}
	private List<Modulo> modulos = new ArrayList<Modulo>();
	/* He leído que se recomienda usar un Set que una List cuando es ToMany, que 
	 * es más óptimo para borrar registros*/	
	
	public void addModulo(Modulo modulo) {
		/* He iniciado el new ArrayList en la definición de la propidad porque, si
		 * implemento un método para borrar registros y este queda vacío, entonces
		 * volvería a crear un ArrayList cuando ya hay uno, ¿no?*/
		//if (modulos == null) {
		//	modulos = new ArrayList<Modulo>();
		//}
		this.modulos.add(modulo);
		modulo.getAlumnos().add(this);
		/* Aquí creamos un "helper" para no dejarnos de actualizar ambos lados de la
		 * relación.*/
	}	
	public void delModulo(Modulo modulo) {
		this.modulos.remove(modulo);
		modulo.getAlumnos().remove(this);
	}	

	/****************************************************************************/
	@Override
	public String toString() {
		String strModulos = modulos
			.stream().map(m -> m.getNombre() + "|")
			.reduce("", String::concat);
			
		return ""
			+ "  idalumno=\"" + idalumno + "\", nombre=\"" + nombre
			+ "\", email=\"" +	email
			+ "\", [idmodulo's=\""  + strModulos.replaceAll("^(.*).{1}$", "$1")
			+ "\"]";
	}
}
