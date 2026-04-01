<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Library Management System</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; }

        /* Navigation Bar Styles */
        .navbar { background: #532828; padding: 15px 40px; display: flex; justify-content: space-between; align-items: center; }
        .navbar .logo { color: white; font-size: 24px; font-weight: bold; text-decoration: none; }
        .navbar .links a { color: #ecf0f1; text-decoration: none; margin-left: 20px; font-weight: bold; transition: color 0.3s; }
        .navbar .links a:hover { color: #960000; }

        /* Hero Section Styles */
        .hero { background: linear-gradient(135deg, #532828 0%, #960000 100%); color: white; padding: 100px 20px; text-align: center; }
        .hero h1 { font-size: 48px; margin: 0 0 20px 0; }
        .hero p { font-size: 20px; max-width: 600px; margin: 0 auto 40px auto; line-height: 1.6; color: #ecf0f1; }

        /* Button Styles */
        .btn-group { display: flex; justify-content: center; gap: 20px; }
        .btn { padding: 15px 30px; font-size: 18px; font-weight: bold; border-radius: 8px; text-decoration: none; transition: transform 0.2s, box-shadow 0.2s; }
        .btn-primary { background: #e74c3c; color: white; box-shadow: 0 4px 15px rgba(231, 76, 60, 0.4); }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(231, 76, 60, 0.6); }
        .btn-outline { background: transparent; color: white; border: 2px solid white; }
        .btn-outline:hover { background: rgba(255,255,255,0.1); transform: translateY(-2px); }

        /* Feature Cards Styles */
        .features { display: flex; justify-content: center; gap: 30px; padding: 60px 20px; max-width: 1000px; margin: 0 auto; flex-wrap: wrap; }
        .feature-card { background: white; padding: 30px; border-radius: 10px; width: 280px; text-align: center; box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
        .feature-card h3 { color: #960000; margin-top: 0; }
        .feature-card p { color: #7f8c8d; line-height: 1.5; }

        /* Footer Styles */
        .footer { text-align: center; padding: 20px; background: #532828; color: #bdc3c7; margin-top: 40px; }
    </style>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="logo">LMS</a>
        <div class="links">
            <a href="${pageContext.request.contextPath}/login">Login</a>
            <a href="${pageContext.request.contextPath}/register">Register</a>
        </div>
    </nav>
    <div class="hero">
        <h1>Welcome to LMS</h1>
        <p>Your smart, modern, and efficient library management solution. Explore thousands of books or manage your library's circulation with ease.</p>
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Start Reading</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Join as Member</a>
        </div>
    </div>

    <div class="features">
        <div class="feature-card">
            <h3>Smart Catalog</h3>
            <p>Easily search for books by title, author, or ISBN. Our catalog is always up-to-date with real-time availability</p>
        </div>
        <div class="feature-card">
            <h3>Powerful Admin</h3>
            <p>Librarians have full control over inventory, authors, publishers, and member management.</p>
        </div>
        <div class="feature-card">
            <h3>Fast Circulation</h3>
            <p>A streamlined circulation desk allows for quick book checkouts, returns, and automatic late-fee tracking.</p>
        </div>
    </div>

    <div class="footer">
        &copy 2026 Library Management System. All Rights Reserved.
    </div>
</body>
</html>