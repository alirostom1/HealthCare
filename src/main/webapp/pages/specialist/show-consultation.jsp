<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50 min-h-screen">
<%@ include file="/pages/specialist/header.jsp"%>
<div class="max-w-7xl mx-auto p-6">
    <!-- Notifications -->
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${success}</div>
    </c:if>

    <c:if test="${not empty consultation}">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

            <!-- Left Column - Patient & Medical Information -->
            <div class="lg:col-span-2 space-y-6">

                <!-- Patient Information Card -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-bold text-gray-800 mb-4">Patient Information</h3>
                    <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                        <div>
                            <p class="text-gray-500 text-sm">Patient ID</p>
                            <p class="font-semibold">${consultation.patient.id}</p>
                        </div>
                        <div>
                            <p class="text-gray-500 text-sm">Full Name</p>
                            <p class="font-semibold">${consultation.patient.firstName} ${consultation.patient.lastName}</p>
                        </div>
                        <div>
                            <p class="text-gray-500 text-sm">SSN</p>
                            <p class="font-semibold">${consultation.patient.ssn}</p>
                        </div>
                        <div>
                            <p class="text-gray-500 text-sm">Contact</p>
                            <p class="font-semibold">${consultation.patient.phoneNumber}</p>
                        </div>
                        <div>
                            <p class="text-gray-500 text-sm">Age</p>
                            <p class="font-semibold">${consultation.patient.birthDate}</p>
                        </div>
                    </div>
                </div>

                <!-- Generalist Observations Card -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-bold text-gray-800 mb-4">Generalist Observations</h3>
                    <div class="space-y-4">
                        <div>
                            <h4 class="font-semibold text-gray-700 mb-2">Consultation Motive</h4>
                            <div class="bg-blue-50 rounded p-4">
                                <p class="text-gray-800">${consultation.motive}</p>
                            </div>
                        </div>
                        <div>
                            <h4 class="font-semibold text-gray-700 mb-2">Clinical Observations</h4>
                            <div class="bg-gray-50 rounded p-4">
                                <p class="text-gray-800 whitespace-pre-wrap">
                                    <c:choose>
                                        <c:when test="${not empty consultation.observations}">
                                            ${consultation.observations}
                                        </c:when>
                                        <c:otherwise>
                                            No observations provided by the generalist.
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                        <div class="flex items-center text-sm text-gray-600">
                            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                            </svg>
                            Generalist: Dr. ${consultation.generalist.fullName}
                        </div>
                    </div>
                </div>

                <!-- Vital Signs History -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-bold text-gray-800 mb-4">Vital Signs History</h3>

                    <c:choose>
                        <c:when test="${not empty consultation.patient.vitalSigns and not empty consultation.patient.vitalSigns[0]}">
                            <c:set var="latestVitals" value="${consultation.patient.vitalSigns[0]}" />

                            <!-- Latest Vitals Summary -->
                            <div class="bg-green-50 rounded-lg p-4 mb-6">
                                <h4 class="font-semibold text-gray-700 mb-3">Latest Vital Signs</h4>
                                <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                                    <div>
                                        <p class="text-gray-600 text-sm">Blood Pressure</p>
                                        <p class="font-bold text-lg text-green-700">${latestVitals.bloodPressure} mmHg</p>
                                    </div>
                                    <div>
                                        <p class="text-gray-600 text-sm">Heart Rate</p>
                                        <p class="font-bold text-lg text-green-700">${latestVitals.heartRate} bpm</p>
                                    </div>
                                    <div>
                                        <p class="text-gray-600 text-sm">Temperature</p>
                                        <p class="font-bold text-lg text-green-700">${latestVitals.bodyTemperature} °C</p>
                                    </div>
                                    <div>
                                        <p class="text-gray-600 text-sm">Respiratory Rate</p>
                                        <p class="font-bold text-lg text-green-700">${latestVitals.respiratoryRate} bpm</p>
                                    </div>
                                    <c:if test="${not empty latestVitals.weight}">
                                        <div>
                                            <p class="text-gray-600 text-sm">Weight</p>
                                            <p class="font-bold text-lg text-green-700">${latestVitals.weight} kg</p>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty latestVitals.height}">
                                        <div>
                                            <p class="text-gray-600 text-sm">Height</p>
                                            <p class="font-bold text-lg text-green-700">${latestVitals.height} cm</p>
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <!-- Vitals History Table -->
                            <div class="overflow-x-auto">
                                <table class="w-full text-sm">
                                    <thead class="bg-gray-100">
                                    <tr>
                                        <th class="px-4 py-3 text-left">Date & Time</th>
                                        <th class="px-4 py-3 text-left">Blood Pressure</th>
                                        <th class="px-4 py-3 text-left">Heart Rate</th>
                                        <th class="px-4 py-3 text-left">Temperature</th>
                                        <th class="px-4 py-3 text-left">Respiratory Rate</th>
                                        <th class="px-4 py-3 text-left">Weight</th>
                                        <th class="px-4 py-3 text-left">Height</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${consultation.patient.vitalSigns}" var="vital">
                                        <tr class="border-t hover:bg-gray-50">
                                            <td class="px-4 py-3">${vital.formattedCreatedAt}</td>
                                            <td class="px-4 py-3">${vital.bloodPressure} mmHg</td>
                                            <td class="px-4 py-3">${vital.heartRate} bpm</td>
                                            <td class="px-4 py-3">${vital.bodyTemperature} °C</td>
                                            <td class="px-4 py-3">${vital.respiratoryRate} bpm</td>
                                            <td class="px-4 py-3">
                                                <c:if test="${not empty vital.weight}">${vital.weight} kg</c:if>
                                                <c:if test="${empty vital.weight}">-</c:if>
                                            </td>
                                            <td class="px-4 py-3">
                                                <c:if test="${not empty vital.height}">${vital.height} cm</c:if>
                                                <c:if test="${empty vital.height}">-</c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-8 text-gray-500">
                                <svg class="w-12 h-12 mx-auto text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                                </svg>
                                <p class="mt-2">No vital signs recorded for this patient</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>


            </div>

            <!-- Right Column - Consultation Info & Actions -->
            <div class="space-y-6">
                <!-- Consultation Status Card -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-bold text-gray-800 mb-4">Consultation Status</h3>
                    <div class="space-y-3">
                        <div class="flex justify-between items-center">
                            <span class="text-gray-600">Status:</span>
                            <span class="font-semibold">
                                <c:choose>
                                    <c:when test="${consultation.status == 'OPEN'}">
                                        <span class="bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full text-xs">OPEN</span>
                                    </c:when>
                                    <c:when test="${consultation.status == 'IN_PROGRESS'}">
                                        <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs">IN PROGRESS</span>
                                    </c:when>
                                    <c:when test="${consultation.status == 'COMPLETED'}">
                                        <span class="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs">COMPLETED</span>
                                    </c:when>
                                    <c:when test="${consultation.status == 'AWAITING_SPECIALIST_REVIEW'}">
                                        <span class="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">AWAITING REVIEW</span>
                                    </c:when>
                                    <c:when test="${consultation.status == 'CANCELLED'}">
                                        <span class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-xs">CANCELLED</span>
                                    </c:when>
                                </c:choose>
                            </span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-600">Created:</span>
                            <span class="font-semibold">${consultation.formattedCreatedAt}</span>
                        </div>
                        <div class="flex justify-between">
                            <span class="text-gray-600">Generalist:</span>
                            <span class="font-semibold">Dr. ${consultation.generalist.fullName}</span>
                        </div>
                    </div>
                </div>

                <!-- Quick Actions -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-bold text-gray-800 mb-4">Actions</h3>
                    <div class="space-y-3">
                        <a href="${pageContext.request.contextPath}/specialist/requests"
                           class="block w-full bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700 text-center">
                            Back to requests
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>