package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kyanogen.signatureview.SignatureView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    int drawType = 1;

    public ImageButton buttonDigit;
    public ImageButton buttonCapital;
    public ImageButton buttonSmaller;
    public ImageButton buttonSymbol;

    public ImageButton buttonDraw;
    public ImageButton buttonErase;
    public ImageButton buttonClear;
    public ImageButton buttonCopy;
    public ImageButton buttonCut;
    public ImageButton buttonTextClear;

    public EditText textEditor;

    public ImageButton buttonCompile;

    SignatureView signatureView;
    public Bitmap bitmapSymbol;

    private EnglishAlphabetClassifier cClassifier;
    private EnglishDigitClassifier dClassifier;
    private EnglishAlphabetSmallClassifier sClassifier;

    boolean fastWritingMode;
    public ImageButton buttonFastMode;
    public ImageButton buttonSuperscrpt;
    public ImageButton buttonSubscript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signatureView = (SignatureView) findViewById(R.id.signature_view);
        float initPenSize = signatureView.getPenSize();

        buttonDigit = findViewById(R.id.digitButton);
        buttonCapital = findViewById(R.id.capitalButton);
        buttonSmaller = findViewById(R.id.smallerButton);
        buttonSymbol = findViewById(R.id.symbolButton);

        drawType = 1;
        toggleSetButtonColor(buttonDigit);

        buttonDraw = findViewById(R.id.drawButton);
        buttonErase = findViewById(R.id.eraseButton);
        buttonClear = findViewById(R.id.clearButton);
        
        toggleSetButtonColor(buttonDraw);

        buttonCopy = findViewById(R.id.copyButton);
        buttonCut = findViewById(R.id.cutButton);
        buttonTextClear = findViewById(R.id.clearTextButton);

        textEditor = findViewById(R.id.textEditor);

        buttonCompile = findViewById(R.id.compileButton);

        init();
        fastWritingMode = false;
        buttonFastMode = findViewById(R.id.fastMode);
        buttonSuperscrpt = findViewById(R.id.superscriptButton);
        buttonSubscript = findViewById(R.id.subscriptButton);

        buttonDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawType = 1;
                toggleSetButtonColor(buttonDigit);
                toggleResetButtonColor(buttonCapital);
                toggleResetButtonColor(buttonSmaller);
                toggleResetButtonColor(buttonSymbol);
            }
        });
        buttonCapital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawType = 2;
                toggleSetButtonColor(buttonCapital);
                toggleResetButtonColor(buttonDigit);
                toggleResetButtonColor(buttonSmaller);
                toggleResetButtonColor(buttonSymbol);
            }
        });
        buttonSmaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawType = 3;
                toggleSetButtonColor(buttonSmaller);
                toggleResetButtonColor(buttonDigit);
                toggleResetButtonColor(buttonCapital);
                toggleResetButtonColor(buttonSymbol);
            }
        });
        buttonSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawType = 4;
                toggleSetButtonColor(buttonSymbol);
                toggleResetButtonColor(buttonDigit);
                toggleResetButtonColor(buttonCapital);
                toggleResetButtonColor(buttonSmaller);
            }
        });

        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fastWritingMode) {
                    signatureView.setPenSize(50.f);
                    signatureView.setPenColor(Color.WHITE);
                    toggleSetButtonColor(buttonErase);
                    toggleResetButtonColor(buttonDraw);
                }
            }
        });
        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.setPenColor(Color.BLACK);
                signatureView.setPenSize(initPenSize);
                if (!fastWritingMode) {
                    toggleSetButtonColor(buttonDraw);
                    toggleResetButtonColor(buttonErase);
                }
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
                detectObject();
            }
        });

        buttonFastMode.setOnClickListener(e -> {
            fastWritingMode ^= true;
            if (fastWritingMode) {
                signatureView.clearCanvas();
                toggleSetButtonColor(buttonFastMode);
                toggleSetButtonColor(buttonDraw);
                buttonErase.setBackground(getResources().getDrawable(R.drawable.rect_circular_disabled));
            }
            else {
                toggleResetButtonColor(buttonFastMode);
                toggleResetButtonColor(buttonErase);
            }
        });

        signatureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fastWritingMode && event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    detectObject();
                    signatureView.clearCanvas();
                }
                return false;
            }
        });
    }

    public void setButtonBackgroundColor(ImageButton button) {
        button.setBackground(getResources().getDrawable(R.drawable.rect_circular_toggle));
    }
    public void resetButtonBackgroundColor(ImageButton button) {
        button.setBackground(getResources().getDrawable(R.drawable.rect_circular_button));
    }
    public void setButtonForegroundColor(ImageButton button) {
        button.setColorFilter(Color.rgb(78, 117, 193));
    }
    public void resetButtonForegroundColor(ImageButton button) {
        button.setColorFilter(Color.rgb(255, 255, 255));
    }
    public void toggleSetButtonColor(ImageButton button) {
        setButtonBackgroundColor((button));
        setButtonForegroundColor(button);
    }
    public void toggleResetButtonColor(ImageButton button) {
        resetButtonForegroundColor((button));
        resetButtonBackgroundColor(button);
    }

    public void detectObject() {
        if (drawType == 1) {
            detectDigit();
        }
        if (drawType == 2) {
            detectCapitalAlphabet();
        }
        if (drawType == 3) {
            detectSmallAlphabet();
        }
    }

    public void detectDigit() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 28, 28);
        EnglishDigitResult resultD = dClassifier.classify(bitmapSymbol);
        char digit = (char)(resultD.getNumber() + '0');
        System.out.println("Final Answer Digit = " + digit);
        System.out.println(resultD.getMaxProb());
        System.out.println();
        placeText(digit);
    }
    public void detectCapitalAlphabet() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 28, 28);
        EnglishAlphabetResult resultA = cClassifier.classify(bitmapSymbol);
        char letter = (char)(resultA.getNumber() + 'A');
        System.out.println("Final Answer Captial = " + letter);
        System.out.println(resultA.getMaxProb());
        System.out.println();
        placeText(letter);
    }
    public void detectSmallAlphabet() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 40, 40);
        EnglishAlphabetResult resultS = sClassifier.classify(bitmapSymbol);
        char letter = (char)(resultS.getNumber() + 'a');
        System.out.println("Final Answer Small = " + letter);
        System.out.println(resultS.getMaxProb());
        placeText(letter);
    }

    public void clearTextEditor() {
        textEditor.setText("");
    }

    public void placeText(char text) {
        String s = textEditor.getText().toString();
        s += text;
        textEditor.setText(s);
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
            cClassifier = new EnglishAlphabetClassifier(this);
            sClassifier = new EnglishAlphabetSmallClassifier(this);
            dClassifier = new EnglishDigitClassifier(this);
        } catch (IOException e) {

        }
    }
}