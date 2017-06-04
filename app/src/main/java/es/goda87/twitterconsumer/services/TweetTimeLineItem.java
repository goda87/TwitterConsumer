package es.goda87.twitterconsumer.services;

import com.twitter.sdk.android.core.models.Tweet;

import es.goda87.twitterconsumer.model.TimeLineItem;

/**
 * Created by goda87 on 4/06/17.
 */

public class TweetTimeLineItem implements TimeLineItem {

    private Tweet tweet;

    public TweetTimeLineItem(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public CharSequence getAuthorName() {
        return tweet.user.name;
    }

    @Override
    public CharSequence getAuthorTag() {
        return tweet.user.screenName;
    }

    @Override
    public CharSequence getText() {
        return tweet.text;
    }

    @Override
    public CharSequence getProfilePicture() {
        return tweet.user.profileImageUrl;
    }

    @Override
    public Long getId() {
        return null;
    }
}
