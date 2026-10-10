package balabol.error.free.text.unit.services;

import balabol.error.free.text.unit.dtos.SpellResult;
import balabol.error.free.text.unit.dtos.TextRequest;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface YandexSpellerService {
    List<List<SpellResult>> sendTextRequest(TextRequest textRequest);

    MultiValueMap<String, String> convertToMultiValueMap(TextRequest textRequest);
}
