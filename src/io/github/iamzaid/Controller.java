package io.github.iamzaid;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML
    Canvas canvas;
    @FXML
    LineChart<Number,Number> lineChart;
    @FXML
    NumberAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button controlsButton;

    ImageView controlsImageView;
    private final Image controlsImage = new Image("/GraphMaster99Inputs.png");
    GraphicsContext gc;
    Double originX;
    Double originY;
    XYChart.Series<Number,Number> series1;
    XYChart.Series<Number,Number> series2;
    InputDialogController idc;
    Timer timer;
    private Double amplitude1;
    private Double amplitude2;
    private Double angle1;
    private Double angle2;

    private void drawPhasor(double amplitude,double angle,Color color){
        gc.setStroke(color);
        angle *=Math.PI;
        double x;
        double y;
        //if angle b/w 0 and pi/2 (inc) the Q1
        if(angle >=0 && angle<=Math.PI/2){
            x = amplitude*Math.cos(angle);
            y = amplitude*Math.sin(angle);
            gc.strokeLine(originX,originY,originX+x,originY-y);
        }
        //if angle b/w pi/2 excluded and pi(inc) then Q2
        else if(angle >Math.PI/2 && angle<=Math.PI){
            angle=Math.PI-angle;
            x = amplitude*Math.cos(angle);
            y = amplitude*Math.sin(angle);
            gc.strokeLine(originX,originY,originX-x,originY-y);
        }//If angle b/w pi (excluded) and 3pi/2(inc) then Q3
        else if(angle >Math.PI && angle<=(3*Math.PI)/2){
            angle=angle-Math.PI;
            x = amplitude*Math.cos(angle);
            y = amplitude*Math.sin(angle);
            gc.strokeLine(originX,originY,originX-x,originY+y);
        }
        //If angle b/w 3pi/2 (excluded) and 2pi(inc) then Q4
        else if((angle >(3*Math.PI)/2) && angle<=2*Math.PI){
            angle=(2*Math.PI)-angle;
            x = amplitude*Math.cos(angle);
            y = amplitude*Math.sin(angle);
            gc.strokeLine(originX,originY,originX+x,originY+y);
        }
    }

    private void printSinGraph(Double amplitude,Double initialPhase,XYChart.Series series){
        for(Double i=0.0;i<=Math.PI*2;i+=0.05){
            series.getData().add(new XYChart.Data<>(i,amplitude*Math.sin(i+initialPhase)));
        }
    }
    private void printCosGraph(Double amplitude,Double initialPhase, XYChart.Series series) {
        for(double i=-Math.PI;i<=Math.PI;i+=0.05){
            series.getData().add(new XYChart.Data<>(i, amplitude*Math.cos(i+initialPhase)));
        }
    }
    @FXML
    public void showInputDialog(){
        Dialog<ButtonType> inputDialog = new Dialog<>();
        inputDialog.initOwner(mainAnchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("inputDialog.fxml"));
        try {
            inputDialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
        }
        inputDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        inputDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = inputDialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            idc = fxmlLoader.getController();
            if(idc.checkData()){
                controlsButton.setDisable(true);
                startButton.setDisable(false);
            }
        }
    }

    public void start() {
        if(idc.checkData()){
            amplitude1=idc.getAmplitude1();
            amplitude2=idc.getAmplitude2();
            angle1=idc.getPhase1();
            angle2=idc.getPhase2();
            if(idc.getWave1().equals("Sin wave")){
                printSinGraph(idc.getAmplitude1(),idc.getPhase1(),series1);
            }else if(idc.getWave1().equals("Cos wave")){
                printCosGraph(idc.getAmplitude1(),idc.getPhase1(),series1);
            }
            if(idc.getWave2().equals("Sin wave")){
                printSinGraph(idc.getAmplitude2(), idc.getPhase2(), series2);
            }else if(idc.getWave2().equals("Cos wave")){
                printCosGraph(idc.getAmplitude2(), idc.getPhase2(), series2);
            }
            lineChart.getData().addAll(series1,series2);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    clearRectangle();
                    drawPhasor(amplitude1,angle1,Color.ORANGE);
                    drawPhasor(amplitude2,angle2,Color.RED);
                    if(angle1<=2){
                        angle1+=0.1;
                    }else {
                        angle1=0.0;
                    }
                    if(angle2<=2){
                        angle2+=0.1;
                    }else {
                        angle2=0.0;
                    }
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(task,0,500);
            startButton.setDisable(true);
            stopButton.setDisable(false);
        }
    }

    public void stop(){
        series1.getData().clear();
        series2.getData().clear();
        lineChart.getData().removeAll(series1,series2);
        clearRectangle();
        controlsButton.setDisable(false);
        stopButton.setDisable(true);
        timer.cancel();
    }

    private void clearRectangle(){
        gc.clearRect(0,0,400,400);
        gc.fillRect(0,0,400,400);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0,200,400,200);
        gc.strokeLine(200,0,200,400);
    }

    public void initialize(){
        controlsImageView = new ImageView(controlsImage);
        controlsButton.setGraphic(controlsImageView);
        //Canvas
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        originX = canvas.getWidth()/2;
        originY = canvas.getHeight()/2;
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,400,400);
        gc.strokeLine(0,200,400,200);
        gc.strokeLine(200,0,200,400);
        //LineGraph
        lineChart.setCreateSymbols(false);
        series1 = new XYChart.Series();
        series1.setName("Wave1");
        series2 = new XYChart.Series();
        series2.setName("Wave2");
        stopButton.setDisable(true);
        startButton.setDisable(true);


    }
}
