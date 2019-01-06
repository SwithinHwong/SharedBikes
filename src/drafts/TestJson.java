package drafts;

import java.util.ArrayList;
import java.util.Date;
import com.alibaba.fastjson.JSON;

import models.Order;


public class TestJson {

	public TestJson() {
		// TODO Auto-generated constructor stub
	}

	public static void _main(String[] args) {

		Order order1 = new Order(new Date(), new Date(), new double[]{11.2,23.5}, new double[]{11.2,23.5});
		String json = JSON.toJSONString(order1);
		System.out.println(json);
		
		
		
		
		
	}
	
	public static void main(String[] args) {

		String json = "{\"startLocation\":[11.2,23.5],\"startTime\":\"1996-11-25\",\"stopLocation\":[11.2,23.5],\"stopTime\":\"2017-05-31 19:50:59\"}";
		Order order1 = (Order) JSON.parseObject(json, Order.class);
		
//		System.out.println(order1.getStartLocation()[0] + order1.getStartLocation()[1]);
		
		String json1 = JSON.toJSONString(order1);
		System.out.println(json1);
		
		
		
		
		
	}
	
	public static void main2(String[] args) {

		ArrayList<double[]> loca = new ArrayList<>();
		double[] arr1 = {1.12,3.12};
		loca.add(arr1);
		double[] arr2 = {1.23, 5.21};
		loca.add(arr2);
		String json1 = JSON.toJSONString(loca);
		System.out.println(json1);
		
		
		
		
		
	}
	
	
	

}
