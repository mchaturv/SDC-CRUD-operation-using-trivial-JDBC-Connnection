
import java.sql.Connection;
import java.util.ArrayList;


/**
 * @author Mayank
 * @ServiceClass : Define Method to call ReconsileDao method and paymentDao method to map the existing payments to orders 
 * and retrieving unknown payment and unpaid orders
 */
public class PaymentManagement {

	
	/**
	 * Default Constructor
	 */ 
	public PaymentManagement() {
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * Calls ReconsilePayment from reconsilepaymentDao to map existing payments to
	 * order table
	 */
	public void reconcilePayments(Connection database) {
		ReconcilePaymentDao reconsiledao = new ReconcilePaymentDao(database);
		try {
			reconsiledao.reconcilePayments();
		} catch (UserDefinedSQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Calls payorder method from paymentDao class to pay the unpaid order
	 */
	public boolean payOrder(Connection database, float amount, String cheque_number, ArrayList<Integer> orders) {
		boolean response = false;
		PaymentQueryDao paymentdao = new PaymentQueryDao(database);
		try {
			response = paymentdao.payOrder(amount, cheque_number, orders);
		} catch (UserDefinedSQLException e) {
			// TODO Auto-generated catch block
			// System.out.println(e.getMessage());
			response = false;
		}
		return response;
	}
	
	/**
	 * Calls unpaidOrders method from paymentDao to known the unpaid orders
	 */
	public ArrayList<Integer> unpaidOrders(Connection database) {
		ArrayList<Integer> listOfOrder = new ArrayList<Integer>();
		PaymentQueryDao paymentdao = new PaymentQueryDao(database);
		try {
			listOfOrder = paymentdao.unpaidOrders();
		} catch (UserDefinedSQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			listOfOrder = new ArrayList<Integer>();
		}
		return listOfOrder;
	}
	
	/**
	 * Calls unknownPayments method from paymentDao to known the unknown payment
	 */
	public ArrayList<String> unknownPayments(Connection database) {
		ArrayList<String> listOfPayments = new ArrayList<String>();
		PaymentQueryDao paymentdao = new PaymentQueryDao(database);
		try {
			listOfPayments = paymentdao.unknownPayments();
		} catch (UserDefinedSQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			listOfPayments = new ArrayList<String>();
		}
		return listOfPayments;
	}
	
}
