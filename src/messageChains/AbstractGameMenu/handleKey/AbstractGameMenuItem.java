package messageChains.AbstractGameMenu.handleKey;

/*
 * The Golden Hasweg: A Dwarven Tale
 * 
 * Copyright (C) 2010 Mathias Bielert
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

import de.mbi.goldenhasweg.view.game.GameMenuItem;

/**
 * Base class for menu items.
 * 
 * @author El_Matzos
 */
public abstract class AbstractGameMenuItem implements GameMenuItem {
    private final GameController controller;
    private final char key;
    private String description;

    /**
     * Creates a new menu item
     * 
     * @param controller
     *            Controller
     * @param key
     *            Key to activate this item
     * @param description
     *            How to display this menu item
     */
    public AbstractGameMenuItem(final GameController controller,
            final char key,
            final String description) {
        this.controller = controller;
        this.key = key;
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    public char getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the controller
     * 
     * @return Controller
     */
    protected GameController getController() {
        return controller;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void execute();
}
