package scheduler;

import java.util.Date;


public class TargetSystemRequest implements Comparable<TargetSystemRequest> {

	public TargetRequestComponent TRC;
	public long maxTime;
	public String index;

	public long timeLeft() {
		return Math.abs((new Date()).getTime() - maxTime);
	}

	public TargetSystemRequest(InputRequest req) {
		maxTime = (new Date()).getTime() + req.slaTime;
		index = req.index;
		TRC = new TargetRequestComponent(req.ITEM_ID, req.MERCHANT_ID, req.DATA_TYPE, req.MARKET_PLACE, req.PAYLOAD);
	}

	public void insertRequest(InputRequest req) {
		if (this.timeLeft() > req.slaTime)
			maxTime = req.slaTime;
		index = req.index;
		TRC.insertItem(req.ITEM_ID, req.PAYLOAD);
	}

	public void deleteRequest(InputRequest req) {
		TRC.removeItem(req.ITEM_ID);
	}



	@Override
	public int compareTo(TargetSystemRequest o) {
		return Long.compare(o.timeLeft(), this.timeLeft());
	}

}
