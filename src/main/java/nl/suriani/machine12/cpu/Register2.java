package nl.suriani.machine12.cpu;

public class Register2 {
    private final Cell2 cell;

    public Register2() {
        this.cell = new Cell2();
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
