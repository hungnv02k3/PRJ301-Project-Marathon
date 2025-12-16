<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
          integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
          crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
            integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
            crossorigin=""></script>
<style>
    #map {
        height: 500px !important;
        width: 100% !important;
        margin-top: 20px;
        border: 1px solid #ddd;
        border-radius: 4px;
        position: relative;
        z-index: 1;
    }
    .map-controls {
        margin-top: 10px;
        padding: 10px;
        background-color: #f5f5f5;
        border-radius: 4px;
    }
    /* Ensure Leaflet map tiles are visible */
    .leaflet-container {
        height: 100%;
        width: 100%;
    }
</style>
</head>
<body>

<jsp:include page="../../header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">
                    <c:choose>
                        <c:when test="${route != null}">Edit Route</c:when>
                        <c:otherwise>Create New Route</c:otherwise>
                    </c:choose>
                </h2>
            </div>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form method="post" action="${pageContext.request.contextPath}/organizer/routes/<c:choose><c:when test="${route != null}">edit</c:when><c:otherwise>add</c:otherwise></c:choose>" id="routeForm">
                            <c:if test="${route != null}">
                                <input type="hidden" name="routeId" value="${route.routeId}">
                            </c:if>
                            <input type="hidden" name="eventId" value="${event.eventId}">
                            
                            <div class="form-group">
                                <label for="distanceKm">Distance (km) </label>
                                <input type="number" step="0.01" class="form-control" id="distanceKm" name="distanceKm" 
                                       value="${route.distanceKm}" required readonly>
                                <small class="help-block">
                                    <i class="fa fa-info-circle"></i> Distance is automatically calculated from checkpoints on the map.
                                </small>
                            </div>
                            
                            <div class="form-group">
                                <label for="description">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3">${route.description}</textarea>
                            </div>
                            
                            <div class="form-group">
                                <label>Route Map</label>
                                <div class="map-controls">
                                    <p><b>Instructions:</b></p>
                                    <ol>
                                        <li>Click on the map to set the <b>Start Point</b></li>                                        
                                        <li>You can click multiple times to create a checkpoint</li>
                                        <li>Click "Clear Route" to start over</li>
                                    </ol>
                                    <button type="button" class="btn btn-sm btn-warning" onclick="clearRoute()">
                                        <i class="fa fa-refresh"></i> Clear Route
                                    </button>
                                </div>
                                <div id="map"></div>
                                <input type="hidden" id="startLat" name="startLat">
                                <input type="hidden" id="startLng" name="startLng">
                                <input type="hidden" id="endLat" name="endLat">
                                <input type="hidden" id="endLng" name="endLng">
                                <!-- Checkpoints will be added here as hidden inputs with array parameters -->
                                <div id="checkpointsContainer"></div>
                            </div>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary" style="margin-right: 10px; margin-top: 0; margin-bottom: 0; vertical-align: top;">
                                    <i class="fa fa-save"></i> Save
                                </button>
                                <a href="${pageContext.request.contextPath}/organizer/routes?eventId=${event.eventId}" class="btn btn-default" style="margin-top: 0; margin-bottom: 0; vertical-align: top;">
                                    <i class="fa fa-times"></i> Cancel
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script>
    var map;
    var routeLayer;
    var markers = [];
    var routePoints = [];
    
    function initMap() {
        if (map) {
            return;
        }
        var mapDiv = document.getElementById('map');
        if (!mapDiv || mapDiv._leaflet_id) {
            return;
        }
        
        try {
            // Initialize map centered on Vietnam (Hanoi)
            map = L.map('map', {
                center: [21.0285, 105.8542],
                zoom: 13
            });
            
            var osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                maxZoom: 19,
                subdomains: 'abc',
                errorTileUrl: 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7'
            });
            osmLayer.addTo(map);
            
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
            
            map.on('click', function(e) {
                var lat = e.latlng.lat;
                var lng = e.latlng.lng;
                
                var marker = L.marker([lat, lng]).addTo(routeLayer);
                markers.push(marker);
                routePoints.push([lat, lng]);
                
                // Update start/end hidden fields based on first and last points
                if (routePoints.length >= 1) {
                    document.getElementById('startLat').value = routePoints[0][0];
                    document.getElementById('startLng').value = routePoints[0][1];
                }
                if (routePoints.length >= 2) {
                    var lastIndex = routePoints.length - 1;
                    document.getElementById('endLat').value = routePoints[lastIndex][0];
                    document.getElementById('endLng').value = routePoints[lastIndex][1];
                }
                
                for (var i = 0; i < routePoints.length; i++) {
                    var cpLat = routePoints[i][0];
                    var cpLng = routePoints[i][1];
                    var order = i + 1;
                    var popupHtml;
                    
                    if (i === 0) {
                        popupHtml = '<b>Start Point (Checkpoint 1)</b><br>Lat: ' + cpLat.toFixed(6) + '<br>Lng: ' + cpLng.toFixed(6);
                    } else if (i === routePoints.length - 1) {
                        popupHtml = '<b>End Point (Checkpoint ' + order + ')</b><br>Lat: ' + cpLat.toFixed(6) + '<br>Lng: ' + cpLng.toFixed(6);
                    } else {
                        popupHtml = '<b>Checkpoint ' + order + '</b><br>Lat: ' + cpLat.toFixed(6) + '<br>Lng: ' + cpLng.toFixed(6);
                    }
                    
                    markers[i].bindPopup(popupHtml);
                    // Only open popup for the most recently added point
                    if (i === routePoints.length - 1) {
                        markers[i].openPopup();
                    }
                }
                
                updateCheckpointsData();
                
                updateDistanceDisplay();
                
                if (routePoints.length >= 2) {
                    updateRouteLine();
                }
            });
            
            function calculateTotalDistance() {
                if (routePoints.length < 2) {
                    return 0;
                }
                var totalDistance = 0;
                for (var i = 0; i < routePoints.length - 1; i++) {
                    totalDistance += calculateDistance(
                        routePoints[i][0], routePoints[i][1],
                        routePoints[i + 1][0], routePoints[i + 1][1]
                    );
                }
                return totalDistance;
            }
            
            function updateDistanceDisplay() {
                var totalDistance = calculateTotalDistance();
                var distanceInput = document.getElementById('distanceKm');
                if (distanceInput && totalDistance > 0) {
                    distanceInput.value = totalDistance.toFixed(2);
                }
            }
            
            // Calculate distance when form is submitted
            var routeForm = document.getElementById('routeForm');
            if (routeForm) {
                routeForm.addEventListener('submit', function(e) {
                    if (routePoints.length < 2) {
                        alert('Please set at least a start point and end point on the map!');
                        e.preventDefault();
                        return false;
                    }
                    
                    updateCheckpointsData();
                    
                    var totalDistance = calculateTotalDistance();
                    var distanceInput = document.getElementById('distanceKm');
                    if (distanceInput) {
                        // Always update distance based on checkpoints
                        distanceInput.value = totalDistance.toFixed(2);
                    }
                });
            }
            
        } catch (error) {
        }
    }
    
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initMap);
    } else {
        initMap();
    }
    
    function updateRouteLine() {
        if (!routeLayer) return;
        
        routeLayer.eachLayer(function(layer) {
            if (layer instanceof L.Polyline) {
                routeLayer.removeLayer(layer);
            }
        });
        
        if (routePoints.length >= 2) {
            var polyline = L.polyline(routePoints, {
                color: 'red',
                weight: 4,
                opacity: 0.7
            }).addTo(routeLayer);
        }
    }
    
    function clearRoute() {
        if (routeLayer) {
            routeLayer.clearLayers();
        }
        markers = [];
        routePoints = [];
        document.getElementById('startLat').value = '';
        document.getElementById('startLng').value = '';
        document.getElementById('endLat').value = '';
        document.getElementById('endLng').value = '';
        // Clear checkpoints container
        var container = document.getElementById('checkpointsContainer');
        if (container) {
            container.innerHTML = '';
        }
        
        var distanceInput = document.getElementById('distanceKm');
        if (distanceInput) {
            distanceInput.value = '';
        }
    }
    
    function updateCheckpointsData() {
        var container = document.getElementById('checkpointsContainer');
        if (container) {
            container.innerHTML = '';
        }
        
        // All points are checkpoints (including start and end)
        for (var i = 0; i < routePoints.length; i++) {
            var checkpointName;
            var order = i + 1; // Start from 1
            if (i === 0) {
                checkpointName = 'Start Point';
            } else if (i === routePoints.length - 1) {
                checkpointName = 'End Point';
            } else {
                checkpointName = 'Checkpoint ' + order;
            }
            
            if (container) {
                var nameInput = document.createElement('input');
                nameInput.type = 'hidden';
                nameInput.name = 'checkpointName[]';
                nameInput.value = checkpointName;
                container.appendChild(nameInput);
                
                var latInput = document.createElement('input');
                latInput.type = 'hidden';
                latInput.name = 'checkpointLat[]';
                latInput.value = routePoints[i][0];
                container.appendChild(latInput);
                
                var lngInput = document.createElement('input');
                lngInput.type = 'hidden';
                lngInput.name = 'checkpointLng[]';
                lngInput.value = routePoints[i][1];
                container.appendChild(lngInput);
                
                var orderInput = document.createElement('input');
                orderInput.type = 'hidden';
                orderInput.name = 'checkpointOrder[]';
                orderInput.value = order;
                container.appendChild(orderInput);
            }
        }
    }
    
    // Calculate distance between two points using Haversine formula
    function calculateDistance(lat1, lon1, lat2, lon2) {
        var R = 6371; // Radius of the earth in km
        var dLat = deg2rad(lat2 - lat1);
        var dLon = deg2rad(lon2 - lon1);
        var a = 
            Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
            Math.sin(dLon/2) * Math.sin(dLon/2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        var d = R * c; // Distance in km
        return d;
    }
    
    function deg2rad(deg) {
        return deg * (Math.PI/180);
    }
</script>
<jsp:include page="../../footer.jsp"/>


