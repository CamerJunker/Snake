package snake;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, Clip> clips = new HashMap<>();

    public void load(String key, String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return;
        }

        try (AudioInputStream in = AudioSystem.getAudioInputStream(url)) {
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clips.put(key, clip);
        } catch (Exception e) {
        }
    }

    public void play(String key) {
        Clip clip = clips.get(key);
        if (clip == null) return;

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
}
