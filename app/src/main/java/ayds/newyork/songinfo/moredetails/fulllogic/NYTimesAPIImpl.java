package ayds.newyork.songinfo.moredetails.fulllogic;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


class NYTimesAPIImpl {
        public static NYTimesAPI createNYTimesAPI() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.nytimes.com/svc/search/v2/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            return retrofit.create(NYTimesAPI.class);
        }
}