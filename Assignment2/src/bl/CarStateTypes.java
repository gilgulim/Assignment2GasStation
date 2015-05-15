package bl;

public enum CarStateTypes {
	DISPACHER(0),
	AUTOWASH(1),
	MANUALWASH(2),
	FUEL(3),
	LEFTSTATION(4);
	
	private final int code;
	
	CarStateTypes (int code){
		this.code=code;
	}
}
