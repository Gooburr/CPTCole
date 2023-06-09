import arc.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GooburrsPoker{
	static Console con = new Console("Gooburr's Poker",1280,720);
	static int intDeck[][] = new int[52][3];
	static int intHand[][] = new int[5][2];
	static BufferedImage imgBG;
	static BufferedImage imgCards[] = new BufferedImage[5];
	static boolean blnCardsSwap[] = new boolean[5];
	
	public static void main(String[] args){
		imgBG = con.loadImage("images/menu.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
		while(con.getChar() != 'p'){
			if(con.getChar() == 'h'){
				help();
			}else if(con.getChar() == 'v'){
				viewScores();
			}
		}
		con.print("Enter your name: ");
		String strName = con.readLine();
		int intMoney;
		if(strName.equals("strongertogether")){
			intMoney = 1001;
		}else{
			intMoney = 1000;
		}
		con.clear();
		imgBG = con.loadImage("images/PokerBG.jpg");
		con.drawImage(imgBG,0,0);
		for(int intCount = 0; intCount < 5; intCount++){
			imgCards[intCount] = con.loadImage("images/cards/back.png");
			con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
		}
		con.repaint();
		int intBet = 99999999;
		while(intBet > intMoney || intBet <= 0){
			con.println("Name: "+strName);
			con.println("Money: $"+intMoney);
			con.print("Input your bet: $");
			intBet = con.readInt();
			con.clear();
			if(intBet > intMoney){
				con.println("HEY! You can't bet more money than you acually have!");
			}
			if(intBet < 0){
				con.println("HEY! You can't bet a negative amount of money!");
			}
			if(intBet == 0){
				con.println("HEY! You can't bet nothing!");
			}
		}
		intMoney -= intBet;
		con.println("Name: "+strName);
		con.println("Money: $"+intMoney);
		con.println("Bet: $"+intBet);
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
		boolean blnPlaying = true;
		boolean blnRedraw;
		String strInput;
		while(blnPlaying){
			blnRedraw = true;
			//THE NEXT 10 LINES ARE FOR TESTING
			/*
			intHand[0][0] = 2;
			intHand[0][1] = 0;
			intHand[1][0] = 2;
			intHand[1][1] = 3;
			intHand[2][0] = 2;
			intHand[2][1] = 1;
			intHand[3][0] = 2;
			intHand[3][1] = 2;
			intHand[4][0] = 4;
			intHand[4][1] = 1;
			*/ 
			for(int intCount = 0; intCount < 5; intCount++){
				intHand[intCount][0] = intDeck[intCount][0];
				intHand[intCount][1] = intDeck[intCount][1];
				imgCards[intCount] = con.loadImage("images/cards/"+intHand[intCount][0]+"_"+intHand[intCount][1]+".png");
				blnCardsSwap[intCount] = false;
			}
			while(!clickButton(420,570,480,110)){
				for(int intCount = 0; intCount < 5; intCount++){
					if(clickButton((intCount+1)*120+250,420,71,96) && !blnCardsSwap[intCount]){
						blnCardsSwap[intCount] = !blnCardsSwap[intCount];
						blnRedraw = true;
					}else if(clickButton((intCount+1)*120+250,240,71,96) && blnCardsSwap[intCount]){
						blnCardsSwap[intCount] = !blnCardsSwap[intCount];
						blnRedraw = true;
					}
				}
				if(blnRedraw){
					con.drawImage(imgBG,0,0);
					for(int intCount = 0; intCount < 5; intCount++){
						if(!blnCardsSwap[intCount]){
							con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
						}else{
							con.drawImage(imgCards[intCount],(intCount+1)*120+250,240);
						}
					}
					con.repaint();
					blnRedraw = false;
				}
			}
			intTotalCount = 5;
			con.drawImage(imgBG,0,0);
			for(int intCount = 0; intCount < 5; intCount++){
				if(blnCardsSwap[intCount]){
					intHand[intCount][0] = intDeck[intTotalCount][0];
					intHand[intCount][1] = intDeck[intTotalCount][1];
					imgCards[intCount] = con.loadImage("images/cards/"+intHand[intCount][0]+"_"+intHand[intCount][1]+".png");
					intTotalCount++;
				}
				con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
			}
			con.repaint();
			con.sleep(2000);
			sortHand();
			con.drawImage(imgBG,0,0);
			for(int intCount = 0; intCount < 5; intCount++){
				imgCards[intCount] = con.loadImage("images/cards/"+intHand[intCount][0]+"_"+intHand[intCount][1]+".png");
				con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
			}
			con.repaint();
			System.out.println("pair: "+pair()+" two pairs: "+twoPairs()+" 3OAK: "+threeOAK()+" straight: "+straight()+" flush: "+flush()+" full house: "+fullHouse()+" 4OAK: "+fourOAK()+" straight flush: "+straightFlush());
			con.clear();
			if(straightFlush()){
				con.println("straight flush!!");
				intBet *= 50;
				intMoney += intBet;
			}else if(fourOAK()){
				con.println("four of a kind!");
				intBet *= 25;
				intMoney += intBet;
			}else if(fullHouse()){
				con.println("full house!");
				intBet *= 9;
				intMoney += intBet;
			}else if(flush()){
				con.println("flush!");
				intBet *= 6;
				intMoney += intBet;
			}else if(straight()){
				con.println("straight!");
				intBet *= 4;
				intMoney += intBet;
			}else if(threeOAK()){
				con.println("three of a kind!");
				intBet *= 3;
				intMoney += intBet;
			}else if(twoPairs()){
				con.println("two pairs!");
				intBet *= 2;
				intMoney += intBet;
			}else if(pair()){
				con.println("pair!");
				intMoney += intBet;
			}else{
				con.println("nothing...");
			}
			con.sleep(2000);
			con.clear();
			if(intMoney != 0){
				con.println("Name: "+strName);
				con.println("Money: $"+intMoney);
				con.print("Type \"play\" to play again, type anything else to quit and save your score: ");
				strInput = con.readLine();
				con.clear();
			}else{
				con.println("You're broke! you can't play anymore!");
				strInput = "not play";
			}
			if(strInput.equalsIgnoreCase("play")){
				for(int intCount = 0; intCount < 52; intCount++){
					intDeck[intCount][2] = (int)(Math.random()*100.0 + 1.0);
				}
				sortDeck();
				intBet = 99999999;
				while(intBet > intMoney){
					con.println("Name: "+strName);
					con.println("Money: $"+intMoney);
					con.print("Input your bet: $");
					intBet = con.readInt();
					con.clear();
					if(intBet > intMoney){
						con.println("HEY! You can't bet more money than you acually have!");
					}
				}
				intMoney -= intBet;
				con.println("Name: "+strName);
				con.println("Money: $"+intMoney);
				con.println("Bet: $"+intBet);
				con.drawImage(imgBG,0,0);
				for(int intCount = 0; intCount < 5; intCount++){
					imgCards[intCount] = con.loadImage("images/cards/back.png");
					con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
				}
			}else{
				blnPlaying = false;
			}
		}
	}
	public static void sortDeck(){
		int intTemp;
		int intBelow;
		int intCurrent;
		for(int intCount = 0; intCount < 52; intCount++){
			for(int intCount2 = 0; intCount2 < 52-intCount-1; intCount2++){
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
	public static void sortHand(){
		int intTemp;
		int intBelow;
		int intCurrent;
		for(int intCount = 0; intCount < 5; intCount++){
			for(int intCount2 = 0; intCount2 < 5-intCount-1; intCount2++){
				intBelow = intHand[intCount2+1][0];
				intCurrent = intHand[intCount2][0];
				if(intBelow > intCurrent){
					intTemp = intBelow;
					intHand[intCount2+1][0] = intHand[intCount2][0];
					intHand[intCount2][0] = intTemp;
					intTemp = intHand[intCount2+1][1];
					intHand[intCount2+1][1] = intHand[intCount2][1];
					intHand[intCount2][1] = intTemp;
					
				}
			}
		}
		
	}
	public static boolean clickButton(int intPosX, int intPosY, int intWidth, int intHeight){
		return con.currentMouseButton() == 1 && con.currentMouseX() >= intPosX && con.currentMouseX() <= intPosX + intWidth && con.currentMouseY() >= intPosY && con.currentMouseY() <= intPosY + intHeight;
	}
	public static void help(){
		
	}
	public static void viewScores(){
		
	}
	//Different hand types
	public static boolean pair(){
		for(int intCount = 0; intCount < 4; intCount++){
			if(intHand[intCount][0] == intHand[intCount+1][0] && intHand[intCount][0] > 10){
				return true;
			}
		}
		return false;
	}
	public static boolean twoPairs(){
		int intPairs = 0;
		for(int intCount = 0; intCount < 4; intCount++){
			if(intHand[intCount][0] == intHand[intCount+1][0]){
				intPairs++;
				//intCount++ is to make sure it doesn't count 3 of a kinds
				intCount++;
			}
		}
		return intPairs == 2;
	}
	public static boolean threeOAK(){
		for(int intCount = 0; intCount < 3; intCount++){
			if(intHand[intCount][0] == intHand[intCount+1][0] && intHand[intCount][0] == intHand[intCount+2][0]){
				return true;
			}
		}
		return false;
	}
	public static boolean straight(){
		return intHand[0][0] == intHand[1][0]+1 && intHand[0][0] == intHand[2][0]+2 && intHand[0][0] == intHand[3][0]+3 && intHand[0][0] == intHand[4][0]+4;
	}
	public static boolean flush(){
		return intHand[0][1] == intHand[1][1] && intHand[0][1] == intHand[2][1] && intHand[0][1] == intHand[3][1] && intHand[0][1] == intHand[4][1];
	}
	public static boolean fullHouse(){
		return threeOAK() && twoPairs();
	}
	public static boolean fourOAK(){
		for(int intCount = 0; intCount < 2; intCount++){
			if(intHand[intCount][0] == intHand[intCount+1][0] && intHand[intCount][0] == intHand[intCount+2][0] && intHand[intCount][0] == intHand[intCount+3][0]){
				return true;
			}
		}
		return false;
	}
	public static boolean straightFlush(){
		return straight() && flush();
	}
}
