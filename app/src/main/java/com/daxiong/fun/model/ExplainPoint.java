package com.daxiong.fun.model;

/**
 * 坐标类
 * 
 * @author parsonswang
 * 
 */
public class ExplainPoint {

	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	private float x;
	private float y;
	private String text;
	private String audioPath;
	private int subtype;
	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public ExplainPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public ExplainPoint() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audioPath == null) ? 0 : audioPath.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExplainPoint other = (ExplainPoint) obj;
		if (audioPath == null) {
			if (other.audioPath != null)
				return false;
		} else if (!audioPath.equals(other.audioPath))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", text=" + text + ", audioPath=" + audioPath + "]";
	}
}
