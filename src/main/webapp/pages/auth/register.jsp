<%@ include file="/layout/meta.jsp" %>
<body class="bg-gradient-to-r from-blue-500 to-cyan-400 flex justify-center items-center h-screen">

  <div class="bg-white shadow-lg rounded-2xl w-full max-w-md p-8">
    <h1 class="text-2xl font-bold text-center mb-6 text-gray-800">Register</h1>

    <c:if test="${error != null && error != ''}">
          <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            ${error}
          </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/register" method="POST" class="space-y-4">
      <select name="role" class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400" required>
        <option value="">Select Role</option>
        <option value="nurse">Nurse</option>
        <option value="generalist">Generalist Doctor</option>
        <option value="specialist">Specialist Doctor</option>
      </select>

      <input type="text" name="fullname" placeholder="Full Name" required
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <input type="text" name="username" placeholder="Username" required
         class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <input type="email" name="email" placeholder="Email" required
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <input type="password" name="password" placeholder="Password" required
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <button type="submit"
        class="w-full bg-blue-500 text-white py-2 rounded-lg font-semibold hover:bg-blue-600 transition">
        Register
      </button>

      <p class="text-center text-sm text-gray-600">
        Already have an account?
        <a href="${pageContext.request.contextPath}/auth/login" class="text-blue-500 font-medium hover:underline">Login</a>
      </p>
    </form>
  </div>

</body>
</html>
