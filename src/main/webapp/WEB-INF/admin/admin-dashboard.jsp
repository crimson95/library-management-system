<%--
  Admin dashboard view.
  Expects request attributes from AdminDashboardServlet:
  totalBooks, availableCopies, borrowedCopies, overdueRecords.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Admin Dashboard</title>
    <style>
      body { font-family: Arial, sans-serif; margin: 40px; }
      h1 { color: red; margin-bottom: 16px; }
      .navbar { display: flex; justify-content: space-between; align-items: center; }
      .grid { display: grid; gap: 16px; }
      .cards { grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); }
      .card { background: #ffefef; padding: 20px; border: 1px solid #cacaca; border-radius: 8px; }
      .actions { display: flex; gap: 12px; flex-wrap: wrap; margin: 12px 0; }
      .btn { display:inline-block; padding: 8px 12px; background: #e40a0a; color: #fff; text-decoration: none; border-radius: 6px; }
      .btn.secondary { background-color: #333; }
      .section { margin-top: 24px; }
    </style>
</head>
<body>
  <%-- Dashboard title + top-level navigation --%>
  <h1>Admin Dashboard</h1>
  <nav class="navbar">
      <div class="actions">
          <a class="btn" href="${pageContext.request.contextPath}/admin/books">Manage Books</a>
          <a class="btn" href="${pageContext.request.contextPath}/admin/members">Manage Members</a>
          <a class="btn" href="${pageContext.request.contextPath}/admin/authors">Manage Authors</a>
          <a class="btn" href="${pageContext.request.contextPath}/admin/publishers">Manage Publishers</a>
          <a class="btn" href="${pageContext.request.contextPath}/admin/circulation">Circulation Desk</a>
      </div>
      <div class="logout">
          <a class="btn" href="${pageContext.request.contextPath}/logout">Logout</a>
      </div>
  </nav>

  <%-- Summary cards --%>
  <div class="section">
      <h2>Summary</h2>
      <div class="grid cards">
          <div class="card">Total Books<br><strong style="color: red; font-size: 24px;"><%= request.getAttribute("totalBooks") != null ? request.getAttribute("totalBooks") : 0 %></strong></div>
          <div class="card">Available Copies<br><strong style="color: #27ae60; font-size: 24px;"><%= request.getAttribute("availableCopies") != null ? request.getAttribute("availableCopies") : 0 %></strong></div>
          <div class="card">Borrowed Copies<br><strong style="color: #e67e22; font-size: 24px;"><%= request.getAttribute("borrowedCopies") != null ? request.getAttribute("borrowedCopies") : 0 %></strong></div>
          <div class="card">Overdue Records<br><strong style="color: #7c1904; font-size: 24px;"><%= request.getAttribute("overdueRecords") != null ? request.getAttribute("overdueRecords") : 0 %></strong></div>
      </div>
  </div>

  <%-- Quick links to common admin operations --%>
  <div class="section">
      <h2>Quick Actions</h2>
      <div class="actions">
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/books?action=add-book">Add New Book</a>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/members?action=add-admin">Add Admin User</a>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/authors?action=add">Add New Author</a>
          <a class="btn secondary" href="${pageContext.request.contextPath}/admin/publishers?action=add">Add New Publisher</a>
      </div>
  </div>
</body>
</html>
