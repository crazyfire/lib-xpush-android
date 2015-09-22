package io.xpush.sampleChat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.socketio.client.Ack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.xpush.chat.ApplicationController;
import io.xpush.sampleChat.R;
import io.xpush.chat.models.XPushUser;
import io.xpush.sampleChat.adapters.UserListAdapter;

public class SearchUserFragment extends Fragment {

    private static final String TAG = SearchUserFragment.class.getSimpleName();

    private List<XPushUser> mXpushUsers = new ArrayList<XPushUser>();

    private RecyclerView.Adapter mAdapter;

    private Activity mActivity;

    private String mUsername;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new UserListAdapter(activity, mXpushUsers);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mUsername = ApplicationController.getInstance().getXpushSession().getId();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListView(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_users, container, false);
    }

    private void displayListView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        getUsers();
    }

    public void getUsers(){
        JSONObject jsonObject = new JSONObject();
        JSONObject column = new JSONObject();

        try {
            column.put( "U", true );
            column.put( "DT", true );
            column.put( "A", true );
            column.put( "_id", false );

            jsonObject.put("query", new JSONObject().put("A", getString(R.string.app_id)) );
            jsonObject.put("options", new JSONObject() );
            jsonObject.put("column", column );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApplicationController.getInstance().getClient().emit("user-query", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];

                Log.d(TAG, response.toString());
                if (response.has("result")) {
                    try {
                        JSONArray result = (JSONArray) response.getJSONObject("result").getJSONArray("users");

                        for (int inx = 0; inx < result.length(); inx++) {
                            JSONObject json = (JSONObject) result.get(inx);
                            Log.d(TAG, json.toString());

                            XPushUser xpushUser = new XPushUser();

                            xpushUser.setId(json.getString("U"));

                            if (json.has("DT") && !json.isNull("DT")) {
                                Object obj = json.get("DT");
                                JSONObject data = null;
                                if (obj instanceof JSONObject) {
                                    data = (JSONObject) obj;
                                } else if (obj instanceof String) {
                                    data = new JSONObject((String) obj);
                                }

                                if (data.has("NM")) {
                                    xpushUser.setName(data.getString("NM"));
                                }
                                if (data.has("MG")) {
                                    xpushUser.setMessage(data.getString("MG"));
                                }
                                if (data.has("I")) {
                                    xpushUser.setImage(data.getString("I"));
                                }
                            } else {
                                xpushUser.setName(json.getString("U"));
                            }

                            mXpushUsers.add(xpushUser);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
