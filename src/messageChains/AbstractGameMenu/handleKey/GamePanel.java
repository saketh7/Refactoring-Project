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
import java.awt.Graphics;
import java.util.List;

import de.mbi.goldenhasweg.game.Pos;
import de.mbi.goldenhasweg.game.map.PosImpl;
import de.mbi.goldenhasweg.util.GraphicCharacter;
import de.mbi.goldenhasweg.view.AbstractPanel;
import de.mbi.goldenhasweg.view.ScreenDefinition;
import de.mbi.goldenhasweg.view.game.GameMenuItem;

/**
 * A panel that can display the game.
 * 
 * @author El_Matzos
 */
public class GamePanel extends AbstractPanel {

    private static final long serialVersionUID = 1L;

    private final GameModel model;
    private GameMenu menu;

    /**
     * Creates a new panel
     * 
     * @param model
     *            the game model
     * @param menu
     *            the current menu
     */
    public GamePanel(final GameModel model, final GameMenu menu) {
        this.model = model;
        this.menu = menu;
    }

    /**
     * Displays a new game menu at the panel
     * 
     * @param menu
     *            new menu
     */
    public void updateMenu(final GameMenu menu) {
        this.menu = menu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(final Graphics g) {
        ScreenDefinition screenDefinition = model.getScreenDefinition();

        drawFrame(g, screenDefinition);
        drawGame(g, screenDefinition);
        if (screenDefinition.hasMenu()) {
            drawMenu(g, screenDefinition);
        }
    }

    /**
     * Renders the menu
     * 
     * @param g
     *            Graphics
     * @param screenDefinition
     *            current screen definition
     */
    private void drawMenu(final Graphics g,
            final ScreenDefinition screenDefinition) {
        int posXMenuLine =
            screenDefinition.getStartMenuFrame() * CHARACTER_WIDTH
                    + CHARACTER_WIDTH;
        List<GameMenuItem> menuItems = menu.getMenuItems();
        int iy = 2;
        drawString(g, posXMenuLine, iy * CHARACTER_HEIGHT,
            menu.getHeadline(), Color.WHITE);
        drawString(g, posXMenuLine, (iy + 1) * CHARACTER_HEIGHT,
            "-----------------------------------------------".substring(0,
                menu.getHeadline()
                        .length()), Color.WHITE);

        iy = 5;
        if (!menu.isFirstPage()) {
            drawCharacter(
                g,
                (screenDefinition.getStartMenuFrame() + 25) * CHARACTER_WIDTH,
                iy * CHARACTER_HEIGHT, 24, Color.WHITE);
        }

        for (GameMenuItem menuItem : menuItems) {
            int posYMenuLine = iy * CHARACTER_HEIGHT;

            if (menuItem.isSelected()) {
                for (int i = 0; i < 24; i++) {
                    drawCharacter(g, posXMenuLine + i * CHARACTER_HEIGHT,
                        posYMenuLine, 219, Color.GRAY);
                }
            }

            if (menuItem.getKey() == ' ') {
                drawString(g, posXMenuLine, posYMenuLine,
                    menuItem.getDescription(), Color.WHITE);
            } else {
                drawCharacter(g, posXMenuLine, posYMenuLine,
                    menuItem.getKey(), Color.YELLOW);
                drawCharacter(g, posXMenuLine + 1 * CHARACTER_WIDTH,
                    posYMenuLine, ':', Color.WHITE);
                drawString(g, posXMenuLine + 3 * CHARACTER_WIDTH,
                    posYMenuLine, menuItem.getDescription(), Color.WHITE);
            }
            iy++;
        }

        if (!menu.isLastPage()) {
            drawCharacter(
                g,
                (screenDefinition.getStartMenuFrame() + 25) * CHARACTER_WIDTH,
                iy * CHARACTER_HEIGHT - CHARACTER_HEIGHT, 25, Color.WHITE);
        }

        menuItems = menu.getFooterMenuItems();
        iy = screenDefinition.getHeight();
        for (GameMenuItem menuItem : menuItems) {
            int posYMenuLine = iy * CHARACTER_HEIGHT;

            if (menuItem.getKey() == ' ') {
                drawString(g, posXMenuLine, posYMenuLine,
                    menuItem.getDescription(), Color.WHITE);
            } else {
                drawCharacter(g, posXMenuLine, posYMenuLine,
                    menuItem.getKey(), Color.YELLOW);
                drawCharacter(g, posXMenuLine + 1 * CHARACTER_WIDTH,
                    posYMenuLine, ':', Color.WHITE);

                drawString(g, posXMenuLine + 3 * CHARACTER_WIDTH,
                    posYMenuLine, menuItem.getDescription(), Color.WHITE);
            }
            iy--;
        }
    }

    /**
     * Renders the game
     * 
     * @param g
     *            Graphics
     * @param screenDefinition
     *            current screen definition
     */
    private void drawGame(final Graphics g,
            final ScreenDefinition screenDefinition) {
        int offsetY = CHARACTER_HEIGHT;
        int offsetX =
            screenDefinition.getStartGameFrame() * CHARACTER_WIDTH;
        Pos pos = new PosImpl(0, 0, 0);
        GraphicCharacter graphic = new GraphicCharacter();
        for (int iy = 0; iy <= screenDefinition.getHeight(); iy++) {
            for (int ix = 0; ix <= screenDefinition.getWidthGameFrame(); ix++) {
                pos.setX(ix + model.getSelector().getOffsetX());
                pos.setY(iy + model.getSelector().getOffsetY());
                model.paint(pos, graphic);

                if (graphic.getBackgroundColor() != Color.BLACK) {
                    drawCharacter(g, offsetX, offsetY, 219,
                        graphic.getBackgroundColor());
                }

                drawCharacter(g, offsetX, offsetY, graphic.getCharacter(),
                    graphic.getForegroundColor());
                offsetX = offsetX + CHARACTER_WIDTH + SPACE;
            }
            offsetX =
                screenDefinition.getStartGameFrame() * CHARACTER_WIDTH;
            offsetY = offsetY + CHARACTER_HEIGHT + SPACE;
        }
    }

    /**
     * Renders the frame
     * 
     * @param g
     *            Graphics
     * @param screenDefinition
     *            current screen definition
     */
    private void drawFrame(final Graphics g,
            final ScreenDefinition screenDefinition) {
        // horizontal bars
        for (int ix = 0; ix < 83; ix++) {
            drawCharacter(g, ix * CHARACTER_WIDTH, 0, 178, Color.CYAN);
            drawCharacter(g, ix * CHARACTER_WIDTH, 492, 178, Color.CYAN);
        }
        // vertical bars
        for (int iy = 0; iy < 42; iy++) {
            drawCharacter(g, 0, iy * CHARACTER_HEIGHT, 178, Color.CYAN);
            drawCharacter(g, 995, iy * CHARACTER_HEIGHT, 178, Color.CYAN);
        }
        // vertical bar, which separates the menu
        if (screenDefinition.hasMenu()) {
            int posXMenuLine =
                screenDefinition.getLinePosition() * CHARACTER_WIDTH;
            for (int iy = 0; iy < 41; iy++) {
                drawCharacter(g, posXMenuLine, iy * CHARACTER_HEIGHT, 178,
                    Color.CYAN);

            }
        }

        for (int ix = 32; ix < 51; ix++) {
            drawCharacter(g, ix * CHARACTER_WIDTH, 0, 219, Color.CYAN);
        }
        drawString(g, 33 * CHARACTER_WIDTH, 1, "The Golden Hasweg",
            Color.BLACK);

        if (model.isPause()) {
            for (int ix = 1; ix < 9; ix++) {
                drawCharacter(g, ix * CHARACTER_WIDTH, 0, 219, Color.RED);
            }
            drawString(g, CHARACTER_WIDTH, 1, "*PAUSED*", Color.WHITE);
        }
    }

}
