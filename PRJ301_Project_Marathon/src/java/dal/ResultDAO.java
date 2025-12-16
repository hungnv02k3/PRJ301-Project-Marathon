/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Result;

/**
 *
 * @author User
 */
public class ResultDAO extends DBContext {

    private PreparedStatement stm;
    private ResultSet rs;

    public List<Result> getResultsByRunner(int runnerId) {
        List<Result> list = new ArrayList<>();

        String sql
                = "SELECT r.result_id, r.registration_id, r.start_time, r.finish_time, "
                + "r.net_time, r.ranking_overall, e.event_name, e.event_date, e.location "
                + "FROM Results r "
                + "JOIN Registrations reg ON r.registration_id = reg.registration_id "
                + "JOIN Events e ON reg.event_id = e.event_id "
                + "WHERE reg.runner_id = ? "
                + "ORDER BY e.event_date DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, runnerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Result re = new Result();
                re.setResultId(rs.getInt("result_id"));
                re.setRegistrationId(rs.getInt("registration_id"));
                re.setStartTime(rs.getTimestamp("start_time"));
                re.setFinishTime(rs.getTimestamp("finish_time"));
                re.setNetTime(rs.getInt("net_time"));
                re.setRankingOverall(rs.getInt("ranking_overall"));

                re.setEventName(rs.getString("event_name"));
                re.setEventDate(rs.getDate("event_date"));
                re.setLocation(rs.getString("location"));

                list.add(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
