package nl.suriani.machine8.cpu;

public class CPU8 {
    MemoryBus memoryBus;
    final Register16 pc;
    final Register8 acc;
    final Register2 s;

    static final int CPU_STATE_HALTED = 0;
    static final int CPU_STATE_READY = 1;
    static final int CPU_STATE_RUNNING = 2;
    static final int CPU_STATE_ERROR = 3;

    static final int OPCODE_SYS = 0x0;
    static final int OPCODE_LDI = 0x1;
    static final int OPCODE_LDA = 0x2;
    static final int OPCODE_STR = 0x3;
    static final int OPCODE_PSH = 0x4;
    static final int OPCODE_POP = 0x5;
    static final int OPCODE_ADD = 0x6;
    static final int OPCODE_SUB = 0x7;
    static final int OPCODE_INC = 0x8;
    static final int OPCODE_DEC = 0x9;
    static final int OPCODE_MUL = 0xa;
    static final int OPCODE_DIV = 0xb;
    static final int OPCODE_JMP = 0xc;
    static final int OPCODE_JMZ = 0xd;
    static final int OPCODE_JNZ = 0xe;
    static final int OPCODE_RET = 0xf;

    public CPU8() {
        this.memoryBus = new DetatchedMemoryBus();
        this.pc = new Register16();
        this.acc = new Register8();
        this.s = new Register2();
    }

    public void reset() {
        this.pc.reset();
        this.acc.reset();
        this.s.reset();
    }

    public void attachMemoryBus(MemoryBus memoryBus) {
        this.memoryBus = memoryBus;
        this.s.set(CPU_STATE_READY);
    }

    public void detachMemoryBus() {
        this.memoryBus = new DetatchedMemoryBus();
        this.s.set(CPU_STATE_HALTED);
    }

    public void fetchInstruction() {
        if (s.get() == CPU_STATE_ERROR || s.get() == CPU_STATE_HALTED) {
            return;
        }

        var addressInstruction = pc.get();
        var instruction = memoryBus.fetchInstruction(addressInstruction);
        int opcode  = (instruction >>> 12) & 0xF;
        int operand = instruction & 0x0FFF;
        s.set(CPU_STATE_RUNNING);
        switch (opcode) {
            case OPCODE_SYS -> {
                if (operand == 0) {
                    this.s.set(CPU_STATE_HALTED);
                }
            }

            case OPCODE_LDI -> acc.set(operand);
            case OPCODE_LDA -> acc.set(memoryBus.fetchData(operand));
            case OPCODE_STR -> memoryBus.storeData(operand, acc.get());
            case OPCODE_ADD -> acc.set(acc.get() + operand);
            case OPCODE_SUB -> acc.set(acc.get() - operand);
            case OPCODE_INC -> acc.set(acc.get() + 1);
            case OPCODE_DEC -> acc.set(acc.get() - 1);
            case OPCODE_MUL -> acc.set(acc.get() * operand);
            case OPCODE_DIV -> {
                if (operand == 0) {
                    s.set(CPU_STATE_ERROR);
                    return;
                }
                acc.set(acc.get() / operand);
            }
            default -> {
                throw new UnsupportedOperationException("Unknown opcode " + opcode);
            }
        }

        pc.set(pc.get() + 1);
    }
}
