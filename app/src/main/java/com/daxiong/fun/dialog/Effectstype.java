package com.daxiong.fun.dialog;

import com.daxiong.fun.dialog.effects.BaseEffects;
import com.daxiong.fun.dialog.effects.FadeIn;
import com.daxiong.fun.dialog.effects.Fall;
import com.daxiong.fun.dialog.effects.FlipH;
import com.daxiong.fun.dialog.effects.FlipV;
import com.daxiong.fun.dialog.effects.NewsPaper;
import com.daxiong.fun.dialog.effects.RotateBottom;
import com.daxiong.fun.dialog.effects.RotateLeft;
import com.daxiong.fun.dialog.effects.Shake;
import com.daxiong.fun.dialog.effects.SideFall;
import com.daxiong.fun.dialog.effects.SlideBottom;
import com.daxiong.fun.dialog.effects.SlideLeft;
import com.daxiong.fun.dialog.effects.SlideRight;
import com.daxiong.fun.dialog.effects.SlideTop;
import com.daxiong.fun.dialog.effects.Slit;

public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
