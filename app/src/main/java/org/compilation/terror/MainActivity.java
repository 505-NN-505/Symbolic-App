package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kyanogen.signatureview.SignatureView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public ImageButton buttonDraw;
    public ImageButton buttonErase;
    public ImageButton buttonClear;
    public ImageButton buttonCopy;
    public ImageButton buttonCut;
    public ImageButton buttonTextClear;
    public EditText textEditor;

    public ImageButton buttonCompile;

    public Bitmap bitmapSymbol;

    private EnglishDigitClassifier mClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignatureView signatureView = (SignatureView) findViewById(R.id.signature_view);
        float initPenSize = signatureView.getPenSize();

        buttonDraw = findViewById(R.id.drawButton);
        buttonErase = findViewById(R.id.eraseButton);
        buttonClear = findViewById(R.id.clearButton);

        buttonCopy = findViewById(R.id.copyButton);
        buttonCut = findViewById(R.id.cutButton);
        buttonTextClear = findViewById(R.id.clearTextButton);

        textEditor = findViewById(R.id.textEditor);

        buttonCompile = findViewById(R.id.compileButton);

        init();

        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.setPenSize(50.f);
                signatureView.setPenColor(Color.WHITE);
            }
        });
        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.setPenColor(Color.BLACK);
                signatureView.setPenSize(initPenSize);
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });
        buttonCopy.setOnClickListener(e -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("symbols", textEditor.getText());
            clipboard.setPrimaryClip(data);
        });
        buttonCut.setOnClickListener(e -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("symbols", textEditor.getText());
            clipboard.setPrimaryClip(data);
            clearTextEditor();
        });
        buttonTextClear.setOnClickListener(e -> clearTextEditor());

        buttonCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = signatureView.getSignatureBitmap();
                bitmapSymbol = getResizedBitmap(bitmap, 28, 28);
                EnglishDigitResult result = mClassifier.classify(bitmapSymbol);
                System.out.println("Final Answer = " + result.getNumber());
            }
        });
    }

    public void clearTextEditor() {
        textEditor.setText("");
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private void init() {

        try {
            mClassifier = new EnglishDigitClassifier(this);
        } catch (IOException e) {

        }
    }
}