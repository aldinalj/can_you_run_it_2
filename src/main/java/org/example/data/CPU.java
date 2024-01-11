package org.example.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CPU {

	private double frequency;
	private int cores;

	public CPU(double frequency, int cores) {
		this.frequency = frequency;
		this.cores = cores;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	public static CPU getCPUinfo() throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec("wmic cpu get maxclockspeed, numberofcores");
		process.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

		// Skip the header lines
		reader.readLine();
		reader.readLine();

		double frequency = 0.0;
		int cores = 0;

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.trim().split("\\s+");

			if (tokens.length >= 2) {
				try {
					frequency = Double.parseDouble(tokens[0]);
					cores = Integer.parseInt(tokens[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}

		reader.close();
		return new CPU(frequency, cores);
	}
}
