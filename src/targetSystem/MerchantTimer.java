package targetSystem;

import java.sql.Timestamp;
import java.util.Date;


public class MerchantTimer {

	int currentSize;
	Timestamp stopClock;

	public MerchantTimer() {
		currentSize = 0;
		stopClock = new Timestamp(new Date().getTime());
	}
}
