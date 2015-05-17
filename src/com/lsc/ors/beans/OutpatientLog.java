package com.lsc.ors.beans;

//import java.sql.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;

/**
 * 
 * @author charlieliu
 *
 */
public class OutpatientLog implements BeanObject{

	public static final int INDEX_DEPARTMENT = 1;
	public static final int INDEX_OUTPATIENT_NUM = 2;
	public static final int INDEX_PATIENT = 3;
	public static final int INDEX_PATIENT_GENDER = 4;
	public static final int INDEX_PATIENT_AGE = 5;
	public static final int INDEX_DIAGNOSES = 10;
	public static final int INDEX_RECEPTION = 11;
	public static final int INDEX_REGISTRATION = 12;
	public static final int INDEX_WAIT = 13;
	public static final int INDEX_DOCTOR = 14;
	public static final int INDEX_FURTHER_CONSULTATION = 17;
	
	/**
	 * 对应数据库的18个属性，由于数据的index属性是自动生成的，插入操作时记得排除index
	 */
	public static final String[] KEYS = new String[]{
		"index", "department", "outpatient_number", "patient_name", "patient_gender",
		"patient_age", "patient_job", "patient_phone1", "patient_phone2",
		"patient_family_address", "diagnosis", "reception_time", "registration_time",
		"waiting_time", "doctor_name", "patient_signiture",
		"medical_record_number","further_consultation"
	};
	
	private Integer index;
	private String department;
	private String outpatient_number;
	private String patient_name;
	private String patient_gender;
	private Integer patient_age;
	private String patient_job;
	private String patient_phone1;
	private String patient_phone2;
	private String patient_family_address;
	private String diagnosis;
	private Date reception_time;
	private Date registration_time;
	private Integer waiting_time;
	private String doctor_name;
	private String patient_signiture;
	private String medical_record_number;
	private String further_consultation;
	
	
	public OutpatientLog(int index, String department,
			String outpatient_number, String patient_name,
			String patient_gender, int patient_age, String patient_job,
			String patient_phone1, String patient_phone2,
			String patient_family_address, String diagnosis,
			Date reception_time, Date registration_time, int waiting_time,
			String doctor, String signiture, String medical_record_number,
			String further_consultation) {
		super();
		this.index = index;
		this.department = department;
		this.outpatient_number = outpatient_number;
		this.patient_name = patient_name;
		this.patient_gender = patient_gender;
		this.patient_age = patient_age;
		this.patient_job = patient_job;
		this.patient_phone1 = patient_phone1;
		this.patient_phone2 = patient_phone2;
		this.patient_family_address = patient_family_address;
		this.diagnosis = diagnosis;
		this.reception_time = reception_time;
		this.registration_time = registration_time;
		this.waiting_time = waiting_time;
		this.doctor_name = doctor;
		this.patient_signiture = signiture;
		this.medical_record_number = medical_record_number;
		this.further_consultation = further_consultation;
	}
	
	public OutpatientLog(Date registration_time, Date reception_time, int waiting_time, String doctor_name,
			String diagnosis) {
		super();
		this.registration_time = registration_time;
		this.reception_time = reception_time;
		this.waiting_time = waiting_time;
		this.doctor_name = doctor_name;
		this.diagnosis = diagnosis;
	}
	
	public OutpatientLog(){
		super();
	}
	
