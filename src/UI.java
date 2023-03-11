import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.input.KeyCode;

public class UI extends Application {
	private static double WIDTH = 600;
	private static double HEIGHT = 300;
	private static double INTERVAL = 8.0;
	private static double CENTER = 100;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = new Pane();

		// Draw the lines for the score
		Polyline[] rightHand = {new Polyline(), new Polyline(), new Polyline(), new Polyline(), new Polyline()};
		for(int i = 0 ; i<rightHand.length ; i++) {
			drawXAxis(pane, rightHand[i], INTERVAL*(i+2));
			pane.getChildren().add(rightHand[i]);
		}
		
		Polyline[] leftHand = {new Polyline(), new Polyline(), new Polyline(), new Polyline(), new Polyline()};
		for(int i = 0 ; i<leftHand.length ; i++) {
			drawXAxis(pane, leftHand[i], -INTERVAL*(i+2));
			pane.getChildren().add(leftHand[i]);
		}
		

		//Visualizing the piano
		Piano piano = new Piano(40.0,240.0,10.0,48.0);
		for(int i = 0; i<piano.octave.length;i++) {
			for(int j = 0; j<piano.octave[i].rectangle.length;j++) {
				if(j!=1&&j!=3&&j!=6&&j!=8&&j!=10) {
					pane.getChildren().add(piano.octave[i].rectangle[j]);
				}
			}
			for(int j = 0; j<piano.octave[i].rectangle.length;j++) {
				if(j==1||j==3||j==6||j==8||j==10) {
					pane.getChildren().add(piano.octave[i].rectangle[j]);
				}
			}//make black keys later so they are in front of white keys
		}
		//Extra 4 keys 1,2,3,88
		for(int i = 0; i<piano.extraKeys.length;i++) {
			pane.getChildren().add(piano.extraKeys[i]);
		}
		
		
		//Dividing line on y-Axis
	   Polyline yAxis = new Polyline();
	   drawYAxis(pane, yAxis);
	   pane.getChildren().add(yAxis);
	   
	   
	   //MIDI File reader
	   ReadMidi readmidi = new ReadMidi("test_chords.mid");
	   int[][] keys =readmidi.getTrack();
	   for(int i = 0;i<keys.length;i++) {
		   Arrays.sort(keys[i]);
		   //System.out.println(numbersOfNonZero(i,keys));
	   }
	   
	   
	   
	   int count = 0;
	   int mCount = 0;
	   Ellipse[] notes = new Ellipse[keys.length];
	   Text[] majorKeys = new Text[keys.length];
	   Text[] key = new Text[keys.length];
	   int[] noteNumbers = new int[keys.length];
	   //MIDI to Score
	   for(int i=0;i<keys.length;i++) {
		   for(int j = 0; j<keys[i].length;j++) {
			   if(keys[i][j]!=0) {		   
				notes[count] = new Ellipse(WIDTH/16*(2*i+1) , CENTER + note(keys[i][j]) , 5.0 , INTERVAL/2);
				noteNumbers[count] = keys[i][j];
				notes[count].setFill(Color.BLACK);
				pane.getChildren().add(notes[count]);
				if(!isMajor(keys[i][j])) {
				     majorKeys[mCount] = new Text(WIDTH/16*(2*i+1)-15.0, CENTER + 4 + note(keys[i][j]), "#");
				        pane.getChildren().add(majorKeys[mCount]);
				        mCount++;
				}
				if(keys[i][j]==60||keys[i][j]==61) {
					Line line = new Line(WIDTH/16*(2*i+1)-7.0,CENTER-INTERVAL,WIDTH/16*(2*i+1)+7.0,CENTER-INTERVAL);
					pane.getChildren().add(line);
				}
				key[count] = new Text(notes[count].getCenterX(), CENTER*2.2 -j*INTERVAL*2 , noteToString(keys[i][j]));
				pane.getChildren().add(key[count]);
				count++;
			   }
		   }
	   }

	   
	   
	   //data storage array
	   String [][] data = new String[keys.length][10];
	   for(int i=0; i<keys.length;i++) {
		   int temp = 0;
		   for(int j=0;j<keys[i].length;j++) {
			   if(keys[i][j]!=0) {
			   data[i][temp++] = keys[i][j]+"";
			   int rand = (int)((Math.random()*10000)%5);
			   data[i][4+rand]="1";
			   }
		   }
	   }
	   
	   
	   
	   Text progress = new Text(20,20,0+"%");
	   pane.getChildren().add(progress);
	   
