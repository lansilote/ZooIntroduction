package com.example.zoointroduction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;

public class AreaDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private NestedScrollView nsv;
    private static String E_Name;

    public static AreaDetailFragment newInstance(String name, byte[] pic, String info, String url, String memo, String category) {
        AreaDetailFragment areaDetail = new AreaDetailFragment();
        Bundle args = new Bundle();
        args.putString(Params.E_Name, name);
        args.putByteArray(Params.E_Pic, pic);
        args.putString(Params.E_Info, info);
        args.putString(Params.E_URL, url);
        args.putString(Params.E_Memo, memo);
        args.putString(Params.E_Category, category);
        areaDetail.setArguments(args);
        return areaDetail;
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
        Bundle arguments = getArguments();
        E_Name = arguments.getString( Params.E_Name);
        byte[] byteArray = arguments.getByteArray(Params.E_Pic);
        Bitmap E_Pic = (byteArray == null)? null : BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String E_Info = arguments.getString( Params.E_Info);
        String E_URL = arguments.getString( Params.E_URL);
        String E_Memo = arguments.getString( Params.E_Memo);
        String E_Category = arguments.getString( Params.E_Category);

        getActivity().setTitle(E_Name);

        View view = inflater.inflate(R.layout.fragment_area_detail, container, false);

        ImageView img = view.findViewById(R.id.imageView);
        TextView detail = view.findViewById(R.id.detail);
        TextView memo = view.findViewById(R.id.memo);
        TextView category = view.findViewById(R.id.category);
        TextView url = view.findViewById(R.id.url);

        if (E_Pic != null) {
            img.setImageBitmap(E_Pic);
        } else {
            // set default image
            img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_pic));
        }
        detail.setText(E_Info);
        memo.setText((!TextUtils.isEmpty(E_Memo))? E_Memo : getResources().getString(R.string.no_memo));
        category.setText(E_Category);
        url.setText(Html.fromHtml("<a href=\"" + E_URL + "\">" + getResources().getString(R.string.open_from_browser) + "</a>"));
        url.setMovementMethod(LinkMovementMethod.getInstance());

        nsv = view.findViewById(R.id.nsv);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        PlantItemViewModel itemViewModel = ViewModelProviders.of(this, new PlantItemViewModelFactory(E_Name)).get(PlantItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(getActivity());

        itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Results>>() {
            @Override
            public void onChanged(@Nullable PagedList<Results> items) {
                adapter.submitList(items);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nsv.setFocusable(false);
    }

    public static class ItemAdapter extends PagedListAdapter<Results, ItemAdapter.ItemViewHolder> {

        private Context mCtx;

        ItemAdapter(Context mCtx) {
            super(DIFF_CALLBACK);
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_items, parent, false);
            return new ItemAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemAdapter.ItemViewHolder holder, final int position) {
            final Results result = getItem(position);

            if (result != null) {
                holder.title.setText(result.F_Name_Ch);
                holder.info.setText(result.F_AlsoKnown);
                Glide.with(mCtx)
                        .load(result.F_Pic01_URL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.w("lansilote", "onLoadFailed: " + e);
                                // set default image if picture load failed
                                holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                holder.image.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.default_pic));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        byte[] byteArray = null;
                        if (holder.image.getDrawable() != null) {
                            Bitmap bitmap = ((BitmapDrawable) holder.image.getDrawable()).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();
                        }
                        PlantDetailFragment pd = PlantDetailFragment.newInstance(E_Name, result.F_Name_Ch, result.F_Name_En,
                                byteArray, result.F_AlsoKnown, result.F_Brief, result.F_Feature, result.F_A, result.F_Update);
                        FragmentTransaction fr = ((AppCompatActivity)mCtx).getSupportFragmentManager().beginTransaction();
                        fr.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.replace(R.id.content, pd).addToBackStack( PlantDetailFragment.class.getName() );
                        fr.commit();
                    }
                });
            }
        }

        private static DiffUtil.ItemCallback<Results> DIFF_CALLBACK =
                new DiffUtil.ItemCallback<Results>() {
                    @Override
                    public boolean areItemsTheSame(Results oldItem, Results newItem) {
                        return oldItem._id == newItem._id;
                    }

                    @Override
                    public boolean areContentsTheSame(Results oldItem, Results newItem) {
                        return oldItem.equals(newItem);
                    }
                };

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView info;
            TextView memo;
            ImageView image;
            ImageView next;

            public ItemViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                info = itemView.findViewById(R.id.info);
                memo = itemView.findViewById(R.id.memo);
                image = itemView.findViewById(R.id.image);
                next = itemView.findViewById(R.id.next);

                memo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getActivity().setTitle(getResources().getString(R.string.app_name));
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return true;
            default:
                return false;
        }
    }
}
