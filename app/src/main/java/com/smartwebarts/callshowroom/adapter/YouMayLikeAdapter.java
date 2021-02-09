package com.smartwebarts.callshowroom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.callshowroom.R;
import com.smartwebarts.callshowroom.database.DatabaseClient;
import com.smartwebarts.callshowroom.database.Task;
import com.smartwebarts.callshowroom.database.WishItem;
import com.smartwebarts.callshowroom.models.AddWishListResponse;
import com.smartwebarts.callshowroom.models.ProductModel;
import com.smartwebarts.callshowroom.models.UnitModel;
import com.smartwebarts.callshowroom.productlist.ProductDetailActivity;
import com.smartwebarts.callshowroom.retrofit.UtilMethods;
import com.smartwebarts.callshowroom.retrofit.mCallBackResponse;
import com.smartwebarts.callshowroom.shared_preference.AppSharedPreferences;
import com.smartwebarts.callshowroom.utils.ApplicationConstants;
import com.smartwebarts.callshowroom.utils.MyGlide;
import com.smartwebarts.callshowroom.utils.Toolbar_Set;
import com.smartwebarts.callshowroom.database.SaveProductList;

public class YouMayLikeAdapter extends RecyclerView.Adapter<com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder>{

    private Context context;
    private List<ProductModel> list;

