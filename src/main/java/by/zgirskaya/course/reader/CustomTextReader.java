package by.zgirskaya.course.reader;

import by.zgirskaya.course.exception.CustomTextException;

public interface CustomTextReader {
    String readTextFromFile(String filePath) throws CustomTextException;
}
