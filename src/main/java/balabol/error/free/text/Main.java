package balabol.error.free.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        String regex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("https://music.yandex.ru/collectionduifhbg  без тебя моя жизнь");
        System.out.println(matcher.find());

        System.out.println("drjgub".matches(regex));
        System.out.println("https://music.yandex.ru/collectionduifhbg".matches(regex));


    }
}
