package com.example.twoyears;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.twoyears.view.ArcMenu;
import com.example.twoyears.view.ArcMenu.OnMenuItemClickListener;

public class MainActivity extends Activity {
	   private ArcMenu mArcMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
        mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public void onClick(View view, int pos)
			{
				Toast.makeText(MainActivity.this, pos+":"+view.getTag(), Toast.LENGTH_SHORT).show();
			}
		});
     
    }
}
