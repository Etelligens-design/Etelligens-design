package com.pch.ete.helper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pch.ete.R;
import com.pch.ete.interfaces.PickImage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class PickImageDialog extends DialogFragment {
    public static final int REQUEST_TAKE_PHOTO = 901;
    public static final int PICK_IMAGE_REQUEST = 801;
    public static String TAG = DialogFragment.class.getName();
    private File currentPhotoPath;
    private String destinationPath;

    /**
     * @param fragmentManager manager instance
     * @param type            1 for profile image Aspect ration 4*4
     *                        2 for coupons 16*9
     *                        3 for full image 16*16
     */
    public static void newInstance(FragmentManager fragmentManager, int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);

       PickImageDialog fragment = new PickImageDialog();
        fragment.setArguments(args);
        fragment.showNow(fragmentManager, TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_camera)
    void cameraClick() {
        checkPermission(1);
    }

    @OnClick(R.id.btn_gallery)
    void galleryClick() {
        checkPermission(2);
    }

    private void checkPermission(int type) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    if (type == 1)
                        dispatchTakePictureIntent();
                    else
                        pickImageFromGallery();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image;
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.pch.ete.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                cropImage(Uri.fromFile(currentPhotoPath));
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                cropImage(Uri.fromFile( new File(RealPathUtil.getRealPath(getContext(), data.getData()))));
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                File imageFile = new File(RealPathUtil.getRealPath(getContext(), resultUri));

                try {
                    new Compressor(getContext())
                            .setMaxWidth(512)
                            .setMaxHeight(512)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath())
//                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(imageFile);
                    setImage(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/"+imageFile.getName());
                }catch (Exception e){
                    ProgressHelper.dismiss();
                    Toast.makeText(getContext(), "Getting Image Failed", Toast.LENGTH_LONG).show();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Logger.e("CROP ERROR");
        }
    }

    private void cropImage(Uri sourceUri) {
        try {
            destinationPath = createImageFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(destinationPath))
            return;
        int ratioType = getArguments().getInt("type", 1);
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Crop Image");
        options.setHideBottomControls(true);
        options.setToolbarColor(getResources().getColor(R.color.button_start_end_color));
        options.setStatusBarColor(getResources().getColor(R.color.button_start_end_color));
        UCrop uCrop = UCrop.of(sourceUri, Uri.parse(destinationPath))
//                .withMaxResultSize(500, 500)
                .withOptions(options);
        if (ratioType == 1)
            uCrop.withAspectRatio(4, 4);
        else if (ratioType == 3)
            uCrop.withAspectRatio(16, 16);
//        else
//            uCrop.withAspectRatio(9, 16);
        uCrop.start(getActivity());
    }

    private void setImage(String path) {
        if (getActivity() instanceof PickImage)
            ((PickImage) getActivity()).onImagePicked(path);
//        else if(getActivity() instanceof EditProfileActivity)
//            ((EditProfileActivity)getActivity()).onImagePicked(path);
        dismiss();
    }


//    copy past in activity
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PickImageDialog.TAG);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }*/
}
