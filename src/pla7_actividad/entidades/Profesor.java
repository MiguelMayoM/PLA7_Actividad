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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/* RELACIONES
	 Esta entidad sólo está relacionada con Modulo mediante la relación:
 + Profesor 1:N Modulo

 	El SQL proporcionado para la tabla `profesores` de la BD `instituto` es:
	CREATE TABLE `profesores` (
	  `idprofesor` int(11) NOT NULL AUTO_INCREMENT,
	  `dni` varchar(45) DEFAULT NULL,
	  `nombre` varchar(45) DEFAULT NULL,
	  `email` varchar(45) DEFAULT NULL,
	  PRIMARY KEY (`idprofesor`)
	) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
*/

/* Seguimos el criterio de clase/objeto en singular y tabla en plural */
@Entity
@Table(name = "profesores")
public class Profesor {
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idprofesor", length = 11, nullable = false)
	public int getIdprofesor() {return idprofesor;}
	public void setIdprofesor(int idprofesor) {this.idprofesor = idprofesor;}
	private int idprofesor;
	
	/* Podríamos haber puesto validación de DNI ...*/
	@Column(name = "dni", length = 45)
	public String getDni() {return dni;}
	public void setDni(String dni) {this.dni = dni;}
	private String dni;
	
	@Column(name = "nombre", length = 45)
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}	
	private String nombre;
	
	/* ... y de email ...*/
	@Column(name = "email", length = 45)
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	private String email;
	
	public Profesor() {}
	public Profesor(String dni, String nombre, String email) {
		this.dni = dni;
		this.nombre = nombre;
		this.email = email;
	}
	
	/****************************************************************************/
	/* 													RELACIÓN 1:N 																		*/
	/****************************************************************************/
	/* La relación "Profesor 1:N Modulo" se ha expresado en la parte :N o propietaria,
	 * la entidad "Modulo", mediante un JOIN con el índice "idprofesor" del objeto 
	 * "profesor". Para que esta relación sea bidireccional, desde aquí hemos de
	 * hacer referencia a esa misma propiedad u objeto de la propiedad con:
	 * @mappedBy="profesor".
	 * SÍ, hemos de indicar el objeto "profesor" y no su propiedad índice "idprofesor",
	 * de la misma forma que en la entidad "Modulo" la etiqueta @JoinColumn daba
	 * nombre a una columna índice pero precedía a la propiedad objeto "profesor".
	 * Aquí usamos un nombre en PLURAL para la propiedad "modulos", que pertenece
	 * a la parte :N de la relación y que, por lo tanto, puede albergar varios
	 * valores, los cuales se guardarán en un ArrayList.
	 * @mappedBy indica que la relación 1:N es bidireccional. Si la omitiéramos,
	 * la estructura de la BD sería "la misma", pero no podríamos recuperar los
	 * "modulos" por medio del "profesor" (sólo podríamos, dado un "modulo", saber
	 * qué "profesor" le corresponde). @mappedBy nos está diciendo que la relación
	 * bidireccional se ha mapeado en la entidad "Modulo" mediante la propiedad
	 * objeto "profesor". Y que recurra a ella si queremos conocer o recuperar la
	 * información de los "modulos" asociados a un "profesor".
	 * Si quisiéramos obtener la bidireccionalidad sin mappedBy, habría que realizar
	 * un join de una columna "modulos", que entonces pasaría a formar parte de la
	 * tabla "profesores", ocupando un espacio de información repetida, consumiendo
	 * recursos innecesariamente*/
	@OneToMany(mappedBy = "profesor",
						 cascade = {CascadeType.PERSIST, CascadeType.MERGE,
												CascadeType.DETACH, CascadeType.REFRESH},
						 fetch = FetchType.LAZY /*:N ya lo es por defecto, pero por si acaso*/
	)
	
	public List<Modulo> getModulos() {return modulos;}
	public void setModulos(List<Modulo> modulos) {this.modulos = modulos;}
	private List<Modulo> modulos = new ArrayList<Modulo>();
	/* El setter anterior copia un ArrayList de Modulo a la propiedad modulos,
	 * sobreescribiendo la propiedad si esta estuviera llena. Por ello, si lo que
	 * deseamos es agregar sólo un módulo a la lista, sin sobreescribir los que
	 * pudieran ya existir, es que utilizamos el método siguiente:*/
	public void addModulo(Modulo modulo) {
		//if (modulos == null) {
		//	modulos = new ArrayList<Modulo>();
		//}
		this.modulos.add(modulo);
		/* Y ahora un "helper" para actualizar la otra parte de la relación, si no
		 * no sería coherente la información. Se pone aquí, en vez de realizar este
		 * proceso de forma independiente, porque es algo que puede olvidarse de
		 * hacer, lo cual llevaría a que un profesor pudiera tener un modulo cuya
		 * propiedad profesor, dada por su @JoinColumn, fuera vacía, cosa que sería
		 * contradictoria y corrompería la BD*/
		modulo.setProfesor(this);
		/* Obviamente esta es una posibilidad de corrupción de la BD también podría
		 * pasar si usamos el método setModulos(), ya que podría crear un ArrayList
		 * de modulos con un bucle, añadiendo varios modulos sin fijar la @joinColumn
		 * "profesor" con .setProfesor(this). Y luego "settear" los "modulos" del
		 * "profesor" con ese ArrayList "corrupto". Hay que tenerlo en cuenta. 
		*/
	}
	
	public void delModulo(Modulo modulo) {
		this.modulos.remove(modulo);
		modulo.setProfesor(null);
		/* Obviamente esta es una posibilidad de corrupción de la BD también podría
		 * pasar si usamos el método setModulos(), ya que podría crear un ArrayList
		 * de modulos con un bucle, añadiendo varios modulos sin fijar la @joinColumn
		 * "profesor" con .setProfesor(this). Y luego "settear" los "modulos" del
		 * "profesor" con ese ArrayList "corrupto". Hay que tenerlo en cuenta. 
		*/
	}
	
	/****************************************************************************/
	@Override
	public String toString() {
		/* Aquí hemos de decidir que información ponemos de los módulos. Podríamos
		 * poner el nombre, que quedaría bien, poner toda la información (todos sus
		 * campos entre []) o poner el id y que lo usemos para buscar en su tabla
		 * respectiva, que también se imprime, si requerimos más información; como
		 * riguroso, el "id", pero para evitar una sopa de números, mejor pongo el
		 * nombre*/
		/* Hay que ir con cuidado porque el objeto "modulos" puede estar vacío y daría
		 * error si le aplicamos cualquier método de Modulo*/
		String strModulos = "";
		for (Modulo m: modulos) {
			if (m != null) {strModulos += m.getNombre() + "|"; }
		}
		
		return ""
			+ "  idprofesor=\"" + idprofesor + "\", dni=\"" + dni 
			+	"\", nombre=\"" + nombre + "\", email=\"" + email 
			+	"\", [nombre/s modulo/s=\"" + strModulos.replaceAll("^(.*).{1}$", "$1")
			+ "\"]";
	}
}
