<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50 min-h-screen">
<%@ include file="/pages/specialist/header.jsp"%>
<div class="max-w-7xl mx-auto p-6">
    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6">
            ${error}
        </div>
    </c:if>

    <!-- Success Alert -->
    <c:if test="${not empty success}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-6">
            ${success}
        </div>
    </c:if>

    <!-- Page Header -->
    <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Expertise Requests</h1>
        <p class="text-gray-600">Review and respond to consultation requests from generalists</p>
    </div>

    <!-- Filter Tabs -->
    <div class="bg-white rounded-lg shadow-sm mb-6">
        <div class="border-b border-gray-200">
            <nav class="flex -mb-px">
                <button class="filter-tab active px-6 py-4 text-sm font-medium border-b-2" data-type="all">
                    All Requests
                </button>
                <button class="filter-tab px-6 py-4 text-sm font-medium border-b-2" data-type="AsyncRequest">
                    Async Requests
                </button>
                <button class="filter-tab px-6 py-4 text-sm font-medium border-b-2" data-type="SyncRequest">
                    Sync Requests
                </button>
                <button class="filter-tab px-6 py-4 text-sm font-medium border-b-2" data-type="URGENT">
                    Urgent
                </button>
            </nav>
        </div>
    </div>

    <!-- Requests List -->
    <c:choose>
        <c:when test="${not empty requests}">
            <div class="space-y-4">
                <c:forEach items="${requests}" var="req">
                    <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 request-card"
                         data-type="${req['class'].simpleName}"
                         data-priority="${req.requestPrio}">
                        <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-4">
                            <!-- Left: Request Info -->
                            <div class="flex-1">
                                <div class="flex items-center gap-3 mb-3">
                                    <h3 class="text-lg font-semibold text-gray-900">
                                        Request #${req.id}
                                    </h3>
                                    <c:if test="${req['class'].simpleName == 'AsyncRequest'}">
                                        <span class="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs font-medium">
                                            ASYNC
                                        </span>
                                    </c:if>
                                    <c:if test="${req['class'].simpleName == 'SyncRequest'}">
                                        <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs font-medium">
                                            SYNC
                                        </span>
                                    </c:if>
                                    <span class="priority-badge ${req.requestPrio}">
                                        ${req.requestPrio}
                                    </span>
                                </div>

                                <!-- Patient & Generalist Info -->
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                    <div>
                                        <p class="text-gray-600 text-sm">Patient</p>
                                        <c:if test="${not empty req.consultation and not empty req.consultation.patient}">
                                            <p class="font-medium">${req.consultation.patient.firstName} ${req.consultation.patient.lastName}</p>
                                            <p class="text-gray-500 text-sm">SSN: ${req.consultation.patient.ssn}</p>
                                        </c:if>
                                    </div>
                                    <div>
                                        <p class="text-gray-600 text-sm">Requesting Generalist</p>
                                        <c:if test="${not empty req.consultation and not empty req.consultation.generalist}">
                                            <p class="font-medium">Dr. ${req.consultation.generalist.fullName}</p>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Consultation Details -->
                                <div class="mb-4">
                                    <p class="text-gray-600 text-sm">Consultation Motive</p>
                                    <c:if test="${not empty req.consultation}">
                                        <p class="text-gray-800">${req.consultation.motive}</p>
                                    </c:if>
                                </div>

                                <!-- Request Details -->
                                <c:if test="${req['class'].simpleName == 'AsyncRequest'}">
                                    <div class="bg-gray-50 rounded p-4 mb-4">
                                        <p class="text-gray-600 text-sm font-medium mb-2">Question</p>
                                        <p class="text-gray-800 whitespace-pre-wrap">${req.question}</p>
                                        <div class="flex items-center gap-4 mt-2">
                                            <span class="text-sm text-gray-500">
                                                Created: ${req.formattedCreatedAt}
                                            </span>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${req['class'].simpleName == 'SyncRequest'}">
                                    <div class="bg-gray-50 rounded p-4 mb-4">
                                        <p class="text-gray-600 text-sm font-medium mb-2">Meeting Details</p>
                                        <c:if test="${not empty req.timeSlot}">
                                            <p class="text-gray-800">
                                                <strong>Time:</strong> ${req.timeSlot.formattedStartTime} - ${req.timeSlot.formattedEndTime}
                                            </p>
                                            <p class="text-gray-800">
                                                <strong>Date:</strong> ${req.timeSlot.formattedDate}
                                            </p>
                                        </c:if>
                                        <c:if test="${req.meetingUrl != null}">
                                            <p class="text-gray-800 mt-2">
                                                <strong>Meeting Link:</strong> <a href="${req.meetingUrl}" class="text-blue-600 hover:underline" target="_blank">${req.meetingUrl}</a>
                                            </p>
                                        </c:if>
                                    </div>
                                </c:if>

                                <!-- Response (if exists) -->
                                <c:if test="${req['class'].simpleName == 'AsyncRequest' && req.response != null}">
                                    <div class="bg-blue-50 rounded p-4 border-l-4 border-blue-500">
                                        <p class="text-blue-900 font-medium text-sm mb-2">Your Response</p>
                                        <p class="text-blue-800 whitespace-pre-wrap">${req.response.answer}</p>
                                        <p class="text-blue-600 text-xs mt-2">Responded at: ${req.response.formattedCreatedAt}</p>
                                    </div>
                                </c:if>
                            </div>

                            <!-- Right: Actions -->
                            <div class="flex flex-col gap-2 lg:w-48">
                                <c:if test="${req['class'].simpleName == 'AsyncRequest' && req.response == null}">
                                    <button onclick="showResponseModal('${req.id}')"
                                            class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm font-medium">
                                        Respond
                                    </button>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/specialist/consultation/${req.consultation.id}"
                                   class="w-full bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700 text-center text-sm font-medium">
                                    View Consultation
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="bg-white rounded-lg shadow-sm p-12 text-center">
                <svg class="w-16 h-16 mx-auto text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
                <h3 class="text-lg font-semibold text-gray-900 mb-2">No Requests Found</h3>
                <p class="text-gray-600">You don't have any expertise requests at the moment.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Response Modal -->
