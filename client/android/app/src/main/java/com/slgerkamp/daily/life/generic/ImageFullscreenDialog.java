package com.slgerkamp.daily.life.generic;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.slgerkamp.daily.life.R;
import com.squareup.picasso.Callback;

import butterknife.ButterKnife;
import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * <p>画像を全画面で表示するためのダイアログ</p>
 */
public class ImageFullscreenDialog extends DialogFragment{

    private static final String PARAM_FILE_ID = "fileId";

    @Bind(R.id.image_view) ImageView imageView;
    @Bind(R.id.cancel) ImageButton cancel;
    PhotoViewAttacher attacher;

    public static ImageFullscreenDialog newInstance(Long fileId) {

        ImageFullscreenDialog fragment = new ImageFullscreenDialog();
        Bundle args = new Bundle();
        args.putLong(PARAM_FILE_ID, fileId);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFullscreenDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Long fileId = getArguments().getLong(PARAM_FILE_ID);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View root = layoutInflater.inflate(R.layout.image_fullscreen_dialog, null, false);
        ButterKnife.bind(this, root);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imageView.getLayoutParams().height = size.y;
        imageView.getLayoutParams().width  = size.x;

        new Backend(getActivity())
                .imageLoader()
                .load(fileId).into(imageView, new Callback(){
                    @Override
                    public void onSuccess() {
                        attacher = new PhotoViewAttacher(imageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

        // フルスクリーンで表示するため、Dialogのレイアウト情報を変更
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }
}
