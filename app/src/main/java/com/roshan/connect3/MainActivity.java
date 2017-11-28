package com.roshan.connect3;

import android.graphics.Color;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    int activePlayer = 0; //0 for cross, 1 for circle
    boolean gameIsActive = true;

    int crossScore,circleScore=0;

    int[] gameState = {2,2,2,2,2,2,2,2,2}; // 2 denotes unplayed slot in the board
    int [][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};


    public void dropIn (View view) {

        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            counter.setTranslationY(-1000f);
            gameState[tappedCounter] = activePlayer;

            if (activePlayer == 0) {

                //reset cross image to black
                ImageView cross = (ImageView) findViewById(R.id.cross);
                cross.setImageResource(R.drawable.cross);

                //change turn to circle
                ImageView circle = (ImageView) findViewById(R.id.circle);
                circle.setImageResource(R.drawable.redcircle);

                counter.setImageResource(R.drawable.cross);
                activePlayer = 1;

            } else {

                //reset circle image to black
                ImageView circle = (ImageView) findViewById(R.id.circle);
                circle.setImageResource(R.drawable.naughts);

                //change turn to cross
                ImageView cross = (ImageView) findViewById(R.id.cross);
                cross.setImageResource(R.drawable.redcross);

                counter.setImageResource(R.drawable.naughts);
                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).setDuration(300);

            //loop through the elements inside winningPositions and during each instance of loop,
            //the array winningState is equal to the array in winningPositions at that instance
            //eg. during 2nd loop instance, winningState = {6,7,8} = winningPositions[2][]
            //whenever a move is made, this checks if a winning condition has been met
            for(int [] winningState : winningPositions){

                //check that whether the gameState's(i.e. which player is active on the denoted position) of counter in positions denoted by
                //the elements in array winningState are equal and not equal to 2 (i.e. selected)
                // eg. when winningState = {6,7,8}, check if gameState[6]==gameState[7]==gameState[8]!=2
                if(gameState[winningState[0]] == gameState[winningState[1]] &&
                        gameState[winningState[1]] == gameState[winningState[2]] &&
                        gameState[winningState[0]] != 2){

                    //System.out.println(gameState[winningState[0]]);//prints 0 or 1 to console because if the condition is true, the checked gameState's are either 1 or 2



                    //to display who has won
                    LinearLayout wonLayout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    String winner = "Naughts";


                    if(gameState[winningState[0]] ==0){

                        //circle wins
                        winner = "Cross";

                        if(gameIsActive) {
                            //update score
                            circleScore++;
                            TextView circleScoreText = (TextView) findViewById(R.id.circleWinNumber);
                            circleScoreText.setText(circleScore + " :");
                        }

                    }else{

                        if(gameIsActive) {
                            //cross wins
                            crossScore++;

                            //update score
                            TextView crossScoreText = (TextView) findViewById(R.id.crossWinNumber);
                            crossScoreText.setText(": " + crossScore);
                        }

                    }

                    gameIsActive = false; //so that no move can be made after someone wins

                    TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                    winnerMessage.setText(winner + " wins!");


                    winner = winner.toLowerCase();

                    ImageView winnerImg = (ImageView) findViewById(R.id.winnerImg);
                    winnerImg.setImageResource(getResources().getIdentifier(winner, "drawable", getPackageName()));

                    GridLayout gameGrid = (GridLayout) findViewById(R.id.gameGrid);
                    wonLayout.setVisibility(View.VISIBLE);
                    gameGrid.setAlpha(0.2f);

                    //reset turn images
                    ImageView cross = (ImageView) findViewById(R.id.cross);
                    ImageView circle = (ImageView) findViewById(R.id.circle);

                    cross.setImageResource(R.drawable.cross);
                    circle.setImageResource(R.drawable.naughts);

                    wonLayout.setVisibility(View.VISIBLE);//make the win dialog appear

                    break;

                } else {

                    boolean gameIsOver = true;

                    for(int counterState : gameState) {

                        if(counterState == 2) gameIsOver = false;

                    }

                    //to make sure that the game does not declare draw if someone wins on the last move
                    if(gameIsOver && gameIsActive) {

                        TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                        winnerMessage.setText("Draw!");

                        //change image
                        if(winnerMessage.getText().toString().equals("Draw!")) {
                            ImageView winnerImg = (ImageView) findViewById(R.id.winnerImg);
                            winnerImg.setImageResource(R.drawable.draw);
                        }

                        LinearLayout wonLayout = (LinearLayout)findViewById(R.id.playAgainLayout);
                        wonLayout.setVisibility(View.VISIBLE);

                        GridLayout gameGrid = (GridLayout) findViewById(R.id.gameGrid);
                        gameGrid.setAlpha(0.2f);

                        //reset turn images
                        ImageView cross = (ImageView) findViewById(R.id.cross);
                        ImageView circle = (ImageView) findViewById(R.id.circle);


                        cross.setImageResource(R.drawable.cross);
                        circle.setImageResource(R.drawable.naughts);

                    }

                }


            }
        }
    }

    public void playAgain (View view) {

        activePlayer = 0;
        gameIsActive = true;

        LinearLayout wonLayout = (LinearLayout)findViewById(R.id.playAgainLayout);
        wonLayout.setVisibility(View.INVISIBLE);

        //set turn to circle
        ImageView cross = (ImageView) findViewById(R.id.cross);
        cross.setImageResource(R.drawable.redcross);

        //clear the game states
        for(int i = 0; i<gameState.length; i++) {

            gameState[i]=2;

        }

        //clear the board

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gameGrid);
        gridLayout.setAlpha(1f);

        GridLayout gridBoard = (GridLayout) findViewById(R.id.gameGrid);
        for(int i = 0; i<gridBoard.getChildCount(); i++) {

            ((ImageView)gridBoard.getChildAt(i)).setImageResource(0);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
