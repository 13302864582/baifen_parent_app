package com.daxiong.fun.model;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author parsonswang
 * 
 */
public class ImageItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
