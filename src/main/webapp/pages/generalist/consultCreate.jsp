<%@ include file="/layout/meta.jsp" %>
<body>
<%@ include file="/pages/generalist/header.jsp" %>
<div class="max-w-7xl mx-auto p-6">


    <c:choose>
        <c:when test="${empty error}">
           <div class="flex items-center justify-center">
               <div class="bg-white border rounded-lg p-6 w-full max-w-2xl max-h-[90%] overflow-y-auto">
                   <h3 class="text-xl font-bold mb-4">Create Consultation For SSN : ${patient.ssn}</h3>
                   <form action="${pageContext.request.contextPath}/generalist/consultation/create?id=${param.patientId}" method="post">

                       <!-- Personal Information -->
                       <div class="mb-6">
                          <label class="block text-sm font-medium text-gray-700 mb-1">Motive</label>
                          <input type="text" name="motive"
                                 class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                       </div>
                       <div class="mb-6">
                           <label class="block text-sm font-medium text-gray-700 mb-1">Observations</label>
                           <textarea type="text" name="observations"
                                  class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                       </div>
                        <div class="flex justify-end space-x-3">
                                <button type="submit"
                                        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                                    Create Consultation
                                </button>
                        </div>
                    </form>
                </div>
           </div>
        </c:when>
        <c:when test="${not empty error}">
            <div class="w-full h-[80vh] flex justify-center items-center">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
            </div>
        </c:when>
    </c:choose>
</body>
</html>