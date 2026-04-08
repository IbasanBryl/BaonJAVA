package baon.security;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SmtpClient {
    private static final String SMTP_HOST = "";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_SECURITY = "ssl";
    private static final String SMTP_USERNAME = "";
    private static final String SMTP_PASSWORD = "";
    private static final String SMTP_FROM = SMTP_USERNAME;
    private static final String SMTP_FROM_NAME = "BaonBrain";
    private static final boolean SMTP_SSL = SMTP_PORT == 465;
    private static final boolean SMTP_STARTTLS = SMTP_PORT == 587;

    private SmtpClient() {
    }

    public static void sendEmail(String recipientEmail, String subject, String body) throws IOException {
        String host = SMTP_HOST.trim();
        int port = SMTP_PORT;
        String security = SMTP_SECURITY.trim().toLowerCase();
        String username = SMTP_USERNAME.trim();
        String password = SMTP_PASSWORD;
        String from = SMTP_FROM.trim();
        String fromName = SMTP_FROM_NAME.trim();
        boolean useSsl = "ssl".equals(security) || SMTP_SSL;
        boolean useStartTls = "starttls".equals(security) || (!useSsl && SMTP_STARTTLS);

        if (host.isEmpty() || from.isEmpty()) {
            throw new IOException("SMTP host and sender email must be configured in SmtpClient.");
        }
        if (!username.isEmpty() && password.trim().isEmpty()) {
            throw new IOException("SMTP password is missing for authenticated SMTP.");
        }

        SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket initialSocket = useSsl
                ? sslFactory.createSocket(host, port)
                : new Socket(host, port);

        try (SmtpSession session = new SmtpSession(initialSocket, host, port)) {
            session.expect(220);
            session.sendCommand("EHLO baonbrain.local");
            session.expect(250);

            if (useStartTls) {
                session.sendCommand("STARTTLS");
                session.expect(220);
                session.upgradeToTls();
                session.sendCommand("EHLO baonbrain.local");
                session.expect(250);
            }

            if (!username.isEmpty()) {
                session.sendCommand("AUTH LOGIN");
                session.expect(334);
                session.sendCommand(Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8)));
                session.expect(334);
                session.sendCommand(Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
                session.expect(235);
            }

            session.sendCommand("MAIL FROM:<" + from + ">");
            session.expect(250);
            session.sendCommand("RCPT TO:<" + recipientEmail + ">");
            session.expect(250, 251);
            session.sendCommand("DATA");
            session.expect(354);
            session.sendRaw(buildMessage(fromName, from, recipientEmail, subject, body));
            session.expect(250);
            session.sendCommand("QUIT");
        }
    }

    private static String buildMessage(String fromName, String from, String to, String subject, String body) {
        String normalizedSubject = sanitizeHeader(subject);
        String normalizedBody = dotStuff(body == null ? "" : body).replace("\r\n", "\n").replace("\r", "\n")
                .replace("\n", "\r\n");

        StringBuilder builder = new StringBuilder();
        builder.append("From: ").append(formatFromHeader(fromName, from)).append("\r\n");
        builder.append("To: ").append(to).append("\r\n");
        builder.append("Subject: ").append(normalizedSubject).append("\r\n");
        builder.append("MIME-Version: 1.0\r\n");
        builder.append("Content-Type: text/plain; charset=UTF-8\r\n");
        builder.append("Content-Transfer-Encoding: 8bit\r\n");
        builder.append("\r\n");
        builder.append(normalizedBody).append("\r\n");
        builder.append(".\r\n");
        return builder.toString();
    }

    private static String sanitizeHeader(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\r", " ").replace("\n", " ");
    }

    private static String formatFromHeader(String fromName, String fromEmail) {
        String safeName = sanitizeHeader(fromName == null ? "" : fromName.trim());
        if (safeName.isEmpty()) {
            return fromEmail;
        }
        return safeName + " <" + fromEmail + ">";
    }

    private static String dotStuff(String text) {
        String[] lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n", -1);
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < lines.length; index++) {
            String line = lines[index];
            if (line.startsWith(".")) {
                builder.append('.');
            }
            builder.append(line);
            if (index < lines.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private static final class SmtpSession implements AutoCloseable {
        private Socket socket;
        private final String host;
        private final int port;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String lastResponse;

        private SmtpSession(Socket socket, String host, int port) throws IOException {
            this.socket = socket;
            this.host = host;
            this.port = port;
            resetStreams();
        }

        private void upgradeToTls() throws IOException {
            javax.net.ssl.SSLSocketFactory sslFactory = (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket(socket, host, port, true);
            sslSocket.startHandshake();
            socket = sslSocket;
            resetStreams();
        }

        private void sendCommand(String command) throws IOException {
            sendRaw(command + "\r\n");
        }

        private void sendRaw(String value) throws IOException {
            writer.write(value);
            writer.flush();
        }

        private void expect(int... allowedCodes) throws IOException {
            int code = readResponseCode();
            for (int allowed : allowedCodes) {
                if (code == allowed) {
                    return;
                }
            }
            throw new IOException("SMTP error " + code + ": " + (lastResponse == null ? "" : lastResponse));
        }

        private int readResponseCode() throws IOException {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("SMTP connection closed unexpectedly.");
            }
            lastResponse = line;
            int code = parseCode(line);

            while (line.length() >= 4 && line.charAt(3) == '-') {
                line = reader.readLine();
                if (line == null) {
                    throw new IOException("SMTP response ended unexpectedly.");
                }
                lastResponse = line;
            }

            return code;
        }

        private int parseCode(String line) throws IOException {
            if (line.length() < 3) {
                throw new IOException("Invalid SMTP response: " + line);
            }
            try {
                return Integer.parseInt(line.substring(0, 3));
            } catch (NumberFormatException exception) {
                throw new IOException("Invalid SMTP status code: " + line, exception);
            }
        }

        private void resetStreams() throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}

