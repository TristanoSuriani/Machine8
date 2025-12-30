package nl.suriani.machine12.lexer;

import nl.suriani.machine12.model.Lexer;
import nl.suriani.machine12.model.LexerResult;
import nl.suriani.machine12.model.LocatedChar;

import java.util.*;
import java.util.stream.Stream;

public class AsmLexer implements Lexer<AsmToken> {

    @Override
    public LexerResult<AsmToken> lex(Stream<LocatedChar> chars) {
        var characters = new ArrayDeque<>(chars.toList());
        var tokens = outside(characters);
        return new LexerResult.Ok<>(tokens.stream());
    }

    private List<AsmToken> outside(Deque<LocatedChar> remainingCharacters) {
        var tokens = new ArrayList<AsmToken>();

        while (!remainingCharacters.isEmpty()) {
            var first = remainingCharacters.pop();
            switch (first.character()) {
                case ';' -> insideInlineComment(remainingCharacters, tokens);
                case '.' -> insideDirective(remainingCharacters, tokens);
                case '"' -> insideStringLiteral(remainingCharacters, tokens);
            }
        }
        if (tokens.isEmpty() || !(tokens.getLast() instanceof AsmToken.EOL)) {
            tokens.add(new AsmToken.EOL(tokens.isEmpty() ? 1 : tokens.getLast().row() + 1, 1));
        }
        return tokens;
    }

    private void insideStringLiteral(Deque<LocatedChar> remainingCharacters, List<AsmToken> tokens) {
        if (remainingCharacters.isEmpty()) {
            throw new IllegalStateException("Empty string literal");
        }
        var row = remainingCharacters.peek().lineNumber();
        var column = remainingCharacters.peek().columnNumber() - 1;
        var tokenBuilder = new StringBuilder("\"");
        while (!remainingCharacters.isEmpty()) {
            var first = remainingCharacters.pop();
            if (first.character() == '"') {
                tokenBuilder.append('"');
                tokens.add(new AsmToken.StringLiteral(tokenBuilder.toString(), row, column));
                return;
            }
            tokenBuilder.append(first.character());
        }
    }

    private void insideInlineComment(Deque<LocatedChar> remainingCharacters, List<AsmToken> tokens) {
        while (!remainingCharacters.isEmpty()) {
            var first = remainingCharacters.pop();
            if (first.character() == '\n') {
                remainingCharacters.push(first);
                return;
            }
        }
    }

    private void insideDirective(Deque<LocatedChar> remainingCharacters, List<AsmToken> tokens) {
        if (remainingCharacters.isEmpty()) {
            throw new IllegalStateException("Empty directive");
        }
        var row = remainingCharacters.peek().lineNumber();
        var column = remainingCharacters.peek().columnNumber() - 1;
        var tokenBuilder = new StringBuilder(".");

        while (!remainingCharacters.isEmpty()) {
            var first = remainingCharacters.pop();
            if (isDelimiter(first.character())) {
                remainingCharacters.push(first);
                tokens.add(new AsmToken.Directive(tokenBuilder.toString(), row, column));
                return;
            }
            tokenBuilder.append(first.character());
        }
    }

    private AsmToken insideIdentifier(Deque<LocatedChar> remainingCharacters) {
        return null;
    }

    private boolean isDelimiter(char c) {
        return c == ' '
                || c == '\r'
                || c == '\n'
                || c == '\t'
                || c == '{'
                || c == '}'
                || c == '.'
                || c == ';';
    }
}
