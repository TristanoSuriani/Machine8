package nl.suriani.machine12.cpu;

public class Cell8 {
    private int value;

    public Cell8() {
        this.value = 0;
    }

    public int value() {
        return value & 0xFF;
    }

    public int value(int value) {
        this.value = value & 0xFF;
        return this.value;
    }

    public int reset() {
        this.value = 0;
        return this.value;
    }
}
