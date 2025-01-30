package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import java.io.File;

public class EmotionCapture {

    private String dataPath;
    private VideoCapture cap;
    private CascadeClassifier faceClassif;
    private int count = 0;

    public EmotionCapture(String dataPath) {
        this.dataPath = dataPath;
        this.cap = new VideoCapture(0); // Inicializar la captura de video una vez
        this.faceClassif = new CascadeClassifier(Gdx.files.local("haarcascade_frontalface_default.xml").file().getAbsolutePath()); // Inicializar el clasificador una vez
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

        Mat resizedFrame = new Mat();
        opencv_imgproc.resize(frame, resizedFrame, new Size(640, 480)); // Redimensiona el frame
        Mat gray = new Mat();
        if (resizedFrame.channels() == 1) {
            // Convertir de escala de grises a BGR antes de hacer la conversión a escala de grises
            Mat temp = new Mat();
            opencv_imgproc.cvtColor(resizedFrame, temp, opencv_imgproc.COLOR_GRAY2BGR);
            opencv_imgproc.cvtColor(temp, gray, opencv_imgproc.COLOR_BGR2GRAY); // Ahora a escala de grises
        } else if (resizedFrame.channels() == 3) {
            opencv_imgproc.cvtColor(resizedFrame, gray, opencv_imgproc.COLOR_BGR2GRAY); // Convierte a escala de grises
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
        }
        return false;
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
