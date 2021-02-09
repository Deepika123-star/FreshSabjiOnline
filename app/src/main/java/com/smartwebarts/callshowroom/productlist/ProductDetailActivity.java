package com.smartwebarts.callshowroom.productlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.smartwebarts.callshowroom.BuildConfig;
import com.smartwebarts.callshowroom.R;
import com.smartwebarts.callshowroom.adapter.YouMayLikeAdapter;
import com.smartwebarts.callshowroom.database.SaveProductList;
import com.smartwebarts.callshowroom.database.Task;
import com.smartwebarts.callshowroom.models.ProductDetailImagesModel;
import com.smartwebarts.callshowroom.models.ProductDetailModel;
import com.smartwebarts.callshowroom.models.ProductModel;
import com.smartwebarts.callshowroom.models.UnitModel;
import com.smartwebarts.callshowroom.retrofit.UtilMethods;
import com.smartwebarts.callshowroom.retrofit.mCallBackResponse;
import com.smartwebarts.callshowroom.shared_preference.AppSharedPreferences;
import com.smartwebarts.callshowroom.shared_preference.SharedLinkPrefs;
import com.smartwebarts.callshowroom.utils.ApplicationConstants;
import com.smartwebarts.callshowroom.utils.CustomSlider;
import com.smartwebarts.callshowroom.utils.ImageFullViewActivity;
import com.smartwebarts.callshowroom.utils.Toolbar_Set;

public class ProductDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private com.daimajia.slider.library.SliderLayout viewPager;
    private int currentPage = 0;
    private ArrayList<String> sliderImage= new ArrayList<String>();
    private ProductModel addToCartProductItem;
    private TextView tvName, txt_vName, tvDescription2, tvPrice,tvPricen, tvCurrentPrice, tvDiscount, tvOffer;
    private CardView cvoffer;
//    private ImageView ivVeg;
    public static final String ID = "id";
    private String pid;
    private RecyclerView recyclerView;
    private String unit="", unitIn="", currentPrice="0", buingPrice = "0", discount = "0";
    List<ProductModel> likelist = new ArrayList<>();
    private YouMayLikeAdapter adapter;
    private RecyclerView also_like;
    private String proImageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar_Set.INSTANCE.setToolbar(this);
        viewPager = findViewById(R.id.viewPager);
        tvName = findViewById(R.id.txt_pName);
        txt_vName = findViewById(R.id.txt_vName);
//        tvDescription = findViewById(R.id.txt_pInfo);
        tvDescription2 = findViewById(R.id.tvDescription);
        tvDiscount = findViewById(R.id.txt_discount);
