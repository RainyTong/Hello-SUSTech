package com.example.xuversion.class_add_activity;

public class ClassUtil {
    private static final String[] DAYS = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
    /**
     * set which class time slot is for this class
     * @param i representing which time slot
     * @return the lecture time
     */
    public static LectureTime getLectureTime(int i){
        LectureTime lectureTime = new LectureTime();
        switch (i){
            case 1:
                lectureTime.setDajie("一、二节");
                lectureTime.setTime("08:00-09:50");
                break;
            case 2:
                lectureTime.setDajie("三、四节");
                lectureTime.setTime("10:20-12:10");
                break;
            case 3:
                lectureTime.setDajie("五、六节");
                lectureTime.setTime("13:00-15:50");
                break;
            case 4:
                lectureTime.setDajie("七、八节");
                lectureTime.setTime("16:20-18:10");
                break;
            case 5:
                lectureTime.setDajie("九、十节");
                lectureTime.setTime("19:00-20:50");
                break;
            case 6:
                lectureTime.setDajie("十一节");
                lectureTime.setTime("21:00-22:00");
                break;
            default:
                break;
        }
        return lectureTime;
    }

    /**
     * get week day
     * @param i the index
     * @return the name of week day
     */
    public static String getWeekDay(int i){
        return DAYS[i-1];
    }



}
