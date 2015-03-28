package com.lsc.ors.beans;

/**
 * �洢ÿһ��Ҫ�滭����������Ӧ�ģ��ܵȴ�ʱ�䣬�ȴ����������������ƽ��ֵ
 * @author charlieliu
 *
 */
public class AverageValueObject {

	public static final String[] KEYS = new String[]{
		"����", "ƽ����", "���ֵ", "��Сֵ"
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
