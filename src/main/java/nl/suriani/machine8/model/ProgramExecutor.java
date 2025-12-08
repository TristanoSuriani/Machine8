package nl.suriani.machine8.model;

@FunctionalInterface
public interface ProgramExecutor<S extends ExecutionState> {
    static <A extends AST, S extends ExecutionState> ProgramExecutor<S> of(LanguageFrontend<A> frontend, Interpreter<A,S> interpreter) {
        return (source, initialState) -> {
            var frontendResult = frontend.parse(source);
            return switch (frontendResult) {
                case FrontendResult.LexerError<A> v -> new ExecutionResult.FrontendError<S>(v.message(), FrontendTag.LEXER, v.line(), v.column());
                case FrontendResult.ParserError<A> v ->  new ExecutionResult.FrontendError<S>(v.message(), FrontendTag.PARSER, v.line(), v.column());
                case FrontendResult.Ok<A> v -> {
                    var runtimeResult = interpreter.interpret(v.ast(), initialState);
                    yield switch (runtimeResult) {
                        case RuntimeResult.Error<S> v1 -> new ExecutionResult.RuntimeError<S>(v1.message());
                        case RuntimeResult.Ok<S> v1 -> new ExecutionResult.Ok<S>(v1.finalState());
                    };
                }
            };
        };
    }

    ExecutionResult<S> execute(String program, S initialState);
}
