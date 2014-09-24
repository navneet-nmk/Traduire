package com.example.traduire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class MainActivity extends Activity {
	protected Button takePictureButton;
	protected Button translateButton;
	protected ImageView uploadedImage;
	protected TextView textUpload;
	protected String path;
	protected String path_two;
	protected Uri mMediaUri;
	// Path for storing the language file at for use in Tesseract
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/Traduire/";
	// Defining the language as english
	public static final String lang = "eng";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Checking the paths to check for the existence of the directories.
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					// Log.v(TAG, "ERROR: Creation of directory " + path +
					// " on sdcard failed");
					return;
				} else {
					// Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {
				// For language files stored in assets.
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang
						+ ".traineddata");
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/"
						+ lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				// while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				// gin.close();
				out.close();

				// Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				// Log.e(TAG,
				// "Was unable to copy " + lang + " traineddata "
				// + e.toString());
			}
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Declare the variables
		textUpload = (TextView) findViewById(R.id.textView1);
		uploadedImage = (ImageView) findViewById(R.id.uploadedImage);
		takePictureButton = (Button) findViewById(R.id.takePicture);
		translateButton = (Button) findViewById(R.id.translateButton);
		path = DATA_PATH + "/ocr.jpg";

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
			try {
				onPhotoTaken();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	protected void onPhotoTaken() throws Exception {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		uploadedImage.setImageBitmap(bitmap);
		// Required by tess
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		TessBaseAPI baseAPI = new TessBaseAPI();
		baseAPI.setDebug(true);
		baseAPI.init(DATA_PATH, lang);
		baseAPI.setImage(bitmap);
		String text = baseAPI.getUTF8Text();
		textUpload.setText(text);
		String translatedText = translate(text);
		textUpload.setText(translatedText);

	}

	// Define function to translate the string
	protected String translate(String text) throws Exception {
		Translate.setClientId("NavneetKumarTranslator");
		Translate
				.setClientSecret("csrDHAaipBK9baYP5L2IQVl2TLEactdq7WM0Unffano");
		String translatedtext = "";
		translatedtext = Translate.execute(text, Language.GERMAN);
		return translatedtext;

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
