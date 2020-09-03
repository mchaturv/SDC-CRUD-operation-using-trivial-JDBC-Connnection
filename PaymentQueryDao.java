import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Mayank
 * @PaymentDaoClass Provides Method to 
 *	1. PayOrders
 *	2. fetch Unpaid orders
 *	3. fetch unknown Payments
 */
public class PaymentQueryDao {

	/**
	 * Declaring Connection object
	 */
	Connection connect;
	
	
	/**
	 * Default Constructor -  Establishing the Connection  Class object
	 */
	public PaymentQueryDao() throws UserDefinedSQLException {
		// TODO Auto-generated constructor stub
	
	}
	

	/**
	 * @param connect
	 * Constructor -  Establishing the Connection Class object
	 */
	public PaymentQueryDao(Connection connect) {
		super();
		this.connect = connect;
	}



	/**
	 * @param connect
	 * @param amount
	 * @param cheque_number
	 * @param orders
	 * @throws UserDefinedSQLException
	 */
	public boolean payOrder( float amount, String cheque_number, ArrayList<Integer> orders ) throws UserDefinedSQLException 
	{
		boolean valid = false;
		Customer customer = null;
		float total = 0;
		// check if connection is not null
		if (connect != null) {
			Statement statement = null;
			try {
				statement = connect.createStatement();
				ResultSet resultSet = null;

				// creating a substring of query
				String partQuery = "where orderNumber in (";
				for (int id : orders) {

					// Executing the Query to fetch the order details into resultSet
					resultSet = statement.executeQuery(
							"Select o.orderNumber, o.customerNumber, o.checkNumber,sum(od.quantityOrdered*od.priceEach) as total,\r\n"
									+ "o.orderdate from orderdetails as od\r\n"
									+ "Inner join orders as o on o.orderNumber = od.orderNumber \r\n"
									+ "where o.orderNumber = " + id + " ;");

					// checking if result set in not null
					if (resultSet != null) {

						// iterating the result set
						while (resultSet.next()) {

							// for the first result set or if the customer currently fetched is equal to
							// customer for previous result set
							if (customer == null || customer.getCustomerNumber() == resultSet.getInt(2)) {

								// check if check number is null
								if (resultSet.getString(3) == null) {
									total += resultSet.getFloat(4);
									DecimalFormat df = new DecimalFormat("#.##");
									df.setMaximumFractionDigits(2);
									total = Float.parseFloat(df.format(total));

									// initializing new customer and assign the customer id
									customer = new Customer();
									customer.setCustomerNumber(resultSet.getInt(2));

									// updating the substring fro query
									partQuery = partQuery.concat(id + ",");

									// set the valid as true
									valid = true;
								}
								// if order has already check number linked to it
								else {
									// System.out.println(id+"with cheque id "+resultSet.getString(3));
									valid = false;
									break;
								}
							}
							// if orders provided are of different customers
							else {
								valid = false;
								break;
							}
						}
						// if the previous validation are failed and valid is not true
						if (!valid)
							break;
					}
				}
				if (valid) {
					if (total == amount) {
						java.sql.Date startDateSQL = new java.sql.Date(new Date().getTime());

						// Executing query to insert the new Payment (check Number) entry into payment
						// table
						int insertResult = statement
								.executeUpdate("Insert into payments values(" + customer.getCustomerNumber() + "" + ",'"
										+ cheque_number + "','" + startDateSQL + "'," + amount + ");");

						// if insert was successful
						if (insertResult != 0) {
							int last = partQuery.lastIndexOf(",");
							partQuery = partQuery.substring(0, last).concat(")");

							// executing update query to update the order with newly inserted check number.
							int updateResult = statement.executeUpdate(
									"Update orders set checkNumber = '" + cheque_number + "' " + partQuery + ";");

							// check update was successful
							if (updateResult != 0) {
								valid = true;
							} else {
								valid = false;
							}
						}
						// id insert was not successful
						else
							valid = false;
					} else {
						valid = false;
					}
				}
			} catch (SQLException e) {
				// setting valid as false;
				valid = false;
			}
		}
		// return if opertaion was successfull or not
		return valid;
	}
	
	/**
	 * @Method: unpaidOrders - to retrieve the order which are not been paid yet
	 * @param connect
	 * @throws UserDefinedSQLException
	 */
	public ArrayList<Integer> unpaidOrders() throws UserDefinedSQLException {
		ArrayList<Integer> listOfOrder = new ArrayList<Integer>();
		if (connect != null) {
			Statement statement = null;
			try {
				statement = connect.createStatement();
				ResultSet resultSet = null;

				// executing the query to retrieve the details into result set
				resultSet = statement.executeQuery(
						"Select o.orderNumber from orders as o where o.checkNumber is null and o.status not in('disputed','cancelled');");

				// iterating the result set
				while (resultSet.next()) {
					// adding the fetched details into list of orders
					listOfOrder.add(resultSet.getInt(1));
				}
			} catch (SQLException e) {
				// Throwing User Defined SQL Exception with error message
				throw new UserDefinedSQLException(
						"SQL Connection Error : Please try reconnecting again.\n" + e.getMessage());
			}
		}
		return listOfOrder;
	}
	
	/**
	 * @Method: unknownPayments - to retrieve the payment record  which does not have any order linked yet
	 * @param connect
	 * @throws UserDefinedSQLException
	 */
	public ArrayList<String> unknownPayments() throws UserDefinedSQLException
	{
		ArrayList<String> listOfPayment = new ArrayList<String>();
		if (connect != null) {
			Statement statement = null;
			try {
				statement = connect.createStatement();
				ResultSet resultSet = null;

				// executing the query to retrieve the details of payments into result set
				resultSet = statement.executeQuery(
						"select p.checkNumber from payments as p where p.checkNumber  not in (select p.checkNumber from payments as p\r\n"
								+ "inner join orders as o on p.checkNumber = o.checkNumber group by p.checkNumber);");

				// iterating the result set
				while (resultSet.next()) {
					// adding the fetched details into list of payments
					listOfPayment.add(resultSet.getString(1));
				}

			} catch (SQLException e) {
				// Throwing User Defined SQL Exception with error message
				throw new UserDefinedSQLException(
						"SQL Connection Error : Please try reconnecting again.\n" + e.getMessage());
			}

		}
		return listOfPayment;
	}
	
}
