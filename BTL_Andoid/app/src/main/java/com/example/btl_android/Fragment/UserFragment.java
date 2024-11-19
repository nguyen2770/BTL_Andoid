package com.example.btl_android.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android.Database.DBManager;
import com.example.btl_android.R;
import com.example.btl_android.login.LoginDialogFragment;


public class UserFragment extends Fragment implements LoginDialogFragment.OnLoginSuccessListener {


    @Override
    public void onLoginSuccess() {
        // Cập nhật giao diện khi đăng nhập thành công
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Người dùng");
        String userId = sharedPreferences.getString("userId", ""); // Lấy userId từ SharedPreferences
        txtUserName.setText(" " + userName); // Hiển thị cả tên và ID
        txtUserName.setVisibility(View.VISIBLE);
        btnLogOut.setVisibility(View.VISIBLE);
        btnLogIn.setVisibility(View.GONE);
        ic_account.setImageResource(R.drawable.tunebox);
    }


    TextView txtUserName;
    Button btnLogOut, btnLogIn;
    ImageView ic_account;

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
    private DBManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        txtUserName = view.findViewById(R.id.txtUserName);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogIn = view.findViewById(R.id.btnLogIn);
        ic_account = view.findViewById(R.id.account_ic_imgv);
        dbManager = new DBManager(getActivity());

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);


        if (isLoggedIn) {
            // Người dùng đã đăng nhập
            String userName = sharedPreferences.getString("userName", "Người dùng");
            String userId = sharedPreferences.getString("userId", ""); // Lấy userId

            txtUserName.setText(" " + userName); // Hiển thị cả tên và ID
            txtUserName.setVisibility(View.VISIBLE);
            btnLogOut.setVisibility(View.VISIBLE);
            btnLogIn.setVisibility(View.GONE);
            ic_account.setImageResource(R.drawable.tunebox);
            //Lay thong tin nguoi dung
            ic_account.setOnClickListener(v -> {
                // Tạo và cấu hình Dialog
                Dialog dialog = new Dialog(getActivity()); // Sử dụng "this" nếu trong Activity, hoặc getActivity() nếu trong Fragment
                dialog.setContentView(R.layout.dialog_acc_info);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
                // Thiết lập kích thước cụ thể cho Dialog
                // Thiết lập kích thước của Dialog
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng full màn hình
                params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Chiều cao tự động vừa nội dung
                dialog.getWindow().setAttributes(params);


                dialog.setTitle("Thông tin tài khoản");

                // Lấy thông tin người dùng từ SharedPreferences


                String phoneNumber = sharedPreferences.getString("phoneNumber", "Không rõ");
                String email = sharedPreferences.getString("email", "");

                // Gán thông tin vào TextView
                TextView tvUserName = dialog.findViewById(R.id.tvUserName);
                TextView tvPhoneNumber = dialog.findViewById(R.id.tvPhoneNumber);
                TextView tvEmail = dialog.findViewById(R.id.tvEmail);
                tvUserName.setText("Tên: " + userName);
                tvEmail.setText("Email: "+email);
                tvPhoneNumber.setText("SĐT: " + phoneNumber);

                // Thiết lập nút Đóng
                Button btnClose = dialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Button btnEdit = dialog.findViewById(R.id.btnEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Tạo một dialog mới cho việc chỉnh sửa thông tin
                        Dialog editDialog = new Dialog(getActivity());
                        editDialog.setContentView(R.layout.dialog_edit_info); // Layout mới cho việc chỉnh sửa thông tin
                        editDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);

                        // Thiết lập kích thước dialog
                        WindowManager.LayoutParams editParams = editDialog.getWindow().getAttributes();
                        editParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        editParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        editDialog.getWindow().setAttributes(editParams);

                        // Lấy thông tin hiện tại từ SharedPreferences
                        String currentUserName = sharedPreferences.getString("userName", "Người dùng");
                        String currentPhoneNumber = sharedPreferences.getString("phoneNumber", "Không rõ");
                        String currentEmail = sharedPreferences.getString("email", "");

                        // Tham chiếu tới các EditText trong dialog chỉnh sửa
                        EditText editUserName = editDialog.findViewById(R.id.editUserName);
                        EditText editPhoneNumber = editDialog.findViewById(R.id.editPhoneNumber);
                        EditText editEmail = editDialog.findViewById(R.id.editEmail);

                        // Hiển thị thông tin hiện tại vào các EditText
                        editUserName.setText(currentUserName);
                        editPhoneNumber.setText(currentPhoneNumber);
                        editEmail.setText(currentEmail);



                        // Nút Đóng
                        Button btnCloseEdit = editDialog.findViewById(R.id.btnCloseEdit);
                        btnCloseEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editDialog.dismiss();
                            }
                        });
                        //Nút Update
                        Button btnUpdate = editDialog.findViewById(R.id.btnUpdate);
                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String newUserName = editUserName.getText().toString().trim();
                                String newPhoneNumber = editPhoneNumber.getText().toString().trim();
                                String newEmail = editEmail.getText().toString().trim();

                                String userId = sharedPreferences.getString("userId", "");
                                if (!userId.isEmpty()) {
                                    // Mở kết nối đến database
                                    dbManager.open();
                                    // Gọi phương thức updateUser để cập nhật thông tin
                                    dbManager.updateUser(Integer.parseInt(userId), newUserName, newPhoneNumber, null, newEmail);
                                    // Cập nhật lại thông tin trong SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userName", newUserName);
                                    editor.putString("phoneNumber", newPhoneNumber);
                                    editor.putString("email", newEmail);
                                    editor.apply();
                                    // Cập nhật giao diện với thông tin mới
                                    txtUserName.setText(" " + newUserName);
                                    // Đóng dialog chỉnh sửa
                                    editDialog.dismiss();
                                    // Đóng kết nối đến database
                                    dbManager.close();
                                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        // Hiển thị dialog chỉnh sửa
                        editDialog.show();
                    }
                });



                // Hiển thị Dialog
                dialog.show();
            });
        } else {
            // Người dùng chưa đăng nhập
            txtUserName.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
            ic_account.setEnabled(false);

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
            editor.remove("userName");  // Xóa tên người dùng
            editor.remove("userId");
            editor.apply();  // Lưu thay đổi

            // Cập nhật lại giao diện
            txtUserName.setVisibility(View.GONE);  // Ẩn tên người dùng
            btnLogOut.setVisibility(View.GONE);  // Ẩn nút đăng xuất
            btnLogIn.setVisibility(View.VISIBLE);  // Hiển thị nút đăng nhập
            ic_account.setImageResource(R.drawable.account_img);
            ic_account.setEnabled(false);


            // Hiển thị thông báo đăng xuất thành công
            Toast.makeText(getActivity(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });





        return view;





    }


}