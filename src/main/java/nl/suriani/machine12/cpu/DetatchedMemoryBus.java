package nl.suriani.machine12.cpu;

public class DetatchedMemoryBus implements MemoryBus {
    @Override
    public int fetchInstruction(int address) {
        return 0;
    }

    @Override
    public int fetchData(int address) {
        return 0;
    }

    @Override
    public int storeData(int address, int value) {
        return 0;
    }

    @Override
    public int push(int stackSegment, int value) {
        return 0;
    }

    @Override
    public int pop(int stackSegment) {
        return 0;
    }
}
