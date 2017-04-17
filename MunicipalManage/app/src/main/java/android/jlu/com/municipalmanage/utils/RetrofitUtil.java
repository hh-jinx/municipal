package android.jlu.com.municipalmanage.utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RetrofitUtil {
    @POST("clientLogin.action")
    @FormUrlEncoded
    Call<String> Login(@Field("manager.name") String username, @Field("manager.pass") String password );
}
