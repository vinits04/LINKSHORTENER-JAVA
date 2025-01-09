import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LinkShortener {
    private static final String DOMAIN = "http://short.ly/";
    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private final HashMap<String, String> urlMap;
    private final HashMap<String, String> reverseMap;

    public LinkShortener() {
        urlMap = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    // Validate if a long URL is valid
    private boolean isValidLongUrl(String url) {
        String urlRegex = "^(http|https)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(\\/\\S*)?$";
        return Pattern.matches(urlRegex, url);
    }

    // Validate if a short URL is valid
    private boolean isValidShortUrl(String url) {
        return url.startsWith(DOMAIN) && url.length() == (DOMAIN.length() + SHORT_URL_LENGTH);
    }

    private String generateShortUrl(String longUrl) {
        StringBuilder shortUrl = new StringBuilder();
        int hash = Math.abs(longUrl.hashCode());
        while (shortUrl.length() < SHORT_URL_LENGTH) {
            shortUrl.append(CHAR_SET.charAt(hash % CHAR_SET.length()));
            hash /= CHAR_SET.length();
        }
        return shortUrl.toString();
    }

    public String shortenUrl(String longUrl) {
        if (!isValidLongUrl(longUrl)) {
            return "Error: Invalid long URL format.";
        }
        if (reverseMap.containsKey(longUrl)) {
            return DOMAIN + reverseMap.get(longUrl);
        }
        String shortUrl;
        do {
            shortUrl = generateShortUrl(longUrl);
        } while (urlMap.containsKey(shortUrl));
        urlMap.put(shortUrl, longUrl);
        reverseMap.put(longUrl, shortUrl);
        return DOMAIN + shortUrl;
    }

    public String expandUrl(String shortUrl) {
        if (!isValidShortUrl(shortUrl)) {
            return "Error: Invalid short URL format.";
        }
        String key = shortUrl.replace(DOMAIN, "");
        return urlMap.getOrDefault(key, "Error: Short URL not found.");
    }

    public void startCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Link Shortener CLI!");
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Expand a short URL");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter a long URL: ");
                    String longUrl = scanner.nextLine();
                    String shortUrl = shortenUrl(longUrl);
                    System.out.println(shortUrl);
                    break;
                case 2:
                    System.out.print("Enter a short URL: ");
                    String shortInput = scanner.nextLine();
                    String originalUrl = expandUrl(shortInput);
                    System.out.println(originalUrl);
                    break;
                case 3:
                    System.out.println("Exiting Link Shortener, Thankyou and Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();
        linkShortener.startCLI();
    }
}
