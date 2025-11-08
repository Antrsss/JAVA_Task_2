package by.zgirskaya.course.reader;

import by.zgirskaya.course.exception.CustomTextReaderException;

public interface CustomTextReader {
    String readTextFromFile(String filePath) throws CustomTextReaderException;
}
