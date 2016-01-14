package com.workasintended.chromaggus;

import java.util.HashMap;
import java.util.LinkedList;

public interface EventQueue {
	void handle(float delta);
	
	void register(EventName name, EventHandler handler);
	void enqueue(Event event);
}
