package scheduler;

import java.io.FileReader;
import java.util.Properties;


public class InputRequest {

	public String ITEM_ID;
	public String MERCHANT_ID;
	public String MARKET_PLACE;
	public String PRIORITY;
	public String DATA_TYPE;
	public Object PAYLOAD;

	public long slaTime;
	public String index;

	public InputRequest(String ITEM_ID, String MERCHANT_ID, String MARKETPLACE_ID, String PRIORITY, String DATA_TYPE, Object PAYLOAD) {
		this.ITEM_ID = ITEM_ID;
		this.MERCHANT_ID = MERCHANT_ID;
		this.MARKET_PLACE = MARKETPLACE_ID;
		this.PRIORITY = PRIORITY;
		this.DATA_TYPE = DATA_TYPE;
		this.PAYLOAD = PAYLOAD;
		slaTime = getSLATimeInMinutes(PRIORITY) * 60 * 1000;//milliseconds
		index = MERCHANT_ID + "~" + DATA_TYPE + "~" + MARKETPLACE_ID;
	}

	private long getSLATimeInMinutes(String PRIORITY) {
		long returnValue = 0;
		try {
			FileReader reader = new FileReader("C://Users//Maruthys//Desktop//AmazonHackathon//configSLA.properties");
			Properties prop = new Properties();
			prop.load(reader);
			returnValue = Integer.parseInt(prop.getProperty(PRIORITY));
		} catch (Exception e) {
			System.out.println("File Read Exception: " + e.toString());
		}
		return returnValue;
	}

}
