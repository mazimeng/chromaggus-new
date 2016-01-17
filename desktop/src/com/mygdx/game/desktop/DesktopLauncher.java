package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.workasintended.chromaggus.AndroidGameConfiguration;
import com.workasintended.chromaggus.ChromaggusGame;
import com.workasintended.chromaggus.GameConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		GameConfiguration gameConfiguration = new AndroidGameConfiguration();

		ChromaggusGame chromaggusGame = new ChromaggusGame(gameConfiguration);
		new LwjglApplication(chromaggusGame, config);
	}
}