//        ivVeg = findViewById(R.id.iv_veg);
        tvPrice = findViewById(R.id.txt_price);
        tvPricen = findViewById(R.id.txt_pricen);
        tvCurrentPrice = findViewById(R.id.txt_current_price);
        //tvOffer = (TextView) findViewById(R.id.tvoffer);
        //cvoffer = (CardView) findViewById(R.id.cvoffer);
        recyclerView = findViewById(R.id.recyclerView);
        also_like = findViewById(R.id.also_like);

        if (getIntent().getExtras()!=null) {
            pid = getIntent().getExtras().getString(ID);
        }

        if (pid==null) {
            getDynamicLink();
        } else {
            getDetails();
        }
    }

    private void setUpImageSlider(List<ProductDetailImagesModel> list) {

//        ImageAdapter imageAdapter = new ImageAdapter(this, list);

//        viewPager.setAdapter(imageAdapter);
//
//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == sliderImage.size()) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        Timer timer = new Timer(); // This will create a new Thread
//
//        //delay in milliseconds before task is to be executed
//        long DELAY_MS = 500;
//
//        // time in milliseconds between successive task executions.
//        long PERIOD_MS = 3000;
//
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);

        for ( ProductDetailImagesModel data : list) {
            CustomSlider textSliderView = new CustomSlider(this);
            // initialize a SliderLayout
            textSliderView
                    .description("")
                    .image(ApplicationConstants.INSTANCE.PRODUCT_IMAGES + data.getThumbnail())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Activity activity = ProductDetailActivity.this;
                            Intent intent = new Intent(activity, ImageFullViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("url", ApplicationConstants.INSTANCE.PRODUCT_IMAGES + data.getThumbnail());
                            activity.startActivity(intent);
                        }
                    });

            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra","");

            viewPager.addSlider(textSliderView);
        }

        viewPager.getPagerIndicator().setDefaultIndicatorColor (0xff68B936, 0xffD0D0D0);
        viewPager.startAutoCycle(10000, 10000, true);
        viewPager.setCurrentPosition(0);
    }

    public void addToBag(View view) {

        if (addToCartProductItem !=null){

            List<Task> items = new ArrayList<>();
            Task task = new Task(addToCartProductItem, "1", unit, unitIn, currentPrice, currentPrice);
            items.add(task);
            SaveProductList savepro = new SaveProductList(this, items);
            savepro.isShowSuccessMsg=true;
            savepro.execute();
//            new SaveProductList(this,items).execute();
            Toolbar_Set.INSTANCE.getCartList(this);
        }

    }

     public  void getDetails() {

         if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

             UtilMethods.INSTANCE.getProductDetails(this, pid, new mCallBackResponse() {
                 @Override
                 public void success(String from, String message) {
                     if (!message.isEmpty()){
                         Type listType = new TypeToken<ArrayList<ProductDetailModel>>(){}.getType();
                         List<ProductDetailModel> list = new Gson().fromJson(message, listType);

                         addToCartProductItem = new ProductModel(list.get(0));
                         tvName.setText(addToCartProductItem.getName().trim());

                         if (list.get(0)!=null && list.get(0).getVendorName()!=null) {
                             txt_vName.setText("("+list.get(0).getVendorName().trim()+")");
                         }


//                         tvDescription.setText(addToCartProductItem.getDescription().trim());
                         tvDescription2.setText(addToCartProductItem.getDescription().trim());
                         if (addToCartProductItem.getUnits()!=null && addToCartProductItem.getUnits().size()>0) {
                             unit = addToCartProductItem.getUnits().get(0).getUnit().trim();
                             unitIn = addToCartProductItem.getUnits().get(0).getUnitIn().trim();
                             currentPrice = addToCartProductItem.getUnits().get(0).getBuingprice().trim();
                             buingPrice = addToCartProductItem.getUnits().get(0).getCurrentprice().trim();
                         }

                         try {
                             int a = (int) Double.parseDouble("0"+currentPrice);
                             int b = (int) Double.parseDouble("0"+buingPrice);
                             int c = b-a;
                             discount = ""+c;

                             try {
                                 int p = c*100/b;
                                 tvOffer.setText(p + "%");
                             } catch (Exception ignored){
                                 cvoffer.setVisibility(View.GONE);
                             }
                         } catch (Exception ignored) {}


                         tvCurrentPrice.setText(getString(R.string.currency) + " " + currentPrice);
                         tvPrice.setText(  unit + unitIn);

                         tvPricen.setText( getString(R.string.currency) + " " + buingPrice);
                         tvPricen.setPaintFlags(tvPricen.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                         tvDiscount.setText(getString(R.string.currency) + discount);

//                         if (addToCartProductItem.getProductType().equalsIgnoreCase("Non-Vegetarian")) {
//                             ivVeg.setImageDrawable(getDrawable(R.drawable.nonveg));
//                             ivVeg.setVisibility(View.VISIBLE);
//                         } else if (addToCartProductItem.getProductType().equalsIgnoreCase("Vegetarian")){
//                             ivVeg.setImageDrawable(getDrawable(R.drawable.veg));
//                             ivVeg.setVisibility(View.VISIBLE);
//                         } else {
//                             ivVeg.setVisibility(View.GONE);
//                         }

                         setRecycler(addToCartProductItem.getUnits());

                         UtilMethods.INSTANCE.products(ProductDetailActivity.this, list.get(0).getCat_id(),"",""/*subCategory.getId(), subSubCategory.getId()*/,new mCallBackResponse() {
                             @Override
                             public void success(String from, String message) {
                                 Type listType = new TypeToken<ArrayList<ProductModel>>(){}.getType();
                                 likelist = new Gson().fromJson(message, listType);
                                 adapter = new YouMayLikeAdapter(ProductDetailActivity.this, likelist);
                                 also_like.setAdapter(adapter);
                             }

                             @Override
                             public void fail(String from) {
                                 Toast.makeText(ProductDetailActivity.this, from, Toast.LENGTH_SHORT).show();

                             }
                         });

                     }
                 }

                 @Override
                 public void fail(String from) {

                 }
             });

             UtilMethods.INSTANCE.getProductImages(this, pid, new mCallBackResponse() {
                 @Override
                 public void success(String from, String message) {
                     if (!message.isEmpty()){
                         Type listType = new TypeToken<ArrayList<ProductDetailImagesModel>>(){}.getType();
                         List<ProductDetailImagesModel> list = new Gson().fromJson(message, listType);
                         setUpImageSlider(list);
                         try {
                             proImageLink = ApplicationConstants.INSTANCE.PRODUCT_IMAGES + list.get(0).getThumbnail();
                         } catch (Exception ignored) {
                             ignored.printStackTrace();
                         }
                     }
                 }

                 @Override
                 public void fail(String from) {

                 }
             });
         } else {
             UtilMethods.INSTANCE.internetNotAvailableMessage(this);
         }
     }

    private void setRecycler(List<UnitModel> list) {
        UnitListAdapter adapter = new UnitListAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    public void onClick(int position) {

        if (addToCartProductItem.getUnits().get(position) !=null) {
            UnitModel temp = addToCartProductItem.getUnits().get(position);
            unit = temp.getUnit();
            unitIn = temp.getUnitIn();
            currentPrice = temp.getBuingprice();
            buingPrice = temp.getCurrentprice();
            tvCurrentPrice.setText(getString(R.string.currency) + " " + currentPrice);
            tvPricen.setText(getString(R.string.currency) + " " + buingPrice);
            tvPricen.setPaintFlags(tvPricen.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.setText(  unit + unitIn);

            try {
                int a = (int) Double.parseDouble("0"+currentPrice);
                int b = (int) Double.parseDouble("0"+buingPrice);
                int c = b-a;
                discount = ""+c;
            } catch (Exception ignored) {}

            tvDiscount.setText(getString(R.string.currency) + discount);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public void share_product(View view) {
        if (pid!=null) {
            createlink();
        }
    }

    private void getDynamicLink() {
        Uri uri = getIntent().getData();
        String link = uri.getQueryParameter("link");
        uri = Uri.parse(link);
        pid = uri.getQueryParameter("id");
        Log.e("TAG", "getDynamicLink: " + pid);
        getDetails();
    }

//    private void getDynamicLink() {
//
//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(ProductDetailActivity.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        // Get deep link from result (may be null if no link is found)
//                        Uri deepLink = null;
//                        if (pendingDynamicLinkData != null) {
//                            deepLink = pendingDynamicLinkData.getLink();
//
//                            String referlink = deepLink.toString();
//
//                            Log.e("TAG", "onSuccess: " + referlink);
//                            try {
//                                pid = referlink.substring(referlink.lastIndexOf("=")+1, referlink.length());
//                                getDetails();
////                                Toast.makeText(SignInActivity.this, ""+custid, Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//
//                        // Handle the deep link. For example, open the linked
//                        // content, or apply promotional credit to the user's
//                        // account.
//                        // ...
//
//                        // ...
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", "getDynamicLink:onFailure", e);
//                    }
//                });
//    }

//    private void createlink() {
//
//        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(ApplicationConstants.INSTANCE.SITE_URL))
//                .setDomainUriPrefix("https://callshowroom.page.link")
//                // Open links with this app on Android
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                .buildDynamicLink();
//
//        Uri dynamicLinkUri = dynamicLink.getUri();
//
//        Log.e("TAG", "createlink: " + dynamicLinkUri.toString());
//
//        com.google.android.gms.tasks.Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(ApplicationConstants.INSTANCE.SITE_URL))
//                .setDomainUriPrefix("https://callshowroom.page.link")
//                // Set parameters
//                // ...
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//
//                                    Intent sendIntent = new Intent();
//                                    sendIntent.setAction(Intent.ACTION_SEND);
//                                    sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
//                                    sendIntent.setType("text/plain");
//                                    startActivity(sendIntent);
//                        } else {
//                            Log.e("TAG", "onComplete: "+ task.getException() );
//                        }
//                    }
//                });
//
//    }

    private void createlink() {

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String referlink = "https://callshowroomproduct.page.link?" +
                "apn=" + BuildConfig.APPLICATION_ID +
                "&st="+getString(R.string.app_name) +
                "&sd=limited+stock+available" +
                "&si="+proImageLink +
                "&link=" + ApplicationConstants.INSTANCE.SITE_URL + "api.php?" + ID+"=" + pid;

        Log.e("referlink", referlink);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, referlink.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        /*short the link*/
//        com.google.android.gms.tasks.Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLongLink(Uri.parse(referlink))
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Short link created
//                        Uri shortLink = task.getResult().getShortLink();
//                        Uri flowchartLink = task.getResult().getPreviewLink();
//                        Intent sendIntent = new Intent();
//                        sendIntent.setAction(Intent.ACTION_SEND);
//                        sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
//                        sendIntent.setType("text/plain");
//                        startActivity(sendIntent);
//                    }
//                })
//
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", "onFailure: "+ e.getMessage() );
//                        e.printStackTrace();
//                    }
//                });

    }
}
