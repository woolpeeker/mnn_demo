package com.example.mnn_mnist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.io.File;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView textView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    native String mnist(Object bitmap, String str);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.process).setOnClickListener(this);
        findViewById(R.id.gray).setOnClickListener(this);
        textView = findViewById((R.id.textView));
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.show){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            imageView.setImageBitmap(bitmap);
            copyAssetAndWrite("mnist.mnn");
            File dataFile = new File(getCacheDir(), "mnist.mnn");
            textView.setText(mnist(bitmap, dataFile.getAbsolutePath()));
        }
        else if(v.getId() == R.id.gray){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            // getGray(bitmap);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            // getEdge(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    private boolean copyAssetAndWrite(String fileName){
        try {
            File cacheDir=getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return false;
                }
            }else {
                if (outFile.length()>10){//表示已经写入一次
                    return true;
                }
            }
            InputStream is=getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}