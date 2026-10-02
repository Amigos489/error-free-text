package balabol.error.free.text.services;

import balabol.error.free.text.dtos.SpellResult;

import java.util.List;

public interface YandexSpellerService {
    List<SpellResult> sendTextForCorrection(String[] text, int options);

    void sendTextRequest(String[] text, int options);

    List<List<SpellResult>> sendTextResult(List<String> text, int options);
}
