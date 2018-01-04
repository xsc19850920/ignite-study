package com.genpact.rate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 等额本金计算
 */
public class PrincipalEquals {
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws ParseException, IOException {
		// double invest = 280000;
		// double yearRate = 0.0655;
		// double monthRate = yearRate / 12;
		// int year = 15;
		// int month = year * 12;
		// // 每月本息金额 = (贷款本金÷还款月数) + (贷款本金-已归还本金累计额)×月利率
		// // 每月本金 = 贷款本金÷还款月数
		// // 每月利息 = (贷款本金-已归还本金累计额)×月利率
		// double monthCapital = 0;
		// double tmpCapital = 0;
		// double monthInterest = 0;
		// for (int i = 1; i < month + 1; i++) {
		// monthCapital = (invest / month) + (invest - tmpCapital) * monthRate;
		// monthInterest = (invest - tmpCapital) * monthRate;
		// tmpCapital = tmpCapital + (invest / month);
		// System.out.println("第" + i + "月本息： " + monthCapital + "，本金：" +
		// (invest / month) + "，利息：" + monthInterest);
		// }
		
		long begin = new Date().getTime();
		
		//io part
		FileOutputStream out = new FileOutputStream(new File("d:/housingloan.txt")); 
		
		List<BillRecord> billRecordList = new ArrayList<BillRecord>();
		for (int i = 0; i < 3000; i++) {
			LoanRecord loanRecord = new LoanRecord();
			loanRecord.setInvest(40 * 10000);
			loanRecord.setYearRate(6.5/100 );
			loanRecord.setYear(30);
			loanRecord.setStartMonth(df.parse("2015-09-20"));
			
			billRecordList.addAll(calc(loanRecord));
		}
		for (int j = 0; j < billRecordList.size(); j++) {
			BillRecord billRecord = billRecordList.get(j);
			
			out.write(billRecord.toString().getBytes());
//			System.out.println("第" + df.format(billRecord.getMonthOnBill()) + "月本息： " + billRecord.getMonthCapital()
//					+ "，本金：" + billRecord.getMonthPrincipal() + "，利息：" + billRecord.getMonthInterest());
			if(j%1000 == 0){
				
				out.flush();
			}
		}
		
		if(out != null){
			out.close();
		}
		long end = new Date().getTime();
		System.out.println("===============耗时："+(end-begin));
		
	}

	public static List<BillRecord> calc(LoanRecord loanRecord) {
		double monthRate = loanRecord.getYearRate() / 12;
		int month = loanRecord.getYear() * 12;
		// 每月本息金额 = (贷款本金÷还款月数) + (贷款本金-已归还本金累计额)×月利率
		// 每月本金 = 贷款本金÷还款月数
		// 每月利息 = (贷款本金-已归还本金累计额)×月利率
		// double monthCapital = 0;
		double tmpCapital = 0;
		// double monthInterest = 0;
		Date startMonth = loanRecord.getStartMonth();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startMonth);

		List<BillRecord> billRecordList = new ArrayList<BillRecord>();
		for (int i = 1; i <= month; i++) {
			BillRecord billRecord = new BillRecord();
			billRecord.setMonthCapital(
					(loanRecord.getInvest() / month) + (loanRecord.getInvest() - tmpCapital) * monthRate);
			billRecord.setMonthInterest((loanRecord.getInvest() - tmpCapital) * monthRate);
			tmpCapital = tmpCapital + (loanRecord.getInvest() / month);
			// System.out.println("第" + i + "月本息： " + monthCapital + "，本金：" +
			// (loanRecord.getInvest() / month) + "，利息："
			// + monthInterest);
			billRecord.setMonthPrincipal(loanRecord.getInvest() / month);

			cal.add(Calendar.MONTH, 1);
			billRecord.setMonthOnBill(cal.getTime());

			billRecordList.add(billRecord);
		}
		return billRecordList;
	}

}
