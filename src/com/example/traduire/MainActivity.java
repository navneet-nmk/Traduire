package com.example.traduire;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	protected Button takePictureButton;
	protected Button translateButton;
	protected ImageView uploadedImage;
	protected String path;
	protected String path_two;
	protected Uri mMediaUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Declare the variables
		uploadedImage = (ImageView) findViewById(R.id.uploadedImage);
		takePictureButton = (Button) findViewById(R.id.takePicture);
		translateButton = (Button) findViewById(R.id.translateButton);
		path = Environment.getExternalStorageDirectory()
				+ "/translate.jpg";
		takePictureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// String appname =
				// MainActivity.this.getString(R.string.app_name);
				// File newFile = new File(path);
				// File file;
				// Date now = new Date();
				// String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				// Locale.US).format(now);
				// path = newFile.getPath() + File.separator;
				// path_two = path+timestamp+".jpg";
				// file = new File(path + timestamp + ".jpg");
				// Uri outputFileUri = Uri.fromFile(newFile);
				File file = new File(path);
				Uri outpUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outpUri);
				startActivityForResult(intent, 0);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("MakeMachine", "resultCode: " + resultCode);
		switch (resultCode) {
		case 0:
			Log.i("MakeMachine", "User cancelled");
			break;

		case -1:
			onPhotoTaken();
			break;
		}
	}

	protected void onPhotoTaken() {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		uploadedImage.setImageBitmap(bitmap);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
