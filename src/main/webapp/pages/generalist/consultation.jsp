<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50">
<%@ include file="/pages/generalist/header.jsp"%>
<div class="max-w-7xl mx-auto p-6">


    <!-- Notifications -->
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${success}</div>
    </c:if>

    <c:if test="${not empty consultation}">

    <!-- Main Content Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

        <!-- Left Column - Patient & Consultation Info -->
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

            <!-- Consultation Details Card -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <h3 class="text-xl font-bold text-gray-800 mb-4">Consultation Details</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <h4 class="font-semibold text-gray-700 mb-2">General Information</h4>
                        <div class="space-y-2">
                            <div class="flex justify-between">
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
                                            <span class="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">AWAITING_SPECIALIST_REVIEW</span>
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

                    <div>
                        <h4 class="font-semibold text-gray-700 mb-2">Medical Information</h4>
                        <div class="space-y-2">
                            <div>
                                <p class="text-gray-600 text-sm">Motive</p>
                                <p class="font-semibold">${consultation.motive}</p>
                            </div>
                            <div>
                                <p class="text-gray-600 text-sm">Observations</p>
                                <p class="font-semibold">${consultation.observations}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Medical Acts Section -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-xl font-bold text-gray-800">Medical Acts</h3>
                    <span class="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                        ${consultation.countMedicalActs} acts
                    </span>
                </div>

                <!-- Medical Acts List -->
                <c:choose>
                    <c:when test="${not empty consultation.medicalActs}">
                        <div class="space-y-4">
                            <c:forEach items="${consultation.medicalActs}" var="act" varStatus="status">
                                <div class="border rounded-lg p-4 hover:bg-gray-50">
                                    <div class="flex justify-between items-start">
                                        <div class="flex-1">
                                            <h4 class="font-semibold text-gray-800">${act.name}</h4>
                                            <div class="flex items-center mt-2 space-x-4 text-sm">
                                                <span class="text-green-600 font-semibold">$${act.price}</span>
                                            </div>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/generalist/consultation/medical-act/remove"
                                              method="post" class="ml-4">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                                            <input type="hidden" name="consultationId" value="${consultation.id}">
                                            <input type="hidden" name="medicalActId" value="${act.id}">
                                            <button type="submit"
                                                    class="text-red-600 hover:text-red-800 p-1 rounded"
                                                    onclick="return confirm('Remove this medical act?')">
                                                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                                                </svg>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-8 text-gray-500">
                            <svg class="w-12 h-12 mx-auto text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                            </svg>
                            <p class="mt-2">No medical acts added yet</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- Diagnosis Section -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-xl font-bold text-gray-800">Diagnosis</h3>
                    <button onclick="showModal('editDiagnosis')" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        ${empty consultation.diagnosis ? 'Add' : 'Edit'} Diagnosis
                    </button>
                </div>
                <c:choose>
                    <c:when test="${not empty consultation.diagnosis}">
                        <div class="prose max-w-none">
                            <p class="text-gray-800 whitespace-pre-wrap">${consultation.diagnosis}</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                    <div class="text-center py-8 text-gray-500">
                        <svg class="w-12 h-12 mx-auto text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                        </svg>
                        <p class="mt-2">No diagnosis recorded yet</p>
                    </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- Treatment Plan Section -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-xl font-bold text-gray-800">Treatment Plan</h3>
                    <button onclick="showModal('editTreatment')" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        ${empty consultation.treatment ? 'Add' : 'Edit'} Treatment
                    </button>
                </div>
                <c:choose>
                    <c:when test="${not empty consultation.treatment}">
                        <div class="prose max-w-none">
                            <p class="text-gray-800 whitespace-pre-wrap">${consultation.treatment}</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                    <div class="text-center py-8 text-gray-500">
                        <svg class="w-12 h-12 mx-auto text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                        </svg>
                        <p class="mt-2">No treatment plan recorded yet</p>
                    </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="bg-white rounded-lg shadow-md p-6">
                  <div class="flex justify-between items-center mb-4">
                    <h2 class="text-2xl font-bold text-gray-800">Vital Signs History</h2>
                  </div>

                  <!-- Latest Vitals Summary -->
                  <c:choose>
                  <c:when test="${not empty consultation.patient.vitalSigns and not empty consultation.patient.vitalSigns[0]}">
                  <c:set var="latestVitals" value="${consultation.patient.vitalSigns[0]}" />
                  <div class="bg-blue-50 rounded-lg p-4 mb-6 grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div>
                      <p class="text-gray-600 text-sm">Latest Blood Pressure</p>
                      <p class="font-bold text-xl text-blue-700">${latestVitals.bloodPressure} mmHg</p>
                    </div>
                    <div>
                      <p class="text-gray-600 text-sm">Latest Heart Rate</p>
                      <p class="font-bold text-xl text-blue-700">${latestVitals.heartRate} bpm</p>
                    </div>
                    <div>
                      <p class="text-gray-600 text-sm">Latest Temperature</p>
                      <p class="font-bold text-xl text-blue-700">${latestVitals.bodyTemperature} &deg;C</p>
                    </div>
                    <div>
                      <p class="text-gray-600 text-sm">Latest Respiratory Rate</p>
                      <p class="font-bold text-xl text-blue-700">${latestVitals.respiratoryRate} bpm</p>
                    </div>
                  </div>

                  <!-- Vitals History Table -->
                  <div class="overflow-x-auto">
                    <table class="w-full">
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
                        <c:forEach items="${consultation.patient.vitalSigns}" var="v">
                        <tr class="border-t hover:bg-gray-50">
                          <td class="px-4 py-3">${v.formattedCreatedAt}</td>
                          <td class="px-4 py-3">${v.bloodPressure} mmHg</td>
                          <td class="px-4 py-3">${v.heartRate} bpm</td>
                          <td class="px-4 py-3">${v.bodyTemperature} &deg;C</td>
                          <td class="px-4 py-3">${v.respiratoryRate} bpm</td>
                          <td class="px-4 py-3">${v.weight} kg</td>
                          <td class="px-4 py-3">${v.height} cm</td>
                        </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
                  </c:when>
                  <c:otherwise>
                  <div class="text-center py-8 text-gray-500">
                        <svg class="w-12 h-12 mx-auto text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                        </svg>
                        <p class="mt-2">No vital signs recorded yet</p>
                    </div>
                  </c:otherwise>
                  </c:choose>
                </div>
                <c:if test="${consultation.request != null}">
                    <c:choose>
                        <c:when test="${consultation.request['class'].simpleName == 'SyncRequest'}">
                            <div class="bg-white rounded-lg shadow-md p-6">
                               <div class="flex justify-between items-center mb-6">
                                    <h3 class="text-xl font-bold text-gray-800">Meeting Link</h3>
                                </div>
                                <div class="prose max-w-none">
                                    <a href="${consultation.request.meetingUrl}" class="text-blue-600 hover:underline" target="_blank">${consultation.request.meetingUrl}</a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="bg-white rounded-lg shadow-md p-6">
                               <div class="flex justify-between items-center mb-6">
                                    <h3 class="text-xl font-bold text-gray-800">Specialist Request</h3>
                                </div>
                                <div class="space-y-3">
                                    <div>
                                        <p class="text-gray-600 text-sm">Specialist</p>
                                        <p class="font-semibold">${consultation.request.specialist.fullName}</p>
                                        <p class="text-gray-500 text-sm">${consultation.request.specialist.specialty}</p>
                                    </div>
                                    <c:if test="${consultation.request.question != null}">
                                        <div>
                                            <p class="text-gray-600 text-sm">Question</p>
                                            <p class="text-gray-800 whitespace-pre-wrap">${consultation.request.question}</p>
                                        </div>
                                    </c:if>
                                    <c:if test="${consultation.request.response != null}">
                                        <div class="mt-4 p-4 bg-blue-50 rounded">
                                            <p class="text-gray-600 text-sm font-semibold mb-2">Specialist Response</p>
                                            <p class="text-gray-800 whitespace-pre-wrap">${consultation.request.response.response}</p>
                                            <p class="text-gray-500 text-xs mt-2">Responded at: ${consultation.request.response.formattedRespondedAt}</p>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>
        </div>



        <!-- Right Column - Cost Summary & Actions -->
        <div class="space-y-6">

            <!-- Cost Summary Card -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <h3 class="text-xl font-bold text-gray-800 mb-4">Cost Summary</h3>
                <div class="space-y-3">
                    <div class="flex justify-between">
                        <span class="text-gray-600">Consultation Fee:</span>
                        <span class="font-semibold">$${consultation.cost}</span>
                    </div>
                    <div class="flex justify-between">
                        <span class="text-gray-600">Medical Acts (${consultation.countMedicalActs}):</span>
                        <span class="font-semibold">
                            $${consultation.medicalActsCost}
                        </span>
                    </div>
                    <c:if test="${consultation.request != null}">
                        <div class="flex justify-between">
                            <span class="text-gray-600">Specialist expertise:</span>
                            <span class="font-semibold">
                                $${consultation.request.specialist.fee}
                            </span>
                        </div>
                    </c:if>
                    <hr>
                    <div class="flex justify-between text-lg font-bold">
                        <span>Total:</span>
                        <span class="text-green-600">$${consultation.totalCost}</span>
                    </div>
                </div>
            </div>

            <!-- Quick Actions Card -->
            <div class="bg-white rounded-lg shadow-md p-6">
                <h3 class="text-xl font-bold text-gray-800 mb-4">Quick Actions</h3>
                <div class="space-y-3">
                    <button onclick="showModal('addMedicalAct')"
                            class="w-full bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 flex items-center justify-center">
                        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                        </svg>
                        Add Medical Act
                    </button>
                    <button onclick="showModal('updateStatus')"
                            class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 flex items-center justify-center">
                        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                        </svg>
                        Update Status
                    </button>
                    <c:if test="${consultation.request == null}">
                        <button onclick="showModal('requestModal')"
                                class="w-full bg-yellow-600 text-white px-4 py-2 rounded hover:bg-yellow-700 flex items-center justify-center">
                                <svg class="w-5 h-5 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><path fill="#ffffff" d="M439.4 96L448 96C483.3 96 512 124.7 512 160L512 512C512 547.3 483.3 576 448 576L192 576C156.7 576 128 547.3 128 512L128 160C128 124.7 156.7 96 192 96L200.6 96C211.6 76.9 232.3 64 256 64L384 64C407.7 64 428.4 76.9 439.4 96zM376 176C389.3 176 400 165.3 400 152C400 138.7 389.3 128 376 128L264 128C250.7 128 240 138.7 240 152C240 165.3 250.7 176 264 176L376 176zM320 312C336.1 312 349.2 325.1 349.2 341.2C349.2 349.9 346.1 355.1 342.3 358.9C337.8 363.3 331.6 366.4 325.5 368.4C310.6 373.4 296 387.7 296 407.9C296 421.2 306.7 431.9 320 431.9C331.5 431.9 341.2 423.8 343.5 412.9C362.7 405.8 397.2 386.6 397.2 341.1C397.2 298.5 362.6 263.9 320 263.9C277.4 263.9 242.8 298.5 242.8 341.1C242.8 354.4 253.5 365.1 266.8 365.1C280.1 365.1 290.8 354.4 290.8 341.1C290.8 325 303.9 311.9 320 311.9zM348 480C348 464.5 335.5 452 320 452C304.5 452 292 464.5 292 480C292 495.5 304.5 508 320 508C335.5 508 348 495.5 348 480z"/></svg>
                                    Request Specialist Expertise
                        </button>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/generalist/consultations"
                       class="block w-full bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700 text-center">
                        Back to Consultations
                    </a>
                </div>
            </div>
        </div>
    </div>
    </c:if>
