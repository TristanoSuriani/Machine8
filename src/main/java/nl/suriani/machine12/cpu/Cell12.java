package nl.suriani.machine12.cpu;

public class Cell12 {
    private int value;

    public Cell12() {
        this.value = 0;
    }

    public int value() {
        return value & 0x0FFF;
    }

    public int value(int value) {
        this.value = value & 0x0FFF;
        return this.value;
    }

    public int reset() {
        this.value = 0;
        return this.value;
    }
}
