package es.goda87.twitterconsumer.services;


import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import es.goda87.twitterconsumer.model.TimeLineItem;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by goda87 on 4/06/17.
 */
public class UserTimeLine {
    public static Observable<List<TimeLineItem>> paginatedThings(final Observable<Long> onNextObservable) {
        return onNextObservable
                .observeOn(Schedulers.newThread())
                .map(new Function<Long, Response<List<Tweet>>>() {
                    @Override
                    public Response<List<Tweet>> apply(@NonNull Long untilId) throws Exception {
                        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                        StatusesService statusesService = twitterApiClient.getStatusesService();

                        Log.d("LAST ITEM", "CALL FOR ID: " + untilId);

                        Long id = null;
                        if (untilId != 0L) { id = untilId; }

                        Call<List<Tweet>> call = statusesService.userTimeline(
                                TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId(),
                                null, 50, null, id,
                                false, false, false, false);
                        return call.execute();
                    }
                }).map(new Function<Response<List<Tweet>>, List<TimeLineItem>>() {
                    @Override
                    public List<TimeLineItem> apply(@NonNull Response<List<Tweet>> response) throws Exception {

                        if (response.body() == null) {
                            return new ArrayList<TimeLineItem>();
                        }
                        List<TimeLineItem> items = new ArrayList<TimeLineItem>(response.body().size());

                        for (Tweet tweet : response.body()) {
                            items.add(new TweetTimeLineItem(tweet));
                            Log.d("ID", "ID: " + tweet.getId());
                        }
                        return items;
                    }
                });
    }

}