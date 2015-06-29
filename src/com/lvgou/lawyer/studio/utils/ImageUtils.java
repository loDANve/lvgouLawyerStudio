package com.lvgou.lawyer.studio.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lvgou.lawyer.studio.Globals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;

public class ImageUtils {

	private File pictureFile;

	public void saveImage(Handler handler, int type, String picturePath,
			float width, float height) {
		try {
			Bitmap bitmap = shrinkImage(picturePath, width, height);
			if(type == 1){
				pictureFile = new File(Globals.FILEPATH + "myimage.jpg");
			}else{
				pictureFile = new File(Globals.FILEPATH + "myimage1.jpg");
			}
			if (pictureFile.exists()) {
				pictureFile.delete();
			}
			FileOutputStream out = new FileOutputStream(pictureFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			// bitmap.recycle();
			// bitmap = null;
			handler.sendEmptyMessage(1);
			// Log.i(TAG, "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 缩放图片
	public Bitmap zoomImg(int newWidth, int newHeight, String picturePath) {
		Bitmap bm = BitmapFactory.decodeFile(picturePath);
		if (bm == null) {
			return null;
		}
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		if(bm != newbm){
			bm.recycle();
			bm = null;
		}
		return newbm;
	}

	/**
	 * 旋转图片
	 * 
	 * @param bm
	 * @param picturePath
	 * @return
	 */
	public Bitmap rotateImage(Bitmap bm, String picturePath) {
		if (bm == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		int degree = readPictureDegree(picturePath);// 计算旋转角度
		matrix.postRotate(degree);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		if(bm != newbm){
			bm.recycle();
			bm = null;
		}
		return newbm;
	}

	public Bitmap shrinkImage(String srcPath, float width, float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (w / width);
		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (h / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inJustDecodeBounds = false;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		return bitmap;
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { //
			// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
			// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
		// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
