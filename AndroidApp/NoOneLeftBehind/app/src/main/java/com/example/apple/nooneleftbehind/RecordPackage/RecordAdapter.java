package com.example.apple.nooneleftbehind.RecordPackage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.nooneleftbehind.R;

import java.util.ArrayList;

/**
 * Created by apple on 5/2/17.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    ArrayList<CountRecord> recordsList;

    public RecordAdapter(ArrayList<CountRecord> recordsList) {
        this.recordsList = recordsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View recordListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new ViewHolder(recordListItem);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        CountRecord countRecord = recordsList.get(position);
        holder.dateTimeView.setText(countRecord.getDateAndTimeString());
        holder.countView.setText(Integer.toString(countRecord.getCount()));
        holder.peoplePresentView.setText(countRecord.getPeoplePresentString());
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTimeView;
        public TextView countView;
        public TextView peoplePresentView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTimeView = (TextView) itemView.findViewById(R.id.dateTimeView);
            countView = (TextView) itemView.findViewById(R.id.countView);
            peoplePresentView = (TextView) itemView.findViewById(R.id.peoplePresentView);
        }
    }
}
