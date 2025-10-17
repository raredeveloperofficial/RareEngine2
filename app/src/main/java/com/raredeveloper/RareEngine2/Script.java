package com.raredeveloper.RareEngine2;
import RareEngine2.GameUtils.Component;
import RareEngine2.GameUtils.GameObject;
import RareEngine2.GameUtils.GameView;
import RareEngine2.GameUtils.Vector2;
import RareEngine2.GameUtils.ImageRenderer;
import android.graphics.Color;
import android.widget.Toast;

/**
 * @Author Rare Developer 
 * @Date 2025/06/11 22:13
 */
public class Script extends Component {

    int frames = 0;
    @Override
    public void start(GameObject o, GameView gv) {
        frames = 0;
        super.start(o, gv);
    }

    @Override
    public void update(GameObject o, GameView gv) {
        o.position.add(new Vector2(0,-2));
        gv.currentScene.backgroundcolor = Color.BLUE;
        frames++;
        if(frames ==60){
           /*GameObject op= o.copy(gv);
           op.position.set(0,0);
           //((ImageRenderer)op.getComponentAt(0)).getFromResource(gv.getResources(),android.R.drawable.btn_plus);
           gv.currentScene.addObject(op);*/
           
        }
		
		if(gv.isDown){
			if(o.isDown){
				gv.toast("click",Toast.LENGTH_SHORT);
			}
		}
        super.update(o, gv);
    }

    @Override
    public void destroy(GameObject o, GameView gv) {
        super.destroy(o, gv);
    }
}
