package com.example.staj_deneme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.staj_deneme.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listGroup;
    private HashMap<String, List<String>> listItem;
    public HashMap<Integer, HashMap<Integer, Boolean>> selectedItems;


    public ExpandableListViewAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listItem) {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
        selectedItems = new HashMap<>();
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }
        TextView textView = convertView.findViewById(R.id.groupTitle);
        textView.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);
        }
        TextView textView = convertView.findViewById(R.id.childText);
        CheckBox checkBox = convertView.findViewById(R.id.childCheckbox);
        textView.setText(childText);

        // Ensure the HashMap for this group exists
        if (!selectedItems.containsKey(groupPosition)) {
            selectedItems.put(groupPosition, new HashMap<>());
        }

        // Get checked state for this item
        boolean isChecked = selectedItems.get(groupPosition).getOrDefault(childPosition, false);

        // Important: Remove the listener before setting checked state to avoid recursive calls
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(isChecked);

        // Set a new listener
        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            if (isChecked1) {
                // If this checkbox is being checked, uncheck all others in the same group
                HashMap<Integer, Boolean> groupSelections = selectedItems.get(groupPosition);

                // First clear all selections for this group
                for (Integer key : groupSelections.keySet()) {
                    if (key != childPosition) {
                        groupSelections.put(key, false);
                    }
                }

                // Then set this item as selected
                groupSelections.put(childPosition, true);
            } else {
                // If unchecking, just update this item
                selectedItems.get(groupPosition).put(childPosition, false);
            }

            // Notify adapter to refresh all views
            notifyDataSetChanged();
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
