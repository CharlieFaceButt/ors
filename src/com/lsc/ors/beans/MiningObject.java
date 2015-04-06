package com.lsc.ors.beans;

/**
 * �洢ÿһ��Ҫ�滭����������Ӧ�ģ��ܵȴ�ʱ�䣬�ȴ����������������ƽ��ֵ
 * @author charlieliu
 *
 */
public class MiningObject {

	public static final String[] KEYS = new String[]{
		"����", "ƽ����", "���ֵ", "��Сֵ"
	};
	public static final int INDEX_AMOUNT = 0;
	public static final int INDEX_AVERAGE = 1;
	public static final int INDEX_MAX = 2;
	public static final int INDEX_MIN = 3;
	public MiningObject(){
		this.sum = 0;
		this.count = 0;
		this.max = 0;
		this.min = null;
	}
	public long sum;
	public long count;
	public int max;
	public Integer min;
	public Object get(int index){
		switch (index) {
		case INDEX_AMOUNT:
			return count;
		case INDEX_AVERAGE:
			return (int)(sum / count);
		case INDEX_MAX:
			return max;
		case INDEX_MIN:
			return min;
		default:
			return null;
		}
	}
	public void add(int value){
		sum += value;
		count ++;
		if(value > max) max = value;
		if(min == null || value < min) min = value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String r = super.toString();
		r += ("sum: " + sum + "; divide: " + count);
		return super.toString();
	}
}
