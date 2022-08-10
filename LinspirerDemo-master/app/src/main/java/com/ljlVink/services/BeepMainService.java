package com.ljlVink.services;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.Activity.NewUI;
import com.ljlVink.utils.DataUtils;

public class BeepMainService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    @Override
    public View onCreateInputView() {
        KeyboardView viewa = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard kb = new Keyboard(this, R.xml.keyboard);
        viewa.setKeyboard(kb);
        viewa.setOnKeyboardActionListener(this);
        return viewa;
    }
    @Override
    public void onKey(int code, int[] keyCodes) {
        InputConnection connect = getCurrentInputConnection();
        if (connect != null) {
            DataUtils.saveintvalue(getApplicationContext(),"ime",1);
            if(code ==1 ){
                Intent i = new Intent(this, NewUI.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            if(code == 2){
                getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(),"com.ljlVink.Activity.PreMainActivity"),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
            }
            if (code ==3 ){
                PackageManager p = getPackageManager();
                p.setComponentEnabledSetting(new ComponentName(getPackageName(),"com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }
    @Override
    public void onPress(int i) {}
    @Override
    public void onRelease(int i) { }
    @Override
    public void onText(CharSequence charSequence) { }
    @Override
    public void swipeLeft() { }
    @Override
    public void swipeRight() { }
    @Override
    public void swipeDown() { }
    @Override
    public void swipeUp() {}
}