package com.example.application;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.application.Adapter.ItemListAdapter;
import com.example.application.Database.RoomDB;
import com.example.application.Fragment.ItemTaker;
import com.example.application.Model.Items;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    ItemListAdapter notesListAdapter;
    List<Items> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView search_home;
    Items selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_add = findViewById(R.id.fab_add);
        recyclerView = findViewById(R.id.recycler_home);
        search_home = findViewById(R.id.search_home);
        database =RoomDB.getDatabase(this);
        notes = database.mainDataAccessObject().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ItemTaker.class);
                startActivityForResult(intent , 101);

            }
        });

        search_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }
    private void filter(String newText) {
        List<Items> filteredList = new ArrayList<>();
        for (Items singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                Items new_notes = (Items) data.getSerializableExtra("note");
                database.mainDataAccessObject().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDataAccessObject().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Items new_notes =(Items) data.getSerializableExtra("note");
                database.mainDataAccessObject().update(new_notes.getID(), new_notes.getTitle(),new_notes.getNotes(),new_notes.getRate());
                notes.clear();
                notes.addAll(database.mainDataAccessObject().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Items> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        notesListAdapter = new ItemListAdapter(MainActivity.this,notes,notesClickListener );
        recyclerView.setAdapter(notesListAdapter);

    }

    private final ItemClickListener notesClickListener = new ItemClickListener() {
        @Override
        public void onClick(Items notes) {
            Intent intent = new Intent(MainActivity.this,ItemTaker.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Items notes, CardView cardView) {

            selectedNote = new Items();
            selectedNote = notes;
            showPopup(cardView);

        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }


        @Override
    public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pin:
                    if (selectedNote.isPinned()) {
                        database.mainDataAccessObject().pin(selectedNote.getID(), false);
                        Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();

                    } else {
                        database.mainDataAccessObject().pin(selectedNote.getID(), true);
                        Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
                    }

                    notes.clear();
                    notes.addAll(database.mainDataAccessObject().getAll());
                    notesListAdapter.notifyDataSetChanged();
                    return true;


                case R.id.delete:
                    database.mainDataAccessObject().delete(selectedNote);
                    notes.remove(selectedNote);
                    notesListAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();

                    return true;

                default:
                    return false;
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}