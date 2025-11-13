package by.zgirskaya.course.service;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.exception.CustomTextException;

public interface TextService {
  int findMaxSentenceCountWithSameWords(AbstractTextComponent textComponent) throws CustomTextException;
  void displaySentencesByLexemeCountAscending(AbstractTextComponent textComponent) throws CustomTextException;
  void changeFirstAndLastLexemesInSentences(AbstractTextComponent textComponent) throws CustomTextException;
}
