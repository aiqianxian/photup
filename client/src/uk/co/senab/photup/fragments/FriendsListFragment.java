package uk.co.senab.photup.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.co.senab.photup.FriendsAsyncTask;
import uk.co.senab.photup.FriendsAsyncTask.FriendsResultListener;
import uk.co.senab.photup.R;
import uk.co.senab.photup.listeners.OnFriendPickedListener;
import uk.co.senab.photup.model.Friend;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class FriendsListFragment extends SherlockDialogFragment implements FriendsResultListener, OnItemClickListener {

	private final ArrayList<Friend> mFriends = new ArrayList<Friend>();
	private final ArrayList<Friend> mDisplayedFriends = new ArrayList<Friend>();

	private Set<Friend> mExcludedFriends;

	private ListView mListView;
	private BaseAdapter mAdapter;

	private OnFriendPickedListener mPickedFriendListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(STYLE_NO_TITLE, 0);

		mAdapter = new ArrayAdapter<Friend>(getActivity(), android.R.layout.simple_list_item_1, mDisplayedFriends);

		if (mDisplayedFriends.isEmpty()) {
			new FriendsAsyncTask(getActivity(), this).execute();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container, false);

		mListView = (ListView) view.findViewById(R.id.lv_friends);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);

		return view;
	}

	public void onFriendsLoaded(List<Friend> friends) {
		mFriends.clear();
		mFriends.addAll(friends);
		updateFriends();
	}

	public void setOnFriendPickedListener(OnFriendPickedListener listener) {
		mPickedFriendListener = listener;
	}

	public void setExcludedFriends(Set<Friend> excludeSet) {
		mExcludedFriends = excludeSet;
		updateFriends();
	}

	private void updateFriends() {
		mDisplayedFriends.clear();
		
		if (null != mExcludedFriends && !mExcludedFriends.isEmpty()) {
			for (Friend friend : mFriends) {
				if (!mExcludedFriends.contains(friend)) {
					mDisplayedFriends.add(friend);
				}
			}
		} else {
			mDisplayedFriends.addAll(mFriends);
		}

		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Friend friend = (Friend) parent.getItemAtPosition(position);

		if (null != mPickedFriendListener) {
			mPickedFriendListener.onFriendPicked(friend);
		}

		dismiss();
	}

}
