package es.goda87.twitterconsumer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.goda87.twitterconsumer.R;
import es.goda87.twitterconsumer.model.TimeLineItem;
import es.goda87.twitterconsumer.ui.viewadapters.TimeLineItemAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

/**
 * Created by goda87 on 4/06/17.
 */
public class TwitterTimelineFragment extends Fragment {
    private final int layoutId = R.layout.fragment_twitter_timeline;

    @BindView(R.id.tweets_list)
    RecyclerView layoutTweetsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        layoutTweetsList.setLayoutManager(llm);

        TimeLineItemAdapter adapter = new TimeLineItemAdapter();
        Disposable disposable = adapter.getItemClicks()
                .observeOn(AndroidSchedulers.mainThread()).su
                .subscribe(new Consumer<TimeLineItem>() {
                    @Override
                    public void accept(TimeLineItem timeLineItem) throws Exception {

                    }
                });
        layoutTweetsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<List<Tweet>> call = statusesService.userTimeline(
                TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId(),
                null, 50, null, null,
                false, false, false, false);

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                for (Tweet t : result.data) {
                    TextView textView = new TextView(getContext());
                    textView.setText(t.text);
                    layoutTweetsList.addView(textView);
                }
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });
    }
}
