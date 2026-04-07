package baon.backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public final class BackendServer {
    private static final Path DATA_PATH = Paths.get("data", "database.json");
    private static final Object FILE_LOCK = new Object();
    private static HttpServer server;

    private BackendServer() {
    }

    public static synchronized void start(int port) {
        if (server != null) {
            return;
        }
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            httpServer.createContext("/api/health", BackendServer::handleHealth);
            httpServer.createContext("/api/database", BackendServer::handleDatabase);
            httpServer.setExecutor(null);
            httpServer.start();
            server = httpServer;
            System.out.println("Backend running at http://127.0.0.1:" + port);
        } catch (IOException exception) {
            System.err.println("Backend failed to start: " + exception.getMessage());
        }
    }

    private static void handleHealth(HttpExchange exchange) throws IOException {
        addCors(exchange);
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            send(exchange, 204, "");
            return;
        }
        if (!"GET".equals(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        send(exchange, 200, "{\"status\":\"ok\"}");
    }

    private static void handleDatabase(HttpExchange exchange) throws IOException {
        addCors(exchange);
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            send(exchange, 204, "");
            return;
        }

        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            String json = loadDatabaseJson();
            send(exchange, 200, json);
            return;
        }
        if ("PUT".equals(method)) {
            String body = readBody(exchange.getRequestBody()).trim();
            if (!isLikelyJsonObject(body)) {
                send(exchange, 400, "{\"error\":\"Body must be a JSON object\"}");
                return;
            }
            saveDatabaseJson(body + "\n");
            send(exchange, 200, "{\"saved\":true}");
            return;
        }
        send(exchange, 405, "{\"error\":\"Method not allowed\"}");
    }

    private static void addCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void send(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private static String readBody(InputStream stream) throws IOException {
        try (InputStream input = stream; ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] chunk = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static String loadDatabaseJson() throws IOException {
        synchronized (FILE_LOCK) {
            ensureFileExists();
            byte[] bytes = Files.readAllBytes(DATA_PATH);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    private static void saveDatabaseJson(String json) throws IOException {
        synchronized (FILE_LOCK) {
            ensureFileExists();
            Files.write(DATA_PATH, json.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void ensureFileExists() throws IOException {
        Path parent = DATA_PATH.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(DATA_PATH)) {
            Files.write(DATA_PATH, defaultJson().getBytes(StandardCharsets.UTF_8));
        }
    }

    private static boolean isLikelyJsonObject(String body) {
        return body.startsWith("{") && body.endsWith("}");
    }

    private static String defaultJson() {
        return "{\n"
                + "  \"budgetLimit\": 0.0,\n"
                + "  \"savingGoalTarget\": 0.0,\n"
                + "  \"incomeEntries\": [],\n"
                + "  \"expenseEntries\": [],\n"
                + "  \"savingEntries\": []\n"
                + "}\n";
    }
}

