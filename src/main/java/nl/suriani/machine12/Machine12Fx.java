package nl.suriani.machine12;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nl.suriani.machine12.cpu.CPU12;
import nl.suriani.machine12.cpu.Machine12MemoryBus;
import nl.suriani.machine12.cpu.MemoryBus;

public class Machine12Fx extends Application {

    static final int WIDTH = 64;
    static final int HEIGHT = 64;

    static final int VIDEO_BASE = 0x800;

    @Override
    public void start(Stage stage) {
        CPU12 cpu = new CPU12();
        Machine12MemoryBus bus = new Machine12MemoryBus(programDrawCheckers());
        cpu.attachMemoryBus(bus);

        // Offscreen 64x64 framebuffer image
        WritableImage img = new WritableImage(WIDTH, HEIGHT);
        ImageView view = new ImageView(img);

        // Crisp pixels
        view.setSmooth(false);
        view.setPreserveRatio(true);

        // Fill the window while preserving aspect ratio (letterboxing)
        view.fitWidthProperty().bind(stage.widthProperty());
        view.fitHeightProperty().bind(stage.heightProperty());

        int[] argb = new int[WIDTH * HEIGHT];

        AnimationTimer timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (cpu.s() != CPU12.CPU_STATE_HALTED && cpu.s() != CPU12.CPU_STATE_ERROR) {
                    for (int i = 0; i < 5_000; i++) cpu.fetchInstruction();
                }

                renderMonoFramebuffer(bus, argb);

                img.getPixelWriter().setPixels(
                        0, 0, WIDTH, HEIGHT,
                        PixelFormat.getIntArgbPreInstance(),
                        argb, 0, WIDTH
                );
            }
        };
        timer.start();

        StackPane root = new StackPane(view);
        root.setAlignment(Pos.CENTER); // center with letterboxing

        Scene scene = new Scene(root);

        stage.setTitle("Machine12");
        stage.setScene(scene);

        // Start maximized (practical fullscreen). Use stage.setFullScreen(true) if you want true fullscreen.
        stage.setMaximized(true);

        // Optional: start at actual screen size (some platforms ignore maximize early)
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.show();
    }

    private static void renderMonoFramebuffer(MemoryBus bus, int[] outArgb) {
        final int ON  = 0xFFFFFFFF;
        final int OFF = 0xFF000000;

        for (int p = 0; p < WIDTH * HEIGHT; p++) {
            int wordAddr = VIDEO_BASE + (p / 12);
            int word = bus.fetchData(wordAddr) & 0x0FFF;

            int bit = 11 - (p % 12);
            int pixel = (word >>> bit) & 1;

            outArgb[p] = (pixel != 0) ? ON : OFF;
        }
    }

    static int[] programDrawCheckers() {
        final int W = 64, H = 64;
        final int VIDEO_BASE = 0x800;
        final int WORDS = (W * H + 11) / 12; // 342
        final int TILE = 8; // checker size: 8x8

        int[] fb = new int[WORDS]; // each entry is a 12-bit word

        // Build framebuffer bits
        for (int p = 0; p < W * H; p++) {
            int x = p % W;
            int y = p / W;

            boolean white = (((x / TILE) + (y / TILE)) & 1) == 0; // top-left white
            if (!white) continue;

            int wi = p / 12;
            int bit = 11 - (p % 12);
            fb[wi] |= (1 << bit);
        }

        // Emit ROM: (LDI fb[i]) (STR VIDEO_BASE+i) ... SYS 0
        int[] rom = new int[WORDS * 2 + 1];
        int k = 0;
        for (int i = 0; i < WORDS; i++) {
            rom[k++] = enc(CPU12.OPCODE_LDI, fb[i]);
            rom[k++] = enc(CPU12.OPCODE_STR, VIDEO_BASE + i);
        }
        rom[k] = enc(CPU12.OPCODE_SYS, 0);
        return rom;
    }

    private static int enc(int opcode, int operand) {
        return ((opcode & 0xF) << 12) | (operand & 0x0FFF);
    }

    public static void main(String[] args) {
        launch(args);
    }


}