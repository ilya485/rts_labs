package Lab22;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.pow;
import static javafx.application.Application.launch;

public class Main extends Application {

    public static float[] graph() {

        //Variant
        int n = 10, W_max = 2700, N = 256;

        float x[] = new float[N];
        Random ran = new Random();

        int A, W, f;
        for (int i = 0; i < n; i++) {
            A = ran.nextInt(2);
            W = ran.nextInt(W_max);
            f = ran.nextInt();
            for (int t = 0; t < N; t++) {
                x[t] += A * Math.sin(W * t + f);

            }

        }
        // for (int t = 0; t < N; t++)  {
        //System.out.println(x[t]);}
        return x;
    }

    public static double[][] DFT(float[] x) {
        int N = x.length;
        double[][] dft = new double[N][2]; //перший стовпчик - дійсна частина, другий - уявна
        for (int p = 0; p < N; p++) {
            for (int k = 0; k < N; k++) {
                dft[p][0] += Math.cos(2 * Math.PI * p * k / N) * x[k];
                //System.out.print(dft[p][0] + "    " );
                dft[p][1] += Math.sin(2 * Math.PI * p * k / N) * x[k];
                //System.out.print(dft[p][1] + "    " );
            }
        }
        return dft;
    }


    public static double[][] FFT(float[] x) {
        int N = x.length;
        double[][] fft = new double[N][2]; //перший стовпчик - дійсна частина, другий - уявна
        double[] array1 = new double[2];
        double[] array2 = new double[2];

        for (int i = 0; i < N / 2; i++) {
            double cos;
            double sin;
            array1[0] = array1[1] = array2[0] = array2[1] = 0;
            for (int j = 0; j < N / 2; j++) {
                cos = Math.cos(4 * Math.PI * i * j / N);
                sin = Math.sin(4 * Math.PI * i * j / N);
                array1[0] += x[2 * j + 1] * cos;  //real
                array1[1] += x[2 * j + 1] * sin;  //image
                array2[0] += x[2 * j] * cos; //real
                array2[1] += x[2 * j] * sin; //image
            }

            cos = Math.cos(2 * Math.PI * i / N);
            sin = Math.sin(2 * Math.PI * i / N);
            fft[i][0] = array2[0] + array1[0] * cos - array1[1] * sin; //real
            fft[i][1] = array2[1] + array1[0] * sin + array1[1] * cos; //image
            fft[i + N / 2][0] = array2[0] - (array1[0] * cos - array1[1] * sin); //real
            fft[i + N / 2][1] = array2[1] - (array1[0] * sin + array1[1] * cos); //image
        }
        return fft;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        NumberAxis d = new NumberAxis();
        NumberAxis j = new NumberAxis();
        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x, y);
        LineChart<Number, Number> numberLineChart1 = new LineChart<Number, Number>(d, j);
        LineChart<Number, Number> numberLineChart2 = new LineChart<Number, Number>(d, j);
        XYChart.Series signal = new XYChart.Series();
        XYChart.Series fourier1 = new XYChart.Series();
        XYChart.Series fourier2 = new XYChart.Series();
        ObservableList<XYChart.Data> data_x = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> data_dft = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> data_fft = FXCollections.observableArrayList();

        float[] X = graph();
        double[][] dft = DFT(X);
        double[][] fft = FFT(X);

        double a = 0;
        double b = 0;
        for (int i = 0; i < X.length; i++) {
            data_x.add(new XYChart.Data(i, X[i]));
            a = Math.sqrt(Math.pow(dft[i][0], 2) + Math.pow(dft[i][1], 2));
            b = Math.sqrt(Math.pow(fft[i][0], 2) + Math.pow(fft[i][1], 2));
            data_dft.add(new XYChart.Data(i, a));
            data_fft.add(new XYChart.Data(i, b));
        }
        // }
        signal.setData(data_x);
        signal.setName("Сигнал");
        fourier1.setData(data_dft);
        fourier1.setName("Дискрктне перетворення фур'є");
        fourier2.setData(data_fft);
        fourier2.setName("Швидке перетворення фур'є");
        //numberLineChart.getData().add(signal);
        numberLineChart.getData().add(signal);

        //Scene Scene1 = new Scene(numberLineChart1, 0, 0);

        numberLineChart1.getData().add(fourier1);
        numberLineChart2.getData().add(fourier2);
        Scene scene = new Scene(numberLineChart);
        numberLineChart.setCreateSymbols(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        Scene Scene1 = new Scene(numberLineChart1, 0, 0);
        Scene Scene2 = new Scene(numberLineChart2, 0, 0);
        numberLineChart1.setCreateSymbols(false);
        numberLineChart2.setCreateSymbols(false);

        Stage Window = new Stage();
        Stage Window2 = new Stage();
        Window.setScene(Scene1);
        Window.show();
        Window2.setScene(Scene2);
        Window2.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
