/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.scripting;

import fr.dwightstudio.dsengine.graphics.objects.Transform;
import fr.dwightstudio.dsengine.logging.GameLogger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class RenderGroup {

    // TODO: Transform rotatino support
    private final String name;
    private final List<Component> components;
    private final Transform transform;
    private Transform lastTransform;
    private final int zIndex;

    private boolean dirty = true;

    /**
     * Create a RenderGroup
     * A RenderGroup can contain an unlimited amount of Component objects
     *
     * @param name the RenderGroup name
     */
    public RenderGroup(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.lastTransform = new Transform();
        this.zIndex = 0;
        GameLogger.getLogger("RenderGroup").debug(MessageFormat.format("Created RenderGroup : \"{0}\"", name));
    }

    /**
     * Create a RenderGroup
     * A RenderGroup can contain an unlimited amount of Component objects
     *
     * @param name the RenderGroup name
     * @param transform the RenderGroup position
     */
    public RenderGroup(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.lastTransform = new Transform();
        this.zIndex = 0;
        GameLogger.getLogger("RenderGroup").debug(MessageFormat.format("Create RenderGroup : \"{0}\"", name));
    }

    /**
     * Create a RenderGroup
     * A RenderGroup can contain an unlimited amount of Component objects
     *
     * @param name the RenderGroup name
     * @param zIndex the RenderGroup Z level
     */
    public RenderGroup(String name, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.lastTransform = new Transform();
        this.zIndex = zIndex;
        GameLogger.getLogger("RenderGroup").debug(MessageFormat.format("Create RenderGroup : \"{0}\" with zIndex : {1}", name, zIndex));
    }

    /**
     * Create a RenderGroup
     * A RenderGroup can contain an unlimited amount of Component objects
     *
     * @param name the RenderGroup name
     * @param transform the RenderGroup transform
     * @param zIndex the RenderGroup Z level
     */
    public RenderGroup(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.lastTransform = new Transform();
        this.zIndex = zIndex;
        GameLogger.getLogger("RenderGroup").debug(MessageFormat.format("Create RenderGroup : \"{0}\" with zIndex : {1}", name, zIndex));
    }

    /**
     * You will get the first element searched of the RenderGroup
     *
     * @param componentClass the component class
     * @return the component
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * @param componentClass the component class
     * @return a List containing all the elements of the component class
     */
    public <T extends Component> List<T> getComponents(Class<T> componentClass) {
        List<T> componentList = new ArrayList<>();
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    componentList.add(componentClass.cast(component));
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
        return componentList;
    }

    /**
     * Remove a component from the RenderGroup
     * The component which is deleted will call the remove method if it has one
     *
     * @param component the component to remove
     */
    public void removeComponent(Component component) {
        component.remove();
        components.remove(component);
    }

    /**
     * Add a component to the RenderGroup
     *
     * @param component the component to add
     */
    public void addComponent(Component component) {
        this.components.add(component);
        component.renderGroup = this;
        component.addComponent();
    }

    /**
     * Add multiple components to the RenderGroup
     *
     * @param components the components array to add
     */
    public void addComponents(Component[] components) {
        for (Component component : components) {
            this.components.add(component);
            component.renderGroup = this;
            component.addComponent();
        }
    }

    /**
     * This will update every component every frame
     *
     * @param dt the delta time
     */
    public void update(double dt) {
        if (!this.lastTransform.equals(this.transform)) {
            this.lastTransform = this.transform.copy();
            dirty = true;
        }
        for (Component component : components) {
            component.update(dt);
            if (dirty) {
                component.setGameobjectDirty();
            }
        }
        dirty = false;
    }

    /**
     * Initialize all the components
     * This method is called when a Scene is initialized or when you add a RenderGroup to a Scene
     */
    public void init() {
        for (Component component : components) {
            component.init();
        }
    }

    /**
     * @return the name of the RenderGroup
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Transform object of this RenderGroup
     */
    public Transform getTransform() {
        return this.transform;
    }

    /**
     * @return the Z level of this RenderGroup
     */
    public int getzIndex() {
        return zIndex;
    }
}
