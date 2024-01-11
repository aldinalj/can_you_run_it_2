package org.example.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Disk {

	private long total;
	private long used;
	private long free;

	public Disk(long total, long used, long free) {
		this.total = total;
		this.used = used;
		this.free = free;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public long getFree() {
		return free;
	}

	public void setFree(long free) {
		this.free = free;
	}

	public static Disk getDiskInfo() throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec("wmic logicaldisk get deviceid, freespace, size, volumename");
		process.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

		// Skip the header lines
		reader.readLine();
		reader.readLine();

		long total = 0, used = 0, free = 0;

		while ((line = reader.readLine()) != null) {
			try {
				// Assuming the output is in the format "DeviceID FreeSpace Size VolumeName"
				String[] tokens = line.trim().split("\\s+");
				if (tokens.length >= 3) {
					String deviceID = tokens[0];
					long freeSpace = Long.parseLong(tokens[1]);
					long size = Long.parseLong(tokens[2]);

					total += size;
					used += (size - freeSpace);
					free += freeSpace;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		reader.close();

		// Convert sizes to gigabytes
		long gigabyteFactor = 1024 * 1024 * 1024;
		total /= gigabyteFactor;
		used /= gigabyteFactor;
		free /= gigabyteFactor;

		return new Disk(total, used, free);
	}
}