		Text[] fingerNumbers = new Text[keys.length];
		Counter c = new Counter();
		Counter dataCounter = new Counter();
		highlight(c, piano, notes, noteNumbers);
		pane.setOnKeyPressed(e->{
			switch(e.getCode()) {
			case DIGIT1: 
				fingerNumbers[c.getCounter()-1] = new Text(notes[c.getCounter()-1].getCenterX()+10, notes[c.getCounter()-1].getCenterY(), "1");
				pane.getChildren().add(fingerNumbers[c.getCounter()-1]);
				data[dataCounter.getCounter()][5]="1";
				dataCounter.tick();
				
				//if(c.getCounter()>1) {pane.getChildren().remove(fingerNumbers[c.getCounter()-2]);}
				highlight(c, piano, notes, noteNumbers);
				break;
			case DIGIT2:
				fingerNumbers[c.getCounter()-1] = new Text(notes[c.getCounter()-1].getCenterX()+10, notes[c.getCounter()-1].getCenterY(), "2");
				pane.getChildren().add(fingerNumbers[c.getCounter()-1]);
				//if(c.getCounter()>1) {pane.getChildren().remove(fingerNumbers[c.getCounter()-2]);}
				highlight(c, piano, notes, noteNumbers);
				break;
			case DIGIT3:
				fingerNumbers[c.getCounter()-1] = new Text(notes[c.getCounter()-1].getCenterX()+10, notes[c.getCounter()-1].getCenterY(), "3");
				pane.getChildren().add(fingerNumbers[c.getCounter()-1]);
				//if(c.getCounter()>1) {pane.getChildren().remove(fingerNumbers[c.getCounter()-2]);}
				highlight(c, piano, notes, noteNumbers);
				break;
			case DIGIT4:
				fingerNumbers[c.getCounter()-1] = new Text(notes[c.getCounter()-1].getCenterX()+10, notes[c.getCounter()-1].getCenterY(), "4");
				pane.getChildren().add(fingerNumbers[c.getCounter()-1]);
				//if(c.getCounter()>1) {pane.getChildren().remove(fingerNumbers[c.getCounter()-2]);}
				highlight(c, piano, notes, noteNumbers);
				break;
			case DIGIT5:
				fingerNumbers[c.getCounter()-1] = new Text(notes[c.getCounter()-1].getCenterX()+10, notes[c.getCounter()-1].getCenterY(), "5");
				pane.getChildren().add(fingerNumbers[c.getCounter()-1]);
				//if(c.getCounter()>1) {pane.getChildren().remove(fingerNumbers[c.getCounter()-2]);}
				highlight(c, piano, notes, noteNumbers);
				break;
			default:
				break;
			}
			
		});
		   for(int i = 0; i<data.length;i++) {
			   for(int j = 0; j<data[i].length;j++) {
				 //  System.out.print(data[i][j]+" ");
			   }
			 //  System.out.println();
		   }
		
	   //click event handler
		pane.setOnMouseClicked(e-> {
			if (e.getButton() == MouseButton.PRIMARY) {
				moveNote(notes, majorKeys, key, keys);
				for(int i=0;i<fingerNumbers.length;i++) {
					pane.getChildren().remove(fingerNumbers[i]);
				}
				//progress.setText((double)c.getCounter()*16/keys.length*100+"%");
				} else if (e.getButton() == MouseButton.SECONDARY) {
				//reverseNote(notes, majorKeys, key, keys );
			}	
		});
		output(data);

		
		primaryStage.setScene(new Scene(pane, WIDTH, HEIGHT));
		primaryStage.setTitle("MIDI Fingering Data Collector");
		primaryStage.show();
		
		pane.requestFocus();
	}

	private static int numbersOfNonZero(int a, int[][] keys) {
		int temp=0;
		for(int i=0;i<keys[a].length;i++) {
			if(keys[a][i]!=0) {
				temp++;
			}
		}
		return temp;
	}
	private static int nonVoid(int a, String[][] data) {
		int temp = 0;
		for(int i=0;i<data[a].length;i++) {
			if(data[a][i]!=null) {
				temp++;
			}
		}
		return temp;
	}
	
	private static void moveNote(Ellipse[] notes, Text[] majorKeys, Text[] key, int[][]keys) {
		int ticker = 0;
		int mTicker = 0;
		for(int i=0;i<keys.length;i++) {
			for(int j=0;j<keys[i].length;j++) {
				if(keys[i][j]!=0) {
					notes[ticker].setCenterX(notes[ticker].getCenterX()-WIDTH);
					key[ticker].setX(key[ticker].getX()-WIDTH);
					if(!isMajor(keys[i][j])){
						majorKeys[mTicker].setX(majorKeys[mTicker].getX()-WIDTH);
						mTicker++;
					}
					ticker++;
				}
			}
		}
	}
	private static void reverseNote(Ellipse[] notes, Text[] majorKeys, Text[] key, int[][]keys) {
		int ticker = 0;
		int mTicker = 0;
		for(int i=0;i<keys.length;i++) {
			for(int j=0;j<keys[i].length;j++) {
				if(keys[i][j]!=0) {
					notes[ticker].setCenterX(notes[ticker].getCenterX()+WIDTH/2);
					key[ticker].setX(key[ticker].getX()+WIDTH/2);
					if(!isMajor(keys[i][j])){
						majorKeys[mTicker].setX(majorKeys[mTicker].getX()+WIDTH/2);
						mTicker++;
					}
					ticker++;
				}
			}
		}
	}
	
	private static void output(String[][] array) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter("data.csv"));
		StringBuilder sb = new StringBuilder();

		// Append strings from array
		for(int i = 0; i<array.length; i++) {
			for (String element : array[i]) {
			 sb.append(element);
			 sb.append(",");
			}
			 sb.append("\n");
		}
		br.write(sb.toString());
		br.close();
	}
	
