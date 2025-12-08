package nl.suriani.machine8.model;

import java.util.stream.Stream;

public sealed interface LexerResult<T extends Token> {
    record Ok<T extends Token>(Stream<T> tokens) implements LexerResult<T> {}
    record Error<T extends Token>(String message, int line, int column) implements LexerResult<T> {}
}
