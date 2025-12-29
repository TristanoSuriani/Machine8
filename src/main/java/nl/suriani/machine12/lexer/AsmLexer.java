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
            if (first.character() == ';') {
                var token = insideInlineComment(remainingCharacters, first.lineNumber());
                tokens.add(token);
            }
        }
        return tokens;
    }

    private AsmToken insideStringLiteral(Deque<LocatedChar> remainingCharacters) {
        return null;
    }

    private AsmToken insideInlineComment(Deque<LocatedChar> remainingCharacters, int row) {
        while (!remainingCharacters.isEmpty()) {
            var first = remainingCharacters.pop();
            if (first.character() == '\n') {
                return new AsmToken.EOL(first.lineNumber(), first.columnNumber());
            }
        }
        return new AsmToken.EOL(row + 1, 1);
    }

    private AsmToken insideDirective(Deque<LocatedChar> remainingCharacters) {
        return null;
    }

    private AsmToken insideIdentifier(Deque<LocatedChar> remainingCharacters) {
        return null;
    }
}
