package project.yeoeun.bdc.chooseperson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.yeoeun.bdc.R;
import project.yeoeun.bdc.flipanimation.ApplyRotation;
import project.yeoeun.bdc.util.Constants;
import project.yeoeun.bdc.util.GobobBaseActivity;

public class ChoosePersonActivity extends GobobBaseActivity {
    private int mTotalCount, mWinCount, mCurrentWinCount;

    private LinearLayout[] mLinearLayouts;
    private FrameLayout[] mFrameLayouts;
    private ImageView[][] mImageViews;
    private ApplyRotation mApplyRotation[];
    private int mRotationState[];

    private int[] mWinPositions;
    private int state = GAME_PLAYING;

    private final static int GAME_FINISHED = 0;
    private final static int GAME_PLAYING = 1;

    private final static int FRONT = 0;
    private final static int BACK = 1;

    @Bind(R.id.choose_person_game_btn)
    ImageButton gameBtn;

    @OnClick(R.id.choose_person_game_btn)
    public void mOnClick(View view) {
        if (view.getId() == R.id.choose_person_game_btn) {
            if (state == GAME_FINISHED) {
                // 다시하기
                mCurrentWinCount = mWinCount;
                mWinPositions = getRandomNums();
                for (int i : mWinPositions) {
                    Log.i("onResetClicked", "winPosition is " + i);
                }
                setCardClear();
                mWinPositions = getRandomNums();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setRandomCard(mWinPositions);
                    }
                }, 500);

