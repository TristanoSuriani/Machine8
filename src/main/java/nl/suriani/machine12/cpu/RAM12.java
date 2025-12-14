package nl.suriani.machine12.cpu;

public class RAM12 {
    private final Cell12[] cells;

    public RAM12() {
        this.cells = new Cell12[0x1000];
        reset();
    }

    public int set(int address, int value) {
        return this.cells[address].value(value);
    }

    public int get(int address) {
        return this.cells[address].value();
    }

    public int reset() {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = new Cell12();
        }
        return 0;
    }
}
