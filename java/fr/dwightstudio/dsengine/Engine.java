/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine;

import fr.dwightstudio.dsengine.graphics.objects.Color;

public class Engine {
    public static final long FULLSCREEN = 0;
    public static final long WINDOWED = 1;

    public static class COLOR {
        public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
        public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
        public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);
        public static final Color ORANGE = new Color(1.0f, 0.5f, 0.0f, 1.0f);
        public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f, 1.0f);
        public static final Color PURPLE = new Color(0.5f, 0.0f, 1.0f, 1.0f);
        public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f, 1.0f);
        public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static class SOUND {
        private static float defaultSoundGain = 1.0f;
        private static boolean defaultLoopingState = false;
        private static float defaultSoundPitch = 1.0f;
        private static float defaultSoundPosition = 0.0f;

        public static void setDefaultSoundGain(float gain) {
            defaultSoundGain = gain;
        }
        public static float getDefaultSoundGain() {
            return defaultSoundGain;
        }

        public static void setDefaultLoopingState(boolean looping) {
            defaultLoopingState = looping;
        }
        public static boolean getDefaultLoopingState() {
            return defaultLoopingState;
        }

        public static void setDefaultSoundPitch(float pitch) {
            defaultSoundPitch = pitch;
        }
        public static float getDefaultSoundPitch() {
            return defaultSoundPitch;
        }

        public static void setDefaultSoundPosition(float position) {
            defaultSoundPosition = position;
        }
        public static float getDefaultSoundPosition() {
            return defaultSoundPosition;
        }
    }

}
