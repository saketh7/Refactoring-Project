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
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JPanel;

import de.mbi.goldenhasweg.game.Dwarf;
import de.mbi.goldenhasweg.game.Locatable;
import de.mbi.goldenhasweg.game.action.ActionList;
import de.mbi.goldenhasweg.game.dwarf.DwarfList;
import de.mbi.goldenhasweg.view.Controller;
import de.mbi.goldenhasweg.view.ControllerName;
import de.mbi.goldenhasweg.view.MainController;
import de.mbi.goldenhasweg.view.game.GameInitializer;

/**
 * The controller of the game. If this controller is started for the first time
 * a world map is initialized from a file.
 * 
 * @author El_Matzos
 */
public class GameController extends Thread implements Controller {

    private static final Logger LOG =
        Logger.getLogger(GameController.class.getName());

    private final MainController mainController;
    private final GameModel model;
    private GameMenu menu;
    private final GamePanel view;

    /**
     * Creates a new GameController. Loads world data from file world.cfg,
     * initialize all lists and managers and creates the game menu
     * 
     * @param mainController
     *            The main controller of the game.
     */
    public GameController(final MainController mainController) {
        // read configuration files
        GameInitializer.configureResourcesAndWorkshpos();
        GameInitializer.initializeWorld();

        this.mainController = mainController;
        this.model = new GameModel();
        this.menu = createDefaultMenu();
        view = new GamePanel(model, menu);
    }

    /**
     * Starts the game loop.
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        centerScreenAtTheFirstDwarf();
        long time;

        // repeat
        while (true) {
            // current time
            time = System.currentTimeMillis();

            if (!model.isPause()) {
                // next turn
                doNextTurn();
            }

            // next turn
            model.updateTurn();
            // paint world and menu
            mainController.repaint();

            // calculate time to wait
            time = 250 - System.currentTimeMillis() + time;
            if (time < 0) {
                time = 0;
                LOG.warning("Operation too slow");
            }

            // short break
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                // don't handle this exception
            }
        }
    }

    /**
     * Starts the next turn. Lazy Dwarfs try to find a suitable action, busy
     * dwarfs continue their actions and obsolete actions will be removed from
     * the action list.
     */
    public void doNextTurn() {
        DwarfList.getInstance().doNextTurn();
        ActionList.getInstance().nextTurn();
    }

    /**
     * Displays the given game menu at right hand side of the panel.
     * 
     * @param menu
     *            game menu to display
     */
    public void openSubmenu(final GameMenu menu) {
        menu.setParentMenu(this.menu);
        this.menu = menu;
        this.menu.onShow();
        view.updateMenu(menu);
    }

    /**
     * Closes the current game menu and displays the parent menu.
     */
    public void closeSubmenu() {
        if (menu.getParentMenue() != null && menu.onClose()) {
            menu = menu.getParentMenue();
            menu.onShow();
            view.updateMenu(menu);
        }
    }

    /**
     * Cancels the current game menu and displays the parent menu.
     */
    public void cancelSubmenu() {
        if (menu.getParentMenue() != null) {
            menu.onCancel();
            menu = menu.getParentMenue();
            menu.onShow();
            view.updateMenu(menu);
        }
    }

    /**
     * Returns the current game menu
     * 
     * @return current game menu
     */
    public GameMenu getCurrentMenu() {
        return menu;
    }

    /**
     * Returns the game model
     * 
     * @see de.mbi.dwarfgame.ui.Controller#getModel()
     * @return game model
     */
    @Override
    public GameModel getModel() {
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel getView() {
        return view;
    }

    /**
     * Moves the cursor up. The behavior depends on the selection mode.
     * 
     * @see SelectionMode
     */
    public void up() {
        model.getSelector().up(
            model.getSelectionMode().isSelectionEnabled());
    }

    /**
     * Moves the cursor down. The behavior depends on the selection mode.
     * 
     * @see SelectionMode
     */
    public void down() {
        model.getSelector().down(
            model.getSelectionMode().isSelectionEnabled());
    }

    /**
     * Moves the cursor left. The behavior depends on the selection mode.
     * 
     * @see SelectionMode
     */
    public void left() {
        model.getSelector().left(
            model.getSelectionMode().isSelectionEnabled());
    }

    /**
     * Moves the cursor right. The behavior depends on the selection mode.
     * 
     * @see SelectionMode
     */
    public void right() {
        model.getSelector().right(
            model.getSelectionMode().isSelectionEnabled());
    }

    /**
     * Centers the screen at the given location
     * 
     * @param item
     *            Object that should be centered
     */
    public void center(final Locatable item) {
        if (item.getPosition() != null) {
            model.getSelector().center(item.getPosition().getPos());
            model.highlightItem(item);
        }
    }

    /**
     * Displays the main menu.
     */
    public void switchToEscController() {
        mainController.activateController(ControllerName.MainMenu);
    }

    // -----------------------------------
    // Interface KeyListener
    // -----------------------------------

    /** {@inheritDoc} */
    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        menu.handleKey(keyEvent);
        mainController.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(final KeyEvent keyEvent) {
        // don't handle this event
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(final KeyEvent keyEvent) {
        // don't handle this event
    }

    /**
     * Centers the screen at the first dwarf of the dwarf list.
     */
    protected void centerScreenAtTheFirstDwarf() {
        Iterator<Dwarf> dwarfs = DwarfList.getInstance().getDwarfs();
        if (dwarfs.hasNext()) {
            Dwarf dwarf = dwarfs.next();
            center(dwarf);
        }
    }

    // -----------------------------
    // private methods
    // -----------------------------

    /**
     * Create the default game menu
     * 
     * @return default game menu
     */
    private GameMenu createDefaultMenu() {
        try {
            Class<?> clazz = Class.forName(
                    "de.mbi.goldenhasweg.view.game.menu.DefaultDwarfMenu");
            Constructor<?> constructor =
                clazz.getConstructor(GameController.class);
            return (GameMenu) constructor.newInstance(this);
        } catch (Exception e) {
            LOG.throwing(getClass().getName(), "createDefaultMenu()", e);
            return null;
        }
    }

	boolean isModelPause() {
		return getModel().isPause();
	}

	boolean isModelModeEnabled() {
		return model.isModeEnabled(this);
	}
}