                setGamePlaying();
            } else {
                // 결과 보이기
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                for (int i = 0; i < mTotalCount; i++) {
                    if (mApplyRotation[i].getIsFirstImage()) {
                        mApplyRotation[i].apply(ApplyRotation.ROTATION_FROM_FRONT);
//                        checkWin(position);
                    }
                }
                mCurrentWinCount = 0;
                setGameFinished();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);
        ButterKnife.bind(this);
        init();

        createRow1(); // row1 default;

        if (mTotalCount >= 3) {
            createRow2();
        }
        if (mTotalCount >= 5) {
            createRow3();
        }
        if (mTotalCount >= 7) {
            createRow4();
        }

        mWinPositions = getRandomNums();
        for (int i : mWinPositions) {
            Log.i("oncreate", "winPosition is " + i);
        }
        setRandomCard(mWinPositions);
    }

    private int getRandomNum() {
        Random random = new Random();
        int num = random.nextInt(mTotalCount);

        return num;
    }

    private int[] getRandomNums() {
        Random random = new Random();
        int num[] = new int[mWinCount];

        for (int i = 0; i < mWinCount; i++) {
            num[i] = random.nextInt(mTotalCount);
            for (int j = 0; j < i; j++) {
                if (num[i] == num[j]) {
                    i--;
                }
            }
        }
        return num;
    }

    private void setCardClear() {
        for (int i = 0; i < mTotalCount; i++) {
//            mRotationState[i] = FRONT;
            if (!mApplyRotation[i].getIsFirstImage()) {
                mApplyRotation[i].apply(ApplyRotation.ROTATION_FROM_BACK);
//                        checkWin(position);
            }
        }
    }

    private void setRandomCard(int[] positions) {
        for (int i = 0; i < mTotalCount; i++) {
            mImageViews[i][1].setImageResource(R.drawable.game_card_pass);
            mImageViews[i][1].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < positions.length; i++) {
            mImageViews[positions[i]][1].setImageResource(R.drawable.game_card_win);
            mImageViews[i][1].setVisibility(View.INVISIBLE);
        }
//        for (int i = 0; i < mTotalCount; i++) {
//            mImageViews[0][0].setImageResource(R.drawable.game_card_apple);
//            mImageViews[1][0].setImageResource(R.drawable.game_card_grape);
//        }
    }

    private void createRow1() {
        // 1행
        mLinearLayouts[0] = (LinearLayout) findViewById(R.id.choose_person_layout_row1);

        mFrameLayouts[0] = (FrameLayout) findViewById(R.id.person_frame_1);
        mFrameLayouts[1] = (FrameLayout) findViewById(R.id.person_frame_2);

        mImageViews[0][0] = (ImageView) findViewById(R.id.person_frame_1_img_1);
        mImageViews[0][1] = (ImageView) findViewById(R.id.person_frame_1_img_2);
        mImageViews[1][0] = (ImageView) findViewById(R.id.person_frame_2_img_1);
        mImageViews[1][1] = (ImageView) findViewById(R.id.person_frame_2_img_2);

        createSetOnClickListener(0);
        createSetOnClickListener(1);
    }

    private void createRow2() {
        // 2행
        mLinearLayouts[1] = (LinearLayout) findViewById(R.id.choose_person_layout_row2);
        mLinearLayouts[1].setVisibility(View.VISIBLE);

        mFrameLayouts[2] = (FrameLayout) findViewById(R.id.person_frame_3);
        mFrameLayouts[3] = (FrameLayout) findViewById(R.id.person_frame_4);

        mImageViews[2][0] = (ImageView) findViewById(R.id.person_frame_3_img_1);
        mImageViews[2][1] = (ImageView) findViewById(R.id.person_frame_3_img_2);
        createSetOnClickListener(2);

        if (mTotalCount == 3) {
            mFrameLayouts[3].setVisibility(View.INVISIBLE);
        } else {
            mImageViews[3][0] = (ImageView) findViewById(R.id.person_frame_4_img_1);
            mImageViews[3][1] = (ImageView) findViewById(R.id.person_frame_4_img_2);
            createSetOnClickListener(3);
        }
    }

    private void createRow3() {
        // 3행 // frame4, 5
        mLinearLayouts[2] = (LinearLayout) findViewById(R.id.choose_person_layout_row3);
        mLinearLayouts[2].setVisibility(View.VISIBLE);

        mFrameLayouts[4] = (FrameLayout) findViewById(R.id.person_frame_5);
        mFrameLayouts[5] = (FrameLayout) findViewById(R.id.person_frame_6);

        mImageViews[4][0] = (ImageView) findViewById(R.id.person_frame_5_img_1);
        mImageViews[4][1] = (ImageView) findViewById(R.id.person_frame_5_img_2);
        createSetOnClickListener(4);

        if (mTotalCount == 5) {
            mFrameLayouts[5].setVisibility(View.INVISIBLE);
        } else {
            mImageViews[5][0] = (ImageView) findViewById(R.id.person_frame_6_img_1);
            mImageViews[5][1] = (ImageView) findViewById(R.id.person_frame_6_img_2);
            createSetOnClickListener(5);
        }
    }


    private void createRow4() {
        // 4행 // frame6, 7
        mLinearLayouts[3] = (LinearLayout) findViewById(R.id.choose_person_layout_row4);
        mLinearLayouts[3].setVisibility(View.VISIBLE);

        mFrameLayouts[6] = (FrameLayout) findViewById(R.id.person_frame_7);
        mFrameLayouts[7] = (FrameLayout) findViewById(R.id.person_frame_8);

        mImageViews[6][0] = (ImageView) findViewById(R.id.person_frame_7_img_1);
        mImageViews[6][1] = (ImageView) findViewById(R.id.person_frame_7_img_2);
        createSetOnClickListener(6);

        if (mTotalCount == 7) {
            mFrameLayouts[7].setVisibility(View.INVISIBLE);
        } else {
            mImageViews[7][0] = (ImageView) findViewById(R.id.person_frame_8_img_1);
            mImageViews[7][1] = (ImageView) findViewById(R.id.person_frame_8_img_2);
            createSetOnClickListener(7);
        }
    }

    private void createSetOnClickListener(final int position) {
        mApplyRotation[position] = new ApplyRotation(mImageViews[position][0], mImageViews[position][1]);

        mImageViews[position][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplyRotation[position].getIsFirstImage()) {
                    mApplyRotation[position].apply(ApplyRotation.ROTATION_FROM_FRONT);
                    checkWin(position);
                }

            }
        });
    }

    private void checkWin(int position) {
        for (int i = 0; i < mWinPositions.length; i++) {
            if (comparePosition(position, mWinPositions[i])) {
                break;
            }
        }

        if (mCurrentWinCount == 0) {
            createFinishDialog();
            setGameFinished();
        }
    }

    private boolean comparePosition(int clickedPosition, int winPosition) {
        if (clickedPosition == winPosition) {
            mCurrentWinCount--;
            if (mCurrentWinCount > 0) {
                String message = "아직 안심하긴 이릅니다. " + mCurrentWinCount + " 명 남음 >_<";
                Toast.makeText(ChoosePersonActivity.this, message, Toast.LENGTH_SHORT).show();
            }
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            return true;
        } else {
            return false;
        }
    }

    private void init() {
        Intent intent = getIntent();
        String totalCount = intent.getStringExtra(Constants.PERSON_COUNT_TOTAL);
        String winCount = intent.getStringExtra(Constants.PERSON_COUNT_WIN);
        mTotalCount = Integer.parseInt(totalCount);
        mWinCount = Integer.parseInt(winCount);
        mCurrentWinCount = mWinCount;
        int rowCount = (mTotalCount + 1) / 2;

        Log.i("init()", mWinCount + "/" + mTotalCount);
        mLinearLayouts = new LinearLayout[rowCount];
        if (mTotalCount % 2 == 1) {
            mFrameLayouts = new FrameLayout[mTotalCount + 1];
        } else {
            mFrameLayouts = new FrameLayout[mTotalCount];
        }
        Log.i("init()", "" + mFrameLayouts.length);
        mImageViews = new ImageView[mTotalCount][2];
        mApplyRotation = new ApplyRotation[mTotalCount];
    }

    private void createFinishDialog() {
        String message = null;
        if (mWinCount == 1) {
            message = "축하드립니다 한 명인데 걸리셨군요";
        } else {
            message = "지갑 꺼낼 사람이 모두 정해졌습니다";
        }
        if (state == GAME_PLAYING) {
            new AlertDialog.Builder(
                    ChoosePersonActivity.this)
                    .setTitle(message)
                    .setPositiveButton("확인", null)
                    .show();
        }
    }

    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
        if (mCurrentWinCount != 0) {
            createBackPressedDialog();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
        }
    }


    private void createBackPressedDialog() {
        new AlertDialog.Builder(
                ChoosePersonActivity.this)
                .setTitle("아직 끝나지 않습니다. 나가시겠습니까?")
                .setPositiveButton("확인", mBackPressedOnClickListener)
                .setNegativeButton("취소", null)
                .show();
    }

    private DialogInterface.OnClickListener mBackPressedOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
            finish();
        }
    };

    private void setGameFinished() {
        gameBtn.setImageResource(R.drawable.choose_person_reset_btn);
        state = GAME_FINISHED;
    }

    private void setGamePlaying() {
        gameBtn.setImageResource(R.drawable.choose_person_show_result_btn);
        state = GAME_PLAYING;
    }


    private void setCardFront(int position) {
        mRotationState[position] = FRONT;
    }

    private void setCardBack(int position) {
        mRotationState[position] = BACK;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
