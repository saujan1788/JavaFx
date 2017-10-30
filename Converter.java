/**
 * This application allows the user to convert from imperial units to metric units
 * Conversion factors are stored in a LinkedHashMap
 * There is error checking to ensure that suitable values are entered. 
 */

// imports
package javabyexample;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;


// class definition
public class Converter extends Application {

	// init method
	public void init() {

		// Conversion Type - ComboBox
		conversion_type_combobox = new ComboBox<String>();
		conversion_type_combobox.getItems().addAll("Length", "Mass");
		conversion_type_combobox.setValue("Length");
		
		
		//Initialize Buttons
		convert_button = new Button("Convert");
		reset_button = new Button("Reset");
		
//Initialize all labels
		conversion_type_label = new Label("Conversion Type");
		imperial_label = new Label("Imperial");
		metric_label = new Label("Metric");
		accuracy_label = new Label("Accuracy");
		
		//Initaial text field
		metric_textField = new TextField();
		accuracy_textField = new TextField();
		imperial_textField = new TextField();
		
		
		// Prevent the Accuracy and Metric TextFields being editable  
		accuracy_textField.setEditable(false);
		metric_textField.setEditable(false);
		
		
		//initialize slider
		accuracy_slider = new Slider();
		
		//init toogle
		metric_toggleGroup =  new ToggleGroup();
		imperial_toggleGroup =  new ToggleGroup();
		
		
		//init gridpane
		gp = new GridPane();
		
		gp.setHgap(5);
		gp.setVgap(5);
		
		//init radio butt
		imperial_radioButton_1 = new RadioButton();
		imperial_radioButton_2 = new RadioButton();
		imperial_radioButton_3 = new RadioButton();
		
		metric_radioButton_1 = new RadioButton();
		metric_radioButton_2 = new RadioButton();
		metric_radioButton_3 = new RadioButton();
	
		// Set radio buttons to toggle groups
		
		//metric radio buttons
				metric_radioButton_1.setToggleGroup(metric_toggleGroup);
				metric_radioButton_2.setToggleGroup(metric_toggleGroup);
				metric_radioButton_3.setToggleGroup(metric_toggleGroup);
				//imperial radio buttons
				imperial_radioButton_1.setToggleGroup(imperial_toggleGroup);
				imperial_radioButton_2.setToggleGroup(imperial_toggleGroup);
				imperial_radioButton_3.setToggleGroup(imperial_toggleGroup);
	
		// Layout controls as per the diagram, feel free to improve the UI. 
		// How many rows and columns do you want - work this out on paper first 
		// My version has 5 rows and 8 columns you can look at the JavaFX API to 
		// see how to get controls to span more than one column
		gp.add(conversion_type_label, 0, 0,1,1);
		gp.add(conversion_type_combobox, 1, 0,3,1);
		
		//second row
		gp.add(imperial_label, 0,1,1,1);
		gp.add(imperial_textField,1,1,3,1);
		gp.add(metric_label,4,1,1,1);
		gp.add(metric_textField,5,1,3,1);
		
		
		//third row: Add radio buttons

		gp.add(imperial_radioButton_1,1,2,1,1);
		gp.add(imperial_radioButton_2,2,2,1,1);
		gp.add(imperial_radioButton_3,3,2,1,1);
		
		gp.add(metric_radioButton_1,5,2,1,1);
		gp.add(metric_radioButton_2,6,2,1,1);
		gp.add(metric_radioButton_3,7,2,1,1);
		
	
		gp.add(accuracy_label,0,3,1,1);
		gp.add(accuracy_slider,1,3,3,1);
		gp.add(accuracy_textField,5,3,3,1);
		
		gp.addRow(4,convert_button,reset_button);
		
		// Method call (not declaration!)  to initialize the controls based on the conversion type.
		initalizeControlValues();
		
		// Method call to perform conversion
		convert();

		//  Listener for accuracy_slider to set the number of decimal places in the metric_textField 
		accuracy_slider.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue){
				// Method call to perform conversion
				convert();
				// Put the newValue in the metric_textField 
				accuracy_textField.setText(String.format("%.0f",newValue));
				// Format the value in the metric_textField so that it has the correct number of decimal places
			}
		});

		// Listener for imperial_toggleGroup to perform conversion 
		this.imperial_toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle,Toggle new_toggle) {
				convert();
			}
		});
		
		// Listener for metric_toggleGroup to perform conversion 
		this.metric_toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle,Toggle new_toggle) {
				convert();
			}
		});

		// Listener for convert_button to perform conversion  
		convert_button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				convert();
			}
		});

		// Listener for conversion_type_combobox to initialize control values and perform conversion
		conversion_type_combobox.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				initalizeControlValues();
			}
		});

		// Listener for reset_button to initialize control values and perform conversion
		reset_button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				initalizeControlValues();
				//convert
				convert();
			}
		});
	}

	// Overridden start method
	public void start(Stage primaryStage) {
		// set a title on the window, set a scene, size, and show the window
		primaryStage.setTitle("Unit Converter");
	
		primaryStage.setScene(new Scene(gp,600,400));
		primaryStage.show();
		gp.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                
                "-fx-border-color: grey;");
	
		
	
	}

	// Overridden stop method - add a status message to this 
	public void stop() {
		System.out.println("Program Stopped");
	}

	// Entry point to our program
	public static void main(String[] args) {
		launch(args);
	}

    // Method to harvest value from imperial_textField, perform calculation and display the results
	// in the metric_textField
	 
	private void convert(){
		Double value_imperial;
		// try/catch to test to see if the value in the imperial_textField is a number before attempting to convert 
		try{
			value_imperial = Double.parseDouble(imperial_textField.getText());
			// Get the select keys from the RadioButtons
			String key_imperial = ((RadioButton)this.imperial_toggleGroup.getSelectedToggle()).getText();
			String key_metric = ((RadioButton)this.metric_toggleGroup.getSelectedToggle()).getText();
			
			// Use these keys to get the matching values from the LinkedHashMap
			Double converstion_factor_metric = this.conversion_map.get(key_metric);
			Double converstion_factor_imperial = this.conversion_map.get(key_imperial);
			
			// Calculate the value_metric
			Double value_metric = value_imperial * converstion_factor_metric* converstion_factor_imperial;
			
			// Put this new value_metric in the metric_textField
			metric_textField.setText(Double.toString(value_metric));
		} catch (NumberFormatException e) {
			// Output a suitable error message in the imperial_textField and set the metric_textField to blank
			imperial_textField.setText("Enter a number");
			metric_textField.setText("");
			return;
		}
		
		
	}

	// Method to initialize the controls based on the selection of the conversion type 
	private void initalizeControlValues(){
		// Make an Object array to store all the keys of the LinkedHashMap
		Object[] keys = this.conversion_map.keySet().toArray();

		// Initialization for the controls if the conversion_type_combobox is set to Length
		if(conversion_type_combobox.getValue()=="Length"){

			//Initialize length to 1.0
			imperial_textField.setText("1");
			
			// Set slider scale 0 to 6, set slider value to 4 and ticks to 1 unit intervals
			accuracy_slider.setMin(0);
			accuracy_slider.setMax(6);
			accuracy_slider.setValue(4);
			accuracy_slider.setMajorTickUnit(1);
			
			// Initialize the radio buttons
			imperial_radioButton_1.setText((String) keys[0]);
			imperial_radioButton_2.setText((String) keys[1]);
			imperial_radioButton_3.setText((String) keys[2]);
			metric_radioButton_1.setText((String) keys[3]);
			metric_radioButton_2.setText((String) keys[4]);
			metric_radioButton_3.setText((String) keys[5]);
			// Set default selections for the radio buttons
			imperial_radioButton_1.setSelected(true);
			metric_radioButton_1.setSelected(true);
		}
		// Initialization for the controls if the conversion_type_combobox is set to Mass
		else if(conversion_type_combobox.getValue()=="Mass"){

			// Initialize the mass to .5
			imperial_textField.setText("0.5");
			

			// Set slider scale 0 to 5, set slider value to 3 and ticks to 1 unit intervals
			accuracy_slider.setMin(0);
			accuracy_slider.setMax(5);
			accuracy_slider.setValue(3);
			accuracy_slider.setMajorTickUnit(1);
			
			// Initialize the radio buttons
			imperial_radioButton_1.setText((String) keys[6]);
			imperial_radioButton_2.setText((String) keys[7]);
			imperial_radioButton_3.setText((String) keys[8]);
			metric_radioButton_1.setText((String) keys[9]);
			metric_radioButton_2.setText((String) keys[10]);
			metric_radioButton_3.setText((String) keys[11]);
			
			metric_radioButton_2.setSelected(true);
			imperial_radioButton_2.setSelected(true);

			// Set default selections for the radio buttons
			

		}

		// Slider display ticks etc
		accuracy_slider.setBlockIncrement(1);
		accuracy_slider.setShowTickMarks(true);
		accuracy_slider.setShowTickLabels(true);
		accuracy_slider.setMajorTickUnit(1);
		
	}


	// Variable declaration. Although it is not correct to do so you may choose to initalize 
	// variable here also to avoid a very large init() method

	// Layout
	private GridPane gp;

	// Conversion Type
	private Label conversion_type_label;
	private ComboBox<String> conversion_type_combobox;

	// Conversion Map - facilitates the conversion from imperial to metric. 
	// This LinkedHashMap initialization is complete
	private Map<String,Double> conversion_map = new LinkedHashMap<String, Double>();
	{ 
		conversion_map.put("in",0.0254); // key:in value:the factor required to convert inches to metres
		conversion_map.put("foot",0.3048);
		conversion_map.put("yard",0.9144);
		conversion_map.put("mm",1000.0); // key:mm value:the factor required to then convert metres to mm
		conversion_map.put("cm",100.0);
		conversion_map.put("m",1.0);
		conversion_map.put("oz",0.0283);
		conversion_map.put("lb",0.453);
		conversion_map.put("stone",6.35);
		conversion_map.put("g",1000.0);
		conversion_map.put("kg",1.0);
		conversion_map.put("ton",0.001);
	}

	// Imperial
	private Label imperial_label;
	private TextField imperial_textField;
	private ToggleGroup imperial_toggleGroup = new ToggleGroup();
	private RadioButton imperial_radioButton_1= new RadioButton("oz");
	private RadioButton imperial_radioButton_2= new RadioButton("lb");
	private RadioButton imperial_radioButton_3= new RadioButton("stone");
	// Metric
			private Label metric_label;
	private TextField metric_textField;
	private ToggleGroup metric_toggleGroup;
	private RadioButton metric_radioButton_1;
	private RadioButton metric_radioButton_2;
	private RadioButton metric_radioButton_3;

	//Accuracy
	private Label accuracy_label;
	private Slider accuracy_slider;
	private TextField accuracy_textField;
	private int accuracy;
	
	//Calculate and Erase
	private Button convert_button;
	private Button reset_button;
}