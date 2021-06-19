/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dpt.DSEngine.graphics.objects;

import fr.dwightstudio.dpt.DSEngine.scripting.Scene;

public class Viewport extends Framebuffer {

    Scene scene;

    /**
     * Create a new Frambuffer object
     *
     * @param x          the X position
     * @param y          the Y position
     * @param baseWidth  the base width
     * @param baseHeight the base height
     */
    public Viewport(int x, int y, int baseWidth, int baseHeight) {
        super(x, y, baseWidth, baseHeight);
    }

    /**
     * Create a new Framebuffer object
     *
     * @param x          the X position
     * @param y          the Y position
     * @param baseWidth  the base width
     * @param baseHeight the base height
     * @param scaleX     the X scaling
     * @param scaleY     the Y scaling
     */
    public Viewport(int x, int y, int baseWidth, int baseHeight, int scaleX, int scaleY) {
        super(x, y, baseWidth, baseHeight, scaleX, scaleY);
    }

    public void attachScene(Scene scene) {
        this.scene = scene;
        scene.init();
        scene.start();
    }

    @Override
    public void update(double dt) {
        if (this.scene != null) {
            bind();
            scene.render();
            unbind();
        }
    }
}
