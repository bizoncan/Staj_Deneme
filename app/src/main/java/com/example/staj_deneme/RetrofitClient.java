    package com.example.staj_deneme;

    import com.example.staj_deneme.InterFaces.ApiDeneme;
    import com.example.staj_deneme.InterFaces.ErrorInterface;
    import com.example.staj_deneme.InterFaces.ImageApiInterface;
    import com.example.staj_deneme.InterFaces.MachineApiInterface;
    import com.example.staj_deneme.InterFaces.MachinePartsApiInterface;
    import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
    import com.example.staj_deneme.InterFaces.UserApiInterface;
    import com.example.staj_deneme.InterFaces.WorkInterface;
    import com.example.staj_deneme.InterFaces.WorkOrderInterface;
    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;

    import okhttp3.OkHttpClient;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;

    public class RetrofitClient {
        private static final String BASE_URL="https://10.10.82.247:7296/";
        private static Retrofit retrofit=null;
        public static ApiDeneme getApiService(){
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(ApiDeneme.class);
        }
        public static RecieveNotificationInterface getApiServiceNotification(){
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(RecieveNotificationInterface.class);
        }

        public static MachineApiInterface getApiServiceMachine(){
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(MachineApiInterface.class);
        }
        public static ErrorInterface getApiServiceError(){
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")  // Burada tarih formatını belirleyebilirsiniz
                    .create();
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit.create(ErrorInterface.class);
        }
        public  static MachinePartsApiInterface getApiServiceMachinePart(){
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(MachinePartsApiInterface.class);
        }
        public static UserApiInterface getApiServiceUser(){
            if(retrofit==null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(UserApiInterface.class);
        }
        public  static ImageApiInterface getApiImageService(){
            if (retrofit == null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(ImageApiInterface.class);
        }
        public  static WorkOrderInterface getApiWorkOrderService(){
            if (retrofit == null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(WorkOrderInterface.class);
        }
        public  static WorkInterface getApiWorkService(){
            if (retrofit == null){
                OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(WorkInterface.class);
        }
    }
