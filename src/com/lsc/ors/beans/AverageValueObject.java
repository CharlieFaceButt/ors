package com.lsc.ors.beans;

/**
 * 存储每一个要绘画的柱形所对应的（总等待时间，等待人数），方便计算平均值
 * @author charlieliu
 *
 */
public class AverageValueObject {

	public static final String[] KEYS = new String[]{
		"个数", "平均数", "最大值", "最小值"
	};
	public static final int INDEX_AMOUNT = 0;
	public static final int INDEX_AVERAGE = 1;
	public static final int INDEX_MAX = 2;
	public static final int INDEX_MIN = 3;
	public AverageValueObject(){
		this.total = 0;
		this.divide = 0;
		this.max = 0;
		this.min = null;
	}
	public long total;
	public long divide;
	public int max;
	public Integer min;
	public Object get(int index){
		switch (index) {
		case INDEX_AMOUNT:
			return divide;
		case INDEX_AVERAGE:
			return (int)(total / divide);
		case INDEX_MAX:
			return max;
		case INDEX_MIN:
			return min;
		default:
			return null;
		}
	}
	public void add(int value){
		total += value;
		divide ++;
		if(value > max) max = value;
		if(min == null || value < min) min = value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String r = super.toString();
		r += ("total: " + total + "; divide: " + divide);
		return super.toString();
	}
}
