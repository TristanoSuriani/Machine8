package nl.suriani.machine12.model;

public sealed interface RuntimeResult<S extends ExecutionState> {
    record Ok<S extends ExecutionState>(S finalState) implements RuntimeResult<S> {}
    record Error<S extends ExecutionState>(String message) implements RuntimeResult<S> {}
}