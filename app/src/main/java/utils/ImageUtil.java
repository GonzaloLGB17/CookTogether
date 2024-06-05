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

        // Escoge la escala más pequeña para asegurar que la imagen quepa dentro del nuevo tamaño sin distorsionarla
        float escala = Math.min(escalaAncho, escalaAlto);

        // Calcula las nuevas dimensiones conservando la relación de aspecto
        int nuevoAnchoImagen = Math.round(anchoOriginal * escala);
        int nuevoAltoImagen = Math.round(altoOriginal * escala);

        // Redimensiona la imagen utilizando las nuevas dimensiones
        return Bitmap.createScaledBitmap(imagenOriginal, nuevoAnchoImagen, nuevoAltoImagen, true);
    }

    // Método para comprimir una imagen
    public byte[] comprimirImagen(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Método para optimizar una imagen para que pese menos de 1MB
    public byte[] optimizarImagen(Bitmap bitmap, int maxWidth, int maxHeight, int quality) {
        Bitmap resizedBitmap = redimensionarImagen(bitmap, maxWidth, maxHeight);
        byte[] imageBytes = comprimirImagen(resizedBitmap, quality);

        // Ajusta la calidad hasta que el tamaño sea menor a 1MB
        while (imageBytes.length > 1024 * 1024 && quality > 10) {
            quality -= 5;
            imageBytes = comprimirImagen(resizedBitmap, quality);
        }

        return imageBytes;
    }

}
