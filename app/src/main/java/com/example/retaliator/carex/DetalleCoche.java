package com.example.retaliator.carex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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

    boolean modoEditar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_coche);

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

        ImageButton imageButton = (ImageButton) findViewById(R.id.iconColorVerde);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setColorFilter(Color.GREEN);

        iconoCoche = (ImageView) findViewById(R.id.imageViewCoche);
        iconoCoche.setImageResource(dlgCoche.getIcono());
        iconoCoche.setColorFilter(dlgCoche.getColor());

        habilitarEdicion(false);

        // Asociar el menú contextual de borrar
        ImageView imagenPapelera = (ImageView) findViewById(R.id.imageTrash);
        registerForContextMenu(imagenPapelera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_repostaje_menu, menu);
        getMenuInflater().inflate(R.menu.activity_editar_repostaje_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_guardar);
        item.setVisible(modoEditar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar:
                // Habilitar edición.
                modoEditar = true;
                this.invalidateOptionsMenu();
                habilitarEdicion(true);
                return true;
            case R.id.menu_guardar:
                // Acciones a realizar cuando se le da a guardar
                onBtnGuardarCoche(null);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto_borrar, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mCancel:
                break;
            case R.id.mDelete:
                onBtnBorrar(null);
                break;
        }
        return super.onContextItemSelected(item);
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

    public void onBtnBorrar(View view)
    {
        dlgCoche.setColor(123456789);
        dlgCoche.setIcono(987654321);

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("cocheModificado",dlgCoche);
        setResult(RESULT_OK, data);
        finish();
    }

    public void habilitarEdicion(boolean habilitar){
        TextView nombre  = (TextView) findViewById(R.id.edtNombre);


        ImageView imageViewCoche = (ImageView) findViewById(R.id.imageViewCoche);
        ImageView btnIconFurgo = (ImageView) findViewById(R.id.iconFurgo);
        ImageView btnIconUtilitario = (ImageView) findViewById(R.id.iconUtilitario);
        ImageView btnIconDeportivo = (ImageView) findViewById(R.id.iconDeportivo);

        ImageView btnColorNegro = (ImageView) findViewById(R.id.iconColorNegro);
        ImageView btnColorRojo = (ImageView) findViewById(R.id.iconColorRojo);
        ImageView btnColorAzul = (ImageView) findViewById(R.id.iconColorAzul);
        ImageView btnColorVerde = (ImageView) findViewById(R.id.iconColorVerde);


        ImageView btnImagenBorrar = (ImageView) findViewById(R.id.imageTrash);

        nombre.setEnabled(habilitar);
        imageViewCoche.setEnabled(habilitar);
        btnIconFurgo.setEnabled(habilitar);
        btnIconUtilitario.setEnabled(habilitar);
        btnIconDeportivo.setEnabled(habilitar);

        btnColorNegro.setEnabled(habilitar);
        btnColorRojo.setEnabled(habilitar);
        btnColorAzul.setEnabled(habilitar);
        btnColorVerde.setEnabled(habilitar);

        btnImagenBorrar.setEnabled(habilitar);

        if (habilitar == true) {
            imageViewCoche.setColorFilter(dlgCoche.getColor());

            btnColorNegro.setColorFilter(Color.BLACK);
            btnColorRojo.setColorFilter(Color.RED);
            btnColorAzul.setColorFilter(Color.BLUE);
            btnColorVerde.setColorFilter(Color.GREEN);

            btnImagenBorrar.setColorFilter(Color.RED);
        }
        else {
            imageViewCoche.setColorFilter(Color.GRAY);

            btnColorNegro.setColorFilter(Color.GRAY);
            btnColorRojo.setColorFilter(Color.GRAY);
            btnColorAzul.setColorFilter(Color.GRAY);
            btnColorVerde.setColorFilter(Color.GRAY);

            btnImagenBorrar.setColorFilter(Color.GRAY);
        }
    }
}
