package gongren.com.dlg.utils;

import android.content.Intent;
import android.net.Uri;

public class CropImage {

	/**
	 * 获取剪切后的图片
	 */
	public static Intent getImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
//		 intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}

	/**
	 * 获取的图片
	 */
	public static Intent getImageClipIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 16);// 裁剪框比例
		intent.putExtra("aspectY", 9);
		intent.putExtra("outputX", 480);// 输出图片大小
		intent.putExtra("outputY", 270);
		intent.putExtra("return-data", true);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}

	/**
	 * 获取剪切后的图片
	 */
	public static Intent getSquareImageClipIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 240);// 输出图片大小
		intent.putExtra("outputY", 240);
		intent.putExtra("return-data", true);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}

	/**
	 * 获取本地图片 4.4以上系统
	 * 
	 * @return
	 */
	public static Intent getImageClipIntent_KITKAT() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 16);// 裁剪框比例
		intent.putExtra("aspectY", 9);
		intent.putExtra("outputX", 480);// 输出图片大小
		intent.putExtra("outputY", 270);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * Constructs an intent for image cropping. 剪切图片为正方形
	 */
	public static Intent getSquareCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 240);// 输出图片大小
		intent.putExtra("outputY", 240);
		intent.putExtra("return-data", true);
		return intent;
	}
}
