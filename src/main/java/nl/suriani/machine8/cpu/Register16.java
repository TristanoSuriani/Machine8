package nl.suriani.machine8.cpu;

public class Register16 {
    private final Cell16 cell;

    public Register16() {
        this.cell = new Cell16();
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
