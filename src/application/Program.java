package application;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

public class Program {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		try (Scanner scanner = new Scanner(System.in);) {
			System.out.println("Enter the rental data: ");
			System.out.print("Car model: ");
			String carModel = scanner.nextLine();
			System.out.print("Pick-up (dd/MM/yyyy HH:mm): ");
			LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), format);
			System.out.print("Return (dd/MM/yyyy HH:mm): ");
			LocalDateTime finish = LocalDateTime.parse(scanner.nextLine(), format);

			if (start.isBefore(finish)) {
				CarRental carRental = new CarRental(start, finish, (new Vehicle(carModel)));

				System.out.print("Enter the price per hour: ");
				double pricePerHour = scanner.nextDouble();
				System.out.print("Enter the price per day: ");
				double pricePerDay = scanner.nextDouble();

				RentalService rentalService = new RentalService(pricePerDay, pricePerHour, new BrazilTaxService());

				rentalService.processInvoice(carRental);

				System.out.println("INVOICE: ");
				System.out
						.println("Basic payment: R$" + String.format("%.2f", carRental.getInvoice().getBasicPayment()));
				System.out.println("Tax: R$" + String.format("%.2f", carRental.getInvoice().getTax()));
				System.out
						.println("Total payment: R$" + String.format("%.2f", carRental.getInvoice().getTotalPayment()));
			} else {
				System.out.println("Error: The vehicle pick-up date cannot be later than the return date! ");
			}
		} catch (InputMismatchException e) {
			System.out.println("Error: Invalid data type! ");
		} catch (DateTimeParseException e) {
			System.out.println("Error: Invalid date format! ");
		} catch (Exception e) {
			System.out.println("Error: Something went wrong. ");
		}
	}
}