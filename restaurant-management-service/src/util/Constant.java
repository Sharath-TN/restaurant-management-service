package util;

import java.util.List;

public class Constant {
    // Database connection constants
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "postgres";
    public static final String DB_DRIVER = "org.postgresql.Driver";

    // Connection pool settings
    public static final int MAX_POOL_SIZE = 10;
    public static final int TIMEOUT_SECONDS = 30;

    public static final List<String> SUPPORTED_PAYMENT_METHODS = List.of("Cash", "Card", "UPI");
}