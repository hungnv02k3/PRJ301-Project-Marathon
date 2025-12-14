package controllers;

import dal.EventDAO;
import dal.OrganizerDAO;
import dal.RegistrationDAO;
import dal.ResultDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Event;
import models.Organizer;

/**
 *
 * @author THINKPAD
 */
@WebServlet({
    "/organizer/dashboard",
    "/organizer/events",
    "/organizer/events/add",
    "/organizer/events/edit",
    "/organizer/events/delete",
    "/organizer/events/close",
    "/organizer/registrations",
    "/organizer/registrations/approve",
    "/organizer/registrations/reject",
    "/organizer/registrations/assign-bib",
    "/organizer/registrations/result",
    "/organizer/registrations/result/save"
})
public class OrganizerController extends HttpServlet {
   
    private EventDAO eventDAO = new EventDAO();
    private OrganizerDAO organizerDAO = new OrganizerDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private ResultDAO resultDAO = new ResultDAO();
    
    private static final String ORGANIZER_DASHBOARD_JSP = "/views/organizer/dashboard.jsp";
    private static final String ORGANIZER_EVENTS_JSP = "/views/organizer/events/list.jsp";
    private static final String ORGANIZER_EVENT_FORM_JSP = "/views/organizer/events/form.jsp";
    private static final String ORGANIZER_REGISTRATIONS_JSP = "/views/organizer/registrations/list.jsp";
    private static final String ORGANIZER_RESULT_FORM_JSP = "/views/organizer/registrations/result-form.jsp";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int organizerId = 1;
        
        String action = request.getServletPath();
        
