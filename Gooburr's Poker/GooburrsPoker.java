import arc.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GooburrsPoker{
	static Console con = new Console("Gooburr's Poker",1280,720);
	static int intDeck[][] = new int[52][3];
	static BufferedImage imgBG;
	static BufferedImage imgCards[] = new BufferedImage[5];
	public static void main(String[] args){
		
		int intTotalCount = 0;
		for(int intCount = 0; intCount <= 3; intCount++){
			for(int intCount2 = 1; intCount2 <= 13; intCount2++){
				intDeck[intTotalCount][0] = intCount2;
				intDeck[intTotalCount][1] = intCount;
				intDeck[intTotalCount][2] = (int)(Math.random()*100.0 + 1.0);
				intTotalCount++;
			}
		}
		sortDeck();
		imgBG = con.loadImage("PokerBG.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
	}
	public static void sortDeck(){
		int intTemp;
		int intBelow;
		int intCurrent;
		for(int intCount = 0; intCount < 51; intCount++){
			for(int intCount2 = 0; intCount2 < 51-intCount-1; intCount2++){
				intBelow = intDeck[intCount2+1][2];
				intCurrent = intDeck[intCount2][2];
				if(intBelow > intCurrent){
					intTemp = intBelow;
					intDeck[intCount2+1][2] = intCurrent;
					intDeck[intCount2][2] = intTemp;
					intTemp = intDeck[intCount2+1][1];
					intDeck[intCount2+1][1] = intDeck[intCount2][1];
					intDeck[intCount2][1] = intTemp;
					intTemp = intDeck[intCount2+1][0];
					intDeck[intCount2+1][0] = intDeck[intCount2][0];
					intDeck[intCount2][0] = intTemp;
				}
			}
		}
	}
}
