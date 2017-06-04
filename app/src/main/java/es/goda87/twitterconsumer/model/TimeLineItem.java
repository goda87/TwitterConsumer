package es.goda87.twitterconsumer.model;

/**
 * Created by goda87 on 4/06/17.
 */
public interface TimeLineItem {
    CharSequence getAuthorName();
    CharSequence getAuthorTag();
    CharSequence getText();
    CharSequence getProfilePicture();
    Long getId();
}
