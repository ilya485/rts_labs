package Lab21;

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

        return x;
    }

    public static double[][] DFT(float[] x) {
        int N = x.length;
        double[][] dft = new double[x.length][2]; //перший стовпчик - дійсна частина, другий - уявна
        for (int p = 0; p < N; p++) {
            for (int k = 0; k < N; k++) {
                dft[p][0] += Math.cos(2 * Math.PI * p * k / N) * x[k];
                dft[p][1] += Math.sin(-2 * Math.PI * p * k / N) * x[k];
            }
        }
        return dft;
    }

    public static void main(String[] args) {
        launch(args);
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
        XYChart.Series signal = new XYChart.Series();
        XYChart.Series fourier = new XYChart.Series();
        ObservableList<XYChart.Data> data_x = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> data_dft = FXCollections.observableArrayList();

        float[] X = graph();
        double[][] dft = DFT(X);
        double a = 0;
        for (int i = 0; i < X.length; i++) {
            data_x.add(new XYChart.Data(i, X[i]));
            a = Math.pow(dft[i][0], 2) + Math.pow(dft[i][1], 2);
            data_dft.add(new XYChart.Data(i, a));
        }
        signal.setData(data_x);
        fourier.setData(data_dft);
        numberLineChart.getData().add(signal);

        //Scene Scene1 = new Scene(numberLineChart1, 0, 0);

        numberLineChart1.getData().add(fourier);
        Scene scene = new Scene(numberLineChart);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Scene Scene1 = new Scene(numberLineChart1, 0, 0);
        Scene Scene2 = new Scene(numberLineChart1, 0, 0);

        //New window (Stage)
        //Stage Window1 = new Stage();
        //Window1.setScene(Scene1);
        Stage Window2 = new Stage();
        Window2.setScene(Scene2);
        Window2.show();


    }

}
