package com.example.thuchanhbuoi2;

import java.io.Serializable;

public class Contact implements Serializable {
    // Khóa dùng khi truyền dữ liệu qua Intent
    public static final String EXTRA_CONTACT = "extra_contact";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PHONES = "extra_phones";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    private String name;
    private String phone;
    private String group;
    private String gender;
    private boolean favorite;
    private String note;

    public Contact(String name, String phone, String group, String gender,
                   boolean favorite, String note) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.note = note;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getInitial() {
        String n = name == null ? "" : name.trim();
        if (n.isEmpty()) return "?";
        String[] parts = n.split("\\s+");
        String last = parts[parts.length - 1];
        return last.substring(0, 1).toUpperCase();
    }

    public String getFormattedPhone() {
        if (phone != null && phone.length() == 10) {
            return phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7);
        }
        return phone;
    }
}
