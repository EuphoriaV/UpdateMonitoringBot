package ru.tinkoff.edu.java.link_parser;

sealed interface ParsedObject permits GitHubRepository, StackOverflowQuestion {
}
