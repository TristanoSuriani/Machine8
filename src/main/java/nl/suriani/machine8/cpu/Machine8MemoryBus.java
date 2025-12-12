package nl.suriani.machine8.cpu;

public class Machine8MemoryBus implements MemoryBus {
    private final ROM16 rom16;
    private final RAM8 ram8;

    public Machine8MemoryBus(int[] instructions) {
        this.rom16 = new ROM16(instructions);
        this.ram8 = new RAM8();
    }

    @Override
    public int fetchInstruction(int address) {
        return rom16.get(address);
    }

    @Override
    public int fetchData(int address) {
        return ram8.get(address);
    }

    @Override
    public int storeData(int address, int value) {
        ram8.set(address, value);
        return value;
    }
}
