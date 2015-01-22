package erlangga.com.ribbit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by erlanggagama on 21/01/15.
 */
public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "jOP7KO7FXf8iCoTEq98s4xOxJTjS7hlhiiafKIox", "QFx0PoryYzU7J7hSGvAQLabHQpRwa4o77M8NrWby");



    }

}
