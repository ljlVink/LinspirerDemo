/*
 * Original Copyright 2015 Mars Kwok
 * Modified work Copyright (c) 2020, weishu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ljlVink.keepalive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.Activity.NewUI;
import com.ljlVink.Activity.autostart;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class Service2 extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "kill_restart", Toast.LENGTH_SHORT).show();
        //Intent intent1=new Intent(this, autostart.class);
        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent1);
        return Service.START_NOT_STICKY;

    }
}