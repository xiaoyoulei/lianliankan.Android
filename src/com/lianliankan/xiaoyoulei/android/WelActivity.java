package com.lianliankan.xiaoyoulei.android;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mobstat.SendStrategyEnum;
import com.lianliankan.xiaoyoulei.R;
import com.lianliankan.xiaoyoulei.R.string;
import com.lianliankan.xiaoyoulei.view.GameView;
import com.lianliankan.xiaoyoulei.view.OnStateListener;
import com.lianliankan.xiaoyoulei.view.OnTimerListener;
import com.lianliankan.xiaoyoulei.view.OnToolsChangeListener;

@SuppressLint("HandlerLeak")
public class WelActivity extends Activity
	implements OnClickListener,OnTimerListener,OnStateListener,OnToolsChangeListener{
	
	private ImageButton btnPlay;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
//	private ImageView imgTitle;
	private GameView gameView;
	private SeekBar progress;
	private MyDialog dialog;
//	private ImageView clock;
	private TextView clock_time ;
	private TextView textRefreshNum;
	private TextView textTipNum;
	private TextView model_text;
	private ImageButton stop_button;
	private TextView score_view ;
	
	private MediaPlayer player;
	
	private int game_model ; 
	
	static final int WIN_HANDLE = 0;
	static final int LOSE_HANDLE = 1;
	static final int SET_TIME_HANDLE = 2;
	static final int SET_MODEL_HANDLE = 3;
	static final int SET_SCORE_HANDLE = 4;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case WIN_HANDLE:
				if(game_model == 2)
				{
					// endless
					gameView.endlessPlay();
				}
				else {
					
				
					dialog = new MyDialog(WelActivity.this,gameView,"胜利！",gameView.getNowScore() );
					dialog.show();
				}
				break;
			case LOSE_HANDLE:
				dialog = new MyDialog(WelActivity.this,gameView,"失败！",gameView.getNowScore() );
				dialog.show();
				break ;
			case SET_TIME_HANDLE:
				clock_time.setText(""+msg.arg1);
				if(msg.arg1 % 2 == 0)
				{
					clock_time.setTextColor(Color.YELLOW);
				}
				else {
					clock_time.setTextColor(Color.RED);
				}
				break ;
			case SET_MODEL_HANDLE:
				if(msg.arg1 == 1)
				{
					model_text.setText(R.string.nomal);
				}
				if(msg.arg1 == 2)
				{
					model_text.setText(R.string.endless);
				}
				break;
			case SET_SCORE_HANDLE:
				score_view.setText(msg.arg1+"分");
				break;
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        // baidu mobile tongji
//        com.baidu.mobstat.StatService.setSessionTimeOut(1);
//        com.baidu.mobstat.StatService.setOn(this , StatService.EXCEPTION_LOG);
        com.baidu.mobstat.StatService.setSendLogStrategy(this , SendStrategyEnum.APP_START, 1, false);
        

        Intent intent = getIntent();
        game_model = intent.getIntExtra("model", 1);
        btnPlay = (ImageButton) findViewById(R.id.play_btn);
        btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
        btnTip = (ImageButton) findViewById(R.id.tip_btn);
//        imgTitle = (ImageView) findViewById(R.id.title_img);
        stop_button = (ImageButton)findViewById(R.id.stop_button);
        stop_button.setVisibility(View.GONE);
        stop_button.setEnabled(false);
        gameView = (GameView) findViewById(R.id.game_view);
        score_view = (TextView) findViewById(R.id.score_text);
        score_view.setVisibility(View.GONE);
//        clock = (ImageView) findViewById(R.id.clock);
        clock_time = (TextView)findViewById(R.id.clock_time);
        clock_time.setVisibility(View.GONE);
        model_text = (TextView)findViewById(R.id.model_text);
        model_text.setVisibility(View.GONE);
        Message msg = new Message();
        msg.what =  SET_MODEL_HANDLE;
        msg.arg1 = game_model ;
        handler.sendMessage(msg);
        progress = (SeekBar) findViewById(R.id.timer);
        textRefreshNum = (TextView) findViewById(R.id.text_refresh_num);
        textTipNum = (TextView) findViewById(R.id.text_tip_num);
        //XXX
        progress.setMax(gameView.getTotalTime());
        
        btnPlay.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnTip.setOnClickListener(this);
        stop_button.setOnClickListener(this);
        gameView.setOnTimerListener(this);
        gameView.setOnStateListener(this);
        gameView.setOnToolsChangedListener(this);
        GameView.initSound(this);
        
        Animation scale = AnimationUtils.loadAnimation(this,R.anim.scale_anim);
//        imgTitle.startAnimation(scale);
        btnPlay.startAnimation(scale);
        
        player = MediaPlayer.create(this, R.raw.bg);
        player.setLooping(true);//设置循环播放
        player.start();
        
//        GameView.soundPlay.play(GameView.ID_SOUND_BACK2BG, -1);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	gameView.setMode(GameView.PAUSE);
    }
    
    @Override
	protected void onDestroy() {
    	super.onDestroy();
    	gameView.setMode(GameView.QUIT);
	}

    private int game_state = 0 ;
    private void switch_state()
    {
    	if(game_state == 1)
    	{
    		// 进去暂停模式
    		game_state = 0;
			btnPlay.setVisibility(View.VISIBLE);
			gameView.setVisibility(View.GONE);
   		
			btnRefresh.setVisibility(View.GONE);
			btnTip.setVisibility(View.GONE);
			progress.setVisibility(View.GONE);
			model_text.setVisibility(View.GONE);
			score_view.setVisibility(View.GONE);
			stop_button.setVisibility(View.GONE);
			clock_time.setVisibility(View.GONE);
			textRefreshNum.setVisibility(View.GONE);
			textTipNum.setVisibility(View.GONE);
    	
			gameView.stopTimer();
    	}
    	else {
    		// 进入游戏模式
    		game_state = 1;
			Animation scaleOut = AnimationUtils.loadAnimation(this,R.anim.scale_anim_out);
			Animation transIn = AnimationUtils.loadAnimation(this,R.anim.trans_in);
    		
			btnPlay.startAnimation(scaleOut);
			btnPlay.setVisibility(View.GONE);
			gameView.setVisibility(View.VISIBLE);
   		
			btnRefresh.setVisibility(View.VISIBLE);
			btnTip.setVisibility(View.VISIBLE);
			progress.setVisibility(View.VISIBLE);
			progress.setEnabled(false);
			model_text.setVisibility(View.VISIBLE);
			score_view.setVisibility(View.VISIBLE);
		//	stop_button.setVisibility(View.VISIBLE);
//			stop_button.setEnabled(true);
//    	clock.setVisibility(View.VISIBLE);
			clock_time.setVisibility(View.VISIBLE);
			textRefreshNum.setVisibility(View.VISIBLE);
			textTipNum.setVisibility(View.VISIBLE);
    	
			btnRefresh.startAnimation(transIn);
			btnTip.startAnimation(transIn);
			gameView.startAnimation(transIn);
			player.pause();
			gameView.continue_play();
    	}
   	
    }
	@Override
	public void onClick(View v) {
    	
    	switch(v.getId()){
    	case R.id.play_btn:
    		game_state = 1 ;
    		Animation scaleOut = AnimationUtils.loadAnimation(this,R.anim.scale_anim_out);
        	Animation transIn = AnimationUtils.loadAnimation(this,R.anim.trans_in);
    		
    		btnPlay.startAnimation(scaleOut);
    		btnPlay.setVisibility(View.GONE);
//    		imgTitle.setVisibility(View.GONE);
    		gameView.setVisibility(View.VISIBLE);
    		
    		btnRefresh.setVisibility(View.VISIBLE);
    		btnTip.setVisibility(View.VISIBLE);
    		progress.setVisibility(View.VISIBLE);
    		progress.setEnabled(false);
    		model_text.setVisibility(View.VISIBLE);
    		score_view.setVisibility(View.VISIBLE);
//    		stop_button.setVisibility(View.VISIBLE);
 //   		stop_button.setEnabled(true);
//    		clock.setVisibility(View.VISIBLE);
    		clock_time.setVisibility(View.VISIBLE);
    		textRefreshNum.setVisibility(View.VISIBLE);
    		textTipNum.setVisibility(View.VISIBLE);
    		
    		btnRefresh.startAnimation(transIn);
    		btnTip.startAnimation(transIn);
    		gameView.startAnimation(transIn);
    		player.pause();
    		gameView.startPlay();
    		break;
    	case R.id.refresh_btn:
    		Animation shake01 = AnimationUtils.loadAnimation(this,R.anim.shake);
    		btnRefresh.startAnimation(shake01);
    		gameView.refreshChange();
    		break;
    	case R.id.tip_btn:
    		Animation shake02 = AnimationUtils.loadAnimation(this,R.anim.shake);
    		btnTip.startAnimation(shake02);
    		gameView.autoClear();
    		break;
    	case R.id.stop_button:
    		switch_state();
    		break;
    	}
	}

	@Override
	public void onTimer(int leftTime) {
		Log.i("onTimer", leftTime+"");
		progress.setProgress(leftTime);
//		clock_time.setText(""+leftTime);
		Message msg = new Message();
		msg.what = SET_TIME_HANDLE;
		msg.arg1 = leftTime ;
		handler.sendMessage(msg);
		
	}
	
	@Override
	public void setScore(int score)
	{
		Message msg = new Message();
		msg.what = SET_SCORE_HANDLE ;
		msg.arg1 = score ;
		handler.sendMessage(msg);
	}

	@Override
	public void OnStateChanged(int StateMode) {
		switch(StateMode){
		case GameView.WIN:
			handler.sendEmptyMessage(0);
			break;
		case GameView.LOSE:
			handler.sendEmptyMessage(1);
			break;
		case GameView.PAUSE:
			player.stop();
	    	gameView.player.stop();
	    	gameView.stopTimer();
			break;
		case GameView.QUIT:
			player.release();
	    	gameView.player.release();
	    	gameView.stopTimer();
	    	break;
		case GameView.MENU:
			this.finish();
			break;
		}
	}

	@Override
	public void onRefreshChanged(int count) {
		textRefreshNum.setText(""+gameView.getRefreshNum());
	}

	@Override
	public void onTipChanged(int count) {
		textTipNum.setText(""+gameView.getTipNum());
	}
	
	@Override
	public int addTime()
	{
		if(game_model == 2)
		{
			return 2;
		}
		return 0;
	}
	
	@Override
	public int addScore()
	{
		return 2;
	}

	public void quit(){
		this.finish();
	}
}