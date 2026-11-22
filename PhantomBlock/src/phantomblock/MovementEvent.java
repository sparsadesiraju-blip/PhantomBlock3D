package phantomblock;

public class MovementEvent {
	private Student student;
	private String reason;
	private String origin;
	private String destination;
	private int startTime;
	private int duration;
	public MovementEvent(Student student, String reason , String origin , String destination , int startTime, int duration) {
		this.student = student;
		this.reason = reason;
		this.origin = origin;
		this.destination = destination;
		this.startTime = startTime;
		this.duration = duration;
	}
	public Student getStudent() {
		return student;
	}
	public String getReason() {
		return reason;
	}
	public String getOrigin() {
		return origin;
	}
	public String getDestination() {
		return destination;
	}
	public int getStartTime() {
		return startTime;
	}
	public int getDuration() {
		return duration;
	}
	public int getEndTime() {
		return startTime + duration;
	}
	
	

}
