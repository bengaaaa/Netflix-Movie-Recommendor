package movieApp;

import java.util.Comparator;

public class UComparator implements Comparator<UserPcc>{

	public int compare(UserPcc a, UserPcc b) {
		if ( a.userPcc > b.userPcc) {
			return -1;
		}
		if ( a.userPcc < b.userPcc) {
			return 1;
		}
		else {
			return 0;
		}

	}
}
