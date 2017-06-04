package es.goda87.twitterconsumer.ui.viewadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.goda87.twitterconsumer.R;
import es.goda87.twitterconsumer.model.TimeLineItem;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by goda87 on 4/06/17.
 */

public class TimeLineItemAdapter extends RecyclerView.Adapter<TimeLineItemAdapter.Holder> {

    private final List<TimeLineItem> items = new ArrayList<>();
    private final PublishSubject<TimeLineItem> onClickSubject = PublishSubject.create();

    public void addItems(List<TimeLineItem> newItems) {
        items.addAll(newItems);
    }

    public Observable<TimeLineItem> getItemClicks(){
        return onClickSubject;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline_item, parent, false);
        Holder pvh = new Holder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final TimeLineItem item = items.get(position);
        holder.authorName.setText(item.getAuthorName());
        holder.authorTag.setText(item.getAuthorTag());
        holder.text.setText(item.getText());

        //holder.authorPicture.set(item.getAuthorTag());  //TODO add picture

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.author_name) TextView authorName;
        @BindView(R.id.author_tag) TextView authorTag;
        @BindView(R.id.text) TextView text;
        @BindView(R.id.author_picture) ImageView authorPicture;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
