package com.dungeon;

import java.applet.Applet;
import java.awt.BorderLayout;

public class MainApplet extends Applet {
    private static final long serialVersionUID = 1L;

    private MainComponent game;

    public void init() {
        game = new MainComponent();
        setLayout(new BorderLayout());
        add(game, BorderLayout.CENTER);
    }

    public void start() {
        game.start();
    }

    public void stop() {
        game.stop();
    }
}