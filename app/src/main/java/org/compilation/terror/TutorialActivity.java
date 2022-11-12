package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {
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

    public ImageButton buttonCompile;

    public ImageButton buttonFastMode;

    public ImageButton buttonSuperscript;
    public ImageButton buttonSubscript;

    public Button buttonSymbolic;
    public Button buttonManual;

    ConstraintLayout artboard;
    ConstraintLayout textEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tutorial);

        artboard = findViewById(R.id.artboard);
        textEditor = findViewById(R.id.textEditor);

        buttonDigit = findViewById(R.id.digitButton);
        buttonCapital = findViewById(R.id.capitalButton);
        buttonSmaller = findViewById(R.id.smallerButton);
        buttonSymbol = findViewById(R.id.symbolButton);

        buttonDraw = findViewById(R.id.drawButton);
        buttonErase = findViewById(R.id.eraseButton);
        buttonClear = findViewById(R.id.clearButton);

        buttonCopy = findViewById(R.id.copyButton);
        buttonCut = findViewById(R.id.cutButton);
        buttonTextClear = findViewById(R.id.clearTextButton);

        buttonCompile = findViewById(R.id.compileButton);

        buttonFastMode = findViewById(R.id.fastMode);
        buttonSuperscript = findViewById(R.id.superscriptButton);
        buttonSubscript = findViewById(R.id.subscriptButton);

        buttonSymbolic = findViewById(R.id.buttonSymbolic);
        buttonManual = findViewById(R.id.buttonManual);
        buttonManual.setBackgroundColor(Color.WHITE);
        buttonManual.setTextColor(Color.rgb(78, 117, 193));

        artboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Draw the shapes here!", Toast.LENGTH_SHORT).show();
            }
        });
        textEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "The output will be here. Also you can type in!", Toast.LENGTH_SHORT).show();
            }
        });
        buttonDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Select before drawing any digit", Toast.LENGTH_SHORT).show();
            }
        });
        buttonCapital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Select before drawing any capital letter", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSmaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Select before drawing any smaller letter", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Select before drawing any symbol", Toast.LENGTH_SHORT).show();
            }
        });

        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "to erase on the artboard", Toast.LENGTH_SHORT).show();
            }
        });
        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "to draw on the artboard", Toast.LENGTH_SHORT).show();
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "to clear the artboard", Toast.LENGTH_SHORT).show();
            }
        });
        buttonCopy.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), "to copy the content from TextEditor", Toast.LENGTH_SHORT).show();
        });
        buttonCut.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), "to cut the content from TextEditor", Toast.LENGTH_SHORT).show();
        });
        buttonTextClear.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), "to clear the textboard", Toast.LENGTH_SHORT).show();
        });

        buttonCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "to get the drawing in the text", Toast.LENGTH_SHORT).show();
            }
        });

        buttonFastMode.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), "One touch drawing-output mode", Toast.LENGTH_SHORT).show();
        });

        buttonSuperscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "To convert your text or drawing into Superscript", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "To convert your text or drawing into Subscript", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSymbolic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSymbolicMode();
            }
        });
    }

    public void switchToSymbolicMode() {
        Intent intent = new Intent(this, SymbolicActivity.class);
        startActivity(intent);
        finish();
    }
}