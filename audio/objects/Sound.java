package fr.dwightstudio.dsengine.audio.objects;

import fr.dwightstudio.dsengine.scripting.Component;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.AL_PITCH;

public class Sound extends Component {
    private final int bufferID;
    private final int sourceID;
    private final String filepath;
    private final int channels;
    private final int sampleRate;

    private boolean playing = false;

    /**
     * Create a Sound object
     * This automatically done with the ResourceManager
     *
     * @param bufferID the OpenAL bufferID of the Sound
     * @param sourceID the OpenAL sourceID of the Sound
     * @param filepath the filepath of the Sound
     * @param channels the number of channels of the Sound
     * @param sampleRate the sample rate of the Sound
     */
    public Sound(int bufferID, int sourceID, String filepath, int channels, int sampleRate) {
        this.bufferID = bufferID;
        this.sourceID = sourceID;
        this.filepath = filepath;
        this.channels = channels;
        this.sampleRate = sampleRate;
    }

    /**
     * @return the Sound buffer ID
     */
    public int getBufferID() {
        return bufferID;
    }

    /**
     * @return the Sound source ID
     */
    public int getSourceID() {
        return sourceID;
    }

    /**
     * @return the Sound filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @return the Sound number of channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * @return the Sound sample rate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * @return whether the Sound is playing or not
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Play a Sound
     *
     * @param loop whether the Sound should loop or not
     */
    public void play(boolean loop) {
        alSourcei(sourceID, AL_LOOPING, loop ? 1 : 0);
        alSourcePlay(sourceID);
        playing = true;
    }

    /**
     * Stop a Sound playback
     */
    public void stop() {
        alSourceStop(sourceID);
        playing = false;
    }

    /**
     * Change the gain of the specified Sound object
     *
     * @param gain the new gain for the Sound
     */
    public void gain(float gain) {
        alSourcef(sourceID, AL_GAIN, gain);
    }

    /**
     * Change the pitch of the specified Sound object
     *
     * @param pitch the new pitch for the Sound
     */
    public void pitch(float pitch) {
        alSourcef(sourceID, AL_PITCH, pitch);
    }
}
