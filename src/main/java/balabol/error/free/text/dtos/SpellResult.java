package balabol.error.free.text.dtos;

import java.util.List;

public record SpellResult (
    int code,
    int pos,
    int row,
    int col,
    int len,
    String word,
    List<String> s
) {}
