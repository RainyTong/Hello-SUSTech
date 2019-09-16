package com.example.xuversion.login_activity.anim;

import android.view.animation.LinearInterpolator;

public class JellyInterpolator extends LinearInterpolator {
	private float factor;

	/**
	 * Constructor
	 */
	public JellyInterpolator() {
		this.factor = 0.15f;
	}

	@Override
	public float getInterpolation(float input) {
		return (float) (Math.pow(2, -10 * input)
				* Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
	}
}
