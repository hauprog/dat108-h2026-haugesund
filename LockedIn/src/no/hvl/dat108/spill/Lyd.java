package no.hvl.dat108.spill;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// En tråd som spiller av toner etter hvert som de kommer. Journalen om igjen:
// rottene og angrip produserer, én tråd konsumerer, køen er trådsikker.
public class Lyd implements Runnable {
    private static final float SAMPLERATE = 44100;
    private final BlockingQueue<Integer> koe = new ArrayBlockingQueue<>(8);   // frekvenser i Hz

    // offer, ikke put: er køen full, droppes lyden. Spillet skal aldri vente på en pipelyd.
    public void spill(int hz) {
        koe.offer(hz);
    }

    @Override
    public void run() {
        AudioFormat format = new AudioFormat(SAMPLERATE, 8, 1, true, false);
        try (SourceDataLine linje = AudioSystem.getSourceDataLine(format)) {
            linje.open(format);
            linje.start();
            while (true) {
                int hz = koe.take();                                   // sover til noe kommer
                byte[] tone = new byte[(int) (SAMPLERATE / 10)];       // 100 ms
                for (int i = 0; i < tone.length; i++) {
                    tone[i] = (byte) (Math.sin(2 * Math.PI * i * hz / SAMPLERATE) * 60);
                }
                linje.write(tone, 0, tone.length);
            }
        } catch (InterruptedException e) {
            // avbrutt: ut
        } catch (LineUnavailableException | IllegalArgumentException e) {
            // ingen lydkort, eller format som ikke støttes: spillet går videre uten lyd
        }
    }
}