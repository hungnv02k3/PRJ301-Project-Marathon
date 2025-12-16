package controllers;

import dal.CheckpointDAO;
import dal.EventDAO;
import dal.OrganizerDAO;
import dal.RegistrationDAO;
import dal.ResultDAO;
import dal.RouteDAO;
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
import models.Registration;
import models.Route;

/**
 *
 * @author THINKPAD
 */
@WebServlet({
    "/organizer/dashboard",
    "/organizer/events",
    "/organizer/events/add",
    "/organizer/events/edit",
    "/organizer/events/pause",
    "/organizer/events/close",
    "/organizer/registrations",
    "/organizer/registrations/approve",
    "/organizer/registrations/reject",
    "/organizer/registrations/assign-bib",
    "/organizer/registrations/result",
    "/organizer/registrations/result/save",
    "/organizer/routes",
    "/organizer/routes/add",
    "/organizer/routes/edit",
    "/organizer/routes/delete",
    "/organizer/routes/view",
    "/organizer/checkpoints",
    "/organizer/checkpoints/add",
    "/organizer/checkpoints/edit",
    "/organizer/checkpoints/delete"
})
public class OrganizerController extends HttpServlet {
   
    private EventDAO eventDAO = new EventDAO();
    private OrganizerDAO organizerDAO = new OrganizerDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private ResultDAO resultDAO = new ResultDAO();
    private RouteDAO routeDAO = new RouteDAO();
    private CheckpointDAO checkpointDAO = new CheckpointDAO();
    
    private static final String ORGANIZER_DASHBOARD_JSP = "/views/organizer/dashboard.jsp";
    private static final String ORGANIZER_EVENTS_JSP = "/views/organizer/events/list.jsp";
    private static final String ORGANIZER_EVENT_FORM_JSP = "/views/organizer/events/form.jsp";
    private static final String ORGANIZER_REGISTRATIONS_JSP = "/views/organizer/registrations/list.jsp";
    private static final String ORGANIZER_RESULT_FORM_JSP = "/views/organizer/registrations/result-form.jsp";
    private static final String ORGANIZER_ROUTES_JSP = "/views/organizer/routes/list.jsp";
    private static final String ORGANIZER_ROUTE_FORM_JSP = "/views/organizer/routes/form.jsp";
    private static final String ORGANIZER_ROUTE_VIEW_JSP = "/views/organizer/routes/view.jsp";
    private static final String ORGANIZER_CHECKPOINTS_JSP = "/views/organizer/checkpoints/list.jsp";
    private static final String ORGANIZER_CHECKPOINT_FORM_JSP = "/views/organizer/checkpoints/form.jsp";
    
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
                case "/organizer/routes":
                    showRoutesList(request, response, request.getParameter("eventId"));
                    break;
                case "/organizer/routes/add":
                    showRouteForm(request, response, null, request.getParameter("eventId"));
                    break;
                case "/organizer/routes/edit":
                    showRouteForm(request, response, request.getParameter("id"), null);
                    break;
                case "/organizer/routes/view":
                    showRouteView(request, response, request.getParameter("id"));
                    break;
                case "/organizer/checkpoints":
                    showCheckpointsList(request, response, request.getParameter("routeId"));
                    break;
                case "/organizer/checkpoints/add":
                    showCheckpointForm(request, response, null, request.getParameter("routeId"));
                    break;
                case "/organizer/checkpoints/edit":
                    showCheckpointForm(request, response, request.getParameter("id"), null);
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
                case "/organizer/events/pause":
                    pauseEvent(request, response);
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
                case "/organizer/routes/add":
                    createRoute(request, response);
                    break;
                case "/organizer/routes/edit":
                    updateRoute(request, response);
                    break;
                case "/organizer/routes/delete":
                    deleteRoute(request, response);
                    break;
                case "/organizer/checkpoints/add":
                    createCheckpoint(request, response);
                    break;
                case "/organizer/checkpoints/edit":
                    updateCheckpoint(request, response);
                    break;
                case "/organizer/checkpoints/delete":
                    deleteCheckpoint(request, response);
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
            List<Event> events = eventDAO.getEventsByOrganizer(organizerId);
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
                    request.setAttribute("error", "Cannot edit started event!");
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
            
