package com.mulganov.testtask.view.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mulganov.testtask.R;
import com.mulganov.testtask.dp.DB;
import com.mulganov.testtask.dp.Element;
import com.mulganov.testtask.dp.ElementDoa;

import org.geonames.FeatureClass;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import java.util.ArrayList;

public class Fragment_1 extends Fragment{

    private static ArrayList<String> list = new ArrayList<>();

    public static Fragment_1 newInstance() {
        return new Fragment_1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_content, Fragment_1.newInstance())
//                .commitNow();

        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DB db = Room.databaseBuilder(getActivity().getApplicationContext(),
                        DB.class, "populus-database").build();

                Fragment_1.list = new ArrayList<>();

                for (Element e: db.getElementDoa().getAllElement()){
                    Fragment_1.list.add(e.getText());
                }

                ListView list = getActivity().findViewById(R.id.list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, Fragment_1.list);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(adapter);
                    }
                });

                db.close();
            }
        }).start();

        EditText edit = ((EditText)getActivity().findViewById(R.id.city_name));

        ListView list_text = getActivity().findViewById(R.id.list_text);

        list_text.setOnItemClickListener(this::onTouch);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //ArrayList<String> a = Presenter.city_text(s + "");

                if ((s + "").equalsIgnoreCase("enter")) return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WebService.setUserName("mulganov2001");


                        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
                        searchCriteria.setName(s + "");
                        searchCriteria.setNameEquals(s + "");
                        searchCriteria.setStyle(Style.FULL);
                        searchCriteria.setLanguage("RU");

                        ToponymSearchResult searchResult;

                        ArrayList<String> a = new ArrayList<String>();

                        try {
                            searchResult = WebService.search(searchCriteria);
                            // searchResult = WebService.search(searchCriteria);

                            for (Toponym toponym : searchResult.getToponyms()) {
                                if(toponym.getFeatureClass().equals(FeatureClass.P)){
                                    String str = toponym.getTimezone().getGmtOffset() + "";
                                    if (toponym.getTimezone().getGmtOffset() > 0) str = "+" + toponym.getTimezone().getGmtOffset();
                                    a.add((toponym.getName() + " " +  str));
                                    //a.add((toponym.getName() + " " +  toponym.getTimezone().getTimezoneId()));
                                }

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1, a);

                            ListView list = getActivity().findViewById(R.id.list_text);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    list.setAdapter(adapter);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void hideKeyboard() {
        System.out.println("hide keyboard");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }
    private void onTouch(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView)getActivity().findViewById(R.id.city_name)).setText( ((TextView)view).getText() );

        hideKeyboard();

        DB db = Room.databaseBuilder(getActivity().getApplicationContext(),
                DB.class, "populus-database").build();

        Element e = new Element();
        e.text = ((TextView)view).getText() + "";

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = true;
                for (Element e: db.getElementDoa().getAllElement()){
                    if (e.getText().equalsIgnoreCase(((TextView)view).getText() + "")){
                        run = false;
                    }
                }
                if (run){
                    db.getElementDoa().insertAll(e);
                    ListView list = getActivity().findViewById(R.id.list);

                    Fragment_1.list.add(((TextView)view).getText() + "");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, Fragment_1.list);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.setAdapter(adapter);
                        }
                    });
                }
                System.out.println(db.getElementDoa().getAllElement().toString());
            }
        }).start();

        db.close();
    }

}