	public int getIndex() {
		return index;
	}
	public String getDepartment() {
		return department;
	}
	public String getOutpatient_number() {
		return outpatient_number;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public String getPatient_gender() {
		return patient_gender;
	}
	public int getPatient_age() {
		return patient_age;
	}
	public String getPatient_job() {
		return patient_job;
	}
	public String getPatient_phone1() {
		return patient_phone1;
	}
	public String getPatient_phone2() {
		return patient_phone2;
	}
	public String getPatient_family_address() {
		return patient_family_address;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public Date getReception_time() {
		return reception_time;
	}
	public Date getRegistration_time() {
		return registration_time;
	}
	public int getWaiting_time() {
		return waiting_time;
	}
	public String getDoctor() {
		return doctor_name;
	}
	public String getSigniture() {
		return patient_signiture;
	}
	public String getMedical_record_number() {
		return medical_record_number;
	}
	public String getFurther_consultation() {
		return further_consultation;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setOutpatient_number(String outpatient_number) {
		this.outpatient_number = outpatient_number;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public void setPatient_gender(String patient_gender) {
		this.patient_gender = patient_gender;
	}
	public void setPatient_age(int patient_age) {
		this.patient_age = patient_age;
	}
	public void setPatient_job(String patient_job) {
		this.patient_job = patient_job;
	}
	public void setPatient_phone1(String patient_phone1) {
		this.patient_phone1 = patient_phone1;
	}
	public void setPatient_phone2(String patient_phone2) {
		this.patient_phone2 = patient_phone2;
	}
	public void setPatient_family_address(String patient_family_address) {
		this.patient_family_address = patient_family_address;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public void setReception_time(Date reception_time) {
		this.reception_time = reception_time;
	}
	public void setRegistration_time(Date registration_time) {
		this.registration_time = registration_time;
	}
	public void setWaiting_time(int waiting_time) {
		this.waiting_time = waiting_time;
	}
	public void setDoctor(String doctor) {
		this.doctor_name = doctor;
	}
	public void setSigniture(String signiture) {
		this.patient_signiture = signiture;
	}
	public void setMedical_record_number(String medical_record_number) {
		this.medical_record_number = medical_record_number;
	}
	public void setFurther_consultation(String further_consultation) {
		this.further_consultation = further_consultation;
	}
	

	/**
	@Override
	 * patient_age和waitingtime是int转String<br>
	 * registration_time 和 reception_tim格式是默认格式<br>
	 */
	public String get(int key){
		switch (key) {
		case 1:return department;
		case 2:return outpatient_number;
		case 3:return patient_name;
		case 4:return patient_gender;
		case 5:return "" + patient_age;
		case 6:return patient_job;
		case 7:return patient_phone1;
		case 8:return patient_phone2;
		case 9:return patient_family_address;
		case 10:return diagnosis;
		case 11:
			if(reception_time == null) 
				return null;
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format1.format(reception_time);
		case 12:
			if(registration_time == null)
				return null;
			DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format2.format(registration_time);
		case 13:return "" + waiting_time;
		case 14:return doctor_name;
		case 15:return patient_signiture;
		case 16:return medical_record_number;
		case 17:return further_consultation;
		default:return null;
		}
	}

	/**
	 * 从excel导入数据库时进行的数据转换
	 * @param key
	 * @param value
	 * @return
	 */
	public String generateValueFromExcelCell(int key, Cell cell) {
		// TODO Auto-generated method stub
		if(cell == null) return null;
		
		switch (key) {
		case 10:
			String content = cell.getContents();
			if(content != null)
				content.replaceAll("，", ",");
			break;
		case 11:
		case 12:
			if(cell.getType() != CellType.DATE) return null;
			Date d = ((DateCell)cell).getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.HOUR_OF_DAY, -8);
			d = cal.getTime();
			set(key, d);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(d);
		case 16:
			String mrn = cell.getContents();
			if(mrn.length() > 16){
				mrn = mrn.substring(0, 16);
			}
			set(key, mrn);
		case 17:
			String fc = cell.getContents();
			if(!fc.equals("√")){
				fc = "N";
			}
			else fc = "Y";
			set(key, fc);
			return fc;
		default:break;
		}
		set(key, cell.getContents());
		return cell.getContents();
	}
	
	/**
	 * 获得门诊记录的特征属性
	 * @return
	 */
	public OutpatientLogCharacters generateCharacters(){
		OutpatientLogCharacters olc = new OutpatientLogCharacters();
		olc.set(OutpatientLogCharacters.INDEX_PATIENT_GENDER, patient_gender);
		olc.set(OutpatientLogCharacters.INDEX_PATIENT_AGE, patient_age);
		olc.set(OutpatientLogCharacters.INDEX_DIAGNOSES, diagnosis);
		olc.set(OutpatientLogCharacters.INDEX_RECEPTION, reception_time);
		olc.set(OutpatientLogCharacters.INDEX_REGISTRATION, registration_time);
		olc.set(OutpatientLogCharacters.INDEX_DOCTOR, doctor_name);
		olc.set(OutpatientLogCharacters.INDEX_WAIT, waiting_time);
		olc.set(OutpatientLogCharacters.INDEX_FURTHER_CONSULTATION, further_consultation);
		return olc;
	}

	@Override
	public void set(int key, Object value) {
		// TODO Auto-generated method stub
		if(value == null || value.equals("")) return;
		switch (key) {
		case 1:
			department = value.toString();
			break;
		case 2:
			outpatient_number = value.toString();
			break;
		case 3:
			patient_name = value.toString();
			break;
		case 4:
			patient_gender = value.toString();
			break;
		case 5:
			patient_age = Integer.parseInt(value.toString());
			break;
		case 6:
			patient_job = value.toString();
			break;
		case 7:
			patient_phone1 = value.toString();
			break;
		case 8:
			patient_phone2 = value.toString();
			break;
		case 9:
			patient_family_address = value.toString();
			break;
		case 10:
			diagnosis = value.toString();
			break;
		case 11:
			reception_time = (Date)value;
			break;
		case 12:
			registration_time = (Date)value;
			break;
		case 13:
			waiting_time = Integer.parseInt(value.toString());
			break;
		case 14:
			doctor_name = value.toString();
			break;
		case 15:
			patient_signiture = value.toString();
			break;
		case 16:
			medical_record_number = value.toString();
			break;
		case 17:
			further_consultation = value.toString();
			break;
		default:
			break;
		}
	}

	@Override
	public String[] keySet() {
		// TODO Auto-generated method stub
		return KEYS;
	}
}
