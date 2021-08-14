package io.github.iamzaid;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class InputDialogController {
    @FXML
    private TextField input1Amplitude;
    @FXML
    private TextField input1Phase;
    @FXML
    private ComboBox<String> input1Waveform;
    @FXML
    private TextField input2Amplitude;
    @FXML
    private TextField input2Phase;
    @FXML
    private ComboBox<String> input2Waveform;

    private Double amplitude1;
    private Double amplitude2;
    private Double phase1;
    private Double phase2;
    private String wave1;
    private String wave2;

    public boolean checkData(){
        try{
            amplitude1=Double.parseDouble(input1Amplitude.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Amplitude must a Decimal number between 0 and 400", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        try{
            amplitude2=Double.parseDouble(input2Amplitude.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Amplitude must a Decimal number between 0 and 400", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        try{
            phase1=Double.parseDouble(input1Phase.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Phase must a Decimal number between 0 and 2", ButtonType.OK);
            alert.showAndWait();
            return false;
        }try{
            phase2=Double.parseDouble(input2Phase.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Amplitude must a Decimal number between 0 and 2", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(phase1*Math.PI<0 || phase1*Math.PI>Math.PI*2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Phase must a Decimal number between 0 and 2", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(phase2*Math.PI<0 || phase2*Math.PI>Math.PI*2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Phase must a Decimal number between 0 and 2", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(amplitude1>400 || amplitude2>400){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Amplitude must a Decimal number between 0 and 400", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(input1Waveform.getValue() == null || input2Waveform.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid Input ! Waveform not selected", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        return true;
    }

    public Double getAmplitude1() {
        return amplitude1;
    }

    public Double getAmplitude2() {
        return amplitude2;
    }

    public Double getPhase1() {
        return phase1;
    }

    public Double getPhase2() {
        return phase2;
    }

    public String getWave1() {
        wave1=input1Waveform.getValue();
        return wave1;
    }

    public String getWave2() {
        wave2=input2Waveform.getValue();
        return wave2;
    }


}
