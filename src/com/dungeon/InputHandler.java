package com.dungeon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import com.dungeon.Keys.Key;

public class InputHandler implements KeyListener {
	private Map<Integer, Key> mappings = new HashMap<Integer, Key>();

	public InputHandler(Keys keys) {
		mappings.put(KeyEvent.VK_UP, keys.up);
		mappings.put(KeyEvent.VK_DOWN, keys.down);
		mappings.put(KeyEvent.VK_LEFT, keys.left);
		mappings.put(KeyEvent.VK_RIGHT, keys.right);
		
		mappings.put(KeyEvent.VK_P, keys.pause);
		mappings.put(KeyEvent.VK_I, keys.inventory);

		mappings.put(KeyEvent.VK_W, keys.up);
		mappings.put(KeyEvent.VK_S, keys.down);
		mappings.put(KeyEvent.VK_A, keys.left);
		mappings.put(KeyEvent.VK_D, keys.right);
		
		mappings.put(KeyEvent.VK_1, keys.weap1);
		mappings.put(KeyEvent.VK_2, keys.weap2);
		mappings.put(KeyEvent.VK_3, keys.weap3);
	}

	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}

	public void keyTyped(KeyEvent ke) {
	}

	private void toggle(KeyEvent ke, boolean state) {
		Key key = mappings.get(ke.getKeyCode());
		if (key != null) {
			key.nextState = state;
		}
	}
}
