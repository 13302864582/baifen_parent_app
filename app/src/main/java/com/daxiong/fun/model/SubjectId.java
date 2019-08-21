package com.daxiong.fun.model;

public class SubjectId {
	private float max;
	private float min;
	private float original;

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getOriginal() {
		return original;
	}

	public void setOriginal(float original) {
		this.original = original;
	}

	@Override
	public String toString() {
		return "SubjectId [max=" + max + ", min=" + min + ", original="
				+ original + "]";
	}
	
}
