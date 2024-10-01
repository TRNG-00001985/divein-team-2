<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ProShop Header</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: url('https://cdn.pixabay.com/photo/2017/03/13/17/26/ecommerce-2140603_640.jpg') no-repeat center center fixed;
            background-size: cover;
            color: #333;
        }
        .navbar {
            background-color: #e6e6fa; 
        }
        .navbar-brand {
            color: #000; 
        }
        .navbar-nav .nav-link {
            color: #000; 
        }
        .navbar-nav .nav-link:hover {
            color: #5c5c5c; 
        }
        .btn-outline-primary {
            background-color: #e6e6fa; 
            color: #000; 
        }
        .hero-section {
            position: relative;
            text-align: center;
            color: #000; 
            padding: 100px 20px; 
        }
        .hero-section h1 {
            font-size: 3rem;
            margin-bottom: 20px;
        }
        .hero-section p {
            font-size: 1.5rem; 
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="home.jsp">ProShop</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="home.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/product/browseProducts">Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="categories">Categories</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="orders">Orders</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="profile">Profile</a>
                    </li>
                </ul>
                <form class="d-flex">
                    <a class="btn btn-outline-primary" href="favorites">
                        <i class="bi bi-heart-fill"></i>
                    </a>
                    <a class="btn btn-outline-primary ms-2" href="cart">
                        <i class="bi bi-cart-fill"></i>
                    </a>
                    <a class="btn btn-outline-primary ms-2" href="logout">Logout</a>
                </form>
            </div>
        </div>
    </nav>

    <div class="hero-section">
        <h1>Welcome to ProShop!</h1>
        <p>Your one-stop shop for all your needs.</p>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
