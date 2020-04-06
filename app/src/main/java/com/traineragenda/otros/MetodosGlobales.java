package com.traineragenda.otros;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Element;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class MetodosGlobales {

    Context mContext;
    String nombres;
    Double edad;
    Planilla planilla;

    // constructor
    public MetodosGlobales(Context context,Planilla p_planilla,String p_nombres, Double p_edad){
        this.mContext = context;

        planilla = p_planilla;


    }

    public void createPdfPlanilla(){


        //PREPARANDO DATOS PREVIOS
        String[] mAptitudArray;
        mAptitudArray =   mContext.getResources().getStringArray(R.array.array_aptitud);
        String aptitud = mAptitudArray[planilla.getDatosAptitud().intValue()];

        String[] mModalidadArray;
        mModalidadArray =   mContext.getResources().getStringArray(R.array.array_modalidad);
        String modalidad = mModalidadArray[planilla.getDatosModalidad().intValue()];

        String[] mPeriocidadArray;
        mPeriocidadArray =   mContext.getResources().getStringArray(R.array.array_periodicidad);
        String periodicidad = mPeriocidadArray[planilla.getDatosPeriodicidad().intValue()];


        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo_pdf);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 200, true);

        canvas.drawBitmap(bitmap, 0, 10 , null);


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("PLANILHA MENSAL DE TREINAMENTO", 0, 200, paint);

        //PRIMERA COLUMNA DE DATOS
        canvas.drawText(mContext.getResources().getString(R.string.lb_nombre), 0, 220, paint);
        canvas.drawText(nombres, 200, 220, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_edad), 0, 240, paint);
        canvas.drawText(edad.toString(), 200, 240, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_aptitud), 0, 260, paint);
        canvas.drawText(aptitud, 200, 240, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_objetivo), 0, 280, paint);
        canvas.drawText(planilla.getDatosObjetivo(), 200, 280, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_modalidad), 0, 300, paint);
        canvas.drawText(modalidad, 200, 300, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_periodicidad), 0, 320, paint);
        canvas.drawText(periodicidad, 200, 320, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_periodo), 0, 340, paint);
        canvas.drawText(planilla.getDatosPeriodo(), 200, 340, paint);

        canvas.drawText(mContext.getResources().getString(R.string.lb_frecuencia), 0, 360, paint);
        canvas.drawText(planilla.getDatosFrecuencia(), 200, 360, paint);


        // finish the page
        document.finishPage(page);
        // draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"planilla_"+planilla.getPlanilla_id()+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(mContext, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(mContext, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Double getEdad() {
        return edad;
    }

    public void setEdad(Double edad) {
        this.edad = edad;
    }
}
