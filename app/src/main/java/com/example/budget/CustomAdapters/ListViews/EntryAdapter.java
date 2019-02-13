package com.example.budget.CustomAdapters.ListViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budget.Databases.AppDb.Entities.Entry;
import com.example.budget.EntryDetailsActivity;
import com.example.budget.R;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<Entry> implements View.OnClickListener {

    private ArrayList<Entry> dataSet;
    private Context mContext;
    private static class ViewHolder{
        TextView txtDescription;
        TextView txtAmount;
    }
    public EntryAdapter(ArrayList<Entry> dataSet, Context context){
        super(context, R.layout.view_entries_entry_row_item,dataSet);
        this.dataSet = dataSet;
        this.mContext = context;

    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this.mContext, EntryDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EntryId",v.getTag().toString());
        mContext.startActivity(intent);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Entry dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_entries_entry_row_item,parent,false);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.view_entries_entry_row_item_amount);
            viewHolder.txtAmount.setTag(dataModel.id);
            viewHolder.txtAmount.setOnClickListener(this);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.view_entries_entry_row_item_description);
            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;

        String typeSymbol = "";
        switch(dataModel.type){
            case "in":
                typeSymbol = "+";
                break;
            case "out":
                typeSymbol="-";
                break;
            default:
        }
        viewHolder.txtAmount.setText(typeSymbol + String.valueOf(dataModel.amount));
        viewHolder.txtDescription.setText(dataModel.description);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EntryDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EntryId",dataModel.id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
