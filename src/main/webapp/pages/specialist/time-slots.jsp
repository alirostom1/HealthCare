<%@ include file="/layout/meta.jsp" %>
<body class="bg-gradient-to-br from-blue-50 to-indigo-50 min-h-screen">
<%@ include file="/pages/specialist/header.jsp"%>

<div class="max-w-6xl mx-auto p-6">
    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="bg-red-50 border-l-4 border-red-500 p-4 rounded-lg shadow-sm mb-6 animate-fade-in">
            <div class="flex items-center">
                <div class="flex-shrink-0">
                    <svg class="h-5 w-5 text-red-500" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                    </svg>
                </div>
                <div class="ml-3">
                    <p class="text-sm text-red-700">${error}</p>
                </div>
            </div>
        </div>
    </c:if>
    <c:choose>
    <c:when test="${param.date != null}">
        <!-- Header Section - Always show when date is selected -->
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 gap-4">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Time Slots</h1>
                <p class="text-lg text-gray-600 mt-2">Available slots for <span class="font-semibold text-indigo-600">${param.date}</span></p>
            </div>
            <button onclick="showModal('viewTimeSlots')"
                    class="flex items-center gap-2 bg-gradient-to-r from-green-500 to-emerald-600 text-white px-6 py-3 rounded-xl shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-200 font-semibold">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                Change Date
            </button>
        </div>

        <c:choose>
        <c:when test="${not empty timeSlots}">
            <!-- Time Slots Table -->
            <div class="bg-white rounded-2xl shadow-lg overflow-hidden border border-gray-100">
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gradient-to-r from-gray-50 to-gray-100">
                            <tr>
                                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Start Time</th>
                                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">End Time</th>
                                <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Status</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <c:forEach items="${timeSlots}" var="t" varStatus="status">
                            <tr class="hover:bg-gray-50 transition-colors duration-150 ${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <div class="w-2 h-2 bg-blue-500 rounded-full mr-3"></div>
                                        <span class="text-sm font-medium text-gray-900">${t.formattedStartTime}</span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <div class="w-2 h-2 bg-purple-500 rounded-full mr-3"></div>
                                        <span class="text-sm font-medium text-gray-900">${t.formattedEndTime}</span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <c:choose>
                                        <c:when test="${t.status == 'AVAILABLE'}">
                                            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
                                                </svg>
                                                ${t.status}
                                            </span>
                                        </c:when>
                                        <c:when test="${t.status == 'UNAVAILABLE'}">
                                            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800">
                                                <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"/>
                                                </svg>
                                                ${t.status}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                                                ${t.status}
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Empty State -->
            <div class="flex flex-col items-center justify-center py-16 px-6">
                <div class="bg-white rounded-2xl shadow-lg p-8 max-w-md w-full text-center border border-gray-100">
                    <div class="w-20 h-20 mx-auto mb-6 bg-indigo-50 rounded-full flex items-center justify-center">
                        <svg class="w-10 h-10 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                        </svg>
                    </div>
                    <h3 class="text-xl font-bold text-gray-900 mb-2">No Time Slots Available</h3>
                    <p class="text-gray-600 mb-6">There are no time slots scheduled for ${param.date}.</p>
                    <button onclick="showModal('viewTimeSlots')"
                            class="bg-gradient-to-r from-indigo-500 to-purple-600 text-white px-6 py-3 rounded-xl hover:shadow-lg transform hover:-translate-y-0.5 transition-all duration-200 font-semibold">
                        Choose Another Date
                    </button>
                </div>
            </div>
        </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
    <!-- Empty State -->
    <div class="flex flex-col items-center justify-center py-16 px-6">
        <div class="bg-white rounded-2xl shadow-lg p-8 max-w-md w-full text-center border border-gray-100">
            <div class="w-20 h-20 mx-auto mb-6 bg-indigo-50 rounded-full flex items-center justify-center">
                <svg class="w-10 h-10 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-2">No Time Slots Available</h3>
            <p class="text-gray-600 mb-6">There are no time slots scheduled for ${param.date}.</p>
            <button onclick="showModal('viewTimeSlots')"
                    class="bg-gradient-to-r from-indigo-500 to-purple-600 text-white px-6 py-3 rounded-xl hover:shadow-lg transform hover:-translate-y-0.5 transition-all duration-200 font-semibold">
                Choose Another Date
            </button>
        </div>
    </div>
    </c:otherwise>
    </c:choose>
</div>
<!-- Change Date Modal -->
<div id="viewTimeSlots" class="fixed inset-0 bg-gray-600/80 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-2xl bg-white">
        <!-- Modal Header -->
        <div class="flex items-center justify-between mb-6">
            <h3 class="text-xl font-bold text-gray-900">Select Date</h3>
            <button onclick="closeModal('viewTimeSlots')" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>

        <!-- Modal Body -->
        <div class="mb-6">
            <label for="selectedDate" class="block text-sm font-medium text-gray-700 mb-2">
                Choose a date to view time slots
            </label>
            <input type="date"
                   id="selectedDate"
                   name="date"
                   class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors duration-200"
                   required>
        </div>

        <!-- Modal Footer -->
        <div class="flex gap-3 justify-end">
            <button onclick="closeModal('viewTimeSlots')"
                    class="px-4 py-2 text-gray-600 border border-gray-300 rounded-xl hover:bg-gray-50 transition-colors duration-200 font-medium">
                Cancel
            </button>
            <button onclick="submitDate()"
                    class="px-6 py-2 bg-indigo-600 text-white rounded-xl hover:bg-indigo-700 transition-colors duration-200 font-medium">
                View Slots
            </button>
        </div>
    </div>
</div>

<style>
    @keyframes fade-in {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .animate-fade-in {
        animation: fade-in 0.3s ease-out;
    }
</style>
<script>
function showModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

function submitDate() {
    const selectedDate = document.getElementById('selectedDate').value;
    if (selectedDate) {
        window.location.href = '?date=' + selectedDate;
    }
}

document.getElementById('viewTimeSlots').addEventListener('click', function(e) {
    if (e.target.id === 'viewTimeSlots') {
        closeModal('viewTimeSlots');
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('selectedDate').value = today;
});
</script>
</body>
</html>