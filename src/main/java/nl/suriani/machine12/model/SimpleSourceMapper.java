package nl.suriani.machine12.model;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class SimpleSourceMapper implements SourceMapper {
    @Override
    public Stream<LocatedChar> map(String source) {
        var chars = new ArrayList<LocatedChar>(source.length());
        int line = 1;
        int col  = 1;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            chars.add(new LocatedChar(c, line, col));
            if (c == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        return chars.stream();
    }
}