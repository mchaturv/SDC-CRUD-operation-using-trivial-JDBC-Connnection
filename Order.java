import java.util.Date;


/**
 * @author Mayank
 *	Entity Representation of Order table
 */
public class Order{

	/**
	 * @param: orderId, Integer
	 * @param: totalAmount, Float
	 * @param: orderDate, Date
	 */
	private int orderId;
	private float totalAmount;
	private Date orderdate;
	
	/**
	 * Default Constructor
	 */
	public Order() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param orderId
	 * @param totalAmount
	 */
	public Order(int orderId, float totalAmount, Date orderDate) {
		super();
		this.orderId = orderId;
		this.totalAmount = totalAmount;
		this.orderdate = orderDate;
	}


	/**
	 * @return the orderId
	 */
	public int getOrderId() {
		return orderId;
	}
	
	/**
	 * to set the orderId
	 * @param orderId  
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * @return the totalAmount
	 */
	public float getTotalAmount() {
		return totalAmount;
	}
	
	/**
	 * to set the totalAmount
	 * @param totalAmount 
	 */
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

	/**
	 * @return the orderdate
	 */
	public Date getOrderdate() {
		return orderdate;
	}


	/**
	 * to set the orderdate
	 * @param orderdate 
	 */
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}


	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", totalAmount=" + totalAmount + ", orderdate=" + orderdate + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderId;
		result = prime * result + ((orderdate == null) ? 0 : orderdate.hashCode());
		result = prime * result + Float.floatToIntBits(totalAmount);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderId != other.orderId)
			return false;
		/*
		 * if (orderdate == null) { if (other.orderdate != null) return false; } else if
		 * (!orderdate.equals(other.orderdate)) return false; if
		 * (Float.floatToIntBits(totalAmount) !=
		 * Float.floatToIntBits(other.totalAmount)) return false;
		 */
		return true;
	}	

}
