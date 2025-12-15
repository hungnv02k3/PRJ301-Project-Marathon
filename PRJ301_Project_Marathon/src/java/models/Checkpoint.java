package models;

/**
 *
 * @author THINKPAD
 */
public class Checkpoint {
    private int cpId;
    private int routeId;
    private String cpName;
    private int sequenceOrder;
    
    // Additional fields for map display (stored as JSON in description or separate fields)
    private Double latitude;
    private Double longitude;
    
    // Additional fields for display
    private String routeName;

    public Checkpoint() {
    }

    public Checkpoint(int cpId, int routeId, String cpName, int sequenceOrder) {
        this.cpId = cpId;
        this.routeId = routeId;
        this.cpName = cpName;
        this.sequenceOrder = sequenceOrder;
    }

    public int getCpId() {
        return cpId;
    }

    public void setCpId(int cpId) {
        this.cpId = cpId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}

