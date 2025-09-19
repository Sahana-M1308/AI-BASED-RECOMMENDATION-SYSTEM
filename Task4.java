import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class Task4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter movie genre (e.g., Action, Drama, Comedy, Horror, Fantasy): ");
        String inputGenre = scanner.nextLine().trim();
        
        String csvFile = "E:\\Recommandation.csv"; // Ensure this path is correct
        Map<String, List<Double>> movieRatings = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String genre = parts[1].trim();
                    String movie = parts[2].trim();
                    double rating = Double.parseDouble(parts[3].trim());

                    if (genre.equalsIgnoreCase(inputGenre)) {
                        movieRatings.putIfAbsent(movie, new ArrayList<>());
                        movieRatings.get(movie).add(rating);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        if (movieRatings.isEmpty()) {
            System.out.println("No data found for this genre.");
        } else {
            Map<String, Double> avgRatings = new HashMap<>();

            for (Map.Entry<String, List<Double>> entry : movieRatings.entrySet()) {
                List<Double> ratings = entry.getValue();
                double avg = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                avgRatings.put(entry.getKey(), avg);
            }

            // Find best movie
            String bestMovie = Collections.max(avgRatings.entrySet(), Map.Entry.comparingByValue()).getKey();
            System.out.println("Best movie for genre \"" + inputGenre + "\" based on ratings: " + bestMovie);

            // Print all movies sorted by rating
            System.out.println("\nAll movies in this genre with average ratings:");
            avgRatings.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
        }

        scanner.close();
    }
}
