package fr.dwightstudio.dsengine.audio.utils;

import fr.dwightstudio.dsengine.Engine;
import fr.dwightstudio.dsengine.audio.objects.Sound;
import fr.dwightstudio.dsengine.logging.GameLogger;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.text.MessageFormat;
import java.util.Objects;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class SoundUtil {

    /**
     * Load an OGG-Vorbis sound file
     *
     * @param filepath the sound location
     * @return the newly created Sound object
     */
    // TODO: Only OGG-Vorbis format is supported
    public static Sound loadSound(String filepath) {
        stackPush();
        IntBuffer channelBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelBuffer, sampleRateBuffer);
        if (rawAudioBuffer == null) {
            stackPop();
            stackPop();
            GameLogger.getLogger("SoundUtil").fatal(MessageFormat.format("Unable to load sound file: {0}", filepath));
            return null;
        }
        int numberOfChannels = channelBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        stackPop();
        stackPop();

        int format = -1;
        if (numberOfChannels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (numberOfChannels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        int bufferID = alGenBuffers();
        alBufferData(bufferID, format, Objects.requireNonNull(rawAudioBuffer), sampleRate);

        int sourceID = alGenSources();
        alSourcei(sourceID, AL_BUFFER, bufferID);
        alSourcei(sourceID, AL_LOOPING, Engine.SOUND.getDefaultLoopingState() ? 1 : 0);
        alSourcei(sourceID, AL_POSITION, 0);
        alSourcef(sourceID, AL_GAIN, Engine.SOUND.getDefaultSoundGain());

        free(rawAudioBuffer);
        GameLogger.getLogger("SoundUtil").debug(MessageFormat.format("Successfully loaded Sound : {0}", filepath));
        return new Sound(bufferID, sourceID, filepath, numberOfChannels, sampleRate);
    }

    /**
     * Delete the specified Sound
     *
     * @param sound the Sound to delete
     */
    public static void delete(Sound sound) {
        alDeleteBuffers(sound.getBufferID());
        alDeleteSources(sound.getSourceID());
    }
}
