package com.mulganov.testtask.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.mulganov.testtask.R;
import com.mulganov.testtask.dp.DB;
import com.mulganov.testtask.dp.Element;
import com.mulganov.testtask.tools.BoxAdapter;
import com.mulganov.testtask.tools.Product;

import org.apache.commons.io.FileUtils;
import org.geonames.FeatureClass;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fragment_3 extends Fragment{

    private static ArrayList<String> list = new ArrayList<>();

    public static Fragment_3 newInstance() {
        return new Fragment_3();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.button).setOnTouchListener(this::onClick);

    }

    void GetUrl(String uurl, String file) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ((TextView)getActivity().findViewById(R.id.status)).setText("Begin...");
        try {
            ((TextView)getActivity().findViewById(R.id.status)).setText("Получаем ссылку...");
            URL url = new URL(uurl);
            ((TextView)getActivity().findViewById(R.id.status)).setText("Получили ссылку\n(открывание потока для чтение файла)...");
            bis = new BufferedInputStream(url.openStream());
            ((TextView)getActivity().findViewById(R.id.status)).setText("Открыт поток для чтения...");
            bos = new BufferedOutputStream(new FileOutputStream(new File(file)));
            ((TextView)getActivity().findViewById(R.id.status)).setText("Открыт поток для записи...");

            int c;
            ((TextView)getActivity().findViewById(R.id.status)).setText("Началось чтение файла...");
            while ((c = bis.read()) != -1) {
                bos.write(c);
            }

            ((TextView)getActivity().findViewById(R.id.status)).setText("Чтение завершенно...");
            System.out.println(uurl + " OK");

            bos.close();
            bis.close();
        } catch (Exception e) {
            System.out.println(uurl + " NOT");
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException ignore) {
            }
        }
    }

    private boolean onClick(View view, MotionEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetUrl( ((EditText)getActivity().findViewById(R.id.url)).getText() + "" , getActivity().getCacheDir() + "/temp.zip");

//                File file = new File(getActivity().getCacheDir() + "/temp.zip");
//
//                try {
//                    FileUtils.copyURLToFile(
//                            new URL(((EditText)getActivity().findViewById(R.id.url)).getText() + ""),
//                            file
//                    );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try(ZipInputStream zin = new ZipInputStream(new FileInputStream(getActivity().getCacheDir() + "/temp.zip")))
                {
                    ZipEntry entry;
                    String name;
                    long size;

                    while((entry=zin.getNextEntry())!=null){

                        name = entry.getName(); // получим название файла
                        size=entry.getSize();  // получим его размер в байтах

                        ((TextView)getActivity().findViewById(R.id.status)).setText("Был найден файл " + name + "...");

                        // распаковка
                        File f = new File(getActivity().getCacheDir() + "/temp");
                        f.mkdir();

                        FileOutputStream fout = new FileOutputStream(getActivity().getCacheDir() + "/temp" + File.separator + name);

                        ((TextView)getActivity().findViewById(R.id.status)).setText("Происходит записать в файл " + name + "...");

                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        ((TextView)getActivity().findViewById(R.id.status)).setText("Файл " + name + " Был успешно записан...");

                        fout.flush();
                        zin.closeEntry();
                        fout.close();
                    }
                }
                catch(Exception ex){
                    return;
                }

                String[] a = new File(getActivity().getCacheDir() + "/temp").list();

                ArrayList<Bitmap> array = new ArrayList<>();
                ArrayList<Product> products = new ArrayList<>();


                ((TextView)getActivity().findViewById(R.id.status)).setText("Генерация bitmap для всех файлов(начало)...");

                for (String s: a){
                    Bitmap b = BitmapFactory.decodeFile(getActivity().getCacheDir() + "/temp/" + s);
                    array.add(b);
                    products.add(new Product(b));
                }


                ((TextView)getActivity().findViewById(R.id.status)).setText("Генерация bitmap для всех файлов(конец)...");


                ((TextView)getActivity().findViewById(R.id.status)).setText("Создание адаптера...");

                BoxAdapter boxAdapter = new BoxAdapter(getContext(), products);


                ((TextView)getActivity().findViewById(R.id.status)).setText("Адапетер создан...");

                ListView lvMain = (ListView) getActivity().findViewById(R.id.list_image);

                ((TextView)getActivity().findViewById(R.id.status)).setText("Применение адаптера...");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvMain.setAdapter(boxAdapter);

                        ((TextView)getActivity().findViewById(R.id.status)).setText("Адаптер успешно создан и применен");
                    }
                });
            }
        }).start();




        return false;
    }

}
