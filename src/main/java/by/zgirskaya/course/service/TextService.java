package by.zgirskaya.course.service;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComposite;

public interface TextService {
  int findMaxSentenceCountWithSameWords(AbstractTextComponent textComponent);
  void displaySentencesByLexemeCountAscending(AbstractTextComponent textComponent);
  void changeFirstAndLastLexemesInSentences(AbstractTextComponent textComponent);
}
