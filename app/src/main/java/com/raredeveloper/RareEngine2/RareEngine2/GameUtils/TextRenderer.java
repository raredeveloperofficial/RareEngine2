package RareEngine2.GameUtils;
import android.graphics.*;
import java.util.*;
import java.io.*;

public class TextRenderer extends Renderer
{
	public float fontSize = 30;
	public String text="";
	public int textcolor=Color.WHITE;
	public boolean centered = false,isBold = false;
	public Typeface typeface=Typeface.DEFAULT;
	public FontFrom fontfrom = FontFrom.Default;
	public String fontpath="";
	public int fontResourceId=-1;
	private float prevzoom = 1,error = 1;
	public enum FontFrom{
		Asset,File,Default,Resource
		}

	@Override
	public void start(GameObject o, GameView gv) {
		super.start(o, gv);
	}
	
	@Override
	public void render(Canvas canvas, GameObject object, Paint p, GameView gv)
	{
		super.render(canvas, object, p, gv);
		if(prevzoom != gv.currentScene.getZoom()){
			prevzoom = gv.currentScene.getZoom();
			error = calculateTextZoomCorrection(p,fontSize,prevzoom);
		}
		String[] s =text.split("\n");
		float width = 0;
		float height = 0;
		if(!object.getLayer().equals("ui")){
			p.setTextSize(fontSize*gv.currentScene.getZoom()*error);
		}else{
			p.setTextSize(fontSize*error);
		}
		p.setColor(textcolor);
		p.setAntiAlias(true);
		if(fontfrom==FontFrom.Asset){
			typeface=Typeface.createFromAsset(gv.getActivity().getAssets(),fontpath);
		}else if(fontfrom==FontFrom.File){
			File f = new File(fontpath);
			typeface=typeface.createFromFile(f);
		}else if(fontfrom == FontFrom.Resource){
			typeface=gv.getContext().getResources().getFont(fontResourceId);
		}
		if(isBold){
			Typeface boldFont = Typeface.create(typeface, Typeface.BOLD);
			p.setTypeface(boldFont);
		}else{
			p.setTypeface(typeface);
		}
		for(String ss:s){
			float measuredWidth = p.measureText(ss);
			float measuredHeight = p.getFontMetrics().descent-p.getFontMetrics().ascent;
			scale.set(measuredWidth,measuredHeight);
			if(width<measuredWidth){
				width=measuredWidth;
			}
			int index = Arrays.asList(s).indexOf(ss);
			height+=measuredHeight;
			if(!centered){
				canvas.drawText(ss,-measuredWidth/2,(index*measuredHeight)/*+(measuredHeight/2)*/,p);
			}
		}
		//object.setScale(width,height);
		if(!centered)return;
		for(String ss:s){
			float measuredWidth = p.measureText(ss);
			float measuredHeight = p.getFontMetrics().descent-p.getFontMetrics().ascent;
			scale.set(measuredWidth,measuredHeight);
			int index = Arrays.asList(s).indexOf(ss);
			canvas.drawText(ss,((width-measuredWidth)/2)-(measuredWidth/2),(index*measuredHeight)/*+(measuredHeight/2)*/,p);
		}
	}
	public TextRenderer(){
		scale = new Vector2();
	}
	public FontFrom getFontFrom(){
		return fontfrom;
	}
	
	public void setFontFrom(FontFrom fontfrom){
		this.fontfrom=fontfrom;
	}
	
	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getFontPath() {
		return fontpath;
	}

	public void setFontPath(String fontpath) {
		this.fontpath = fontpath;
	}
	
	public int getTextcolor() {
		return textcolor;
	}

	public void setTextcolor(int textcolor) {
		this.textcolor = textcolor;
	}
	
	public int getFontResourceId() {
		return fontResourceId;
	}

	public void setFontResourceId(int fontResourceId) {
		this.fontResourceId = fontResourceId;
	}
	
	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public boolean isBold() {
		return isBold;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	private float calculateTextZoomCorrection(Paint p, float fontSize, float zoom) {
		p.setTextSize(fontSize);
		Paint.FontMetrics fmBase = p.getFontMetrics();
		float baseHeight = fmBase.descent - fmBase.ascent;

		p.setTextSize(fontSize * zoom);
		Paint.FontMetrics fmZoom = p.getFontMetrics();
		float zoomedHeight = fmZoom.descent - fmZoom.ascent;

		float actualScale = zoomedHeight / baseHeight;
		return zoom / actualScale; // correction factor
	}
}
