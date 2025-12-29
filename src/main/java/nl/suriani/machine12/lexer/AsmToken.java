package nl.suriani.machine12.lexer;

import nl.suriani.machine12.model.Token;

public sealed interface AsmToken extends Token {
    record Identifier(String value, int row, int col) implements AsmToken {}
    record EOL(int row, int col) implements AsmToken {
        @Override
        public String value() {
            return "\n";
        }
    }
    record Label(String value, int row, int col) implements AsmToken {}
    record Number(String value, int row, int col) implements AsmToken {}
    record BeginMacro(int row, int col) implements AsmToken {
        @Override
        public String value() {
            return "macro";
        }
    }
    record EndMacro(int row, int col) implements AsmToken {
        @Override
        public String value() {
            return "endm";
        }
    }
    record LBracket(int row, int col) implements AsmToken {
        @Override
        public String value() {
            return "{";
        }
    }
    record RBracket(int row, int col) implements AsmToken {
        @Override
        public String value() {
            return "}";
        }
    }
    record Directive(String value, int row, int col) implements AsmToken {}
    record StringLiteral(String value, int row, int col) implements AsmToken {}
}
