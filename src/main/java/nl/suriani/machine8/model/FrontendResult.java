package nl.suriani.machine8.model;

public sealed interface FrontendResult<A extends AST> {
    record Ok<A extends AST>(A ast) implements FrontendResult<A> {}
    record LexerError<A extends AST>(String message, int line, int column) implements FrontendResult<A> {}
    record ParserError<A extends AST>(String message, int line, int column) implements FrontendResult<A> {}
}