package nl.suriani.machine12.model;

import java.util.stream.Stream;

@FunctionalInterface
public interface SourceMapper {
    Stream<LocatedChar> map(String source);
}
