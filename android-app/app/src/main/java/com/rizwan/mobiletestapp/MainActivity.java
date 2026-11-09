package com.rizwan.mobiletestapp;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private LinearLayout root;
    private String savedName = "";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showLogin();
    }

    // Density independent padding; also keeps content clear of the status bar on Android 15 edge to edge.
    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private TextView text(String value, int size) {
        TextView view = new TextView(this);
        view.setText(value); view.setTextSize(size); view.setPadding(0, 12, 0, 12);
        return view;
    }

    private Button button(String label, String id, Runnable action) {
        Button b = new Button(this); b.setText(label); b.setAllCaps(false);
        b.setContentDescription(label);
        b.setOnClickListener(v -> action.run());
        b.setTag(id);
        return b;
    }

    private EditText input(String hint, String id) {
        EditText e = new EditText(this); e.setHint(hint); e.setSingleLine(true);
        e.setContentDescription(id); e.setTag(id);
        return e;
    }

    private void base(String title) {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(56), dp(16), dp(16)); root.setGravity(Gravity.TOP);
        setContentView(root);
        TextView heading = text(title, 26); heading.setTypeface(null, Typeface.BOLD);
        root.addView(heading);
    }

    private void showLogin() {
        base("Mobile Test App");
        root.addView(text("Sign in to continue", 16));
        EditText email = input("Email", "email"); email.setId(View.generateViewId()); email.setTag("email");
        EditText password = input("Password", "password"); password.setId(View.generateViewId()); password.setTag("password");
        password.setInputType(129);
        root.addView(email); root.addView(password);
        TextView error = text("", 14); error.setTextColor(0xffb00020); error.setTag("login-error"); error.setContentDescription("login-error");
        root.addView(error);
        root.addView(button("Sign in", "login-button", () -> {
            if ("qa@example.com".equals(email.getText().toString().trim()) && "Password123!".equals(password.getText().toString())) {
                showHome();
            } else error.setText("Invalid email or password");
        }));
    }

    private void showHome() {
        base("Home");
        root.addView(text("Welcome, QA Engineer", 18));
        root.addView(button("Open profile", "profile-button", this::showProfile));
        root.addView(button("Log out", "logout-button", this::showLogin));
    }

    private void showProfile() {
        base("Profile");
        root.addView(text("Update your display name", 16));
        EditText name = input("Full name", "profile-name");
        name.setText(savedName);
        root.addView(name);
        TextView error = text("", 14); error.setTextColor(0xffb00020); error.setTag("profile-error"); error.setContentDescription("profile-error");
        root.addView(error);
        root.addView(button("Save profile", "save-profile-button", () -> {
            String value = name.getText().toString().trim();
            if (value.isEmpty()) error.setText("Name is required");
            else { savedName = value; showProfileSaved(); }
        }));
        root.addView(button("Back", "back-button", this::showHome));
    }

    private void showProfileSaved() {
        base("Profile saved");
        root.addView(text("Profile updated successfully", 16));
        root.addView(text("Saved name: " + savedName, 14));
        root.addView(button("Back to home", "home-button", this::showHome));
    }
}
