<%@ include file="/layout/meta.jsp" %>
<body class="bg-gray-50 min-h-screen">
<%@ include file="/pages/generalist/header.jsp"%>

<div class="max-w-4xl mx-auto p-6">
    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6">
                ${error}
        </div>
    </c:if>

    <!-- Page Header -->
    <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-gray-900 mb-2">Request Expert Consultation</h1>
        <p class="text-gray-600">Select a specialist and describe your consultation needs</p>
    </div>

    <c:choose>
    <c:when test="${not empty specialists}">
    <!-- Specialists List -->
    <div class="space-y-4">
        <c:forEach items="${specialists}" var="specialist">
        <div class="bg-white rounded-lg border border-gray-200 p-6">
            <!-- Specialist Info -->
            <div class="flex items-center justify-between">
                <div class="flex items-center space-x-4">
                    <div class="w-12 h-12 bg-blue-500 rounded-full flex items-center justify-center text-white font-bold">
                            ${fn:substring(specialist.fullName, 0, 1)}
                    </div>
                    <div>
                        <h3 class="font-semibold text-gray-900">${specialist.fullName}</h3>
                        <p class="text-gray-600">${specialist.specialty}</p>
                    </div>
                </div>
                <div class="text-right">
                    <div class="text-lg font-bold text-green-600">$${specialist.fee}</div>
                    <div class="text-sm text-gray-500">${specialist.duration} min</div>
                </div>
            </div>

            <!-- Select Specialist Button -->
            <button class="w-full mt-4 bg-blue-600 text-white py-2 rounded font-semibold hover:bg-blue-700 select-specialist-btn"
                    data-specialist-id="${specialist.id}"
                    data-specialist-name="${specialist.fullName}"
                    data-specialty="${specialist.specialty}"
                    data-fee="${specialist.fee}">
                Select Specialist
            </button>
        </div>
    </c:forEach>
</div>

<!-- Request Form -->
<div class="bg-white rounded-lg border border-gray-200 p-6 mt-8 hidden" id="requestForm">
    <h3 class="text-lg font-semibold mb-4">Consultation Request</h3>
    <form action="${pageContext.request.contextPath}/generalist/request/async" method="POST">
        <input type="hidden" name="csrfToken" value="${csrfToken}">
        <input type="hidden" name="specialistId" id="specialistId">
        <input type="hidden" name="consultationId" value="${param.consultationId}">

        <div id="specialistDetails" class="text-gray-600 mb-4 p-3 bg-gray-50 rounded">
            Please select a specialist above
        </div>

        <!-- Question/Description Input -->
        <div class="mb-4">
            <label for="question" class="block text-sm font-medium text-gray-700 mb-2">
                Your Question *
            </label>
            <textarea
                   id="question"
                   name="question"
                   rows="6"
                   placeholder="Describe your consultation needs and any specific questions you have..."
                   class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                   required></textarea>
            <p class="text-sm text-gray-500 mt-1">
                The specialist will review your question and respond when available
            </p>
        </div>

        <!-- Priority Selection -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
                Priority
            </label>
            <div class="space-y-2">
                <label class="flex items-center p-3 border border-gray-300 rounded cursor-pointer hover:bg-gray-50">
                    <input type="radio" name="priority" value="NORMAL" class="mr-3" checked>
                    <div>
                        <div class="font-medium">Normal Priority</div>
                        <div class="text-sm text-gray-500">Response within 7 days</div>
                    </div>
                </label>
                <label class="flex items-center p-3 border border-gray-300 rounded cursor-pointer hover:bg-gray-50">
                    <input type="radio" name="priority" value="URGENT" class="mr-3">
                    <div>
                        <div class="font-medium">High Priority</div>
                        <div class="text-sm text-gray-500">Response within 24 hours</div>
                    </div>
                </label>
            </div>
        </div>

        <button type="submit"
                class="w-full bg-green-600 text-white py-3 rounded font-semibold hover:bg-green-700">
            Submit Request
        </button>
    </form>
</div>

</c:when>
<c:otherwise>
    <!-- No Specialists Available -->
    <div class="bg-white rounded-lg border border-gray-200 p-8 text-center">
        <div class="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center">
            <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/>
            </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-900 mb-2">No Specialists Available</h3>
        <p class="text-gray-600 mb-4">No specialists are currently available for your request.</p>
        <button onclick="history.back()" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
            Go Back
        </button>
    </div>
</c:otherwise>
</c:choose>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const selectButtons = document.querySelectorAll('.select-specialist-btn');
        const requestForm = document.getElementById('requestForm');
        const specialistDetails = document.getElementById('specialistDetails');
        const specialistIdInput = document.getElementById('specialistId');
        const questionInput = document.getElementById('question');

        // Handle specialist selection
        selectButtons.forEach(button => {
            button.addEventListener('click', function() {
                const specialistId = this.dataset.specialistId;
                const specialistName = this.dataset.specialistName;
                const specialty = this.dataset.specialty;
                const fee = this.dataset.fee;

                // Update form with specialist info
                specialistDetails.innerHTML = "<strong>" + specialistName + "</strong> - " + specialty + "<br><strong>Fee: $" + fee + "</strong>";

                specialistIdInput.value = specialistId;

                // Show request form
                requestForm.classList.remove('hidden');

                // Clear question when changing specialist
                questionInput.value = '';

                // Scroll to form
                requestForm.scrollIntoView({ behavior: 'smooth', block: 'center' });
            });
        });
    });
</script>
</body>
</html>