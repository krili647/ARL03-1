/**
 * The component into which the game screen will be drawn
 */

package se.liu.ida.awesomeroguelike2003;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class RLComponent extends JComponent
{
    private Game game;

    public RLComponent(final Game game) {
	this.game = game;
	assignKeys();
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;


	//Fill background with super metal pitch-black darkness
	g2d.setColor(Color.BLACK);
	g2d.fillRect(0,0,TestGame.WIDTH, TestGame.HEIGHT);

	//Draw only the tiles that can be found in the scope
	final int startDrawingAtX = game.getPlayer().getX() - TestGame.SCOPEWIDTH/2;
	final int startDrawingAtY = game.getPlayer().getY() - TestGame.SCOPEHEIGHT/2;

	for(int x = 0; x < TestGame.SCOPEWIDTH; x++) {
	    for(int y = 0; y < TestGame.SCOPEHEIGHT; y++) {

		if (startDrawingAtX + x >= 0 && startDrawingAtY  + y >= 0) {
		    if(startDrawingAtX + x < game.getMap().getMapWidth() && startDrawingAtY  + y < game.getMap().getMapHeight()) {

			Tile tile = game.getMap().getTileAt(startDrawingAtX + x, startDrawingAtY + y);
			tile.draw(g2d, x, y);
			if (!tile.isSolid()) {
			    for (GameObject o : tile.getGameObjects()) {
				o.draw(g2d, x, y);
			    }
			}
		    }
		}
	    }
	}

	drawPlayerInventory(g2d);

	if (game.getGameState() == GameState.PICKINGUP) {
	    drawItemScreen(g2d);
	}
    }

    private void drawItemScreen(Graphics2D g2d) {
	//draw background
	g2d.setColor(new Color(100, 100, 100, 150));
	g2d.fillRect(10, 10, 200, 300);

	//draw items at player square
	List<GameObject> items = game.getMap().getTileAt(game.getPlayer().getX(), game.getPlayer().getY()).getGameObjects();

	int iterator = 0;

	for(GameObject o : items) {
	    if (!o.equals(game.getPlayer())) {
		o.draw(g2d, 1, iterator*2 + 1);

		if(game.getPlayer().getInventory().getInventoryNavigator() == iterator) {
		    g2d.setColor(Color.YELLOW);
		} else {
		    g2d.setColor(Color.WHITE);
		}

		g2d.drawString(o.getName(), TestGame.SQUARESIZE*3, (iterator*2 + 2)*TestGame.SQUARESIZE - 5);

		iterator++;
	    }
	}

    }

    public void drawPlayerInventory(Graphics2D g2d) {
	g2d.setColor(Color.GRAY);
	g2d.fillRect(TestGame.SCOPEWIDTH*TestGame.SQUARESIZE, (TestGame.SCOPEHEIGHT - 5)*TestGame.SQUARESIZE,
		     TestGame.WIDTH - TestGame.SCOPEWIDTH*TestGame.SQUARESIZE, TestGame.HEIGHT - (TestGame.SCOPEHEIGHT - 5)*TestGame.SQUARESIZE);
	List<GameObject> items = game.getPlayer().getInventory().getInventory();

	if (!items.isEmpty()) {
	    for (int x = 0; x < items.size(); x++) {
		int dx = TestGame.SCOPEWIDTH + x%10;
		int dy = (TestGame.SCOPEHEIGHT - 5) + x/10;

		items.get(x).draw(g2d, dx, dy);
	    }
	}
    }

    private void assignKeys() {
		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD8"), "goNorth");
					final Action pressedUp = new AbstractAction()
						{
						    @Override public void actionPerformed(ActionEvent e) {
							if(game.getGameState() == GameState.PLAYING) {
							    game.getPlayer().moveTo(0, -1);
							    game.gameUpdated();
							} else if(game.getGameState() == GameState.PICKINGUP) {
							    game.getPlayer().decrementInventoryNavigator();
							    game.gameUpdated();
							}
						    }
				    };
		getActionMap().put("goNorth", pressedUp);

		getInputMap().put(KeyStroke.getKeyStroke("UP"), "goNorth");

		getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "goSouth");
					final Action pressedDown = new AbstractAction()
						{
						    @Override public void actionPerformed(ActionEvent e) {
							if(game.getGameState() == GameState.PLAYING) {
							    game.getPlayer().moveTo(0, 1);
							    game.gameUpdated();
							} else if(game.getGameState() == GameState.PICKINGUP) {
							    game.getPlayer().incrementInventoryNavigator();
							    game.gameUpdated();
							}
						    }
				    };
		getActionMap().put("goSouth", pressedDown);

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD2"), "goSouth");

		getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "goEast");
			final Action pressedRight = new AbstractAction()
				{
				    @Override public void actionPerformed(ActionEvent e) {
					game.getPlayer().moveTo(1, 0);
					game.gameUpdated();
					if(game.getGameState() == GameState.PICKINGUP) {
					    game.setGameState(GameState.PLAYING);
					}
				    }
		    };
		getActionMap().put("goEast", pressedRight);

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD6"), "goEast");

		getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "goWest");
				final Action pressedLeft = new AbstractAction()
					{
					    @Override public void actionPerformed(ActionEvent e) {
						game.getPlayer().moveTo(-1, 0);
						game.gameUpdated();
						if(game.getGameState() == GameState.PICKINGUP) {
						    game.setGameState(GameState.PLAYING);
						}
					    }
			    };
		getActionMap().put("goWest", pressedLeft);

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD4"), "goWest");

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD7"), "goNorthWest");
					final Action pressedSeven = new AbstractAction()
						{
						    @Override public void actionPerformed(ActionEvent e) {
							game.getPlayer().moveTo(-1, -1);
							game.gameUpdated();
							if(game.getGameState() == GameState.PICKINGUP) {
							    game.setGameState(GameState.PLAYING);
							}
						    }
				    };
		getActionMap().put("goNorthWest", pressedSeven);

	getInputMap().put(KeyStroke.getKeyStroke("NUMPAD9"), "goNorthEast");
						final Action pressedNine = new AbstractAction()
							{
							    @Override public void actionPerformed(ActionEvent e) {
								game.getPlayer().moveTo(1, -1);
								game.gameUpdated();
								if(game.getGameState() == GameState.PICKINGUP) {
								    game.setGameState(GameState.PLAYING);
								}
							    }
					    };
		getActionMap().put("goNorthEast", pressedNine);

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD1"), "goSouthWest");
							final Action pressedOne = new AbstractAction()
								{
								    @Override public void actionPerformed(ActionEvent e) {
									game.getPlayer().moveTo(-1, 1);
									game.gameUpdated();
									if(game.getGameState() == GameState.PICKINGUP) {
									    game.setGameState(GameState.PLAYING);
									}
								    }
						    };
		getActionMap().put("goSouthWest", pressedOne);

		getInputMap().put(KeyStroke.getKeyStroke("NUMPAD3"), "goSouthEast");
								final Action pressedThree = new AbstractAction()
									{
									    @Override public void actionPerformed(ActionEvent e) {
										game.getPlayer().moveTo(1, 1);
										game.gameUpdated();
										if(game.getGameState() == GameState.PICKINGUP) {
										    game.setGameState(GameState.PLAYING);
										}
									    }
							    };
		getActionMap().put("goSouthEast", pressedThree);

	getInputMap().put(KeyStroke.getKeyStroke("COMMA"), "pickUp");

	final Action pressedComma = new AbstractAction()
	    {
		@Override public void actionPerformed(ActionEvent e) {
		    if(game.getGameState() == GameState.PICKINGUP) {
			game.setGameState(GameState.PLAYING);
			game.gameUpdated();
		    } else if(game.getGameState() == GameState.PLAYING) {
			game.getPlayer().pickUp();
			game.gameUpdated();
		    }
	    }
	};
	getActionMap().put("pickUp", pressedComma);

	getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pickUpSelected");

	final Action pressedEnter = new AbstractAction()
	{
	    @Override public void actionPerformed(ActionEvent e) {
		if (game.getGameState() == GameState.PICKINGUP) {
		    game.getPlayer().pickUpSelectedItem();
		    game.gameUpdated();
		}
	    }
	};
	getActionMap().put("pickUpSelected", pressedEnter);
    }
}