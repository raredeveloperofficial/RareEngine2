package RareEngine2.GameUtils;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;

public class LineRenderer extends Renderer {
    public ArrayList<Vector2> points;
    public float strokeWidth;
    public int color;
    public boolean antiAlias;

    public LineRenderer() {
        points = new ArrayList<>();
        strokeWidth = 4f;
        color = Color.WHITE;
        antiAlias = true;
    }

    @Override
    public void start(GameObject o, GameView gv) {
        super.start(o, gv);
    }

    @Override
    public void update(GameObject o, GameView gv) {
        super.update(o, gv);
    }

    @Override
    public void destroy(GameObject o, GameView gv) {
        super.destroy(o, gv);
    }

    @Override
    public void render(Canvas canvas, GameObject object, Paint p, GameView gv) {
        super.render(canvas, object, p, gv);
        if (points.size() < 2) return;
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(strokeWidth);
        p.setAntiAlias(antiAlias);
        p.setColor(color);
        for (int i = 0; i < points.size() - 1; i++) {
			Vector2 a = null;
			Vector2 b = null;
			if (!object.getLayer().equals("ui")) {
				a = points.get(i).staticMultiply(gv.currentScene.getZoom());
				b = points.get(i + 1).staticMultiply(gv.currentScene.getZoom());
			}else{
				a = points.get(i);
				b = points.get(i + 1);
			}
            canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), p);
        }
    }

    public void addPoint(float x, float y) {
        points.add(new Vector2(x, y));
    }

    public void clearPoints() {
        points.clear();
    }

    public void setColor(int c) {
        color = c;
    }

    public void setStrokeWidth(float w) {
        strokeWidth = w;
    }

    public void setAntiAlias(boolean v) {
        antiAlias = v;
    }
	public void addRectangle(float width, float height) {
		points.clear();
		float hw = width / 2f;
		float hh = height / 2f;
		points.add(new Vector2(-hw, -hh));
		points.add(new Vector2(hw, -hh));
		points.add(new Vector2(hw, hh));
		points.add(new Vector2(-hw, hh));
		points.add(new Vector2(-hw, -hh));
	}
    @Override
    public LineRenderer copy() {
        LineRenderer r = (LineRenderer) super.copy();
        r.points = new ArrayList<>();
        for (Vector2 v : points) r.points.add(new Vector2(v));
        r.strokeWidth = strokeWidth;
        r.color = color;
        r.antiAlias = antiAlias;
        return r;
    }
	
}
