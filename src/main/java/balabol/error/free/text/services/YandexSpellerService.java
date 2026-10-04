package balabol.error.free.text.services;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;

import java.util.List;

public interface YandexSpellerService {
    List<List<SpellResult>> sendTextRequest(TextRequest textRequest);
}
