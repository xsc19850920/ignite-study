package com.genpact.rate;

import java.util.Date;

public class BillRecord {
	private Date monthOnBill;
	private double monthPrincipal;
	private double monthCapital;
	private double monthInterest;

	public Date getMonthOnBill() {
		return monthOnBill;
	}

	public void setMonthOnBill(Date monthOnBill) {
		this.monthOnBill = monthOnBill;
	}

	public double getMonthPrincipal() {
		return monthPrincipal;
	}

	public void setMonthPrincipal(double monthPrincipal) {
		this.monthPrincipal = monthPrincipal;
	}

	public double getMonthCapital() {
		return monthCapital;
	}

	public void setMonthCapital(double monthCapital) {
		this.monthCapital = monthCapital;
	}

	public double getMonthInterest() {
		return monthInterest;
	}

	public void setMonthInterest(double monthInterest) {
		this.monthInterest = monthInterest;
	}
	@Override
	public String toString() {
		return "第 " + monthOnBill + " 月本息： "+monthCapital+" , 本金： "+monthPrincipal+" , 利息："+monthInterest;
	}
	
	

}
