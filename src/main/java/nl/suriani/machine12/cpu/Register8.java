package nl.suriani.machine12.cpu;

public class Register8 {
    private final Cell8 cell;

    public Register8() {
        this.cell = new Cell8();
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
