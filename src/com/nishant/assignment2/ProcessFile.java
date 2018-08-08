package com.nishant.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessFile {
    private int comment;
    private int badlyFormatedData;
    private int badTimeFormat;
    private List<String> files;
    private Map<Integer, Set<String>> usersAtParticularTime;

    public ProcessFile(List<String> files) {
        this.files = files;
        this.comment = 0;
        this.badlyFormatedData = 0;
        this.badTimeFormat = 0;
        this.usersAtParticularTime = new HashMap<>();
    }

    public int getComment() {
        return comment;
    }

    public int getBadlyFormatedData() {
        return badlyFormatedData;
    }

    public int getBadTimeFormat() {
        return badTimeFormat;
    }

    public List<String> getFiles() {
        return files;
    }

    public void calculateCommentsAndDataFromat() {
        for (String filePath : this.files) {
            if (filePath != null) {
                try (Scanner sc = new Scanner(new File(filePath))) {

                    String str = "";
                    while (sc.hasNext()) {
                        str = sc.nextLine().trim();
                        try {
                            if (str.startsWith("#")) {
                                //System.out.println("Comment");
                                comment++;
                            } else if ((int) str.charAt(0) == 65279) {
                                if (str.charAt(1) == '#') {
                                    //System.out.println("Comment");
                                    comment++;
                                }
                            } else {
//                                System.out.println("Data");
//                                System.out.println(str);
                                String[] userData = str.split("\\s*\\|\\s*");
                                if (userData.length != 4) {
                                    badlyFormatedData++;
                                } else {
                                    if (!checkSessionTime(userData[2], userData[3])) {
                                        badTimeFormat++;
                                    } else {
                                        int hours[] = usersInSession(cvtToGmt(new Date(Long.parseLong(userData[2]))), cvtToGmt(new Date(Long.parseLong(userData[3]))));
                                        for (int hour : hours) {
                                            if (this.usersAtParticularTime.get(hour) != null) {
                                            	System.out.println(hour +" "+userData[1]);
                                                this.usersAtParticularTime.get(hour).add(userData[1]);
                                            } else {
                                                this.usersAtParticularTime.put(hour, new HashSet<>());
                                                this.usersAtParticularTime.get(hour).add(userData[1]);
                                                System.out.println(hour +" "+userData[1]);
                                            }
                                        }
                                    }
                                }
                            }
                            
                        } catch (StringIndexOutOfBoundsException siobe) {

                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                    System.out.println("Not able to open the file. Reason : Invalid file path.");
                }
                //System.out.println(comment + " " + badlyFormatedData + " " + badTimeFormat);
            }
        }
        System.out.println(this.usersAtParticularTime);
    }

    private boolean checkSessionTime(String startSessionTime, String endSessionTime) {
        Date start = new Date(Long.parseLong(startSessionTime));
        Date end = new Date(Long.parseLong(endSessionTime));
        Date endBoundaryDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, 2010);
        cal.setTimeZone(TimeZone.getTimeZone("GMT")); 
        Date startboundaryDate = cal.getTime();
        //System.out.println(startboundaryDate);
        return endBoundaryDate.compareTo(start) == 1 && endBoundaryDate.compareTo(end) == 1 &&
                end.compareTo(startboundaryDate) == 1 && start.compareTo(startboundaryDate) == 1 && end.compareTo(start)
                == 1 ? true : false;
    }

    private static int[] usersInSession(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        System.out.println(start);
        int startHour = cal.get(Calendar.HOUR_OF_DAY);
        cal.setTime(end);
        System.out.println(end);
        int endHour = cal.get(Calendar.HOUR_OF_DAY);
        if (startHour == endHour) {
            return new int[]{startHour};
        }
        return new int[]{startHour, endHour};

    }

    //The time has to be in the format like HH PM or HH AM
    public Set<String> getUsersAtParticularTime(String time) {
        String timeArr[] = time.trim().split(" ");
        if (timeArr[1].toLowerCase().equals("pm")) {
        	System.out.println(timeArr[0]);
            return this.usersAtParticularTime.get(12 + Integer.parseInt(timeArr[0]));
        }
        return this.usersAtParticularTime.get(Integer.parseInt(timeArr[0]));
    }

private static Date cvtToGmt( Date date )
{
   TimeZone tz = TimeZone.getDefault();
   Date ret = new Date( date.getTime() - tz.getRawOffset() );
   // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
   if ( tz.inDaylightTime( ret ))
   {
      Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );
      // check to make sure we have not crossed back into standard time
      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
      if ( tz.inDaylightTime( dstDate ))
      {
         ret = dstDate;
      }
   }
   return ret;
}
}