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
    @DisplayName("Given in text contains regex When ifRegexAreInTheText Then true")
    public void InTextContainsRegex_ifRegexAreInTheText_true() {

        String regex = "[0-9]+";

        String textTest = "text test123";

        boolean actual = textCorrectionService.ifRegexAreInTheText(textTest, regex);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Given in text not contains regex When ifRegexAreInTheText Then false")
    public void InTextNotContainsRegex_ifRegexAreInTheText_false() {

        String regex = "[0-9]+";

        String textTest = "text test";

        boolean actual = textCorrectionService.ifRegexAreInTheText(textTest, regex);

        Assertions.assertFalse(actual);
    }
}
