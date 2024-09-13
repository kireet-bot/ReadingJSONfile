package org.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class VelocityJSON_File {
    public static void main(String[] args) throws Exception {

        ClassLoader classLoader = VelocityJSON_File.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("input.json");

        if (inputStream == null){
            throw new IllegalArgumentException("File not found: input.json");
        } else {
            System.out.println("input.json loaded successfully");
        }

    // Step 1: Read the input JSON file
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode root = objectMapper.readTree(new InputStreamReader(inputStream));
    JsonNode data = root.get("data").get(0);
    JsonNode stage = data.get("Stage");

    // Step 2: Extract and manipulate date fields (convert to UTC and EST)
    String createdTime = stage.get("CreatedTimeStamp").asText();

    // Convert to UTC
    ZonedDateTime utcTime = LocalDateTime.parse(createdTime).atZone(ZoneId.of("UTC"));

    // Convert to EST (America/New_York)
    ZonedDateTime estTime = utcTime.withZoneSameInstant(ZoneId.of("America/New_York"));

    // Step 3: Create a Velocity context and populate it with values
    VelocityContext context = new VelocityContext();
        context.put("utcTime", utcTime);
        context.put("estTime", estTime);
        context.put("messageId", data.get("MessageId").asText());

        InputStream templateStream = classLoader.getResourceAsStream("velocityFile.vm");
        if (templateStream == null){
            throw new IllegalArgumentException("File not found: velocityFile.vm");
        } else {
            System.out.println("velocityFile.vm loaded successfully");
        }

    // Step 4: Load and process the Velocity template
    StringWriter writer = new StringWriter();
        Velocity.init();
        Velocity.evaluate(context, writer, "logTag", new InputStreamReader(templateStream));

    // Step 5: Output the processed template to a file
    try( FileWriter fileWriter = new FileWriter("output.json")) {
        fileWriter.write(writer.toString());
        File file = new File("output.json");

        System.out.println("Output written to output.json" + file.getAbsolutePath());
    }
    }
}
