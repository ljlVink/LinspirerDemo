package com.ljlVink.lsphunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.huosoft.wisdomclass.linspirerdemo.R;

import com.ljlVink.lsphunter.adapter.CustomMenuAdapter;
import com.ljlVink.lsphunter.models.MenuItemModel;

import java.util.ArrayList;
import java.util.List;

public class LeftMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);

        ListView listView = view.findViewById(R.id.one_level_menu_list);
        CustomMenuAdapter adapter = new CustomMenuAdapter(requireContext(), getMenuItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> ((MainActivity) requireActivity()).onMenuItemClicked(position));
        listView.setOnItemLongClickListener((adapterView, view12, pos, id) -> {
            ((MainActivity) requireActivity()).onMenuItemLongClicked(pos);
            return true;
        });
        return view;
    }

    private List<MenuItemModel> getMenuItems() {
        List<MenuItemModel> mData = new ArrayList<>();
        mData.add(new MenuItemModel(R.mipmap.icon, "主页"));
        mData.add(new MenuItemModel(R.drawable.backtodesktop, "返回桌面"));
        mData.add(new MenuItemModel(R.drawable.installapps, "应用安装"));
        mData.add(new MenuItemModel(R.drawable.appmanage,"应用管理"));
        mData.add(new MenuItemModel(R.drawable.action_hide, "回领创配置隐藏"));
        mData.add(new MenuItemModel(R.drawable.recycle, "杀进程(长按配置)"));
        mData.add(new MenuItemModel(R.drawable.floatview, "打开悬浮窗"));
        mData.add(new MenuItemModel(R.drawable.huawei, "华为专区"));
        mData.add(new MenuItemModel(R.drawable.lenovo, "联想专区"));
        mData.add(new MenuItemModel(R.drawable.device_settings, "设备设置"));
        mData.add(new MenuItemModel(R.drawable.app_settings, "程序设置"));
        mData.add(new MenuItemModel(R.drawable.activitylauncher_ic_launcher_foreground, "活动启动器"));
        mData.add(new MenuItemModel(R.drawable.help, "帮助"));
        mData.add(new MenuItemModel(R.drawable.ic_baseline_calculate_24, "计算器"));
        mData.add(new MenuItemModel(R.drawable.swordplan, "执剑计划"));
        mData.add(new MenuItemModel(R.drawable.linspirer, "密码计算"));
        mData.add(new MenuItemModel(R.drawable.linspirer, "应用上传(长按配置)"));
        mData.add(new MenuItemModel(R.drawable.linspirer, "三方教育桌面app隐藏"));
        mData.add(new MenuItemModel(R.drawable.tensafe,"T11专区"));
        mData.add(new MenuItemModel(R.drawable.ic_adb,"ADB-OTG"));
        mData.add(new MenuItemModel(R.drawable.about,"关于"));
        return mData;
    }
}
