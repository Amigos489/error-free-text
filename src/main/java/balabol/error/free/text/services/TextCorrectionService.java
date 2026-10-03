package balabol.error.free.text.services;

public interface TextCorrectionService {
    String correctText(String uncorrectedText);
    boolean ifRegexAreInTheText(String text, String regex);
}
