package org.example.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RAM {

	private long memory; // Memory in bytes

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public RAM(long memory) {
		this.memory = memory;
	}

	public static RAM getRAMinfo() throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec("wmic memorychip get capacity");
		process.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

		// Skip the header lines
			reader.readLine();


		long memoryInBytes = 0;

		while ((line = reader.readLine()) != null) {
			try {
				// Assuming the output is in the format "Capacity"
				String[] tokens = line.trim().split("\\s+");
				if (tokens.length >= 1) {
					memoryInBytes += Long.parseLong(tokens[0]);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		reader.close();

		// Convert bytes to gigabytes
		long memoryInGB = memoryInBytes / (1024 * 1024 * 1024);

		return new RAM(memoryInGB);
	}
}
