package nl.suriani.machine8.cpu;

public class RAM8 {
    private final Cell8[] cells;

    public RAM8() {
        this.cells = new Cell8[0x1000];
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
            this.cells[i] = new Cell8();
        }
        return 0;
    }
}
