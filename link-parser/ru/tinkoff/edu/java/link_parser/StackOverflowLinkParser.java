package ru.tinkoff.edu.java.link_parser;

import java.net.URL;

public class StackOverflowLinkParser extends Parser {
    public StackOverflowLinkParser(Parser nextParser) {
        super(nextParser);
    }

    @Override
    public ParsedObject parseLinkImpl(URL url) {
        if (url.getHost().equals("stackoverflow.com")) {
            String[] arr = url.getPath().split("/");
            try {
                if (arr.length > 2 && arr[1].equals("questions")) {
                    long id = Long.parseLong(arr[2]);
                    return new StackOverflowQuestion(id);
                }
                return null;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
