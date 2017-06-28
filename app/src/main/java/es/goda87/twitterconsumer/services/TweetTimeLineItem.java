package es.goda87.twitterconsumer.services;

import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.goda87.twitterconsumer.model.TimeLineItem;

/**
 * Created by goda87 on 4/06/17.
 */

public class TweetTimeLineItem implements TimeLineItem {
    private static final SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);

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
        return tweet.getId();
    }

    @Override
    public Calendar getDate() { // Mon Jun 12 09:30:59 +0000 2017
        Calendar c = null;
        try {
            Date d = parser.parse(tweet.createdAt);
            c = Calendar.getInstance();
            c.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }
}
