package nl.suriani.machine12.model;

@FunctionalInterface
public interface Interpreter<A extends AST, S extends ExecutionState> {
    RuntimeResult<S> interpret(A ast, S initialState);
}
