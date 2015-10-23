package messageChains.AbstractGameMenu.handleKey;

/*
 * The Golden Hasweg: A Dwarven Tale
 * 
 * Copyright (C) 2011 Mathias Bielert
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

import java.awt.event.KeyEvent;
import java.util.List;

import de.mbi.goldenhasweg.view.game.GameMenuItem;
import de.mbi.goldenhasweg.view.game.SelectionMode;

/**
 * Interface for all game menus.
 * 
 * @author El_Matzos
 */
public interface GameMenu {

    /**
     * Is called before the menu is shown.
     */
    void onShow();

    /**
     * Sets a new selection mode.
     * 
     * @param requiredSelectionMode
     *            new selection mode.
     */
    void setSelectionMode(final SelectionMode requiredSelectionMode);

    /**
     * Returns all menu item
     * 
     * @return list of all menu items
     */
    List<GameMenuItem> getMenuItems();

    /**
     * Returns all footer menu item
     * 
     * @return list of all footer menu items
     */
    List<GameMenuItem> getFooterMenuItems();

    /**
     * Handles key events
     * 
     * @param keyEvent
     *            key event
     */
    void handleKey(final KeyEvent keyEvent);

    /**
     * Sets the given menu as parent menu
     * 
     * @param parentMenu
     *            parent menu
     */
    void setParentMenu(final GameMenu parentMenu);

    /**
     * Returns the parent menu
     * 
     * @return parent menu
     */
    GameMenu getParentMenue();

    /**
     * Is called before close a menu
     * 
     * @return true, if the menu can be closed.
     */
    boolean onClose();

    /**
     * Is called before cancel a menu
     */
    void onCancel();

    /**
     * Returns the headline of this menu
     * 
     * @return headline
     */
    String getHeadline();

    /**
     * Returns true, if the menu has no additional pages after the current page
     * 
     * @return true, if the menu has no additional pages after the current page
     */
    boolean isLastPage();

    /**
     * Sets, if the menu has no additional pages after the current page
     * 
     * @param lastPage
     *            true, if the menu has no additional pages after the current
     *            page
     */
    void setLastPage(final boolean lastPage);

    /**
     * Returns true, if the menu has no additional pages before the current page
     * 
     * @return true, if the menu has no additional pages before the current page
     */
    boolean isFirstPage();

    /**
     * Sets, if the menu has no additional pages before the current page
     * 
     * @param firstPage
     *            true, if the menu has no additional pages before the current
     *            page
     */
    void setFirstPage(final boolean firstPage);

}