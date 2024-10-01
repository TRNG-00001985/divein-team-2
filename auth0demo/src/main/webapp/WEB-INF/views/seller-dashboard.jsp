<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Seller Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0; /* Light gray background */
            margin: 0;
            padding: 0;
        }
        .container {
            padding: 20px;
            max-width: 1200px;
            margin: auto;
            background-color: #ffffff; 
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow */
        }
        h1 {
            text-align: center;
            color: #333333; /* Dark gray text */
            font-size: 2.5em;
            margin-bottom: 20px;
        }
        .btn {
            display: block;
            width: 200px;
            margin: 20px auto;
            padding: 15px;
            text-align: center;
            text-decoration: none;
            color: #ffffff; /* White text */
            background-color: #6a0dad; /* Lavender background */
            border-radius: 5px;
            font-size: 1.1em;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #4b0082; /* Darker lavender */
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .footer {
            text-align: center;
            margin-top: 30px;
            font-size: 0.9em;
            color: #666666; /* Light gray text */
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Welcome, </h1>
        </div>
        <a href="addproduct" class="btn">Add New Product</a>
		
        <a href="#" class="btn">View My Products</a>
        <a href="logout" class="btn">Logout</a>
        
    </div>
</body>
</html>
