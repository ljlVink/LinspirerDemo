package com.ljlVink.Activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.VpnService;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huosoft.wisdomclass.linspirerdemo.DataBinderMapperImpl;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.DataUtils;
import com.ljlVink.services.vpnService;

import java.util.ArrayList;
import java.util.Objects;

import activitylauncher.RootDetection;
import activitylauncher.SettingsUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences prefs;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.linspirerdemosettings, rootKey);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().getBaseContext());
        SwitchPreference vpnmode=Objects.requireNonNull(findPreference("vpnmode"));
        DropDownPreference vpn_stop=Objects.requireNonNull(findPreference("vpn_stop"));
        DropDownPreference hide_settings=Objects.requireNonNull(findPreference("hide_settings"));
        DropDownPreference show_settings=Objects.requireNonNull(findPreference("show_settings"));
        DropDownPreference sn_settings=Objects.requireNonNull(findPreference("sn_settings"));
        EditTextPreference password=Objects.requireNonNull(findPreference("password"));
        DropDownPreference clear_password=Objects.requireNonNull(findPreference("clear_password"));
        EditTextPreference password_factory=Objects.requireNonNull(findPreference("password_factory"));
        DropDownPreference clear_password_factory=Objects.requireNonNull(findPreference("clear_password_factory"));
        DropDownPreference get_in_app_with_ime=Objects.requireNonNull(findPreference("get_in_app_with_ime"));
        DropDownPreference get_in_app_with_assist=Objects.requireNonNull(findPreference("get_in_app_with_assist"));
        DropDownPreference emui_control=Objects.requireNonNull(findPreference("emui_control"));
        DropDownPreference desktop_pkg=Objects.requireNonNull(findPreference("desktop_pkg"));
        DropDownPreference miahash_add=Objects.requireNonNull(findPreference("miahash_add"));
        vpnmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean)newValue == true){
                    DataUtils.saveintvalue(getContext(),"vpnmode",1);
                }else{
                    DataUtils.saveintvalue(getContext(),"vpnmode",0);
                    vpnService.stop(getContext());
                }
                return true;
            }
        });
        vpn_stop.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                vpnService.stop(getContext());
                return false;
            }
        });
        hide_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (NewUI.isActiveime(getContext()) && DataUtils.readint(getContext(), "ime") == 1) {
                    getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                } else {
                    Toast.makeText(getContext(), "失败,请成功通过输入法或者语音助手打开后设置", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        show_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                return false;
            }
        });
    sn_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"SN","null"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("请输入sn")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DataUtils.saveStringValue(getContext(),"SN",et.getText().toString());
                        }
                    }).setNegativeButton("取消", null).show();
            return false;
        }
    });
    password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            DataUtils.saveStringValue(getContext(), "password", (String) newValue);
            return false;
        }
    });
    clear_password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            DataUtils.saveStringValue(getContext(), "password", "");
            return false;
        }
    });
    password_factory.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            DataUtils.saveStringValue(getContext(), "factory_password", (String) newValue);
            return false;
        }
    });
    clear_password_factory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            DataUtils.saveStringValue(getContext(), "factory_password", "");
            return false;
        }
    });
    get_in_app_with_ime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent intent111 = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent111);
            return false;
        }
    });
    get_in_app_with_assist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent intent111111 = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
            PackageManager packageManager = getContext().getPackageManager();
            if (intent111111.resolveActivity(packageManager) != null) {
                startActivity(intent111111);
            } else {
                Toast.makeText(getContext(), "这个rom不能设置语音助手", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    });
    emui_control.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String[] items=new String[]{"解控模式","管控模式","自动"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("手动设置管控模式(非必要不要设置)").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0){
                        DataUtils.saveintvalue(getContext(),"emui_control",2);
                    }
                    if(i==1){
                        DataUtils.saveintvalue(getContext(),"emui_control",1);
                    }
                    if(i==2){
                        DataUtils.saveintvalue(getContext(),"emui_control",0);
                    }
                }
            }).show();
            return false;
        }
    });
    desktop_pkg.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"desktop_pkg","com.android.launcher3"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("请输入包名(默认为com.android.launcher3)")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DataUtils.saveStringValue(getContext(),"desktop_pkg",et.getText().toString());
                        }
                    }).setNegativeButton("取消", null).show();
            return false;

        }
    });
    miahash_add.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String[] items=new String[]{"自动","写入白名单尾部加;miahash","写入白名单不添加;miahash"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("手动设置管控模式(非必要不要设置)").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0){
                        DataUtils.saveintvalue(getContext(),"miahash_add",0);
                    }
                    if(i==1){
                        DataUtils.saveintvalue(getContext(),"miahash_add",1);
                    }
                    if(i==2){
                        DataUtils.saveintvalue(getContext(),"miahash_add",2);
                    }
                }
            }).show();

            return false;
        }
    });
    //  SwitchPreference hidePrivate = Objects.requireNonNull(findPreference("hide_private"));
       // SwitchPreference allowRoot = Objects.requireNonNull(findPreference("allow_root"));



       // hidePrivate.setOnPreferenceChangeListener((preference, newValue) -> onHidePrivateUpdated((Boolean) newValue));
     //   allowRoot.setOnPreferenceChangeListener((preference, newValue) -> onAllowRootUpdated((Boolean) newValue));
    }

}
