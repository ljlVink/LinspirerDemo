package com.ljlVink.lsphunter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.models.MenuItemModel;

import java.util.List;

public class CustomMenuAdapter extends ArrayAdapter<MenuItemModel> {
    private Context context;

    public CustomMenuAdapter(Context context, List<MenuItemModel> menuItems) {
        super(context, 0, menuItems);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MenuItemModel menuItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item_layout, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.item_icon);
        TextView textTextView = convertView.findViewById(R.id.item_text);

        if (menuItem != null) {
            iconImageView.setImageResource(menuItem.getIconResource());
            textTextView.setText(menuItem.getText());
        }
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,130);//设置宽度和高度
        convertView.setLayoutParams(params);
        return convertView;
    }
}
