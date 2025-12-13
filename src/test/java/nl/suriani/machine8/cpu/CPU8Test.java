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

    private int encodeInstruction(int opcode, int operand) {
        return ((opcode & 0xF) << 12) | (operand & 0x0FFF);
    }
}