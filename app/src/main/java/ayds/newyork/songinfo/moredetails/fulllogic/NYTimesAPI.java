package ayds.newyork.songinfo.moredetails.fulllogic;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface NYTimesAPI {
  Call<String> getArtistInfo(@Query("q") String artist);
}