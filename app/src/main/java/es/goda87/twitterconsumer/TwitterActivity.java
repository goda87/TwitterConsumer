package es.goda87.twitterconsumer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import es.goda87.twitterconsumer.ui.TwitterTimelineFragment;

/**
 * Created by goda87 on 4/06/17.
 */

public class TwitterActivity extends AppCompatActivity {
    private final int layoutId = R.layout.activity_twitter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        TwitterTimelineFragment firstFragment = new TwitterTimelineFragment();
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
    }
}
