<nav class="bg-blue-600 text-white px-6 py-4 flex justify-between items-center shadow">
    <h1 class="text-xl font-bold">Nurse Dashboard</h1>
    <form action="${pageContext.request.contextPath}/auth/logout" method="post">
      <button type="submit" class="bg-white text-blue-600 px-4 py-2 rounded hover:bg-gray-100">Logout</button>
    </form>
</nav>
