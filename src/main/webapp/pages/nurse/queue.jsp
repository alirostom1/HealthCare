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

            <h2 class="text-2xl font-bold mb-4">Patient Queue</h2>
            <div class="bg-white rounded shadow">
                <ul class="divide-y">
                    <c:forEach items="${queue}" var="q">
                    <li class="px-4 py-3 flex justify-between items-center">
                        <div>
                            <span class="font-semibold">${q.patient.firstName} ${q.patient.lastName}</span>
                            <span class="text-gray-500 text-sm ml-2">${q.patient.ssn}</span>
                        </div>
                        <form action="${pageContext.request.contextPath}/nurse/queue/remove" method="POST" class="inline">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">
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
</body>
</html>