package by.zgirskaya.course.service;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.exception.CustomTextReaderException;

public interface TextService {
  int findMaxSentenceCountWithSameWords(AbstractTextComponent textComponent) throws CustomTextReaderException;
  void displaySentencesByLexemeCountAscending(AbstractTextComponent textComponent) throws CustomTextReaderException;
  void changeFirstAndLastLexemesInSentences(AbstractTextComponent textComponent) throws CustomTextReaderException;
}
