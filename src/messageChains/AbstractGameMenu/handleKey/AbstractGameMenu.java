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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mbi.goldenhasweg.view.game.GameMenuItem;
import de.mbi.goldenhasweg.view.game.SelectionMode;

/**
 * Absract Game Menu Class jeevan
 * Base class of all menus.
 * 
 * @author El_Matzos
 */
public abstract class AbstractGameMenu implements GameMenu {

    private GameMenu parentMenu = null;

    private final GameController controller;

    private final String headline;
    private final List<GameMenuItem> menuItems =
        new ArrayList<GameMenuItem>();
    private final List<GameMenuItem> footerMenuItems =
        new ArrayList<GameMenuItem>();

    private final Map<Character, GameMenuItem> commands =
        new HashMap<Character, GameMenuItem>();

    private boolean lastPage = true;
    private boolean firstPage = true;

    private SelectionMode selectionMode;

    /**
     * Creates a new menu
     * 
     * @param controller
     *            Controller
     */
    public AbstractGameMenu(final GameController controller) {
        this(controller, "");
    }

    /**
     * Creates a new menu with a given headline.
     * 
     * @param controller
     *            Controller
     * @param headline
     *            headline
     */
    public AbstractGameMenu(final GameController controller,
            final String headline) {
        this.headline = headline;
        this.controller = controller;
        this.selectionMode = SelectionMode.OFF;

        //addFooterMenuItem(new NavigateBackMenuItem(controller));
        //addFooterMenuItem(new PauseContinueMenuItem(controller));
        //addFooterMenuItem(new SelectedTextMenuItem(""));
    }

    /** {@inheritDoc} */
    public void onShow() {
        controller.getModel().setSelectionMode(selectionMode);
    }

    /** {@inheritDoc} */
    public void setSelectionMode(final SelectionMode requiredSelectionMode) {
        this.selectionMode = requiredSelectionMode;
    }

    /**
     * Adds a new item to the menu
     * 
     * @param item
     *            new menu item
     */
    protected void addMenuItem(final AbstractGameMenuItem item) {
        menuItems.add(item);
        if (item.getKey() != ' ') {
            commands.put(item.getKey(), item);
        }
    }

    /**
     * Adds a new item to the footer of the menu
     * 
     * @param item
     *            new menu item
     */
    protected final void addFooterMenuItem(final AbstractGameMenuItem item) {
        footerMenuItems.add(item);
        if (item.getKey() != ' ') {
            commands.put(item.getKey(), item);
        }
    }

    /**
     * Removes all items from the menu
     */
    protected void reset() {
        menuItems.clear();
    }

    /**
     * Removes all footer menu items
     * 
     */
    protected void resetFooter() {
        footerMenuItems.clear();

        //addFooterMenuItem(new NavigateBackMenuItem(controller));
        //addFooterMenuItem(new PauseContinueMenuItem(controller));
    }

    /** {@inheritDoc} */
    public List<GameMenuItem> getMenuItems() {
        return menuItems;
    }

    /** {@inheritDoc} */
    public List<GameMenuItem> getFooterMenuItems() {
        return footerMenuItems;
    }

    /** {@inheritDoc} */
    public void handleKey(final KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                // is root menu?
                if (controller.getCurrentMenu().getParentMenue() == null) {
                    // pause and switch to main menu
                    if (!isModelPause()) {
                        controller.getModel().setPause();
                    }
                    controller.switchToEscController();
                } else {
                    // close sub menu
                    controller.cancelSubmenu();
                }
                break;
            case KeyEvent.VK_SPACE:
                controller.getModel().setPause();
                break;
            case KeyEvent.VK_COMMA:
                if (isModelPause()) {
                    controller.doNextTurn();
                }
                break;
            case KeyEvent.VK_F1:
                controller.getModel().toggleMenu();
                break;
            case KeyEvent.VK_DOWN:
                controller.down();
                break;
            case KeyEvent.VK_UP:
                controller.up();
                break;
            case KeyEvent.VK_RIGHT:
                controller.right();
                break;
            case KeyEvent.VK_LEFT:
                controller.left();
                break;
            case KeyEvent.VK_ENTER:
			if (isModelModeEnabled()) {
                    if (controller.getModel().getSelectionMode().isMultiSelectionEnabled()) {
                        if (controller.getModel().isFirstSelectionDone()) {
                            controller.closeSubmenu();
                        } else {
                            controller.getModel().firstSelectionDone();
                        }
                    } else {
                        controller.closeSubmenu();
                    }
                }
                break;
            default:
                GameMenuItem item = commands.get(keyEvent.getKeyChar());
                if (item != null) {
                    item.execute();
                }
        }
    }

	private boolean isModelModeEnabled() {
		return controller.getModel().getSelectionMode().isSelectionEnabled();
	}

	private boolean isModelPause() {
		return controller.isModelPause();
	}

    /** {@inheritDoc} */
    public void setParentMenu(final GameMenu parentMenu) {
        this.parentMenu = parentMenu;
    }

    /** {@inheritDoc} */
    public GameMenu getParentMenue() {
        return parentMenu;
    }

    /** {@inheritDoc} */
    public abstract boolean onClose();

    /** {@inheritDoc} */
    public void onCancel() {
        // default implementation is empty
    }

    /**
     * Returns the controller of this menu
     * 
     * @return controller of this menu
     */
    protected GameController getController() {
        return controller;
    }


    /** {@inheritDoc} */
    public String getHeadline() {
        return headline;
    }

    /** {@inheritDoc} */
    public boolean isLastPage() {
        return lastPage;
    }

    /** {@inheritDoc} */
    public void setLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
    }

    /** {@inheritDoc} */
    public boolean isFirstPage() {
        return firstPage;
    }

    /** {@inheritDoc} */
    public void setFirstPage(final boolean firstPage) {
        this.firstPage = firstPage;
    }
}
