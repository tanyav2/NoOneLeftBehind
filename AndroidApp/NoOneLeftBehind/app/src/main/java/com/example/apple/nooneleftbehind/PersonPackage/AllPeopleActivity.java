package com.example.apple.nooneleftbehind.PersonPackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.apple.nooneleftbehind.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPeopleActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private RecyclerView mRecylerView;
    private ArrayList<Person> mPeople = new ArrayList<>();
    private PersonAdapter mPersonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("People");
        mRecylerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mPersonAdapter = new PersonAdapter(mPeople);
        mRecylerView.setAdapter(mPersonAdapter);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mPeople.add(snapshot.getValue(Person.class));
                }
                mPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
