package com.smartwebarts.callshowroom.shopbycategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.callshowroom.R;
import com.smartwebarts.callshowroom.models.CategoryModel;
import com.smartwebarts.callshowroom.models.SubCategoryModel;
import com.smartwebarts.callshowroom.productlist.ProductListActivity2;
import com.smartwebarts.callshowroom.utils.ApplicationConstants;
import com.smartwebarts.callshowroom.utils.MyGlide;

import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubCategoryModel> list;
    private CategoryModel categoryModel;
    public ChildListAdapter(Context mContext, List<SubCategoryModel> list, CategoryModel categoryModel) {
        this.mContext = mContext;
        this.list = list;
        this.categoryModel = categoryModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SubCategoryModel model = list.get(position);
        holder.groupHeader.setText(model.getName());
        MyGlide.with(mContext, ApplicationConstants.INSTANCE.CATEGORY_IMAGES + model.getImage(), holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductListActivity2.class);
                intent.putExtra(ProductListActivity2.CID, categoryModel.getId());
                intent.putExtra(ProductListActivity2.SID, model.getId());
                intent.putExtra(ProductListActivity2.SNAME, model.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView groupHeader;
        ImageView plus, imageView;
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            groupHeader = itemView.findViewById(R.id.groupHeader);
            plus = itemView.findViewById(R.id.plus);
            imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}
