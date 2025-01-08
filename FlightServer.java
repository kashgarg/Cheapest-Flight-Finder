import java.io.*;
import java.net.*;
import java.util.*;

public class FlightServer {
    static class Flight {
        String airline;
        String origin;
        String destination;
        double price;

        Flight(String airline, String origin, String destination, double price) {
            this.airline = airline;
            this.origin = origin;
            this.destination = destination;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        List<Flight> flights = Arrays.asList(
            new Flight("Airline A", "NYC", "LAX", 250.0),
            new Flight("Airline B", "NYC", "LAX", 200.0),
            new Flight("Airline C", "NYC", "LAX", 180.0),
            new Flight("Airline D", "SFO", "LAX", 100.0),
            new Flight("Airline E", "NYC", "SFO", 300.0)
        );

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Flight server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket, flights)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, List<Flight> flights) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String origin = in.readLine();
            String destination = in.readLine();

            Flight cheapestFlight = null;
            for (Flight flight : flights) {
                if (flight.origin.equalsIgnoreCase(origin) && flight.destination.equalsIgnoreCase(destination)) {
                    if (cheapestFlight == null || flight.price < cheapestFlight.price) {
                        cheapestFlight = flight;
                    }
                }
            }

            if (cheapestFlight != null) {
                out.println("Cheapest flight found:");
                out.println("Airline: " + cheapestFlight.airline);
                out.println("Price: $" + cheapestFlight.price);
            } else {
                out.println("No flights found for the given route.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

