//Problem Statement:
//        Generate a weekly lecture timetable for a classroom (Monday to Friday).
//        Each day has 6 time slots, with a fixed break from 11 AM to 12 PM (slot 3).
//        Assign each subject a fixed number of lectures per week, without repeating on the same day.
//        Display classroom availability and a list of faculty members.
//        Ensure faculty are not scheduled beyond their allotted lecture count.
//Data Structures Used:
//        Map<String, Integer>: Stores required lectures per subject.
//        List<Faculty>: Holds faculty details.
//        Map<String, Faculty>: Maps subjects to faculty.
//        String[][] timetable: Stores the generated schedule.
//        boolean[][] classroomAvailable: Tracks free/occupied slots.
//        Random: Helps assign lectures randomly within rules.


package com.buffer;

import java.util.*;

class Faculty {
    String name;
    String subject;
    int totalClasses;
    int scheduledClasses = 0;

    Faculty(String name, String subject, int totalClasses) {
        this.name = name;
        this.subject = subject;
        this.totalClasses = totalClasses;
    }

    boolean scheduleClass() {
        if (scheduledClasses < totalClasses) {
            scheduledClasses++;
            return true;
        }
        return false;
    }
}

class TimetableScheduler {
    static final int DAYS_PER_WEEK = 5;
    static final int SLOTS_PER_DAY = 6;
    static final int BREAK_SLOT = 3; // 11-12pm
    static final String[] TIMESLOTS = {"8-9am", "9-10am", "10-11am", "11-12pm (Break)", "1-2pm", "2-3pm"};
    static final String[] WEEKDAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};

    String[][] timetable = new String[DAYS_PER_WEEK][SLOTS_PER_DAY];
    Map<String, Integer> subjectLectureCount;
    Map<String, Faculty> facultyMap;
    boolean[][] classroomAvailable;

    TimetableScheduler(Map<String, Integer> subjectLectureCount, List<Faculty> facultyList) {
        this.subjectLectureCount = new HashMap<>(subjectLectureCount);
        this.facultyMap = new HashMap<>();
        for (Faculty faculty : facultyList) {
            facultyMap.put(faculty.subject, faculty);
        }

        this.classroomAvailable = new boolean[DAYS_PER_WEEK][SLOTS_PER_DAY];
        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            Arrays.fill(classroomAvailable[i], true);
            classroomAvailable[i][BREAK_SLOT] = false;
            timetable[i][BREAK_SLOT] = "Break";
        }
    }

    public void generateTimetable() {
        Random rand = new Random();
        List<String> subjects = new ArrayList<>(subjectLectureCount.keySet());

        for (String subject : subjects) {
            int remainingLectures = subjectLectureCount.get(subject);
            Faculty faculty = facultyMap.get(subject);

            int tries = 0;
            while (remainingLectures > 0 && tries < 1000) {
                int day = rand.nextInt(DAYS_PER_WEEK);

                // Check if subject is already scheduled that day
                boolean subjectExistsToday = false;
                for (int s = 0; s < SLOTS_PER_DAY; s++) {
                    if (subject.equals(timetable[day][s])) {
                        subjectExistsToday = true;
                        break;
                    }
                }
                if (subjectExistsToday) {
                    tries++;
                    continue;
                }

                int slot = rand.nextInt(SLOTS_PER_DAY);
                if (slot == BREAK_SLOT) {
                    tries++;
                    continue;
                }

                if (classroomAvailable[day][slot] && faculty.scheduleClass()) {
                    timetable[day][slot] = subject;
                    classroomAvailable[day][slot] = false;
                    remainingLectures--;
                }
                tries++;
            }
        }

        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            for (int j = 0; j < SLOTS_PER_DAY; j++) {
                if (timetable[i][j] == null)
                    timetable[i][j] = "-";
            }
        }
    }

    public void displayTimetable() {
        System.out.println("\nGenerated Timetable:");
        System.out.printf("%-10s", "");
        for (String time : TIMESLOTS) {
            System.out.printf("%-20s", time);
        }
        System.out.println();

        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            System.out.printf("%-10s", WEEKDAYS[i]);
            for (int j = 0; j < SLOTS_PER_DAY; j++) {
                System.out.printf("%-20s", timetable[i][j]);
            }
            System.out.println();
        }
    }

    public void displayClassroomAvailability() {
        System.out.println("\nClassroom 25 Availability:");
        System.out.printf("%-10s", "");
        for (String time : TIMESLOTS) {
            System.out.printf("%-20s", time);
        }
        System.out.println();

        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            System.out.printf("%-10s", WEEKDAYS[i]);
            for (int j = 0; j < SLOTS_PER_DAY; j++) {
                String status = classroomAvailable[i][j] ? "Free" : "Booked";
                System.out.printf("%-20s", status);
            }
            System.out.println();
        }
    }
}

public class ByteKnights {
    public static void main(String[] args) {
        Map<String, Integer> subjectLectureCount = new LinkedHashMap<>();
        List<Faculty> facultyList = new ArrayList<>();

        subjectLectureCount.put("EG", 4);
        subjectLectureCount.put("SE", 3);
        subjectLectureCount.put("LL", 1);
        subjectLectureCount.put("Phy", 3);
        subjectLectureCount.put("MVC", 4);
        subjectLectureCount.put("OOPJ", 3);

        facultyList.add(new Faculty("Mr.Mali", "EG", 4));
        facultyList.add(new Faculty("Mr.Deore", "SE", 3));
        facultyList.add(new Faculty("Mrs.Patil", "LL", 1));
        facultyList.add(new Faculty("Mrs.Nair", "Phy", 3));
        facultyList.add(new Faculty("Mrs.Purandare", "MVC", 4));
        facultyList.add(new Faculty("Mrs.Mane", "OOPJ", 3));

        System.out.println("\nClassroom: 25");
        System.out.println("Division: D");

        TimetableScheduler scheduler = new TimetableScheduler(subjectLectureCount, facultyList);
        scheduler.generateTimetable();
        scheduler.displayTimetable();

        System.out.println("\nFaculty List:");
        System.out.printf("%-20s%-10s\n", "Name", "Subject");
        for (Faculty faculty : facultyList) {
            System.out.printf("%-20s%-10s\n", faculty.name, faculty.subject);
        }

        scheduler.displayClassroomAvailability();
    }
}

