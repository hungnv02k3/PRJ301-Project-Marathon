package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Checkpoint;

/**
 *
 * @author THINKPAD
 */
public class CheckpointDAO extends DBContext {
    
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<Checkpoint> getCheckpointsByRoute(int routeId) {
        List<Checkpoint> list = new ArrayList<>();
        try {
            String sqlStatement = "SELECT c.*, r.description as route_name "
                                + "FROM Checkpoints c "
                                + "LEFT JOIN Routes r ON c.route_id = r.route_id "
                                + "WHERE c.route_id = ? ORDER BY c.sequence_order ASC";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, routeId);
            rs = stm.executeQuery();
            while (rs.next()) {
                int cpId = rs.getInt("checkpoint_id");
                int rtId = rs.getInt("route_id");
                String cpName = rs.getString("cp_name");
                if (cpName == null || cpName.isEmpty()) {
                    cpName = rs.getString("location");
                    if (cpName == null) {
                        cpName = "Checkpoint " + cpId;
                    }
                }
                int sequenceOrder = rs.getInt("sequence_order");
                if (rs.wasNull()) {
                    sequenceOrder = 1;
                }
                
                Checkpoint checkpoint = new Checkpoint(cpId, rtId, cpName, sequenceOrder);
                
                Double latitude = rs.getDouble("latitude");
                if (!rs.wasNull()) {
                    checkpoint.setLatitude(latitude);
                }
                Double longitude = rs.getDouble("longitude");
                if (!rs.wasNull()) {
                    checkpoint.setLongitude(longitude);
                }
                
                checkpoint.setRouteName(rs.getString("route_name"));
                list.add(checkpoint);
            }
        } catch (Exception e) {
        }
        return list;
    }
    
    public Checkpoint getCheckpointById(int cpId) {
        Checkpoint checkpoint = null;
        try {
            String sqlStatement = "SELECT c.*, r.description as route_name "
                                + "FROM Checkpoints c "
                                + "LEFT JOIN Routes r ON c.route_id = r.route_id "
                                + "WHERE c.checkpoint_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, cpId);
            rs = stm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("checkpoint_id");
                int routeId = rs.getInt("route_id");
                String cpName = rs.getString("cp_name");
                if (cpName == null || cpName.isEmpty()) {
                    cpName = rs.getString("location");
                    if (cpName == null) {
                        cpName = "Checkpoint " + id;
                    }
                }
                int sequenceOrder = rs.getInt("sequence_order");
                if (rs.wasNull()) {
                    sequenceOrder = 1;
                }
                
                checkpoint = new Checkpoint(id, routeId, cpName, sequenceOrder);
                
                Double latitude = rs.getDouble("latitude");
                if (!rs.wasNull()) {
                    checkpoint.setLatitude(latitude);
                }
                Double longitude = rs.getDouble("longitude");
                if (!rs.wasNull()) {
                    checkpoint.setLongitude(longitude);
                }
                
                checkpoint.setRouteName(rs.getString("route_name"));
            }
        } catch (Exception e) {
        }
        return checkpoint;
    }
    
    public void createCheckpoint(Checkpoint checkpoint) {
        try {
            String sqlStatement = "INSERT INTO Checkpoints (route_id, cp_name, sequence_order, latitude, longitude) "
                                + "VALUES (?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, checkpoint.getRouteId());
            stm.setString(2, checkpoint.getCpName());
            stm.setInt(3, checkpoint.getSequenceOrder());
            stm.setObject(4, checkpoint.getLatitude());
            stm.setObject(5, checkpoint.getLongitude());
            
            stm.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public void updateCheckpoint(Checkpoint checkpoint) {
        try {
            String sqlStatement = "UPDATE Checkpoints SET cp_name = ?, sequence_order = ?, latitude = ?, longitude = ? WHERE checkpoint_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setString(1, checkpoint.getCpName());
            stm.setInt(2, checkpoint.getSequenceOrder());
            stm.setObject(3, checkpoint.getLatitude());
            stm.setObject(4, checkpoint.getLongitude());
            stm.setInt(5, checkpoint.getCpId());
            
            stm.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public void deleteCheckpoint(int cpId) {
        try {
            String sqlStatement = "DELETE FROM Checkpoints WHERE checkpoint_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, cpId);
            stm.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public int getNextSequenceOrder(int routeId) {
        int nextOrder = 1;
        try {
            String sqlStatement = "SELECT MAX(sequence_order) as max_order FROM Checkpoints WHERE route_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            stm.setInt(1, routeId);
            rs = stm.executeQuery();
            if (rs.next() && !rs.wasNull()) {
                nextOrder = rs.getInt("max_order") + 1;
            }
        } catch (Exception e) {
        }
        return nextOrder;
    }
    
    public void reorderCheckpoints(int routeId) {
        try {
            List<Checkpoint> checkpoints = getCheckpointsByRoute(routeId);
            String sqlStatement = "UPDATE Checkpoints SET sequence_order = ? WHERE checkpoint_id = ?";
            stm = connection.prepareStatement(sqlStatement);
            
            int order = 1;
            for (Checkpoint cp : checkpoints) {
                stm.setInt(1, order);
                stm.setInt(2, cp.getCpId());
                stm.executeUpdate();
                order++;
            }
        } catch (Exception e) {
        }
    }
}

