/*
 * Copyright (c) 2020-2021 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fr.dwightstudio.dsengine.events.types.gui.button;

import fr.dwightstudio.dsengine.graphics.gui.Button;

/**
 * Event fired when a Button that was hover is 'unhover'
 */
public class ButtonUnhoverEvent extends ButtonEvent {
    public ButtonUnhoverEvent(Button button) {
        super(button);
    }
}
