package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	List<Feature> countries;
	List<Marker> countryMarkers;
	List<Marker> airporcity;
	private Marker lastSelected;
	private Marker lastClicked;
	HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
	List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
	List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
//			routeList.add(sl);
		}
		
		
//		for (Marker ahide : airportList) {
//			System.out.println(ahide.getProperties());
//			
//		}
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		paintcountry();
		showroutes();
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkclick();
			if (lastClicked == null) {
				checkclick();
			}
		}
	}
	private void checkclick() {
		if (lastClicked != null) return;
		for (Marker m : countryMarkers) {			
		if (!m.isHidden()&&m.isInside(map, mouseX, mouseY)) {
			lastClicked =m;	
			System.out.println(m.getProperty("name"));
			for (Marker mhide : countryMarkers) {
				if (mhide != lastClicked) {
					mhide.setHidden(true);
				}
			}
//			for (int i = 0; i < airports.size(); i++) {
//				System.out.println(airportList);
//			}
			for (Marker dd : airportList) {
				ArrayList<String> fina = new ArrayList<String>();
				String pp=(String) dd.getProperty("country");
				String rr=quitarapostr(pp);
				
				
//				System.out.println(dd.getProperty("name"));
				
			if (!rr.equals(m.getProperty("name"))) {
				fina.add(rr);
				dd.setHidden(true);
				System.out.println();
//				System.out.println(lastClicked.getProperty("name")+" aeroport "+dd.getProperty("name"));
//				System.out.println("yeah!!");
			}
			}
//			System.out.println(m.getProperties());
			}
		
		}


	}
	private String quitarapostr(String cad) {
		cad=cad.replaceAll("\"", "");
//		System.out.println(cad);
		return cad;
	}
	
	private void showroutes() {
		// TODO Auto-generated method stub
		
//		m.setHidden(false);
		for (Marker m3 :  countryMarkers) {
			String paisID = m3.getId();
			if (airportList.contains(paisID)) {
				m3.setHidden(true);
				System.out.println(m3);
			}
		
		}
	}

	private void unhideMarkers() {
		
		for(Marker marker : countryMarkers) {
			marker.setHidden(false);
			
		}
			
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}
	public void paintcountry() {
		for (Marker m: countryMarkers) {
//			String paisID = m.getId();
//			System.out.println(paisID);
//			if (airportList.contains(paisID)) {
//				m.setColor(color(0,255,0));
//			}
//			else {
				m.setColor(color(255,255,0));
//			}
			
		}
	}
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
//		selectMarkerIfHover(cityMarkers);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

}
