package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PriorityActivity extends BifurcationActivity {

    private EditText startTimeInput, endTimeInput;
    private List<EditText> subjectInputs = new ArrayList<>();
    private List<CheckBox> completionCheckboxes = new ArrayList<>();
    private Button viewScheduleButton;
    private static final int BREAK_TIME = 10; // Break time in minutes between subjects
    private static final int MEAL_BREAK_TIME = 30; // Meal break time in minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority);

        startTimeInput = findViewById(R.id.editTextStartTime);
        endTimeInput = findViewById(R.id.editTextEndTime);
        viewScheduleButton = findViewById(R.id.btnViewSchedule);

        subjectInputs.add(findViewById(R.id.editTextText));   // Priority 1
        subjectInputs.add(findViewById(R.id.editTextText3));  // Priority 2
        subjectInputs.add(findViewById(R.id.editTextText4));  // Priority 3
        subjectInputs.add(findViewById(R.id.editTextText5));  // Priority 4
        subjectInputs.add(findViewById(R.id.editTextText6));  // Priority 5
        subjectInputs.add(findViewById(R.id.editTextText7));  // Priority 6

        completionCheckboxes.add(findViewById(R.id.checkboxSubject1));  // Subject 1 completion
        completionCheckboxes.add(findViewById(R.id.checkboxSubject2));  // Subject 2 completion
        completionCheckboxes.add(findViewById(R.id.checkboxSubject3));  // Subject 3 completion
        completionCheckboxes.add(findViewById(R.id.checkboxSubject4));  // Subject 4 completion
        completionCheckboxes.add(findViewById(R.id.checkboxSubject5));  // Subject 5 completion
        completionCheckboxes.add(findViewById(R.id.checkboxSubject6));  // Subject 6 completion

        viewScheduleButton.setOnClickListener(v -> generateSchedule());
    }

    private void generateSchedule() {
        String startStr = startTimeInput.getText().toString();
        String endStr = endTimeInput.getText().toString();

        if (startStr.isEmpty() || endStr.isEmpty()) {
            showDialog("Error", "Please enter both start and end times.");
            return;
        }

        int startMinutes = parseTime(startStr);
        int endMinutes = parseTime(endStr);
        int totalMinutes = endMinutes - startMinutes;

        if (totalMinutes <= 0) {
            showDialog("Error", "End time must be after start time.");
            return;
        }

        List<Subject> subjectList = new ArrayList<>();
        int priority = 1;
        for (EditText subjectInput : subjectInputs) {
            String name = subjectInput.getText().toString().trim();
            if (!name.isEmpty()) {
                subjectList.add(new Subject(name, priority));
            }
            priority++;
        }

        if (subjectList.isEmpty()) {
            showDialog("Error", "Please enter at least one subject.");
            return;
        }

        // Assign weights: Priority 1 gets highest weight
        int totalWeight = 0;
        for (Subject subject : subjectList) {
            subject.weight = 7 - subject.priority; // Priority 1 = weight 6, Priority 6 = weight 1
            totalWeight += subject.weight;
        }

        // Calculate total break time (regular breaks + meal break)
        int totalBreakTime = (subjectList.size() - 1) * BREAK_TIME;
        totalBreakTime += MEAL_BREAK_TIME; // Adding the meal break time

        // Adjust total available time after considering breaks
        totalMinutes -= totalBreakTime;

        // Distribute time
        StringBuilder schedule = new StringBuilder();
        int currentTime = startMinutes;

        for (Subject subject : subjectList) {
            int timeForSubject = (subject.weight * totalMinutes) / totalWeight;
            String timeSlot = formatTime(currentTime) + " - " + formatTime(currentTime + timeForSubject);
            schedule.append(subject.name).append(" → ").append(timeSlot).append("\n");
            currentTime += timeForSubject;

            // Add break after each subject except the last one
            if (subject != subjectList.get(subjectList.size() - 1)) {
                currentTime += BREAK_TIME; // Adding regular break
                schedule.append("Break → ").append(formatTime(currentTime - BREAK_TIME) + " - " + formatTime(currentTime)).append("\n");
            }

            // Add meal break after a specific number of subjects, e.g., after 2 subjects
            if (subjectList.indexOf(subject) == 2) {
                currentTime += MEAL_BREAK_TIME; // Adding meal break
                schedule.append("Meal Break → ").append(formatTime(currentTime - MEAL_BREAK_TIME) + " - " + formatTime(currentTime)).append("\n");
            }
        }

        // Check completion status
        int completedSubjects = 0;
        for (CheckBox checkbox : completionCheckboxes) {
            if (checkbox.isChecked()) {
                completedSubjects++;
            }
        }

        String completionMessage = "Completed " + ((completedSubjects * 100) / subjectList.size()) + "%";
        showDialog("Generated Schedule", schedule.toString() + "\n" + completionMessage);
    }

    private int parseTime(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } catch (Exception e) {
            return -1;
        }
    }

    private String formatTime(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return String.format("%02d:%02d", h, m);
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(PriorityActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    static class Subject {
        String name;
        int priority;
        int weight;

        Subject(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }
}