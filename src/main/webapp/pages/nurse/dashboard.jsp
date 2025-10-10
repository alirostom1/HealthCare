<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50">
<%@ include file="/pages/nurse/header.jsp"%>
<div class="max-w-7xl mx-auto p-6">

    <!-- Notifications -->
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${success}</div>
    </c:if>

    <!-- Tabs -->
    <div class="flex space-x-2 mb-6 border-b">
        <button onclick="showTab('patients')" class="tab-btn px-6 py-3 font-semibold text-blue-600 border-b-2 border-blue-600">Patients</button>
        <button onclick="showTab('queue')" class="tab-btn px-6 py-3 font-semibold text-gray-600">Queue</button>
    </div>

    <!-- PATIENTS TAB -->
    <div id="patients" class="tab-content">
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-2xl font-bold">Patients List</h2>
            <button onclick="showModal('addPatient')" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">+ Add Patient</button>
        </div>

        <!-- Patients Table -->
        <div class="bg-white rounded shadow overflow-hidden">
            <table class="w-full">
                <thead class="bg-gray-100">
                    <tr>
                        <th class="px-4 py-3 text-left">SSN</th>
                        <th class="px-4 py-3 text-left">Full Name</th>
                        <th class="px-4 py-3 text-left">Birthdate</th>
                        <th class="px-4 py-3 text-left">Phone</th>
                        <th class="px-4 py-3 text-left">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${patients}" var="p">
                    <tr class="border-t hover:bg-gray-50">
                        <td class="px-4 py-3">${p.ssn}</td>
                        <td class="px-4 py-3">${p.firstName} ${p.lastName}</td>
                        <td class="px-4 py-3">${p.birthDate}</td>
                        <td class="px-4 py-3">${p.phoneNumber}</td>
                        <td class="px-4 py-3">
                            <button onclick="showVitals('${p.id}', '${p.firstName} ${p.lastName}')"
                                    class="bg-blue-500 text-white px-3 py-1 rounded text-sm mr-1">Vitals</button>
                            <button onclick="addToQueue('${p.id}')"
                                    class="bg-purple-600 text-white px-3 py-1 rounded text-sm">Queue</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- QUEUE TAB -->
    <div id="queue" class="tab-content hidden">
        <h2 class="text-2xl font-bold mb-4">Patient Queue</h2>
        <div class="bg-white rounded shadow">
            <ul class="divide-y">
                <c:forEach items="${queue}" var="q">
                <li class="px-4 py-3 flex justify-between items-center">
                    <div>
                        <span class="font-semibold">${q.patient.firstName} ${q.patient.lastName}</span>
                        <span class="text-gray-500 text-sm ml-2">${q.patient.ssn}</span>
                    </div>
                    <form action="${pageContext.request.contextPath}/nurse/queue/remove" method="post" class="inline">
                        <input type="hidden" name="patientId" value="${q.patient.id}">
                        <button type="submit" class="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600">
                            Remove
                        </button>
                    </form>
                </li>
                </c:forEach>
            </ul>
        </div>
    </div>

<!-- ADD PATIENT MODAL -->
<div id="addPatient" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[90%] overflow-y-auto">
        <h3 class="text-xl font-bold mb-4">Register New Patient</h3>
        <form action="${pageContext.request.contextPath}/nurse/patient/register" method="post">

            <!-- Personal Information -->
            <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">First Name *</label>
                    <input type="text" name="firstName" required
                           class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Last Name *</label>
                    <input type="text" name="lastName" required
                           class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Birth Date *</label>
                    <input type="date" name="birthDate" required
                           class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">SSN *</label>
                    <input type="text" name="ssn" required
                           class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
            </div>

            <div class="mb-6">
                <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                <input type="text" name="phoneNumber"
                       class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <!-- Vital Signs (Optional) -->
            <div class="border-t pt-4 mb-6">
                <h4 class="text-md font-medium text-gray-900 mb-3">Initial Vital Signs (Optional)</h4>

                <div class="grid grid-cols-2 gap-4 mb-3">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Blood Pressure</label>
                        <input type="text" name="bloodPressure" placeholder="e.g., 120/80"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Heart Rate (bpm)</label>
                        <input type="number" name="heartRate" placeholder="e.g., 72"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4 mb-3">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Body Temperature (°C)</label>
                        <input type="number" step="0.1" name="bodyTemperature" placeholder="e.g., 36.6"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Respiratory Rate</label>
                        <input type="number" name="respiratoryRate" placeholder="e.g., 16"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Weight (kg)</label>
                        <input type="number" step="0.1" name="weight" placeholder="e.g., 70.5"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Height (cm)</label>
                        <input type="number" step="0.1" name="height" placeholder="e.g., 175.0"
                               class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>
            </div>

            <div class="flex justify-end space-x-3">
                <button type="button" onclick="hideModal('addPatient')"
                        class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition-colors">
                    Cancel
                </button>
                <button type="submit"
                        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                    Register Patient
                </button>
            </div>
        </form>
    </div>
</div>

<!-- VITALS MODAL -->
<div id="vitalsModal" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
        <h3 class="text-xl font-bold mb-4">Vital Signs</h3>
        <div id="vitalsPatientName" class="text-gray-600 mb-4"></div>

        <!-- Add New Vitals Form -->
        <form action="vitals/add" method="post" class="bg-gray-50 p-4 rounded mb-4">
            <input type="hidden" name="patientId" id="vitalsPatientId">
            <h4 class="font-semibold mb-2">Add New Reading</h4>
            <div class="grid grid-cols-3 gap-2 mb-2">
                <input type="text" name="bloodPressure" placeholder="BP (120/80)" class="border rounded px-3 py-2" required>
                <input type="number" name="heartRate" placeholder="HR (bpm)" class="border rounded px-3 py-2" required>
                <input type="number" step="0.1" name="temperature" placeholder="Temp (°C)" class="border rounded px-3 py-2" required>
            </div>
            <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Save Reading</button>
        </form>

        <button type="button" onclick="hideModal('vitalsModal')" class="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400">Close</button>
    </div>
</div>

<script>
    // Tab switching
    function showTab(tab) {
        document.querySelectorAll('.tab-content').forEach(el => el.classList.add('hidden'));
        document.getElementById(tab).classList.remove('hidden');

        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
            btn.classList.add('text-gray-600');
        });
        event.target.classList.remove('text-gray-600');
        event.target.classList.add('text-blue-600', 'border-b-2', 'border-blue-600');
    }

    // Modal functions
    function showModal(id) {
        document.getElementById(id).classList.remove('hidden');
    }
    function hideModal(id) {
        document.getElementById(id).classList.add('hidden');
    }

    // Show vitals
    function showVitals(patientId, patientName) {
        document.getElementById('vitalsPatientId').value = patientId;
        document.getElementById('vitalsPatientName').textContent = patientName;
        showModal('vitalsModal');
    }

    // Add to queue
    function addToQueue(patientId) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = '${pageContext.request.contextPath}/nurse/queue/add';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'patientId';
        input.value = patientId;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>