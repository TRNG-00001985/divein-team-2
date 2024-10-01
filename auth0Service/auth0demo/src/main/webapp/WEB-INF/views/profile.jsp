<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f4f7fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 350px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: 2px solid transparent;
            background-clip: padding-box;
        }

        .card:hover {
            transform: translateY(-10px) scale(1.05);
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            border: 2px solid #ffc0e7;
        }

        .card h2 {
            font-size: 28px;
            color: #2c3e50;
            margin-bottom: 25px;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
            font-weight: 700;
            transition: color 0.3s ease;
        }

        .card:hover h2 {
            color: #e74c3c;
        }

        .card p {
            font-size: 18px;
            color: #34495e;
            margin: 12px 0;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
            transition: color 0.3s ease, transform 0.3s ease;
        }

        .card p:hover {
            color: #9b59b6;
            transform: scale(1.05);
        }

        .card strong {
            color: #2c3e50;
            font-weight: 600;
            text-transform: uppercase;
        }

        .profile-picture {
            border-radius: 50%;
            width: 120px;
            height: 120px;
            margin-bottom: 20px;
            border: 4px solid transparent;
            background: linear-gradient(white, white), linear-gradient(135deg, #2ecc71, #3498db);
            background-origin: border-box;
            background-clip: content-box, border-box;
            transition: border-color 0.3s ease, background 0.4s ease;
        }

        .profile-picture:hover {
            background: linear-gradient(white, white), linear-gradient(135deg, #f39c12, #e74c3c);
        }

        .card p a {
            text-decoration: none;
            color: #b929b6;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .card p a:hover {
            color: #db34b1;
        }

        .card-footer p {
            font-size: 14px;
            color: #7f8c8d;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="card">
        <h2>My Account</h2>
        <p><strong>Email:</strong> ${profile['email']}</p>
        <p><strong>Name:</strong> ${profile['nickname']}</p>
        <p><strong>Picture:</strong> <img src="${profile['picture']}" alt="Profile Picture" class="profile-picture"></p>
        <p><strong>User Id:</strong> <%= (session.getAttribute("user_id") != null) ? session.getAttribute("user_id") : "Not available" %></p>
        <!-- <p><strong>Role:</strong> Administrator</p> -->
    </div>
</body>
</html>