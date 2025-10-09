<%@ include file="/layout/meta.jsp" %>
<body class="bg-gradient-to-r from-blue-500 to-cyan-400 flex justify-center items-center h-screen">

  <div class="bg-white shadow-lg rounded-2xl w-full max-w-md p-8">
    <h1 class="text-2xl font-bold text-center mb-6 text-gray-800">Login</h1>
    <c:if test="${error != null && error != ''}">
          <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            ${error}
          </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/login" method="POST" class="space-y-4">
      <input type="text" name="username" placeholder="Username" required
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <input type="password" name="password" placeholder="Password" required
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-2 focus:ring-blue-400">

      <button type="submit"
        class="w-full bg-blue-500 text-white py-2 rounded-lg font-semibold hover:bg-blue-600 transition">
        Login
      </button>

      <p class="text-center text-sm text-gray-600">
        Don't have an account?
        <a href="${pageContext.request.contextPath}/auth/register" class="text-blue-500 font-medium hover:underline">Register</a>
      </p>
    </form>
  </div>
 </body>
 </html>