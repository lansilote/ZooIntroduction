package com.example.zoointroduction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlantDetailFragment extends Fragment {

    private String E_Name;

    public static PlantDetailFragment newInstance(String eName, String fName, String fEnName, byte[] pic, String alsoKnown,
                                                  String brief, String feature, String fa, String update) {
        PlantDetailFragment plantDetail = new PlantDetailFragment();
        Bundle args = new Bundle();
        args.putString(Params.E_Name, eName);
        args.putString(Params.F_Name_Ch, fName);
        args.putString(Params.F_Name_En, fEnName);
        args.putByteArray(Params.F_Pic, pic);
        args.putString(Params.F_AlsoKnown, alsoKnown);
        args.putString(Params.F_Brief, brief);
        args.putString(Params.F_Feature, feature);
        args.putString(Params.F_A, fa);
        args.putString(Params.F_Update, update);
        plantDetail.setArguments(args);
        return plantDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        Bundle arguments = getArguments();
        E_Name = arguments.getString( Params.E_Name);
        byte[] byteArray = arguments.getByteArray(Params.F_Pic);
        Bitmap F_Pic = (byteArray == null)? null : BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String F_Name_Ch = arguments.getString( Params.F_Name_Ch);
        String F_Name_En = arguments.getString( Params.F_Name_En);
        String F_AlsoKnown = arguments.getString( Params.F_AlsoKnown);
        String F_Brief = arguments.getString( Params.F_Brief);
        String F_Feature = arguments.getString( Params.F_Feature);
        String F_A = arguments.getString( Params.F_A);
        String F_Update = arguments.getString( Params.F_Update);

        getActivity().setTitle(F_Name_Ch);

        ImageView img = view.findViewById(R.id.imageView);
        TextView name = view.findViewById(R.id.name);
        TextView en_name = view.findViewById(R.id.en_name);
        TextView alias_name = view.findViewById(R.id.alias_name);
        TextView brief = view.findViewById(R.id.brief);
        TextView feature = view.findViewById(R.id.feature);
        TextView functionality = view.findViewById(R.id.functionality);
        TextView update = view.findViewById(R.id.update);

        if (F_Pic != null) {
            img.setImageBitmap(F_Pic);
        } else {
            img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_pic));
        }
        name.setText(F_Name_Ch);
        en_name.setText(F_Name_En);
        alias_name.setText(formStringFromResource(R.string.plant_alias, F_AlsoKnown));
        brief.setText(formStringFromResource(R.string.plant_brief, F_Brief));
        feature.setText(formStringFromResource(R.string.plant_feature, F_Feature));
        functionality.setText(formStringFromResource(R.string.plant_functionality, F_A));
        update.setText(formStringFromResource(R.string.plant_update, F_Update));

        return view;
    }

    private String formStringFromResource(int resource_id, String content) {
        return String.format(getResources().getString(resource_id), content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().setTitle(E_Name);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return true;
            default:
                return false;
        }
    }
}
