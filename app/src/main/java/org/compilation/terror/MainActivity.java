package org.compilation.terror;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.kyanogen.signatureview.SignatureView;

public class MainActivity extends AppCompatActivity {
    public ImageButton buttonDraw;
    public ImageButton buttonErase;
    public ImageButton buttonClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignatureView signatureView = (SignatureView)findViewById(R.id.signature_view);
        float initPenSize = signatureView.getPenSize();


        buttonDraw = findViewById(R.id.drawButton);
        buttonErase = findViewById(R.id.eraseButton);
        buttonClear = findViewById(R.id.clearButton);

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
    }
}