</div>

<!-- MODALS -->

<!-- Add Medical Act Modal -->
<div id="addMedicalAct" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-xl font-bold mb-4">Add Medical Act</h3>
        <form action="${pageContext.request.contextPath}/generalist/consultation/medical-act/add" method="post">
            <input type="hidden" name="consultationId" value="${consultation.id}">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="block mb-4">
                <span class="text-gray-700">Select Medical Act</span>
                <select name="medicalActId" required class="w-full border rounded px-3 py-2 mt-1">
                    <option value="">Choose a medical act...</option>
                    <c:forEach items="${availableMedicalActs}" var="act">
                        <option value="${act.id}">${act.name} - $${act.price}</option>
                    </c:forEach>
                </select>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded flex-1 hover:bg-green-700">Add Act</button>
                <button type="button" onclick="hideModal('addMedicalAct')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
            </div>
        </form>
    </div>
</div>
<!-- Request Modal -->
<div id="requestModal" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-xl font-bold mb-4">Select request type and Speciality</h3>
        <form action="${pageContext.request.contextPath}/generalist/request/create" method="post">
            <input type="hidden" name="consultationId" value="${consultation.id}">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="block mb-4">
                <span class="text-gray-700">Status</span>
                <select name="type" required class="w-full border rounded px-3 py-2 mt-1">
                    <option value="sync">Synchronous request(meeting call)</option>
                    <option value="async">ASynchronous request(24h/48h)</option>
                </select>
            </label>
            <label class="block mb-4">
                <span class="text-gray-700">Speciality</span>
                <select name="specialty" required class="w-full border rounded px-3 py-2 mt-1">
                    <option value="">Select a specialty...</option>
                    <c:forEach items="${specialties}" var="s">
                        <option value="${s}">${s}</option>
                    </c:forEach>
                </select>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-yellow-600 text-white px-4 py-2 rounded flex-1 hover:bg-yellow-700">Create Request</button>
                <button type="button" onclick="hideModal('requestModal')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
            </div>
        </form>
    </div>
