<%@ include file="/layout/meta.jsp" %>
<body>
<%@ include file="/pages/generalist/header.jsp" %>

<div class="max-w-7xl mx-auto p-6">
    <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold">Consultations List</h2>
    </div>

    <!-- Patients Table -->
    <div class="bg-white rounded shadow overflow-hidden">
        <table class="w-full">
            <thead class="bg-gray-100">
                <tr>
                    <th class="px-4 py-3 text-left">Date created</th>
                    <th class="px-4 py-3 text-left">Patient SSN</th>
                    <th class="px-4 py-3 text-left">Status</th>
                    <th class="px-4 py-3 text-left">Generalist ID</th>
                    <th class="px-4 py-3 text-left">cost</th>
                    <th class="px-4 py-3 text-left">medical acts used</th>
                    <th class="px-4 py-3 text-left">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${consultations}" var="c">
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-3">${c.formattedCreatedAt}</td>
                    <td class="px-4 py-3">${c.patient.ssn}</td>
                    <td class="px-4 py-3">${c.status}</td>
                    <td class="px-4 py-3">${c.generalist.id}</td>
                    <td class="px-4 py-3">${c.totalCost}</td>
                    <td class="px-4 py-3">${c.countMedicalActs}</td>
                    <td class="px-4 py-3">
                        <div class="flex space-x-2">
                                <a href="${pageContext.request.contextPath}/generalist/consultation/${c.id}"
                                    class="flex-1 text-center w-full bg-blue-500 text-white px-3 py-1 rounded text-sm hover:bg-blue-600">
                                    View
                                </a>
                        </div>
                    </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>