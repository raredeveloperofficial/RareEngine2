package RareEngine2.GameUtils;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import java.util.*;
import android.widget.*;
import android.app.*;
import android.util.*;
import android.content.res.Configuration;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public boolean paused = false,destroyed = false;
	private Handler h;
	public static final String REMOVE_WATERMARK_PASS = "-watermark";
	private long frames = 0;
	private HandlerThread thread;
	public Scene currentScene;
	public ArrayList<String> layers = new ArrayList<>();
	private Paint paint;
	private int touchmilli;
	private GameViewComponent comp;
	private Vector2 touchdist = new Vector2();
	public Vector2 touchPosition = new Vector2(),touchPositionui = new Vector2(),dragValue = new Vector2(),dragValueUi = new Vector2();
	public boolean isDown,isUp,isClick,isDrag;
	private Activity activity;
	private boolean drawWaterMark=true ;


	@Override
	public void surfaceCreated(final SurfaceHolder sh) {
		thread = new HandlerThread("thread");
		thread.start();
		paint = new Paint();
		//currentScene=new Scene();
		if (currentScene == null) {
			setScene(new Scene());
		}

		isDown = false;isUp = true;isClick = false;isDrag = false;paused = false;

		//currentScene.setBackgroundColour(Color.WHITE);
		h = new Handler(thread.getLooper());

		h.post(new Runnable(){
				@Override
				public void run() {
					if (!destroyed) {
						Canvas can = sh.lockCanvas();
						if (can != null) {
							can.drawColor(currentScene.backgroundcolor);
							try {
								update(can);

							} catch (Exception e) {}
						}
						if (can != null && drawWaterMark) {
							drawWaterMark(can);
						}
						if (can != null)sh.unlockCanvasAndPost(can);

						if (!paused) {
							h.postDelayed(this, 16);
						}
					}
				}
			});

	}
	public void drawWaterMark(Canvas can) {
		can.save();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(8);
		float width = paint.measureText("made with RareEngine2");
		can.translate(getWidth() / 2 - (width / 2), getHeight() - 80);
		paint.setColor(Color.WHITE);paint.setTextSize(30);paint.setAntiAlias(true);
		can.drawText("made with RareEngine2", 0, 0, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		can.drawText("made with RareEngine2", 0, 0, paint);
		can.restore();

	}
	public void drawOwnWaterMark(Canvas can, String WaterMarkText, int outlineColor, int fillColor, float textSize) {
		can.save();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(8);
		float width = paint.measureText(WaterMarkText);
		can.translate(getWidth() / 2 - (width / 2), getHeight() - 80);
		paint.setColor(outlineColor);paint.setTextSize(textSize);paint.setAntiAlias(true);
		can.drawText(WaterMarkText, 0, 0, paint);
		paint.setColor(fillColor);
		paint.setStyle(Paint.Style.FILL);
		can.drawText(WaterMarkText, 0, 0, paint);
		can.restore();

	}
	public void removeWaterMark(String password) {
		if (password.equals(GameView.REMOVE_WATERMARK_PASS)) {
			drawWaterMark = false;
		} else {
			drawWaterMark = true;
		}
	}

	public void findPositions(MotionEvent e) {
		touchPositionui.set(e.getX() - (getWidth() / 2), -(e.getY() - (getHeight() / 2)));
		touchPosition.set(e.getX() - (getWidth() / 2) + currentScene.CameraPosition.getX(), -(e.getY() - (getHeight() / 2) + currentScene.CameraPosition.getY()));
	}


	public void onUp(MotionEvent event) {
		for (GameObject object:currentScene.objects) {
			object.event = event;
			if (object.getLayer().equals("ui")) {
				if (isPointInsideRect(getRectFOfObjectUi(object), touchPositionui)) {
					object.isUp = true;
					object.isDown = false;
					object.isDrag = false;
					dragValueUi.set(0, 0);

				} else {
					allup(object);
				}
			} else {
				if (isPointInsideRect(getRectFOfObject(object), touchPosition)) {
					object.isUp = true;
					object.isDown = false;
					object.isDrag = false;
					dragValue.set(0, 0);
				} else {
					allup(object);
				}
			}
		}
	}
	public void allup(GameObject object) {
		object.isDown = false;
		object.isDrag = false;
		object.isUp = true;
		object.isClick = false;
	}
	public void onDown(MotionEvent event) {
		for (GameObject object:currentScene.objects) {
			object.event = event;
			if (object.getLayer().equals("ui")) {
				if (isPointInsideRect(getRectFOfObjectUi(object), touchPositionui)) {
					object.isDown = true;
					object.isUp = false;
					object.isClick = false;
					object.touchPosition.set(touchPositionui.getX(), touchPositionui.getY());
				} else {
					allup(object);
				}
			} else {
				if (isPointInsideRect(getRectFOfObject(object), touchPosition)) {
					object.isDown = true;
					object.isUp = false;
					object.isClick = false;
					object.touchPosition.set(touchPosition.getX(), touchPosition.getY());
				} else {
					allup(object);
				}
			}
		}
	}
	public void setScene(Scene s) {
		currentScene = s;

	}
	public Scene getScene() {
		return currentScene;
	}
	public void onDrag(MotionEvent event) {
		for (GameObject object:currentScene.objects) {
			object.event = event;
			if (object.getLayer().equals("ui")) {
				if (isPointInsideRect(getRectFOfObjectUi(object), touchPositionui)) {
					object.isDrag = true;
					object.isUp = false;
					//object.isClick = false; 
					object.touchPosition.set(touchPositionui.getX(), touchPositionui.getY());
					object.dragValue.set(dragValueUi.getX(), dragValueUi.getY());
				} else {
					allup(object);
				}
			} else {
				if (isPointInsideRect(getRectFOfObject(object), touchPosition)) {
					object.isDrag = true;
					object.isUp = false;
					//object.isClick = false;
					object.touchPosition.set(touchPosition.getX(), touchPosition.getY());
					object.dragValue.set(dragValue.getX(), dragValue.getY());
				} else {
					allup(object);
				}
			}
		}
	}
	public void onClick(MotionEvent event) {
		for (GameObject object:currentScene.objects) {
			object.event = event;
			if (object.getLayer().equals("ui")) {
				if (isPointInsideRect(getRectFOfObjectUi(object), touchPositionui)) {
					object.isClick = true;
					object.isDrag = false;
					object.isDown = false;

				} else {
					allup(object);
				}
			} else {
				if (isPointInsideRect(getRectFOfObject(object), touchPosition)) {
					object.isClick = true;
					object.isDrag = false;
					object.isDown = false;

				} else {
					allup(object);
				}
			}
		}
	}
	public Vector2 invertY(Vector2 vec) {
		Vector2 vec2 = new Vector2(vec);
		vec2.setY(-vec2.getY());
		return vec2;
	}
	public RectF getRectFWithScaleAndPositionUi(Vector2 p, Vector2 s) {
		return new RectF(p.getX() - s.getX() / 2, p.getY() - s.getY() / 2, p.getX() + s.getX() / 2, p.getY() + s.getY() / 2);
	}
	public RectF getRectFOfObjectUi(GameObject o) {
		return getRectFWithScaleAndPositionUi(o.globalposition, o.globalscale);
	}
	public boolean isPointInsideRect(RectF r, Vector2 point) {
		return r.contains(point.getX(), point.getY());
	}
	public RectF getRectFWithScaleAndPosition(Vector2 p, Vector2 s) {
		return new RectF(((p.getX()) - (s.getX()) / 2)*currentScene.getZoom(), ((p.getY()) - (s.getY()) / 2)*currentScene.getZoom(), ((p.getX()) + (s.getX()) / 2)*currentScene.getZoom(),( (p.getY()) + (s.getY()) / 2)*currentScene.getZoom());
	}
	public RectF getRectFOfObject(GameObject o) {
		return getRectFWithScaleAndPosition(o.globalposition, o.globalscale);
	}
	@Override
	public void surfaceChanged(SurfaceHolder sh, int p2, int p3, int p4) {
		Canvas can = sh.lockCanvas();
		update(can);
		if (can != null)sh.unlockCanvasAndPost(can);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder sh) {
		paused = true;
		h.removeCallbacksAndMessages(null);
		thread.stop();
		thread.destroy();
		thread.quitSafely();
		destroyed = true;


	}
	public void addObject(GameObject o) {
		currentScene.objects.add(o);
	}
	public void removeObject(GameObject o) {
		currentScene.objects.remove(o);
	}
	public void removeObjectWithName(String name) {
		for (GameObject o:currentScene.objects) {
			if (o.getName().equals(name)) {
				currentScene.objects.remove(o);
				break;
			}
		}
	}
	private void update(Canvas canvas) {
		frames++;

		if (frames == 1) {
			start(frames);
			if (comp != null)comp.onStart(this);
			if (currentScene != null && currentScene.getSceneComponent() != null)currentScene.getSceneComponent().onStart(this);
		}
		if (comp != null && !paused) {
			comp.update(this, canvas, paint);
		} else if (comp != null) {
			comp.paused(this);
		}
		if (currentScene.getSceneComponent() != null && !paused) {
			currentScene.getSceneComponent().update(this, canvas, paint); 

		} else if (currentScene.getSceneComponent() != null) {
			currentScene.getSceneComponent().paused(this);
		}
		Log.d("update", "working update");

		for (GameObject object:currentScene.objects) {
			object.calculateGlobals();
			for (String layer : layers) {
				Log.d("layercheck", "reached layer check witb number of objects:" + currentScene.objects.size());
				if (!object.getLayer().equals("ui") && layer.equals(object.getLayer()))render(canvas, object);
			}
		}

		for (GameObject object:currentScene.objects) {
			if (object.getLayer().equals("ui"))render(canvas, object);
		}
	}
	public void setGameViewComponent(GameViewComponent gvc) {
		comp = gvc;
	}
	public GameViewComponent getGameViewComponent() {
		return comp;
	}
	public void render(Canvas canvas, GameObject object) {
		Log.d("render", "working render");
		if (object.isEnabled && !paused) {

			for (Component component:object.getComponents()) {
				Log.d("updatecomp", "working updatecomp");
				if (component.isEnabled)component.update(object, this);
			}
		} else {
			for (Component component:object.getComponents()) {
				Log.d("pausedcomp", "working pausedcomp");
				if (component.isEnabled)component.paused(object, this);
			}
		}
        //canvas.drawColor(currentScene.backgroundcolor);
		for (Component component:object.getComponents()) {
			if (component instanceof Renderer) {
				if (component.isEnabled && object.isVisible && object.isEnabled) {
					int id=canvas.save();
					Vector2 cam = new Vector2(currentScene.CameraPosition);
					Log.d("rendercomp", "working rendercomp");
					if (!object.getLayer().equals("ui")) {
						
						//canvas.translate((getWidth() / 2) ,(getHeight()/2));
						//canvas.scale(currentScene.getZoom(), currentScene.getZoom(), (getWidth() / 2) + currentScene.getPinchPoint().getX(), (getHeight() / 2) + currentScene.getPinchPoint().getY());
						object.globalposition.multiply(currentScene.getZoom());
						object.scale.multiply(currentScene.getZoom());
						currentScene.CameraPosition.multiply(currentScene.getZoom());
						canvas.translate((getWidth() / 2) + object.globalposition.getX() - cam.getX(), (getHeight() / 2) - object.globalposition.getY() - cam.getY());
						canvas.rotate(object.globalrotation);
						((Renderer)component).render(canvas, object, paint, this);
						canvas.restoreToCount(id);
						currentScene.CameraPosition.divide(currentScene.getZoom());
						object.globalposition.divide(currentScene.getZoom());
						object.scale.divide(currentScene.getZoom());
					} else {
						canvas.translate((getWidth() / 2) + object.globalposition.getX(), (getHeight() / 2) - object.globalposition.getY());
						canvas.rotate(object.globalrotation);
						((Renderer)component).render(canvas, object, paint, this);
						canvas.restoreToCount(id);
					}
				}
			}
		}
	}
	private void start(long frames) {

	}
	public void toast(String text, int length) {
		Toast.makeText(getContext(), text, length).show();
	}
	public void copy(String text) {
		ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

		// Copy the exception message to the clipboard
		if (clipboard != null) {
			android.content.ClipData clip = android.content.ClipData.newPlainText("text", text);
			clipboard.setPrimaryClip(clip);
		}
	}
	public GameView(Context context, Activity activity) {
		super(context);

		currentScene = new Scene();
		if (activity.getActionBar() != null)activity.getActionBar().hide();
		if (activity.getWindow() != null)activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getHolder().addCallback(this);
		this.activity = activity;
		setFocusable(true);
		setFocusableInTouchMode(true);
		layers.add("background");
		layers.add("objects");
		layers.add("ui");
		setOnTouchListener(new SurfaceView.OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if (isUp) {
								isClick = true;
								onClick(event);
							}
							findPositions(event);
							isDown = true;
							isUp = false;
							//isClick = false;
							//isDrag = false;
							touchmilli = (int) System.currentTimeMillis();
							touchdist.set(event.getX(), -event.getY());
							findPositions(event);
							onDown(event);
							break;
						case MotionEvent.ACTION_UP:
							isUp = true;
							isDrag = false;
							dragValue.set(0, 0);
							dragValueUi.set(0, 0);
							if (isDown) {
								if (((System.currentTimeMillis() - touchmilli) <= 200) && touchdist != null) {
									if (touchdist.distance(new Vector2(event.getX(), -event.getY())) <= 50) {
										isClick = true;
										onClick(event);
									} else isClick = false;
								} else isClick = false;
							} else isClick = false;
							isDown = false;
							touchmilli = 0;
							touchdist.set(0, 0);
							onUp(event);
							break;
						case MotionEvent.ACTION_MOVE:
							isDrag = true;
							isClick = false;
							isUp = false;
							Vector2 prevpos = new Vector2(touchPosition);
							Vector2 prevposui = new Vector2(touchPositionui);
							findPositions(event);
							Vector2 nowpos = new Vector2(touchPosition);
							Vector2 nowposui = new Vector2(touchPositionui);
							dragValue = nowpos.subtract(prevpos);
							dragValueUi = nowposui.subtract(prevposui);
							onDrag(event);
							break;
					}
					return true;
				}
			});

	}
	public Activity getActivity() {
		return activity;
	}
	public void pause() {
		paused = true;
		try {
			thread.stop(); // wait for the thread to finish
		} catch (Exception e) {

		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			resume();
		} else {
			pause();
		}

	}

	public void resume() {
		paused = false;

	}

}
