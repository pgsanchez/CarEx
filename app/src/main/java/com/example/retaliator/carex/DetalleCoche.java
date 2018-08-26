package com.example.retaliator.carex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Juan Nadie on 28/04/2018.
 */

public class DetalleCoche extends AppCompatActivity {
    Coche dlgCoche = new Coche();
    ImageView iconoCoche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_coche);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null){
            dlgCoche = (Coche)getIntent().getExtras().getSerializable("cocheEditable");
        }

        Log.i("Detalle Coche = ", dlgCoche.getNombre());

        TextView edtNombre  = (TextView) findViewById(R.id.edtNombre);
        edtNombre.setText(String.valueOf(dlgCoche.getNombre()));

        ImageView iconoWhite = (ImageView) findViewById(R.id.iconColorNegro);
        iconoWhite.setBackgroundColor(Color.TRANSPARENT);
        iconoWhite.setColorFilter(Color.BLACK);

        ImageView iconoRed = (ImageView) findViewById(R.id.iconColorRojo);
        iconoRed.setBackgroundColor(Color.TRANSPARENT);
        iconoRed.setColorFilter(Color.RED);

        ImageView iconoBlue = (ImageView) findViewById(R.id.iconColorAzul);
        iconoBlue.setBackgroundColor(Color.TRANSPARENT);
        iconoBlue.setColorFilter(Color.BLUE);

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setColorFilter(Color.GREEN);

        iconoCoche = (ImageView) findViewById(R.id.imageViewCoche);
        iconoCoche.setImageResource(dlgCoche.getIcono());
        iconoCoche.setColorFilter(dlgCoche.getColor());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_repostaje_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_guardar:
                // Acciones a realizar cuando se le da a guardar

                return true;

            case android.R.id.home:
                // Acciones a realizar cuando se pulsa "flecha atrás"

                finish();
                // La función finish() vuelve a la actividad anterior como
                // si se hubiese hecho un CANCEL, es decir vuelve al método onActivityResult
                // con código "RESULT_CANCELED" pero además destruye esta actividad.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBtnFurgoClick(View view)
    {
        // FURGO
        dlgCoche.setIcono(R.drawable.ic_car_estate);
        iconoCoche.setImageResource(dlgCoche.getIcono());
    }

    public void onBtnUtilitarioClick(View view)
    {
        // UTILITARIO
        dlgCoche.setIcono(R.drawable.ic_car_hatchback);
        iconoCoche.setImageResource(dlgCoche.getIcono());
    }

    public void onBtnDeportivoClick(View view)
    {
        // DEPORTIVO
        dlgCoche.setIcono(R.drawable.ic_car_sports);
        iconoCoche.setImageResource(dlgCoche.getIcono());
    }

    public void onBtnGreenClick(View view)
    {
        dlgCoche.setColor(Color.GREEN);
        iconoCoche.setColorFilter(dlgCoche.getColor());
    }

    public void onBtnBlackClick(View view)
    {
        dlgCoche.setColor(Color.BLACK);
        iconoCoche.setColorFilter(dlgCoche.getColor());
    }

    public void onBtnRedClick(View view)
    {
        dlgCoche.setColor(Color.RED);
        iconoCoche.setColorFilter(dlgCoche.getColor());
    }

    public void onBtnBlueClick(View view)
    {
        dlgCoche.setColor(Color.BLUE);
        iconoCoche.setColorFilter(dlgCoche.getColor());
    }

    public void onBtnGuardarCoche(View view)
    {
        EditText edtNombre = (EditText) findViewById(R.id.edtNombre);
        dlgCoche.setNombre(edtNombre.getText().toString());

        // Cerrar la Activity y devolver el objeto Coche con los datos
        Intent data = new Intent();
        data.putExtra("cocheModificado",dlgCoche);
        setResult(RESULT_OK, data);
        finish();
    }
}
