package com.example.btl_android.login;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.btl_android.Fragment.UserFragment;
import com.example.btl_android.R;

public class LoginDialogFragment extends DialogFragment {

    // Interface để thông báo khi đăng nhập thành công
    public interface OnLoginSuccessListener {
        void onLoginSuccess();
    }
    private EditText etPhoneNumber, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private DBManager dbManager;

    private OnLoginSuccessListener loginSuccessListener;


    public LoginDialogFragment() {
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dialog, container, false);

        // Khởi tạo các view
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);


        // Khởi tạo DBManager
        dbManager = new DBManager(getContext());
        dbManager.open(); // Mở kết nối database


        btnLogin.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();



            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra đăng nhập từ cơ sở dữ liệu
                String[] userInfo = dbManager.checkUserLogin(phoneNumber, password);

                if (userInfo != null) {
                    // Nếu đăng nhập thành công, lưu trạng thái đăng nhập
                    String id = userInfo[0]; // id
                    String fullName = userInfo[1]; // fullName
                    String email = userInfo[2]; //email

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);  // Lưu trạng thái đã đăng nhập
                    editor.putString("userName", fullName); // Lưu tên người dùng
//                    editor.putString("userId", id); // Lưu id người dùng
                    editor.putString("userId", String.valueOf(id)); // Lưu dưới dạng chuỗi
                    editor.putString("phoneNumber", phoneNumber);
                    editor.putString("email", email);
                    editor.apply();  // Lưu thay đổi

                    // Thông báo đăng nhập thành công
                    Toast.makeText(getActivity(), fullName + " đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // Gọi callback để cập nhật UserFragment
                    if (loginSuccessListener != null) {
                        loginSuccessListener.onLoginSuccess();
                    }

                    // Đóng dialog sau khi đăng nhập thành công
                    dismiss();
                } else {
                    // Nếu sai thông tin đăng nhập, thông báo lỗi
                    Toast.makeText(getActivity(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });




        tvRegister.setOnClickListener(v -> {
            SignDialogFragment signDialog = new SignDialogFragment();
            signDialog.show(getChildFragmentManager(), "SignDialogFragment");

            // Đóng dialog
//            dismiss();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Gắn listener là UserFragment hoặc Activity
            loginSuccessListener = (OnLoginSuccessListener) getParentFragment(); // Hoặc getActivity() nếu sử dụng trong Activity
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment cha phải implement OnLoginSuccessListener");
        }
    }


}
