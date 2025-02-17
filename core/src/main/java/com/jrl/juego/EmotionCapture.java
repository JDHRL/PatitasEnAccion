package com.jrl.juego;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGRA;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.badlogic.gdx.graphics.Texture;

public class EmotionCapture {

    private String dataPath;
    private VideoCapture cap;
    private CascadeClassifier faceClassif;
    private int count = 0;

    public EmotionCapture(String dataPath) {
        this.dataPath = dataPath;

        try {
            this.cap = new VideoCapture(1); // Intentar inicializar la cámara 1
            if (!cap.isOpened()) {
                throw new Exception("No se pudo abrir la cámara 1");
            }
        } catch (Exception e) {
            System.out.println("Error al abrir la cámara 1: " + e.getMessage());
            System.out.println("Probando con la cámara 0...");
            this.cap = new VideoCapture(0); // Intentar con la cámara 0
            if (!cap.isOpened()) {
                System.out.println("Error al abrir la cámara 0.");
            } else {
                System.out.println("Cámara 0 abierta con éxito.");
            }
        } // Inicializar la captura de video una vez
        this.faceClassif = new CascadeClassifier(copyHaarCascadeToLocal());// Inicializar el clasificador una vez
    }
    public static String copyHaarCascadeToLocal() {
        System.out.println("COPIANDO");
        String localPath = Gdx.files.getExternalStoragePath() +  "/haarcascade_frontalface_default.xml";

        File file = new File(localPath);
        if (file.exists()) {
            return localPath; // Si ya existe, no lo copiamos de nuevo
        }

        FileHandle assetFile = Gdx.files.internal("haarcascade_frontalface_default.xml");

        try (InputStream input = assetFile.read();
             FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return localPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean captureEmotions(String emotionName) {
        String emotionsPath = dataPath + "/" + emotionName;

        // Crear carpeta para emociones si no existe
        FileHandle emotionsDir = Gdx.files.local(emotionsPath);
        if (!emotionsDir.exists()) {
            try {
                emotionsDir.mkdirs(); // Crea la carpeta
                System.out.println("Carpeta creada: " + emotionsPath);
            } catch (Exception e) {
                System.out.println("Error al crear la carpeta: " + emotionsPath);
                return false;
            }
        }

        Mat frame = new Mat();
        if (!cap.read(frame)) {
            System.out.println("Error al leer el frame de la cámara.");
            return false;
        }
        //matToLibGDXTexture(frame);
        Mat resizedFrame = new Mat();
        opencv_imgproc.resize(frame, resizedFrame, new Size(640, 480)); // Redimensiona el frame
        Mat gray = new Mat();
        if (resizedFrame.channels() == 1) {
            // Convertir de escala de grises a BGR antes de hacer la conversión a escala de grises
            Mat temp = new Mat();
            cvtColor(resizedFrame, temp, opencv_imgproc.COLOR_GRAY2BGR);
            cvtColor(temp, gray, opencv_imgproc.COLOR_BGR2GRAY); // Ahora a escala de grises
        } else if (resizedFrame.channels() == 3) {
            cvtColor(resizedFrame, gray, opencv_imgproc.COLOR_BGR2GRAY); // Convierte a escala de grises
        } else {
           System.out.println("canales no soportados");
        }

        RectVector faces = new RectVector();
        Size minSize = new Size(30, 30); // Tamaño mínimo de detección
        Size maxSize = new Size(); // Sin tamaño máximo (0, 0)

        // Llama a detectMultiScale con todos los parámetros requeridos
        faceClassif.detectMultiScale(gray, faces, 1.3, 5, 0, minSize, maxSize); // Detecta caras

        for (int i = 0; i < faces.size(); i++) {
            Rect face = faces.get(i);
            opencv_imgproc.rectangle(resizedFrame, new Point(face.x(), face.y()),
                new Point(face.x() + face.width(), face.y() + face.height()),
                new Scalar(0, 255, 0, 0), 2, 8, 0); // Dibuja rectángulo alrededor de la cara

            Mat rostro = new Mat(resizedFrame, face); // Recorta la cara
            Mat resizedRostro = new Mat();
            opencv_imgproc.resize(rostro, resizedRostro, new Size(150, 150)); // Redimensiona el rostro

            // Guarda la imagen del rostro
            try {
                String filePath = emotionsPath + "/rostro_" + count + ".jpg";
                imwrite(filePath, resizedRostro);
                count++;

                return true;
            } catch (Exception e) {
                System.out.println("Error al guardar la imagen: " + e.getMessage());
            }
            /*rostro.release();
            resizedRostro.release();*/
        }
        /*
        faces.clear();
        resizedFrame.release();
        frame.release();
        gray.release();*/
        return false;
    }
    public Pixmap matToPixmap(Mat mat) {
        // Asegurarse de que la imagen esté en formato RGBA (4 canales)
        if (mat.channels() != 4) {
            Mat rgbaMat = new Mat();
            cvtColor(mat, rgbaMat, COLOR_BGR2BGRA);  // Convierte BGR a RGBA
            mat = rgbaMat;
        }

        int width = mat.cols();
        int height = mat.rows();

        // Crear un Pixmap para almacenar la imagen convertida
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Obtener los datos de la imagen
        byte[] data = new byte[width * height * 4];  // RGBA (4 bytes por píxel)
        mat.data().get(data);  // Llenar el array con los datos de la imagen

        // Copiar los píxeles del Mat al Pixmap
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Obtener el valor RGBA
                int r = data[index++] & 0xFF;  // Rojo
                int g = data[index++] & 0xFF;  // Verde
                int b = data[index++] & 0xFF;  // Azul
                int a = data[index++] & 0xFF;  // Alfa

                // Construir el valor del píxel (RGBA)
                int pixel = (a ) | (r ) | (g) | b;

                // Asignar el píxel al Pixmap
                pixmap.drawPixel(x, height - y - 1, pixel);  // Invertir eje Y para coincidir con LibGDX
            }
        }

        return pixmap;
    }

    public Texture pixmapToTexture(Pixmap pixmap) {
        return new Texture(pixmap);
    }
    private Texture textura;

    public Texture getTextura() {
        return textura;
    }

    public void matToLibGDXTexture(Mat mat) {
        Pixmap pixmap = matToPixmap(mat);
        textura=pixmapToTexture(pixmap);
    }

    // Función auxiliar para escribir imágenes
    private static void imwrite(String filePath, Mat image) {
        // Usa la función correspondiente para escribir la imagen
        opencv_imgcodecs.imwrite(filePath, image);
    }

    // Método para liberar la cámara cuando ya no se necesite capturar más
    public void release() {
        cap.release();
    }
}
