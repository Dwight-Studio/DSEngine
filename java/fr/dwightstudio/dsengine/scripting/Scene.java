/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.scripting;

import fr.dwightstudio.dsengine.graphics.objects.Camera;
import fr.dwightstudio.dsengine.graphics.objects.Color;
import fr.dwightstudio.dsengine.graphics.renderers.RendererHelper;
import fr.dwightstudio.dsengine.graphics.utils.SceneManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glClearColor;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<RenderGroup> renderGroups = new ArrayList<>();
    protected RendererHelper rendererHelper = new RendererHelper();

    /**
     * Create a new Scene
     */
    public Scene() {
        this.camera = new Camera(new Vector2f());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        SceneManager.add(this);
    }

    /**
     * Initialize the Scene
     * This will initialize all the RenderGroup
     */
    public void start() {
        for (RenderGroup renderGroup : renderGroups) {
            renderGroup.init();
            rendererHelper.addGameObject(renderGroup);
        }
    }

    public void init() {

    }

    public void update(double dt) {
        for (RenderGroup renderGroup : this.renderGroups) {
            renderGroup.update(dt);
        }
        if (SceneManager.getCurrentScene() == this) {
            render();
        }
    }

    public void render() {
        rendererHelper.render();
    }

    /**
     * Add a RenderGroup to the Scene
     *
     * @param renderGroup the RenderGroup to add
     */
    public void addGameObject(RenderGroup renderGroup) {
        if (!isRunning) {
            renderGroups.add(renderGroup);
        } else {
            renderGroups.add(renderGroup);
            renderGroup.init();
            rendererHelper.addGameObject(renderGroup);
        }
    }

    /**
     * Set the background color of the Scene
     *
     * @param color a color
     */
    public void setBackgroundColor(Color color) {
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * @return the camera used in the Scene
     */
    public Camera getCamera() {
        return this.camera;
    }
}
