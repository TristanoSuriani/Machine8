package nl.suriani.machine12.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciMachine12Test {

    @Test
    void writesFirst10FibonacciNumbersToRam() {
        var cpu = new CPU12();

        // Program:
        // A at 0x0F0, B at 0x0F1, TMP at 0x0F2
        // Output at 0x100..0x109
        int A   = 0x0F0;
        int B   = 0x0F1;
        int TMP = 0x0F2;
        int OUT = 0x100;

        var program = new int[] {
                // A = 0
                enc(CPU12.OPCODE_LDI, 0),
                enc(CPU12.OPCODE_STR, A),

                // B = 1
                enc(CPU12.OPCODE_LDI, 1),
                enc(CPU12.OPCODE_STR, B),

                // out0 = A
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_STR, OUT + 0),

                // out1 = B
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, OUT + 1),

                // ---- fib2: TMP=A+B; A=B; B=TMP; out2=B
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 2),

                // ---- fib3
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 3),

                // ---- fib4
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 4),

                // ---- fib5
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 5),

                // ---- fib6
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 6),

                // ---- fib7
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 7),

                // ---- fib8
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 8),

                // ---- fib9
                enc(CPU12.OPCODE_LDA, A),
                enc(CPU12.OPCODE_ADD, B),
                enc(CPU12.OPCODE_STR, TMP),
                enc(CPU12.OPCODE_LDA, B),
                enc(CPU12.OPCODE_STR, A),
                enc(CPU12.OPCODE_LDA, TMP),
                enc(CPU12.OPCODE_STR, B),
                enc(CPU12.OPCODE_STR, OUT + 9),

                // HALT
                enc(CPU12.OPCODE_SYS, 0)
        };

        var bus = new Machine12MemoryBus(program);
        cpu.attachMemoryBus(bus);

        // Run until HALT or ERROR, but don't hang tests if something goes wrong.
        for (int i = 0; i < 10_000 && cpu.s.get() != CPU12.CPU_STATE_HALTED && cpu.s.get() != CPU12.CPU_STATE_ERROR; i++) {
            cpu.fetchInstruction();
        }
        assertEquals(CPU12.CPU_STATE_HALTED, cpu.s.get(), "CPU did not halt (or hit error)");

        // Assert outputs
        int[] expected = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], bus.fetchData(OUT + i), "Mismatch at fib[" + i + "]");
        }
    }

    private static int enc(int opcode, int operand) {
        return ((opcode & 0xF) << 12) | (operand & 0x0FFF);
    }
}