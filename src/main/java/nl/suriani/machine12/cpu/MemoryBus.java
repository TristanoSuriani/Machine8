package nl.suriani.machine12.cpu;

public interface MemoryBus {
    int fetchInstruction(int address);
    int fetchData(int address);
    int storeData(int address, int value);
    int push(int stackSegment, int value);
    int pop(int stackSegment);
}
