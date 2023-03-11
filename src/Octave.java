import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
public class Octave {
	Rectangle[] rectangle = new Rectangle[12];
	final int C = 0, Cm =1, D=2, Dm=3, E=4, F=5, Fm=6, G=7, Gm=8, A=9, Am=10, B=11;
	Octave(double x, double y, double width, double height) {
		
		double blackKeyWidth = width *0.6;
		double blackKeyHeight = height * 0.7;
		
		rectangle[C] = 	new Rectangle(x,							y,	width,			height);
		rectangle[Cm] = new Rectangle(x + blackKeyWidth,			y, 	blackKeyWidth, 	blackKeyHeight);
		rectangle[D] = 	new Rectangle(x + width, 					y, 	width, 			height);
		rectangle[Dm] = new Rectangle(x + width + 0.8*width,		y, 	blackKeyWidth, 	blackKeyHeight);
		rectangle[E] = 	new Rectangle(x + width*2, 					y, 	width, 			height);
		rectangle[F] =	new Rectangle(x + width*3, 					y, 	width, 			height);
		rectangle[Fm] = new Rectangle(x + width*3 + blackKeyWidth, 	y, 	blackKeyWidth, 	blackKeyHeight);
		rectangle[G] = 	new Rectangle(x + width*4, 					y, 	width, 			height);
		rectangle[Gm] = new Rectangle(x + width*4 + 0.7*width, 	y, 	blackKeyWidth, 	blackKeyHeight);
		rectangle[A] = 	new Rectangle(x + width*5, 					y, 	width, 			height);
		rectangle[Am] =	new Rectangle(x + width*5 + 0.8*width, 	y, 	blackKeyWidth, 	blackKeyHeight);
		rectangle[B] = 	new Rectangle(x + width*6, 					y, 	width, 			height);	
		
		for(int i = 0; i< rectangle.length;i++) {
			if(i!=Cm&&i!=Dm&&i!=Fm&&i!=Gm&&i!=Am) {
				rectangle[i].setFill(Color.WHITE);
				rectangle[i].setStrokeWidth(1);
				rectangle[i].setStroke(Color.BLACK);
			}
			else {
				rectangle[i].setFill(Color.BLACK);
				rectangle[i].setStrokeWidth(1);
				rectangle[i].setStroke(Color.BLACK);
			}
		}
	}
}
