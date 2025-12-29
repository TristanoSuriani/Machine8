package nl.suriani.machine12.lexer;

import nl.suriani.machine12.model.LexerResult;
import nl.suriani.machine12.model.LocatedChar;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AsmLexerTest {
    private final AsmLexer asmLexer = new AsmLexer();

    @Test
    void nothing() {
        var program = "";
        var result = asmLexer.lex(programToStream(program));
        assertInstanceOf(LexerResult.Ok.class, result);
    }

    @Test
    void aCommentAndEndOfLine() {
        var program = "; this is a serious comment\n";
        var result = asmLexer.lex(programToStream(program));
        assertInstanceOf(LexerResult.Ok.class, result);
        var ok = (LexerResult.Ok) result;
        var tokens = ok.tokens().toList();
        assertEquals(1, tokens.size());
        assertInstanceOf(AsmToken.EOL.class, tokens.getFirst());
    }

    @Test
    void aCommentAndNoEndOfLine() {
        var program = "; this is a serious comment";
        var result = asmLexer.lex(programToStream(program));
        assertInstanceOf(LexerResult.Ok.class, result);
        var ok = (LexerResult.Ok) result;
        var tokens = ok.tokens().toList();
        assertEquals(1, tokens.size());
        assertInstanceOf(AsmToken.EOL.class, tokens.getFirst());
    }

    private Stream<LocatedChar> programToStream(String program) {
        var locatedChars = new ArrayList<LocatedChar>();
        var row = 1;
        var col = 1;
        for (char c : program.toCharArray()) {
            locatedChars.add(new LocatedChar(c, row, col));
            if (c == '\n') {
                row++;
                col = 1;
                continue;
            }
            col++;
        }
        return locatedChars.stream();
    }
}