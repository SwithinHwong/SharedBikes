package models;

public class ComparableIndex_Double implements Comparable<ComparableIndex_Double>
{
	private double value;
	private int index;
	public ComparableIndex_Double(double value, int index) {
		this.value = value;
		this.index = index;
	}
	@Override
	public int compareTo(ComparableIndex_Double o) {
		// TODO Auto-generated method stub
		Double integer = new Double(value);
		return integer.compareTo(o.getValue());
		
	}
	
	public double getValue()
	{
		return value;
	}
	
	public int getIndex() {
		return index;
	}
}
