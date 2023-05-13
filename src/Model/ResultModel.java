package Model;

public class ResultModel {
	private long durationInSeconds;
	private double mobsKilledPerSec;
	private long expPerSec;
	private long goldMesosPerSec;
	private long redMesosPerSec;
	private long totalMesosPerSec;

	public long getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(long durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	public double getMobsKilledPerSec() {
		return mobsKilledPerSec;
	}

	public void setMobsKilledPerSec(double mobsKilledPerSec) {
		this.mobsKilledPerSec = mobsKilledPerSec;
	}

	public long getExpPerSec() {
		return expPerSec;
	}

	public void setExpPerSec(long expPerSec) {
		this.expPerSec = expPerSec;
	}

	public long getGoldMesosPerSec() {
		return goldMesosPerSec;
	}

	public void setGoldMesosPerSec(long goldMesosPerSec) {
		this.goldMesosPerSec = goldMesosPerSec;
	}

	public long getRedMesosPerSec() {
		return redMesosPerSec;
	}

	public void setRedMesosPerSec(long redMesosPerSec) {
		this.redMesosPerSec = redMesosPerSec;
	}

	public long getTotalMesosPerSec() {
		return totalMesosPerSec;
	}

	public void setTotalMesosPerSec(long totalMesosPerSec) {
		this.totalMesosPerSec = totalMesosPerSec;
	}

	public void calculateTime(String duration) {
		System.out.println("duration  : " + duration);
		String minuteSeconds[] = duration.split(":");
		System.out.println("minuteSeconds[0] : " + minuteSeconds[0]);
		System.out.println("minuteSeconds.length: " + minuteSeconds.length);

		if (minuteSeconds[1].startsWith("0")) {
			minuteSeconds[1] = String.valueOf(minuteSeconds[1].charAt(1));
		}

		int minutes = Integer.parseInt(minuteSeconds[0]);
		int seconds = Integer.parseInt(minuteSeconds[1]);

		int totalSeconds;

		try {
			if (minuteSeconds.length > 1) {
				totalSeconds = (minutes * 60) + seconds;
			} else {
				totalSeconds = (Integer.parseInt(minuteSeconds[0]) * 60);
			}

			System.out.println("calc totalSeconds : " + totalSeconds);
			this.durationInSeconds = totalSeconds;
			System.out.println("this.durationInSeconds : " + this.durationInSeconds);
		} catch (NumberFormatException ex) { // handle your exception
			System.out.println(ex);
		}
	}

	public void calculateExp(String exp) {
		// TODO Auto-generated method stub
		this.expPerSec = (Long.valueOf(replaceComma(exp)) / this.getDurationInSeconds());
	}

	public void calculateRedMesos(String redMesos) {
		// TODO Auto-generated method stub
		this.redMesosPerSec = (Long.valueOf(replaceComma(redMesos)) / this.getDurationInSeconds());
	}

	public void calculateGoldMesos(String goldMesos) {
		// TODO Auto-generated method stub
		this.goldMesosPerSec = (Long.valueOf(replaceComma(goldMesos)) / this.getDurationInSeconds());

	}

	public void calculateNumberOfMobsKilled(String numberOfMobsKilled) {
		this.mobsKilledPerSec = (Double.valueOf(replaceComma(numberOfMobsKilled)) / this.getDurationInSeconds());
	}

	private String replaceComma(String input) {
		return input.replace(",", "");
	}
}
