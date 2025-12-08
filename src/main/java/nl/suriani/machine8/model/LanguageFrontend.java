package nl.suriani.machine8.model;

@FunctionalInterface
public interface LanguageFrontend<A extends AST> {
    FrontendResult<A> parse(String source);

    static <T extends Token, A extends AST> LanguageFrontend<A> of(
            SourceMapper sourceMapper,
            Lexer<T> lexer,
            Parser<T, A> parser) {

        return source -> {
            var chars = sourceMapper.map(source);
            var lexerResult = lexer.lex(chars);
            return switch (lexerResult) {
                case LexerResult.Error<T> v -> new FrontendResult.LexerError<>(v.message(), v.line(), v.column());
                case LexerResult.Ok<T> v -> {
                    var parserResult = parser.parse(v.tokens());
                    yield switch (parserResult) {
                        case ParserResult.Error<A> v1 -> new FrontendResult.ParserError<>(v1.message(), v1.line(), v1.column());
                        case ParserResult.Ok<A> v1 -> new FrontendResult.Ok<>(v1.ast());
                    };
                }
            };
        };
    }
}
