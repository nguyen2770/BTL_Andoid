package com.example.btl_android.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android.R;
import com.example.btl_android.login.LoginDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements LoginDialogFragment.OnLoginSuccessListener {


    TextView txtUserName;
    Button btnLogOut, btnLogIn;
    ImageView ic_account;

    @Override
    public void onLoginSuccess() {
        // Cập nhật giao diện khi đăng nhập thành công
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Người dùng");
        String userId = sharedPreferences.getString("userId", ""); // Lấy userId từ SharedPreferences
        txtUserName.setText(" " + userName); // Hiển thị cả tên và ID
        txtUserName.setVisibility(View.VISIBLE);
        btnLogOut.setVisibility(View.VISIBLE);
        btnLogIn.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Xin chào " + userName, Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        txtUserName = view.findViewById(R.id.txtUserName);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogIn = view.findViewById(R.id.btnLogIn);
        ic_account = view.findViewById(R.id.account_ic_imgv);
        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

//        if (isLoggedIn) {
//            // Người dùng đã đăng nhập
//            String userName = sharedPreferences.getString("userName", "Người dùng");
//
//            txtUserName.setText("Xin chào, " + userName);
//            txtUserName.setVisibility(View.VISIBLE);
//            btnLogOut.setVisibility(View.VISIBLE);
//            btnLogIn.setVisibility(View.GONE);
//
//            Toast.makeText(getActivity(), "Xin chào " + userName, Toast.LENGTH_SHORT).show();
        if (isLoggedIn) {
            // Người dùng đã đăng nhập
            String userName = sharedPreferences.getString("userName", "Người dùng");
            String userId = sharedPreferences.getString("userId", ""); // Lấy userId

            txtUserName.setText(" " + userName); // Hiển thị cả tên và ID
            txtUserName.setVisibility(View.VISIBLE);
            btnLogOut.setVisibility(View.VISIBLE);
            btnLogIn.setVisibility(View.GONE);

            Toast.makeText(getActivity(), "Xin chào " + userName, Toast.LENGTH_SHORT).show();
        } else {
            // Người dùng chưa đăng nhập
            txtUserName.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
        }

        // Sự kiện nút Đăng nhập
        btnLogIn.setOnClickListener(v -> {
            LoginDialogFragment loginDialog = new LoginDialogFragment();
            loginDialog.show(getChildFragmentManager(), "LoginDialogFragment");
        });

        // Xử lý sự kiện cho nút Đăng xuất
        btnLogOut.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);  // Xóa trạng thái đăng nhập
            editor.remove("userName");
            editor.remove("userId");// Xóa tên người dùng
            editor.apply();  // Lưu thay đổi

            // Cập nhật lại giao diện
            txtUserName.setVisibility(View.GONE);  // Ẩn tên người dùng
            btnLogOut.setVisibility(View.GONE);  // Ẩn nút đăng xuất
            btnLogIn.setVisibility(View.VISIBLE);  // Hiển thị nút đăng nhập

            // Hiển thị thông báo đăng xuất thành công
            Toast.makeText(getActivity(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}