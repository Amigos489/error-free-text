package balabol.error.free.text.services;

import balabol.error.free.text.services.impl.TextCorrectionServiceImpl;
import balabol.error.free.text.services.impl.YandexSpellerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TextCorrectionServiceImpl")
public class TextCorrectionServiceImplTest {

    @InjectMocks
    private TextCorrectionServiceImpl textCorrectionService;

    @Mock
    private YandexSpellerServiceImpl yandexSpellerService;

    @Test
    @DisplayName("Given text with numbers When ifNumbersAreInTheText Then true")
    public void textWithNumbers_ifNumbersAreInTheText_true() {

        String textTest = "text test 123";

        boolean actual = textCorrectionService.ifNumbersAreInTheText(textTest);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Given text without numbers When ifNumbersAreInTheText Then false")
    public void textWithoutNumbers_ifNumbersAreInTheText_false() {

        String textTest = "text test";

        boolean actual = textCorrectionService.ifNumbersAreInTheText(textTest);

        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("Given text with urls When ifUrlsAreInTheText Then true")
    public void textWithUrls_ifUrlsAreInTheText_true() {

        String textTest = "text test https://text.test";

        boolean actual = textCorrectionService.ifUrlsAreInTheText(textTest);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Given text without urls When ifUrlsAreInTheText Then false")
    public void textWithoutUrls_ifUrlsAreInTheText_false() {

        String textTest = "text test";

        boolean actual = textCorrectionService.ifUrlsAreInTheText(textTest);

        Assertions.assertFalse(actual);
    }
}
