package nl.suriani.machine8.cpu;

public class Cell2 {
    private int value;

    public Cell2() {
        this.value = 0;
    }

    public int value() {
        return value & 0x03;
    }

    public int value(int value) {
        this.value = value & 0x03;
        return this.value;
    }

    public int reset() {
        this.value = 0;
        return this.value;
    }
}
