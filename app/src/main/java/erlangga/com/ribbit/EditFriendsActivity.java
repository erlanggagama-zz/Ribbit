package erlangga.com.ribbit;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EditFriendsActivity extends ListActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @InjectView(R.id.editFriendsProgressBar)ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.KEY_FRIENDS);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                setProgressBar();
                if (e ==null) {
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];

                    int i = 0;

                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                    addFriendsCheckmark();
                } else {
                    Log.e(TAG, e.getMessage());
                    showDialog(e.getMessage());
                }
            }
        });

    }

    private void addFriendsCheckmark() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null) {
                    ParseUser user;
                    //looking for a match
                    for (int i = 0; i < mUsers.size(); i++) {
                        user = mUsers.get(i);

                        for (ParseUser friend : friends) {
                            if (user.getObjectId().equals(friend.getObjectId())) {
                                getListView().setItemChecked(i, true);
                            }
                        }

                    }


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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this)
                .setTitle(getString(R.string.error_message_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getListView().isItemChecked(position)) {
            mFriendsRelation.add(mUsers.get(position));

        } else {
            mFriendsRelation.remove(mUsers.get(position));

        }
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    showDialog(e.getMessage());
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
