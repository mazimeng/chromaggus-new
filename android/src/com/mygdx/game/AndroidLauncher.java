package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.workasintended.chromaggus.AndroidGameConfiguration;
import com.workasintended.chromaggus.AndroidInputHandler;
import com.workasintended.chromaggus.ChromaggusGame;
import com.workasintended.chromaggus.GameConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		GameConfiguration gameConfiguration = new AndroidGameConfiguration();

		ChromaggusGame chromaggusGame = new ChromaggusGame(gameConfiguration);
		initialize(chromaggusGame, config);
	}
}
