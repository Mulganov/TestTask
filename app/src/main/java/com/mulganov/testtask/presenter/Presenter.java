package com.mulganov.testtask.presenter;

import androidx.fragment.app.Fragment;

import com.mulganov.testtask.view.fragments.Fragment_1;
import com.mulganov.testtask.view.fragments.Fragment_2;
import com.mulganov.testtask.view.fragments.Fragment_3;

import java.util.ArrayList;

public class Presenter {
    public static Fragment getFrame(String text) {
        switch (Integer.parseInt(text)){
            case 1: return Fragment_1.newInstance();
            case 2: return Fragment_2.newInstance();
            case 3: return Fragment_3.newInstance();
        }
        return null;
    }

}
