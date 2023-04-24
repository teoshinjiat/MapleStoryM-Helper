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

}
