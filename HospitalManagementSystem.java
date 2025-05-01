package com.hospital_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {

	private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital";
	private static final String userName = "root";
	private static final String password = "root";

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url, userName, password);

			Patient patient = new Patient(connection, sc);
			Doctors doctors = new Doctors(connection);

			while (true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1- Add Patient");
				System.out.println("2- View Patient");
				System.out.println("3- Check Patient");
				System.out.println("4- Remove Patient Data");
				System.out.println("5- View Doctors");
				System.out.println("6- Book Appointment");
				System.out.println("7- Exit");
				System.out.println("Enter your choice:");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:
					// Add patient
					patient.addPatient();
					System.out.println();
					break;

				case 2:
					// view patient
					patient.viewPatients();
					System.out.println();
					break;

				case 3:
					System.out.println("Enter Patient ID");
					int id = sc.nextInt();
					patient.getPatientById(id);
					System.out.println();
					break;

				case 4:
					System.out.println("Enter Patient ID");
					int idToDelete = sc.nextInt();
					patient.deletePatientById(idToDelete);
					System.out.println();
					break;

				case 5:
					// view doctor
					doctors.viewDoctors();
					System.out.println();
					break;

				case 6:
					// book appointment
					bookAppointment(patient, doctors, connection, sc);
					System.out.println();
					break;

				case 7:
					System.out.println("Thank You for visiting us!!");
					return;

				default:
					System.out.println("Enter valid choice");
					break;
				}

			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void bookAppointment(Patient patient, Doctors doctors, Connection connection, Scanner sc) {
		System.out.println("Enter patient id:");
		int patientId = sc.nextInt();
		System.out.println("Enter Doctor id:");
		int doctorId = sc.nextInt();
		System.out.println("Enter Appointment date (YYYY-MM-DD): ");
		String appointmentDate = sc.next();
		if (patient.getPatientById(patientId) && doctors.getDoctorById(doctorId)) {
			if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
				String query = "insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";

				try {
					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, patientId);
					preparedStatement.setInt(2, doctorId);
					preparedStatement.setString(3, appointmentDate);

					int affectedRow = preparedStatement.executeUpdate();
					if (affectedRow > 0) {
						System.out.println("Appointment Booked!!");
					} else {
						System.out.println("This slot is already booked! please try again for another date");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		String query = "select count(*) from appointments where doctor_id=? and appointment_date=? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultset = preparedStatement.executeQuery();
			if (resultset.next()) {
				int count = resultset.getInt(1);
				if (count == 0) {
					return true;
				} else {
					return false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

}