package nl.suriani.machine8.cpu;

public interface MemoryBus {
    int fetchInstruction(int address);
    int fetchData(int address);
    int storeData(int address, int value);
}
