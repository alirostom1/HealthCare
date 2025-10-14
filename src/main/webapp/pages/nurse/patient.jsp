<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50">
<%@ include file="/pages/nurse/header.jsp"%>
<div class="max-w-6xl mx-auto p-6">

    <!-- Patient Information Card -->
    <div class="bg-white rounded-lg shadow-md p-6 mb-6">
      <div class="flex justify-between items-start mb-4">
        <h2 class="text-2xl font-bold text-gray-800">Patient Information</h2>
        <button onclick="showModal('editPatient')" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">
          Edit Info
        </button>
      </div>
       <!-- Notifications -->
          <c:if test="${not empty error}">
              <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
          </c:if>
          <c:if test="${not empty success}">
              <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${success}</div>
          </c:if>
      <c:if test="${not empty patient}">

      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">

        <!-- Patient ID -->
        <div>
          <p class="text-gray-500 text-sm">Patient ID</p>
          <p class="font-semibold text-lg">${patient.id}</p>
        </div>

        <!-- Full Name -->
        <div>
          <p class="text-gray-500 text-sm">Full Name</p>
          <p class="font-semibold text-lg">${patient.firstName} ${patient.lastName}</p>
        </div>

        <!-- Age (calculated from birthDate) -->
        <div>
          <p class="text-gray-500 text-sm">Age</p>
          <p class="font-semibold text-lg">${patient.birthDate}</p>
        </div>

        <!-- SSN -->
        <div>
          <p class="text-gray-500 text-sm">SSN</p>
          <p class="font-semibold text-lg">${patient.ssn}</p>
        </div>

        <!-- Contact Number -->
        <div>
          <p class="text-gray-500 text-sm">Contact Number</p>
          <p class="font-semibold text-lg">${patient.phoneNumber}</p>
        </div>

        <!-- Registration Date -->
        <div>
          <p class="text-gray-500 text-sm">Registration Date</p>
          <p class="font-semibold text-lg">${patient.createdAt}</p>
        </div>
      </div>
    </div>

    <!-- Vital Signs Section -->
    <div class="bg-white rounded-lg shadow-md p-6">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold text-gray-800">Vital Signs History</h2>
        <button onclick="showModal('addVitals')" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
          + Add New Reading
        </button>
      </div>

      <!-- Latest Vitals Summary -->
      <c:if test="${not empty patient.vitalSigns and not empty patient.vitalSigns[0]}">
      <c:set var="latestVitals" value="${patient.vitalSigns[0]}" />
      <div class="bg-blue-50 rounded-lg p-4 mb-6 grid grid-cols-2 md:grid-cols-4 gap-4">
        <div>
          <p class="text-gray-600 text-sm">Latest Blood Pressure</p>
          <p class="font-bold text-xl text-blue-700">${patient.vitalSigns[0].bloodPressure} mmHg</p>
        </div>
        <div>
          <p class="text-gray-600 text-sm">Latest Heart Rate</p>
          <p class="font-bold text-xl text-blue-700">${patient.vitalSigns[0].heartRate} bpm</p>
        </div>
        <div>
          <p class="text-gray-600 text-sm">Latest Temperature</p>
          <p class="font-bold text-xl text-blue-700">${patient.vitalSigns[0].bodyTemperature}°C</p>
        </div>
        <div>
          <p class="text-gray-600 text-sm">Latest Respiratory Rate</p>
          <p class="font-bold text-xl text-blue-700">${patient.vitalSigns[0].respiratoryRate} bpm</p>
        </div>
      </div>
      </c:if>

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
            <c:forEach items="${patient.vitalSigns}" var="v">
            <tr class="border-t hover:bg-gray-50">
              <td class="px-4 py-3">${v.createdAt}</td>
              <td class="px-4 py-3">${v.bloodPressure} mmHg</td>
              <td class="px-4 py-3">${v.heartRate} bpm</td>
              <td class="px-4 py-3">${v.bodyTemperature}°C</td>
              <td class="px-4 py-3">${v.respiratoryRate} bpm</td>
              <td class="px-4 py-3">${v.weight} kg</td>
              <td class="px-4 py-3">${v.height} cm</td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>

  </div>

  <!-- EDIT PATIENT MODAL -->
  <div id="editPatient" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <h3 class="text-xl font-bold mb-4">Edit Patient Information</h3>
      <form action="${pageContext.request.contextPath}/nurse/patient/update" method="post">
        <input type="hidden" name="patientId" value="${patient.id}">

        <label class="block mb-2">
          <span class="text-gray-700">First Name</span>
          <input type="text" name="firstName" value="${patient.firstName}" required class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <label class="block mb-2">
          <span class="text-gray-700">Last Name</span>
          <input type="text" name="lastName" value="${patient.lastName}" required class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <label class="block mb-2">
          <span class="text-gray-700">Birth Date</span>
          <input type="date" name="birthDate" value="${patient.birthDate}" required class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <label class="block mb-2">
          <span class="text-gray-700">SSN</span>
          <input type="text" name="ssn" value="${patient.ssn}" required class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <label class="block mb-4">
          <span class="text-gray-700">Phone Number</span>
          <input type="text" name="phoneNumber" value="${patient.phoneNumber}" class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <div class="flex space-x-2">
          <button type="submit" class="bg-yellow-500 text-white px-4 py-2 rounded flex-1 hover:bg-yellow-600">Update</button>
          <button type="button" onclick="hideModal('editPatient')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <!-- ADD VITALS MODAL -->
  <div id="addVitals" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-lg">
      <h3 class="text-xl font-bold mb-4">Add New Vital Signs Reading</h3>
      <form action="${pageContext.request.contextPath}/nurse/patient/vitals/add" method="post">
        <input type="hidden" name="id" value="${patient.id}">
        <input type="hidden" name="ssn" value="${patient.ssn}">

        <div class="grid grid-cols-2 gap-4 mb-3">
          <label class="block">
            <span class="text-gray-700">Blood Pressure (mmHg)</span>
            <input type="text" name="bloodPressure" placeholder="120/80" class="w-full border rounded px-3 py-2 mt-1">
          </label>

          <label class="block">
            <span class="text-gray-700">Heart Rate (bpm)</span>
            <input type="number" name="heartRate" placeholder="72" class="w-full border rounded px-3 py-2 mt-1">
          </label>
        </div>

        <div class="grid grid-cols-2 gap-4 mb-3">
          <label class="block">
            <span class="text-gray-700">Temperature (°C)</span>
            <input type="number" step="0.1" name="bodyTemperature" placeholder="36.5" class="w-full border rounded px-3 py-2 mt-1">
          </label>

          <label class="block">
            <span class="text-gray-700">Respiratory Rate (bpm)</span>
            <input type="number" name="respiratoryRate" placeholder="16" class="w-full border rounded px-3 py-2 mt-1">
          </label>
        </div>

        <div class="grid grid-cols-2 gap-4 mb-3">
          <label class="block">
            <span class="text-gray-700">Weight (kg)</span>
            <input type="number" step="0.1" name="weight" placeholder="70.5" class="w-full border rounded px-3 py-2 mt-1">
          </label>

          <label class="block">
            <span class="text-gray-700">Height (cm)</span>
            <input type="number" step="0.1" name="height" placeholder="175.0" class="w-full border rounded px-3 py-2 mt-1">
          </label>
        </div>

        <div class="flex space-x-2">
          <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded flex-1 hover:bg-green-700">Save Reading</button>
          <button type="button" onclick="hideModal('addVitals')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
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
    document.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.classList.add('hidden');
        }
    });
  </script>
  </c:if>

</body>
</html>