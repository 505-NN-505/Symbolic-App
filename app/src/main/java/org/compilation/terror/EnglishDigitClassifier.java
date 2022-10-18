package org.compilation.terror;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class EnglishDigitClassifier {

    private static final String LOG_TAG = EnglishDigitClassifier.class.getSimpleName();

    private static final String MODEL_NAME = "tf_lite_english_digit_model.tflite";

    private static final int BATCH_SIZE = 1;
    public static final int IMG_HEIGHT = 28;
    public static final int IMG_WIDTH = 28;
    private static final int NUM_CHANNEL = 1;
    private static final int NUM_CLASSES = 10;
    public static float total =0;

    private final Interpreter.Options options = new Interpreter.Options();
    private final Interpreter mInterpreter;
    private ByteBuffer mImageData;
    private int[] mImagePixels = new int[IMG_HEIGHT * IMG_WIDTH];
    private float[][] mResult = new float[1][NUM_CLASSES];

    public EnglishDigitClassifier(Activity activity) throws IOException {
        mInterpreter = new Interpreter(loadModelFile1(activity), options);
        mImageData = ByteBuffer.allocateDirect(
                4 * BATCH_SIZE * IMG_HEIGHT * IMG_WIDTH * NUM_CHANNEL);
        mImageData.order(ByteOrder.nativeOrder());
    }

    public EnglishDigitResult classify(Bitmap bitmap) {
        System.out.println("bitmap = " + bitmap.getWidth() + " " + bitmap.getHeight());
        convertBitmapToByteBuffer(bitmap);
        mInterpreter.run(mImageData, mResult);
        Log.v(LOG_TAG, "classify(): result = " + Arrays.toString(mResult[0]));
        return new EnglishDigitResult(mResult[0]);
    }

    private MappedByteBuffer loadModelFile1(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_NAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (mImageData == null) {
            return;
        }
        mImageData.rewind();
        total=0;
        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < IMG_WIDTH; ++i) {
            for (int j = 0; j < IMG_HEIGHT; ++j) {
                int value = mImagePixels[pixel++];
                //mImageData.putFloat(convertPixelWhite(value));
                total = total + convertPixelWhite(value);
            }
        }
        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        pixel=0;
        if(total>800) {
            for (int i = 0; i < IMG_WIDTH; ++i) {
                for (int j = 0; j < IMG_HEIGHT; ++j) {
                    int value = mImagePixels[pixel++];
                    mImageData.putFloat(convertPixelWhite(value));

                }
            }
        }
        else{
            for (int i = 0; i < IMG_WIDTH; ++i) {
                for (int j = 0; j < IMG_HEIGHT; ++j) {
                    int value = mImagePixels[pixel++];
                    mImageData.putFloat(convertPixelBlack(value));

                }
            }
        }
    }



    private static float convertPixelWhite(int color) {
        float color2 = ((((color >> 16) & 0xFF) * 0.299f
                + ((color >> 8) & 0xFF) * 0.587f
                + (color & 0xFF) * 0.114f)) / 255.0f;
        if(color2 < 0.6f)
            return 0.0f;
        else
            return 1.0f;
    }
    private static float convertPixelBlack(int color) {
        float color2 = (255.0f - (((color >> 16) & 0xFF) * 0.299f
                + ((color >> 8) & 0xFF) * 0.587f
                + (color & 0xFF) * 0.114f)) / 255.0f;
        if(color2 < 0.6f)
            return 0.0f;
        else
            return 1.0f;
    }

}
