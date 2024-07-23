package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import Model.Model;
import view_model.ViewModel;

public class WindowController extends Observable {
    @FXML
    Canvas joystick;

    @FXML
    Slider rudderSlide;

    @FXML
    Slider throttleSlide;

    ViewModel vm;

    DoubleProperty aileron,elevator;

    private Model model;
    boolean mousePushed;
    double jx, jy;
    double mx, my;


    public WindowController() {
        mousePushed = false;
        jx = 0;
        jy = 0;
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
    }

    void init(ViewModel vm){
        this.vm=vm;
        vm.throttle.bind(throttleSlide.valueProperty());
        vm.rudder.bind(rudderSlide.valueProperty());
        vm.aileron.bind(aileron);
        vm.elevators.bind(elevator);
    }

    @FXML
    public void initialize() {
        sliderInit();
    }

    private void sliderInit() {
        rudderSlide.setMin(-1);
        rudderSlide.setMax(1);
        rudderSlide.setShowTickMarks(true);
        rudderSlide.setBlockIncrement(0.4);

        throttleSlide.setMin(-1);
        throttleSlide.setMax(1);
        throttleSlide.setShowTickMarks(true);
        throttleSlide.setBlockIncrement(0.2);

        rudderSlide.setValue(0);
        throttleSlide.setValue(1);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void paint() {
        GraphicsContext gc = joystick.getGraphicsContext2D();
        mx = joystick.getWidth() / 2;
        my = joystick.getHeight() / 2;
        gc.clearRect(0, 0, joystick.getWidth(), joystick.getHeight());
        gc.strokeOval(jx - 50, jy - 50, 100, 100);
        aileron.set((jx - mx) / mx);
        elevator.set((jy - my) / my);
        // Clamp values to [-1, 1]
        aileron.set(Math.max(-1, Math.min(1, aileron.get())));
        elevator.set(Math.max(-1, Math.min(1, elevator.get())));

        //System.out.println("Aileron: " + aileron + ", Elevator: " + elevator);
    }

    @FXML
    public void mouseDown(MouseEvent me) {
        if (!mousePushed) {
            mousePushed = true;
            //System.out.println("Mouse is down");
        }
    }

    @FXML
    public void mouseUp(MouseEvent me) {
        if (mousePushed) {
            mousePushed = false;
            //System.out.println("Mouse is up");
            jx = mx;
            jy = my;
            paint();
        }
    }

    @FXML
    public void mouseMove(MouseEvent me) {
        if (mousePushed) {
            jx = me.getX();
            jy = me.getY();
            paint();
        }
    }

    @FXML
    public void keyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                throttleSlide.setValue(Math.min(throttleSlide.getValue() + 0.2, 1));
                model.setThrottle(throttleSlide.getValue());
                break;
            case DOWN:
                throttleSlide.setValue(Math.max(throttleSlide.getValue() - 0.2, -1));
                model.setThrottle(throttleSlide.getValue());
                break;
            case LEFT:
                rudderSlide.setValue(Math.max(rudderSlide.getValue() - 0.1, -1));
                model.setRudder(rudderSlide.getValue());
                break;
            case RIGHT:
                rudderSlide.setValue(Math.min(rudderSlide.getValue() + 0.1, 1));
                model.setRudder(rudderSlide.getValue());
                break;
            default:
                break;
        }
        //System.out.println("Throttle: " + throttleslide.getValue() + ", Rudder: " + rudderslide.getValue());
    }
}