</div>

<!-- Update Status Modal -->
<div id="updateStatus" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-xl font-bold mb-4">Update Consultation Status</h3>
        <form action="${pageContext.request.contextPath}/generalist/consultation/update-status" method="post">
            <input type="hidden" name="consultationId" value="${consultation.id}">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="block mb-4">
                <span class="text-gray-700">Status</span>
                <select name="status" required class="w-full border rounded px-3 py-2 mt-1">
                    <option value="OPEN" ${consultation.status == 'OPEN' ? 'selected' : ''}>OPEN</option>
                    <option value="IN_PROGRESS" ${consultation.status == 'IN_PROGRESS' ? 'selected' : ''}>IN PROGRESS</option>
                    <option value="COMPLETED" ${consultation.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                    <option value="AWAITING_SPECIALIST_REVIEW" ${consultation.status == 'AWAITING_SPECIALIST_REVIEW' ? 'selected' : ''}>AWAITING SPECIALIST REVIEW</option>
                    <option value="CANCELLED" ${consultation.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                </select>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded flex-1 hover:bg-blue-700">Update Status</button>
                <button type="button" onclick="hideModal('updateStatus')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
            </div>
        </form>
    </div>
</div>
<!-- Edit Diagnosis Modal -->
<div id="editDiagnosis" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
        <h3 class="text-xl font-bold mb-4">${empty consultation.diagnosis ? 'Add' : 'Edit'} Diagnosis</h3>
        <form action="${pageContext.request.contextPath}/generalist/consultation/update-diagnosis" method="post">
            <input type="hidden" name="consultationId" value="${consultation.id}">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="block mb-4">
                <span class="text-gray-700">Diagnosis Details</span>
                <textarea name="diagnosis" rows="8" placeholder="Enter diagnosis details..."
                          class="w-full border rounded px-3 py-2 mt-1">${consultation.diagnosis}</textarea>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded flex-1 hover:bg-blue-700">Save Diagnosis</button>
                <button type="button" onclick="hideModal('editDiagnosis')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Treatment Modal -->
<div id="editTreatment" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
        <h3 class="text-xl font-bold mb-4">${empty consultation.treatment ? 'Add' : 'Edit'} Treatment Plan</h3>
        <form action="${pageContext.request.contextPath}/generalist/consultation/update-treatment" method="post">
            <input type="hidden" name="consultationId" value="${consultation.id}">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="block mb-4">
                <span class="text-gray-700">Treatment Plan</span>
                <textarea name="treatment" rows="8" placeholder="Enter treatment plan..."
                          class="w-full border rounded px-3 py-2 mt-1">${consultation.treatment}</textarea>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded flex-1 hover:bg-blue-700">Save Treatment</button>
                <button type="button" onclick="hideModal('editTreatment')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
            </div>
        </form>
    </div>
</div>

<script>
    function showModal(id) {
        document.getElementById(id).classList.remove('hidden');
    }

    function hideModal(id) {
        document.getElementById(id).classList.add('hidden');
    }

    // Close modal when clicking outside
    document.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.classList.add('hidden');
        }
    });
</script>

</body>
</html>