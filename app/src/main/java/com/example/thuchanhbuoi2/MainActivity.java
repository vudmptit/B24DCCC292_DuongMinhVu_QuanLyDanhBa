package com.example.thuchanhbuoi2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Danh sách liên hệ lưu trong bộ nhớ khi ứng dụng chạy
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;
    private TextView tvTotal;
    private TextView tvFavorite;

    // Nhận kết quả từ màn hình Thêm liên hệ
    private final ActivityResultLauncher<Intent> addLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    @SuppressWarnings("deprecation")
                    Contact c = (Contact) result.getData().getSerializableExtra(Contact.EXTRA_CONTACT);
                    if (c != null) {
                        contacts.add(c);
                        refresh();
                    }
                }
            });

    // Nhận kết quả từ màn hình Chi tiết (cập nhật yêu thích / xóa)
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() != RESULT_OK || data == null) return;

                int position = data.getIntExtra(Contact.EXTRA_POSITION, -1);
                String action = data.getStringExtra(Contact.EXTRA_ACTION);
                if (position < 0 || position >= contacts.size() || action == null) return;

                if (Contact.ACTION_DELETE.equals(action)) {
                    contacts.remove(position);
                } else if (Contact.ACTION_UPDATE.equals(action)) {
                    @SuppressWarnings("deprecation")
                    Contact c = (Contact) data.getSerializableExtra(Contact.EXTRA_CONTACT);
                    if (c != null) contacts.set(position, c);
                }
                refresh();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotal = findViewById(R.id.tvTotal);
        tvFavorite = findViewById(R.id.tvFavorite);
        ListView lvContacts = findViewById(R.id.lvContacts);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Dữ liệu mẫu
        contacts.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, ""));
        contacts.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối"));
        contacts.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam", false, ""));

        adapter = new ContactAdapter(this, contacts);
        lvContacts.setAdapter(adapter);
        updateStats();

        lvContacts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(Contact.EXTRA_CONTACT, contacts.get(position));
            intent.putExtra(Contact.EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            // Gửi danh sách số điện thoại hiện có để kiểm tra trùng
            ArrayList<String> phones = new ArrayList<>();
            for (Contact c : contacts) phones.add(c.getPhone());
            intent.putStringArrayListExtra(Contact.EXTRA_PHONES, phones);
            addLauncher.launch(intent);
        });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        updateStats();
    }

    private void updateStats() {
        int favorites = 0;
        for (Contact c : contacts) {
            if (c.isFavorite()) favorites++;
        }
        tvTotal.setText(String.valueOf(contacts.size()));
        tvFavorite.setText(String.valueOf(favorites));
    }
}