    public YouMayLikeAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trending_now_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder holder, final int position) {

        try {

            MyGlide.with(context, ApplicationConstants.INSTANCE.PRODUCT_IMAGES + list.get(position).getThumbnail(), holder.prodImage);

            holder.txt_pName.setText(list.get(position).getName().trim());
            holder.txt_pInfo.setText(list.get(position).getDescription().trim());

            ArrayList<String> units = new ArrayList<>();
            for (UnitModel unitModel: list.get(position).getUnits()) {
                units.add(unitModel.getUnit() + unitModel.getUnitIn());
            }


            if (list.get(position).getUnits()!=null && list.get(position).getUnits().size()>0)
            {
                holder.currentprice.setText(String.format("%s%s", context.getString(R.string.currency), list.get(position).getUnits().get(0).getBuingprice()));
                holder.price.setText(String.format("%s%s", context.getString(R.string.currency), list.get(position).getUnits().get(0).getCurrentprice()));
                holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            List<UnitModel> temp = list.get(position).getUnits();
            if (temp!=null && temp.size()>0) {
                int pos = 0;
                holder.strUnit[position] = temp.get(pos).getUnit();
                holder.strUnitIn[position] = temp.get(pos).getUnitIn();
                holder.currentprice.setText(String.format("%s%s", context.getString(R.string.currency), temp.get(pos).getBuingprice()));
                holder.price.setText(String.format("%s%s", context.getString(R.string.currency), temp.get(pos).getCurrentprice().trim()));
                holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                try {
                    int a = (int) Double.parseDouble("0"+temp.get(pos).getBuingprice().trim());
                    int b = (int) Double.parseDouble("0"+temp.get(pos).getCurrentprice().trim());
                    int c = (b-a);
                    int d = (int) c*100/b;
                    String discount = "" + d;
                    holder.txt_discountOff.setText(String.format("%s%% Off", discount));
                } catch (Exception ignored) {
                    holder.txt_discountOff.setText("NEW");
                }
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.simple_list_item_1, units);
            holder.txt_unit.setAdapter(arrayAdapter);

            holder.txt_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    holder.strUnit[position] = list.get(position).getUnits().get(pos).getUnit();
//                    holder.strUnitIn[position] = list.get(position).getUnits().get(pos).getUnitIn();
//                    holder.currentprice.setText(context.getString(R.string.currency) + list.get(position).getUnits().get(pos).getBuingprice());
//
                    List<UnitModel> temp = list.get(position).getUnits();
                    if (temp!=null && temp.size()>0) {
                        holder.strUnit[position] = temp.get(pos).getUnit();
                        holder.strUnitIn[position] = temp.get(pos).getUnitIn();
                        holder.currentprice.setText(String.format("%s%s", context.getString(R.string.currency), temp.get(pos).getBuingprice()));
                        holder.price.setText(String.format("%s%s", context.getString(R.string.currency), temp.get(pos).getCurrentprice().trim()));
                        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                        try {
                            int a = (int) Double.parseDouble("0"+temp.get(pos).getBuingprice().trim());
                            int b = (int) Double.parseDouble("0"+temp.get(pos).getCurrentprice().trim());
                            int c = (b-a)*(-1);
                            int d = (int) c*100/b;
                            String discount = "" + d;
                            holder.txt_discountOff.setText(discount+"%");
                        } catch (Exception ignored) {
                            holder.txt_discountOff.setText("NEW");
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

//            holder.price.setText( list.get(position).getPrice().trim());
//            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.addToWishList.setImageDrawable(
                    context.getDrawable(list.get(position).isWishlist()?
                            R.drawable.ic_heart_red:
                            R.drawable.ic_heart_black));

            holder.addToWishList.setTag(list.get(position).isWishlist()?
                    context.getString(R.string.selected):
                    context.getString(R.string.unselected));

//            double currentPrice = Double.parseDouble("0"+list.get(position).getCurrentprice().trim());
//            double price = Double.parseDouble("0"+list.get(position).getPrice().trim());
//            double discount = (price - currentPrice) *100/price;
//            NumberFormat nf = NumberFormat.getInstance();
//            nf.setMaximumFractionDigits(2);
//            holder.txt_discountOff.setText(""+nf.format(discount*-1)+"%");

        } catch ( Exception ignored){

        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.ID, list.get(position).getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImage;
        ImageView addToWishList;
        TextView txt_pName, txt_pInfo, currentprice, price, txt_discountOff, txtQuan;
        Spinner txt_unit;
        RelativeLayout rlQuan;
        LinearLayout ll_addQuan, ll_Add;
        TextView plus, minus;
        int minteger = 0;
        String[] strUnit = new String[list.size()];
        String[] strUnitIn = new String[list.size()];

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.prodImage);
            addToWishList = itemView.findViewById(R.id.addToWishList);

            txt_pName = itemView.findViewById(R.id.txt_pName);
            txt_pInfo = itemView.findViewById(R.id.txt_pInfo);
            txt_unit = itemView.findViewById(R.id.txt_current_price);
            currentprice = itemView.findViewById(R.id.currentprice);
            price = itemView.findViewById(R.id.txt_price);
            txt_discountOff = itemView.findViewById(R.id.txt_discountOff);

            rlQuan =  itemView.findViewById(R.id.rlQuan);
            ll_Add =  itemView.findViewById(R.id.ll_Add);
            ll_addQuan =  itemView.findViewById(R.id.ll_addQuan);
            txtQuan =  itemView.findViewById(R.id.txtQuan);
            minus =  itemView.findViewById(R.id.minus);
            plus =  itemView.findViewById(R.id.plus);

            addToWishList.setOnClickListener(v -> {

                AppSharedPreferences preferences = null;
                if (context instanceof Activity)
                    preferences = new AppSharedPreferences(((Activity) context).getApplication());
                if (preferences != null) {
                    if (!preferences.getLoginUserLoginId().isEmpty()) {

                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                            /*add to wishlist*/

                            if (addToWishList.getTag().toString().equalsIgnoreCase(context.getString(R.string.unselected))){
                                UtilMethods.INSTANCE.addtowishlist(context, list.get(com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder.this.getAdapterPosition()).getId(),
                                        preferences.getLoginUserLoginId(), strUnitIn[getAdapterPosition()],strUnit[getAdapterPosition()]
                                        , new mCallBackResponse() {
                                            @Override
                                            public void success(String from, String message) {

                                                addToWishList.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                                                addToWishList.setTag(context.getString(R.string.selected));
                                                AddWishListResponse response = new Gson().fromJson(message, AddWishListResponse.class);
                                                if (response != null && response.getMessage() != null && response.getMessage().equalsIgnoreCase("success")) {
                                                    list.get(getAdapterPosition()).setWishlist(true);
                                                    notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void fail(String from) {

                                            }
                                        });
                            } else {
                                UtilMethods.INSTANCE.removewishlist(context, list.get(com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder.this.getAdapterPosition()).getId(),
                                        preferences.getLoginUserLoginId(),"",""
                                        , new mCallBackResponse() {
                                            @Override
                                            public void success(String from, String message) {

                                                addToWishList.setImageDrawable(context.getDrawable(R.drawable.ic_heart_black));
                                                addToWishList.setTag(context.getString(R.string.unselected));
                                            }

                                            @Override
                                            public void fail(String from) {

                                            }
                                        });
                            }

                        } else {
                            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
                        }

                    } else {
                        Toast.makeText(context, "Login to create a wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ll_Add.setOnClickListener(v -> {
                ll_Add.setVisibility(View.GONE);
                ll_addQuan.setVisibility(View.VISIBLE);
                txtQuan.setText("1");
                com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder.this.addToBag("1");
            });

            plus.setOnClickListener(v -> {
                com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder.this.increaseInteger();

                if (Float.parseFloat(txtQuan.getText().toString()) == 1) {

                } else if (Float.parseFloat(txtQuan.getText().toString()) > 1) {
                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder.this.decreaseInteger();
                        }
                    });
                }
            });
        }


        public void increaseInteger() {
            minteger = minteger + 1;
            display(minteger);
        }

        public void decreaseInteger() {
            if (minteger == 1) {
                minteger = 1;
                display(minteger);
                ll_addQuan.setVisibility(View.GONE);
                ll_Add.setVisibility(View.VISIBLE);
            } else {
                minteger = minteger - 1;
                display(minteger);

            }
        }

        private void display(Integer number) {
            txtQuan.setText("" + number);
            addToBag(""+number);
        }

        public void addToBag(String quantity) {

            int price = Integer.parseInt("0"+list.get(getAdapterPosition()).getUnits().get(txt_unit.getSelectedItemPosition()).getBuingprice().trim());
            int qty = Integer.parseInt("0"+quantity.trim());
            int finalprice = price*qty;

            List<Task> items = new ArrayList<>();
            Task task = new Task(list.get(getAdapterPosition()),
                    quantity,
                    list.get(getAdapterPosition()).getUnits().get(txt_unit.getSelectedItemPosition()).getUnit(),
                    list.get(getAdapterPosition()).getUnits().get(txt_unit.getSelectedItemPosition()).getUnitIn(),
                    list.get(getAdapterPosition()).getUnits().get(txt_unit.getSelectedItemPosition()).getBuingprice(),
                    ""+finalprice
            );
            items.add(task);
            new SaveProductList(context,items).execute();
            Toolbar_Set.INSTANCE.getCartList((Activity) context);
        }

    }

    public static void delete(final Activity activity, final WishItem task) {

        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                DatabaseClient.getmInstance(activity.getApplicationContext())
                        .getAppDatabase()
                        .wishDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.AsyncTask;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.gson.Gson;
//import com.smartwebarts.callshowroom.R;
//import com.smartwebarts.callshowroom.database.DatabaseClient;
//import com.smartwebarts.callshowroom.database.SaveProductList;
//import com.smartwebarts.callshowroom.database.Task;
//import com.smartwebarts.callshowroom.models.AddWishListResponse;
//import com.smartwebarts.callshowroom.models.ProductModel;
//import com.smartwebarts.callshowroom.models.UnitModel;
//import com.smartwebarts.callshowroom.productlist.ProductDetailActivity;
//import com.smartwebarts.callshowroom.retrofit.UtilMethods;
//import com.smartwebarts.callshowroom.retrofit.mCallBackResponse;
//import com.smartwebarts.callshowroom.shared_preference.AppSharedPreferences;
//import com.smartwebarts.callshowroom.utils.ApplicationConstants;
//import com.smartwebarts.callshowroom.utils.MyGlide;
//import com.smartwebarts.callshowroom.utils.Toolbar_Set;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class YouMayLikeAdapter extends RecyclerView.Adapter<YouMayLikeAdapter.MyViewHolder>{
//
//    private Context context;
//    private List<ProductModel> list;
//
//    public YouMayLikeAdapter(Context context, List<ProductModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.youmaylike_list_item, parent, false));
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    @Override
//    public void onBindViewHolder(@NonNull com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter.MyViewHolder holder, final int position) {
//
//        try {
//
//            MyGlide.with(context, ApplicationConstants.INSTANCE.PRODUCT_IMAGES + list.get(position).getThumbnail(), holder.prodImage);
//
//            holder.txt_pName.setText(list.get(position).getName().trim());
//            holder.txt_pInfo.setText(list.get(position).getDescription().trim());
//
//            ArrayList<String> units = new ArrayList<>();
//            for (UnitModel unitModel: list.get(position).getUnits()) {
//                units.add(unitModel.getUnit() + unitModel.getUnitIn());
//            }
//
//            if (list.get(position).getUnits()!=null && list.get(position).getUnits().size()>0) {
//                holder.currentprice.setText(String.format("%s%s", context.getString(R.string.currency), list.get(position).getUnits().get(0).getBuingprice()));
//                holder.price.setText(String.format("%s%s", context.getString(R.string.currency), list.get(position).getUnits().get(0).getCurrentprice()));
//                holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//
//            holder.txt_unit.setText(units.get(0));
//            try {
//                int a = (int) Double.parseDouble("0"+list.get(position).getUnits().get(0).getBuingprice().trim());
//                int b = (int) Double.parseDouble("0"+list.get(position).getUnits().get(0).getCurrentprice().trim());
//                int c = (b-a);
//                int d = c*100/b;
//                String discount = "" + d;
//                holder.txt_discountOff.setText(String.format("OFF %s%%", discount));
//            } catch (Exception ignored) {
//                holder.txt_discountOff.setText(R.string._new);
//            }
//
//            if (list.get(position).getUnits()!=null && Integer.parseInt("0" + list.get(position).getUnits().get(0).getQuantity())>0) {
//                holder.txt_add.setText(context.getString(R.string.add));
//                holder.ll_Add.setEnabled(true);
//            } else {
//                holder.txt_add.setText(context.getString(R.string.out_of_stock));
//                holder.txt_add.setPadding(0,0,0,0);
//                holder.txt_add.setTextSize(12);
//                holder.ll_Add.setEnabled(false);
//            }
//
//        } catch (Exception ignored){
//
//        }
//
//        holder.prodImage.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ProductDetailActivity.class);
//            intent.putExtra(ProductDetailActivity.ID, list.get(position).getId());
//            context.startActivity(intent);
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView txt_add;
//        ImageView prodImage;
//        ImageView addToWishList;
//        TextView txt_pName, txt_pInfo, currentprice, price, txt_discountOff, txtQuan;
//        TextView txt_unit;
//        RelativeLayout rlQuan;
//        LinearLayout ll_addQuan, ll_Add;
//        TextView plus, minus;
//        int minteger = 0;
//        String[] strUnit = new String[list.size()];
//        String[] strUnitIn = new String[list.size()];
//        Button addToCart;
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            prodImage = itemView.findViewById(R.id.prodImage);
//            addToWishList = itemView.findViewById(R.id.addToWishList);
//
//            txt_pName = itemView.findViewById(R.id.txt_pName);
//            txt_pInfo = itemView.findViewById(R.id.txt_pInfo);
//            txt_unit = itemView.findViewById(R.id.txt_current_price);
//            txt_add = itemView.findViewById(R.id.txt_add);
//            currentprice = itemView.findViewById(R.id.currentprice);
//            price = itemView.findViewById(R.id.txt_price);
//            txt_discountOff = itemView.findViewById(R.id.txt_discountOff);
//
//            rlQuan =  itemView.findViewById(R.id.rlQuan);
//            ll_Add =  itemView.findViewById(R.id.ll_Add);
//            ll_addQuan =  itemView.findViewById(R.id.ll_addQuan);
//            txtQuan =  itemView.findViewById(R.id.txtQuan);
//            minus =  itemView.findViewById(R.id.minus);
//            plus =  itemView.findViewById(R.id.plus);
//            addToCart =  itemView.findViewById(R.id.addToCart);
//
//            addToWishList.setOnClickListener(v -> {
//
//                AppSharedPreferences preferences = null;
//                if (context instanceof Activity)
//                    preferences = new AppSharedPreferences(((Activity) context).getApplication());
//                if (preferences != null) {
//                    if (!preferences.getLoginUserLoginId().isEmpty()) {
//
//                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
//
//                            /*add to wishlist*/
//
//                            if (addToWishList.getTag().toString().equalsIgnoreCase(context.getString(R.string.unselected))){
//                                UtilMethods.INSTANCE.addtowishlist(context, list.get(MyViewHolder.this.getAdapterPosition()).getId(),
//                                        preferences.getLoginUserLoginId(), strUnitIn[getAdapterPosition()],strUnit[getAdapterPosition()]
//                                        , new mCallBackResponse() {
//                                            @Override
//                                            public void success(String from, String message) {
//
//                                                addToWishList.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
//                                                addToWishList.setTag(context.getString(R.string.selected));
//                                                AddWishListResponse response = new Gson().fromJson(message, AddWishListResponse.class);
//                                                if (response != null && response.getMessage() != null && response.getMessage().equalsIgnoreCase("success")) {
//                                                    list.get(getAdapterPosition()).setWishlist(true);
//                                                    notifyDataSetChanged();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void fail(String from) {
//
//                                            }
//                                        });
//                            } else {
//                                UtilMethods.INSTANCE.removewishlist(context, list.get(MyViewHolder.this.getAdapterPosition()).getId(),
//                                        preferences.getLoginUserLoginId(),"",""
//                                        , new mCallBackResponse() {
//                                            @Override
//                                            public void success(String from, String message) {
//
//                                                addToWishList.setImageDrawable(context.getDrawable(R.drawable.ic_heart_black));
//                                                addToWishList.setTag(context.getString(R.string.unselected));
//                                            }
//
//                                            @Override
//                                            public void fail(String from) {
//
//                                            }
//                                        });
//                            }
//
//                        } else {
//                            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
//                        }
//
//                    } else {
//                        Toast.makeText(context, "Login to create a wishlist", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            addToCart.setOnClickListener(v -> {
//                txtQuan.setText("1");
//                MyViewHolder.this.addToBag("1");
//                addToCart.setTextColor(Color.GREEN);
//                Toast.makeText(context, "Item Added in to your Cart ", Toast.LENGTH_SHORT).show();
//            });
//            ll_Add.setOnClickListener(v -> {
//                ll_Add.setVisibility(View.GONE);
//                ll_addQuan.setVisibility(View.VISIBLE);
//                txtQuan.setText("1");
//                minteger =1;
//                MyViewHolder.this.addToBag("1");
//            });
//
//            plus.setOnClickListener(v -> MyViewHolder.this.increaseInteger());
//
//            minus.setOnClickListener(v1 -> MyViewHolder.this.decreaseInteger());
//        }
//
//
//        public void increaseInteger() {
//            minteger = minteger + 1;
//            display(minteger);
//        }
//
//        public void decreaseInteger() {
//            minteger = minteger - 1;
//            if (minteger <= 0) {
//                minteger = 0;
//                display(minteger);
//                ll_addQuan.setVisibility(View.GONE);
//                ll_Add.setVisibility(View.VISIBLE);
//                delete(context, list.get(getAdapterPosition()));
//            } else {
//                display(minteger);
//            }
//        }
//
//        private void display(Integer number) {
//            txtQuan.setText(String.format("%s", number));
//            addToBag(""+number);
//        }
//
//        public void addToBag(String quantity) {
//
//            int price = Integer.parseInt("0"+list.get(getAdapterPosition()).getUnits().get(0).getBuingprice().trim());
//            int qty = Integer.parseInt("0"+quantity.trim());
//            int finalprice = price*qty;
//
//            List<Task> items = new ArrayList<>();
//            Task task = new Task(list.get(getAdapterPosition()),
//                    quantity,
//                    list.get(getAdapterPosition()).getUnits().get(0).getUnit(),
//                    list.get(getAdapterPosition()).getUnits().get(0).getUnitIn(),
//                    list.get(getAdapterPosition()).getUnits().get(0).getBuingprice(),
//                    ""+finalprice
//            );
//            items.add(task);
//
//            new SaveProductList(context,items).execute();
//            Toolbar_Set.INSTANCE.getCartList((Activity) context);
//        }
//
//    }
//
//    public static void delete(final Context context, final ProductModel task) {
//
//        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {
//
//            @Override
//            protected ArrayList<Task> doInBackground(Void... voids) {
//                DatabaseClient.getmInstance(context.getApplicationContext())
//                        .getAppDatabase()
//                        .taskDao()
//                        .deleteWithID(task.getId());
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<Task> tasks) {
//                Toolbar_Set.INSTANCE.getCartList((Activity) context);
//
////                FragmentActivityMessage activityActivityMessage =
////                        new FragmentActivityMessage(new Gson().toJson(task), "updatelist");
////                GlobalBus.getBus().post(activityActivityMessage);
//            }
//        }
//
//        GetTasks gt = new GetTasks();
//        gt.execute();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    public void setList(List<ProductModel> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//}
