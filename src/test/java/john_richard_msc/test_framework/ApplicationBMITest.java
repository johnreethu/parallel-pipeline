/***
 * 
 */
package john_richard_msc.test_framework;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author richa
 *
 */
public class ApplicationBMITest {

	ApplicationBMI application;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		application = new ApplicationBMI();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		application = null;
	}

	@Test
	public void validCalculateBMItest() {
		double value = application.calculateBMI(78, 6.2);
		System.out.println("\n Valid Test : "+value);
		Assert.assertNotEquals(0.0, value);
	}
	
	@Test
	public void inValidCalculateBMItest() {
		double value = application.calculateBMI(0, 6.2);
		System.out.println("\n In Valid Test : "+value);
		Assert.assertEquals(0.0, value, 0.0);
	}

	@Test
	public void ValidBMIResult() {
		Result result = application.getBMIResult("Edwin", 78.0, 6.2);
		Assert.assertNotNull(result);
		Assert.assertEquals("Edwin", result.getName());
		Assert.assertNotEquals(0.0, result.getBmi());
		Assert.assertNotEquals("", result.getStatus());
	}
	
	@Test
	public void ValidateConvertToMeter() {
		double value = application.convertToMeter(6.2);
		Assert.assertNotEquals(0.0, value);
	}
	
	@Test
	public void ValidategetBMIStatus() {
		String value = application.getBMIStatus(22.5);
		Assert.assertEquals("normal", value);
	}
	
	@Test
	public void inValidgetBMIStatusTest() {
		String value = application.getBMIStatus(-1);
		Assert.assertTrue(value.startsWith("Invalid BMI Value"));
	}

}
