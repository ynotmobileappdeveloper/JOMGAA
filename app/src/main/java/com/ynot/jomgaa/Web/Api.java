package com.ynot.jomgaa.Web;

import com.ynot.jomgaa.FunctionModels.CartItems;
import com.ynot.jomgaa.FunctionModels.CategoryModel;
import com.ynot.jomgaa.FunctionModels.FavModel;
import com.ynot.jomgaa.FunctionModels.FeatureModel;
import com.ynot.jomgaa.FunctionModels.HomeSlider;
import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.FunctionModels.OrderItems;
import com.ynot.jomgaa.FunctionModels.ProductsModel;
import com.ynot.jomgaa.FunctionModels.ProfileModel;
import com.ynot.jomgaa.FunctionModels.SubcategoryModel;
import com.ynot.jomgaa.FunctionModels.productDetailModel;
import com.ynot.jomgaa.Model.GetOTP;
import com.ynot.jomgaa.Model.LoginUser;
import com.ynot.jomgaa.Model.RegisterUser;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("login")
    Call<LoginUser> userLogin(
            @Field("mob") String user);

    @FormUrlEncoded
    @POST("registration")
    Call<GetOTP> registration(
            @Field("email") String email,
            @Field("mobile") String mob);

    @FormUrlEncoded
    @POST("registration_complete")
    Call<RegisterUser> Registeruser(
            @Field("name") String name,
            @Field("smsotp") String smsotp,
            @Field("otp") String otp,
            @Field("mob") String mob,
            @Field("email") String email,
            @Field("password") String password);

    @POST("category")
    Call<CategoryModel> GetCategory();

    @FormUrlEncoded
    @POST("subcategory")
    Call<SubcategoryModel>
    GetSubcategory(@Field("category_id") String cat_id);

    @FormUrlEncoded
    @POST("get_fav_list")
    Call<FavModel> GetFav(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ProfileModel> EditProfile(
            @Field("user_id") String user_id,
            @Field("email") String mob,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("get_cart_items")
    Call<CartItems> GetCartItems(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_order_items")
    Call<OrderItems> GetOrderItems(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("products")
    Call<ProductsModel> getProducts(@Field("sub_cat_id") String id,
                                    @Field("user_id") String user_id);

    @POST("get_todays_deal")
    Call<ProductsModel> getTodaysDeals();

    @FormUrlEncoded
    @POST("get_product_details")
    Call<productDetailModel> getProductDetails(@Field("product_id") String id,
                                               @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("add_to_cart")
    Call<NormalData> add_to_cart(@Field("user_id") String user_id,
                                 @Field("product_id") String product_id,
                                 @Field("size_id") String size_id,
                                 @Field("quantity") String qua);

    @FormUrlEncoded
    @POST("set_fav")
    Call<NormalData> set_fav(@Field("product_id") String product_id,
                             @Field("user_id") String user_id,
                             @Field("fav_status") String fav_id);

    @FormUrlEncoded
    @POST("search_item")
    Call<ProductsModel> getSearchData(@Field("search_key") String key,
                                      @Field("user_id") String user_id);

    @POST("get_home_slider")
    Call<HomeSlider> getHomeSlider();

    @FormUrlEncoded
    @POST("change_password")
    Call<NormalData> ChangePassword(@Field("old_pass") String old,
                                    @Field("new_pass") String new_pass,
                                    @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_app_feature")
    Call<FeatureModel> GetAppFeature();

}
