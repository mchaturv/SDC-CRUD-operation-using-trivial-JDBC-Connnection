import java.util.ArrayList;

/**
 * @author Mayank 
 * Entity Representation of customer table
 */
public class Customer {

	/**
	 * @param: customerNumber
	 * @param: listofpayments
	 * @param: listoforders
	 */
	private int customerNumber;
	private ArrayList<Payment> listofpayments;
	private ArrayList<Order> listoforders;

	/**
	 * Default Constructor
	 */
	public Customer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param customerNumber
	 * @param listofpayments
	 */
	public Customer(int customerNumber, ArrayList<Payment> listofpayments) {
		super();
		this.customerNumber = customerNumber;
		this.listofpayments = listofpayments;
	}

	/**
	 * @return the customerNumber
	 */
	public int getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * to set the customerNumber
	 * 
	 * @param customerNumber
	 */
	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * @return the listofpayments
	 */
	public ArrayList<Payment> getListofpayments() {
		return listofpayments;
	}

	/**
	 * to set the listofpayments
	 * 
	 * @param listofpayments
	 */
	public void setListofpayments(ArrayList<Payment> listofpayments) {
		this.listofpayments = listofpayments;
	}

	/**
	 * @return the listoforders
	 */
	public ArrayList<Order> getListoforders() {
		return listoforders;
	}

	/**
	 * to set the listoforders
	 * 
	 * @param listoforders
	 */
	public void setListoforders(ArrayList<Order> listoforders) {
		this.listoforders = listoforders;
	}

	@Override
	public String toString() {
		return "Customer [customerNumber=" + customerNumber + ", listofpayments=" + listofpayments + ", listoforders="
				+ listoforders + "]";
	}

}
