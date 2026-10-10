package balabol.error.free.text.unit.services;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.services.impl.TextCorrectionServiceImpl;
import balabol.error.free.text.services.impl.YandexSpellerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("unit tests for TextCorrectionServiceImpl")
public class TextCorrectionServiceImplTest {

    private static final TextRequest TEXT_REQUEST = new TextRequest("Превет", 0, LanguageType.RU);

    private static List<List<SpellResult>> LIST_SPELL_RESULT;

    @InjectMocks
    private TextCorrectionServiceImpl textCorrectionService;

    @Mock
    private YandexSpellerServiceImpl yandexSpellerService;

    @BeforeAll
    public static void setBeforeAll() {
        initializeListSpellResult();
    }

    public static void initializeListSpellResult() {
        LIST_SPELL_RESULT = new ArrayList<>();
        LIST_SPELL_RESULT.add(new ArrayList<>());
        LIST_SPELL_RESULT.get(0).add(
                new SpellResult(1, 0, 0,0,6,"Превет",
                        new ArrayList<>()));
        LIST_SPELL_RESULT.get(0).get(0).s().add("Привет");
    }

    @Test
    @DisplayName("Given text with spell errors When method correctSpellInText " +
            "Then text without spell errors")
    public void textWithSpellErrors_correctSpellInText_textWithoutSpellErrors() {

        String textWithSpellErrors = "Превет";

        Mockito.when(yandexSpellerService.sendTextRequest(TEXT_REQUEST))
                .thenReturn(LIST_SPELL_RESULT);

        String actualText = textCorrectionService
                .correctSpellInText(textWithSpellErrors, LanguageType.RU);

        Assertions.assertEquals("Привет", actualText);
    }

    @Test
    @DisplayName("Given text with spell errors When method processText Then text without spell errors")
    public void textWithSpellErrors_processText_textWithoutSpellErrors() {
        Mockito.when(yandexSpellerService.sendTextRequest(TEXT_REQUEST))
                .thenReturn(LIST_SPELL_RESULT);

        String actualText = textCorrectionService.processText(TEXT_REQUEST, new StringBuilder());

        Assertions.assertEquals("Привет", actualText);
    }

    @Test
    @DisplayName("When sendRequestOnCheckSpellText Then method list spell result")
    public void sendRequestOnCheckSpellText_listSpellResult() {

        List<List<SpellResult>> expectedListSpellResult = LIST_SPELL_RESULT;

        Mockito.when(yandexSpellerService.sendTextRequest(TEXT_REQUEST))
                .thenReturn(expectedListSpellResult);

        List<List<SpellResult>> actualListSpellResult = textCorrectionService
                .sendRequestOnCheckSpellText(TEXT_REQUEST);

        Assertions.assertEquals(expectedListSpellResult, actualListSpellResult);
    }

    @Test
    @DisplayName("Given list spell results and text with spell errors " +
            "When method processResponseFromTextSpellCheck " +
            "Then text without spell errors")
    public void listSpellResultsAndTextWithSpellErrors_processResponseFromTextSpellCheck_textWithoutSpellErrors() {

        StringBuilder textWithoutSpellErrorsBuilder = textCorrectionService
                .processResponseFromTextSpellCheck(LIST_SPELL_RESULT,
                        new StringBuilder("Превет"));

        Assertions.assertEquals("Привет", textWithoutSpellErrorsBuilder.toString());
    }

    @Test
    @DisplayName("Given in text contains regex When method ifRegexAreInTheText Then true")
    public void InTextContainsRegex_ifRegexAreInTheText_true() {

        String regex = "[0-9]+";

        String textWithRegex = "text123";

        boolean actualCheckResult = textCorrectionService
                .ifRegexAreInTheText(textWithRegex, regex);

        Assertions.assertTrue(actualCheckResult);
    }

    @Test
    @DisplayName("Given in text not contains regex When method ifRegexAreInTheText Then false")
    public void InTextNotContainsRegex_ifRegexAreInTheText_false() {

        String regex = "[0-9]+";

        String textWithoutRegex = "text";

        boolean actualCheckResult = textCorrectionService
                .ifRegexAreInTheText(textWithoutRegex, regex);

        Assertions.assertFalse(actualCheckResult);
    }

    @Test
    @DisplayName("Given original word one symbol shorter and offset equal zero" +
            "When method changeOffset " +
            "Then offset equal negative one")
    public void originalWordOneSymbolShorterAndOffsetEqualZero_changeOffset_offsetEqualOne() {

        int offset = 0;

        int actual = textCorrectionService.changeOffset("word", "words", offset);

        Assertions.assertEquals(1, actual);
    }

    @Test
    @DisplayName("Given original word one symbol longer and offset equal zero" +
            "When method changeOffset " +
            "Then offset equal negative one")
    public void originalWordOneSymbolLongerAndOffsetEqualZero_changeOffset_offsetEqualNegativeOne() {

        int offset = 0;

        int actual = textCorrectionService.changeOffset("words", "word", offset);

        Assertions.assertEquals(-1, actual);
    }

    @Test
    @DisplayName("Given text with size ten characters and max length part equal two " +
            "When method splitTextOnPart Then five")
    public void textWithSizeTenCharactersAndMaxLengthPartEqualTwo_splitTextOnPart_five() {

        String textWithSizeTenCharacters = "0123456789";

        int maxLengthPart = 2;

        int actual = textCorrectionService.splitTextOnPart(textWithSizeTenCharacters, maxLengthPart).size();

        Assertions.assertEquals(5, actual);
    }

    @Test
    @DisplayName("Given text contains urls and numbers " +
            "When method getOptionsForText Then six")
    public void textContainsUrlsAndNumbers_getOptionsForText_six() {

        String textContainsUrlsAndNumbers = "text https://text.test 123";

        Assertions.assertEquals(6, textCorrectionService
                .getOptionsForText(textContainsUrlsAndNumbers));
    }

    @Test
    @DisplayName("Given text no contains urls and numbers " +
            "When method getOptionsForText Then zero")
    public void textNoContainsUrlsAndNumbers_getOptionsForText_zero() {

        String textNoContainsUrlsAndNumbers = "text";

        Assertions.assertEquals(0, textCorrectionService
                .getOptionsForText(textNoContainsUrlsAndNumbers));
    }

    @Test
    @DisplayName("Given text contains urls and no contains numbers " +
            "When method getOptionsForText Then four")
    public void textContainsUrlsAndNoContainsNumbers_getOptionsForText_four() {
        String textContainsUrlsAndNoContainsNumbers = "text https://text.test";

        Assertions.assertEquals(4, textCorrectionService
                .getOptionsForText(textContainsUrlsAndNoContainsNumbers));
    }

    @Test
    @DisplayName("Given text no contains urls and contains numbers " +
            "When method getOptionsForText Then two")
    public void textNoContainsUrlsAndContains_numbers_getOptionsForText_two() {
        String textNoContainsUrlsAndContainsNumbers = "text 1234";

        Assertions.assertEquals(2, textCorrectionService
                .getOptionsForText(textNoContainsUrlsAndContainsNumbers));
    }
}
