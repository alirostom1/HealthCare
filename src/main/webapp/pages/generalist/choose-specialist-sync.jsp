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
        <h1 class="text-2xl font-bold text-gray-900 mb-2">Choose a Specialist</h1>
        <p class="text-gray-600">Select a specialist to see their available time slots</p>
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
                        View Available Time Slots
                    </button>

                    <!-- Time Slots (Hidden by default) -->
                    <div id="timeSlots-${specialist.id}" class="hidden mt-4">
                        <h4 class="font-medium text-gray-900 mb-3">Available Time Slots:</h4>
                        <c:set var="currentDate" value=""/>
                        <c:forEach items="${specialist.timeSlots}" var="slot">
                            <c:if test="${slot.status == 'AVAILABLE' && slot.isAvailable()}">
                                <c:set var="slotDate" value="${slot.startTime.toLocalDate()}"/>
                                <c:if test="${slotDate != currentDate}">
                                    <c:if test="${not empty currentDate}">
                                        </div>
                                    </c:if>
                                    <button class="date-toggle-btn w-full text-left p-3 bg-gray-100 rounded font-semibold text-gray-900 hover:bg-gray-200 mt-3"
                                            data-date="${slotDate}"
                                            data-specialist-id="${specialist.id}">
                                        ${slotDate} &#9660;
                                    </button>
                                    <div class="date-slots hidden grid grid-cols-1 md:grid-cols-2 gap-2 mt-2" data-date="${slotDate}">
                                    <c:set var="currentDate" value="${slotDate}"/>
                                </c:if>
                                <label class="flex items-center p-3 border border-gray-300 rounded cursor-pointer hover:bg-gray-50">
                                    <input type="radio" name="selectedSlot"
                                           value="${slot.id}"
                                           class="mr-3"
                                           data-specialist-id="${specialist.id}"
                                           data-slot-id="${slot.id}">
                                    <div>
                                        <div class="font-medium">${slot.formattedStartTime} - ${slot.formattedEndTime}</div>
                                    </div>
                                </label>
                            </c:if>
                        </c:forEach>
                        <c:if test="${not empty currentDate}">
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Booking Form -->
        <div class="bg-white rounded-lg border border-gray-200 p-6 mt-8 hidden" id="bookingForm">
            <h3 class="text-lg font-semibold mb-4">Booking Summary</h3>
            <form action="${pageContext.request.contextPath}/generalist/request/sync" method="POST">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="specialistId" id="specialistId">
                <input type="hidden" name="timeSlotId" id="timeSlotId">
                <input type="hidden" name="consultationId" value="${param.consultationId}">

                <div id="bookingDetails" class="text-gray-600 mb-4">
                    Please select a time slot above
                </div>

                <!-- Meeting URL Input -->
                <div class="mb-4">
                    <label for="meetingUrl" class="block text-sm font-medium text-gray-700 mb-2">
                        Meeting URL *
                    </label>
                    <input type="url"
                           id="meetingUrl"
                           name="meetingUrl"
                           placeholder="https://meet.google.com/abc-def-ghi"
                           class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                           required>
                    <p class="text-sm text-gray-500 mt-1">
                        Enter the video meeting link (Google Meet, Zoom, Teams, etc.)
                    </p>
                </div>

                <button type="submit"
                        class="w-full bg-green-600 text-white py-3 rounded font-semibold hover:bg-green-700">
                    Confirm Booking
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
    const bookingForm = document.getElementById('bookingForm');
    const bookingDetails = document.getElementById('bookingDetails');
    const specialistIdInput = document.getElementById('specialistId');
    const timeSlotIdInput = document.getElementById('timeSlotId');
    const meetingUrlInput = document.getElementById('meetingUrl');

    let selectedSpecialistId = null;

    // Handle specialist selection
    selectButtons.forEach(button => {
        button.addEventListener('click', function() {
            const specialistId = this.dataset.specialistId;
            const specialistName = this.dataset.specialistName;
            const specialty = this.dataset.specialty;
            const fee = this.dataset.fee;

            // Hide all time slots
            document.querySelectorAll('[id^="timeSlots-"]').forEach(el => {
                el.classList.add('hidden');
            });

            // Reset all radio buttons
            document.querySelectorAll('input[name="selectedSlot"]').forEach(radio => {
                radio.checked = false;
            });

            const timeSlotsDiv = document.getElementById("timeSlots-" + specialistId);
            timeSlotsDiv.classList.remove('hidden');

            // Update booking form with specialist info
            bookingDetails.innerHTML = "<strong> " + specialistName + "</strong> - " + specialty + "<br>Please select a time slot<br><strong>$" + fee + "</strong> ";

            selectedSpecialistId = specialistId;
            specialistIdInput.value = specialistId;

            // Show booking form
            bookingForm.classList.remove('hidden');

            // Clear meeting URL when changing specialist
            meetingUrlInput.value = '';

            // Scroll to time slots
            timeSlotsDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        });
    });

    // Handle date toggle button clicks
    document.addEventListener('click', function(e) {
        if (e.target.closest('.date-toggle-btn')) {
            const btn = e.target.closest('.date-toggle-btn');
            const date = btn.dataset.date;
            const specialistId = btn.dataset.specialistId;

            // Find the slots container for this date
            const slotsDiv = btn.nextElementSibling;

            // Toggle visibility
            slotsDiv.classList.toggle('hidden');

            // Update arrow
            btn.textContent = slotsDiv.classList.contains('hidden') ?
                btn.textContent.replace('▲', '▼') :
                btn.textContent.replace('▼', '▲');
        }
    });

    // Handle time slot selection
    document.addEventListener('change', function(e) {
        if (e.target.name === 'selectedSlot') {
            const slotId = e.target.value;
            const specialistId = e.target.dataset.specialistId;

            timeSlotIdInput.value = slotId;

            // Update booking details with selected time
            const selectedTime = e.target.parentElement.querySelector('.font-medium').textContent;
            const currentDetails = bookingDetails.innerHTML;
            bookingDetails.innerHTML = currentDetails.replace('Please select a time slot', selectedTime);
        }
    });
});
</script>
</body>
</html>