package com.jrl.juego;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.global.opencv_imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Entrenamiento {

    private static final String DATA_PATH = "./Data"; // Ruta de datos
    static final String RECOGNIZER_METHOD = "LBPH"; // Método de reconocimiento

    public void iniciarEntrenamiento() {
        String[] emotionsList = verificarDirectorioDatos();
        if (emotionsList == null) return;

        listarEmociones(emotionsList);

        List<Integer> labels = new ArrayList<>();
        List<Mat> facesData = recopilarImagenesEtiquetas(emotionsList, labels);

        obtenerModelo(RECOGNIZER_METHOD, facesData, labels);
    }

    public String[] verificarDirectorioDatos() {
        File dataDir = new File(DATA_PATH);
        String[] emotionsList = dataDir.list();
        if (emotionsList == null) {
            System.out.println("No se encontraron carpetas en la ruta especificada.");
        }
        return emotionsList;
    }

    public void listarEmociones(String[] emotionsList) {
        System.out.println("Lista de personas: ");
        for (String emotion : emotionsList) {
            System.out.println(emotion);
        }
    }

    public List<Mat> recopilarImagenesEtiquetas(String[] emotionsList, List<Integer> labels) {
        List<Mat> facesData = new ArrayList<>();
        int label = 0;

        for (String nameDir : emotionsList) {
            String emotionsPath = DATA_PATH + "/" + nameDir;
            File emotionsDir = new File(emotionsPath);
            String[] files = emotionsDir.list();

            if (files == null) continue;

            for (String fileName : files) {
                System.out.println("Rostros: " + nameDir + "/" + fileName);
                labels.add(label);
                Mat face = opencv_imgcodecs.imread(emotionsPath + '/' + fileName, opencv_imgcodecs.IMREAD_GRAYSCALE);
                facesData.add(face);
            }
            label++;
        }
        return facesData;
    }

    public void obtenerModelo(String method, List<Mat> facesData, List<Integer> labels) {
        FaceRecognizer emotionRecognizer = crearReconocedor(method);

        MatVector faces = convertirA_MatVector(facesData);
        Mat labelsMat = crearMatrizEtiquetas(labels);

        entrenarYGuardarModelo(emotionRecognizer, faces, labelsMat, method);
    }

    public FaceRecognizer crearReconocedor(String method) {
        switch (method) {
            case "EigenFaces":
                return EigenFaceRecognizer.create();
            case "FisherFaces":
                return FisherFaceRecognizer.create();
            case "LBPH":
            default:
                return LBPHFaceRecognizer.create();
        }
    }

    public MatVector convertirA_MatVector(List<Mat> facesData) {
        MatVector faces = new MatVector(facesData.size());
        for (int i = 0; i < facesData.size(); i++) {
            faces.put(i, facesData.get(i));
        }
        return faces;
    }

    public Mat crearMatrizEtiquetas(List<Integer> labels) {
        Mat labelsMat = new Mat(labels.size(), 1, org.bytedeco.opencv.global.opencv_core.CV_32SC1);
        for (int i = 0; i < labels.size(); i++) {
            labelsMat.ptr(i).putInt(labels.get(i));
        }
        return labelsMat;
    }

    public void entrenarYGuardarModelo(FaceRecognizer emotionRecognizer, MatVector faces, Mat labelsMat, String method) {
        System.out.println("Entrenando (" + method + ")...");
        long startTime = System.currentTimeMillis();
        emotionRecognizer.train(faces, labelsMat);
        long trainingTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo de entrenamiento (" + method + "): " + trainingTime + " ms");

        emotionRecognizer.save("modelo" + method + ".xml");
        System.out.println("Modelo guardado como: modelo" + method + ".xml");
    }
}
