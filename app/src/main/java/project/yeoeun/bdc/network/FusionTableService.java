package project.yeoeun.bdc.network;

import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.vo.FusionTableInfo;
import project.yeoeun.bdc.vo.FusionTableList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FusionTableService {
    @GET(Constants.FUSION_TABLE_ID)
    Call<FusionTableInfo> getFusionTable(@Query("key") String key);

    @GET(Constants.FUSION_TABLE_ID)
    Call<FusionTableList> getRowFusionTable(
            @Query("sql") String sql
            ,@Query("key") String key);


}
