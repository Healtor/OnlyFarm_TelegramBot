package OF_Bot;

public class Usuario implements Comparable<Usuario>{

	int id;
	String fName;
	String lName;
	String Alias;
	int NAvisos;
	int NMensajes;
	int NAvisosTotal;
	String descripcion;
	int expulsiones;
	int abandonos;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getAlias() {
		return Alias;
	}
	public void setAlias(String alias) {
		Alias = alias;
	}
	public int getNAvisos() {
		return NAvisos;
	}
	public void setNAvisos(int nAvisos) {
		NAvisos = nAvisos;
	}
	public int getNMensajes() {
		return NMensajes;
	}
	public void setNMensajes(int nMensajes) {
		NMensajes = nMensajes;
	}
	public int getNAvisosTotal() {
		return NAvisosTotal;
	}
	public void setNAvisosTotal(int nAvisosTotal) {
		NAvisosTotal = nAvisosTotal;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	public int getExpulsiones() {
		return expulsiones;
	}
	public void setExpulsiones(int expulsiones) {
		this.expulsiones = expulsiones;
	}
	public int getAbandonos() {
		return abandonos;
	}
	public void setAbandonos(int abandonos) {
		this.abandonos = abandonos;
	}
	
	@Override
	public String toString() {
		String s= id + ", "+fName+ ", "+lName+ ", "+Alias+ ", "+NAvisos+ ", "+NMensajes+ ", "+NAvisosTotal+ ", "+descripcion+ ", "+expulsiones+ ", "+abandonos;
		return s;
	}
	
	@Override
	public int compareTo(Usuario o) {
		 if (NMensajes < o.NMensajes) {
             return 1;
         }
         if (NMensajes > o.NMensajes) {
             return -1;
         }
		return 0;
	}
}