private static void highlight(Counter c, Piano piano, Ellipse[] notes, int[] noteNumbers) {
	if(c.getCounter()>0) {
		notes[c.getCounter()-1].setFill(Color.BLACK);
		if(isMajor(noteNumbers[c.getCounter()-1])) {
		piano.octave[noteNumbers[c.getCounter()-1]/12-2].rectangle[noteNumbers[c.getCounter()-1]%12].setFill(Color.WHITE);
		}
		else {
			piano.octave[noteNumbers[c.getCounter()-1]/12-2].rectangle[noteNumbers[c.getCounter()-1]%12].setFill(Color.BLACK);
		}
	}
	notes[c.getCounter()].setFill(Color.RED);
	piano.octave[noteNumbers[c.getCounter()]/12-2].rectangle[noteNumbers[c.getCounter()]%12].setFill(Color.RED);
	c.tick();
}
	
	private static String noteToString(int key) {
		final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
		int octave = (key / 12)-1;
        int note = key % 12;
        return NOTE_NAMES[note]+octave;
	}
	
	
	private static void drawXAxis(Pane pane, Polyline xAxis, double interval) {
		ObservableList<Double> xAxisList = xAxis.getPoints();
		for (double x = 0; x < WIDTH; x++) {
			xAxisList.add(x);
			xAxisList.add(CENTER - interval);
		}
	}

	
	private static void drawYAxis(Pane pane, Polyline yAxis) {
		ObservableList<Double> xAxisList = yAxis.getPoints();
		for (double y = 52; y < 150; y++) {
			xAxisList.add(WIDTH/2);
			xAxisList.add(y);
		}
	}
	
	/*
	private static Ellipse drawNote(double x, double y) {

	 
		Ellipse e = new Ellipse(x , y , 5.0 , 4.0);
		e.setFill(Color.BLACK);
		return e;
	}
	*/
	
	
	private static boolean isMajor(int key) {
		int temp = key%12;
		return temp!=1&&temp!=3&&temp!=6&&temp!=8&&temp!=10;
	}

	

	
	private static int note(int key) {
		if(key>20&&key<60) {
			switch(key%12) {
			case 0: 	return 36+(4-(key/12))*28;
			case 1: 	return 36+(4-(key/12))*28;
			case 2: 	return 32+(4-(key/12))*28;
			case 3: 	return 32+(4-(key/12))*28;
			case 4: 	return 28+(4-(key/12))*28;
			case 5: 	return 24+(4-(key/12))*28;
			case 6: 	return 24+(4-(key/12))*28;
			case 7: 	return 20+(4-(key/12))*28;
			case 8: 	return 20+(4-(key/12))*28;
			case 9: 	return 16+(4-(key/12))*28;
			case 10:	return 16+(4-(key/12))*28;
			case 11: 	return 12+(4-(key/12))*28;
			}
		}
		else if(key>=60&&key<=108) {
			switch(key%12) {
			case 0:		return -8-((key/12)-5)*28;
			case 1:		return -8-((key/12)-5)*28;
			case 2:		return -12-((key/12)-5)*28;
			case 3:		return -12-((key/12)-5)*28;
			case 4:		return -16-((key/12)-5)*28;
			case 5:		return -20-((key/12)-5)*28;
			case 6:		return -20-((key/12)-5)*28;
			case 7:		return -24-((key/12)-5)*28;
			case 8:		return -24-((key/12)-5)*28;
			case 9:		return -28-((key/12)-5)*28;
			case 10:	return -28-((key/12)-5)*28;
			case 11:	return -32-((key/12)-5)*28;
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
