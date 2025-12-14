package nl.suriani.machine12.cpu;

public class Cell16 {
    private int value;

    public Cell16() {
        this.value = 0;
    }

    public int value() {
        return value & 0xFFFF;
    }

    public int value(int value) {
        this.value = value & 0xFFFF;
        return this.value;
    }

    public int reset() {
        this.value = 0;
        return this.value;
    }
}
