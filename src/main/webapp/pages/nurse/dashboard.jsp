<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50">
<%@ include file="/pages/nurse/header.jsp"%>
  <div class="max-w-7xl mx-auto p-6">

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
              <th class="px-4 py-3 text-left">ID</th>
              <th class="px-4 py-3 text-left">Name</th>
              <th class="px-4 py-3 text-left">Age</th>
              <th class="px-4 py-3 text-left">Gender</th>
              <th class="px-4 py-3 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            <!-- JSP Loop: c:forEach items="${patients}" var="p" -->
            <tr class="border-t hover:bg-gray-50">
              <td class="px-4 py-3">001</td>
              <td class="px-4 py-3">John Doe</td>
              <td class="px-4 py-3">45</td>
              <td class="px-4 py-3">Male</td>
              <td class="px-4 py-3">
                <button onclick="editPatient(1)" class="bg-yellow-500 text-white px-3 py-1 rounded text-sm mr-1">Edit</button>
                <button onclick="showVitals(1)" class="bg-blue-500 text-white px-3 py-1 rounded text-sm mr-1">Vitals</button>
                <button onclick="addToQueue(1, 'John Doe')" class="bg-purple-600 text-white px-3 py-1 rounded text-sm">Queue</button>
              </td>
            </tr>
            <tr class="border-t hover:bg-gray-50">
              <td class="px-4 py-3">002</td>
              <td class="px-4 py-3">Jane Smith</td>
              <td class="px-4 py-3">32</td>
              <td class="px-4 py-3">Female</td>
              <td class="px-4 py-3">
                <button onclick="editPatient(2)" class="bg-yellow-500 text-white px-3 py-1 rounded text-sm mr-1">Edit</button>
                <button onclick="showVitals(2)" class="bg-blue-500 text-white px-3 py-1 rounded text-sm mr-1">Vitals</button>
                <button onclick="addToQueue(2, 'Jane Smith')" class="bg-purple-600 text-white px-3 py-1 rounded text-sm">Queue</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- QUEUE TAB -->
    <div id="queue" class="tab-content hidden">
      <h2 class="text-2xl font-bold mb-4">Patient Queue</h2>
      <div class="bg-white rounded shadow">
        <ul id="queueList" class="divide-y">
          <!-- JSP Loop: c:forEach items="${queue}" var="q" -->
          <li class="px-4 py-3 flex justify-between items-center">
            <div>
              <span class="font-semibold">John Doe</span>
              <span class="text-gray-500 text-sm ml-2">(ID: 001)</span>
            </div>
            <form action="removeFromQueue" method="post" style="display:inline">
              <input type="hidden" name="patientId" value="1">
              <button type="submit" class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600">Remove</button>
            </form>
          </li>
        </ul>
      </div>
    </div>

  </div>

  <!-- ADD PATIENT MODAL -->
  <div id="addPatient" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <h3 class="text-xl font-bold mb-4">Add New Patient</h3>
      <form action="addPatient" method="post">
        <input type="text" name="name" placeholder="Full Name" required class="w-full border rounded px-3 py-2 mb-3">
        <input type="number" name="age" placeholder="Age" required class="w-full border rounded px-3 py-2 mb-3">
        <select name="gender" required class="w-full border rounded px-3 py-2 mb-3">
          <option value="">Select Gender</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
        </select>
        <input type="text" name="contact" placeholder="Contact Number" class="w-full border rounded px-3 py-2 mb-4">

        <h4 class="font-semibold mb-2">Initial Vital Signs</h4>
        <input type="text" name="bloodPressure" placeholder="Blood Pressure (e.g. 120/80)" class="w-full border rounded px-3 py-2 mb-2">
        <input type="text" name="heartRate" placeholder="Heart Rate (bpm)" class="w-full border rounded px-3 py-2 mb-2">
        <input type="text" name="temperature" placeholder="Temperature (°C)" class="w-full border rounded px-3 py-2 mb-4">

        <div class="flex space-x-2">
          <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded flex-1 hover:bg-green-700">Save</button>
          <button type="button" onclick="hideModal('addPatient')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <!-- EDIT PATIENT MODAL -->
  <div id="editPatient" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <h3 class="text-xl font-bold mb-4">Edit Patient</h3>
      <form action="updatePatient" method="post">
        <input type="hidden" name="patientId" id="editPatientId">
        <input type="text" name="name" id="editName" placeholder="Full Name" required class="w-full border rounded px-3 py-2 mb-3">
        <input type="number" name="age" id="editAge" placeholder="Age" required class="w-full border rounded px-3 py-2 mb-3">
        <select name="gender" id="editGender" required class="w-full border rounded px-3 py-2 mb-3">
          <option value="Male">Male</option>
          <option value="Female">Female</option>
        </select>
        <input type="text" name="contact" id="editContact" placeholder="Contact Number" class="w-full border rounded px-3 py-2 mb-4">

        <div class="flex space-x-2">
          <button type="submit" class="bg-yellow-500 text-white px-4 py-2 rounded flex-1 hover:bg-yellow-600">Update</button>
          <button type="button" onclick="hideModal('editPatient')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <!-- VITALS MODAL -->
  <div id="vitalsModal" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-screen overflow-y-auto">
      <h3 class="text-xl font-bold mb-4">Vital Signs History</h3>
      <div id="vitalsPatientName" class="text-gray-600 mb-4"></div>

      <!-- Add New Vitals Form -->
      <form action="addVitals" method="post" class="bg-gray-50 p-4 rounded mb-4">
        <input type="hidden" name="patientId" id="vitalsPatientId">
        <h4 class="font-semibold mb-2">Add New Reading</h4>
        <div class="grid grid-cols-3 gap-2">
          <input type="text" name="bloodPressure" placeholder="BP (120/80)" class="border rounded px-3 py-2">
          <input type="text" name="heartRate" placeholder="HR (bpm)" class="border rounded px-3 py-2">
          <input type="text" name="temperature" placeholder="Temp (°C)" class="border rounded px-3 py-2">
        </div>
        <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded mt-2 hover:bg-blue-700">Save Reading</button>
      </form>

      <!-- Vitals History Table -->
      <table class="w-full border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 text-left">Date/Time</th>
            <th class="px-3 py-2 text-left">Blood Pressure</th>
            <th class="px-3 py-2 text-left">Heart Rate</th>
            <th class="px-3 py-2 text-left">Temperature</th>
          </tr>
        </thead>
        <tbody>
          <!-- JSP Loop: c:forEach items="${vitals}" var="v" -->
          <tr class="border-t">
            <td class="px-3 py-2">2025-10-09 10:30</td>
            <td class="px-3 py-2">120/80</td>
            <td class="px-3 py-2">72 bpm</td>
            <td class="px-3 py-2">36.5°C</td>
          </tr>
          <tr class="border-t">
            <td class="px-3 py-2">2025-10-09 14:15</td>
            <td class="px-3 py-2">118/78</td>
            <td class="px-3 py-2">70 bpm</td>
            <td class="px-3 py-2">36.7°C</td>
          </tr>
        </tbody>
      </table>

      <button type="button" onclick="hideModal('vitalsModal')" class="bg-gray-300 px-4 py-2 rounded mt-4 hover:bg-gray-400">Close</button>
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

    // Edit patient
    function editPatient(id) {
      // In JSP, populate with actual data from server
      document.getElementById('editPatientId').value = id;
      showModal('editPatient');
    }

    // Show vitals
    function showVitals(id) {
      document.getElementById('vitalsPatientId').value = id;
      showModal('vitalsModal');
    }

    // Add to queue
    function addToQueue(id, name) {
      const form = document.createElement('form');
      form.method = 'post';
      form.action = 'addToQueue';

      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'patientId';
      input.value = id;

      form.appendChild(input);
      document.body.appendChild(form);
      form.submit();
    }
  </script>

</body>
</html>