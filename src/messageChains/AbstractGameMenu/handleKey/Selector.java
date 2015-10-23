package messageChains.AbstractGameMenu.handleKey;

import de.mbi.goldenhasweg.game.Pos;
import de.mbi.goldenhasweg.game.map.PosImpl;
import de.mbi.goldenhasweg.game.map.WorldMap;
import de.mbi.goldenhasweg.view.ScreenDefinition;

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

/**
 * Util class, that selects a position from the position of a cursor at the
 * screen.
 * 
 * @author El_Matzos
 */
class Selector {
    private int offsetX;
    private int offsetY;
    private int selectorPosX;
    private int selectorPosY;
    private ScreenDefinition screenDefinition;

    /**
     * Creates a new selector.
     * 
     * @param screenDefinition
     *            Current screen definition
     */
    public Selector(final ScreenDefinition screenDefinition) {
        this.screenDefinition = screenDefinition;
        offsetX = 0;
        offsetY = 0;
        selectorPosX = screenDefinition.getWidthGameFrame() / 2;
        selectorPosY = screenDefinition.getHeight() / 2;
    }

    /**
     * Returns the x offset of the displayed position that is most left.
     * 
     * @return x offset
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Returns the y offset of the displayed position that is most up.
     * 
     * @return y offset
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Updates the x and the y offset. This changes the displayed fields.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getWidthGameFrame() for x values and between 0 and
     * screenDefinition.getHeight() for y values.
     * 
     * @param offX
     *            amount of change the x offset
     * @param offY
     *            amount of change the y offset
     */
    public void setOffset(final int offX, final int offY) {
        offsetX = offsetX + offX;
        if (offsetX + screenDefinition.getWidthGameFrame() > WorldMap
                .getInstance().getSizeX() - 1) {
            offsetX = WorldMap.getInstance().getSizeX()
                    - screenDefinition.getWidthGameFrame() - 1;
        }
        if (offsetX < 0) {
            offsetX = 0;
        }

        offsetY = offsetY + offY;
        if (offsetY + screenDefinition.getHeight() > WorldMap
                .getInstance().getSizeY() - 1) {
            offsetY = WorldMap.getInstance().getSizeY()
                    - screenDefinition.getHeight() - 1;
        }
        if (offsetY < 0) {
            offsetY = 0;
        }
    }

    /**
     * Returns the x position of the cursor at the screen.
     * 
     * @return x position
     */
    public int getSelectedPosX() {
        return selectorPosX;
    }

    /**
     * Returns the y position of the cursor at the screen.
     * 
     * @return y position
     */
    public int getSelectedPosY() {
        return selectorPosY;
    }

    /**
     * Updates the x and the y position of the cursor.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getWidthGameFrame() for x values and between 0 and
     * screenDefinition.getHeight() for y values.
     * 
     * @param offsetX
     *            amount of change the x value
     * @param offsetY
     *            amount of change the y value
     */
    protected void setSelectedPos(final int offsetX, final int offsetY) {
        selectorPosX = selectorPosX + offsetX;
        if (selectorPosX > screenDefinition.getWidthGameFrame()) {
            selectorPosX = screenDefinition.getWidthGameFrame();
        }
        if (selectorPosX < 0) {
            selectorPosX = 0;
        }

        selectorPosY = selectorPosY + offsetY;
        if (selectorPosY > screenDefinition.getHeight()) {
            selectorPosY = screenDefinition.getHeight();
        }
        if (selectorPosY < 0) {
            selectorPosY = 0;
        }
    }

    /**
     * Returns the position in the world of the cursor.
     * 
     * @return position
     */
    public Pos getSelectedPosition() {
        return new PosImpl(offsetX + selectorPosX, offsetY + selectorPosY,
                0);
    }

    /**
     * Copies the values of this cursor to the given one.
     * 
     * @param selector
     *            cursor that should have the same values
     */
    public void copy(final Selector selector) {
        offsetX = selector.getOffsetX();
        offsetY = selector.getOffsetY();
        selectorPosX = selector.getSelectedPosX();
        selectorPosY = selector.getSelectedPosY();
    }

