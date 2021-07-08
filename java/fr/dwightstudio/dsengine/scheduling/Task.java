/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.scheduling;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class Task implements Comparable {
    public final int ID;
    public final int stages;
    public final int priority;
    public final int delay;
    protected int internalDelay;
    public final boolean repeatable;
    public final int period;
    protected int internalPeriod;

    protected final Runnable runnable;

    public boolean canceled = false;

    protected Task(Runnable runnable, int stages, int priority, int delay, boolean repeatable, int period) {
        this.ID = Scheduler.getID();
        this.stages = stages;
        this.priority = priority;
        this.delay = delay;
        this.internalDelay = 0;
        this.repeatable = repeatable;
        this.period = period;
        this.internalPeriod = period;
        this.runnable = runnable;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof Task) {
            return this.ID - ((Task) o).ID;
        } else {
            throw new ClassCastException(MessageFormat.format("Cannot compare Task and {0}", o.getClass().getName()));
        }
    }
}
