package ru.tinkoff.edu.java.link_parser;

import java.net.MalformedURLException;
import java.net.URL;

public class LinkParser {
    public static ParsedObject parseLink(String link) {
        try {
            URL url = new URL(link);
            Parser parser = new GitHubLinkParser(new StackOverflowLinkParser(null));
            return parser.parseLink(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
