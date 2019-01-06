package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;

import models.BasicOrderStatData;
import models.ComparableIndex_Double;
import models.ComparableWithIndex;
import models.MyKMeans;
import models.Order;
import net.sf.javaml.*;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;



public class OrdersManager 
{
	private static final OrdersManager instance = new OrdersManager(); //单例模式
	private ArrayList<Order> orderList;
	private ArrayList<Order> selectedOrderList;
	private BasicOrderStatData basicOrderStatData;
	private double random_value;
	private int[] assignment; //orderList按顺序划入的簇
	private int[] depart; //聚类后每一个簇出发的车数
	private int[] arri; //聚类后每一个簇到达的车数
	private double[][] clus_center;
	private int[] balance;
	private int[][] nearestIndex;
	private double[][] clus_distance = null;
	private ArrayList<int[]> arrange;
	
	public void calcu_clus_distance() {
		int num = clus_center.length;
		clus_distance = new double[num][num];
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < num; j++) {
				double x = clus_center[i][0] - clus_center[j][0];
				double y = clus_center[i][1] - clus_center[j][1];
				clus_distance[i][j] = x*x + y*y;
			}
		}
	}
	
	public void find_nearest_index()
	{
		if (clus_distance == null) {
			calcu_clus_distance();
		}
		int num = clus_center.length;
		nearestIndex = new int[num][num-1];
		
		System.out.println(num);
		
		for (int i = 0; i < num; i++) {
			ArrayList<ComparableIndex_Double> arr_dis= new ArrayList<>();
			for (int j = 0; j < num; j++) {
				
				arr_dis.add(new ComparableIndex_Double(clus_distance[i][j], j));
			}
			
			Collections.sort(arr_dis);
			for (int j = 0; j < num-1; j++) {
				nearestIndex[i][j] = arr_dis.get(j+1).getIndex();
			}
		}
		
		
		
		
	}
	
	public ArrayList<int[]> logistics() {
		int cls_num = depart.length;
		balance = new int[cls_num];
		for (int i = 0; i < balance.length; i++) {
			balance[i] = arri[i]-depart[i];
		}
		find_nearest_index();
		
		boolean[] moved = new boolean[cls_num];
		for (int i = 0; i < moved.length; i++) {
			moved[i] = false;
		}
		

//		ArrayList<ComparableWithIndex> arr_demand= new ArrayList<>();
//		for (int i = 0; i < cls_num; i++) {
//			if (balance[i]<0) {
//				
//			}
//			arr_demand.add(new ComparableWithIndex(balance[i], i));
//		}
//		
//		Collections.sort(arr_dep);
		
		int waiting_for_arrange = 0;
		boolean[] affordable = new boolean[cls_num];
		int[] demand = new int[cls_num];
		for (int i = 0; i < demand.length; i++) {
			affordable[i] = false;
			if (balance[i]<0) {
				demand[i] = balance[i]*(-1);
			}
			else {
				demand[i] = 0;
				if (balance[i]>0) {
					waiting_for_arrange++;
					affordable[i] = true;
				}
			}
		}
		
		arrange = new ArrayList<>();
		for (int i = 0; waiting_for_arrange>0 && i < cls_num-1; i++) {
			for (int j = 0; j < cls_num; j++) {
				//先把最近的点两两运输，再把没有配对的点找最近的运输
				if (affordable[j] && demand[nearestIndex[j][i]]>0) {
					int[] mm = new int[3];
					mm[0] = j;
					mm[1] = nearestIndex[j][i];
					mm[2] = balance[j];
					arrange.add(mm);
					affordable[j] = false;
					waiting_for_arrange--;
					demand[nearestIndex[j][i]] -= balance[j];
					
				}
			}
		}
		

		FileWriter fw;
		try {
			fw = new FileWriter("./src/arrange.txt", false);
			BufferedWriter bf = new BufferedWriter(fw);
//			bf.write("var data =");
			bf.write(JSON.toJSONString(arrange));
//			bf.write(";");
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return arrange;
		
	}
	
	public ArrayList<int[]> getArrange() {
		return arrange;
	}
	
	public static OrdersManager getInstance() {
		 return instance;
	}
	
	private OrdersManager() 
	{
		orderList = new ArrayList<>();
		selectedOrderList = new ArrayList<>();
	}
	
	

	
	public void loadOrderFile(String filepath) throws FileNotFoundException, IOException
	{
		
		ArrayList<Order> newOrderList = new ArrayList<>();
		Scanner scanner = new Scanner(new FileInputStream(filepath));
		while(scanner.hasNextLine())
		{
//			Order order1 = (Order) JSON.parseObject(scanner.nextLine().trim() , Order.class);
			newOrderList.add( (Order) JSON.parseObject(scanner.nextLine().trim() , Order.class) );
		}
		scanner.close();
		
		if (newOrderList.size()>0) {
			orderList.clear();
			orderList = newOrderList;
			selectedOrderList.clear();
			selectedOrderList = (ArrayList<Order>) orderList.clone();
			
			Random random = new Random();
			random_value = random.nextGaussian();
		}
		
		
	}
	
	public ArrayList<Order> getOrderList() 
	{
		return orderList;
	}
	
	public void selectTimeScope(String str_startTime, String str_endTime) throws ParseException 
	{
		//str_endTime都是hh:mm的格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = sdf.parse("2017-06-01 "+str_startTime+":00");
		Date endTime = sdf.parse("2017-06-01 "+str_endTime+":00");
		
		selectTimeScope( startTime,  endTime) ;
		
		
		
	}
	
	public void selectTimeScope(Date startTime, Date endTime) 
	{
		
		selectedOrderList.clear();
		for (Order order : orderList) 
		{
			if (order.getStartTime().after(startTime)  && order.getStartTime().before(endTime) ) 
			{
				selectedOrderList.add(order);
			}
		}
	}
	
	
	public void writeSelectedLocation2JS(String filepath) throws IOException 
	{
		ArrayList<String > locations = new ArrayList<>();
		for (Order order : selectedOrderList) 
		{
			String loc = Double.toString(order.getStartLocation()[0]) + "," + Double.toString(order.getStartLocation()[1]);
			locations.add(loc);
		}
		
		FileWriter fw = new FileWriter(filepath, false);
		BufferedWriter bf = new BufferedWriter(fw);
		
		bf.write("var data =");
		bf.write(JSON.toJSONString(locations));
		bf.write(";");
		bf.newLine();
		bf.flush();
		bf.close();
	}
	
	public void writeSelected2JS() throws IOException 
	{
		writeSelectedLocation2JS("./src/location.js");
	}
//	
//	public void writeLocationData(String filepath) throws IOException 
//	{
//		FileWriter fw = new FileWriter(filepath, false);
//		BufferedWriter bf = new BufferedWriter(fw);
//		for (Order order : orderList) 
//		{
//			String loc = Double.toString(order.getStartLocation()[0]) + "," + Double.toString(order.getStartLocation()[1]);
//			bf.write(loc);
//			bf.newLine();
//		}
//		bf.flush();
//		bf.close();
//	}
//	
	
	public int getOrdersNum() {
		return orderList.size();
	}
	
	public BasicOrderStatData calcu_basic_stat() 
	{
		//基本信息统计
		int ordersNum;
//		String date = "2017-06-01";
		double average_distance;
		double average_duration;
		double average_bike_using_times;
		double average_user_times;
		
		ordersNum = getOrdersNum();
//		date = "2017-06-01";
		
		
		double sum_distance = 0;
		long sum_time = 0;
		for (Order order : orderList) {
			sum_time += order.getDuration()/1000;
			sum_distance += order.getDistance();
		}
		average_distance = sum_distance/ordersNum;
		average_duration = sum_time/ordersNum;
		
		average_bike_using_times = 9.2;
		
		int users = (int) (0.9*ordersNum + random_value*0.03*ordersNum);
		average_user_times = ((double)ordersNum)/users;
		
		
		BasicOrderStatData basic_data = new BasicOrderStatData();
		
		
		basic_data.ordersNum = ordersNum;
//		basic_data.date = date;
		basic_data.average_distance = average_distance;
		basic_data.average_duration = average_duration;
		basic_data.average_bike_using_times = average_bike_using_times;
		basic_data.average_user_times = average_user_times;
		
		basicOrderStatData = basic_data;
		
		return basicOrderStatData;
	}
	
	
	public int[] countOrdersPreHour()
	{
		//计算每个小时订单数
		int[] counts = new int[24];
		for (int i = 0; i < counts.length; i++) {
			counts[i] = 0;
		}
		
		int hour;
		Calendar calendar = Calendar.getInstance();
		
		for (Order order : orderList) {
			calendar.setTime(order.getStartTime());
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			counts[hour]++;
//			System.out.println(hour+"");
		}
		
		
		return counts;
	}
	
	public void writeCluster2JS(Dataset[] clusters) throws IOException 
	{
		String locFilepath = "./src/cluster_locations.js";
		String cluFilepath = "./src/cluster_id.js";
		ArrayList<String > locations = new ArrayList<>();
		ArrayList<Integer> cluster_id = new ArrayList<>();
		
		
		
		Dataset dataset;
		for (int i = 0; i < clusters.length; i++) {
			dataset = clusters[i];
			for (Instance instance : dataset) {
				String loc = Double.toString(instance.value(0)) + "," + Double.toString(instance.value(1));
				locations.add(loc);
				cluster_id.add(i);
			}
		}
		
		FileWriter fw = new FileWriter(locFilepath, false);
		BufferedWriter bf = new BufferedWriter(fw);
		
		bf.write("var data =");
		bf.write(JSON.toJSONString(locations));
		bf.write(";");
		bf.newLine();
		bf.flush();
		bf.close();
		
		

		fw = new FileWriter(cluFilepath, false);
		bf = new BufferedWriter(fw);
		
		bf.write("var cluster_id =");
		bf.write(JSON.toJSONString(cluster_id));
		bf.write(";");
		bf.newLine();
		bf.flush();
		bf.close();
	}
	
	
	public void clustering() throws IOException 
	{
		//聚类，结果写入地图文件
		
		
		
		
		
		
		Dataset data = new DefaultDataset();
		double[] loc = new double[2];
		int size = orderList.size();
		if (size>20000) {
			size = 20000;
		}
		for (int i = 0; i < size; i++) {
			loc = orderList.get(i).getStartLocation().clone();
			data.add(new DenseInstance(loc));
		}
		
		System.out.println("Finished Clustering Preparation");
//		for (Order order : orderList) 
//		{
//			
//			loc = order.getStartLocation().clone();
//			data.add(new DenseInstance(loc));
//		}
		
//		FileHandler.exportDataset(data,new File("./src/output.txt"));
		Clusterer km = new KMeans(193);
		Dataset[] clusters = km.cluster(data);
		
		writeCluster2JS(clusters);
		
		
		
		
	}
	
	public int[] cluster_with_info()
	{
		//聚类，计算基本簇信息
		
		int maxsize = 30000;
		Dataset data = new DefaultDataset();
		double[] loc = new double[2];
		int size = orderList.size();
		if (size>maxsize) {
			size = maxsize;
		}
		for (int i = 0; i < size; i++) {
			loc = orderList.get(i).getStartLocation().clone();
			data.add(new DenseInstance(loc));
		}
		for (int i = 0; i < size; i++) {
			loc = orderList.get(i).getStopLocation().clone();
			data.add(new DenseInstance(loc));
		}
		
		System.out.println("Finished Clustering Preparation");
//		for (Order order : orderList) 
//		{
//			
//			loc = order.getStartLocation().clone();
//			data.add(new DenseInstance(loc));
//		}
		
//		FileHandler.exportDataset(data,new File("./src/output.txt"));
		MyKMeans km = new MyKMeans(193);
		assignment = km.cluster(data);
		
		depart = new int[193];
		arri = new int[193];
		
		double[][] sum_loca = new double[193][2];
//		double[][] sum_loca2 = new double[193][2];
		for (int i = 0; i < 193; i++) {
			depart[i] = 0;
			arri[i] = 0;
			sum_loca[i][0] = 0;
			sum_loca[i][1] = 0;
//			sum_loca2[i][0] = 0;
//			sum_loca2[i][1] = 0;
			
		}
		
		
		
		for (int i = 0; i < size; i++) {
			depart[assignment[i]]++;
			arri[assignment[i+size]]++;
			sum_loca[assignment[i]][0]+=orderList.get(i).getStartLocation()[0];
			sum_loca[assignment[i]][1]+=orderList.get(i).getStartLocation()[1];
//			sum_loca[assignment[i+size]][0]+=orderList.get(i).getStopLocation()[0];
//			sum_loca[assignment[i+size]][1]+=orderList.get(i).getStopLocation()[1];
		}
		
		
		clus_center = new double[193][2];
		for (int i = 0; i < 193; i++) {
			clus_center[i][0] = sum_loca[i][0]/(depart[i]);
			clus_center[i][1] = sum_loca[i][0]/(depart[i]);
		}
		
//		for (int i = 0; i < 20; i++) {
//			System.out.println(depart[i]);
//			System.out.println(clus_center[i][0]);
//		}
		return assignment;
	}
	
	public int[] getAssignment()
	{
		return assignment;
	}
	
	public double[][] getClusterCenter() {
		return clus_center;
	}
	
	public int[] getDepartNum() {
		return depart;
	}
	
	public int[] getArrivNum() {
		return arri;
	}
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		OrdersManager oManager = new OrdersManager();
		oManager.loadOrderFile("./src/orders/orders.txt");
		System.out.println("Loaded!");
//		Order order100 = oManager.getOrderList().get(100);
//		System.out.println(order100.getStartTime());
		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date startTime =  dateFormat.parse("2017-06-01 08:00:00");
//		Date endTime =  dateFormat.parse("2017-06-01 09:00:00");
//		oManager.selectTimeScope(startTime, endTime);
//		oManager.writeSelectedLocation2JS("./src/location.js");
//		oManager.writeLocationData("./src/location.data");
		
//		oManager.clustering();
		oManager.cluster_with_info();
		
		System.out.println("Done!");
		
		

	}
	
	
	

}