        try {
            switch (action) {
                case "/organizer/dashboard":
                    showDashboard(request, response, organizerId);
                    break;
                case "/organizer/events":
                    showEventsList(request, response, organizerId);
                    break;
                case "/organizer/events/add":
                    showEventForm(request, response, null);
                    break;
                case "/organizer/events/edit":
                    showEventForm(request, response, request.getParameter("id"));
                    break;
                case "/organizer/registrations":
                    showRegistrationsList(request, response, request.getParameter("eventId"));
                    break;
                case "/organizer/registrations/result":
                    showResultForm(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/organizer/dashboard");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int organizerId = 1; 
        
        String action = request.getServletPath();
        
        try {
            switch (action) {
                case "/organizer/events/add":
                    createEvent(request, response, organizerId);
                    break;
                case "/organizer/events/edit":
                    updateEvent(request, response, organizerId);
                    break;
                case "/organizer/events/delete":
                    deleteEvent(request, response);
                    break;
                case "/organizer/events/close":
                    closeEvent(request, response);
                    break;
                case "/organizer/registrations/approve":
                    approveRegistration(request, response);
                    break;
                case "/organizer/registrations/reject":
                    rejectRegistration(request, response);
                    break;
                case "/organizer/registrations/assign-bib":
                    assignBibNumber(request, response);
                    break;
                case "/organizer/registrations/result/save":
                    saveResult(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/organizer/dashboard");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    // GET Methods
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, int organizerId)
            throws ServletException, IOException {
        try {           
            List<Event> events = eventDAO.getEventsByOrganizer(organizerId);
            request.setAttribute("events", events);
            request.setAttribute("totalEvents", events.size());
            
            int totalRegistrations = 0;
            for (Event event : events) {
                totalRegistrations += eventDAO.getRegistrationCountByEvent(event.getEventId());
            }
            request.setAttribute("totalRegistrations", totalRegistrations);
            
            request.getRequestDispatcher(ORGANIZER_DASHBOARD_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showEventsList(HttpServletRequest request, HttpServletResponse response, int organizerId)
            throws ServletException, IOException {
        try {
            java.util.List<Event> events = eventDAO.getEventsByOrganizer(organizerId);
            request.setAttribute("events", events);
            request.getRequestDispatcher(ORGANIZER_EVENTS_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showEventForm(HttpServletRequest request, HttpServletResponse response, String eventIdParam)
            throws ServletException, IOException {
        try {
            Event event = null;
            if (eventIdParam != null && !eventIdParam.isEmpty()) {
                int eventId = Integer.parseInt(eventIdParam);
                event = eventDAO.getEventById(eventId);
                
                if (event != null && event.hasStarted()) {
                    request.setAttribute("error", "Không thể sửa giải chạy đã bắt đầu!");
                    response.sendRedirect(request.getContextPath() + "/organizer/events");
                    return;
                }
            }
            request.setAttribute("event", event);
            request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showRegistrationsList(HttpServletRequest request, HttpServletResponse response, String eventIdParam)
            throws ServletException, IOException {
        try {
            if (eventIdParam == null || eventIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = Integer.parseInt(eventIdParam);
            Event event = eventDAO.getEventById(eventId);
            if (event == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            java.util.List<models.Registration> registrations = registrationDAO.getRegistrationsByEvent(eventId);
            request.setAttribute("event", event);
            request.setAttribute("registrations", registrations);
            request.getRequestDispatcher(ORGANIZER_REGISTRATIONS_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    // POST Methods
    private void createEvent(HttpServletRequest request, HttpServletResponse response, int organizerId)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String eventStartTimeStr = request.getParameter("eventStartTime");
            String location = request.getParameter("location");
            String maxParticipantsStr = request.getParameter("maxParticipants");
            String registrationDeadlineStr = request.getParameter("registrationDeadline");
            String status = request.getParameter("status");
            
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Event name cannot be empty!");
                request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
                return;
            }
            
            if (eventStartTimeStr == null || eventStartTimeStr.isEmpty()) {
                request.setAttribute("error", "Event Start Time is required!");
                request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
                return;
            }
            
            if (registrationDeadlineStr == null || registrationDeadlineStr.isEmpty()) {
                request.setAttribute("error", "Registration Deadline is required!");
                request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
                return;
            }
            
            // Parse event start time (ISO format: yyyy-MM-ddTHH:mm:ss)
            LocalDateTime eventStartLocalDateTime = LocalDateTime.parse(eventStartTimeStr);
            Timestamp eventStartTime = Timestamp.valueOf(eventStartLocalDateTime);
            
            // Parse registration deadline (ISO format: yyyy-MM-ddTHH:mm:ss)
            LocalDateTime registrationDeadlineLocalDateTime = LocalDateTime.parse(registrationDeadlineStr);
            Timestamp registrationDeadlineTimestamp = Timestamp.valueOf(registrationDeadlineLocalDateTime);
            
            if (registrationDeadlineTimestamp.after(eventStartTime)) {
                request.setAttribute("error", "Registration deadline must be before or equal to Event Start Time!");
                request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
                return;
            }
            
            Date eventDate = new Date(eventStartTime.getTime());
            
            Event event = new Event();
            event.setOrganizerId(organizerId);
            event.setName(name);
            event.setDescription(description);
            event.setEventDate(eventDate);
            event.setEventStartTime(eventStartTime);
            event.setLocation(location);
            event.setMaxParticipants(Integer.parseInt(maxParticipantsStr));
            event.setRegistrationDeadline(new Date(registrationDeadlineTimestamp.getTime()));
            event.setStatus(status != null ? status : "Open");
            
            eventDAO.createEvent(event);
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating event: " + e.getMessage());
            request.getRequestDispatcher(ORGANIZER_EVENT_FORM_JSP).forward(request, response);
        }
    }
    
    private void updateEvent(HttpServletRequest request, HttpServletResponse response, int organizerId)
            throws ServletException, IOException {
        try {
            String eventIdStr = request.getParameter("eventId");
            if (eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = Integer.parseInt(eventIdStr);
            Event event = eventDAO.getEventById(eventId);
            
            if (event != null && event.hasStarted()) {
                request.setAttribute("error", "Không thể sửa giải chạy đã bắt đầu!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            if (event == null || event.getOrganizerId() != organizerId) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String eventStartTimeStr = request.getParameter("eventStartTime");
            String location = request.getParameter("location");
            String maxParticipantsStr = request.getParameter("maxParticipants");
            String registrationDeadlineStr = request.getParameter("registrationDeadline");
            String status = request.getParameter("status");
            
            if (eventStartTimeStr == null || eventStartTimeStr.isEmpty()) {
                request.setAttribute("error", "Event Start Time is required!");
                response.sendRedirect(request.getContextPath() + "/organizer/events/edit?id=" + eventId);
                return;
            }
            
            if (registrationDeadlineStr == null || registrationDeadlineStr.isEmpty()) {
                request.setAttribute("error", "Registration Deadline is required!");
                response.sendRedirect(request.getContextPath() + "/organizer/events/edit?id=" + eventId);
                return;
            }
            
            LocalDateTime eventStartLocalDateTime = LocalDateTime.parse(eventStartTimeStr);
            Timestamp eventStartTime = Timestamp.valueOf(eventStartLocalDateTime);
            
            LocalDateTime registrationDeadlineLocalDateTime = LocalDateTime.parse(registrationDeadlineStr);
            Timestamp registrationDeadlineTimestamp = Timestamp.valueOf(registrationDeadlineLocalDateTime);
            
            if (registrationDeadlineTimestamp.after(eventStartTime)) {
                request.setAttribute("error", "Registration deadline must be before or equal to Event Start Time!");
                response.sendRedirect(request.getContextPath() + "/organizer/events/edit?id=" + eventId);
                return;
            }
            
            Date eventDate = new Date(eventStartTime.getTime());
            
            event.setName(name);
            event.setDescription(description);
            event.setEventDate(eventDate);
            event.setEventStartTime(eventStartTime);
            event.setLocation(location);
            event.setMaxParticipants(Integer.parseInt(maxParticipantsStr));
            // Convert Timestamp to Date for registration deadline (store only date part)
            event.setRegistrationDeadline(new Date(registrationDeadlineTimestamp.getTime()));
            event.setStatus(status);
            
            eventDAO.updateEvent(event);
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi cập nhật sự kiện: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events/edit?id=" + request.getParameter("eventId"));
        }
    }
    
    private void deleteEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String eventIdStr = request.getParameter("eventId");
            if (eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = Integer.parseInt(eventIdStr);
            Event event = eventDAO.getEventById(eventId);
            
            if (event != null && event.hasStarted()) {
                request.setAttribute("error", "Không thể xóa giải chạy đã bắt đầu!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            eventDAO.deleteEvent(eventId);
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xóa sự kiện: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void closeEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String eventIdStr = request.getParameter("eventId");
            if (eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = Integer.parseInt(eventIdStr);
            eventDAO.closeEvent(eventId);
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đóng sự kiện: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void approveRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String registrationIdStr = request.getParameter("registrationId");
            String eventIdStr = request.getParameter("eventId");
            
            if (registrationIdStr == null || registrationIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int registrationId = Integer.parseInt(registrationIdStr);
            registrationDAO.approveRegistration(registrationId);
            
            if (eventIdStr != null && !eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventIdStr);
            } else {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void rejectRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String registrationIdStr = request.getParameter("registrationId");
            String eventIdStr = request.getParameter("eventId");
            
            if (registrationIdStr == null || registrationIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int registrationId = Integer.parseInt(registrationIdStr);
            registrationDAO.rejectRegistration(registrationId);
            
            if (eventIdStr != null && !eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventIdStr);
            } else {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void assignBibNumber(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String registrationIdStr = request.getParameter("registrationId");
            String eventIdStr = request.getParameter("eventId");
            
            if (registrationIdStr == null || registrationIdStr.isEmpty() || eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int registrationId = Integer.parseInt(registrationIdStr);
            int eventId = Integer.parseInt(eventIdStr);
            
            Event event = eventDAO.getEventById(eventId);
            if (event != null && event.hasStarted()) {
                request.setAttribute("error", "Cannot assign bib number after event has started!");
                response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventId);
                return;
            }
            
            String bibNumber = registrationDAO.generateUniqueBibNumber(eventId);
            registrationDAO.assignBibNumber(registrationId, bibNumber);
            
            response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void showResultForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String registrationIdStr = request.getParameter("registrationId");
            String eventIdStr = request.getParameter("eventId");
            
            if (registrationIdStr == null || registrationIdStr.isEmpty() || eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int registrationId = Integer.parseInt(registrationIdStr);
            int eventId = Integer.parseInt(eventIdStr);
            
            Event event = eventDAO.getEventById(eventId);
            if (event == null || !event.hasStarted()) {
                request.setAttribute("error", "Event has not started yet!");
                response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventId);
                return;
            }
            
            models.Registration registration = registrationDAO.getRegistrationById(registrationId);
            models.Result result = resultDAO.getResultByRegistration(registrationId);
            
            request.setAttribute("event", event);
            request.setAttribute("registration", registration);
            request.setAttribute("result", result);
            request.getRequestDispatcher(ORGANIZER_RESULT_FORM_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void saveResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String registrationIdStr = request.getParameter("registrationId");
            String eventIdStr = request.getParameter("eventId");
            String finishTimeStr = request.getParameter("finishTime");
            
            if (registrationIdStr == null || registrationIdStr.isEmpty() || 
                eventIdStr == null || eventIdStr.isEmpty() ||
                finishTimeStr == null || finishTimeStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int registrationId = Integer.parseInt(registrationIdStr);
            int eventId = Integer.parseInt(eventIdStr);
            
            Event event = eventDAO.getEventById(eventId);
            if (event == null || !event.hasStarted()) {
                request.setAttribute("error", "Event has not started yet!");
                response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventId);
                return;
            }
            
            Timestamp finishTime;
            if (finishTimeStr.contains("T")) {
                String timePart = finishTimeStr.split("T")[1];
                if (timePart.split(":").length == 2) {
                    finishTime = Timestamp.valueOf(finishTimeStr.replace("T", " ") + ":00");
                } else {
                    finishTime = Timestamp.valueOf(finishTimeStr.replace("T", " "));
                }
            } else {
                finishTime = Timestamp.valueOf(finishTimeStr);
            }
            
            Timestamp startTime = event.getEventStartTime();
            if (startTime == null && event.getEventDate() != null) {
                startTime = new Timestamp(event.getEventDate().getTime());
            }
            
            if (startTime == null) {
                request.setAttribute("error", "Event start time is not set!");
                response.sendRedirect(request.getContextPath() + "/organizer/registrations/result?registrationId=" + registrationId + "&eventId=" + eventId);
                return;
            }
            
            if (finishTime.before(startTime)) {
                request.setAttribute("error", "Finish time must be after start time!");
                response.sendRedirect(request.getContextPath() + "/organizer/registrations/result?registrationId=" + registrationId + "&eventId=" + eventId);
                return;
            }
            
            resultDAO.createOrUpdateResult(registrationId, finishTime);
            response.sendRedirect(request.getContextPath() + "/organizer/registrations?eventId=" + eventId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error saving result: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
}
