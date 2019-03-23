package com.knexis.hotspot;

import java.util.ArrayList;

/**
 * Created by Nana Kwame Nyantakyi on 11/01/2018.
 * Purpose:
 */

public interface HotspotListener {
	/**
	 * Interface called when the scan method finishes. Network operations should not execute on UI thread
	 * @param clients
	 */
	void OnDevicesConnectedRetrieved(ArrayList<ConnectedDevice> clients);

	void OnHotspotStartResult(ConnectionResult result);
}
