package com.example.zoointroduction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;


public class AreaListFragment extends Fragment {
    private RecyclerView recyclerView;

    public static AreaListFragment newInstance() {
        return new AreaListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.app_name));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View view = inflater.inflate(R.layout.fragment_area_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
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

    public static class ItemAdapter extends PagedListAdapter<Results, ItemAdapter.ItemViewHolder> {

        private Context mCtx;

        ItemAdapter(Context mCtx) {
            super(DIFF_CALLBACK);
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_items, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
            final Results result = getItem(position);

            if (result != null) {
                holder.title.setText(result.E_Name);
                if (result.E_Info != null) holder.info.setText(result.E_Info.substring(0, 28) + "...");
                holder.memo.setText((!TextUtils.isEmpty(result.E_Memo))? result.E_Memo :
                        mCtx.getResources().getString(R.string.no_memo));
                Glide.with(mCtx)
                        .load(result.E_Pic_URL)
                        .into(holder.image);
                if (holder.image.getDrawable() == null) holder.image.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.default_pic));
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

                        AreaDetailFragment ad = AreaDetailFragment.newInstance(result.E_Name, byteArray, result.E_Info,
                                result.E_URL, result.E_Memo, result.E_Category);
                        FragmentTransaction fr = ((AppCompatActivity) mCtx).getSupportFragmentManager().beginTransaction();
                        fr.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        fr.replace(R.id.content, ad).addToBackStack(AreaDetailFragment.class.getName());
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
            }
        }
    }
}
