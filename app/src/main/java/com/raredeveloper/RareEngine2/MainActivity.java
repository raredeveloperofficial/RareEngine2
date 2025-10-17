package com.raredeveloper.RareEngine2;
 
import RareEngine2.GameUtils.GameObject;
import RareEngine2.GameUtils.GameView;
import RareEngine2.GameUtils.ImageRenderer;
import RareEngine2.GameUtils.PreLoadScene;
import RareEngine2.GameUtils.TextRenderer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
//@author raredeveloper
public class MainActivity extends Activity {
	
    //main game view
     public GameView gamev;
	public PreLoadScene pls;
	public static Context ct;
	@Override()
	public void onPointerCaptureChanged(boolean hasCapture) {
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		MainActivity.ct = getApplicationContext();
        pls = new PreLoadScene();
        //initialize a game view
        gamev = new GameView(this,this);
        gamev.currentScene.backgroundcolor =(Color.BLUE);
        //set game view to screen
        setContentView(gamev);
        //creating game object
        GameObject o = new GameObject();
        //create and add components
        ImageRenderer ir = new ImageRenderer();
        ir.setImage(ImageRenderer.getFromResource(getResources(),R.drawable.ic_launcher));
        o.addComponent(ir,gamev);
		TextRenderer tr = new TextRenderer();
		tr.setText("udhduxhxjxhxuxuxj");
		tr.setTextcolor(Color.RED);
		tr.setBold(true);
		o.addComponent(tr,gamev);
        Script sc = new Script();
        o.addComponent(sc,gamev);
        //add object to game view's current scene
        pls.addObject(o);
        pls.setBackgroundColour(Color.GRAY);
        gamev.setScene(pls.createScene(gamev));
		gamev.currentScene.setZoom(1f);
		gamev.currentScene.CameraPosition.setX(-40);;
        gamev.currentScene.backgroundcolor =(Color.BLUE);
		
    }
	public static void toast(String s){
		Toast.makeText(ct,s,Toast.LENGTH_SHORT).show();
	}
	
}

