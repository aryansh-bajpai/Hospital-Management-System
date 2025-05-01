package com.hospital_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner sc;

	public Patient(Connection connection, Scanner sc) {
		this.connection = connection;
		this.sc = sc;
	}

	public void addPatient() {
		System.out.println("Enter patient name: ");
		String name = sc.next();
		System.out.println("Enter patient age: ");
		int age = sc.nextInt();
		System.out.println("Enter patient gender: ");
		String gender = sc.next();

		try {
			String query = "Insert into patients(name,age,gender) values(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);

			int affectRows = preparedStatement.executeUpdate();
			if (affectRows > 0) {
				System.out.println("Patient added successfully!!");

			} else {
				System.out.println("Failed to add patient!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void viewPatients() {
		String query = "select * from patients";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("Patients : ");
	
			System.out.println("+------------+----------------+-----------+--------------+");
			System.out.println("| Patient Id | Name           | Age       | Gender       |");
			System.out.println("+------------+----------------+-----------+--------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				System.out.printf("|%-12s|%-16s|%-11s|%-14s|\n",id,name,age,gender);
				System.out.println("+------------+----------------+-----------+--------------+");
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getPatientById(int id) {
		String query = "select * from patients where id=?";

		try {
			PreparedStatement prepareStatement = connection.prepareStatement(query);
			prepareStatement.setInt(1, id);
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next()) {
				System.out.println("+------------+----------------+-----------+--------------+");
				System.out.println("| Patient Id | Name           | Age       | Gender       |");
				System.out.println("+------------+----------------+-----------+--------------+");
				
					int Pid = resultSet.getInt("id");
					String name = resultSet.getString("name");
					int age = resultSet.getInt("age");
					String gender = resultSet.getString("gender");
					System.out.printf("|%-12s|%-16s|%-11s|%-14s|\n",Pid,name,age,gender);
					System.out.println("+------------+----------------+-----------+--------------+");
				return true;
			}
			
				else {
					System.out.println("Patient doesn't exists in records");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	public void deletePatientById(int id)
	{
		String query="delete from patients where id=?";
		
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			int affectedRow=preparedStatement.executeUpdate();
			if(affectedRow>0)
			{
				System.out.println("Patient removed successfully!!");
			}
			else
			{
				System.out.println("Failed to remove!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
