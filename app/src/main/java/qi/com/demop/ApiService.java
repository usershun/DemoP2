package qi.com.demop;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("json")
    Observable<PingBean> ping();
    //http://58.220.51.10:8080/GameData/SaveSql
    @POST("GameData/SaveSql")
    Observable<TureBean> ture(@Query("regionName") String regionName,@Query("city") String city,@Query("query") String query,
                              @Query("org") String org,@Query("dst") String dst,@Query("min") String min,@Query("max") String max,
                              @Query("loss") String loss,@Query("date") String date

                              );
}
