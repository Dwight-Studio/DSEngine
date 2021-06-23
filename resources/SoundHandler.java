package fr.dwightstudio.dsengine.resources;

import fr.dwightstudio.dsengine.audio.objects.Sound;
import fr.dwightstudio.dsengine.audio.utils.SoundUtil;

import java.util.HashMap;
import java.util.Map;

public class SoundHandler implements TypeHandler<Sound> {

    private static final Map<String, Sound> SOUNDS = new HashMap<>();

    @Override
    public Sound get(String filepath) {
        if (!SOUNDS.containsKey(filepath)) {
            Sound sound = SoundUtil.loadSound(filepath);
            if (sound != null) {
                SOUNDS.put(filepath, sound);
            } else {
                return null;
            }
        }
        return SOUNDS.get(filepath);
    }

}
