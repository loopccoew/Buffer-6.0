package com.example.myapplication;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity3 extends BifurcationActivity {

    // Declare variables
    EditText subject1, subject2, subject3, subject4, subject5, subject6;
    EditText hours1, hours2, hours3, hours4, hours5, hours6;
    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6;
    EditText startTime;
    TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize variables
        subject1 = findViewById(R.id.subject1);
        subject2 = findViewById(R.id.subject2);
        subject3 = findViewById(R.id.subject3);
        subject4 = findViewById(R.id.subject4);
        subject5 = findViewById(R.id.subject5);
        subject6 = findViewById(R.id.subject6);

        hours1 = findViewById(R.id.editTextText8);
        hours2 = findViewById(R.id.editTextText9);
        hours3 = findViewById(R.id.editTextText10);
        hours4 = findViewById(R.id.editTextText11);
        hours5 = findViewById(R.id.editTextText12);
        hours6 = findViewById(R.id.editTextText13);

        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox4 = findViewById(R.id.checkbox4);
        checkbox5 = findViewById(R.id.checkbox5);
        checkbox6 = findViewById(R.id.checkbox6);

        startTime = findViewById(R.id.startTime);
        displayText = findViewById(R.id.displayText);
    }

    // Method to calculate and display progress when Save button is clicked
    public void openTimeBasedSchedule(View view) {
        // Clear previous displayed schedule
        displayText.setText("");

        // Get user input for start time
        String startTimeText = startTime.getText().toString();
        if (startTimeText.isEmpty()) {
            Toast.makeText(this, "Please enter a start time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert start time to a calendar object
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar startCalendar;
        try {
            startCalendar = Calendar.getInstance();
            startCalendar.setTime(sdf.parse(startTimeText));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid start time format. Please use HH:mm", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get user input for subjects and hours
        String[] subjectNames = new String[6];
        String[] hourStrings = new String[6];
        EditText[] subjectFields = {subject1, subject2, subject3, subject4, subject5, subject6};
        EditText[] hourFields = {hours1, hours2, hours3, hours4, hours5, hours6};
        CheckBox[] checkboxes = {checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6};

        // Initialize the total hours and completed subjects counter
        float totalHours = 0;
        float completedSubjects = 0;

        // StringBuilder to hold the schedule output
        StringBuilder scheduleOutput = new StringBuilder();

        // Process each subject
        for (int i = 0; i < 6; i++) {
            subjectNames[i] = subjectFields[i].getText().toString();
            hourStrings[i] = hourFields[i].getText().toString();

            if (!subjectNames[i].isEmpty() && !hourStrings[i].isEmpty()) {
                float hours = Float.parseFloat(hourStrings[i]);
                totalHours += hours;

                // Display subject info
                scheduleOutput.append("Subject: ").append(subjectNames[i]).append("\n");
                scheduleOutput.append("Hours: ").append(hours).append("\n");

                // Allocate time based on start time
                String allocatedStartTime = DateFormat.format("HH:mm", startCalendar).toString();
                startCalendar.add(Calendar.HOUR_OF_DAY, (int) hours); // Add hours for this subject

                String allocatedEndTime = DateFormat.format("HH:mm", startCalendar).toString();

                scheduleOutput.append("Allocated Time: ").append(allocatedStartTime).append(" to ").append(allocatedEndTime).append("\n");

                // Add break after the subject (assuming 15 minutes for short breaks, 30 minutes for meal breaks)
                if (i < 5) { // Don't add breaks after the last subject
                    startCalendar.add(Calendar.MINUTE, 15); // Short break after subject
                }

                if (checkboxes[i].isChecked()) {
                    completedSubjects++;
                }

                scheduleOutput.append("------------\n");
            }
        }

        // Calculate percentage of completed study time
        if (totalHours > 0) {
            float progressPercentage = (completedSubjects / 6) * 100;
            String progressMessage = "You have completed " + progressPercentage + "% of your study schedule.";
            scheduleOutput.append("\n").append(progressMessage);
            displayText.setText(scheduleOutput.toString());
        } else {
            displayText.setText("Please fill in all the necessary fields.");
        }

        // Optional: Provide feedback that schedule was saved successfully
        Toast.makeText(this, "Schedule saved!", Toast.LENGTH_SHORT).show();
    }
}
