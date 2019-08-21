package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class SegmentedControl extends LinearLayout {  
	  
    @SuppressLint("UseSparseArrays")
	private Map<Integer,IButton> indexButtonMap = new HashMap<Integer, IButton>();  
    private Map<IButton,Integer> buttonIndexMap = new HashMap<IButton, Integer>();  
      
    private int selectedIndex;  
      
    public static final int TAB = 1;  
    public static final int SEGMENT = 2;  
      
    private int currentStyle = SEGMENT;  
      
    private int maxButtonSize;
      
    private LayoutParams  layoutMarginsParams;  
      
    private boolean onlyIsPressed;  
    private OnSegmentChangedListener onSegmentChangedListener;  
      
    public SegmentedControl(Context context, AttributeSet attrs) {  
        super(context,attrs);  
          
        this.setOrientation(HORIZONTAL);
        layoutMarginsParams = (LayoutParams) this.getLayoutParams();
        layoutMarginsParams = new LayoutParams(  
                LinearLayout.LayoutParams.WRAP_CONTENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
        layoutMarginsParams.setMargins(0, 0, 1, 0);  
    }  
      
    public SegmentedControl(Context context,int style) {  
        super(context,null);  
//        this.setOrientation(HORIZONTAL);  
//        currentStyle = style;
//        layoutMarginsParams = (LayoutParams) this.getLayoutParams();
//        
//        layoutMarginsParams = new LayoutParams(  
//                LinearLayout.LayoutParams.WRAP_CONTENT,  
//                LinearLayout.LayoutParams.WRAP_CONTENT);  
//        layoutMarginsParams.setMargins(marginsLeft, 0, 0, 0);  
    }  
      
    public void setStyle(int style) {  
        currentStyle = style;  
    }  
      
	public void setWidth(int width, int height, int num) {
		// int itemWidth = width/num;
		// layoutMarginsParams.width = itemWidth;
		// layoutMarginsParams.height = height;
	}  
      
    public int getButtonCount(){  
        return maxButtonSize;  
    }  
      
    public IButton getButton(int index){  
        return indexButtonMap.get(index);  
    }  
      
    public void setSelectedIndex(int index){  
        if(index <= maxButtonSize){  
            selectedIndex = index;  
            selectButton(index);  
        }  
    }  
      
    public int getSelectedIndex(){  
        return selectedIndex;  
    }  
      
    /** 
     * 废弃 
     * 请使用setOnSegmentChangedListener代替 
     * @param index 
     * @param l 
     */  
    @Deprecated  
    public void bindOnChooseListener(int index, IButton.OnChooseListener l){  
        indexButtonMap.get(index).setOnChooseListener(l);  
    }  
      
    public void clearButton() {  
        this.removeAllViews();  
        maxButtonSize = 0;  
    }  
      
    public IButton newButton(int drawableId, int id){  
        IButton button = new IButton(getContext(), id, IButton.PICTURE);  
//        button.setLayoutParams(layoutMarginsParams);  
  
        button.setBackgroundResource(drawableId);  
          
        postNewButton(button);  
        return button;  
    }  
      
      
    @SuppressLint("ClickableViewAccessibility")
	private void postNewButton(IButton button){  
        this.addView(button);  
        addButtonToMap(button, maxButtonSize);  
        maxButtonSize++;  
        button.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                if (event.getAction() == MotionEvent.ACTION_DOWN) {  
                    selectedIndex = buttonIndexMap.get(v);  
                    selectButton(selectedIndex);  
                }  
                return false;  
            }  
        });  
    }  
    public IButton newButton(String text, int id){  
        IButton button = null;  
        if(currentStyle == TAB){  
            button = new IButton(getContext(), id, IButton.TAB);  
        }else if(currentStyle == SEGMENT){  
            if(maxButtonSize == 0){  
                button = new IButton(getContext(), id);  
            }else{  
                button = new IButton(getContext(), id, IButton.SEGMENT_CENTER);  
            }  

            if(maxButtonSize == 1){  
                getButton(0).changeButtonStyle(IButton.SEGMENT_LEFT);  
                button.changeButtonStyle(IButton.SEGMENT_RIGHT);  
            }  
            
            if(maxButtonSize > 1){  
                    getButton(0).changeButtonStyle(IButton.SEGMENT_LEFT);  
                    getButton(maxButtonSize - 1).changeButtonStyle(IButton.SEGMENT_CENTER);  
                    button.changeButtonStyle(IButton.SEGMENT_RIGHT);  
            }  
              
        }  
        //layoutMarginsParams = new LayoutParams(45, 35);  
        button.setLayoutParams(layoutMarginsParams);  
          
        //button背景色可以在这里设置  
//        button.setPressedColor(Color.rgb(0, 189, 189), Color.rgb(9, 151, 151));  
        button.setPressedColor(Color.rgb(0, 189, 189), Color.rgb(0,189,189)); 
  
        button.setTextSize(15);  
        button.setText(text);  
        postNewButton(button);  
        return button;  
    }  
      
    private void addButtonToMap(IButton button, int index){  
        this.indexButtonMap.put(maxButtonSize, button);  
        this.buttonIndexMap.put(button, maxButtonSize);  
    }  
      
    private void selectButton(int index){  
        //1  
        if(maxButtonSize == 1){  
            IButton button = indexButtonMap.get(0);  
            button.onDefaultUp();  
                if(!onlyIsPressed){  
                    button.onDown();  
                    if(button.hasPressedDrawable()){  
                        button.setPressedDrawable();  
                    }  
                    if(onSegmentChangedListener != null){  
                        onSegmentChangedListener.onSegmentChanged(button.getCmdId());  
                    }  
                    onlyIsPressed = true;  
                }else{  
                    if(button.hasDefaultDrawable()){  
                        button.setDefaultDrawable();  
                    }  
                    button.onUp();  
                    onlyIsPressed = false;  
                }  
        //more  
        }else{  
            for (int i = 0; i < maxButtonSize; i++) {  
                IButton button = indexButtonMap.get(i);  
                if(i == index){  
                    if(button.isNormal()){  
                        button.onDown();  
                        if(button.hasPressedDrawable()){  
                            button.setPressedDrawable();  
                        }  
                        if(onSegmentChangedListener != null){  
                            onSegmentChangedListener.onSegmentChanged(button.getCmdId());  
                        }  
                    }  
                }else{  
                    if(button.hasDefaultDrawable()){  
                        button.setDefaultDrawable();  
                    }  
                    button.onDefaultUp();  
                }  
            }  
        }  
  
    }  
      
    public interface OnSegmentChangedListener{  
        public void onSegmentChanged(int index);  
    }  
      
    public void setOnSegmentChangedListener(OnSegmentChangedListener l){  
        this.onSegmentChangedListener = l;  
    }  
      
}
