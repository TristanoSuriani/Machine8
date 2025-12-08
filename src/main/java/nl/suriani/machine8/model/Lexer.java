package nl.suriani.machine8.model;

import java.util.stream.Stream;

@FunctionalInterface
public interface Lexer<T extends Token> {
    LexerResult<T> lex(Stream<LocatedChar> chars);
}
