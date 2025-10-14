<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50">
<%@ include file="/pages/specialist/header.jsp"%>
<div class="max-w-7xl mx-auto p-6">
    <div class="bg-white rounded-lg shadow-md p-6 mb-6">
          <div class="flex justify-between items-start mb-4">
            <h2 class="text-2xl font-bold text-gray-800">Specialist Profile</h2>
            <button onclick="showModal('editSpecialist')" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">
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
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">

            <!-- Patient ID -->
            <div>
              <p class="text-gray-500 text-sm">Specialist ID</p>
              <p class="font-semibold text-lg">${specialist.id}</p>
            </div>

            <!-- Full Name -->
            <div>
              <p class="text-gray-500 text-sm">Full Name</p>
              <p class="font-semibold text-lg">${specialist.fullName}</p>
            </div>


            <!-- SSN -->
            <div>
              <p class="text-gray-500 text-sm">Date Joined</p>
              <p class="font-semibold text-lg">${specialist.formattedCreatedAt}</p>
            </div>

            <!-- Contact Number -->
            <div>
              <p class="text-gray-500 text-sm">Specialty</p>
              <p class="font-semibold text-lg">${specialist.specialty != null ? specialist.specialty : "N/A"}</p>
            </div>

            <!-- Registration Date -->
            <div>
              <p class="text-gray-500 text-sm">Consultation duration</p>
              <p class="font-semibold text-lg">${specialist.duration}</p>
            </div>
            <div>
              <p class="text-gray-500 text-sm">Consultation fee</p>
              <p class="font-semibold text-lg">${specialist.fee != 0 ? specialist.fee : "N/A" }</p>
            </div>
        </div>
    </div>
</div>
 <!-- EDIT Specialist MODAL -->
  <div id="editSpecialist" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <h3 class="text-xl font-bold mb-4">Edit specialist profile</h3>
      <form action="${pageContext.request.contextPath}/specialist/update" method="post">
        <input type="hidden" name="specialistId" value="${specialist.id}">

        <label class="block mb-2">
          <span class="text-gray-700">Speciality</span>
            <select name="specialty" required class="w-full border rounded px-3 py-2 mt-1">
                <option value="">Choose a specialty ...</option>
                <c:forEach items="${specialities}" var="specialty">
                    <option value="${specialty}" ${specialist.specialty == specialty ? "SELECTED" : ""}>${specialty}</option>
                </c:forEach>
            </select>
        </label>

        <label class="block mb-4">
          <span class="text-gray-700">Consultation fee</span>
          <input type="text" name="fee" value="${specialist.fee != 0.0 ? specialist.fee : ''}" class="w-full border rounded px-3 py-2 mt-1">
        </label>

        <div class="flex space-x-2">
          <button type="submit" class="bg-yellow-500 text-white px-4 py-2 rounded flex-1 hover:bg-yellow-600">Update</button>
          <button type="button" onclick="hideModal('editSpecialist')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">Cancel</button>
        </div>
      </form>
    </div>
  </div>
  <script>
        function showModal(id){
            document.getElementById(id).classList.remove("hidden");
        }
        function hideModal(id){
            document.getElementById(id).classList.add("hidden");
        }
        document.addEventListener('click', function(event) {
            if (event.target.classList.contains('modal')) {
                event.target.classList.add('hidden');
            }
        });
  </script>
</body>
</html>