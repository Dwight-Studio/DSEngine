/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.scheduling;

import fr.dwightstudio.dsengine.graphics.GLFWWindow;
import fr.dwightstudio.dsengine.graphics.utils.FramebufferManager;
import fr.dwightstudio.dsengine.graphics.utils.SceneManager;
import fr.dwightstudio.dsengine.logging.GameLogger;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class Scheduler {

    // Basic var
    private static int lastTaskID = 0;
    private static CopyOnWriteArrayList<Task> pendingTasks = new CopyOnWriteArrayList<>();

    // Pre/Post Loops
    public static final int PRE_INPUT = 0b1;
    public static final int POST_INPUT = 0b10;
    public static final int PRE_UPDATE = 0b100;
    public static final int POST_UPDATE = 0b1000;
    public static final int PRE_RENDER = 0b10000;
    public static final int POST_RENDER = 0b100000;

    // Scheduling specific
    public static final int POST_PROCESSING = 0b1000000;
    public static final int CLEANING = 0b10000000;

    // Main loops (in the order of call)

    public static void inputLoop(double dt) {

    }

    public static void updateLoop(double dt) {

    }

    public static void renderLoop(double dt) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the current framebuffer

        // Render the current scene
        SceneManager.updateScenes(dt);

        FramebufferManager.renderAll();
        glfwSwapBuffers(GLFWWindow.getWindow()); // Swap the buffers
    }

    // Master loop

    public static void masterLoop(double dt) {

        execute(PRE_INPUT);
        inputLoop(dt);
        execute(POST_INPUT);

        execute(PRE_UPDATE);
        updateLoop(dt);
        execute(POST_UPDATE);

        execute(PRE_RENDER);
        renderLoop(dt);
        execute(POST_RENDER);

        execute(POST_PROCESSING);
        execute(CLEANING);
        clean();

    }

    // Task Execution and Cleaning

    private static void execute(int stage) {
        List<Task> stageTask = pendingTasks.stream().filter(task -> (task.stages & stage) == stage && !task.isCanceled()).sorted(Comparator.comparingInt(task -> task.priority)).collect(Collectors.toList());

        stageTask.forEach(task -> {
            try {
                task.internalDelay += GLFWWindow.getDeltaTime() * 1000;
                if (task.repeatable) {
                    task.internalPeriod += GLFWWindow.getDeltaTime() * 1000;
                    if (task.internalDelay >= task.delay && task.internalPeriod >= task.period) {
                        if (task.async) {
                            Thread t = new Thread(task.runnable);
                            t.setName(MessageFormat.format("Engine Task {0} Thread", task.ID));
                            t.start();
                        } else {
                            task.runnable.run();
                        }
                        task.internalPeriod = 0;
                    }
                } else {
                    if (task.internalDelay >= task.delay) {
                        if (task.async) {
                            Thread t = new Thread(task.runnable);
                            t.setName(MessageFormat.format("Engine Task {0} Thread", task.ID));
                            t.start();
                        } else {
                            task.runnable.run();
                        }
                        task.setCanceled(true);
                        pendingTasks.remove(task);
                    }
                }
            } catch (CancelTaskError e) {
                task.setCanceled(true);
                pendingTasks.remove(task);
            } catch (Exception e) {
                GameLogger.getLogger("Scheduler").warn(MessageFormat.format("Error while executing Task#{0}", task.ID));
                GameLogger.getLogger("Scheduler").warn(e.getStackTrace());
            }
        });
    }

    private static void clean() {
        pendingTasks = pendingTasks.stream().filter(task -> !task.canceled).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    // Task Scheduling

    /**
     * Plans a task to be executed at a specified execution stage.
     *
     * @param runnable the runnable to be executed
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param async boolean value that indicates if the task is asynchronous (executed in a thread)
     * @param priority the Execution priority (lowest priority = execute first)
     * @return the Task (can be canceled)
     */
    public static Task plan(Runnable runnable, int stages, boolean async, int priority) {
        Task task = new Task(runnable, stages, async, priority, 0, false, 0);
        pendingTasks.add(task);
        return task;
    }

    /**
     * Plans a task to be executed at a specified execution stage after a certain amount of time.
     *
     * @param runnable the runnable to be executed
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param async boolean value that indicates if the task is asynchronous (executed in a thread)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param delay the delay in millis to wait before executing (executed in the right stage after the delay is over)
     * @return the Task (can be canceled)
     */
    public static Task delay(Runnable runnable, int stages, boolean async, int priority, int delay) {
        Task task = new Task(runnable, stages, async, priority, delay, false, 0);
        pendingTasks.add(task);
        return task;
    }

    /**
     * Plans a task to be executed at a specified execution stage multiple times (with a period).
     *
     * @param runnable the runnable to be executed
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param async boolean value that indicates if the task is asynchronous (executed in a thread)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param period the delay between each execution (0 = execution at each iteration)
     * @return the Task (can be canceled)
     */
    public static Task planRepeated(Runnable runnable, int stages, boolean async, int priority, int period) {
        Task task = new Task(runnable, stages, async, priority, 0, true, period);
        pendingTasks.add(task);
        return task;
    }

    /**
     * Plans a task to be executed at a specified execution stage multiple times (with a period) after a certain amount of time.
     *
     * @param runnable the runnable to be executed
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param async boolean value that indicates if the task is asynchronous (executed in a thread)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param period the delay between each execution (0 = execution at each iteration)
     * @param delay the delay in millis to wait before executing (executed in the right stage after the delay is over)
     * @return the Task (can be canceled)
     */
    public static Task delayRepeated(Runnable runnable, int stages, boolean async, int priority, int period, int delay) {
        Task task = new Task(runnable, stages, async, priority, delay, true, period);
        pendingTasks.add(task);
        return task;
    }

    /**
     * Plans a runnable to be executed at a specified execution stage multiple times (with a period, if repeatable) after a certain amount of time.
     *
     * @param runnable the runnable to be executed
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param async boolean value that indicates if the task is asynchronous (executed in a thread)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param delay the delay in millis to wait before executing (executed in the right stage after the delay is over)
     * @param repeatable boolean value that indicates the periodicity of the task
     * @param period the delay between each execution (0 = execution at each iteration)
     * @return the Task (can be canceled)
     */
    public static Task schedule(Runnable runnable, int stages, boolean async, int priority, int delay, boolean repeatable, int period) {
        Task task = new Task(runnable, stages, async, priority, delay, repeatable, period);
        pendingTasks.add(task);
        return task;
    }

    /**
     * @return a fresh Task ID
     */
    protected static int getID() {
        lastTaskID++;
        return lastTaskID-1;
    }
}
