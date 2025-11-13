package by.zgirskaya.course.service.impl;

import by.zgirskaya.course.component.AbstractTextComponent;
import by.zgirskaya.course.component.TextComponentType;
import by.zgirskaya.course.component.TextComposite;
import by.zgirskaya.course.exception.CustomTextException;
import by.zgirskaya.course.service.TextService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class TextServiceImpl implements TextService {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public int findMaxSentenceCountWithSameWords(AbstractTextComponent textComponent) throws CustomTextException {
    logger.info("Finding max sentence count with same words. Component type: {}",
            textComponent.getComponentType());

    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    List<Set<String>> sentencesWords = collectAllSentencesWords(textComponent);
    int result = findMaxSentencesWithSameWords(sentencesWords);

    logger.info("Max sentence count with same words: {}", result);

    return result;

  }

  @Override
  public void displaySentencesByLexemeCountAscending(AbstractTextComponent textComponent) throws CustomTextException {
    logger.info("Displaying sentences by lexeme count ascending. Component type: {}",
            textComponent.getComponentType());

    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    List<SentenceInfo> sentencesInfo = collectSentencesInfo(textComponent);
    sentencesInfo.sort(Comparator.comparingInt(SentenceInfo::lexemeCount));

    System.out.println("Sentences in the ascending order by lexemes count:");
    for (SentenceInfo info : sentencesInfo) {
      System.out.println("Lexemes count: " + info.lexemeCount() + " -> " + info.sentenceText());
    }

    logger.info("Displayed {} sentences sorted by lexeme count", sentencesInfo.size());
  }

  @Override
  public AbstractTextComponent changeFirstAndLastLexemesInSentences(AbstractTextComponent textComponent) throws CustomTextException {
    logger.info("Changing first and last lexemes in sentences. Component type: {}",
            textComponent.getComponentType());

    if (textComponent.getComponentType() != TextComponentType.PARAGRAPH &&
            textComponent.getComponentType() != TextComponentType.SENTENCE) {
      throw new CustomTextException("Component must be paragraph or sentence type");
    }

    TextComposite copy = (TextComposite) textComponent.makeCopy();
    logger.debug("Created copy of component");

    changeLexemesInCopy(copy);
    logger.info("Successfully changed lexemes in copy");

    return copy;
  }

  private void changeLexemesInCopy(TextComposite copy) {
    if (copy.getComponentType() == TextComponentType.SENTENCE) {

      changeFirstAndLastLexemesInSentence(copy);
    } else {

      for (AbstractTextComponent child : copy.getChildComponents()) {
        if (child instanceof TextComposite composite) {
          changeLexemesInCopy(composite);
        }
      }
    }
  }

  private void changeFirstAndLastLexemesInSentence(TextComposite sentence) {
    List<AbstractTextComponent> children = sentence.getChildComponents();
    List<AbstractTextComponent> lexemes = new ArrayList<>();

    for (AbstractTextComponent child : children) {
      if (child instanceof TextComposite &&
              child.getComponentType() == TextComponentType.LEXEME) {
        lexemes.add(child);
      }
    }

    if (lexemes.size() >= 2) {
      int firstIndex = children.indexOf(lexemes.getFirst());
      int lastIndex = children.indexOf(lexemes.getLast());

      if (firstIndex != -1 && lastIndex != -1) {
        List<AbstractTextComponent> newChildren = new ArrayList<>(children);
        Collections.swap(newChildren, firstIndex, lastIndex);
        sentence.setChildComponents(newChildren);
      }
    }
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

        for (AbstractTextComponent child : composite.getChildComponents()) {
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

      for (AbstractTextComponent child : composite.getChildComponents()) {
        extractWords(child, words);
      }
    } else if (component.getComponentType() == TextComponentType.WORD) {
      words.add(component.toString().toLowerCase());
    }
  }

  private int findMaxSentencesWithSameWords(List<Set<String>> sentencesWords) {

    Set<String> allWords = sentencesWords.stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

    int maxCount = 0;

    for (var word : allWords) {
      int currentSentenceCount = 0;

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

        for (AbstractTextComponent child : composite.getChildComponents()) {
          collectSentencesInfo(child, sentencesInfo);
        }
      }
    }
  }

  private int countLexemesInSentence(TextComposite sentence) {
    return countLexemes(sentence, 0);
  }

  private int countLexemes(AbstractTextComponent component, int count) {

    if (component instanceof TextComposite composite) {

      if (composite.getComponentType() == TextComponentType.LEXEME) {
        return count + 1;
      } else {

        for (AbstractTextComponent child : composite.getChildComponents()) {
          count = countLexemes(child, count);
        }
      }
    }
    return count;
  }

  private record SentenceInfo(String sentenceText, int lexemeCount) {}
}