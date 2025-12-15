<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../../header.jsp"/>
<style>
    #map {
        height: 600px !important;
        width: 100% !important;
        margin-top: 20px;
        border: 1px solid #ddd;
        border-radius: 4px;
        position: relative;
        z-index: 1;
    }
    .leaflet-container {
        height: 100%;
        width: 100%;
    }
</style>
</head>
<body>
<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Route Map - ${route.description}</h2>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <a href="${pageContext.request.contextPath}/organizer/routes?eventId=${event.eventId}" class="btn btn-default">
                    <i class="fa fa-arrow-left"></i> Back to Routes
                </a>
                <a href="${pageContext.request.contextPath}/organizer/checkpoints?routeId=${route.routeId}" class="btn btn-primary">
                    <i class="fa fa-map-marker"></i> Manage Checkpoints
                </a>
            </div>
        </div>
        
        <div class="row" style="margin-top: 20px;">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Route Information</h3>
                    </div>
                    <div class="panel-body">
                        <p><strong>Event:</strong> ${event.name}</p>
                        <p><strong>Distance:</strong> ${route.distanceKm} km</p>
                        <p><strong>Description:</strong> ${route.description}</p>
                        <p><strong>Checkpoints:</strong> ${route.checkpointCount}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Route Map</h3>
                    </div>
                    <div class="panel-body">
                        <div id="map"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script>
    var map;
    var routeLayer;
    var checkpoints = [
        <c:forEach var="checkpoint" items="${checkpoints}" varStatus="status">
        {
            id: ${checkpoint.cpId},
            name: "${checkpoint.cpName}",
            order: ${checkpoint.sequenceOrder},
            lat: ${checkpoint.latitude != null ? checkpoint.latitude : 0},
            lng: ${checkpoint.longitude != null ? checkpoint.longitude : 0}
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
    
    function initMap() {
        var mapDiv = document.getElementById('map');
        if (!mapDiv || map) {
            return;
        }
        if (typeof L === 'undefined') {
            return;
        }
        
        try {
            map = L.map('map', {
                center: [21.0285, 105.8542],
                zoom: 13
            });
            
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                maxZoom: 19,
                subdomains: 'abc'
            }).addTo(map);
            
            routeLayer = L.layerGroup().addTo(map);
            
            // Fix map size after delays
            setTimeout(function() {
                if (map) {
                    map.invalidateSize();
                }
            }, 300);
            
            setTimeout(function() {
                if (map) {
                    map.invalidateSize();
                }
            }, 1000);
            
            // Draw route if we have checkpoints with coordinates
            var routePoints = [];
            checkpoints.forEach(function(cp) {
                if (cp.lat !== 0 && cp.lng !== 0) {
                    routePoints.push([cp.lat, cp.lng]);
                    
                    // Add marker for checkpoint
                    var marker = L.marker([cp.lat, cp.lng]).addTo(routeLayer);
                    marker.bindPopup('<b>Checkpoint ' + cp.order + '</b><br>' + cp.name + 
                                   '<br>Lat: ' + cp.lat.toFixed(6) + '<br>Lng: ' + cp.lng.toFixed(6));
                }
            });
            
            // Draw polyline if we have at least 2 points
            if (routePoints.length >= 2) {
                var polyline = L.polyline(routePoints, {
                    color: 'red',
                    weight: 4,
                    opacity: 0.7
                }).addTo(routeLayer);
                
                // Fit map to show all points
                map.fitBounds(polyline.getBounds());
            } else {
                // No checkpoints with coordinates, show message
                var infoDiv = document.createElement('div');
                infoDiv.className = 'alert alert-info';
                infoDiv.innerHTML = '<i class="fa fa-info-circle"></i> No checkpoints with coordinates found. Please add checkpoints with map locations.';
                document.getElementById('map').parentNode.insertBefore(infoDiv, document.getElementById('map'));
            }
            
        } catch (error) {

        }
    }
    

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initMap);
    } else {
        initMap();
    }
</script>
<jsp:include page="../../footer.jsp"/>

