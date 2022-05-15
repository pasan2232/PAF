package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class UserManagment {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/ceb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertProject(String name, String email, String password,String mobile, String otp, String status) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into usermanagement(`id`, `name`, `email`, `password`, `mobile`, `otp`, `status`)"
					+ " values ( ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, email);
			preparedStmt.setString(4, password);
			preparedStmt.setString(5, mobile);
			preparedStmt.setString(6, otp);
			preparedStmt.setString(7, "In Active");

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while inserting the project.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String readProject() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\'1\'><tr><th> Name</th><th>Email</th><th>Password</th><th>Mobile</th><th>OTP</th><th>Status</th><th>Update</th><th>Delete</th></tr>";
			String query = "select * from usermanagement";

			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String id = Integer.toString(rs.getInt("id"));
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String mobile = rs.getString("mobile");
				String otp = rs.getString("otp");
				String status = rs.getString("status");

				// Add into the html table
				output += "<tr><td><input id=\'hidProjectIDUpdate\' name=\'hidProjectIDUpdate\' type=\'hidden\' value=\'"
						+ id + "'>" + name + "</td>";
				output += "<td>" + email + "</td>";
				output += "<td>" + password + "</td>";
				output += "<td>" + mobile + "</td>";
				output += "<td>" + otp + "</td>";
				output += "<td>" + status + "</td>";

				
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-userid='"
						+ id + "'>" + "</td></tr>";
			}

			con.close();

			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the projects.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String updateProject(String id, String name, String email, String password,String mobile, String otp) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for updating.";
			}

			// create a prepared statement
			String query = "UPDATE usermanagement SET name=?,email=?,password=?,mobile=?,otp=?,status=? WHERE id=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values

			preparedStmt.setString(1, name);
			preparedStmt.setString(2, email);
			preparedStmt.setString(3, password);
			preparedStmt.setString(4, mobile);
			preparedStmt.setString(5, otp);
			preparedStmt.setString(6, "In Active");
			preparedStmt.setInt(7, Integer.parseInt(id));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while updating the project.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String deleteProject(String id) {

		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}

			// create a prepared statement
			String query = "delete from usermanagement where id=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(id));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "Error while deleting the project.";
			System.err.println(e.getMessage());
		}

		return output;
	}

}
