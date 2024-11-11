package com.jrl.juego;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.pattern.ElmanPattern;

import java.util.LinkedList;

public class DogMoodPredictor {

    private BasicNetwork network;
    LinkedList<double[]> moodHistory; // Almacena el historial de ánimo del perro y emociones

    private static final int MAX_HISTORY_SIZE = 5; // Número de estados anteriores a considerar
    private static final double MAX_INPUT_VALUE = 4.0; // Máximo valor de entrada (emociones)
    private static final double MAX_OUTPUT_VALUE = 100.0; // Máximo valor de salida (ánimo)

    // Constructor que crea y entrena la red
    public DogMoodPredictor() {
        this.network = createElmanNetwork(1, 7, 1); // Cambiar inputNeurons a 2 para incluir userEmotion
        this.moodHistory = new LinkedList<>();
        // Entrenar la red con datos de entrenamiento iniciales
        trainNetwork(generateTrainingData());
    }

    // Método para crear una red Elman
    private BasicNetwork createElmanNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {
        ElmanPattern pattern = new ElmanPattern();
        pattern.setActivationFunction(new ActivationSigmoid());
        pattern.setInputNeurons(inputNeurons);
        pattern.addHiddenLayer(hiddenNeurons);
        pattern.setOutputNeurons(outputNeurons);
        return (BasicNetwork) pattern.generate();
    }

    // Método para generar datos de entrenamiento
    private MLDataSet generateTrainingData() {
        // Aquí irían tus datos de entrenamiento inicial
        double[][] input = {
            {1}, {4}, {3}, {1}, {2},
            {4}, {3}, {4}, {2}, {1},{2}
        };

        double[][] output = {
            {91}, {30}, {10}, {99}, {10},
            {38}, {19}, {41}, {9}, {98},{9}
        };

        // Normalizar los datos de entrada
        for (int i = 0; i < input.length; i++) {
            input[i][0] /= MAX_INPUT_VALUE; // Normaliza la emoción del usuario
        }

        // Normalizar los datos de salida
        for (int i = 0; i < output.length; i++) {
            output[i][0] /= MAX_OUTPUT_VALUE; // Normaliza el ánimo del perro
        }

        return new BasicMLDataSet(input, output);
    }

    // Método para entrenar la red
    private void trainNetwork(MLDataSet trainingSet) {
        CalculateScore score = new TrainingSetScore(trainingSet);
        MLTrain trainAnneal = new NeuralSimulatedAnnealing(network, score, 90, 2, 100);
        MLTrain trainMain = new Backpropagation(network, trainingSet, 0.00005, 0.3);

        StopTrainingStrategy stop = new StopTrainingStrategy();
        trainMain.addStrategy(new Greedy());
        trainMain.addStrategy(new HybridStrategy(trainAnneal));
        trainMain.addStrategy(stop);

        int epoch = 0;
        while (!stop.shouldStop()) {
            trainMain.iteration();
            //System.out.println("Epoch #" + epoch + " Error: " + trainMain.getError());
            epoch++;
        }
    }

    // Método para realizar la predicción del ánimo del perro secuencialmente
    public double predictDogMood(double currentDogMood, double userEmotion) {
        // Agregar el estado actual a la historia
        if (moodHistory.size() >= MAX_HISTORY_SIZE) {
            moodHistory.removeFirst(); // Elimina el más antiguo si se alcanza el tamaño máximo
        }
        moodHistory.add(new double[]{currentDogMood, userEmotion});

        // Preparar los datos de entrada para la predicción
        double[] input = new double[2];
        if (moodHistory.size() < 2) {
            throw new IllegalStateException("Se requieren al menos 2 estados anteriores para realizar una predicción.");
        }

        // Usar el último estado para la predicción
        input[0] = moodHistory.getLast()[0] / MAX_OUTPUT_VALUE; // Normaliza el ánimo del perro
        input[1] = moodHistory.getLast()[1] / MAX_INPUT_VALUE; // Normaliza la emoción del usuario

        MLData mlData = new BasicMLData(input);
        MLData output = network.compute(mlData);

        // Desnormalizar la salida a un valor en el rango original (0-100)
        return output.getData(0) * MAX_OUTPUT_VALUE;
    }

    // Método para cerrar Encog
    public void shutdown() {
        Encog.getInstance().shutdown();
    }
}
