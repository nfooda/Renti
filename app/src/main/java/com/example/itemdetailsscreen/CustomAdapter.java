package com.example.itemdetailsscreen;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends ArrayAdapter<String> {
    Context context;
    String [] names;
    String [] city;
    String[] price;
    double [] rate;
    int [] rate_no;
    int[] photo;

    public CustomAdapter(Context context, String[] namesList, String[] cityList,String  [] priceList, double[] rateList, int [] rateNoList , int[] photoList) {
        super(context, R.layout.listview_item);
        this.context = context;
        this.names = namesList;
        this.city = cityList;
        this.price = priceList;
        this.rate = rateList;
        this.rate_no=rateNoList;
        this.photo=photoList;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
            viewHolder.name = (TextView) view.findViewById(R.id.textview_name);
            viewHolder.photo = (ImageView) view.findViewById(R.id.imageview_photo);
            viewHolder.city = (TextView) view.findViewById(R.id.textview_city);
            viewHolder.price = (TextView) view.findViewById(R.id.textview_price);
            viewHolder.rate = (TextView) view.findViewById(R.id.textview_rate);
            viewHolder.rateNo = (TextView) view.findViewById(R.id.textview_rateno);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(names[i]);
        viewHolder.price.setText(price[i]);
        viewHolder.city.setText(city[i]);
        viewHolder.rate.setText(String.valueOf(rate[i]));
        viewHolder.rateNo.setText("("+String.valueOf(rate_no[i])+")");
        viewHolder.photo.setImageResource(photo[i]);
        return view;
    }

    static class ViewHolder {
        ImageView photo;
        TextView name;
        TextView city;
        TextView price;
        TextView rate;
        TextView rateNo;
    }
}
