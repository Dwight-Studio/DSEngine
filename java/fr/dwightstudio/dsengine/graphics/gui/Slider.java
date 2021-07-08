/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.graphics.gui;

import fr.dwightstudio.dsengine.Engine;
import fr.dwightstudio.dsengine.graphics.objects.Color;
import fr.dwightstudio.dsengine.graphics.primitives.Surface;
import fr.dwightstudio.dsengine.inputs.MouseListener;
import fr.dwightstudio.dsengine.logging.GameLogger;
import org.joml.Vector2f;
import org.lwjgl.opengl.GLUtil;

import javax.swing.*;

public class Slider extends Surface {

    private double minValue;
    private double maxValue;
    private int orientation;
    private boolean selected = false;

    private final Surface sliderPoint;

    private double currentValue;

    /**
     * Create a new Slider
     * This will use the Engine's included default Slider.
     *
     * @param position the Slider position
     * @param scale    the Slider scale
     * @param color    the Slider color
     */
    public Slider(Vector2f position, Vector2f scale, Color color, float minValue, float maxValue) {
        super(position, scale, color);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = minValue;
        this.sliderPoint = new Surface(new Vector2f(position.x, position.y), new Vector2f(scale.y / 6, scale.y), Engine.COLOR.BLACK);
        this.orientation = Engine.GUI.HORIZONTAL;
    }

    /**
     * Create a new Slider
     * This will use the Engine's included default Slider.
     *
     * @param position the Slider position
     * @param scale    the Slider scale
     * @param color    the Slider color
     * @param orientation the slider orientation can be either VERTICAL or HORIZONTAL
     */
    public Slider(Vector2f position, Vector2f scale, Color color, float minValue, float maxValue, int orientation) {
        super(position, scale, color);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.orientation = orientation;
        if (orientation == Engine.GUI.HORIZONTAL) {
            this.sliderPoint = new Surface(new Vector2f(position.x, position.y - (scale.y / 10) / 2), new Vector2f(scale.y / 6, scale.y + scale.y / 10), Engine.COLOR.BLACK);
        } else if (orientation == Engine.GUI.VERTICAL) {
            this.sliderPoint = new Surface(new Vector2f(position.x - (scale.x / 10) / 2, position.y), new Vector2f(scale.x + scale.x / 10, scale.x / 6), Engine.COLOR.BLACK);
        } else {
            this.sliderPoint = new Surface(new Vector2f(position.x, position.y - (scale.y / 10) / 2), new Vector2f(scale.y / 6, scale.y + scale.y / 10), Engine.COLOR.BLACK);
        }
    }

    @Override
    public void addComponent() {
        renderGroup.addComponent(sliderPoint);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (MouseListener.isButtonPressed(0)) {
            if (MouseListener.getOrthoCursorPos().x >= this.getTransform().position.x + this.renderGroup.getTransform().position.x && MouseListener.getOrthoCursorPos().x <= this.getTransform().position.x + this.renderGroup.getTransform().position.x + this.getTransform().scale.x + this.renderGroup.getTransform().scale.x) {
                if (MouseListener.getOrthoCursorPos().y >= this.getTransform().position.y + this.renderGroup.getTransform().position.y && MouseListener.getOrthoCursorPos().y <= this.getTransform().position.y + this.renderGroup.getTransform().position.y + this.getTransform().scale.y + this.renderGroup.getTransform().scale.y) {
                    selected = true;
                }
            }
        }
        if (!MouseListener.isButtonPressed(0) && selected) {
            selected = false;
        }
        // While we hold the click we can continue to move the slider point
        if (selected) {
            if (orientation == Engine.GUI.HORIZONTAL) {
                if (sliderPoint.getTransform().position.x + sliderPoint.getTransform().scale.x / 2 >= this.getTransform().position.x) {
                    sliderPoint.getTransform().position.x = MouseListener.getOrthoCursorPos().x - (sliderPoint.getTransform().scale.x / 2);
                }
            } else if (orientation == Engine.GUI.VERTICAL) {
                if (sliderPoint.getTransform().position.y + sliderPoint.getTransform().scale.y / 2 >= this.getTransform().position.y) {
                    sliderPoint.getTransform().position.y = MouseListener.getOrthoCursorPos().y - (sliderPoint.getTransform().scale.y / 2);
                }
            }
        }
        // Here we are checking if the slider point is outside of the max lenght or height of the slider box
        if (orientation == Engine.GUI.HORIZONTAL) {
            if (sliderPoint.getTransform().position.x + sliderPoint.getTransform().scale.x / 2 < this.getTransform().position.x) {
                sliderPoint.getTransform().position.x = this.getTransform().position.x - sliderPoint.getTransform().scale.x / 2;
            }
            if (sliderPoint.getTransform().position.x + sliderPoint.getTransform().scale.x / 2 > this.getTransform().position.x + this.getTransform().scale.x) {
                sliderPoint.getTransform().position.x = this.getTransform().position.x + this.getTransform().scale.x - sliderPoint.getTransform().scale.x / 2;
            }
        } else if (orientation == Engine.GUI.VERTICAL) {
            if (sliderPoint.getTransform().position.y + sliderPoint.getTransform().scale.y / 2 < this.getTransform().position.y) {
                sliderPoint.getTransform().position.y = this.getTransform().position.y - sliderPoint.getTransform().scale.y / 2;
            }
            if (sliderPoint.getTransform().position.y + sliderPoint.getTransform().scale.y / 2 > this.getTransform().position.y + this.getTransform().scale.y) {
                sliderPoint.getTransform().position.y = this.getTransform().position.y + this.getTransform().scale.y - sliderPoint.getTransform().scale.y / 2;
            }
        }
        calculateNewValue();
    }

    private void calculateNewValue() {
        if (orientation == Engine.GUI.HORIZONTAL) {
            /*double normalizedSliderPos = (sliderPoint.getTransform().position.x + sliderPoint.getTransform().scale.x / 2) / getTransform().scale.x - 1.5625;
            double interval = maxValue - minValue;*/
            this.currentValue = (getTransform().position.x + getTransform().scale.x - getTransform().position.x) * (maxValue - minValue) + minValue;
        }
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public int getOrientation() {
        return orientation;
    }
}
