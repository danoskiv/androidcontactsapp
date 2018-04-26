package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Award;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Vladimir on 3/28/2018.
 */

public class AwardsAdapter extends ArrayAdapter<Award> {

    private DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public AwardsAdapter(Context context, List<Award> awards)
    {
        super(context, 0, awards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Award award = getItem(position);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int DATABASE_VERSION = sharedPreferences.getInt("count", 1);
        dbHelper = new DbHelper(getContext(), DATABASE_VERSION);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_award, parent, false);
        }

        TextView awardName = convertView.findViewById(R.id.award_name);
        TextView awardCat = convertView.findViewById(R.id.award_category);
        TextView awardNumber = convertView.findViewById(R.id.award_number);

        String catname = dbHelper.getCategoryByPid(award.getPid());
        awardName.setText(award.getName());
        awardCat.setText(catname);
        awardNumber.setText(String.valueOf(award.getValue()));

        return convertView;
    }
}
