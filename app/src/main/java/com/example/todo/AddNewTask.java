package com.example.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.todo.model.ToDoModel;
import com.example.todo.utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    static final String TAG= "AddNewTask";

    private EditText editText;
    private Button saveButton;

    private DatabaseHelper db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.add_newtask,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText=view.findViewById(R.id.editTextId);
        saveButton=view.findViewById(R.id.saveButtonId);

        db=new DatabaseHelper(getActivity());

        Boolean isUpdate=false;

        Bundle bundle=getArguments();
        if(bundle!= null){
            isUpdate=true;
            String task = bundle.getString("task");
            editText.setText(task);

            if(task.length() > 0){
                saveButton.setEnabled(false);
            }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }
                else{
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text);
                }
                else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    db.insertTask(item);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity=getActivity();
        if(activity instanceof OnDialogClose){
            ((OnDialogClose)activity).onDClose(dialog);
        }
    }
}
