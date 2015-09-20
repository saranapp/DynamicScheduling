package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.PriorityQueue;
import scheduler.InputRequest;
import scheduler.TargetSystemRequest;
import targetSystem.TargetSystem;
import utility.Utility;
import dataStructure.IndexQueueMapper;


public class Main {

	public static int totalPriorities = 5;
	public static int TargetSystemCount = 5;

	public static void main(String[] args) {
		PriorityQueue<TargetSystemRequest>[] queues = initQueues();
		TargetSystem[] targetSystems = initTargetSystems();
		Utility.refreshOutputFile();
		try {
			Thread thread1 = new Thread() {

				public void run() {
					enqueuer(queues);
				}
			};

			Thread thread2 = new Thread() {

				public void run() {
					disBatcher(queues, targetSystems);
				}
			};

			thread1.start();
			Thread.sleep(5000);
			thread2.start();

			thread1.join();
			thread2.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}



	public static void enqueuer(PriorityQueue<TargetSystemRequest>[] queues) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader("C://Users//Maruthys//Desktop//AmazonHackathon//input1.txt"));
			String line;
			while (true) {
				if ((line = rd.readLine()) == null)
					break;

				InputRequest req = new InputRequest(line.split(",")[0], line.split(",")[1], line.split(",")[2], line.split(",")[3], line.split(",")[4], new Object());

				PriorityQueue<TargetSystemRequest> requiredQueue;
				removeFromExistingQueue(req);
				requiredQueue = IndexQueueMapper.indexQueueMap.get(req.ITEM_ID + "~" + req.index);
				if (requiredQueue == null) {
					IndexQueueMapper.indexQueueMap.put(req.ITEM_ID + "~" + req.index, queues[getPriority(req.PRIORITY) - 1]);
					requiredQueue = IndexQueueMapper.indexQueueMap.get(req.ITEM_ID + "~" + req.index);
				}
				boolean inserted = false;


				Iterator<TargetSystemRequest> it = requiredQueue.iterator();
				while (it.hasNext()) {
					TargetSystemRequest TSR = it.next();
					if (TSR.index.equals(req.index)) {
						if (!TSR.TRC.hasReachedPayLoadLimit()) {
							TSR.insertRequest(req);
							inserted = true;
						}
						else {
							TSR = new TargetSystemRequest(req);
							requiredQueue.add(TSR);
							inserted = true;
						}
						break;
					}
				}

				if (!inserted)
					requiredQueue.add(new TargetSystemRequest(req));
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	private static void removeFromExistingQueue(InputRequest req) {
		try {
			//search and remove from other priority queues
			PriorityQueue<TargetSystemRequest> toBeUpdatedQueue = IndexQueueMapper.indexQueueMap.get(req.ITEM_ID + "~" + req.index);
			Iterator<TargetSystemRequest> it = toBeUpdatedQueue.iterator();
			while (it.hasNext()) {
				TargetSystemRequest TSR = it.next();
				if (TSR.index.equals(req.index)) {
					TSR.deleteRequest(req);
					break;
				}
			}
		} catch (NullPointerException npe) {
			//in case HashMap does not have the requested index. That is, req.index is a new index 
		}

	}



	public static int getPriority(String PRIORITY) {
		int returnValue = 0;
		switch (PRIORITY) {
			case "HIGHEST":
				returnValue = 1;
				break;
			case "HIGH":
				returnValue = 2;
				break;
			case "NORMAL":
				returnValue = 3;
				break;
			case "LOW":
				returnValue = 4;
				break;
			case "LOWEST":
				returnValue = 5;
				break;
		}
		return returnValue;
	}


	public static void disBatcher(PriorityQueue<TargetSystemRequest>[] queues, TargetSystem[] targetSystems) {
		while (!isEmpty(queues)) {
			long minTime = -1;
			int queueNo = -1;
			for (int i = 0; i < queues.length; i++) {
				PriorityQueue<TargetSystemRequest> queue = queues[i];

				if (queue.isEmpty())
					continue;

				if (queue.peek().TRC.isPayLoadListEmpty()) {
					queue.remove();
					continue;
				}

				long thisQueueTime = queue.peek().timeLeft();
				if (minTime < 0 || thisQueueTime < minTime) {
					minTime = thisQueueTime;
					queueNo = i;
				}
			}

			PriorityQueue<TargetSystemRequest> queue = queues[queueNo];
			TargetSystemRequest TSR = queue.remove();
			for (int i = 0; i < targetSystems.length; i++) {
				String Merchant_ID = TSR.TRC.MERCHANT_ID;
				if (targetSystems[i].checkIfARecordCanBeInsertedForMerchantID(Merchant_ID)) {
					targetSystems[i].postRequest(TSR.TRC);
					break;
				}
			}
		}
	}


	private static PriorityQueue<TargetSystemRequest>[] initQueues() {
		PriorityQueue<TargetSystemRequest>[] queues = new PriorityQueue[totalPriorities];
		for (int i = 0; i < totalPriorities; i++)
			queues[i] = new PriorityQueue<TargetSystemRequest>();
		return queues;
	}

	private static TargetSystem[] initTargetSystems() {
		TargetSystem[] ts = new TargetSystem[TargetSystemCount];
		for (int i = 0; i < TargetSystemCount; i++)
			ts[i] = new TargetSystem(i);

		return ts;
	}

	private static boolean isEmpty(PriorityQueue<TargetSystemRequest>[] queues) {
		boolean returnValue = true;
		for (PriorityQueue<TargetSystemRequest> queue : queues)
			if (!queue.isEmpty()) {
				returnValue = false;
				break;
			}
		return returnValue;
	}

}
