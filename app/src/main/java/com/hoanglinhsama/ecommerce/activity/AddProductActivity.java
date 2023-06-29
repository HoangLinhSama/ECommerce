package com.hoanglinhsama.ecommerce.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hoanglinhsama.ecommerce.databinding.ActivityAddProductBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private ActivityAddProductBinding activityAddProductBinding;
    private int typeProduct;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int REQUEST_CODE = 7;
    private Intent intentGallery;
    private Product productModify;
    private boolean isModify = false; // flag kiem tra sua hay them moi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddProductBinding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(activityAddProductBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            getEventAddProduct();
            getEventSelectPicture();
            setRegisterForActivityResult();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Nhan ket qua tra ve tu mot Activity khac (cai Activity duoc no goi bang startActivityForResult )
     */
    private void setRegisterForActivityResult() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                getPicturePath(data);
            }
        });
    }

    /**
     * Lay ra duong dan cua hinh anh tren server sau khi upload thanh cong len server
     */
    private void getPicturePath(Intent data) {
        Uri uri = data.getData();
        String realPath = getRealPathFromURI(uri);
        File file = new File(realPath);
        String filePath = file.getAbsolutePath();
        String[] arrayFileName = filePath.split("\\.");
        filePath = arrayFileName[0] + System.currentTimeMillis() + "." + arrayFileName[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pictureProduct", filePath, requestBody);
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.upLoadPictureProduct(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    activityAddProductBinding.textViewPictureAddProductScreen.setText(ApiUtils.baseUrl + "picture/product/" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("setRegisterForActivityResult", t.getMessage());
            }
        });
    }

    private void getEventSelectPicture() {
        activityAddProductBinding.imageButtonGalleryAddProductScreen.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(intentGallery); // execute ActivityResultContract (ActivityResultContract thay cho REQUEST_CODE)
            } else {
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        });
    }

    /**
     * Nhan ket qua tu viec yeu cau cap quyen
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    activityResultLauncher.launch(intentGallery);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getEventAddProduct() {
        activityAddProductBinding.buttonAddProductScreen.setOnClickListener(v -> {
            if (!isModify) { // them moi
                if (TextUtils.isEmpty(activityAddProductBinding.editTextNameAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập tên sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextPriceAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập giá sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextQuantityAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập số lượng sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextDescriptionAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập mô tả sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.textViewPictureAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa chọn hình ảnh sản phẩm !", Toast.LENGTH_SHORT).show();
                } else {
                    DataClient dataClient = ApiUtils.getData();
                    Call<String> call = dataClient.addProduct(activityAddProductBinding.editTextNameAddProductScreen.getText().toString().trim()
                            , Long.parseLong(activityAddProductBinding.editTextPriceAddProductScreen.getText().toString().trim())
                            , activityAddProductBinding.textViewPictureAddProductScreen.getText().toString().trim()
                            , activityAddProductBinding.editTextDescriptionAddProductScreen.getText().toString().trim()
                            , typeProduct
                            , Integer.parseInt(activityAddProductBinding.editTextQuantityAddProductScreen.getText().toString().trim()));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddProductActivity.this, ProductManageActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("getEventAddProduct", t.getMessage());
                        }
                    });
                }
            } else { // sua
                if (TextUtils.isEmpty(activityAddProductBinding.editTextNameAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập tên sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextPriceAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập giá sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextQuantityAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập số lượng sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.editTextDescriptionAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa nhập mô tả sản phẩm !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activityAddProductBinding.textViewPictureAddProductScreen.getText().toString().trim())) {
                    Toast.makeText(this, "Chưa chọn hình ảnh sản phẩm !", Toast.LENGTH_SHORT).show();
                } else {
                    DataClient dataClient = ApiUtils.getData();
                    Call<String> call = dataClient.updateProduct(productModify.getId()
                            , activityAddProductBinding.editTextNameAddProductScreen.getText().toString().trim()
                            , Long.parseLong(activityAddProductBinding.editTextPriceAddProductScreen.getText().toString().trim())
                            , Integer.parseInt(activityAddProductBinding.editTextQuantityAddProductScreen.getText().toString().trim())
                            , activityAddProductBinding.editTextDescriptionAddProductScreen.getText().toString().trim()
                            , typeProduct
                            , activityAddProductBinding.textViewPictureAddProductScreen.getText().toString().trim());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddProductActivity.this, "Sửa sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddProductActivity.this, ProductManageActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("getEventAddProduct", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        /* Khoi tao Intent */
        intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");

        /* Khoi tao Spinner */
        List<String> listTypeProduct = new ArrayList<>();
        listTypeProduct.add("Điện Thoại");
        listTypeProduct.add("Lap Top");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listTypeProduct);
        activityAddProductBinding.spinnerAddProductScreen.setAdapter(adapterSpinner);
        activityAddProductBinding.spinnerAddProductScreen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeProduct = position + 1; // dien thoai type 1, laptop type 2
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Nhan Intent tu ProductManageActivity */
        productModify = (Product) getIntent().getSerializableExtra("Product Modify");

        /* Kiem tra la sua hay them moi */
        if (productModify == null) { // them moi
            isModify = false; // khong phai sua, thi la them moi
        } else {
            activityAddProductBinding.toolBarAddProductScreen.setTitle("Sửa sản phẩm");
            activityAddProductBinding.buttonAddProductScreen.setText("Sửa sản phẩm");
            isModify = true; // sua

            /* Khoi tao gia tri cua san pham can sua */
            activityAddProductBinding.editTextNameAddProductScreen.setText(productModify.getName());
            activityAddProductBinding.editTextPriceAddProductScreen.setText(String.valueOf(productModify.getPrice()));
            activityAddProductBinding.editTextQuantityAddProductScreen.setText(String.valueOf(productModify.getQuantity()));
            activityAddProductBinding.editTextDescriptionAddProductScreen.setText(productModify.getDescription());
            activityAddProductBinding.textViewPictureAddProductScreen.setText(productModify.getPicture());
            activityAddProductBinding.spinnerAddProductScreen.setSelection(productModify.getType() - 1);
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityAddProductBinding.toolBarAddProductScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityAddProductBinding.toolBarAddProductScreen.setNavigationOnClickListener(v -> finish());
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}