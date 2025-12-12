package nl.suriani.machine8.cpu;

public class CPU8 {
    MemoryBus memoryBus;
    final Register16 pc;
    final Register16 sp1;
    final Register16 sp2;
    final Register16 sp3;
    final Register16 sp4;
    final Register8 acc;
    final Register8 r1;
    final Register8 r2;
    final Register8 r3;
    final Register8 r4;
    final Register8 r5;
    final Register8 r6;
    final Register8 r7;
    final Register8 r8;
    final Register2 s;
    final Register2 pbr;

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
        this.sp1 = new Register16();
        this.sp2 = new Register16();
        this.sp3 = new Register16();
        this.sp4 = new Register16();
        this.acc = new Register8();
        this.r1 = new Register8();
        this.r2 = new Register8();
        this.r3 = new Register8();
        this.r4 = new Register8();
        this.r5 = new Register8();
        this.r6 = new Register8();
        this.r7 = new Register8();
        this.r8 = new Register8();
        this.s = new Register2();
        this.pbr = new Register2();
    }

    public void reset() {
        this.pc.reset();
        this.sp1.reset();
        this.sp2.reset();
        this.sp3.reset();
        this.sp4.reset();
        this.acc.reset();
        this.r1.reset();
        this.r2.reset();
        this.r3.reset();
        this.r4.reset();
        this.r5.reset();
        this.r6.reset();
        this.r7.reset();
        this.r8.reset();
        this.s.reset();
        this.pbr.reset();
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
        var addressInstruction = pc.get();
        var instruction = memoryBus.fetchInstruction(addressInstruction);
        var opcode = instruction >> 12;
        var operand = instruction & 0x0FFF;
        s.set(CPU_STATE_RUNNING);
        switch (opcode) {
            case OPCODE_SYS -> {
                if (operand == 0) {
                    this.s.set(CPU_STATE_HALTED);
                }
            }

            case OPCODE_LDI -> acc.set(operand);

            default -> {
                throw new UnsupportedOperationException("Unknown opcode " + opcode);
            }
        }

        pc.set(pc.get() + 1);
    }
}
