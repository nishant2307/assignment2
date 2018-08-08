package com.nishant.assignment2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AssignmentMain {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

		String file ="E:\\NishantAssifnment\\src\\com\\nishant\\checkfile\\testfile.txt";
		List<String> files = new ArrayList<String>();
		files.add(file);
		ProcessFile ps =new ProcessFile(files);
		Long startTime = System.currentTimeMillis();
		for(int i=0;i<=10000;i++) {
		ps.calculateCommentsAndDataFromat();
		}
		Long endTime = System.currentTimeMillis();
		
		int comment= ps.getComment();
		int badDateTimeFormat = ps.getBadTimeFormat();
		int badlyFormattedData = ps.getBadlyFormatedData();
		System.out.println("Comments: "+comment);
		System.out.println("bad date format: "+badDateTimeFormat);
		System.out.println("bad date format: "+badlyFormattedData);
		System.out.println(ps.getUsersAtParticularTime("09 pm"));
		
		System.out.println(startTime);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String file ="E:\\NishantAssifnment\\src\\com\\nishant\\checkfile\\testfile.txt";
				List<String> files = new ArrayList<String>();
				files.add(file);
				ProcessFile ps =new ProcessFile(files);
				Long startTime = System.currentTimeMillis();
				for(int i=0;i<=10000;i++) {
				ps.calculateCommentsAndDataFromat();
				}
				Long endTime = System.currentTimeMillis();
				
				int comment= ps.getComment();
				int badDateTimeFormat = ps.getBadTimeFormat();
				int badlyFormattedData = ps.getBadlyFormatedData();
				System.out.println("Comments: "+comment);
				System.out.println("bad date format: "+badDateTimeFormat);
				System.out.println("bad date format: "+badlyFormattedData);
				System.out.println(ps.getUsersAtParticularTime("09 pm"));
				
				System.out.println(startTime);
				System.out.println(endTime);
				System.err.println(endTime-startTime);

			}
		});
		t.start();
		System.out.println(endTime);
		System.err.println(endTime-startTime);
	}

}
