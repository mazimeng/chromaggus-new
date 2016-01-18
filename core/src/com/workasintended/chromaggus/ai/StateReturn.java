//package com.workasintended.chromaggus.ai;
//
//import com.badlogic.gdx.math.Vector2;
//import com.workasintended.chromaggus.*;
//import com.workasintended.chromaggus.order.Move;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Observable;
//import java.util.Observer;
//
//public class StateReturn extends AiState {
//	private Vector2 returnTo;
//
//	public StateReturn(Unit unit, Vector2 returnTo) {
//		super(unit);
//		this.returnTo = returnTo;
//	}
//
//	@Override
//	public void onEnter() {
//		Move move = new Move(getUnit(), returnTo);
//		move.onComplete().addObserver(new Observer() {
//			@Override
//			public void update(Observable o, Object arg) {
//				StateReturn.this.getUnit().ai.setAiState(new StateIdle(StateReturn.this.getUnit()));
//				System.out.println("returned " + returnTo);
//
//			}
//		});
//		getUnit().setOrder(move);
//	}
//
//	@Override
//	public void update(float delta) {
//
//	}
//
//}
