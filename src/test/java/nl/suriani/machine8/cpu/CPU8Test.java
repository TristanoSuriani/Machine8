package nl.suriani.machine8.cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPU8Test {

    @Test
    void fetchInstructionNoProgramLoaded() {
        var cpu = new CPU8();
        var bus = new Machine8MemoryBus(new int[]{});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU8.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionMachineAlreadyHalted() {
        var cpu = new CPU8();
        cpu.s.set(CPU8.CPU_STATE_HALTED);
        var instruction = encodeInstruction(CPU8.OPCODE_SYS, 0);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU8.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionSys0() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_SYS, 0);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU8.CPU_STATE_HALTED, cpu.s.get());
    }

    @Test
    void fetchInstructionLdi() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_LDI, 10);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU8.CPU_STATE_RUNNING, cpu.s.get());
        assertEquals(10, cpu.acc.get());
    }

    @Test
    void fetchInstructionLda() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_LDA, 10);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.memoryBus.storeData(10, 5);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(5, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionStr() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_STR, 10);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(300);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(44, cpu.memoryBus.fetchData(10));
        assertEquals(44, cpu.acc.get()); // 300 is higher than 8-bit so it wraps to 44.
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionAdd() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_ADD, 5);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        bus.storeData(5, 10);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(16, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionSub() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_SUB, 2);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        bus.storeData(2, 6);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(5);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(255, cpu.acc.get()); // underflow by wrapping around
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionInc() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_INC, 6); // the operand gets actually ignored here
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(7, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDec() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_DEC, 3); // the operand gets actually ignored here
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(5, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionMul() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_MUL, 4);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        bus.storeData(4, 3);
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(18, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDiv() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_DIV, 2);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        bus.storeData(2, 3);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(2, cpu.acc.get());
        assertEquals(1, cpu.pc.get());
    }

    @Test
    void fetchInstructionDiv_by0_error() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_DIV, 0);
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(CPU8.CPU_STATE_ERROR, cpu.s.get());
    }

    @Test
    void fetchInstructionJmp() {
        var cpu = new CPU8();
        var instruction = encodeInstruction(CPU8.OPCODE_JMP, 3); // the operand is ignored here
        var bus = new Machine8MemoryBus(new int[]{instruction});
        cpu.attachMemoryBus(bus);
        cpu.acc.set(6);
        assertEquals(CPU8.CPU_STATE_READY, cpu.s.get());
        cpu.fetchInstruction();
        assertEquals(6, cpu.pc.get());
    }

    private int encodeInstruction(int opcode, int operand) {
        return ((opcode & 0xF) << 12) | (operand & 0x0FFF);
    }
}