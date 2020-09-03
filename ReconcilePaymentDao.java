import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mayank
 * @ReconsileDaoClass Provides Method to 
 *	1. Map the existing payment from payment table to order table
 */
public class ReconcilePaymentDao {

	/**
	 * Declaring Connection object
	 */
	Connection connect;
	
	
	/**
	 * Default Constructor -  Establishing the Connection through JDBCConnector Class object
	 */
	public ReconcilePaymentDao() throws UserDefinedSQLException {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * Parameterized Constructor -  Establishing the Connection object
	 * @param connect
	 */
	public ReconcilePaymentDao(Connection connect) {
		super();
		this.connect = connect;
	}



	/**
	 * reconcilePayments -  Method to Map the existing payment from payment table to order table
	 * 
	 */
	public void reconcilePayments() throws UserDefinedSQLException {
		ArrayList<Customer> listOfCustomers = new ArrayList<Customer>();

		if (connect != null) {
			Statement statement = null;
			try {
				statement = connect.createStatement();
				ResultSet resultSet = null;

				// Executing the Query to fetch the payment details into resultSet
				resultSet = statement.executeQuery("SELECT * FROM payments order by customernumber, paymentDate asc;");

				// passing the resultset to @getCustomerPayment Method to organized the detail
				// and return into list of customers.
				listOfCustomers = getCustomerPayment(resultSet);

				// Iterating through the customer list
				for (Customer customer : listOfCustomers) {
					
					// Executing the Query to fetch the order details for given customer into resultSet
					resultSet = statement.executeQuery(
							"Select od.orderNumber, sum(od.quantityOrdered*od.priceEach) as total, o.orderdate from orderdetails as od\r\n"
									+ "Inner join orders as o on o.orderNumber = od.orderNumber\r\n"
									+ "where o.customerNumber = " + customer.getCustomerNumber()
									+ " group by o.orderNumber, o.orderDate;");

					ArrayList<Order> listOfOrders = new ArrayList<Order>();

					// Iterating through result set and adding the order details into list of
					// orders.
					while (resultSet.next()) {
						Order order = new Order(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getDate(3));
						listOfOrders.add(order);
					}

					// and finally, allocating the order list to the customer.
					customer.setListoforders(listOfOrders);

					// method to Map payment from List of Payments to orders from List of order of
					// the given customer.
					mapOrdertoPayment(customer);

				}
			} catch (SQLException e) {
				// Throwing User Defined SQL Exception with error message
				throw new UserDefinedSQLException(
						"SQL Connection Error : Please try reconnecting again.\n" + e.getMessage());
			}

		}
	}


	/**
	 * @Method : mapOrderToPayment -  Method to Map the existing payment from payment table to order table for provide customer
	 * @param : customer
	 * @throws UserDefinedSQLException 
	 */
	public void mapOrdertoPayment(Customer customer) throws UserDefinedSQLException {
		Statement statement = null;

		// Creating and pointing the list of the ListofOrders of the customer
		ArrayList<Order> unMapped = new ArrayList<Order>();
		unMapped = customer.getListoforders();

		// Iterating through list of payments done by the customer
		for (Payment payment : customer.getListofpayments()) {

			float total = 0;
			String checkNumber = payment.getCheckNumber();

			/**
			 * @Method: checkMapping - method to check if mapping between order(s) and
			 *          payment check number exists
			 * @return: List of order with mapping to the check number
			 */
			ArrayList<Order> toBeMapped = checkMapping(payment, unMapped, total);
			if (!toBeMapped.isEmpty()) {
				String partQuery = "where orderNumber in (";

				// creating a substring of query with all the order number to be updated
				for (Order order : toBeMapped) {
					partQuery = partQuery.concat(order.getOrderId() + ",");
				}
				int last = partQuery.lastIndexOf(",");
				partQuery = partQuery.substring(0, last).concat(")");

				try {
					statement = connect.createStatement();

					// Executing the Query to update the order details for given customer with checkNumber
			
					int resultSet = statement.executeUpdate("Update orders set checkNumber = '" + checkNumber + "' "
							+ partQuery + " and customerNumber = " + customer.getCustomerNumber() + ";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new UserDefinedSQLException(
							"SQL Connection Error : Please try reconnecting again.\n" + e.getMessage());
				}
				unMapped.removeAll(toBeMapped);
			}
		}
	}
	
	
	/**
	 * @Method : checkMapping -  Method to check if any mapping existing between order(s) and the payment.
	 * @param : payment
	 * @param : unMappedOrders
	 * @param : total
	 * @throws UserDefinedSQLException 
	 */
	public ArrayList<Order> checkMapping(Payment payment, ArrayList<Order> unMappedOrders, float total) {
		ArrayList<Order> toBeMapped = new ArrayList<Order>();

		// Formating of float (to keep it to one decimal place, easy to compare.
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(1);
		float temp = total;
		float amounttocompare = Float.parseFloat(df.format(payment.getAmount()));
		float amount = payment.getAmount();

		// Iterating through the unmapped orders
		Iterator<Order> it = unMappedOrders.iterator();
		while (it.hasNext()) {
			Order order = it.next();

			// checking if order date is before or on same date as payment date.
			if (order.getOrderdate().compareTo(payment.getPaymentDate()) < 0
					|| order.getOrderdate().compareTo(payment.getPaymentDate()) == 0) {
				
				// Formating of float (to keep it to one decimal place, easy to compare.
				float orderamount = order.getTotalAmount();
				total = temp + orderamount;
				float totaltocompare = Float.parseFloat(df.format(total));

				// checking if order amount is equal to payment amount
				if (orderamount == amount) {
					toBeMapped.clear();
					toBeMapped.add(order);
					break;
				}

				// checking if total order amount current and previous orders is equal to payment amount
				else if (total == amount || totaltocompare == amounttocompare) {
					toBeMapped.add(order);
					break;
				}
				
				// go through the next orders
				else {
					
					// calling method recursively to check mapping with next order
					if (temp == 0) {
						unMappedOrders.remove(order);
						toBeMapped.add(order);
						ArrayList<Order> returnlist = checkMapping(payment, unMappedOrders, total);
						
						// if returnlist is empty
						if (returnlist.isEmpty()) {
							toBeMapped.remove(order);
						} else {
							toBeMapped.addAll(returnlist);
						}
						break;
					} else {
						unMappedOrders.remove(order);
						toBeMapped.add(order);
						ArrayList<Order> returnlist = checkMapping(payment, unMappedOrders, total);
						
						// if returnlist is empty
						if (returnlist.isEmpty()) {
							toBeMapped.addAll(checkMapping(payment, unMappedOrders, orderamount));
						} else {
							toBeMapped.addAll(returnlist);
						}
						break;
					}
				}
			}
		}

		// return the list of order which need to be mapped to the payment
		return toBeMapped;
	}
	
	
	/**
	 * @Method : getcustomer mapping -  return the details received in result set in organised way of list of customer .
	 * @param : resultset
	 * 
	 */
	public ArrayList<Customer> getCustomerPayment(ResultSet resultSet) {
		ArrayList<Customer> listOfCustomers = new ArrayList<Customer>();
		Customer customer = new Customer();
		ArrayList<Payment> listofPayments = new ArrayList<Payment>();

		try {
			while (resultSet.next()) {

				// checking if next payment is of same customer
				if (customer != null && customer.getCustomerNumber() == resultSet.getInt(1)) {
					
					float amount = resultSet.getFloat(4);
					Payment payment = new Payment(resultSet.getString(2), amount, resultSet.getDate(3));
					listofPayments.add(payment);
				}
				// if next payment is of different customer
				else {
					customer = new Customer();
					customer.setCustomerNumber(resultSet.getInt(1));
					listofPayments = new ArrayList<Payment>();
					float amount = resultSet.getFloat(4);
					Payment payment = new Payment(resultSet.getString(2), amount, resultSet.getDate(3));
					listofPayments.add(payment);
					
					// adding payment to list of payments for a customer
					customer.setListofpayments(listofPayments);
					
					// adding customer to list of customer
					listOfCustomers.add(customer);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// returning list of customers
		return listOfCustomers;
	}
	
	

}
