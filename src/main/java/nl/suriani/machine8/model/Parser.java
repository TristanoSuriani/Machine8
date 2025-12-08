package nl.suriani.machine8.model;

import java.util.stream.Stream;

@FunctionalInterface
public interface Parser<T extends Token, A extends AST> {
    ParserResult<A> parse(Stream<T> programStream);
}
