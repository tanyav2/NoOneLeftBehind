package com.example.apple.nooneleftbehind.PersonPackage;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.nooneleftbehind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apple on 5/2/17.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    ArrayList<Person> people;

    public PersonAdapter(ArrayList<Person> people) {
        this.people = people;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View personListItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.person_list_item, parent, false);
        return new ViewHolder(personListItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = people.get(position);

        holder.firstNameView.setText(person.getFirstName());
        holder.lastNameView.setText(person.getLastName());
        Picasso.with(holder.imageView3.getContext()).load(Uri.parse(person.getImgPath())).into(holder.imageView3);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstNameView;
        public TextView lastNameView;
        public ImageView imageView3;

        public ViewHolder(View itemView) {
            super(itemView);
            firstNameView = (TextView) itemView.findViewById(R.id.firstNameView);
            lastNameView = (TextView) itemView.findViewById(R.id.lastNameView);
            imageView3 = (ImageView) itemView.findViewById(R.id.imageView3);
        }
    }
}
