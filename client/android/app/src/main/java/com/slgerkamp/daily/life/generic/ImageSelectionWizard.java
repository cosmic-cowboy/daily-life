package com.slgerkamp.daily.life.generic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

public class ImageSelectionWizard extends DialogFragment{


    private static final String PARAM_SIZE = "size";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_FROM_LIBRARY = 2;

    private PublishSubject<Bitmap> signal = PublishSubject.create();

    /** 結果のビットマップを返す Observable です。 */
    public Observable<Bitmap> result = signal.asObservable().doOnCompleted(new Action0() {
        @Override
        public void call() {
            doClose();
        }
    });

    private int size;
    private Dialog dialog;
    private Optional<File> photoToSave = Optional.absent();


    // ----------------------------------------------------------------
    //     初期化
    // ----------------------------------------------------------------

    public static ImageSelectionWizard create(int size) {
        ImageSelectionWizard res = new ImageSelectionWizard();
        Bundle args = new Bundle();
        args.putInt(PARAM_SIZE, size);
        res.setArguments(args);
        return res;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        size = args.getInt(PARAM_SIZE);

        dialog = showSourceSelectionDialog();
        return dialog;
    }


    // ----------------------------------------------------------------
    //     アクティビティ呼び出し
    // ----------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Activity activity = getActivity();
        if (resultCode != Activity.RESULT_OK) {
            close();

        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            Uri bitmap = Uri.fromFile(photoToSave.get());
            notifyPhotoCreated(bitmap);
            signal.onNext(BitmapFactory.decodeFile(photoToSave.get().getAbsolutePath()));
            close();

        } else if (requestCode == REQUEST_IMAGE_FROM_LIBRARY) {
            Uri image = data.getData();

            Bitmap bitmap;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), image);
            } catch(IOException e){
               throw new IllegalArgumentException(e);
            }

            signal.onNext(bitmap);
            close();

        } else {
            close();
        }
    }

    private Dialog showSourceSelectionDialog() {
        ListView selection = new ListView(getActivity());
        selection.getDivider().setAlpha(0);
        ArrayAdapter<String> selections = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        selections.addAll("写真を撮る", "ライブラリから選択");
        selection.setAdapter(selections);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(selection);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        selection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    openCamera();
                } else if (position == 1) {
                    openLibrary();
                } else {
                    close();
                }
            }
        });

        return alertDialog;
    }

    private void openLibrary() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_FROM_LIBRARY);
    }

    private void openCamera() {
        // Create the File where the photo should go
        try {
            photoToSave = Optional.of(createEmptyFileOnPicturesDirectory());
        } catch (IOException ex) {
            return;
        }
        // Continue only if the File was successfully created
        if (photoToSave.isPresent()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoToSave.get()));
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // ----------------------------------------------------------------
    //     終了処理
    // ----------------------------------------------------------------

    private void close() {
        signal.onCompleted();
    }

    /**
     * <p>終了処理を実行します。</p>
     *
     * <p>終了処理を実行する際はこのメソッドは直接呼び出さず、close() を呼び出します。</p>
     */
    private void doClose() {
        delete(photoToSave);

        dialog.dismiss();
    }


    // ----------------------------------------------------------------
    //     その他
    // ----------------------------------------------------------------

    /**
     * 端末共有の写真保存用ディレクトリに、空のファイルを作成します。
     */
    private File createEmptyFileOnPicturesDirectory() throws IOException {
        // 画像を作成
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    /**
     * フォトアプリなどに、新しく写真が保存されたことを通知します。
     */
    private void notifyPhotoCreated(Uri path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(path);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private static void delete(Optional<File> file) {
        if (file.isPresent()) {
            boolean deleted = file.get().delete();
            if (!deleted)  {
                throw new IllegalStateException();
            }
        }
    }
}
