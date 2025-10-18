<nav class="bg-blue-600 text-white px-6 py-4 flex justify-between items-center shadow">

    <h1 class="text-xl font-bold">Nurse Dashboard</h1>

    <!-- Center: Navigation links -->
    <div class="flex space-x-6">
        <a href="${pageContext.request.contextPath}/nurse/"
           class="hover:text-gray-200 font-medium transition">
            Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/nurse/queue"
           class="hover:text-gray-200 font-medium transition">
            Queue
        </a>
    </div>

    <!-- Right: Logout -->
    <form action="${pageContext.request.contextPath}/auth/logout" method="post">
        <button type="submit"
                class="bg-white text-blue-600 px-4 py-2 rounded hover:bg-gray-100 transition">
            Logout
        </button>
        <input type="hidden" name="csrfToken" value="${csrfToken}">
    </form>
</nav>
