package utils;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class DependencySetup {
    private static final String[] DEPENDENCIES = {
            "https://repo1.maven.org/maven2/org/java-websocket/Java-WebSocket/1.5.3/Java-WebSocket-1.5.3.jar"
    };

    public static void main(String[] args) {
        try {
            // Create lib directory if it doesn't exist
            File libDir = new File("lib");
            if (!libDir.exists()) {
                libDir.mkdir();
                System.out.println("Created lib directory");
            }

            // Download dependencies
            for (String depUrl : DEPENDENCIES) {
                String fileName = depUrl.substring(depUrl.lastIndexOf('/') + 1);
                File jarFile = new File("lib/" + fileName);

                if (!jarFile.exists()) {
                    System.out.println("Downloading " + fileName + "...");
                    downloadFile(depUrl, jarFile.getPath());
                    System.out.println("Successfully downloaded " + fileName);
                } else {
                    System.out.println(fileName + " already exists");
                }
            }

            // Update VS Code settings
            updateVSCodeSettings();

            System.out.println("\nSetup complete! Please restart VS Code to apply changes.");
            System.out.println("Then you can run your project with the WebSocket functionality.");

        } catch (Exception e) {
            System.err.println("Error during setup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void downloadFile(String url, String outputPath) throws IOException {
        URL website = new URL(url);
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    private static void updateVSCodeSettings() throws IOException {
        File settingsFile = new File(".vscode/settings.json");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            settingsFile.createNewFile();
        }

        // Read existing settings or create new ones
        String settings = """
                {
                    "java.project.referencedLibraries": [
                        "lib/**/*.jar"
                    ]
                }
                """;

        // Write settings
        try (FileWriter writer = new FileWriter(settingsFile)) {
            writer.write(settings);
        }
    }
}
