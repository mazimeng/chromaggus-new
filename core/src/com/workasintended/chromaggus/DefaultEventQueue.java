package com.workasintended.chromaggus;

import com.workasintended.chromaggus.event.MoveToPositionEvent;

import java.util.HashMap;
import java.util.LinkedList;

public class DefaultEventQueue implements EventQueue {
	private LinkedList<Event> events;
	private HashMap<Class<?>, LinkedList<EventHandler>> handlers;
	public DefaultEventQueue() {
		this.events = new LinkedList<Event>();
		this.handlers = new HashMap<Class<?>, LinkedList<EventHandler>>();
	}
	
	public void handle(float delta) {
		while(!this.events.isEmpty()) {
			Event event = this.events.pop();
			LinkedList<EventHandler> handlerList = handlers.get(event.getName());
			
			if(handlerList == null) continue;
			
			for(EventHandler handler: handlerList) {
				handler.handle(event);
			}
		}
	}
	
	public void register(Class<Event> eventClass, EventHandler handler) {
//		LinkedList<EventHandler> handlerList = null;
//		if(!this.handlers.containsKey(name)) {
//			handlerList = new LinkedList<EventHandler>();
//			this.handlers.put(name, handlerList);
//		}
//		else {
//
//		}

		LinkedList<EventHandler> handlerList = this.handlers.get(eventClass);
		if(handlerList == null) {
			handlerList = new LinkedList<EventHandler>();
		}
		this.handlers.put(name, handlerList);
		handlerList.add(handler);
	}

	@Override
	public void enqueue(Event event) {
		this.events.add(event);
	}
}