            List<Registration> registrations = registrationDAO.getRegistrationsByEvent(eventId);
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
            event.setEventName(name);
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
                request.setAttribute("error", "Cannot edit started events!");
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
            
            event.setEventName(name);
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
            request.setAttribute("error", "Error when update event: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events/edit?id=" + request.getParameter("eventId"));
        }
    }
    
    private void pauseEvent(HttpServletRequest request, HttpServletResponse response)
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
                request.setAttribute("error", "Cannot pause started event!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            eventDAO.pauseEvent(eventId);
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error when pausing event: " + e.getMessage());
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
            request.setAttribute("error", "Error when close event: " + e.getMessage());
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
            
            // - If format is yyyy-MM-dd HH:mm (no seconds), append :00
            String normalizedFinish = finishTimeStr.replace("T", " ");
            if (normalizedFinish.length() == 16) { // yyyy-MM-dd HH:mm
                normalizedFinish += ":00";
            }
            Timestamp finishTime = Timestamp.valueOf(normalizedFinish);
            
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
    
    // Route Management Methods
    private void showRoutesList(HttpServletRequest request, HttpServletResponse response, String eventIdStr)
            throws ServletException, IOException {
        try {
            if (eventIdStr == null || eventIdStr.isEmpty()) {
                response.sendRedirect("events");
                return;
            }
            int eventId = Integer.parseInt(eventIdStr);
            Event event = eventDAO.getEventById(eventId);
            if (event == null) {
                response.sendRedirect(request.getContextPath() + "events");
                return;
            }
            List<Route> routes = routeDAO.getRoutesByEvent(eventId);
            request.setAttribute("event", event);
            request.setAttribute("routes", routes);
            request.getRequestDispatcher(ORGANIZER_ROUTES_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showRouteForm(HttpServletRequest request, HttpServletResponse response, String routeIdStr, String eventIdStr)
            throws ServletException, IOException {
        try {
            Route route = null;
            Event event = null;
            
            if (routeIdStr != null && !routeIdStr.isEmpty()) {
                int routeId = Integer.parseInt(routeIdStr);
                route = routeDAO.getRouteById(routeId);
                if (route != null) {
                    event = eventDAO.getEventById(route.getEventId());
                }
            } else if (eventIdStr != null && !eventIdStr.isEmpty()) {
                int eventId = Integer.parseInt(eventIdStr);
                event = eventDAO.getEventById(eventId);
            }
            
            if (event == null) {
                response.sendRedirect("organizer/events");
                return;
            }
            
            request.setAttribute("route", route);
            request.setAttribute("event", event);
            request.getRequestDispatcher(ORGANIZER_ROUTE_FORM_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showRouteView(HttpServletRequest request, HttpServletResponse response, String routeIdStr)
            throws ServletException, IOException {
        try {
            if (routeIdStr == null || routeIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            int routeId = Integer.parseInt(routeIdStr);
            models.Route route = routeDAO.getRouteById(routeId);
            if (route == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            Event event = eventDAO.getEventById(route.getEventId());
            List<models.Checkpoint> checkpoints = checkpointDAO.getCheckpointsByRoute(routeId);
            request.setAttribute("route", route);
            request.setAttribute("event", event);
            request.setAttribute("checkpoints", checkpoints);
            request.getRequestDispatcher(ORGANIZER_ROUTE_VIEW_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void createRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String eventIdStr = request.getParameter("eventId");
            String distanceKmStr = request.getParameter("distanceKm");
            String description = request.getParameter("description");
            String startLatStr = request.getParameter("startLat");
            String startLngStr = request.getParameter("startLng");
            String endLatStr = request.getParameter("endLat");
            String endLngStr = request.getParameter("endLng");
            
            // Get checkpoints from form array parameters
            String[] checkpointNames = request.getParameterValues("checkpointName[]");
            String[] checkpointLats = request.getParameterValues("checkpointLat[]");
            String[] checkpointLngs = request.getParameterValues("checkpointLng[]");
            String[] checkpointOrders = request.getParameterValues("checkpointOrder[]");
            
            if (eventIdStr == null || eventIdStr.isEmpty()) {
                request.setAttribute("error", "Event ID is required!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = Integer.parseInt(eventIdStr);
            double distanceKm = distanceKmStr != null && !distanceKmStr.isEmpty() ? Double.parseDouble(distanceKmStr) : 0.0;
            
            models.Route route = new models.Route();
            route.setEventId(eventId);
            route.setDistanceKm(distanceKm);
            route.setDescription(description);
            
            // Create route and get route_id
            int routeId = routeDAO.createRoute(route);
            
            if (routeId == 0) {
                request.setAttribute("error", "Error creating route!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            // Update coordinates if provided
            if (startLatStr != null && !startLatStr.isEmpty() && startLngStr != null && !startLngStr.isEmpty() &&
                endLatStr != null && !endLatStr.isEmpty() && endLngStr != null && !endLngStr.isEmpty()) {
                Double startLat = Double.parseDouble(startLatStr);
                Double startLng = Double.parseDouble(startLngStr);
                Double endLat = Double.parseDouble(endLatStr);
                Double endLng = Double.parseDouble(endLngStr);
                routeDAO.updateRouteCoordinates(routeId, startLat, startLng, endLat, endLng);
            }
            
            // Create checkpoints if provided (from form array parameters)
            if (checkpointNames != null && checkpointNames.length > 0 &&
                checkpointLats != null && checkpointLats.length == checkpointNames.length &&
                checkpointLngs != null && checkpointLngs.length == checkpointNames.length &&
                checkpointOrders != null && checkpointOrders.length == checkpointNames.length) {
                try {
                    for (int i = 0; i < checkpointNames.length; i++) {
                        String name = checkpointNames[i];
                        String latStr = checkpointLats[i];
                        String lngStr = checkpointLngs[i];
                        String orderStr = checkpointOrders[i];
                        
                        if (name != null && !name.isEmpty() && 
                            latStr != null && !latStr.isEmpty() &&
                            lngStr != null && !lngStr.isEmpty() &&
                            orderStr != null && !orderStr.isEmpty()) {
                            
                            models.Checkpoint checkpoint = new models.Checkpoint();
                            checkpoint.setRouteId(routeId);
                            checkpoint.setCpName(name);
                            checkpoint.setLatitude(Double.parseDouble(latStr));
                            checkpoint.setLongitude(Double.parseDouble(lngStr));
                            checkpoint.setSequenceOrder(Integer.parseInt(orderStr));
                            checkpointDAO.createCheckpoint(checkpoint);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error creating checkpoints: " + e.getMessage());
                    e.printStackTrace();
                    // Continue even if checkpoint creation fails
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/organizer/routes?eventId=" + eventId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating route: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void updateRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String routeIdStr = request.getParameter("routeId");
            if (routeIdStr == null || routeIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int routeId = Integer.parseInt(routeIdStr);
            models.Route route = routeDAO.getRouteById(routeId);
            if (route == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            String distanceKmStr = request.getParameter("distanceKm");
            String description = request.getParameter("description");
            String startLatStr = request.getParameter("startLat");
            String startLngStr = request.getParameter("startLng");
            String endLatStr = request.getParameter("endLat");
            String endLngStr = request.getParameter("endLng");
            
            // Get checkpoints from form array parameters
            String[] checkpointNames = request.getParameterValues("checkpointName[]");
            String[] checkpointLats = request.getParameterValues("checkpointLat[]");
            String[] checkpointLngs = request.getParameterValues("checkpointLng[]");
            String[] checkpointOrders = request.getParameterValues("checkpointOrder[]");
            
            double distanceKm = distanceKmStr != null && !distanceKmStr.isEmpty() ? Double.parseDouble(distanceKmStr) : route.getDistanceKm();
            
            route.setDistanceKm(distanceKm);
            route.setDescription(description);
            
            routeDAO.updateRoute(route);
            
            // uupdate coordinates 
            if (startLatStr != null && !startLatStr.isEmpty() && startLngStr != null && !startLngStr.isEmpty() &&
                endLatStr != null && !endLatStr.isEmpty() && endLngStr != null && !endLngStr.isEmpty()) {
                Double startLat = Double.parseDouble(startLatStr);
                Double startLng = Double.parseDouble(startLngStr);
                Double endLat = Double.parseDouble(endLatStr);
                Double endLng = Double.parseDouble(endLngStr);
                routeDAO.updateRouteCoordinates(routeId, startLat, startLng, endLat, endLng);
            }
            
            // update checkpoints
            if (checkpointNames != null && checkpointNames.length > 0 &&
                checkpointLats != null && checkpointLats.length == checkpointNames.length &&
                checkpointLngs != null && checkpointLngs.length == checkpointNames.length &&
                checkpointOrders != null && checkpointOrders.length == checkpointNames.length) {
                try {
                    //delete old
                    List<models.Checkpoint> existingCheckpoints = checkpointDAO.getCheckpointsByRoute(routeId);
                    for (models.Checkpoint cp : existingCheckpoints) {
                        checkpointDAO.deleteCheckpoint(cp.getCpId());
                    }
                    
                    // create new checkpoints
                    for (int i = 0; i < checkpointNames.length; i++) {
                        String name = checkpointNames[i];
                        String latStr = checkpointLats[i];
                        String lngStr = checkpointLngs[i];
                        String orderStr = checkpointOrders[i];
                        
                        if (name != null && !name.isEmpty() && 
                            latStr != null && !latStr.isEmpty() &&
                            lngStr != null && !lngStr.isEmpty() &&
                            orderStr != null && !orderStr.isEmpty()) {
                            
                            models.Checkpoint checkpoint = new models.Checkpoint();
                            checkpoint.setRouteId(routeId);
                            checkpoint.setCpName(name);
                            checkpoint.setLatitude(Double.parseDouble(latStr));
                            checkpoint.setLongitude(Double.parseDouble(lngStr));
                            checkpoint.setSequenceOrder(Integer.parseInt(orderStr));
                            checkpointDAO.createCheckpoint(checkpoint);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error updating checkpoints: " + e.getMessage());
                    e.printStackTrace();
                    // Continue even if checkpoint update fails
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/organizer/routes?eventId=" + route.getEventId());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating route: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void deleteRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String routeIdStr = request.getParameter("id");
            if (routeIdStr == null || routeIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            int routeId = Integer.parseInt(routeIdStr);
            models.Route route = routeDAO.getRouteById(routeId);
            if (route == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int eventId = route.getEventId();
            boolean deleted = routeDAO.deleteRoute(routeId);
            
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/organizer/routes?eventId=" + eventId);
            } else {
                request.setAttribute("error", "Failed to delete route. Please try again.");
                response.sendRedirect(request.getContextPath() + "/organizer/routes?eventId=" + eventId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String eventIdStr = request.getParameter("eventId");
            if (eventIdStr != null && !eventIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/routes?eventId=" + eventIdStr);
            } else {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
            }
        }
    }
    
    // Checkpoint Management Methods
    private void showCheckpointsList(HttpServletRequest request, HttpServletResponse response, String routeIdStr)
            throws ServletException, IOException {
        try {
            if (routeIdStr == null || routeIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            int routeId = Integer.parseInt(routeIdStr);
            models.Route route = routeDAO.getRouteById(routeId);
            if (route == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            Event event = eventDAO.getEventById(route.getEventId());
            List<models.Checkpoint> checkpoints = checkpointDAO.getCheckpointsByRoute(routeId);
            request.setAttribute("route", route);
            request.setAttribute("event", event);
            request.setAttribute("checkpoints", checkpoints);
            request.getRequestDispatcher(ORGANIZER_CHECKPOINTS_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void showCheckpointForm(HttpServletRequest request, HttpServletResponse response, String cpIdStr, String routeIdStr)
            throws ServletException, IOException {
        try {
            models.Checkpoint checkpoint = null;
            models.Route route = null;
            
            if (cpIdStr != null && !cpIdStr.isEmpty()) {
                int cpId = Integer.parseInt(cpIdStr);
                checkpoint = checkpointDAO.getCheckpointById(cpId);
                if (checkpoint != null) {
                    route = routeDAO.getRouteById(checkpoint.getRouteId());
                }
            } else if (routeIdStr != null && !routeIdStr.isEmpty()) {
                int rtId = Integer.parseInt(routeIdStr);
                route = routeDAO.getRouteById(rtId);
            }
            
            if (route == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            request.setAttribute("checkpoint", checkpoint);
            request.setAttribute("route", route);
            request.getRequestDispatcher(ORGANIZER_CHECKPOINT_FORM_JSP).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void createCheckpoint(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String routeIdStr = request.getParameter("routeId");
            String cpName = request.getParameter("cpName");
            String latitudeStr = request.getParameter("latitude");
            String longitudeStr = request.getParameter("longitude");
            
            if (routeIdStr == null || routeIdStr.isEmpty()) {
                request.setAttribute("error", "Route ID is required!");
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int routeId = Integer.parseInt(routeIdStr);
            int sequenceOrder = checkpointDAO.getNextSequenceOrder(routeId);
            
            models.Checkpoint checkpoint = new models.Checkpoint();
            checkpoint.setRouteId(routeId);
            checkpoint.setCpName(cpName);
            checkpoint.setSequenceOrder(sequenceOrder);
            
            if (latitudeStr != null && !latitudeStr.isEmpty() && longitudeStr != null && !longitudeStr.isEmpty()) {
                checkpoint.setLatitude(Double.parseDouble(latitudeStr));
                checkpoint.setLongitude(Double.parseDouble(longitudeStr));
            }
            
            checkpointDAO.createCheckpoint(checkpoint);
            response.sendRedirect(request.getContextPath() + "/organizer/checkpoints?routeId=" + routeId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating checkpoint: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void updateCheckpoint(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpIdStr = request.getParameter("cpId");
            if (cpIdStr == null || cpIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            int cpId = Integer.parseInt(cpIdStr);
            models.Checkpoint checkpoint = checkpointDAO.getCheckpointById(cpId);
            if (checkpoint == null) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            
            String cpName = request.getParameter("cpName");
            String sequenceOrderStr = request.getParameter("sequenceOrder");
            String latitudeStr = request.getParameter("latitude");
            String longitudeStr = request.getParameter("longitude");
            
            checkpoint.setCpName(cpName);
            if (sequenceOrderStr != null && !sequenceOrderStr.isEmpty()) {
                checkpoint.setSequenceOrder(Integer.parseInt(sequenceOrderStr));
            }
            
            if (latitudeStr != null && !latitudeStr.isEmpty() && longitudeStr != null && !longitudeStr.isEmpty()) {
                checkpoint.setLatitude(Double.parseDouble(latitudeStr));
                checkpoint.setLongitude(Double.parseDouble(longitudeStr));
            }
            
            checkpointDAO.updateCheckpoint(checkpoint);
            response.sendRedirect(request.getContextPath() + "/organizer/checkpoints?routeId=" + checkpoint.getRouteId());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating checkpoint: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
    
    private void deleteCheckpoint(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpIdStr = request.getParameter("id");
            if (cpIdStr == null || cpIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
                return;
            }
            int cpId = Integer.parseInt(cpIdStr);
            models.Checkpoint checkpoint = checkpointDAO.getCheckpointById(cpId);
            if (checkpoint != null) {
                int routeId = checkpoint.getRouteId();
                checkpointDAO.deleteCheckpoint(cpId);
                checkpointDAO.reorderCheckpoints(routeId);
                response.sendRedirect(request.getContextPath() + "/organizer/checkpoints?routeId=" + routeId);
            } else {
                response.sendRedirect(request.getContextPath() + "/organizer/events");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/organizer/events");
        }
    }
}
