package models;

/**
 *
 * @author THINKPAD
 */
public class Route {
    private int routeId;
    private int eventId;
    private double distanceKm;
    private String description;
    
    private String eventName;
    private int checkpointCount;

    public Route() {
    }

    public Route(int routeId, int eventId, double distanceKm, String description) {
        this.routeId = routeId;
        this.eventId = eventId;
        this.distanceKm = distanceKm;
        this.description = description;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getCheckpointCount() {
        return checkpointCount;
    }

    public void setCheckpointCount(int checkpointCount) {
        this.checkpointCount = checkpointCount;
    }
}

