package balabol.error.free.text.services;

import balabol.error.free.text.dtos.SpellResult;

import java.util.List;

public interface YandexSpellerService {
    List<List<SpellResult>> sendTextResult(List<String> text, int options);
}
