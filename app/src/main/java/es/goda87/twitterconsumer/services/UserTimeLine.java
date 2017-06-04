package es.goda87.twitterconsumer.services;


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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by goda87 on 4/06/17.
 */
public class UserTimeLine {
    public static Observable<List<TimeLineItem>> paginatedThings(final Observable<Long> onNextObservable) {

        Observable<List<TimeLineItem>> responseObservable =
                Observable.fromCallable(new Callable<List<TimeLineItem>>() {
                    @Override
                    public List<TimeLineItem> call() throws Exception {
                        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                        StatusesService statusesService = twitterApiClient.getStatusesService();
                        Call<List<Tweet>> call = statusesService.userTimeline(
                                TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId(),
                                null, 50, null, null,
                                false, false, false, false);
                        Response<List<Tweet>> response = call.execute();
                        List<TimeLineItem> items = new ArrayList<TimeLineItem>(response.body().size());

                        for (Tweet tweet : response.body()) {
                            items.add(new TweetTimeLineItem(tweet));
                        }

                        return items;
                    }
                });

        onNextObservable
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {





                    }
                });



        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<List<Tweet>> call = statusesService.userTimeline(
                TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId(),
                null, 50, null, null,
                false, false, false, false);

        Response<List<Tweet>> response = call.execute();


        return Observable.fromCallable(new Callable<List<TimeLineItem>>() {
            @Override
            public List<Tweet> call() throws Exception {
                return null;
            }
        });


//        return Observable.create(new Observable.onSubscribe<List<Tweet>>() {
//            @Override
//            public void call(final Subscriber<? super List<Tweet>> subscriber) {
//
//                onNextObservable.subscribe(new Observer<Long>() {
//                    int latestPage = -1;
//
//                    @Override
//                    public void onSubscribe(Disposable disposable) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        latestPage++;
//                        List<String> pageItems = new ArrayList<String>();
//                        for (int i = 0; i < 10; i++) {
//                            pageItems.add("page " + latestPage + " item " + i);
//                        }
//                        subscriber.onNext(pageItems);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//            }
//        });
    }

}