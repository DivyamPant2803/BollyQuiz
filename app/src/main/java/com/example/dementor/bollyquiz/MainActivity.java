package com.example.dementor.bollyquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private boolean mIsCheater;

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX="index";

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_1, true),
            new TrueFalse(R.string.question_2, false),
            new TrueFalse(R.string.question_3, false),
            new TrueFalse(R.string.question_4, false),
            new TrueFalse(R.string.question_5, true),
    };
    private int mCurrentIndex = 0;

    private void updateQuestion(){
        if(mCurrentIndex!=-1) {
           if(mCurrentIndex>=4){
               int resId = R.string.error_toast_last;
               Toast.makeText(this,resId,Toast.LENGTH_SHORT).show();
               //int question = mQuestionBank[mCurrentIndex].getmQuestion();
               //mQuestionTextView.setText(question);
           }
           else {
               int question = mQuestionBank[mCurrentIndex].getmQuestion();
               mQuestionTextView.setText(question);
           }
        }
        else {
            int resId = R.string.error_toast;
            Toast.makeText(this,resId,Toast.LENGTH_SHORT).show();
            int question = mQuestionBank[mCurrentIndex+1].getmQuestion();
            mQuestionTextView.setText(question);
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        if(mCurrentIndex!=-1) {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();

            int messageResId = 0;

            if(mIsCheater){
                messageResId = R.string.judgement_toast;
            }
            else {
                if (userPressedTrue == answerIsTrue) {            //If user's answer matches the answer in the current TrueFalse obj.
                    messageResId = R.string.correct_toast;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");           // d stands for debug.Sends log messages. The first parameter TAG is a constant which tells the source of message, while the second is the contents of the message.
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
               mIsCheater=false;
                updateQuestion();
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {     //Anonymous inner class inside mTrueButton.setOnClickListener()
            @Override
            public void onClick(View v) {
                checkAnswer(true);

            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        mPrevButton = (ImageButton)findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex-1);
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Cheat Activity
                Intent i = new Intent(MainActivity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,answerIsTrue);
                //startActivity(i);
                startActivityForResult(i,0);        // Here 0 is a Request Code. A Request Code is a user-defined integer that is sent to child activity and then recieved back by the parent.
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1);
                mIsCheater=false;
                updateQuestion();
            }
        });
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);     // If the activity has been destroyed and re-created, set the value of currentPosition from the activity's previous state
            mIsCheater = savedInstanceState.getBoolean(CheatActivity.EXTRA_ANSWER_SHOWN,true);
        }
        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {       // When this method is called, the data is saved to the Bundle object. That Bundle object is then stuffed into ur activity's activity record by OS.
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState");
        outState.putInt(KEY_INDEX,mCurrentIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() is called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() is called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() is called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() is called");
    }
}
