package com.example.simplewomensafetyapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Sign extends AppCompatActivity {

    private Map<Integer, String> emergencySigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);

        initializeEmergencySigns();

        LinearLayout emergencyLayout = findViewById(R.id.emergencySignsLayout);

        for (Map.Entry<Integer, String> entry : emergencySigns.entrySet()) {
            addEmergencySign(emergencyLayout, entry.getKey(), entry.getValue());
        }
        // Loop through emergency signs
        for (Map.Entry<Integer, String> entry : emergencySigns.entrySet()) {
            addEmergencySign(emergencyLayout, entry.getKey(), entry.getValue());
        }

// ✅ Add this block right below the loop
// Add a header for the IPC laws section
        TextView lawsHeader = new TextView(this);
        lawsHeader.setText("Important Laws for Women's Safety");
        lawsHeader.setTextSize(18);
        lawsHeader.setPadding(0, 32, 0, 16);
        lawsHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        emergencyLayout.addView(lawsHeader);

// Add detailed info about each law
        String[] laws = {
                "• IPC Section 354 – Assault on a woman with intent to outrage her modesty.",
                "• IPC Section 354D – Stalking, physically or through electronic means.",
                "• IPC Section 509 – Words/gestures to insult a woman's modesty.",
                "• IPC Sections 375 & 376 – Defines and punishes rape.",
                "• Dowry Prohibition Act, 1961 – Prohibits giving or taking dowry.",
                "• Domestic Violence Act, 2005 – Protection from physical, mental, or verbal abuse.",
                "• Workplace Harassment Act, 2013 – Safeguards women at workplaces.",
                "• POCSO Act, 2012 – Protection of children from sexual offenses."
        };

        for (String law : laws) {
            TextView lawText = new TextView(this);
            lawText.setText(law);
            lawText.setTextSize(15);
            lawText.setPadding(0, 8, 0, 0);
            emergencyLayout.addView(lawText);
        }

    }

    private void initializeEmergencySigns() {
        emergencySigns = new LinkedHashMap<>(); // keeps order
        emergencySigns.put(R.drawable.thumb_in_palm, "Thumb tucked into palm then closed in a fist — a distress hand signal widely used to indicate silent help.");
        emergencySigns.put(R.drawable.safetysign_h, "Writing or showing 'SOS' on your palm or hand can silently alert others that you're in trouble.");
        emergencySigns.put(R.drawable.interact_eye, "Raising arms in a 'Y' shape — used to gain attention, especially from a distance.");
        emergencySigns.put(R.drawable.palm_to_palm, "Pressing palms together and pushing forward — a signal of needing help or expressing distress.");
        emergencySigns.put(R.drawable.waving_hand, "Waving one hand slowly — a discreet way to ask for attention or help without speaking.");
        //  emergencySigns.put(R.drawable.finger_lip, "Index finger over lips – A silent signal meaning I'm in danger but can't speak. Used when being watched or in a threatening situation where talking is unsafe.");
        // emergencySigns.put(R.drawable.crossed_arm,"Crossed arms over chest – A signal for I need help. Recognized in survival and rescue training. It’s visible from a distance and commonly used to attract help from helicopters or responders.");
        emergencySigns.put(R.drawable.finger_lip, "Index finger over lips – Silent signal meaning you're in danger but can't speak.");
        emergencySigns.put(R.drawable.crossed_arm, "Crossed arms over chest – Used in rescue training to signal 'I need help'.");

    }

    private void addEmergencySign(LinearLayout layout, int imageRes, String description) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setPadding(16, 16, 16, 16);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Image
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(imageRes);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(400, 400);
        imageParams.setMargins(0, 0, 24, 0); // margin between image and text
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Text
        TextView textView = new TextView(this);
        textView.setText(description);
        textView.setTextSize(16);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // weight to fill remaining space
        ));

        // Add views
        rowLayout.addView(imageView);
        rowLayout.addView(textView);
        layout.addView(rowLayout);
    }
}