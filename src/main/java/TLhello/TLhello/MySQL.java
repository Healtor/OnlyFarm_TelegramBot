package TLhello.TLhello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 

public class MySQL {

	String myDriver = "com.mysql.jdbc.Driver";
	String myUrl = "jdbc:mysql://localhost/OnlyFarm";
	
	public int insertarUsuario(long id, String FName, String LName, String Alias, int NAvisos) {
		try {
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");
			
			String query = "SELECT * FROM usuarios WHERE UserID ="+id;
			Statement st2 = conn.createStatement();
			ResultSet rs = st2.executeQuery(query);
			Statement st = conn.createStatement();
			
			if(!rs.next()) { //Ya exite usuario
				// create the java statemen
				st.executeUpdate("INSERT INTO usuarios " + "VALUES ("+id+", '"+ FName+"', '"+ LName +"', '" + Alias+"', "+ NAvisos+")");
				System.out.println("ejecutando inside");
			}
			st2.close();
			st.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return 1;
		
	}
	
	
	public int modificarUsuario(long id) {
		int kick =-1;
		try {
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");
			
			String query = "SELECT * FROM usuarios WHERE UserID ="+id;
			Statement st2 = conn.createStatement();
			ResultSet rs = st2.executeQuery(query);
			Statement st = conn.createStatement();

			if (rs.next()) { // Ya exite usuario

				int avisos = rs.getInt("NAvisos") + 1;
				if (avisos == 3) {
					kick = 4;
					avisos = 0;
				} else
					kick = avisos;
				st.executeUpdate("UPDATE usuarios SET NAVISOS = " + avisos + " WHERE UserID = " + id);
			}
			st2.close();
			st.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		return kick;
				
	}
	
	public String quitarWarns(long id) {
		String alias = null;
		try {
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");
			
			String query = "SELECT * FROM usuarios WHERE UserID ="+id;
			Statement st2 = conn.createStatement();
			ResultSet rs = st2.executeQuery(query);
			Statement st = conn.createStatement();

			if (rs.next()) { // Ya exite usuario
				alias=rs.getString("Alias");
				st.executeUpdate("UPDATE usuarios SET NAVISOS = 0 WHERE UserID = " + id);
			}
			st2.close();
			st.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return alias;
				
	}
	
	
	public String getGif(String nombre) {
		String id = null;
		try {
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");

			String query = "SELECT * FROM documentos WHERE nombre = '" + nombre+"'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			if (rs.next()) { // Ya exite usuario
				id = rs.getString("DocID");

			}

			st.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	public void prueba() {

		try {

			// create our mysql database connection
			
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");

			// our SQL SELECT query.
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM usuarios";

			// create the java statement
			Statement st = conn.createStatement();

			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);
	
			// iterate through the java resultset
			while (rs.next()) {
				int id = rs.getInt("UserId");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				String alias = rs.getString("Alias");
				int nAvisos = rs.getInt("NAvisos");

				// print the results
				System.out.format("%s, %s, %s, %s, %s\n", id, firstName, lastName, alias, nAvisos);
			}
			st.close();
		} catch (Exception e) {
//			System.err.println("Got an exception! ");
//			System.err.println(e.getMessage());
	          e.printStackTrace();

		}

	}
}
