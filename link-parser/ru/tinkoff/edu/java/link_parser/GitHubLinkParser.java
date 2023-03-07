package ru.tinkoff.edu.java.link_parser;

import java.net.URL;

public class GitHubLinkParser extends Parser {
    public GitHubLinkParser(Parser nextParser) {
        super(nextParser);
    }

    @Override
    public ParsedObject parseLinkImpl(URL url) {
        if (url.getHost().equals("github.com")) {
            String[] arr = url.getPath().split("/");
            if (arr.length > 2) {
                return new GithubRepository(arr[1], arr[2]);
            }
            return null;
        }
        return null;
    }
}
