package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Route;

/**
 *
 * @author THINKPAD
 */
public class RouteDAO extends DBContext {
    
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<Route> getRoutesByEvent(int eventId) {
        List<Route> list = new ArrayList<>();
        try {
            String sqlStatement = "SELECT r.*, e.name as event_name, "
                                + "(SELECT COUNT(*) FROM Checkpoints WHERE route_id = r.route_id) as checkpoint_count "
                                + "FROM Routes r "
                                + "INNER JOIN Events e ON r.event_id = e.event_id "
                                + "WHERE r.event_id = ? ORDER BY r.route_id DESC";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int routeId = rs.getInt("route_id");
                int evtId = rs.getInt("event_id");
                double distanceKm = rs.getDouble("distance_km");
                String description = rs.getString("description");
                
                Route route = new Route(routeId, evtId, distanceKm, description);
                route.setEventName(rs.getString("event_name"));
                route.setCheckpointCount(rs.getInt("checkpoint_count"));
                list.add(route);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public Route getRouteById(int routeId) {
        Route route = null;
        try {
            String sqlStatement = "SELECT r.*, e.name as event_name, "
                                + "(SELECT COUNT(*) FROM Checkpoints WHERE route_id = r.route_id) as checkpoint_count "
                                + "FROM Routes r "
                                + "INNER JOIN Events e ON r.event_id = e.event_id "
                                + "WHERE r.route_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, routeId);
            rs = stm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("route_id");
                int eventId = rs.getInt("event_id");
                double distanceKm = rs.getDouble("distance_km");
                String description = rs.getString("description");
                
                route = new Route(id, eventId, distanceKm, description);
                route.setEventName(rs.getString("event_name"));
                route.setCheckpointCount(rs.getInt("checkpoint_count"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return route;
    }
    
    public int createRoute(Route route) {
        try {
            String sqlStatement = "INSERT INTO Routes (event_id, distance_km, description) VALUES (?, ?, ?)";
            stm = connection.prepareStatement(sqlStatement, java.sql.Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, route.getEventId());
            stm.setDouble(2, route.getDistanceKm());
            stm.setString(3, route.getDescription());
            
            stm.executeUpdate();
            
            // Get generated route_id
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int routeId = generatedKeys.getInt(1);
                route.setRouteId(routeId);
                return routeId;
            }
        } catch (Exception e) {
            System.out.println("Error creating route: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public void updateRoute(Route route) {
        try {
            String sqlStatement = "UPDATE Routes SET distance_km = ?, description = ? WHERE route_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setDouble(1, route.getDistanceKm());
            stm.setString(2, route.getDescription());
            stm.setInt(3, route.getRouteId());
            
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating route: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean deleteRoute(int routeId) {
        try {
            String deleteCheckpointsSql = "DELETE FROM Checkpoints WHERE route_id = ?";
            PreparedStatement deleteCheckpointsStm = connection.prepareStatement(deleteCheckpointsSql);
            deleteCheckpointsStm.setInt(1, routeId);
            deleteCheckpointsStm.executeUpdate();
            
            String sqlStatement = "DELETE FROM Routes WHERE route_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, routeId);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error deleting route: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateRouteCoordinates(int routeId, Double startLat, Double startLng, Double endLat, Double endLng) {
        try {
            String sqlStatement = "UPDATE Routes SET start_latitude = ?, start_longitude = ?, end_latitude = ?, end_longitude = ? WHERE route_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setObject(1, startLat);
            stm.setObject(2, startLng);
            stm.setObject(3, endLat);
            stm.setObject(4, endLng);
            stm.setInt(5, routeId);
            stm.executeUpdate();
        } catch (Exception e) {
            // Columns might not exist, ignore
            System.out.println("Note: Coordinate columns may not exist: " + e.getMessage());
        }
    }
}

