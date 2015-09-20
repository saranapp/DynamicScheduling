package targetSystem;

import java.util.Date;
import java.util.HashMap;
import scheduler.TargetRequestComponent;
import utility.Utility;


public class TargetSystem {

	public int TargetSystemID;

	public HashMap<String, MerchantTimer> hmap;
	public static int maxRequestsPerMerchantPerHour = 10;

	public void postRequest(TargetRequestComponent req) {
		Utility.appendToOutputFile("Message from TargetSystem: " + TargetSystemID);
		req.print();
		try {
			hmap.get(req.MERCHANT_ID).currentSize++;
		} catch (NullPointerException npe) {
			hmap.put(req.MERCHANT_ID, new MerchantTimer());
			hmap.get(req.MERCHANT_ID).currentSize++;
		}
	}

	public boolean checkIfARecordCanBeInsertedForMerchantID(String Merchant_ID) {
		boolean returnValue = false;
		long timeLeftForTimerReset;
		try {
			try {
				timeLeftForTimerReset = Math.abs((new Date()).getTime() - hmap.get(Merchant_ID).stopClock.getTime());
			} catch (NullPointerException npe) {
				return true;
			}
			if (this.hmap.get(Merchant_ID).currentSize <= 10) {
				if (timeLeftForTimerReset < 3600000) //3600000 corresponds to 1 Hour in milliseconds
					returnValue = true;
				else
					returnValue = false;
			}
			else {
				if (timeLeftForTimerReset > 3600000) {
					this.hmap.put(Merchant_ID, new MerchantTimer());
					returnValue = true;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in checkIfARecordCanBeInsertedForMerchantID:" + e.toString());
		}
		return returnValue;
	}

	public TargetSystem(int id) {
		TargetSystemID = id;
		hmap = new HashMap<String, MerchantTimer>();
	}
}
