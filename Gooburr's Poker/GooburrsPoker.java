import arc.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GooburrsPoker{
	//set up static variables
	static Console con = new Console("Gooburr's Poker",1280,720);
	static int intDeck[][] = new int[52][3];
	static int intHand[][] = new int[5][2];
	static BufferedImage imgBG;
	static BufferedImage imgCards[] = new BufferedImage[5];
	static boolean blnCardsSwap[] = new boolean[5];
	static char chrInput = ' ';
	
	public static void main(String[] args){
		//set up screen
		imgBG = con.loadImage("images/menu.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
		boolean blnMenu = true;
		//menu key input
		while(chrInput != 'p'){
			chrInput = con.getChar();
			if(chrInput == 'h'){
				help();
			}
			if(chrInput == 'v'){
				viewScores();
			}
			if(chrInput == 's'){
				secret();
			}
		}
		//name input
		con.print("Enter your name: ");
		String strName = con.readLine();
		int intMoney;
		if(strName.equals("strongertogether")){
			intMoney = 1001;
		}else{
			intMoney = 1000;
		}
		con.clear();
		//show game background and the backs of the cards
		imgBG = con.loadImage("images/PokerBG.jpg");
		con.drawImage(imgBG,0,0);
		for(int intCount = 0; intCount < 5; intCount++){
			imgCards[intCount] = con.loadImage("images/cards/back.png");
			con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
		}
		con.repaint();
		//the player inputs their bet
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
		//the deck is set up, and shuffled
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
		//variables are set up for the main gameplay loop
		boolean blnPlaying = true;
		boolean blnRedraw;
		String strInput;
		//intHovering shows what the player is hovering on, 0 is nothing, 1-5 are the respective non-swap pile cards, 6-10 are the respective swap pile cards, 11 is ready button
		int intHover = 0;
	
		//set the draw color for the hover button outline
		con.setDrawColor(Color.YELLOW);
		//set images for the card states (IE pair, full house, etc)
		BufferedImage imgState;
		//load the money image and array for their positions for the straight flush animation
		BufferedImage imgMoney = con.loadImage("images/states/money.png");
		int intMoneyX[] = new int[50];
		int intMoneyY[] = new int[50];
		//main gameplay loop
		while(blnPlaying){
			blnRedraw = true;
			//hand is set up
			for(int intCount = 0; intCount < 5; intCount++){
				imgCards[intCount] = con.loadImage("images/cards/"+intHand[intCount][0]+"_"+intHand[intCount][1]+".png");
				blnCardsSwap[intCount] = false;
			}
			//loop is repeated while the player is not clicking the ready button
			while(!clickButton(420,570,480,110)){
				//check if the player is clicking a card to swap it, and hovering a card/ready button to draw the outline
				if(hoverButton(420,570,480,110) && intHover == 0){
					intHover = 11;
					blnRedraw = true;
				}
				for(int intCount = 0; intCount < 5; intCount++){
					if(clickButton((intCount+1)*120+250,420,71,96) && !blnCardsSwap[intCount]){
						blnCardsSwap[intCount] = !blnCardsSwap[intCount];
						blnRedraw = true;
					}else if(clickButton((intCount+1)*120+250,240,71,96) && blnCardsSwap[intCount]){
						blnCardsSwap[intCount] = !blnCardsSwap[intCount];
						blnRedraw = true;
					}
					if(hoverButton((intCount+1)*120+250,420,71,96) && !blnCardsSwap[intCount] && intHover == 0){
						intHover = intCount+1;
						blnRedraw = true;
					}else if(hoverButton((intCount+1)*120+250,240,71,96) && blnCardsSwap[intCount] && intHover == 0){
						intHover = intCount+6;
						blnRedraw = true;
					}
				}
				//Imma just be real w/ u there's prolly an easier way to do this whole check but I'm too lazy to figure it out and this works ¯\_(ツ)_/¯
				if(!hoverButton((1)*120+250,240,71,96) &&  !hoverButton((2)*120+250,240,71,96) && !hoverButton((3)*120+250,240,71,96) && !hoverButton((4)*120+250,240,71,96) && !hoverButton((5)*120+250,240,71,96) && !hoverButton((1)*120+250,420,71,96) && !hoverButton((2)*120+250,420,71,96) && !hoverButton((3)*120+250,420,71,96) && !hoverButton((4)*120+250,420,71,96) && !hoverButton((5)*120+250,420,71,96) && !hoverButton(420,570,480,110) && intHover != 0){
					intHover = 0;
					blnRedraw = true;
				}
				//redraw the card to put it in the swap pile
				if(blnRedraw){
					con.drawImage(imgBG,0,0);
					for(int intCount = 0; intCount < 5; intCount++){
						if(!blnCardsSwap[intCount]){
							con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
						}else{
							con.drawImage(imgCards[intCount],(intCount+1)*120+250,240);
						}
						if(hoverButton((intCount+1)*120+250,420,71,96) && !blnCardsSwap[intCount]){
							con.drawRect((intCount+1)*120+249,419,73,98);
						}else if(hoverButton((intCount+1)*120+250,240,73,98) && blnCardsSwap[intCount]){
							con.drawRect((intCount+1)*120+249,239,73,98);
						}
					}
					if(hoverButton(420,570,480,110)){
						con.drawRect(420,569,475,112);
					}
					con.repaint();
					blnRedraw = false;
				}
			}
			//draw the new hand with the swapped cards
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
			//after 2 seconds, sort the hand and draw the sorted hand
			con.sleep(2000);
			sortHand();
			con.drawImage(imgBG,0,0);
			for(int intCount = 0; intCount < 5; intCount++){
				imgCards[intCount] = con.loadImage("images/cards/"+intHand[intCount][0]+"_"+intHand[intCount][1]+".png");
				con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
			}
			con.repaint();
			//check which state the deck is in, and give the appropriate amount of money to the player
			System.out.println("pair: "+pair()+" two pairs: "+twoPairs()+" 3OAK: "+threeOAK()+" straight: "+straight()+" flush: "+flush()+" full house: "+fullHouse()+" 4OAK: "+fourOAK()+" straight flush: "+straightFlush());
			con.clear();
			con.sleep(3000);
			if(straightFlush()){
				con.println("straight flush!!");
				intBet *= 50;
				intMoney += intBet;
				imgState = con.loadImage("images/states/straight flush.png");
				for(int intCount = 0; intCount <= 49; intCount++){
					intMoneyX[intCount] = (int)(Math.random()*1180);
					intMoneyY[intCount] = (int)(Math.random()*(-72)-5)*10;
				}
				for(int intCount = 1; intCount <= 400; intCount++){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,(int)(Math.random()*40-20),(int)(Math.random()*40-20));
					for(int intCount2 = 1; intCount2 <= 49; intCount2++){
						intMoneyY[intCount2] += 10;
						if(intMoneyY[intCount2] >= 720){
							intMoneyX[intCount2] = (int)(Math.random()*1180);
							intMoneyY[intCount2] = -50;
						}
						con.drawImage(imgMoney,intMoneyX[intCount2],intMoneyY[intCount2]);
					}
					con.repaint();
					con.sleep(33);
				}
			}else if(fourOAK()){
				intBet *= 25;
				intMoney += intBet;
				imgState = con.loadImage("images/states/four.png");
				for(int intCount = 1; intCount <= 200; intCount++){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,(int)(Math.random()*40+120),(int)(Math.random()*40-20));
					con.repaint();
					con.sleep(33);
				}
			}else if(fullHouse()){
				intBet *= 9;
				intMoney += intBet;
				imgState = con.loadImage("images/states/full house.png");
				for(int intCount = 1; intCount <= 200; intCount++){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,(int)(Math.random()*20+180),0);
					con.repaint();
					con.sleep(33);
				}
			}else if(flush()){
				intBet *= 6;
				intMoney += intBet;
				imgState = con.loadImage("images/states/flush.png");
				for(int intCount = 150; intCount >= 0; intCount--){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,(int)(5*intCount*Math.sin((Math.PI*intCount)/50)) + 190,(int)(5*intCount*Math.cos((Math.PI*intCount)/50)) + 60);
					con.repaint();
					con.sleep(33);
				}
				con.sleep(2000);
			}else if(straight()){
				intBet *= 4;
				intMoney += intBet;
				imgState = con.loadImage("images/states/straight.png");
				for(int intCount = -800; intCount <= 240; intCount += 10){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,intCount,60);
					con.repaint();
					con.sleep(33);
				}
				con.sleep(1000);
				for(int intCount = 240; intCount <= 1280; intCount += 10){
					con.drawImage(imgBG,0,0);
					con.drawImage(imgState,intCount,60);
					con.repaint();
					con.sleep(33);
				}
			}else if(threeOAK()){
				intBet *= 3;
				intMoney += intBet;
				imgState = con.loadImage("images/states/three.png");
				con.drawImage(imgState,240,110);
				con.repaint();
				con.sleep(5000);
			}else if(twoPairs()){
				intBet *= 2;
				intMoney += intBet;
				imgState = con.loadImage("images/states/two pair.png");
				con.drawImage(imgState,290,70);
				con.repaint();
				con.sleep(5000);
			}else if(pair()){
				intMoney += intBet;
				imgState = con.loadImage("images/states/pair.png");
				con.drawImage(imgState,290,160);
				con.repaint();
				con.sleep(5000);
			}else{
				imgState = con.loadImage("images/states/nothin.png");
				con.drawImage(imgState,290,210);
				con.repaint();
				con.sleep(5000);
			}
			con.clear();
			con.drawImage(imgBG,0,0);
			con.repaint();
			//check if the player wishes to play again
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
			//run if the player wishes to play again
			if(strInput.equalsIgnoreCase("play")){
				//shuffle the deck
				for(int intCount = 0; intCount < 52; intCount++){
					intDeck[intCount][2] = (int)(Math.random()*100.0 + 1.0);
				}
				sortDeck();
				//ask for the player's bet
				intBet = 99999999;
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
				con.drawImage(imgBG,0,0);
				//set up and draw the player's hand
				for(int intCount = 0; intCount < 5; intCount++){
					imgCards[intCount] = con.loadImage("images/cards/back.png");
					con.drawImage(imgCards[intCount],(intCount+1)*120+250,420);
				}
				//since we are in a while loop, and blnPlaying is true, the loop will restart
			//if the player doesn't wish to play again
			}else{
				//set up variables related to saving the player's score
				TextInputFile txtScoresInput = new TextInputFile("scores.txt");
				TextOutputFile txtScoresOutput = new TextOutputFile("scores.txt");
				String strNames[] = new String[25];
				int intScores[] = new int[25];
				intTotalCount = 0;
				int intScoresCount;
				//set the strNames and intScores array to the scores listed in scores.txt
				while(!txtScoresInput.eof() && intTotalCount < 25){
					strNames[intTotalCount] = txtScoresInput.readLine();
					intScores[intTotalCount] = txtScoresInput.readInt();
					intTotalCount++;
				}
				intScoresCount = intTotalCount;
				System.out.println("scores count: "+intScoresCount);
				//if there's no scores, just print out the player's current score
				if(intScoresCount == 0){
					txtScoresOutput.println(strName);
					txtScoresOutput.println(intMoney);
				//else, if the players score is bigger than the last score, insert it in where it belongs, scores are ranked from highest score to lowest
				}else if(intMoney > intScores[intScoresCount]){
					strNames[intScoresCount] = strName;
					intScores[intScoresCount] = intMoney;
					int intTemp;
					String strTemp;
					while(intMoney > intScores[intTotalCount-1] && intTotalCount > 1){
						intTemp = intScores[intTotalCount];
						intScores[intTotalCount] = intScores[intTotalCount-1];
						intScores[intTotalCount-1] = intTemp;
						strTemp = strNames[intTotalCount];
						strNames[intTotalCount] = strNames[intTotalCount-1];
						strNames[intTotalCount-1] = strTemp;
						intTotalCount--;
					}
					for(int intCount = 0; intCount <= intScoresCount; intCount++){
						txtScoresOutput.println(strNames[intCount]);
						txtScoresOutput.println(intScores[intCount]);
						System.out.println(strNames[intCount]);
						System.out.println(intScores[intCount]);
					}
				}
				//exit the while loop
				blnPlaying = false;
				con.println("your score has been saved!");
			}
		}
	}
	//sort the deck based on the random int, essentially shuffling it
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
	//sort the hand so it is easier to categorize
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
	//check if the user is clicking or hovering over a rectangular area on the screen
	public static boolean clickButton(int intPosX, int intPosY, int intWidth, int intHeight){
		return con.currentMouseButton() == 1 && con.currentMouseX() >= intPosX && con.currentMouseX() <= intPosX + intWidth && con.currentMouseY() >= intPosY && con.currentMouseY() <= intPosY + intHeight;
	}
	public static boolean hoverButton(int intPosX, int intPosY, int intWidth, int intHeight){
		return con.currentMouseX() >= intPosX && con.currentMouseX() <= intPosX + intWidth && con.currentMouseY() >= intPosY && con.currentMouseY() <= intPosY + intHeight;
	}
	//methods that show the help and scores screens respectively
	public static void help(){
		System.out.println("help");
		imgBG = con.loadImage("images/help1.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
		boolean blnNextHelp = false;
		while(chrInput != 'b'){
			chrInput = con.getChar();
			if(chrInput == 'n' && !blnNextHelp){
				imgBG = con.loadImage("images/help2.jpg");
				con.drawImage(imgBG,0,0);
				con.repaint();
				blnNextHelp = true;
			}
		}
		imgBG = con.loadImage("images/menu.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
	}
	public static void viewScores(){
		System.out.println("view scores");
		imgBG = con.loadImage("images/highScoreBG.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
		TextInputFile txtScoresInput = new TextInputFile("scores.txt");
		int intScoreCount = 1;
		if(txtScoresInput.eof()){
			con.println("There's no scores right now!");
		}else{
			while(!txtScoresInput.eof()){
				con.println(intScoreCount+". "+txtScoresInput.readLine()+" - $"+txtScoresInput.readLine());
				intScoreCount++;
			}
		}
		//this is just to stall it so that it runs the rest when you press 'b'
		while(chrInput != 'b'){		
			chrInput = con.getChar();
		}
		imgBG = con.loadImage("images/menu.jpg");
		con.drawImage(imgBG,0,0);
		con.repaint();
		con.clear();
	}
	//secret joke
	public static void secret(){
		System.out.println("secret");
		int intRandom = (int)(Math.random()*3+1);
		BufferedImage imgDargon = con.loadImage("images/dargon"+intRandom+".png");
		BufferedImage imgText = con.loadImage("images/text.gif");
		if(intRandom == 1){
			//dargon extrem sprots
			for(int intCount = 1280; intCount >= -600; intCount -= 12){
				con.drawImage(imgBG,0,0);
				con.drawImage(imgText,intCount,500);
				con.drawImage(imgDargon,intCount+86,150);
				con.repaint();
				con.sleep(33);
			}
		}else if(intRandom == 2){
			//dargon flies up
			for(int intCount = 720; intCount >= -300; intCount -= 20){
				con.drawImage(imgBG,0,0);
				con.drawImage(imgDargon,390,intCount);
				con.repaint();
				con.sleep(33);
			}
		}else{
			//dargon big face goes in then out
			for(int intCount = 1280; intCount >= 0; intCount -= 40){
				con.drawImage(imgBG,0,0);
				con.drawImage(imgDargon,intCount,0);
				con.repaint();
				con.sleep(33);
			}
			con.sleep(2000);
			for(int intCount = 0; intCount <= 1280; intCount += 40){
				con.drawImage(imgBG,0,0);
				con.drawImage(imgDargon,intCount,0);
				con.repaint();
				con.sleep(33);
			}
		}
	}
	//Different hand type checks
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
