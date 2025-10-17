package RareEngine2.GameUtils;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Toast;


//@author raredeveloper
public class ImageRenderer extends Renderer
{
	public Bitmap image;
	public Vector2 tilescale,tileid;
	public static final float MATCH_IMAGE =-1;
	private Vector2 prevscale;
	private Bitmap previmg;
	public boolean pixeled = true;
	public ImageRenderer(){
		scale = new Vector2(100,100);
		tileid = new Vector2(0,0);
		tilescale = new Vector2();
		prevscale = new Vector2();
	}
	@Override
	public void start(GameObject o,GameView gv)
	{
		super.start(o,gv);
		prevscale.set(o.scale.getX(),o.scale.getY());
		//scale.set(prevscale.getX(),prevscale.getY());
	}
	public Vector2 getTileIdWithNumber(int number){
		int width = Math.round(image.getWidth()/tilescale.getX());
		int x = number%width;
		int y = (int)Math.ceil((double)number/width);
		return new Vector2(x,y);
	}
	@Override
	public void update(GameObject o,GameView gv)
	{
		super.update(o,gv);
	}
	
	@Override
	public void destroy(GameObject o,GameView gv)
	{
		super.destroy(o,gv);
		if(image!= null){
			image.recycle();
			image = null;
		}
	}
	@Override
	public void render(Canvas canvas, GameObject object,Paint p,GameView gv)
	{
		super.render(canvas, object,p,gv);
		if(image != null){
			if(!(tilescale.getX() == MATCH_IMAGE)){
				tilescale.setX(image.getWidth());
			}
			if(!(tilescale.getY() == MATCH_IMAGE)){
				tilescale.setY(image.getHeight());
			}
			Rect src = new Rect();
			if(prevscale.getX()!=scale.getX()||prevscale.getY()!=scale.getY()||previmg!=image){
				prevscale.set(scale.getX(),scale.getY());
				Bitmap mapi = Bitmap.createScaledBitmap(image,(int)prevscale.getX(),(int)prevscale.getY(),!pixeled);
				//gv.toast(object.getName(),Toast.LENGTH_SHORT);
				image = mapi;
                previmg = image;
				
				scale.set(prevscale.getX(),prevscale.getY());
			}
			src.set(Math.round(tileid.getX()*tilescale.getX()),Math.round(tileid.getY()*tilescale.getY()),Math.round(tilescale.getX()+(tileid.getX()*tilescale.getX())),Math.round(tilescale.getY()+(tileid.getY()*tilescale.getY())));
			RectF dst = new RectF();
			//ltrb
			dst.set(-object.scale.getX()/2,-object.scale.getY()/2,object.scale.getX()/2,object.scale.getY()/2);
			p.setFilterBitmap(!pixeled);
			p.setAntiAlias(!pixeled);
			canvas.drawBitmap(image,src,dst,p);
			
		}
		
	}
	public void setImage(Bitmap map){
		image = map;
	}
	public static Bitmap getFromResource(Resources resource,int resid){
		Bitmap m = BitmapFactory.decodeResource(resource,resid);
		return m;
	}
	public static Bitmap getFromPath(String path){
		Bitmap m=BitmapFactory.decodeFile(path);
		return m;
	}

    @Override
    public ImageRenderer copy() {
        ImageRenderer r = (ImageRenderer)super.copy();
        r.tileid = new Vector2(tileid);
        r.tilescale = new Vector2(tilescale);
		r.prevscale = new Vector2(prevscale);
        return r;
    }
    
}
