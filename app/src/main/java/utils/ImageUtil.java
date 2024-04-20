package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    // Método para convertir un Bitmap a un array de bytes
    public byte[] transformarBitmapBytes(Bitmap bitmap) {
        // Crea un nuevo objeto ByteArrayOutputStream para almacenar los datos de la imagen en bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Comprime el bitmap en formato PNG y escribe los datos comprimidos en el ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        // Convierte el ByteArrayOutputStream a un array de bytes y lo devuelve
        return stream.toByteArray();
    }

    // Método para convertir un array de bytes a un Bitmap
    public Bitmap transformarBytesBitmap(byte[] imagenBytes) {
        if (imagenBytes == null || imagenBytes.length == 0) {
            return null;
        }
        // Convierte los bytes de la imagen en un objeto Bitmap y lo devuelve
        return BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
    }

    // Método para redimensionar un Bitmap a un nuevo tamaño
    public Bitmap redimensionarImagen(Bitmap imagenOriginal, int nuevoAncho, int nuevoAlto) {
        // Obtiene las dimensiones originales de la imagen
        int anchoOriginal = imagenOriginal.getWidth();
        int altoOriginal = imagenOriginal.getHeight();

        // Calcula las escalas de redimensionamiento para el ancho y el alto
        float escalaAncho = ((float) nuevoAncho) / anchoOriginal;
        float escalaAlto = ((float) nuevoAlto) / altoOriginal;

        // Crea una matriz de redimensionamiento y aplica las escalas
        Matrix matrizRedimensionamiento = new Matrix();
        matrizRedimensionamiento.postScale(escalaAncho, escalaAlto);

        // Crea y devuelve una nueva imagen redimensionada utilizando la matriz de redimensionamiento
        return Bitmap.createBitmap(imagenOriginal, 0, 0, anchoOriginal, altoOriginal, matrizRedimensionamiento, true);
    }

}
