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
    private int width;
    private int height;

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
        boolean detected=false;
        for (int i = 0; i < faces.size(); i++) {
            Rect face = faces.get(i);
            opencv_imgproc.rectangle(resizedFrame, new Point(face.x(), face.y()),
                new Point(face.x() + face.width(), face.y() + face.height()),
                new Scalar(0, 255, 0, 0), 2, 8, 0); // Dibuja rectángulo alrededor de la cara
            opencv_imgproc.rectangle(frame, new Point(face.x(), face.y()),
                new Point(face.x() + face.width(), face.y() + face.height()),
                new Scalar(0, 255, 0, 0), 2, 8, 0);

            Mat rostro = new Mat(resizedFrame, face); // Recorta la cara
            Mat resizedRostro = new Mat();
            opencv_imgproc.resize(rostro, resizedRostro, new Size(150, 150)); // Redimensiona el rostro

            // Guarda la imagen del rostro
            try {
                String filePath = emotionsPath + "/rostro_" + count + ".jpg";
                imwrite(filePath, resizedRostro);
                count++;
                matToLibGDXTexture(frame);
                detected=true;
                return true;
            } catch (Exception e) {
                System.out.println("Error al guardar la imagen: " + e.getMessage());
            }
            /*rostro.release();
            resizedRostro.release();*/
        }
        if(!detected) {
            matToLibGDXTexture(frame);
        }


        /*
        faces.clear();
        resizedFrame.release();
        frame.release();
        gray.release();*/
        return false;
    }

    /*public Pixmap matToPixmap(Mat mat) {
        if (mat.channels() != 4) {
            Mat rgbaMat = new Mat();
            cvtColor(mat, rgbaMat, COLOR_BGR2BGRA);  // Convertir BGR a BGRA
            mat = rgbaMat;
        }

        int width = mat.cols();
        //int height = mat.rows();
        //int width=100;
        int height=400;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        byte[] data = new byte[width * height * 4];
        mat.data().get(data);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = data[index++] & 0xFF;
                int g = data[index++] & 0xFF;
                int b = data[index++] & 0xFF;
                int a = data[index++] & 0xFF;

                int pixel = (r << 24) | (g << 16) | (b << 8) | a;
                pixmap.drawPixel(x, y , pixel);
            }
        }

        return pixmap;
    }*/
    public Pixmap matToPixmap(Mat bgr) {
        // ── 1. BGR → RGBA ───────────────────────────────────
        Mat rgba = new Mat();
        cvtColor(bgr, rgba, opencv_imgproc.COLOR_BGR2RGBA);

        int w = rgba.cols();
        int h = 400;       // usa la altura real
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // ── 2. Obtener los bytes RGBA ───────────────────────
        byte[] data = new byte[w * h * 4];
        rgba.data().get(data);

        // ── 3. Copiar invirtiendo X (espejo horizontal) ─────
        int srcIdx;                // índice en data
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // columna destino = w‑1‑x
                srcIdx = (y * w + x) * 4;
                int r = data[srcIdx]     & 0xFF;
                int g = data[srcIdx + 1] & 0xFF;
                int b = data[srcIdx + 2] & 0xFF;
                int a = data[srcIdx + 3] & 0xFF;

                // empaquetar en RGBA8888
                int pixel = (r << 24) | (g << 16) | (b << 8) | a;
                pix.drawPixel(w - 1 - x, y, pixel);  // ← espejo
            }
        }
        return pix;
    }



    public Texture pixmapToTexture(Pixmap pixmap) {
        return new Texture(pixmap);
    }
    private Texture textura;

    public Texture getTextura() {
        return textura;
    }

    public void matToLibGDXTexture(Mat mat) {
        if(textura!=null){textura.dispose();}
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
