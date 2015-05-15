package ui;

public class CarCell {
	private String fileName;
	private String label;
	
	public CarCell (String fileName, String label){
		this.fileName = fileName;
		this.label = label;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