<div id="responseModal" class="modal hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
        <h3 class="text-xl font-bold mb-4">Provide Response</h3>
        <form action="${pageContext.request.contextPath}/specialist/request/respond" method="POST">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <input type="hidden" name="requestId" id="responseRequestId">

            <label class="block mb-4">
                <span class="text-gray-700 font-medium">Your Response *</span>
                <textarea name="answer" rows="8"
                          placeholder="Provide your expert consultation response..."
                          class="w-full border rounded px-3 py-2 mt-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                          required></textarea>
            </label>

            <div class="flex space-x-2">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded flex-1 hover:bg-blue-700">
                    Submit Response
                </button>
                <button type="button" onclick="hideModal('responseModal')" class="bg-gray-300 px-4 py-2 rounded flex-1 hover:bg-gray-400">
                    Cancel
                </button>
            </div>
        </form>
    </div>
</div>

<style>
.priority-badge {
    padding: 0.25rem 0.75rem;
    border-radius: 9999px;
    font-size: 0.75rem;
    font-weight: 500;
}
.priority-badge.NORMAL {
    background-color: #e5e7eb;
    color: #374151;
}
.priority-badge.URGENT {
    background-color: #fee2e2;
    color: #991b1b;
}

.filter-tab {
    border-color: transparent;
    color: #6b7280;
    transition: all 0.2s;
}
.filter-tab:hover {
    color: #111827;
    border-color: #d1d5db;
}
.filter-tab.active {
    color: #2563eb;
    border-color: #2563eb;
}
</style>

<script>
function showResponseModal(requestId) {
    document.getElementById('responseRequestId').value = requestId;
    document.getElementById('responseModal').classList.remove('hidden');
}

function hideModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

// Close modal when clicking outside
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.add('hidden');
    }
});

// Filter functionality
document.addEventListener('DOMContentLoaded', function() {
    const filterTabs = document.querySelectorAll('.filter-tab');
    const requestCards = document.querySelectorAll('.request-card');

    filterTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const filterType = this.dataset.type;

            // Update active tab
            filterTabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            // Filter cards
            requestCards.forEach(card => {
                const cardType = card.dataset.type;
                const cardPriority = card.dataset.priority;

                if (filterType === 'all') {
                    card.style.display = 'block';
                } else if (filterType === 'URGENT') {
                    card.style.display = cardPriority === 'URGENT' ? 'block' : 'none';
                } else {
                    card.style.display = cardType === filterType ? 'block' : 'none';
                }
            });
        });
    });
});
</script>

</body>
</html>
