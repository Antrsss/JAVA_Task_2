package by.zgirskaya.course.service.impl;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.component.TextLeaf;
import by.zgirskaya.course.exception.CustomTextException;
import by.zgirskaya.course.service.TextService;

import java.util.*;
import java.util.stream.Collectors;

public class TextServiceImpl implements TextService {

  @Override
  public int findMaxSentenceCountWithSameWords(AbstractTextComponent textComponent) throws CustomTextException {
    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    List<Set<String>> sentencesWords = collectAllSentencesWords(textComponent);

    return findMaxSentencesWithSameWords(sentencesWords);
  }

  @Override
  public void displaySentencesByLexemeCountAscending(AbstractTextComponent textComponent) throws CustomTextException {
    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    List<SentenceInfo> sentencesInfo = collectSentencesInfo(textComponent);

    sentencesInfo.sort(Comparator.comparingInt(SentenceInfo::lexemeCount));

    System.out.println("Предложения в порядке возрастания количества лексем:");
    for (SentenceInfo info : sentencesInfo) {
      System.out.println("Лексем: " + info.lexemeCount() + " -> " + info.sentenceText());
    }
  }

  @Override
  public void changeFirstAndLastLexemesInSentences(AbstractTextComponent textComponent) throws CustomTextException {
    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    changeLexemesInAllSentences((TextComposite) textComponent);
  }

  private List<Set<String>> collectAllSentencesWords(AbstractTextComponent component) {
    List<Set<String>> sentencesWords = new ArrayList<>();
    collectSentencesWords(component, sentencesWords);
    return sentencesWords;
  }

  private void collectSentencesWords(AbstractTextComponent component, List<Set<String>> sentencesWords) {
    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.SENTENCE) {
        Set<String> words = extractWordsFromSentence(composite);
        sentencesWords.add(words);
      } else {
        for (AbstractTextComponent child : composite.getChildren()) {
          collectSentencesWords(child, sentencesWords);
        }
      }
    }
  }

  private Set<String> extractWordsFromSentence(TextComposite sentence) {
    Set<String> words = new HashSet<>();
    extractWords(sentence, words);
    return words;
  }

  private void extractWords(AbstractTextComponent component, Set<String> words) {
    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.WORD) {
        words.add(composite.toString().toLowerCase());
      } else {
        for (AbstractTextComponent child : composite.getChildren()) {
          extractWords(child, words);
        }
      }
    } else if (component instanceof TextLeaf &&
            component.getComponentType() == TextComponentType.WORD) {
      words.add(component.toString().toLowerCase());
    }
  }

  private int findMaxSentencesWithSameWords(List<Set<String>> sentencesWords) {

    Set<String> allWords = sentencesWords.stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

    int maxCount = 0;

    for (var word : allWords) {
      int currentSentenceCount = 1;

      for (var sentenceWords : sentencesWords) {
        if (sentenceWords.contains(word)) {
          currentSentenceCount++;
        }
      }

      maxCount = Math.max(maxCount, currentSentenceCount);
    }

    return maxCount;
  }

  private List<SentenceInfo> collectSentencesInfo(AbstractTextComponent component) {
    List<SentenceInfo> sentencesInfo = new ArrayList<>();
    collectSentencesInfo(component, sentencesInfo);
    return sentencesInfo;
  }

  private void collectSentencesInfo(AbstractTextComponent component, List<SentenceInfo> sentencesInfo) {
    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.SENTENCE) {
        int lexemeCount = countLexemesInSentence(composite);
        String sentenceText = composite.toString().trim();
        sentencesInfo.add(new SentenceInfo(sentenceText, lexemeCount));
      } else {
        for (AbstractTextComponent child : composite.getChildren()) {
          collectSentencesInfo(child, sentencesInfo);
        }
      }
    }
  }

  private int countLexemesInSentence(TextComposite sentence) {
    int count = 0;
    count = countLexemes(sentence, count);
    return count;
  }

  private int countLexemes(AbstractTextComponent component, int count) {
    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.LEXEME) {
        return count + 1;
      } else {
        for (AbstractTextComponent child : composite.getChildren()) {
          count = countLexemes(child, count);
        }
      }
    }
    return count;
  }

  private void changeLexemesInAllSentences(TextComposite component) {
    if (component.getComponentType() == TextComponentType.SENTENCE) {
      changeFirstAndLastLexemes(component);
    } else {
      for (AbstractTextComponent child : component.getChildren()) {
        if (child instanceof TextComposite) {
          changeLexemesInAllSentences((TextComposite) child);
        }
      }
    }
  }

  private void changeFirstAndLastLexemes(TextComposite sentence) {
    List<AbstractTextComponent> lexemes = findLexemesInSentence(sentence);

    if (lexemes.size() >= 2) {
      AbstractTextComponent firstLexeme = lexemes.getFirst();
      AbstractTextComponent lastLexeme = lexemes.getLast();

      int firstIndex = findComponentIndex(sentence, firstLexeme);
      int lastIndex = findComponentIndex(sentence, lastLexeme);

      if (firstIndex != -1 && lastIndex != -1) {
        List<AbstractTextComponent> children = sentence.getChildren();
        children.set(firstIndex, lastLexeme);
        children.set(lastIndex, firstLexeme);
      }
    }
  }

  private List<AbstractTextComponent> findLexemesInSentence(TextComposite sentence) {
    List<AbstractTextComponent> lexemes = new ArrayList<>();
    findLexemes(sentence, lexemes);
    return lexemes;
  }

  private void findLexemes(AbstractTextComponent component, List<AbstractTextComponent> lexemes) {
    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.LEXEME) {
        lexemes.add(composite);
      } else {
        for (AbstractTextComponent child : composite.getChildren()) {
          findLexemes(child, lexemes);
        }
      }
    }
  }

  private int findComponentIndex(TextComposite parent, AbstractTextComponent child) {
    List<AbstractTextComponent> children = parent.getChildren();
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i) == child) {
        return i;
      }
    }
    return -1;
  }

  private record SentenceInfo(String sentenceText, int lexemeCount) {}
}