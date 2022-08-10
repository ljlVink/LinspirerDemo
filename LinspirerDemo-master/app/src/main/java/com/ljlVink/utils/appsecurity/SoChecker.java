package com.ljlVink.utils.appsecurity;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljlVink 2022/2/27
 * */
public class SoChecker {
    Context mContext;
    public SoChecker(Context context){
        this.mContext=context;
    }
    public boolean socheck(){
        List<String> so=readlibs();
        if(so.size()==1){
            return true;
        }
        return false;
    }
    private List<String> readlibs(){
        File file=new File(mContext.getApplicationInfo().nativeLibraryDir);
        File[] files=file.listFiles();
        if (files == null){
        }
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getName());
        }
        return s;
    }

}