package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SearchResultAdapter;
import com.example.myapplication.model.DatabaseAccess;
import com.example.myapplication.model.Word;

import java.util.List;

public class FragmentSearchFragment extends Fragment {

    ImageButton btnSearch;
    private EditText edttInput;
    private ListView lstResult;
    private SearchResultAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        edttInput = view.findViewById(R.id.editTextInput);
        lstResult = view.findViewById(R.id.list_search_result);
        btnSearch = view.findViewById(R.id.imageButtonSearch);
        final Context ct = this.getContext();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edttInput.getText().toString().toLowerCase();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ct);
                databaseAccess.open();
                List<Word> list = databaseAccess.getWord(input);
                adapter = new SearchResultAdapter(ct, R.layout.search_result_item, list);
                lstResult.setAdapter(adapter);
            }
        });
        return view;
    }
}
