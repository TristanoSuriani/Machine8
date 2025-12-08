package nl.suriani.machine8.model;

import java.util.stream.Stream;

@FunctionalInterface
public interface SourceMapper {
    Stream<LocatedChar> map(String source);
}
