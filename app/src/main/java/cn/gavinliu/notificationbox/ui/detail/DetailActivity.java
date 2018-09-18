package cn.gavinliu.notificationbox.ui.detail;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.gavinliu.notificationbox.R;

/**
 * Created by Gavin on 2016/10/11.
 *
 */

// detailActivity即点开应用就看到的明细列表界面
public class DetailActivity extends AppCompatActivity {
   public String appName="";
   public String packageName="";

    public    Button SaveMessageBlackList;
    public EditText EditMessageBlackList;
    public EditText EditMessageBlackList2;
    public EditText EditMessageBlackList1;
    public ImageButton ImageButtonSave;

    public EditText Edit1;
    public EditText Edit2;
    public ImageButton ImageButtonQuery;
    public  ImageButton ImageButtonQuery2;
    public  ImageButton ImageButtonSave2;

    View ViewMessageBlackList;
    View ViewMessageQuery;

    String query1="";
    String query2="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);

        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_applist);

        if (detailFragment == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            appName=bundle.getString("appName");
            packageName=bundle.getString("packageName");

            detailFragment = DetailFragment.newInstance( appName,packageName);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, detailFragment)
                    .commit();
        }
        new DetailPresenter(detailFragment,query1,query2);
     //   new DetailPresenter(detailFragment);

        ViewMessageQuery=(View)findViewById(R.id.viewMessageQurey);
        ViewMessageBlackList=(View)findViewById(R.id.viewMessageBlackList);

        Edit1=(EditText)findViewById(R.id.editMessageQuery1);
        Edit2=(EditText)findViewById(R.id.editMessageQuery2);
        ImageButtonQuery=(ImageButton)findViewById(R.id.imageButtonQuery);

        ImageButtonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query1=Edit1.getText().toString();
                query2=Edit2.getText().toString();
                reload();
                Toast.makeText(DetailActivity.this,"Finished",Toast.LENGTH_SHORT).show();
                query1="";
                query2="";
            }
        });

        EditMessageBlackList=(EditText)findViewById(R.id.editMessageBlackList);
        EditMessageBlackList1=(EditText)findViewById(R.id.editMessageBlackList1);
        EditMessageBlackList2=(EditText)findViewById(R.id.editMessageBlackList2);

        ImageButtonSave=(ImageButton)findViewById(R.id.imageButtonSave);

        getSetting();

        ImageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putSetting();
                Toast.makeText(DetailActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            }
        });

        ImageButtonQuery2=(ImageButton)findViewById(R.id.imageButtonQuery2);
        ImageButtonSave2=(ImageButton)findViewById(R.id.imageButtonSave2);

        ImageButtonQuery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewMessageBlackList.setVisibility(View.GONE);
                ViewMessageQuery.setVisibility(View.VISIBLE);
            }
        });

        ImageButtonSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewMessageBlackList.setVisibility(View.VISIBLE);
                ViewMessageQuery.setVisibility(View.GONE);
            }
        });

        if(packageName.length()<4){
            ViewMessageBlackList.setVisibility(View.GONE);
            ViewMessageQuery.setVisibility(View.VISIBLE);
                ImageButtonSave2.setVisibility(View.GONE);
        }



    }


    public  void getSetting() {
        try{
            String MessageBlackList;
            SharedPreferences read = getSharedPreferences("setting",MODE_MULTI_PROCESS);
            MessageBlackList = read.getString(packageName, "");
            EditMessageBlackList.setText(MessageBlackList);

            MessageBlackList = read.getString(packageName+".1", "");
            EditMessageBlackList1.setText(MessageBlackList);

            MessageBlackList = read.getString(packageName+".2", "");
            EditMessageBlackList2.setText(MessageBlackList);

            }catch(Exception e) {
                Log.i(packageName,"error");
                e.printStackTrace();

            }
    }

    public void putSetting(){
  //      SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        String MessageBlackList;
        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_MULTI_PROCESS).edit();
        MessageBlackList=EditMessageBlackList.getText().toString();
        editor.putString(packageName, MessageBlackList);

        MessageBlackList=EditMessageBlackList1.getText().toString();
        editor.putString(packageName+".1", MessageBlackList);

        MessageBlackList=EditMessageBlackList2.getText().toString();
        editor.putString(packageName+".2", MessageBlackList);

        editor.commit();
        getSetting();
    }

    public void reload(){
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_applist);
        detailFragment = DetailFragment.newInstance( appName,packageName);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, detailFragment)
                    .commit();
        Log.w("query1"+query1,"query2"+query2);
        new DetailPresenter(detailFragment,query1,query2);

    }
}
