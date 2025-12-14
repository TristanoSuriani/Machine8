package nl.suriani.machine12.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPU12Test {

    @Test
    void fetchInstructionNoProgramLoaded() {
        var cpu = new CPU12();
        var bus = new Machine12MemoryBus(new int[]{});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU12.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionMachineAlreadyHalted() {
        var cpu = new CPU12();
        cpu.s.set(CPU12.CPU_STATE_HALTED);
        var instruction = encodeInstruction(CPU12.OPCODE_SYS, 0);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU12.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionSys0() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_SYS, 0);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU12.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionLdi() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_LDI, 10);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU12.CPU_STATE_RUNNING, cpu.s.get());
        assertEquals(10, cpu.acc.get());
    }

    @Test
    void fetchInstructionLda() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_LDA, 10);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.memoryBus.storeData(10, 5);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(5, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionStr() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_STR, 10);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(5000);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(904, cpu.memoryBus.fetchData(10));
        assertEquals(904, cpu.acc.get()); // 5000 is higher than 12-bit so it wraps to 904.
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionsPSH() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_PSH, 2);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(10);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(10, cpu.acc.get());
        assertEquals(10, cpu.memoryBus.fetchData(0xE00));
    }

    @Test
    void fetchInstructionsPop() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_POP, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.push(3, 155);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(10);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(155, cpu.acc.get());
    }

    @Test
    void fetchInstructionAdd() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_ADD, 5);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(5, 10);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(16, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionSub() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_SUB, 2);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(2, 6);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(5);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(4095, cpu.acc.get()); // underflow by wrapping around
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionInc() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_INC, 6); // the operand gets actually ignored here
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(7, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDec() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_DEC, 3); // the operand gets actually ignored here
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(5, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionMul() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_MUL, 4);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(4, 3);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(18, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDiv() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_DIV, 2);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        bus.storeData(2, 3);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(2, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDiv_by0_error() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_DIV, 0);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU12.CPU_STATE_ERROR, cpu.s.get());
    }

    @Test
    void fetchInstructionJmp() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_JMP, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(7, cpu.pc.get());
    }

    @Test
    void fetchInstructionJnz_accIsNotZero() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_JNZ, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(7, cpu.pc.get());
    }

    @Test
    void fetchInstructionJnz_accIsZero() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_JNZ, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(0);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionJmz_accIsNotZero() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_JMZ, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionJmz_accIsZero() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_JMZ, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(0);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(7, cpu.pc.get());
    }

    @Test
    void fetchInstructionXor() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_XOR, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 7);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(10);
        assertEquals(CPU12.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(13, cpu.acc.get());
    }

    @Test
    void fetchInstructionXor_zeroesAcc() {
        var cpu = new CPU12();
        var instruction = encodeInstruction(CPU12.OPCODE_XOR, 3);
        var bus = new Machine12MemoryBus(new int[]{instruction});
        bus.storeData(3, 10);
        cpu.attachMemoryBus(bus);

        cpu.acc.set(10);
        cpu.fetchInstruction();

        assertEquals(0, cpu.acc.get());
    }

    @Test
    void pcWrapsAt4096() {
        var cpu = new CPU12();
        var bus = new Machine12MemoryBus(new int[]{ encodeInstruction(CPU12.OPCODE_INC, 0) });
        cpu.attachMemoryBus(bus);
        cpu.pc.set(0x0FFF);
        cpu.fetchInstruction();
        assertEquals(0, cpu.pc.get()); // wrap
    }

    @Test
    void jmpTargetWraps() {
        var cpu = new CPU12();
        var bus = new Machine12MemoryBus(new int[]{ encodeInstruction(CPU12.OPCODE_JMP, 3) });
        bus.storeData(3, 5000); // wraps to 904
        cpu.attachMemoryBus(bus);
        cpu.fetchInstruction();
        assertEquals(904, cpu.pc.get());
    }

    private int encodeInstruction(int opcode, int operand) {
        return ((opcode & 0xF) << 12) | (operand & 0x0FFF);
    }
}