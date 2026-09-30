package balabol.error.free.text.services;

public interface TextCorrectionService {
    String correctText(String uncorrectedText);
    boolean ifNumbersAreInTheText(String text);
    boolean ifUrlsAreInTheText(String text);
}
