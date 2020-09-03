import java.util.Date;


/**
 * @author Mayank 
 * Entity Representation of Payment Table
 */
public class Payment {

	/**
	 * @param: checkNumber
	 * @param: amount
	 * @param: paymentDate
	 */
	private String checkNumber;
	private float amount;
	private Date paymentDate;

	/**
	 * Default Constructor
	 */
	public Payment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param customerNumber
	 * @param checkNumber
	 * @param amount
	 */
	public Payment(String checkNumber, float amount, Date paymentDate) {
		super();
		this.checkNumber = checkNumber;
		this.amount = amount;
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * to set the checkNumber
	 * 
	 * @param checkNumber
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * to set the amount
	 * 
	 * @param amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * to set the paymentDate
	 * 
	 * @param paymentDate
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Override
	public String toString() {
		return "Payment [checkNumber=" + checkNumber + ", amount=" + amount + ", paymentDate=" + paymentDate + "]";
	}

}
