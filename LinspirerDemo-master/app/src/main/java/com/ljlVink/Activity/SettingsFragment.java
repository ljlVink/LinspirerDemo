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

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huosoft.wisdomclass.linspirerdemo.DataBinderMapperImpl;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.ToastUtils.Toast;
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
        SwitchPreference appstore_internet=Objects.requireNonNull(findPreference("appstore_internet"));
        Preference vpn_stop=Objects.requireNonNull(findPreference("vpn_stop"));
        Preference hide_settings=Objects.requireNonNull(findPreference("hide_settings"));
        Preference show_settings=Objects.requireNonNull(findPreference("show_settings"));
        Preference sn_settings=Objects.requireNonNull(findPreference("sn_settings"));
        EditTextPreference password=Objects.requireNonNull(findPreference("password"));
        Preference clear_password=Objects.requireNonNull(findPreference("clear_password"));
        EditTextPreference password_factory=Objects.requireNonNull(findPreference("password_factory"));
        Preference clear_password_factory=Objects.requireNonNull(findPreference("clear_password_factory"));
        Preference get_in_app_with_ime=Objects.requireNonNull(findPreference("get_in_app_with_ime"));
        Preference get_in_app_with_assist=Objects.requireNonNull(findPreference("get_in_app_with_assist"));
        Preference emui_control=Objects.requireNonNull(findPreference("emui_control"));
        Preference desktop_pkg=Objects.requireNonNull(findPreference("desktop_pkg"));
        Preference miahash_add=Objects.requireNonNull(findPreference("miahash_add"));
        SwitchPreference bjszmode=Objects.requireNonNull(findPreference("bjsz_mode"));

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
        appstore_internet.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean) newValue==true){
                    DataUtils.saveintvalue(getContext(),"disallow_appstore_internet",1);
                }else {
                    DataUtils.saveintvalue(getContext(),"disallow_appstore_internet",0);

                }
                return true;
            }
            public void reload(){
                vpnService.reload("",getContext());
            }
        });
        vpn_stop.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                vpnService.stop(getContext());
                return true;
            }
        });
        hide_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (NewUI.isActiveime(getContext()) && DataUtils.readint(getContext(), "ime") == 1) {
                    getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                } else {
                    Toast.ShowErr(getContext(), "??????,?????????????????????????????????????????????????????????");
                }
                return true;
            }
        });
        show_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                return true;
            }
        });
    sn_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"SN","null"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("?????????sn")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DataUtils.saveStringValue(getContext(),"SN",et.getText().toString());
                        }
                    }).setNegativeButton("??????", null).show();
            return true;
        }
    });
    password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            DataUtils.saveStringValue(getContext(), "password", (String) newValue);
            return true;
        }
    });
    clear_password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            DataUtils.saveStringValue(getContext(), "password", "");
            return true;
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
            return true;
        }
    });
    get_in_app_with_ime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent intent111 = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent111);
            return true;
        }
    });
    get_in_app_with_assist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent intent111111 = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
            PackageManager packageManager = getContext().getPackageManager();
            if (intent111111.resolveActivity(packageManager) != null) {
                startActivity(intent111111);
            }
            return true;
        }
    });
    emui_control.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String[] items=new String[]{"????????????","????????????","??????"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("????????????????????????(?????????????????????)").setItems(items, new DialogInterface.OnClickListener() {
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
            return true;
        }
    });
    desktop_pkg.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"desktop_pkg","com.android.launcher3"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("???????????????(?????????com.android.launcher3)")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DataUtils.saveStringValue(getContext(),"desktop_pkg",et.getText().toString());
                        }
                    }).setNegativeButton("??????", null).show();
            return true;

        }
    });
    miahash_add.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String[] items=new String[]{"??????","????????????????????????;miahash","????????????????????????;miahash"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("????????????????????????(?????????????????????)").setItems(items, new DialogInterface.OnClickListener() {
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

            return true;
        }
    });
        bjszmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean)newValue==true){
                    DataUtils.saveintvalue(getContext(),"bjsz_mode",1);
                    ArrayList<String> pkgname=AppManageActivity.FindLspDemoPkgName(getContext(),"assistlauncher");
                    if(pkgname.size()>0){
                        Toast.ShowInfo(getContext(),"????????????????????????"+pkgname.get(0)+",??????????????????????????????");
                        DataUtils.saveStringValue(getContext(),"desktop_pkg",pkgname.get(0));
                    }
                    else{
                        Toast.ShowWarn(getContext(),"?????????Assist Launcher");
                        DataUtils.saveStringValue(getContext(),"desktop_pkg","");
                    }
                }else{
                    Toast.ShowInfo(getContext(),"?????????????????????");
                    DataUtils.saveStringValue(getContext(),"desktop_pkg","");
                }
                return true;
            }
        });
    //  SwitchPreference hidePrivate = Objects.requireNonNull(findPreference("hide_private"));
       // SwitchPreference allowRoot = Objects.requireNonNull(findPreference("allow_root"));



       // hidePrivate.setOnPreferenceChangeListener((preference, newValue) -> onHidePrivateUpdated((Boolean) newValue));
     //   allowRoot.setOnPreferenceChangeListener((preference, newValue) -> onAllowRootUpdated((Boolean) newValue));
    }

}
