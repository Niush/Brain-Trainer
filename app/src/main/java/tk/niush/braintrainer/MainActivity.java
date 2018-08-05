package tk.niush.braintrainer;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random;

    private int num1;
    private int num2;

    private int sum;
    private int option2;
    private int option3;
    private int option4;

    TextView question;

    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;

    private TextView timer;

    TextView finalScore;
    Button restart;

    private int totalCorrect;
    private int totalAsked;

    private TextView score;

    RelativeLayout startScreen;

    MediaPlayer right;
    MediaPlayer wrong;
    MediaPlayer tick;
    MediaPlayer over;

    SeekBar difficulty;
    int max = 101;
    int maxSum = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty = (SeekBar)findViewById(R.id.difficulty);
        difficulty.setMax(2);
        difficulty.setProgress(1);

        difficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView diffStatus = (TextView)findViewById(R.id.diffStatus);
                if(i==0){
                    diffStatus.setText("Difficulty: Easy");
                    max=11;
                    maxSum=21;
                    createRandom(diffStatus);
                }else if(i==1){
                    diffStatus.setText("Difficulty: Average");
                    max=51;
                    maxSum=101;
                    createRandom(diffStatus);
                }else if(i==2){
                    diffStatus.setText("Difficulty: Hard");
                    max=501;
                    maxSum=1001;
                    createRandom(diffStatus);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        choice1 = (Button)findViewById(R.id.choice1);
        choice2 = (Button)findViewById(R.id.choice2);
        choice3 = (Button)findViewById(R.id.choice3);
        choice4 = (Button)findViewById(R.id.choice4);

        finalScore = (TextView)findViewById(R.id.finalScore);
        restart = (Button)findViewById(R.id.restart);

        totalAsked = 0;
        totalCorrect = 0;

        random = new Random();

        TextView title = (TextView)findViewById(R.id.title);
        title.animate().scaleY(1f).scaleX(1f).alpha(1f).setDuration(500);

        Button start = (Button)findViewById(R.id.start);
        start.animate().translationY(0f).alpha(1f).setDuration(500);

        tick = MediaPlayer.create(MainActivity.this,R.raw.tick);
        right = MediaPlayer.create(MainActivity.this,R.raw.right);
        wrong = MediaPlayer.create(MainActivity.this,R.raw.wrong);
        over = MediaPlayer.create(MainActivity.this,R.raw.over);

        View view = new View(getApplicationContext());
        createRandom(view);

    }

    public void start(View view){
        startScreen = (RelativeLayout)findViewById(R.id.startScreen);
        startScreen.animate().alpha(0f).translationX(4000f).setDuration(500);
        startScreen.setVisibility(View.GONE);

        startTimer(view);
    }

    public void createRandom(View view){
        num1 = random.nextInt(max);
        num2 = random.nextInt(max);

        question = (TextView)findViewById(R.id.question);
        question.setText(String.valueOf(num1) + " + " + String.valueOf(num2));

        sum = num1 + num2; //option 1
        option2 = random.nextInt(maxSum);
        option3 = random.nextInt(maxSum);
        option4 = random.nextInt(maxSum);
        while(option2==sum||option3==sum||option4==sum){
            option2 = random.nextInt(maxSum);
            option3 = random.nextInt(maxSum);
            option4 = random.nextInt(maxSum);
        }

        ArrayList<Integer> options = new ArrayList<Integer>();
        options.addAll(Arrays.asList(sum,option2,option3,option4));

        choice1.setBackgroundColor(0xFF878787);
        int which1 = random.nextInt(4);
        choice1.setText(String.valueOf(options.get(which1)));
        choice1.setTag(options.get(which1));
        options.remove(which1);

        choice2.setBackgroundColor(0xFF878787);
        int which2 = random.nextInt(3);
        choice2.setText(String.valueOf(options.get(which2)));
        choice2.setTag(options.get(which2));
        options.remove(which2);

        choice3.setBackgroundColor(0xFF878787);
        int which3 = random.nextInt(2);
        choice3.setText(String.valueOf(options.get(which3)));
        choice3.setTag(options.get(which3));
        options.remove(which3);

        choice4.setBackgroundColor(0xFF878787);
        choice4.setText(String.valueOf(options.get(0)));
        choice4.setTag(options.get(0));
        options.remove(0);

        options.clear();
    }

    public void startTimer(View view){
        timer = (TextView)findViewById(R.id.timer);
        tick.start();
        new CountDownTimer(60000+100,1000){
            @Override
            public void onTick(long l) {
                timer.setText(String.valueOf((int)Math.floor(l)/1000) + " sec");
                if((int)Math.floor(l)<20000){
                    RelativeLayout gameScreen = (RelativeLayout)findViewById(R.id.gameScreen);
                    gameScreen.setBackgroundColor(0xFF121212);
                }
                else if((int)Math.floor(l)<40000){
                    RelativeLayout gameScreen = (RelativeLayout)findViewById(R.id.gameScreen);
                    gameScreen.setBackgroundColor(0xFF323232);
                }
            }

            @Override
            public void onFinish() {
                over.start();

                timer.setText("Time Up..");
                finalScore.animate().alpha(1f).translationY(0f).setDuration(1000);
                finalScore.setVisibility(View.VISIBLE);

                restart.animate().alpha(1f).translationX(0f).setDuration(1000);
                restart.setVisibility(View.VISIBLE);

                if(totalAsked==totalCorrect){
                    question.setText("100% Correct, Impressive.");
                }else {
                    question.setText("¯\\_(ツ)_/¯");
                }

                choice1.setClickable(false);
                choice1.setText(":D");
                choice2.setClickable(false);
                choice2.setText(":)");
                choice3.setClickable(false);
                choice3.setText(":>");
                choice4.setClickable(false);
                choice4.setText(";)");
            }
        }.start();
    }

    public void check(View view){
        final Button choice = (Button)view;
        int userChoice = (int)choice.getTag();
        totalAsked++;

        if(userChoice==sum){
            totalCorrect++;
            new CountDownTimer(200,100){
                @Override
                public void onFinish() {
                    choice.setBackgroundColor(0xFF878787);
                }

                @Override
                public void onTick(long l) {
                    choice.setBackgroundColor(0xFF22aa22);
                }
            }.start();
            right.stop();
            right = MediaPlayer.create(MainActivity.this,R.raw.right);
            right.start();
        }else{
            new CountDownTimer(200,100){
                @Override
                public void onFinish() {
                    choice.setBackgroundColor(0xFF878787);
                }

                @Override
                public void onTick(long l) {
                    choice.setBackgroundColor(0xFFaa2222);
                }
            }.start();
            wrong.stop();
            wrong = MediaPlayer.create(MainActivity.this,R.raw.wrong);
            wrong.start();
        }

        score = (TextView)findViewById(R.id.score);
        score.setText(String.valueOf(totalCorrect) + "/" + String.valueOf(totalAsked));
        finalScore.setText("You Got : " + String.valueOf(totalCorrect) + "/" + String.valueOf(totalAsked));

        createRandom(view);
    }

    public void restart(View view){
        timer.setText("60 sec");
        finalScore.animate().alpha(0f).translationY(200f).setDuration(1000);
        finalScore.setVisibility(View.INVISIBLE);

        restart.animate().alpha(0f).translationX(-200f).setDuration(1000);
        restart.setVisibility(View.INVISIBLE);

        createRandom(view);
        startTimer(view);

        choice1.setClickable(true);
        choice2.setClickable(true);
        choice3.setClickable(true);
        choice4.setClickable(true);

        totalAsked = 0;
        totalCorrect = 0;

        score.setText("0/0");
        finalScore.setText("You Got : 0/0");

        tick = MediaPlayer.create(MainActivity.this,R.raw.tick);
        right.stop();
        right = MediaPlayer.create(MainActivity.this,R.raw.right);
        wrong.stop();
        wrong = MediaPlayer.create(MainActivity.this,R.raw.wrong);
        over.stop();
        over = MediaPlayer.create(MainActivity.this,R.raw.over);
    }
}
