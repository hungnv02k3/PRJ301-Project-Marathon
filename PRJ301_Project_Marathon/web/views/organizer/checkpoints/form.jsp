<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title><c:choose><c:when test="${checkpoint != null}">Edit Checkpoint</c:when><c:otherwise>Add New Checkpoint</c:otherwise></c:choose></title>
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
                height: 400px;
                width: 100%;
                margin-top: 20px;
                border: 1px solid #ddd;
                border-radius: 4px;
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
                                <c:when test="${checkpoint != null}">Edit Checkpoint</c:when>
                                <c:otherwise>Add New Checkpoint</c:otherwise>
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
                                <form method="post" action="${pageContext.request.contextPath}/organizer/checkpoints/<c:choose><c:when test="${checkpoint != null}">edit</c:when><c:otherwise>add</c:otherwise></c:choose>" id="checkpointForm">
                                    <c:if test="${checkpoint != null}">
                                        <input type="hidden" name="cpId" value="${checkpoint.cpId}">
                                    </c:if>
                                    <input type="hidden" name="routeId" value="${route.routeId}">

                                    <div class="form-group">
                                        <label for="cpName">Checkpoint Name *</label>
                                        <input type="text" class="form-control" id="cpName" name="cpName" 
                                               value="${checkpoint.cpName}" required>
                                    </div>

                                    <c:if test="${checkpoint != null}">
                                        <div class="form-group">
                                            <label for="sequenceOrder">Sequence Order *</label>
                                            <input type="number" class="form-control" id="sequenceOrder" name="sequenceOrder" 
                                                   value="${checkpoint.sequenceOrder}" min="1" required>
                                        </div>
                                    </c:if>

                                    <div class="form-group">
                                        <label>Location on Map</label>
                                        <p class="help-block">Click on the map to set the checkpoint location</p>
                                        <div id="map"></div>
                                        <input type="hidden" id="latitude" name="latitude" value="${checkpoint.latitude}">
                                        <input type="hidden" id="longitude" name="longitude" value="${checkpoint.longitude}">
                                    </div>

                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary" style="margin-right: 10px; margin-top: 0; margin-bottom: 0; vertical-align: top;">
                                            <i class="fa fa-save"></i> Save
                                        </button>
                                        <a href="${pageContext.request.contextPath}/organizer/checkpoints?routeId=${route.routeId}" class="btn btn-default" style="margin-top: 0; margin-bottom: 0; vertical-align: top;">
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
            var marker;

            window.addEventListener('load', function () {
                if (typeof L === 'undefined') {
                    console.error('Leaflet.js is not loaded!');
                    document.getElementById('map').innerHTML = '<div class="alert alert-danger">Map library failed to load. Please refresh the page.</div>';
                    return;
                }

            <c:choose>
                <c:when test="${checkpoint != null && checkpoint.latitude != null && checkpoint.longitude != null}">
        map = L.map('map').setView([${checkpoint.latitude}, ${checkpoint.longitude}], 15);
                </c:when>
                <c:otherwise>
                map = L.map('map').setView([21.0285, 105.8542], 13);
                </c:otherwise>
            </c:choose>

                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                    maxZoom: 19,
                    subdomains: 'abc'
                }).addTo(map);

            <c:if test="${checkpoint != null && checkpoint.latitude != null && checkpoint.longitude != null}">
                marker = L.marker([${checkpoint.latitude}, ${checkpoint.longitude}]).addTo(map);
                marker.bindPopup('<b>${checkpoint.cpName}</b><br>Lat: ${checkpoint.latitude}<br>Lng: ${checkpoint.longitude}').openPopup();
            </c:if>

                setTimeout(function () {
                    map.invalidateSize();
                }, 200);

                map.on('click', function (e) {
                    var lat = e.latlng.lat;
                    var lng = e.latlng.lng;

                    if (marker) {
                        map.removeLayer(marker);
                    }

                    // Add new marker
                    marker = L.marker([lat, lng]).addTo(map);
                    marker.bindPopup('<b>Checkpoint Location</b><br>Lat: ' + lat.toFixed(6) + '<br>Lng: ' + lng.toFixed(6)).openPopup();

                    // Update hidden inputs
                    document.getElementById('latitude').value = lat;
                    document.getElementById('longitude').value = lng;
                });
            });
        </script>
        <jsp:include page="../../footer.jsp"/>

