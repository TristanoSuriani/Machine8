package nl.suriani.machine12.cpu;

public class Machine12MemoryBus implements MemoryBus {
    private final ROM16 rom;
    private final RAM12 ram;

    private static final int STACK_REGION1 = 0xC00;
    private static final int STACK_REGION2 = 0xD00;
    private static final int STACK_REGION3 = 0xE00;
    private static final int STACK_REGION4 = 0xF00;

    private int sp1 = 0;
    private int sp2 = 0;
    private int sp3 = 0;
    private int sp4 = 0;

    public Machine12MemoryBus(int[] instructions) {
        this.rom = new ROM16(instructions);
        this.ram = new RAM12();
    }

    @Override
    public int fetchInstruction(int address) {
        return rom.get(address);
    }

    @Override
    public int fetchData(int address) {
        return ram.get(address);
    }

    @Override
    public int storeData(int address, int value) {
        ram.set(address, value);
        return value;
    }

    @Override
    public int push(int stackSegment, int value) {
        stackSegment = stackSegment  & 0x3;
        return switch (stackSegment) {
            case 0 -> {
                ram.set(sp1 + STACK_REGION1, value);
                sp1 = (sp1 + 1) & 0xFF;
                yield value;
            }
            case 1 -> {
                ram.set(sp2 + STACK_REGION2, value);
                sp2 = (sp2 + 1) & 0xFF;
                yield value;
            }
            case 2 -> {
                ram.set(sp3 + STACK_REGION3, value);
                sp3 = (sp3 + 1) & 0xFF;
                yield value;
            }
            case 3 -> {
                ram.set(sp4 + STACK_REGION4, value);
                sp4 = (sp4 + 1) & 0xFF;
                yield value;
            }
            default -> throw new IllegalStateException("Unexpected value: " + stackSegment);
        };
    }

    @Override
    public int pop(int stackSegment) {
        stackSegment = stackSegment  & 0x3;
        return switch (stackSegment) {
            case 0 -> {
                sp1 = (sp1 - 1) & 0xFF;
                yield ram.get(STACK_REGION1 + sp1);
            }
            case 1 -> {
                sp2 = (sp2 - 1) & 0xFF;
                yield ram.get(STACK_REGION2 + sp2);
            }
            case 2 -> {
                sp3 = (sp3 - 1) & 0xFF;
                yield ram.get(STACK_REGION3 + sp3);
            }
            case 3 -> {
                sp4 = (sp4 - 1) & 0xFF;
                yield ram.get(STACK_REGION4 + sp4);
            }
            default -> throw new IllegalStateException("Unexpected value: " + stackSegment);
        };
    }
}
