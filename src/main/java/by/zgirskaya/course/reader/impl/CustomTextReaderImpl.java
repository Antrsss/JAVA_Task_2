package by.zgirskaya.course.reader.impl;

import by.zgirskaya.course.exception.CustomTextException;
import by.zgirskaya.course.reader.CustomTextReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomTextReaderImpl implements CustomTextReader {

    private static final Logger logger = LogManager.getLogger();

    public String readTextFromFile(String filePath) throws CustomTextException {
        logger.debug("Attempting to read file from: {}", filePath);

        if (filePath == null || filePath.isBlank()) {
            throw new CustomTextException("File path cannot be null or empty");
        }

        try {
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                throw new CustomTextException("File does not exist: " + filePath);
            }

            if (!Files.isRegularFile(path)) {
                throw new CustomTextException("Path is not a file: " + filePath);
            }

            long fileSize = Files.size(path);
            if (fileSize == 0) {
                logger.warn("File is empty: {}", filePath);
                return "";
            }

            String content = new String(Files.readAllBytes(path));
            logger.debug("Successfully read {} characters from file: {}", content.length(), filePath);

            return content;

        } catch (IOException e) {
            throw new CustomTextException("Error reading file: " + filePath, e);
        } catch (Exception e) {
            throw new CustomTextException("Unexpected error while reading file: " + filePath, e);
        }
    }
}