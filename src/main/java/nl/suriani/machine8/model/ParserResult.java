package nl.suriani.machine8.model;

public sealed interface ParserResult<A extends AST> {
    record Ok<A extends AST>(A ast) implements ParserResult<A> {}
    record Error<A extends AST>(String message, int line, int column) implements ParserResult<A> {}
}
