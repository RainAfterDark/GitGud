package org.gitgud.core.terminal;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class InputHandler {
    static final int FAST_SPEED = 3;

    final ConsoleRenderer renderer;
    final Terminal terminal;
    final NonBlockingReader reader;

    public InputHandler(ConsoleRenderer renderer) {
        this.renderer = renderer;
        terminal = renderer.terminal;
        reader = terminal.reader();
        resetCamera();
    }

    private void resetCamera() {
        renderer.camX.set(-terminal.getWidth() / 2);
        renderer.camY.set(-terminal.getHeight() / 2);
    }

    public void startAsync() {
        Thread inputThread = new Thread(() -> {
            try {
                while (renderer.running.get()) {
                    int input = reader.read();

                    if (input == 'q') {
                        renderer.running.set(false);
                        break;
                    }

                    int speed = 1;
                    if (Character.isUpperCase(input))
                        speed = FAST_SPEED;

                    input = Character.toLowerCase(input);
                    if (input == 'w') renderer.camY.addAndGet(-speed);
                    if (input == 's') renderer.camY.addAndGet(speed);
                    if (input == 'a') renderer.camX.addAndGet(-speed * 2);
                    if (input == 'd') renderer.camX.addAndGet(speed * 2);
                    if (input == 'r') resetCamera();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        inputThread.start();
    }
}
