package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Utility {

	public static void appendToOutputFile(String data) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("C://Users//Maruthys//Desktop//AmazonHackathon//output.txt", true));
			bw.write(data);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
		}
	}

	public static void refreshOutputFile() {
		try {
			File file = new File("C://Users//Maruthys//Desktop//AmazonHackathon//output.txt");
			file.delete();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
