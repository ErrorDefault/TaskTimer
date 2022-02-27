package com.example.tasktimer;


import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<TaskDataModel> implements View.OnClickListener{

    private ArrayList<TaskDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtTime;
        Button startButton;
    }

    public CustomAdapter(ArrayList<TaskDataModel> data, Context context) {
        super(context, R.layout.task, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TaskDataModel dataModel=(TaskDataModel)object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.task_time);
            viewHolder.startButton = (Button) convertView.findViewById(R.id.start_button);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtTime.setText(String.format("%02d:%02d:%02d", dataModel.getHours(), dataModel.getMinutes(), dataModel.getSeconds()));
        viewHolder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object object= getItem(position);
                TaskDataModel dataModel=(TaskDataModel)object;
                new CountDownTimer(dataModel.getMilliseconds(), 1000){

                    public void onTick(long millisUntilFinished){
                        long s = millisUntilFinished/1000;
                        viewHolder.txtTime.setText(String.format("%02d:%02d:%02d", s/3600, s/60%60, s%60));
                    }
                    public void onFinish(){
                        viewHolder.txtTime.setText("00:00:00");
                    }
                }.start();
            }
        });
        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
