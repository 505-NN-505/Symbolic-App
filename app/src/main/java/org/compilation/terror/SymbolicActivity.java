
package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SymbolicActivity extends AppCompatActivity {
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
    private SymbolClassifier symClassifier;

    boolean fastWritingMode;
    public ImageButton buttonFastMode;

    boolean superscriptMode, subscriptMode;
    String superscriptDigitString = "⁰¹²³⁴⁵⁶⁷⁸⁹";
    String superscriptSmallerString = "ᵃᵇᶜᵈᵉᶠᵍʰᶦʲᵏˡᵐⁿᵒᵖᑫʳˢᵗᵘᵛʷˣʸᶻ";
    String superscriptCapitalString = "ᴬᴮᶜᴰᴱᶠᴳᴴᴵᴶᴷᴸᴹᴺᴼᴾQᴿˢᵀᵁⱽᵂˣʸᶻ";
    String subscriptDigitString = "₀₁₂₃₄₅₆₇₈₉";
    String subscriptSmallerLay = "acegijklmnoprstuvxy";
    String subscriptSmallerString = "ₐ꜀ₑₕᵢⱼₖₗₘₙₒₚᵣₛₜᵤᵥᵥₓᵧ";
    String subscriptCapitalLay = "AEHIJKLMNOPRSTUVXY";
    String subscriptCapitalString = "ₐₑₕᵢⱼₖₗₘₙₒₚᵣₛₜᵤᵥₓᵧ";
    public ImageButton buttonSuperscript;
    public ImageButton buttonSubscript;

    public Button buttonSymbolic;
    public Button buttonManual;

    HashMap<Character, Character> supToNorm = new HashMap<>();
    HashMap<Character, Character> subToNorm = new HashMap<>();

    ConstraintLayout backGround;
    int track;

    public void populateSupSubConversion() {
        Character d = '0';
        for (int i = 0; i < superscriptDigitString.length(); i++) {
            Character ch = superscriptDigitString.charAt(i);
            supToNorm.put(ch, d++);
        }
        d = 'a';
        for (int i = 0; i < superscriptSmallerString.length(); i++) {
            Character ch = superscriptSmallerString.charAt(i);
            supToNorm.put(ch, d++);
        }
        d = 'A';
        for (int i = 0; i < superscriptCapitalString.length(); i++) {
            Character ch = superscriptCapitalString.charAt(i);
            supToNorm.put(ch, d++);
        }
        d = '0';
        for (int i = 0; i < subscriptDigitString.length(); i++) {
            Character ch = subscriptDigitString.charAt(i);
            subToNorm.put(ch, d++);
        }
        for (int i = 0; i < subscriptSmallerLay.length(); i++) {
            Character ch = subscriptSmallerString.charAt(i);
            subToNorm.put(ch, subscriptSmallerLay.charAt(i));
        }
        for (int i = 0; i < subscriptCapitalLay.length(); i++) {
            Character ch = subscriptCapitalString.charAt(i);
            subToNorm.put(ch, subscriptCapitalLay.charAt(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_logo);

        signatureView = (SignatureView) findViewById(R.id.signature_view);
        float initPenSize = signatureView.getPenSize();

        populateSupSubConversion();

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
        buttonSuperscript = findViewById(R.id.superscriptButton);
        buttonSubscript = findViewById(R.id.subscriptButton);

        superscriptMode = false;
        subscriptMode = false;

        buttonSymbolic = findViewById(R.id.buttonSymbolic);
        buttonManual = findViewById(R.id.buttonManual);

        buttonSymbolic.setBackgroundColor(Color.WHITE);
        buttonSymbolic.setTextColor(Color.rgb(78, 117, 193));

        backGround = findViewById(R.id.platform);
        track = 0;

        backGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawType == 4) {
                    track++;
                }
            }
        });
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
            Toast.makeText(getApplicationContext(), "Copied into the clipboard!", Toast.LENGTH_SHORT).show();
        });
        buttonCut.setOnClickListener(e -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("symbols", textEditor.getText());
            clipboard.setPrimaryClip(data);
            clearTextEditor();
            Toast.makeText(getApplicationContext(), "Cut into the clipboard!", Toast.LENGTH_SHORT).show();
        });
        buttonTextClear.setOnClickListener(e -> clearTextEditor());

        buttonCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signatureView.isBitmapEmpty()) {
                    Toast.makeText(getApplicationContext(), "The artboard is empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    detectObject();
                }
            }
        });

        buttonFastMode.setOnClickListener(e -> {
            fastWritingMode ^= true;
            if (fastWritingMode) {
                signatureView.clearCanvas();
                toggleSetButtonColor(buttonFastMode);
                toggleSetButtonColor(buttonDraw);
                signatureView.setPenColor(Color.BLACK);
                signatureView.setPenSize(initPenSize);
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

        buttonSuperscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextSelectionMode()) {
                    superscriptMode = false;
                    toggleResetButtonColor(buttonSuperscript);
                    modifySelectedText(1);
                }
                else {
                    superscriptMode ^= true;
                    if (superscriptMode) {
                        toggleSetButtonColor(buttonSuperscript);
                        toggleResetButtonColor(buttonSubscript);
                    } else {
                        toggleResetButtonColor(buttonSuperscript);
                    }
                    if (superscriptMode) {
                        subscriptMode = false;
                    }
                }
            }
        });
        buttonSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextSelectionMode()) {
                    subscriptMode = false;
                    toggleResetButtonColor(buttonSubscript);
                    modifySelectedText(2);
                }
                else {
                    subscriptMode ^= true;
                    if (subscriptMode) {
                        toggleSetButtonColor(buttonSubscript);
                        toggleResetButtonColor(buttonSuperscript);
                    } else {
                        toggleResetButtonColor(buttonSubscript);
                    }
                    if (subscriptMode) {
                        superscriptMode = false;
                    }
                }
            }
        });

        buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTutorialMode();
            }
        });

    }

    public void switchToTutorialMode() {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
        finish();
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
        if (drawType == 4) {
            detectSymbols();
        }
    }

    public void detectDigit() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 28, 28);
        EnglishDigitResult resultD = dClassifier.classify(bitmapSymbol);
        char digit = (char)(resultD.getNumber() + '0');
        placeText(digit);
    }
    public void detectCapitalAlphabet() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 28, 28);
        EnglishAlphabetResult resultA = cClassifier.classify(bitmapSymbol);
        char letter = (char)(resultA.getNumber() + 'A');
        placeText(letter);
    }
    public void detectSmallAlphabet() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmapSymbol = getResizedBitmap(bitmap, 40, 40);
        EnglishAlphabetResult resultS = sClassifier.classify(bitmapSymbol);
        char letter = (char)(resultS.getNumber() + 'a');
        placeText(letter);
    }

    ArrayList<Character> syms = new ArrayList<>(Arrays.asList('2', 'X', '3', '5', '1', '0', '7', '/', '-', '8', '+', '4', '6', '9'));
    ArrayList<Character> bck = new ArrayList<>(Arrays.asList('∫', '√', '+', 'X'));
    public void detectSymbols() {
        if (track == 0) {
            Bitmap bitmap = signatureView.getSignatureBitmap();
            bitmapSymbol = getResizedBitmap(bitmap, 32, 32);
            SymbolResult resultSym = symClassifier.classify(bitmapSymbol);
            System.out.println(resultSym.getNumber());
            char sym = syms.get(resultSym.getNumber());
            placeText(sym);
        }
        else {
            placeText(bck.get(track - 1));
        }
        track = 0;
    }

    public void clearTextEditor() {
        textEditor.setText("");
    }

    public void placeText(char text) {
        if (superscriptMode) {
            if (drawType == 1) {
                text = superscriptDigitString.charAt(text - '0');
            }
            else if (drawType == 2) {
                text = superscriptCapitalString.charAt(text - 'A');
            }
            else if (drawType == 3) {
                text = superscriptSmallerString.charAt(text - 'a');
            }
        }
        if (subscriptMode) {
            if (drawType == 1) {
                text = subscriptDigitString.charAt(text - '0');
            }
            else if (drawType == 2) {
                text = subscriptCapitalString.charAt(text - 'A');
            }
            else if (drawType == 3) {
                text = subscriptSmallerString.charAt(text - 'a');
            }
        }
        String s = textEditor.getText().toString();
        s += text;
        textEditor.setText(s);
    }

    public boolean isTextSelectionMode() {
        return textEditor.getSelectionEnd() > textEditor.getSelectionStart();
    }

    public HashMap<Integer, Integer> mapCharModes = new HashMap<Integer, Integer>();
    public void modifySelectedText(Integer mode) {
        int startSelection = textEditor.getSelectionStart();
        int endSelection = textEditor.getSelectionEnd();
        String text = textEditor.getText().toString();
        for (Integer i = startSelection; i < endSelection; i++) {
            char ch = text.charAt(i);
            char tar = '#';
            if (Character.isUpperCase(ch)) {
                if (mode == 1) tar = superscriptCapitalString.charAt(ch - 'A');
                else tar = subscriptCapitalString.charAt(ch - 'A');
                mapCharModes.put(i, mode);
                text = text.substring(0, i) + tar + text.substring(i + 1);
            }
            else if (Character.isLowerCase(ch)) {
                if (mode == 1) tar = superscriptSmallerString.charAt(ch - 'a');
                else tar = subscriptSmallerString.charAt(ch - 'a');
                mapCharModes.put(i, mode);
                text = text.substring(0, i) + tar + text.substring(i + 1);
            }
            else if (Character.isDigit(ch)) {
                if (mode == 1) tar = superscriptDigitString.charAt(ch - '0');
                else tar = subscriptDigitString.charAt(ch - '0');
                mapCharModes.put(i, mode);
                text = text.substring(0, i) + tar + text.substring(i + 1);
            }
            else if (mapCharModes.containsKey(i) && mapCharModes.get(i) == 1) {
                if (mode == 1) {
                    tar = supToNorm.get(ch);
                    mapCharModes.remove(i);
                    text = text.substring(0, i) + tar + text.substring(i + 1);
                }
            }
            else if (mapCharModes.containsKey(i) && mapCharModes.get(i) == 2) {
                if (mode == 2) {
                    subToNorm.get(ch);
                    mapCharModes.remove(i);
                    text = text.substring(0, i) + tar + text.substring(i + 1);
                }
            }
        }
        textEditor.setText(text);
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
            symClassifier = new SymbolClassifier(this);
        } catch (IOException e) {

        }
    }
}