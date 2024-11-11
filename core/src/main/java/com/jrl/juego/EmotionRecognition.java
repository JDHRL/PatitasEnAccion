package com.jrl.juego;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_videoio.VideoCapture; // Cambiar a VideoCapture de Bytedeco
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class EmotionRecognition {

    private FaceRecognizer emotionRecognizer;
    private CascadeClassifier faceClassifier;
    protected VideoCapture videoCapture;

    public EmotionRecognition() {
        this.emotionRecognizer = LBPHFaceRecognizer.create();
        this.faceClassifier = new CascadeClassifier("haarcascade_frontalface_default.xml");
        this.videoCapture = new VideoCapture(0);
    }

    public Mat emotionImage(String emotion) {
        Mat image = new Mat();
        switch (emotion) {
            case "Felicidad":
                image = opencv_imgcodecs.imread("Emojis/felicidad.jpeg");
                break;
            case "Enojo":
                image = opencv_imgcodecs.imread("Emojis/enojo.jpeg");
                break;
            case "Sorpresa":
                image = opencv_imgcodecs.imread("Emojis/sorpresa.jpeg");
                break;
            case "Tristeza":
                image = opencv_imgcodecs.imread("Emojis/tristeza.jpeg");
                break;
        }
        return image;
    }

    public void setup() {
        try {
            emotionRecognizer.read("modeloLBPH.xml");
        } catch (Exception e) {
            System.out.println("Error loading model: " + e.getMessage());
        }
    }

    public List<String> loadImagePaths(String dataPath) {
        List<String> imagePaths = new ArrayList<>();
        try {
            Files.list(Paths.get(dataPath)).forEach(path -> imagePaths.add(path.toString()));
        } catch (Exception e) {
            System.out.println("Error reading image paths: " + e.getMessage());
        }
        return imagePaths;
    }

    public String captureAndRecognize() {
        Mat frame = new Mat();
        if (!videoCapture.read(frame)) return "No se pudo capturar el fotograma";

        Mat gray = new Mat();
        opencv_imgproc.cvtColor(frame, gray, opencv_imgproc.COLOR_BGR2GRAY);
        Mat auxFrame = gray.clone();

        // Definir los tamaños mínimos y máximos de la cara que quieres detectar
        org.bytedeco.opencv.opencv_core.Size minSize = new org.bytedeco.opencv.opencv_core.Size(30, 30);
        org.bytedeco.opencv.opencv_core.Size maxSize = new org.bytedeco.opencv.opencv_core.Size(); // Sin especificar tamaño máximo

        // Llama a detectMultiScale con los parámetros correctos
        org.bytedeco.opencv.opencv_core.RectVector faces = new org.bytedeco.opencv.opencv_core.RectVector();
        faceClassifier.detectMultiScale(gray, faces, 1.3, 5, 0, minSize, maxSize);

        List<String> imagePaths = loadImagePaths("./Data"); // Cambia a la ruta donde hayas almacenado Data

        String emotionDetected = "No se detectó ninguna emoción"; // Valor predeterminado
        for (int i = 0; i < faces.size(); i++) {
            org.bytedeco.opencv.opencv_core.Rect face = faces.get(i);
            Mat rostro = new Mat(auxFrame, face);
            opencv_imgproc.resize(rostro, rostro, new org.bytedeco.opencv.opencv_core.Size(150, 150));

            int[] label = new int[1];
            double[] confidence = new double[1];
            emotionRecognizer.predict(rostro, label, confidence);

            if (confidence[0] < 60) {
                emotionDetected = new File(imagePaths.get(label[0])).getName();
                break; // Salir del bucle una vez que se detecta una emoción
            }
        }

        // Manejo de excepciones para entrada/salida
        try {
            // Presiona 'Esc' para salir
            if (System.in.available() > 0) {
                char k = (char) System.in.read();
                if (k == 27) return "Detención por Esc"; // 27 es el código ASCII para Esc
            }
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        }

        return emotionDetected;
    }

    public static void main(String[] args) {
        EmotionRecognition emotionRecognition = new EmotionRecognition();
        emotionRecognition.setup();

        // Capturar y reconocer en lugar de un ciclo infinito
        String detectedEmotion = emotionRecognition.captureAndRecognize();
        System.out.println("Emoción detectada: " + detectedEmotion);

        // Liberar la captura de video
        emotionRecognition.videoCapture.release();
    }
}

