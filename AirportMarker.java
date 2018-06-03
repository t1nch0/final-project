package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	protected boolean clicked = false;
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	public boolean getClicked() {
		return clicked;
	}
	
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(150, 30, 30);
		pg.ellipse(x, y, 5, 5);
		
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		String tit=getName();
		pg.pushStyle();
		pg.rectMode(PConstants.CORNER);
		pg.fill(255,255,255);
		pg.rect(x, y+15, pg.textWidth(tit)+6, 18);
		pg.fill(0);
		pg.textAlign(PConstants.LEFT,PConstants.TOP);
		pg.text(tit, x+3, y+18);
		pg.popStyle();
//		 show routes
		
		
	}

	private String getName() {
		// TODO Auto-generated method stub
		return (String)getProperty("name");
	}
	
}
