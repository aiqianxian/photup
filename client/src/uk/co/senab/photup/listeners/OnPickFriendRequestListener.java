package uk.co.senab.photup.listeners;

import java.util.Set;

import uk.co.senab.photup.model.Friend;


public interface OnPickFriendRequestListener {
	
	void onPickFriendRequested(OnFriendPickedListener listener, Set<Friend> excludeSet);

}
