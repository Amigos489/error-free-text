package balabol.error.free.text.unit.services;

import balabol.error.free.text.unit.dtos.SpellResult;
import balabol.error.free.text.unit.dtos.TextRequest;
import balabol.error.free.text.unit.enums.LanguageType;
import balabol.error.free.text.unit.services.impl.TextCorrectionServiceImpl;
import balabol.error.free.text.unit.services.impl.YandexSpellerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TextCorrectionServiceImpl")
public class TextCorrectionServiceImplTest {

    @InjectMocks
    private TextCorrectionServiceImpl textCorrectionService;

    @Mock
    private YandexSpellerServiceImpl yandexSpellerService;

    private static List<List<SpellResult>> response;

    private static TextRequest testTextRequest;

    @BeforeAll
    public static void setBeforeAll() {
        response = new ArrayList<>();
        response.add(new ArrayList<>());
        response.get(0).add(new SpellResult(1, 0, 0,0,6,"Превет", new ArrayList<>()));
        response.get(0).get(0).s().add("Привет");

        testTextRequest = new TextRequest("Превет", 0, LanguageType.RU);
    }

    @Test
    @DisplayName("Given uncorrected text When correctSpellInText Then corrected text")
    public void uncorrectedText_correctSpellInText_correctedText() {

        String uncorrectedText = "Превет";

        Mockito.when(yandexSpellerService
                .sendTextRequest(any(TextRequest.class)))
                .thenReturn(response);

        String actual = textCorrectionService
                .correctSpellInText(uncorrectedText, LanguageType.RU);

        Assertions.assertEquals("Привет", actual);
    }

    @Test
    @DisplayName("Given uncorrected text When processText Then uncorrected text")
    public void uncorrectedText_processText_correctedText() {
        Mockito.when(yandexSpellerService.sendTextRequest(any(TextRequest.class))).thenReturn(response);

        String actual = textCorrectionService.processText(testTextRequest, new StringBuilder());

        Assertions.assertEquals("Привет", actual);
    }

    @Test
    @DisplayName("When sendRequestOnCheckSpellText Then SpellResults")
    public void sendRequestOnCheckSpellText_SpellResults() {

        List<List<SpellResult>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.get(0).add(new SpellResult(1, 0, 0,0,6,"Превет", new ArrayList<>()));
        expected.get(0).get(0).s().add("Привет");

        Mockito.when(yandexSpellerService.sendTextRequest(any(TextRequest.class))).thenReturn(expected);

        List<List<SpellResult>> actual = textCorrectionService
                .sendRequestOnCheckSpellText(testTextRequest);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Given in text contains regex When processResponseFromTextSpellCheck Then true")
    public void processResponseFromTextSpellCheck() {
        StringBuilder actual = new StringBuilder();

        actual = textCorrectionService.processResponseFromTextSpellCheck(response, actual);

        Assertions.assertEquals("Привет", actual.toString());
    }

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

    @Test
    @DisplayName("Given original word short word for replace on 1 When changeOffset Then negative 1")
    public void originalWordLongerWordForReplaceOn1_changeOffset_negative1() {

        String originalWord = "words";

        String wordForReplace = "word";

        int offset = 0;

        int actual = textCorrectionService.changeOffset(originalWord, wordForReplace, offset);

        Assertions.assertEquals(-1, actual);
    }

    @Test
    @DisplayName("Given original word longer word for replace on 1 When changeOffset Then 1")
    public void originalWordLongerWordForReplaceOn1_changeOffset_1() {

        String originalWord = "word";

        String wordForReplace = "words";

        int offset = 0;

        int actual = textCorrectionService.changeOffset(originalWord, wordForReplace, offset);

        Assertions.assertEquals(1, actual);
    }

    @Test
    @DisplayName("Given text with size = 10 When splitTextOnPart Then 5")
    public void textWithSize10_splitTextOnPart_5() {

        String str = "0123456789";

        int maxLengthPart = 2;

        int actual = textCorrectionService.splitTextOnPart(str, maxLengthPart).size();

        Assertions.assertEquals(5, actual);
    }

    @Test
    @DisplayName("Given text contains urls and numbers When getOptionsForText Then 6")
    public void textContainsUrlsAndNumbers_getOptionsForText_6() {

        String text = "text test 1 https://text.test";

        Assertions.assertEquals(6, textCorrectionService.getOptionsForText(text));
    }

    @Test
    @DisplayName("Given text no contains urls and numbers When getOptionsForText Then 0")
    public void textNoContainsUrlsAndNumbers_getOptionsForText_0() {

        String text = "text test";

        Assertions.assertEquals(0, textCorrectionService.getOptionsForText(text));
    }

    @Test
    @DisplayName("Given text contains urls and no contains numbers When getOptionsForText Then 4")
    public void textContainsUrlsAndNoContains_numbers_getOptionsForText_4() {
        String text = "text test https://text.test";

        Assertions.assertEquals(4, textCorrectionService.getOptionsForText(text));
    }

    @Test
    @DisplayName("Given text no contains urls and contains numbers When getOptionsForText Then 2")
    public void textNoContainsUrlsAndContains_numbers_getOptionsForText_2() {
        String text = "text 1234";

        Assertions.assertEquals(2, textCorrectionService.getOptionsForText(text));
    }
}
