package com.example.thuchanhbuoi2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private static final int[] AVATAR_COLORS = {
            Color.parseColor("#4F46E5"),
            Color.parseColor("#EC4899"),
            Color.parseColor("#0D9488"),
            Color.parseColor("#F97316"),
            Color.parseColor("#7C3AED"),
            Color.parseColor("#2563EB")
    };

    private final LayoutInflater inflater;

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> contacts) {
        super(context, 0, contacts);
        inflater = LayoutInflater.from(context);
    }

    public static void applyAvatar(TextView tv, Contact c) {
        tv.setText(c.getInitial());
        int color = AVATAR_COLORS[Math.abs(c.getName().hashCode()) % AVATAR_COLORS.length];
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(color);
        tv.setBackground(bg);
    }

    static class ViewHolder {
        TextView tvAvatar, tvName, tvPhone, tvGroup, tvStar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_contact, parent, false);
            h = new ViewHolder();
            h.tvAvatar = convertView.findViewById(R.id.tvAvatar);
            h.tvName = convertView.findViewById(R.id.tvName);
            h.tvPhone = convertView.findViewById(R.id.tvPhone);
            h.tvGroup = convertView.findViewById(R.id.tvGroup);
            h.tvStar = convertView.findViewById(R.id.tvStar);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        Contact c = getItem(position);
        if (c != null) {
            applyAvatar(h.tvAvatar, c);
            h.tvName.setText(c.getName());
            h.tvPhone.setText(c.getFormattedPhone());
            h.tvGroup.setText(c.getGroup());
            h.tvStar.setVisibility(c.isFavorite() ? View.VISIBLE : View.GONE);
        }
        return convertView;
    }
}
