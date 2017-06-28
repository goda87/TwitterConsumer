package es.goda87.twitterconsumer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.goda87.twitterconsumer.R;
import es.goda87.twitterconsumer.model.TimeLineItem;
import es.goda87.twitterconsumer.services.UserTimeLine;
import es.goda87.twitterconsumer.ui.viewadapters.TimeLineItemAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by goda87 on 4/06/17.
 */
public class TwitterTimelineFragment extends Fragment {
    private final int layoutId = R.layout.fragment_twitter_timeline;

    @BindView(R.id.tweets_list)
    RecyclerView layoutTweetsList;

    TimeLineItemAdapter adapter;
    Observable<Long> o;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        layoutTweetsList.setLayoutManager(llm);

        adapter = new TimeLineItemAdapter();
        Disposable disposable = adapter.getItemClicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TimeLineItem>() {
                    @Override
                    public void accept(TimeLineItem timeLineItem) throws Exception {

                    }
                });

        o = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Long> observableEmitter) throws Exception {
                observableEmitter.onNext(0L);
                layoutTweetsList.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int lastPosition = manager.findLastVisibleItemPosition();
                        int itemCount = manager.getItemCount();

                        if (itemCount - lastPosition < 5) {
                            RecyclerView.Adapter adapter = recyclerView.getAdapter();
                            Long lastItem = adapter.getItemId(adapter.getItemCount() - 1);
                            observableEmitter.onNext(lastItem);
                        }

                    }
                });
            }
        }).distinct();
        layoutTweetsList.setAdapter(adapter);

        Observable<List<TimeLineItem>> op = UserTimeLine.paginatedThings(o);
        op.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TimeLineItem>>() {
                    @Override
                    public void accept(@NonNull List<TimeLineItem> timeLineItems) throws Exception {
                        adapter.addItems(timeLineItems);
                    }
                });

        return view;
    }
}
