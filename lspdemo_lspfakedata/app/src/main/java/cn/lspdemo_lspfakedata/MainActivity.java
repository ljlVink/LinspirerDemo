package cn.lspdemo_lspfakedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cn.lspdemo_lspfakedata.linspirer.LingchuangUtils;
import cn.lspdemo_lspfakedata.tools.AesUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView blacklistview=(ListView)findViewById(R.id.listview_black);
        ListView whitelistview=(ListView)findViewById(R.id.listview_white);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1);
        blacklistview.setAdapter(arrayAdapter);
        whitelistview.setAdapter(arrayAdapter);


    }
}