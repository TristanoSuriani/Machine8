package nl.suriani.machine12.cpu;

public class Register12 {
    private final Cell12 cell;

    public Register12() {
        this.cell = new Cell12();
    }

    int set(int value) {
        return this.cell.value(value);
    }

    int get() {
        return this.cell.value();
    }

    int reset() {
        return this.cell.reset();
    }
}
