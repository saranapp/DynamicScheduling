package scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import utility.Utility;

public class TargetRequestComponent {


	public String MERCHANT_ID;
	public String DATA_TYPE;
	public String MARKET_PLACE;
	private HashMap<String, Object> payLoadList;
	public static int payLoadLimit = 10000;

	public boolean hasReachedPayLoadLimit() {
		return payLoadList.size() > payLoadLimit;
	}

	public TargetRequestComponent(String ITEM_ID, String MERCHANT_ID, String DATA_TYPE, String MARKET_PLACE, Object payLoad) {

		this.MERCHANT_ID = MERCHANT_ID;
		this.DATA_TYPE = DATA_TYPE;
		this.MARKET_PLACE = MARKET_PLACE;
		this.payLoadList = new HashMap<String, Object>();
		payLoadList.put(ITEM_ID, payLoad);
	}

	public void insertItem(String ITEM_ID, Object payLoad) {
		payLoadList.put(ITEM_ID, payLoad);
	}

	public void removeItem(String ITEM_ID) {
		payLoadList.remove(ITEM_ID);
	}

	public boolean isPayLoadListEmpty() {
		return this.payLoadList.isEmpty();
	}

	public void print() {
		Utility.appendToOutputFile("MERCHANT_ID: " + this.MERCHANT_ID);
		Utility.appendToOutputFile("DATA_TYPE: " + this.DATA_TYPE);
		Utility.appendToOutputFile("MARKET_PLACE: " + this.MARKET_PLACE);
		Utility.appendToOutputFile("---ITEM --- PAYLOAD---");
		Iterator it = payLoadList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Utility.appendToOutputFile(pair.getKey() + " = " + pair.getValue());
		}
		Utility.appendToOutputFile("_________________________");
	}
}
