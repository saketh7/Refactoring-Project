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

import java.awt.Color;

import de.mbi.goldenhasweg.game.Field;
import de.mbi.goldenhasweg.game.Locatable;
import de.mbi.goldenhasweg.game.Pos;
import de.mbi.goldenhasweg.game.map.WorldMap;
import de.mbi.goldenhasweg.util.GraphicCharacter;
import de.mbi.goldenhasweg.view.Model;
import de.mbi.goldenhasweg.view.ScreenDefinition;
import de.mbi.goldenhasweg.view.game.SelectionMode;

/**
 * This class holds information of the displayed data of the game.
 * 
 * @author El_Matzos
 */
public class GameModel implements Model {

    // if selection mode is multi and the selection of the first
    // position of the area is done this flag is true
    private boolean firstSelectionDone = false;

    private SelectionMode selectionMode;

    private final Selector selector1;
    private final Selector selector2;

    private Locatable highlightedItem = null;
    private int highlightDuration = 0;

    private boolean pause = true;
    private int turn = 0;

    private ScreenDefinition screenDefinition;

    /**
     * Creates a new game model
     */
    public GameModel() {
        selectionMode = SelectionMode.OFF;
        screenDefinition = ScreenDefinition.MENU_RIGHT;
        selector1 = new Selector(screenDefinition);
        selector2 = new Selector(screenDefinition);
    }

    /**
     * Counts the turns from 0 to 11. This number is used to display different
     * phases of an animation.
     */
    protected void updateTurn() {
        if (turn == 11) {
            turn = 0;
        } else {
            turn++;
        }
    }

    /**
     * Sets the character to display at the given position.
     * 
     * @param pos
     *            Position that should displayed
     * @param graphic
     *            Cell to display
     */
    protected void paint(final Pos pos, final GraphicCharacter graphic) {
        // paint field
        paintField(pos, graphic);

        if (highlightedItem != null && selector1.isSelectedPosition(pos)) {
            // paint highlighted item
            if (turn % 2 == 0) {
                highlightedItem.paint(graphic, 0, pause);
                highlightDuration++;
            } else {
                paintSelectionMark(graphic, 'x');
            }

            if (highlightDuration == 4) {
                highlightedItem = null;
                highlightDuration = 0;
            }
        } else if (isSelectionMarkPosition(pos)) {
            // paint selection mark for single selection
            paintSelectionMark(graphic, 'X');
        } else if (isMultiSelectionMarkPosition(pos)) {
            // paint selection mark for multi selection
            paintSelectionMark(graphic, '+');
        }
    }

    /**
     * Renders the contents of the field at the given position in the given
     * cell.
     * 
     * @param position
     *            Position that should displayed
     * @param graphic
     *            Cell to display
     */
    protected void paintField(final Pos position,
            final GraphicCharacter graphic) {
        graphic.setCharacter(0);

        Field field = WorldMap.getInstance().getField(position);
        if (field != null) {
            field.paint(graphic, turn, pause);
        }
    }

    /**
     * Switch the pause mode.
     */
    public void setPause() {
        pause = !pause;
    }

    /**
     * Returns true if the pause mode is enabled
     * 
     * @return true if the pause mode is enabled
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * Sets a new selection mode
     * 
     * @param selectionMode
     *            new selection mode
     * @see SelectionMode
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        firstSelectionDone = false;
    }

    /**
     * Returns the current selection mode
     * 
     * @return current selection mode
     */
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    /**
     * If a multiselection is enabled, returns if the first position was
     * selected. Otherwise false.
     * 
     * @return If a multiselection is enabled, returns true if the first
     *         position was selected. Otherwise false.
     */
    public boolean isFirstSelectionDone() {
        return firstSelectionDone;
    }

    /**
     * If a multiselection is enabled, mark the current position is selected.
     * Otherwise the method does nothing.
     */
    public void firstSelectionDone() {
        if (selectionMode.isMultiSelectionEnabled()) {
            selector2.copy(selector1);
            firstSelectionDone = true;
        }
    }

    /**
     * Returns the selector
     * 
     * @return selector
     * @see Selector
     */
    public Selector getSelector() {
        return selector1;
    }

    /**
     * Returns the selected position.
     * 
     * @return selected position
     */
    public Pos getSelectedPosition() {
        return selector1.getSelectedPosition();
    }

    /**
     * If a multiselection is enabled, returns the selected second position.
     * 
     * @return selected second position
     */
    public Pos getSelectedPosition2() {
        return selector2.getSelectedPosition();
    }


    /**
     * Highlights the given building, item or dwarf for a short time.
     * 
     * @param item
     *            building, item or dwarf to highlight
     */
    void highlightItem(final Locatable item) {
        highlightedItem = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScreenDefinition getScreenDefinition() {
        return screenDefinition;
    }

    /**
     * Toggle the current state of the menu. First the menu is displayed at the
     * right side. After the first call of the method it is display at the left
     * side. If you call this method again, the menu disappears.
     */
    public void toggleMenu() {
        if (screenDefinition == ScreenDefinition.MENU_RIGHT) {
            screenDefinition = ScreenDefinition.MENU_LEFT;
        } else if (screenDefinition == ScreenDefinition.MENU_LEFT) {
            screenDefinition = ScreenDefinition.NO_MENU;
        } else if (screenDefinition == ScreenDefinition.NO_MENU) {
            screenDefinition = ScreenDefinition.MENU_RIGHT;
        }
        selector1.updateScreenDefinition(screenDefinition);
        selector2.updateScreenDefinition(screenDefinition);
    }

    // ------------------------------
    // private methods
    // ------------------------------

    /**
     * Returns true, if a multi selection is active, the first selection is done
     * and the selected position is given position
     * 
     * @param pos
     *            position
     * @return true, if a multi selection is active, the first selection is done
     *         and the selected position is given position
     */
    private boolean isMultiSelectionMarkPosition(final Pos pos) {
        return turn % 2 == 0 && selectionMode.isMultiSelectionEnabled()
                && firstSelectionDone
                && pos.getX() - selector2.getOffsetX()
                        == selector2.getSelectedPosX()
                && pos.getY() - selector2.getOffsetY()
                        == selector2.getSelectedPosY();
    }

    /**
     * Returns true if a selection is active and the selection mark is at the
     * given position
     * 
     * @param pos
     *            position
     * @return true if a selection is active and the selection mark is at the
     *         given position
     */
    private boolean isSelectionMarkPosition(final Pos pos) {
        return selectionMode.isSelectionEnabled()
                && selector1.isSelectedPosition(pos);
    }

    /**
     * Sets a selection mark at the given cell
     * 
     * @param graphic
     *            Cell to display
     * @param sign
     *            Sign to paint
     */
    private void paintSelectionMark(final GraphicCharacter graphic,
            final char sign) {
        graphic.setCharacter(sign);

        // use inverted background color
        int rgb = graphic.getBackgroundColor().getRGB();
        int inverseRgb = rgb ^ 0x00FFFFFF;
        graphic.setForegroundColor(new Color(inverseRgb));
    }

	boolean isModeEnabled(GameController gameController) {
		return gameController.getModel().getSelectionMode().isSelectionEnabled();
	}

}
