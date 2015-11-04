package erlangga.com.ribbit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by erlanggagama on 22/01/15.
 */
public class FriendsFragment extends ListFragment {

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.friendsProgressBar);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.KEY_FRIENDS);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstant.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null) {
                    setProgressBar();
                    ParseUser user;
                    mFriends = friends;
                    String[] friendsArray = new String[mFriends.size()];


                    //looking for a match
                    for (int i = 0; i < mFriends.size(); i++) {
                        user = mFriends.get(i);

                        for (ParseUser friend : friends) {
                            if (user.getObjectId().equals(friend.getObjectId())) {
                                friendsArray[i] = user.getUsername();
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getListView().getContext(),
                        android.R.layout.simple_list_item_1,
                        friendsArray
                    );
                     setListAdapter(adapter);

                } else {
                    showDialog(e.getMessage());
                }
            }
        });

    }

    private void setProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.error_message_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
