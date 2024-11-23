package com.example.btl_android.login;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.btl_android.Database.DBManager;
import com.example.btl_android.R;

public class SignDialogFragment extends DialogFragment {
    private EditText etFullName, etPhoneNumber, etPassword, etConfirmPassword, etEmail;
    private Button btnSignUp;
    private TextView tvLogin;
    private DBManager dbManager;

    public SignDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_dialog, container, false);

        // Khởi tạo các view
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        tvLogin = view.findViewById(R.id.tvLogin);

        // Khởi tạo DBManager
        dbManager = new DBManager(getContext());
        dbManager.open(); // Mở kết nối cơ sở dữ liệu

        btnSignUp.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim(); //Them
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }else if (!email.endsWith("@gmail.com")) { // Kiểm tra đuôi email
                Toast.makeText(getActivity(), "Email không đúng định dạng @gmail.com", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }else if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra nếu số điện thoại đã tồn tại trong cơ sở dữ liệu
                Cursor cursor = dbManager.getDb().query("User", new String[]{"phoneNumber"}, "phoneNumber = ?", new String[]{phoneNumber}, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    // Số điện thoại đã tồn tại, hiển thị thông báo và focus vào etPhoneNumber
                    Toast.makeText(getActivity(), "Số điện thoại đã tồn tại. Vui lòng nhập số khác.", Toast.LENGTH_SHORT).show();
                    etPhoneNumber.requestFocus();
                    cursor.close();
                } else {
                    // Số điện thoại không tồn tại, thêm người dùng mới
                    dbManager.addUser(fullName, phoneNumber, password, email);
//                    dbManager.addUser(fullName, phoneNumber, password, ""); // Thêm email là "" nếu không có trường email
                    Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    // Đóng SignDialogFragment và mở LoginDialogFragment
                    dismiss();

//                    LoginDialogFragment loginDialog = new LoginDialogFragment();
//                    loginDialog.show(requireActivity().getSupportFragmentManager(), "LoginDialogFragment");
                }
            }
        });



        // Xử lý sự kiện khi nhấn "Đã có tài khoản? Đăng nhập"
        tvLogin.setOnClickListener(v -> {
            // Gọi lại LoginDialogFragment
            LoginDialogFragment loginDialog = new LoginDialogFragment();
            loginDialog.show(requireActivity().getSupportFragmentManager(), "LoginDialogFragment");

            // Đóng dialog
            dismiss();
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Đặt kích thước cho dialog
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            // Đặt background cho dialog để bo góc
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        }
    }
}
