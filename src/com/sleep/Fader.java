package com.sleep;

import com.badlogic.gdx.graphics.Color;

public abstract class Fader {
	protected Color c;
	protected Color target;
	protected float frequency;
	private float steps;
	
	protected float incrementR;
	protected float incrementG;
	protected float incrementB;
	protected float incrementA;
	
	protected float timer = 0;
	
	public Fader(Color c, Color target, float frequency, float steps) {
		this.c = c;
		this.target = target;
		this.frequency = frequency;
		this.steps = steps;
		calcIncrements();
	}
	
	public Fader(Color c, Color target) {
		this.c = c;
		this.target = target;
		this.frequency = 0.05f;
		this.steps = 50;
		calcIncrements();
	}
	
	public Fader(Color c) {
		this.c = c;
		this.target = new Color(c);
		this.frequency = 0.05f;
		this.steps = 50;
		calcIncrements();
	}
	
	private void calcIncrements() {
		incrementR = (target.r - c.r) / steps;
		incrementG = (target.g - c.g) / steps;
		incrementB = (target.b - c.b) / steps;
		incrementA = (target.a - c.a) / steps;
	}
	
	protected float colorSum(Color c) {
		return c.r + c.g + c.b + c.a;
	}
	
	protected float incrementSum() {
		return incrementR + incrementG + incrementB + incrementA;
	}
	
	public void fadeIn() {
		target.set(Color.WHITE);
		calcIncrements();
	}
	
	public void fadeOut() {
		target.set(Color.BLACK);
		calcIncrements();
	}
	
	public void fadeTo(Color to) {
		target.set(to);
		calcIncrements();
	}
	
	public void goDark() {
		c.set(Color.BLACK);
		target.set(Color.BLACK);
		calcIncrements();
	}
	
	public void goLight() {
		c.set(Color.WHITE);
		target.set(Color.WHITE);
		calcIncrements();
	}
	
	public void goTo(Color to) {
		c.set(to);
		target.set(to);
		calcIncrements();
	}
}
