package nl.suriani.machine12.model;

public sealed interface ExecutionResult<S extends ExecutionState> {
    record Ok<S extends ExecutionState>(S finalState) implements ExecutionResult<S> {}
    record FrontendError<S extends ExecutionState>(String message, FrontendTag tag, int line, int column) implements ExecutionResult<S> {}
    record RuntimeError<S extends ExecutionState>(String message) implements ExecutionResult<S> {}
}
