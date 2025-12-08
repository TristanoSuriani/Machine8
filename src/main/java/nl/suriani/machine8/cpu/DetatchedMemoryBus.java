package nl.suriani.machine8.cpu;

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
}
