package com.ljlVink.lsphunter.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.services.vpnService;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
        SwitchPreference no_hmscore_hms=Objects.requireNonNull(findPreference("no_hmscore_hms"));
        Preference miahash_add=Objects.requireNonNull(findPreference("miahash_add"));
        SwitchPreference bjszmode=Objects.requireNonNull(findPreference("bjsz_mode"));
        SwitchPreference firewall_ctrl=Objects.requireNonNull(findPreference("firewall_ctrl"));
        SwitchPreference bjsz_mode_disable_recent=Objects.requireNonNull(findPreference("bjsz_mode_disable_recent"));
        SwitchPreference pulldown_app_until_volume_press=Objects.requireNonNull(findPreference("pulldown_app_until_volume_press"));
        vpnmode.setOnPreferenceChangeListener((preference, newValue) -> {
            if((Boolean) newValue){
                DataUtils.saveintvalue(getContext(),"vpnmode",1);
            }else{
                DataUtils.saveintvalue(getContext(),"vpnmode",0);
                vpnService.stop(getContext());
            }
            return true;
        });
        appstore_internet.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean) newValue){
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
        vpn_stop.setOnPreferenceClickListener(preference -> {
            vpnService.stop(getContext());
            return true;
        });
        hide_settings.setOnPreferenceClickListener(preference -> {
            if (Sysutils.isActiveime(getContext()) && DataUtils.readint(getContext(), "ime") == 1) {
                getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.lsphunter.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            } else {
                Toast.ShowErr(getContext(), "失败,请成功通过输入法或者语音助手打开后设置");
            }
            return true;
        });
        show_settings.setOnPreferenceClickListener(preference -> {
            getContext().getPackageManager().setComponentEnabledSetting(new ComponentName(getContext().getPackageName(), "com.ljlVink.lsphunter.Activity.PreMainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            return true;
        });
        sn_settings.setOnPreferenceClickListener(preference -> {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"SN","null"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("请输入sn")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("确定", (dialogInterface, i) -> DataUtils.saveStringValue(getContext(),"SN",et.getText().toString())).setNegativeButton("取消", null).show();
            return true;
        });
        password.setOnPreferenceChangeListener((preference, newValue) -> {
            DataUtils.saveStringValue(getContext(), "password", (String) newValue);
            return true;
        });
        clear_password.setOnPreferenceClickListener(preference -> {
            DataUtils.saveStringValue(getContext(), "password", "");
            return true;
        });
        password_factory.setOnPreferenceChangeListener((preference, newValue) -> {
            DataUtils.saveStringValue(getContext(), "factory_password", (String) newValue);
            return false;
        });
        clear_password_factory.setOnPreferenceClickListener(preference -> {
            DataUtils.saveStringValue(getContext(), "factory_password", "");
            return true;
        });
        get_in_app_with_ime.setOnPreferenceClickListener(preference -> {
            Intent intent111 = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent111);
            return true;
        });
        get_in_app_with_assist.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
            PackageManager packageManager = getContext().getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            }
            return true;
        });
        emui_control.setOnPreferenceClickListener(preference -> {
            String[] items=new String[]{"解控模式","管控模式","自动"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("手动设置管控模式(非必要不要设置)").setItems(items, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        DataUtils.saveintvalue(getContext(), "emui_control", 2);
                        break;
                    case 1:
                        DataUtils.saveintvalue(getContext(), "emui_control", 1);
                        break;
                    case 2:
                        DataUtils.saveintvalue(getContext(), "emui_control", 0);
                        break;
                }
            }).show();
            return true;
        });
        desktop_pkg.setOnPreferenceClickListener(preference -> {
            final EditText et = new EditText(getContext());
            et.setText(DataUtils.readStringValue(getContext(),"desktop_pkg","com.android.launcher3"));
            new MaterialAlertDialogBuilder(getContext()).setTitle("请输入包名(默认为com.android.launcher3)")
                    .setIcon(R.drawable.installapps)
                    .setView(et)
                    .setPositiveButton("确定", (dialogInterface, i) -> DataUtils.saveStringValue(getContext(),"desktop_pkg",et.getText().toString())).setNegativeButton("取消", null).show();
            return true;

        });
        miahash_add.setOnPreferenceClickListener(preference -> {
            String[] items=new String[]{"自动","写入白名单尾部加;miahash","写入白名单不添加;miahash"};
            new MaterialAlertDialogBuilder(getContext()).setIcon(R.drawable.settings).setTitle("设置白名单miahash写入模式").setItems(items, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        DataUtils.saveintvalue(getContext(), "miahash_add", 0);
                        break;
                    case 1:
                        DataUtils.saveintvalue(getContext(), "miahash_add", 1);
                        break;
                    case 2:
                        DataUtils.saveintvalue(getContext(), "miahash_add", 2);
                        break;
                }
            }).show();
            return true;
        });
        bjszmode.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean) newValue){
                DataUtils.saveintvalue(getContext(),"bjsz_mode",1);
                ArrayList<String> pkgname=Sysutils.FindLspDemoPkgName(getContext(),"assistlauncher");
                if(pkgname.size()>0){
                    Toast.ShowInfo(getContext(),"已修改管控包名为"+pkgname.get(0)+",请不要再修改管控包名");
                    DataUtils.saveStringValue(getContext(),"desktop_pkg",pkgname.get(0));
                }
                else{
                    Toast.ShowWarn(getContext(),"请安装Assist Launcher");
                    DataUtils.saveStringValue(getContext(),"desktop_pkg","");
                }
            }else{
                Toast.ShowInfo(getContext(),"已清空管控包名");
                DataUtils.saveStringValue(getContext(),"desktop_pkg","");
            }
            return true;
        });
        firewall_ctrl.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean) newValue){
                DataUtils.saveintvalue(getContext(),"no_firewall_ctrl",1);
            }else{
                DataUtils.saveintvalue(getContext(),"no_firewall_ctrl",0);
            }
            return true;
        });
        bjsz_mode_disable_recent.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean) newValue){
                DataUtils.saveintvalue(getContext(),"bjsz_mode_disable_recent",1);
            }else{
                DataUtils.saveintvalue(getContext(),"bjsz_mode_disable_recent",0);
            }
            return true;
        });
        no_hmscore_hms.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean) newValue){
                DataUtils.saveintvalue(getContext(),"not_load_hmscore",1);
            }else{
                DataUtils.saveintvalue(getContext(),"not_load_hmscore",0);
            }
            return true;
        });
        pulldown_app_until_volume_press.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean) newValue){
                DataUtils.saveintvalue(getContext(),"pulldown_app_until_volume_press",1);
            }else{
                DataUtils.saveintvalue(getContext(),"pulldown_app_until_volume_press",0);
            }
            return true;
        });
    }

}
