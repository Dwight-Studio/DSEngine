/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.scheduling;

public abstract class ScheduledRunnable implements Runnable {
    /**
     * Plans this runnable to be executed at a specified execution stage.
     *
     * @param stages a Bit flag that indicates the execution stage
     * @param priority the Execution priority (lowest priority = execute first)
     * @return the Task (can be canceled)
     */
    public final Task plan(int stages, int priority) {
        return Scheduler.plan(this, stages, priority);
    }

    /**
     * Plans this runnable to be executed at a specified execution stage after a certain amount of time.
     *
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param delay the delay in millis to wait before executing (executed in the right stage after the delay is over)
     * @return the Task (can be canceled)
     */
    public final Task delay(int stages, int priority, int delay) {
        return Scheduler.delay(this, stages, priority, delay);
    }

    /**
     * Plans this runnable to be executed at a specified execution stage multiple times (with a period).
     *
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param period the delay between each execution (0 = execution at each iteration)
     * @return the Task (can be canceled)
     */
    public final Task planRepeated(int stages, int priority, int period) {
        return Scheduler.planRepeated(this, stages, priority, period);
    }

    /**
     * Plans this runnable to be executed at a specified execution stage multiple times (with a period) after a certain amount of time.
     *
     * @param stages a Bit flag that indicates the execution stage (see const PRE_RENDER for example)
     * @param priority the Execution priority (lowest priority = execute first)
     * @param period the delay between each execution (0 = execution at each iteration)
     * @param delay the delay in millis to wait before executing (executed in the right stage after the delay is over)
     * @return the Task (can be canceled)
     */
    public final Task delayRepeated(int stages, int priority, int period, int delay) {
        return Scheduler.delayRepeated(this, stages, priority, period, delay);
    }

    /**
     * Cancels the task.
     * @throws CancelTaskError to indicate that it is canceled.
     */
    public final void cancel() throws CancelTaskError {
        throw new CancelTaskError();
    }
}
