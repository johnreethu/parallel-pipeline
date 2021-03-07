/**
 * 
 */
package john_richard_msc.test_framework;

/**
 * @author richa
 * 
 */
public class Result {

	/**
	 * This to say if person is obesity
	 */
	private String status;
	
	/**
	 * This is to handle the BMI value
	 */
	private double bmi;
	
	/**
	 * This object hold the name of the person
	 */
	private String name;
	
	String getStatus() {
		return status;
	}

	void setStatus(String status) {
		this.status = status;
	}

	public double getBmi() {
		return bmi;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
