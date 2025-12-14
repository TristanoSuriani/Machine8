package nl.suriani.machine12.cpu;

public class ROM16 {
    private final Cell16[] cells;

    public ROM16(int[] instructions) {
        this.cells = new Cell16[0x1000];

        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = new Cell16();
            if (i < instructions.length) {
                this.cells[i].value(instructions[i]);
            } else  {
                this.cells[i].value(0);
            }
        }
    }

    public int get(int address) {
        return this.cells[address].value();
    }
}
