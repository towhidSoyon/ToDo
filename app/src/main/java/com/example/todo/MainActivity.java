package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.example.todo.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogClose{

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private DatabaseHelper databaseHelper=new DatabaseHelper(MainActivity.this);

    private List<ToDoModel> modelList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerViewId);
        addButton=findViewById(R.id.addButtonId);

        modelList=new ArrayList<>();
        adapter=new ToDoAdapter(databaseHelper,MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        modelList=databaseHelper.getAllTasks();
        Collections.reverse(modelList);
        adapter.setTasks(modelList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDClose(DialogInterface dialogInterface) {
        modelList=databaseHelper.getAllTasks();
        Collections.reverse(modelList);
        adapter.setTasks(modelList);
        adapter.notifyDataSetChanged();
    }
}