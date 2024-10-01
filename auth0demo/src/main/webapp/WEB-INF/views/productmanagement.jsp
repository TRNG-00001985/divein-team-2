<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4; /* Light background for the page */
        }

        .navbar {
            display: flex; /* Use Flexbox for horizontal alignment */
            align-items: center; /* Center items vertically */
            justify-content: space-between; /* Space items evenly */
            background-color: #007bff; /* Navbar background color */
            padding: 10px; /* Add some padding */
            color: white; /* Text color */
        }

        .navbar-nav {
            display: flex; /* Use Flexbox for horizontal alignment */
            list-style: none; /* Remove default list styles */
            padding: 0; /* Remove default padding */
            margin: 0; /* Remove default margin */
        }

        .nav-item {
            margin: 0 15px; /* Add horizontal margin between items */
        }

        .nav-link {
            color: white; /* Link color */
            text-decoration: none; /* Remove underline */
            font-weight: bold; /* Bold font for links */
        }

        .nav-link:hover {
            text-decoration: underline; /* Underline on hover */
        }

        .dashboard-item {
            margin-bottom: 20px;
        }

        .product-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around; /* Center items */
            margin-top: 20px;
        }

        .product-card {
            background-color: #ffffff; /* White background for cards */
            border: 1px solid #dee2e6; /* Light border color */
            border-radius: 5px; /* Rounded corners for cards */
            margin: 10px;
            padding: 10px;
            text-align: center; /* Center text within cards */
            width: 200px; /* Fixed width for cards */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Subtle shadow */
        }

        .product-image {
            width: 100px; /* Set a fixed width for images */
            height: auto;
            border-radius: 5px; /* Rounded corners for images */
        }

        .product-price {
            font-size: 18px;
            color: #28a745; /* Green color for price */
            margin: 10px 0;
        }

        .product-description {
            font-size: 14px; /* Smaller font for description */
            color: #555; /* Gray color */
            margin: 5px 0;
        }

        .action-button {
            margin: 5px; /* Add some margin between buttons */
            cursor: pointer; /* Show pointer cursor on hover */
            background-color: #007bff; /* Bootstrap primary color */
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 3px; /* Rounded corners for buttons */
            transition: background-color 0.3s;
        }

        .action-button:hover {
            background-color: #0056b3; /* Darker blue on hover */
        }

        /* Modal Styles */
        .modal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0;
            top: 0;
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgba(0,0,0,0.4);
            padding-top: 60px;
        }

        .modal-content {
            background-color: #fefefe;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 300px; 
            border-radius: 5px;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link active" aria-current="page" href="home1">Home</a>
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

    <!-- Main Content -->
    <div class="dashboard-item">
        <h2>View Products</h2>
    </div>

    <div id="productsList" class="product-container">
        <!-- Dynamic product cards will be added here -->
    </div>


    <!-- JavaScript Section -->
    <script>
        // Fetch products automatically on page load
        window.onload = function() {
            fetchProducts();
        };

        function fetchProducts() {
    console.log('Fetching products...'); // Debugging log
    fetch('http://localhost:8088/api/product/allproducts')
        .then(response => {
            console.log('Response status:', response.status); // Debugging log
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(products => {
            console.log('Fetched products:', products); // Debugging log
            const productsContainer = document.querySelector('#productsList');
            productsContainer.innerHTML = ''; // Clear previous products
            products.forEach(product => {
                const productCard = document.createElement('div');
                productCard.classList.add('product-card');
                productCard.innerHTML = `
                    <img src="\${product.imageurl}" alt="\${product.name}" class="product-image" onerror="this.onerror=null; this.src='path/to/placeholder-image.jpg';">
                    <h3>\${product.name}</h3>
                    <div class="product-price">&#8377;\${product.price.toFixed(2)}</div>
                    <div class="product-description">\${product.description}</div>
                    <div>SKU: \${product.skuCode}</div>
                    <button class="action-button" onclick="addCart(\${product.id}, '\${product.skuCode}', \${product.price})">ADD to Cart</button>
                    <button class="action-button" onclick="deleteProduct(${product.id})">Reviews</button>
                    <button class="action-button" onclick="addFavourite(\${product.id})">Favourite</button>
                `;
                productsContainer.appendChild(productCard);
            });
        })
        .catch(error => console.error('Error fetching products:', error));
}


    function addCart(productId, skuCode, unitPrice) {
    const userId = 'user12'; // Implement this function to get the current user's ID

    const requestBody = {
        productId: productId,
        skuCode: skuCode,
        userId: userId,
        unitPrice: unitPrice
    };

    fetch('http://localhost:8081/api/cart/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Product added to cart:', data); // Debugging log
        // Optionally, update UI or notify user here
    })
    .catch(error => console.error('Error adding product to cart:', error));
}
function addFavourite(productId) {
    const userId = 'user12'; // Implement this function to get the current user's ID

    const requestBody = {
        productId: productId,
        userId: userId
    };

    fetch('http://localhost:8081/api/favourites/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Product added to Favorites:', data); // Debugging log
        // Optionally, update UI or notify user here
    })
    .catch(error => console.error('Error adding product to Favourites:', error));
}

    </script>
</body>
</html>
