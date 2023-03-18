package ru.tinkoff.edu.java.link_parser;

import java.net.URL;

abstract class Parser {
    protected Parser nextParser;

    public Parser(Parser nextParser) {
        this.nextParser = nextParser;
    }

    abstract ParsedObject parseLinkImpl(URL url);

    public ParsedObject parseLink(URL url) {
        ParsedObject res = parseLinkImpl(url);
        if (res != null) {
            return res;
        }
        if (nextParser != null) {
            return nextParser.parseLink(url);
        }
        return null;
    }
}
