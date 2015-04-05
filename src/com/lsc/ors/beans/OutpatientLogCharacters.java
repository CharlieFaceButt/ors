package com.lsc.ors.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutpatientLogCharacters implements BeanObject {
	
	public static final int INDEX_PATIENT_GENDER = 0;
	public static final int INDEX_PATIENT_AGE = 1;
	public static final int INDEX_DIAGNOSES = 2;
	public static final int INDEX_RECEPTION = 3;
	public static final int INDEX_REGISTRATION = 4;
	public static final int INDEX_WAIT = 5;
	public static final int INDEX_DOCTOR = 6;
	public static final int INDEX_FURTHER_CONSULTATION = 7;
	/**
	 * 对应数据库的18个属性，由于数据的index属性是自动生成的，插入操作时记得排除index
	 */
	public static final String[] KEYS = new String[]{
		"patient_gender", "patient_age", "diagnosis", "reception_time", 
		"registration_time", "waiting_time", "doctor_name", "further_consultation"
	};

//	private Integer index;
//	private String department;
//	private String outpatient_number;
//	private String patient_name;
//	private String patient_job;
//	private String patient_phone1;
//	private String patient_phone2;
//	private String patient_family_address;
//	private String patient_signiture;
//	private String medical_record_number;
	private String patient_gender;
	private Integer patient_age;
	private String diagnosis;
	private Date reception_time;
	private Date registration_time;
	private Integer waiting_time;
	private String doctor_name;
	private String further_consultation;
	
	public OutpatientLogCharacters() {
		super();
	}

	@Override
	public void set(int key, Object value) {
		// TODO Auto-generated method stub
		if(value == null || value.equals("")) return;
		switch (key) {
		case INDEX_PATIENT_GENDER:
			patient_gender = value.toString();
			break;
		case INDEX_PATIENT_AGE:
			patient_age = Integer.parseInt(value.toString());
			break;
		case INDEX_DIAGNOSES:
			diagnosis = value.toString();
			break;
		case INDEX_RECEPTION:
			reception_time = (Date)value;
			break;
		case INDEX_REGISTRATION:
			registration_time = (Date)value;
			break;
		case INDEX_WAIT:
			waiting_time = Integer.parseInt(value.toString());
			break;
		case INDEX_DOCTOR:
			doctor_name = value.toString();
			break;
		case INDEX_FURTHER_CONSULTATION:
			further_consultation = value.toString();
			break;
		default:
			break;
		}
	}

	@Override
	public String get(int key) {
		// TODO Auto-generated method stub
		switch (key) {
		case INDEX_PATIENT_GENDER:return patient_gender;
		case INDEX_PATIENT_AGE:return "" + patient_age;
		case INDEX_DIAGNOSES:return diagnosis;
		case INDEX_RECEPTION:
			if(reception_time == null) 
				return null;
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format1.format(reception_time);
		case INDEX_REGISTRATION:
			if(registration_time == null)
				return null;
			DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format2.format(registration_time);
		case INDEX_WAIT:return "" + waiting_time;
		case INDEX_DOCTOR:return doctor_name;
		case INDEX_FURTHER_CONSULTATION:return further_consultation;
		default:return null;
		}
	}

	public String getPatient_gender() {
		return patient_gender;
	}

	public Integer getPatient_age() {
		return patient_age;
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

	public Integer getWaiting_time() {
		return waiting_time;
	}

	public String getDoctor_name() {
		return doctor_name;
	}

	public String getFurther_consultation() {
		return further_consultation;
	}

	public void setPatient_gender(String patient_gender) {
		this.patient_gender = patient_gender;
	}

	public void setPatient_age(Integer patient_age) {
		this.patient_age = patient_age;
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

	public void setWaiting_time(Integer waiting_time) {
		this.waiting_time = waiting_time;
	}

	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}

	public void setFurther_consultation(String further_consultation) {
		this.further_consultation = further_consultation;
	}

	@Override
	public String[] keySet() {
		// TODO Auto-generated method stub
		return KEYS;
	}

}
