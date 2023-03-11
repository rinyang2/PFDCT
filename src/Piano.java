import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
public class Piano {
	Octave[] octave = new Octave[7];
	Rectangle[] extraKeys = new Rectangle[4];
	Piano(double x, double y, double width, double height){
		extraKeys[0] = new Rectangle(x,y,width,height);						//KEY 01
		extraKeys[1] = new Rectangle(x+width, y, width, height);				//KEY 03
		extraKeys[2] = new Rectangle(x+width*51, y, width, height);			//KEY 88
		extraKeys[3] = new Rectangle(x+0.8*width,y, 0.6*width, 0.7*height);	//KEY 02(black)
		for(int i = 0; i<extraKeys.length;i++) {
			if(i!=3) {
				extraKeys[i].setFill(Color.WHITE);
				extraKeys[i].setStrokeWidth(1);
				extraKeys[i].setStroke(Color.BLACK);
			}
			else {
				extraKeys[i].setFill(Color.BLACK);
				extraKeys[i].setStrokeWidth(1);
				extraKeys[i].setStroke(Color.BLACK);
			}
		}
		for(int i=0; i<octave.length ; i++) {
			octave[i] = new Octave(x+width*2+i*width*7,y,width, height);
		}
	}
}
