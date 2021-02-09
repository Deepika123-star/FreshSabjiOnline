package com.smartwebarts.callshowroom.dashboard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.callshowroom.R;
import com.smartwebarts.callshowroom.dashboard.DashboardActivity;
import com.smartwebarts.callshowroom.models.Banners;
import com.smartwebarts.callshowroom.models.CategoryModel;
import com.smartwebarts.callshowroom.models.SubCategoryModel;
import com.smartwebarts.callshowroom.productlist.ProductDetailActivity;
import com.smartwebarts.callshowroom.productlist.ProductListActivity;
import com.smartwebarts.callshowroom.retrofit.UtilMethods;
import com.smartwebarts.callshowroom.retrofit.mCallBackResponse;
import com.smartwebarts.callshowroom.utils.ApplicationConstants;
import com.smartwebarts.callshowroom.utils.CustomSlider;
import com.smartwebarts.callshowroom.utils.MyGlide;

import static com.smartwebarts.callshowroom.productlist.ProductDetailActivity.ID;


public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.MyViewHolder> {

    private final Context context;
    private final List<CategoryModel> list;


    public BottomAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


        for (Banners data : list.get(position).getBanners()) {
            CustomSlider textSliderView = new CustomSlider(context);
            // initialize a SliderLayout
            textSliderView
//                    .description("")
                    .image(ApplicationConstants.INSTANCE.OFFER_IMAGES + data.getBanner())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(context, ProductDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(ID, data.getP_id());
                            context.startActivity(intent);
                        }
                    });

            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra","");

            holder.sliderLayout.addSlider(textSliderView);
        }
//        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES+list.get(position).getImage(), holder.imageView);

        holder.categoryName.setText(list.get(position).getName());

        holder.tvViewAll.setOnClickListener(v -> {

//                Intent intent = new Intent(context, CategoryActivity.class);
//                intent.putExtra(CategoryActivity.CATEGORY, list.get(position));
//                context.startActivity(intent);

            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("category", list.get(position));
            context.startActivity(intent);
        });

        holder.imageView.setOnClickListener(v -> holder.tvViewAll.performClick());

        holder.categoryName.setOnClickListener(v -> holder.tvViewAll.performClick());

        UtilMethods.INSTANCE.subCategories(context, list.get(position).getId(), new mCallBackResponse() {
            @Override
            public void success(String from, String message) {
                Type listType = new TypeToken<ArrayList<SubCategoryModel>>(){}.getType();
                List<SubCategoryModel> sublist = new Gson().fromJson(message, listType);
                setCategoryRecycler(holder.recyclerViewCategory,sublist, list.get(position));
            }
            @Override
            public void fail(String from) {
            }
        });
    }

    private void setCategoryRecycler(RecyclerView recyclerView, List<SubCategoryModel> list, CategoryModel categoryModel) {
        GridAdapter adapter = new GridAdapter(context,list, categoryModel);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoryName, tvViewAll;
        RecyclerView recyclerViewCategory;
        SliderLayout sliderLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tvName);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            tvViewAll = (TextView) itemView.findViewById(R.id.tv_viewAll);
            recyclerViewCategory = (RecyclerView) itemView.findViewById(R.id.recyclerViewCategory);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.home_img_slider);
        }
    }
}
