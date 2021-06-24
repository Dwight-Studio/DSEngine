/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.graphics.gui;

import fr.dwightstudio.dsengine.graphics.objects.Color;
import fr.dwightstudio.dsengine.graphics.primitives.Surface;
import org.joml.Vector2f;

public class Slider extends Surface {



    /**
     * Create a new Slider with default textures
     *
     * @param position the Slider position
     * @param scale    the Slider scale
     * @param color    the Slider color
     */
    public Slider(Vector2f position, Vector2f scale, Color color) {
        super(position, scale, color);
    }
}
