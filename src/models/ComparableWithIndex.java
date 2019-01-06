package models;

public class ComparableWithIndex implements Comparable<ComparableWithIndex>
{
	private int value;
	private int index;
	public ComparableWithIndex(int value, int index) {
		this.value = value;
		this.index = index;
	}
	@Override
	public int compareTo(ComparableWithIndex o) {
		// TODO Auto-generated method stub
		Integer integer = new Integer(o.getValue());
		return integer.compareTo(value);
		
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getIndex() {
		return index;
	}
}
