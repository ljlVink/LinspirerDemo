package adbotg;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


public class UsbReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
        if (intent!=null && (action = intent.getAction()) !=null && action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
            Intent intent1 = new Intent("htetznaing.usb.permission");
            intent1.putExtra("device",(Parcelable) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));
            context.sendBroadcast(intent1);
        }
    }
}
