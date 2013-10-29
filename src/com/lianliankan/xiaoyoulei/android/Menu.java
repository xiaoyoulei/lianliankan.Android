package com.lianliankan.xiaoyoulei.android;

import com.lianliankan.xiaoyoulei.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.*;
import android.view.View.OnClickListener;

import com.lianliankan.xiaoyoulei.*;

public class Menu extends Activity{
	
	private Button Button_1 ;
	private Button Button_2 ;
	private Button Button_3 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		Button_1 = (Button)findViewById(R.id.button_1);
		Button_2 = (Button)findViewById(R.id.button_2);
		Button_3 = (Button)findViewById(R.id.button_3);
		
		OnClickListener lin = new StartGame() ;
		Button_1.setOnClickListener(lin);
		Button_2.setOnClickListener(lin);
		Button_3.setOnClickListener(lin);
	}
	class StartGame implements OnClickListener{

		public void onClick(View v)
		{
			switch(v.getId())
			{
			case R.id.button_1 :
				Intent myintent= new Intent();
				myintent.putExtra("model", 1) ;
				myintent.setClass(Menu.this , WelActivity.class );
				Menu.this.startActivity(myintent);
				break ;
			case R.id.button_2 :
				Intent myintent2 = new Intent();
				myintent2.putExtra("model", 2) ;
				myintent2.setClass(Menu.this , WelActivity.class );
				Menu.this.startActivity(myintent2);
				break ;
			case R.id.button_3:
				finish();
				break ;
			}
		}
	}
}
