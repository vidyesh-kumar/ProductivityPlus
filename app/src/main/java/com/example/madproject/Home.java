package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements RecycleListener {
    ImageView profile,pomo,task;

    TextView userid;

    RecyclerView recyclerView;
    PreferenceManager preferenceManager;

    TextView remaining,completed,deleted,perc;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUI();
        profile.setOnClickListener(view -> setActivity(Profile.class));
        pomo.setOnClickListener(view -> setActivity(Pomodoro.class));
        task.setOnClickListener(view->setActivity(AllTasks.class));
    }

    private void getUI()
    {   preferenceManager = PreferenceManager.getInstance(this);
        profile = findViewById(R.id.profile_id);
        pomo = findViewById(R.id.pomodoro_button);
        recyclerView=findViewById(R.id.categories_layout);
        userid = findViewById(R.id.nameuser);
        task = findViewById(R.id.task_button);
        remaining = findViewById(R.id.remaining);
        completed = findViewById(R.id.completed);
        deleted = findViewById(R.id.deleted);
        perc = findViewById(R.id.perc);
        pb = findViewById(R.id.pb);
        String welcome ="Hello,\n"+preferenceManager.getString("Username");
        userid.setText(welcome);
        Gson gson = new Gson();
        String catJson = preferenceManager.getString("Categories");
        ArrayList <Category> categories = gson.fromJson(catJson,new TypeToken<ArrayList<Category>>(){}.getType());
        String taskJson = preferenceManager.getString("Tasks");
        ArrayList <Category> allTasks = gson.fromJson(taskJson,new TypeToken<ArrayList<Category>>(){}.getType());
        remaining.setText(""+allTasks.size());
        int comple = Integer.parseInt(preferenceManager.getString("Completed"));
        completed.setText(""+comple);
        int dele = Integer.parseInt(preferenceManager.getString("Deleted"));
        deleted.setText(""+dele);
        if(comple+dele==0) {
            String perce="N/A";
            perc.setText(perce);
            pb.setProgress(0);
        }
        else
        {   int perce =(comple*100/(comple+dele));
            perc.setText(""+perce+" %");
            pb.setProgress(perce);
        }

        RecyclerAdapter adapter=new RecyclerAdapter(categories,this,this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecorator itemDecoration = new ItemOffsetDecorator(getApplicationContext(),R.dimen.gap);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        byte[] b = Base64.decode(preferenceManager.getString("image_data"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profile.setImageBitmap(bitmap);

    }
    protected void setActivity(Class ctx)
    {   Intent i = new Intent(getApplicationContext(),ctx);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemClicked(Category c) {
        Intent i = new Intent(getApplicationContext(),CategoryView.class);
        Bundle b = new Bundle();
        b.putString("Header",c.getTitle());
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}