    /**
     * Move the cursor one step north. The distance is depending on the current
     * selection mode. If the selection mode is off, the cursor moves 10 fields,
     * otherwise one field.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getHeight().
     * 
     * @param isSelectionMode
     *            Flag, if a selection mode is on
     */
    public void up(final boolean isSelectionMode) {
        if (isSelectionMode) {
            setSelectedPos(0, -1);
            if (selectorPosY == 4) {
                int offset = offsetY;
                setOffset(0, -10);
                setSelectedPos(0, offset - offsetY);
            }
        } else {
            setOffset(0, -10);
        }
    }

    /**
     * Move the cursor one step south. The distance is depending on the current
     * selection mode. If the selection mode is off, the cursor moves 10 fields,
     * otherwise one field.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getHeight().
     * 
     * @param isSelectionMode
     *            Flag, if a selection mode is on
     */
    public void down(final boolean isSelectionMode) {
        if (isSelectionMode) {
            setSelectedPos(0, 1);
            if (selectorPosY == screenDefinition.getHeight() - 4) {
                int offset = offsetY;
                setOffset(0, 10);
                setSelectedPos(0, offset - getOffsetY());
            }
        } else {
            setOffset(0, 10);
        }
    }

    /**
     * Move the cursor one step west. The distance is depending on the current
     * selection mode. If the selection mode is off, the cursor moves 10 fields,
     * otherwise one field.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getWidthGameFrame().
     * 
     * @param isSelectionMode
     *            Flag, if a selection mode is on
     */
    public void left(final boolean isSelectionMode) {
        if (isSelectionMode) {
            setSelectedPos(-1, 0);
            if (selectorPosX == 4) {
                int offset = offsetX;
                setOffset(-10, 0);
                setSelectedPos(offset - offsetX, 0);
            }
        } else {
            setOffset(-10, 0);
        }
    }

    /**
     * Move the cursor one step east. The distance is depending on the current
     * selection mode. If the selection mode is off, the cursor moves 10 fields,
     * otherwise one field.
     * 
     * The implementation ensured that the displayed fields are always between 0
     * and screenDefinition.getWidthGameFrame().
     * 
     * @param isSelectionMode
     *            Flag, if a selection mode is on
     */
    public void right(final boolean isSelectionMode) {
        if (isSelectionMode) {
            setSelectedPos(1, 0);
            if (selectorPosX == screenDefinition.getWidthGameFrame() - 4) {
                int offset = offsetX;
                setOffset(10, 0);
                setSelectedPos(offset - offsetX, 0);
            }
        } else {
            setOffset(10, 0);
        }
    }

    /**
     * Centers the screen at the given position.
     * 
     * @param pos
     *            position to center.
     */
    public void center(final Pos pos) {
        int newOffsetX = pos.getX() - screenDefinition.getWidthGameFrame()
                / 2;
        int newOffsetY = pos.getY() - screenDefinition.getHeight() / 2;
        setOffset(newOffsetX - offsetX, newOffsetY - offsetY);

        Pos selectedPos = getSelectedPosition();
        int newSelector1PosX = pos.getX() - selectedPos.getX();
        int newSelector1PosY = pos.getY() - selectedPos.getY();
        setSelectedPos(newSelector1PosX, newSelector1PosY);
    }

    /**
     * returns true, if the given position is the selected position.
     * 
     * @param pos
     *            Position
     * @return true, if the given position is the selected position.
     */
    public boolean isSelectedPosition(final Pos pos) {
        return offsetX + selectorPosX == pos.getX()
                && offsetY + selectorPosY == pos.getY();
    }

    /**
     * Sets a new screen definition
     * 
     * @param screenDefinition
     *            new screen definition
     */
    public void updateScreenDefinition(
            final ScreenDefinition screenDefinition) {
        this.screenDefinition = screenDefinition;
        setOffset(0, 0);
    }
}
