package project.yeoeun.bdc.network;

import project.yeoeun.bdc.util.Constants;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class ServiceGenerator {

    private static ServiceGenerator instance;

    private ServiceGenerator() {
    }

    public static ServiceGenerator getInstance() {
        if (instance == null) {
            instance = new ServiceGenerator();
        }
        return instance;
    }

    private <T> T createService(Class<T> serviceClass) {
        return new Retrofit.Builder()
                .baseUrl(Constants.FUSION_TABLE_QUERY_URL)
//                .baseUrl(Constants.LOCAL_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(serviceClass);
    }

    public FusionTableService getFusionTableService() {
        return createService(FusionTableService.class);
    }
}
