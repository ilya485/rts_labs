package Lab12;

import java.util.Random;

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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        NumberAxis t = new NumberAxis();
        NumberAxis cor = new NumberAxis();
        NumberAxis a = new NumberAxis();
        NumberAxis b = new NumberAxis();
        long TM = 0;
        long TD = 0;
        float m = 0;
        float d = 0;


        //float val_x[] = graph();
        //float val_y[] = graph();

        long timeBefore = System.nanoTime();
        //m = expectation(val_x);
        long timeAfter = System.nanoTime();
        TM = timeAfter - timeBefore;
        timeBefore = System.nanoTime();
        //d = disp(val_x, m);
        timeAfter = System.nanoTime();
        TD = timeAfter - timeBefore;

        float[] cor_xx, cor_xy;
        //cor_xx = cor(val_x, val_x);
        //cor_xy = cor(val_x, val_y);

        long[] t_xy = new long[1000 - 256];
        long[] t_xx = new long[1000 - 256];
        for (int i = 256; i < 1000; i++) {


            float val_x[] = graph(i);
            float val_y[] = graph(i);

            timeBefore = System.nanoTime();
            cor_xy = cor(val_x, val_y);
            timeAfter = System.nanoTime();
            t_xy[i - 256] = timeAfter - timeBefore;

            timeBefore = System.nanoTime();
            cor_xx = cor(val_x, val_x);
            timeAfter = System.nanoTime();
            t_xx[i - 256] = timeAfter - timeBefore;


        }
        System.out.println("Math Expectation: " + m + "     " + TM + "" + "\n" + "Dispersion: " + d + "     " + TD + "");


        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x, y);
        //LineChart<Number, Number> numberLineChart1 = new LineChart<Number, Number>(cor, t);
        //LineChart<Number, Number> numberLineChart2 = new LineChart<Number, Number>(a, b);


        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        XYChart.Series series4 = new XYChart.Series();

        ObservableList<XYChart.Data> data_x = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> data_y = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data> data_xx = FXCollections.observableArrayList();
//        ObservableList<XYChart.Data> data_xy = FXCollections.observableArrayList();


        for (int i = 0; i < 1000 - 256; i++) {
            //if (time[i]<10000)
            if (t_xx[i] < 350000)
                data_x.add(new XYChart.Data(i + 256, t_xx[i]));
            if (t_xy[i] < 350000)
                data_y.add(new XYChart.Data(i + 256, t_xy[i]));
        }

        /*for (int i = 0; i < val_x.length; i++) {
            //if (time[i]<10000)
            data_x.add(new XYChart.Data(i, val_x[i]));
            data_y.add(new XYChart.Data(i, val_y[i]));}
        for (int i = 0; i < val_x.length/2; i++) {
            data_xx.add(new XYChart.Data(i, cor_xx[i]));
            data_xy.add(new XYChart.Data(i, cor_xy[i]));

        }
*/

        series1.setData(data_x);
        series2.setData(data_y);
        //series3.setData(data_xx);
        //series4.setData(data_xy);
        series1.setName("xx");
        series2.setName("xy");
        series3.setName("cor xx");
        series4.setName("cor xy");

        Scene scene = new Scene(numberLineChart);

        numberLineChart.getData().add(series1);
        numberLineChart.getData().add(series2);
        //numberLineChart1.getData().add(series3);
        //numberLineChart2.getData().add(series4);
        primaryStage.setScene(scene);
        primaryStage.show();

        Group root1 = new Group();
        Group root2 = new Group();
        //Scene Scene1 = new Scene(numberLineChart1, 0, 0);
        //Scene Scene2 = new Scene(numberLineChart2, 0, 0);

        //New window (Stage)
        //Stage Window1 = new Stage();
        //Window1.setScene(Scene1);
        //Stage Window2 = new Stage();
        //Window2.setScene(Scene2);


        // Set position of second window, related to primary window.|

        //Window1.show();
        //Window2.show();

    }


    public static float[] graph(int N) {

        /*Variant*/
        int n = 10, W_max = 2700;

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

    public static float expectation(float[] x) {
        float m = 0;
        for (int i = 0; i < x.length; i++) {
            m += x[i];
        }
        //System.out.println(m);
        m /= x.length;
        return m;
    }

    public static float expectation(float[] x, int t) {
        float m = 0;
        for (int i = t; i < x.length; i++) {
            m += x[i];
        }
        //System.out.println(m);
        m /= (x.length - t);
        return m;
    }

    public static float disp(float[] x, float m) {
        float d = 0;
        for (int i = 0; i < x.length; i++) {
            d += Math.pow(x[i] - m, 2);
        }
        d /= x.length;
        return d;
    }

    public static float[] cor(float[] x, float[] y) {
        float R[] = new float[x.length / 2];
        float Mx = expectation(x);
        float My = expectation(y);
        for (int t = 0; t < x.length / 2 - 1; t++) {
            R[t] = 0;
            for (int i = 0; i < x.length / 2 - 1; i++) {
                R[t] += (x[i] - Mx) * (y[i + t] - My);
            }
            R[t] /= (x.length - 1);
            t++;
        }
        return R;
    }

    public static void main(String[] args) {
        launch(args);

    }
}
