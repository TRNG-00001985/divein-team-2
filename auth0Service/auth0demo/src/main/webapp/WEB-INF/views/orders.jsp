<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Orders</title>
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

        .orders-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around; /* Center items */
            margin-top: 20px;
        }

        .order-card {
            background-color: #ffffff; /* White background for cards */
            border: 1px solid #dee2e6; /* Light border color */
            border-radius: 5px; /* Rounded corners for cards */
            margin: 10px;
            padding: 10px;
            text-align: center; /* Center text within cards */
            width: 300px; /* Fixed width for cards */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Subtle shadow */
        }

        .order-price {
            font-size: 18px;
            color: #28a745; /* Green color for total amount */
            margin: 10px 0;
        }

        .order-date {
            font-size: 14px; /* Smaller font for date */
            color: #555; /* Gray color */
            margin: 5px 0;
        }

        .order-address {
            font-size: 14px; /* Smaller font for addresses */
            color: #555; /* Gray color */
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

        /* Modal styles */
        .modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0,0,0); /* Fallback color */
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            padding-top: 60px; /* Location of the box */
        }

        .modal-content {
            background-color: #fefefe;
            margin: 5% auto; /* 15% from the top and centered */
            padding: 20px;
            border: 1px solid #888;
            width: 80%; /* Could be more or less, depending on screen size */
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
    </div>

    <!-- Main Content -->
    <div class="dashboard-item">
        <h2>Your Orders</h2>
    </div>

    <!-- Orders List Container -->
    <div class="orders-container" id="ordersList"></div>

    <!-- Order Details Modal -->
    <div id="orderDetailsModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="document.getElementById('orderDetailsModal').style.display='none'">&times;</span>
            <h2>Order Details</h2>
            <ul id="order-items-list"></ul>
        </div>
    </div>

    <!-- JavaScript Section -->
    <script>
        // Fetch orders automatically on page load
        window.onload = function() {
            fetchOrders();
        };

        function fetchOrders() {
            console.log('Fetching orders...'); // Debugging log

            fetch('http://localhost:8086/api/order/1')
                .then(response => {
                    console.log('Response status:', response.status); // Debugging log
                    if (!response.ok) {
                        throw new Error('Network response was not ok ' + response.statusText);
                    }
                    return response.json();
                })
                .then(orders => {
                    console.log('Fetched orders:', orders); // Debugging log
                    const ordersContainer = document.querySelector('#ordersList');
                    ordersContainer.innerHTML = ''; // Clear previous orders

                    if (orders.length === 0) {
                        ordersContainer.innerHTML = '<p>No orders found</p>';
                        return;
                    }

                    // Loop through each order and display its details
                    orders.forEach(order => {
                        const orderCard = document.createElement('div');
                        orderCard.classList.add('order-card');
                        orderCard.innerHTML = `
                            <h3>Order Number: \${order.orderNumber}</h3>
                            <div>Status: \${order.status}</div>
                            <div class="order-price">Total Amount: &#8377;\${order.totalAmount.toFixed(2)}</div>
                            <div class="order-date">Order Date: \${new Date(order.orderDate).toLocaleString()}</div>
                            <div class="order-address">Shipping Address: \${order.shippingAddress}</div>
                            <div class="order-address">Billing Address: \${order.billingAddress}</div>
                            <button class="action-button" onclick='viewOrderDetails(\${JSON.stringify(order.orderDetailsList)})'>View Details</button>
                        `;
                        ordersContainer.appendChild(orderCard);
                    });
                })
                .catch(error => console.error('Error fetching orders:', error));
        }

        function viewOrderDetails(orderDetailsList) {
            console.log('View details for order:', orderDetailsList); // Log the order details list for debugging
            const orderItemsList = document.querySelector('#order-items-list');
            orderItemsList.innerHTML = ''; // Clear previous items

            // Populate the order items list with details from orderDetailsList
            orderDetailsList.forEach(detail => {
                const listItem = document.createElement('li');
                listItem.innerHTML = `
                    SKU Code: \${detail.skuCode}, 
                    Quantity: \${detail.quantity}, 
                    Price: &#8377;\${detail.price.toFixed(2)}
                `;
                orderItemsList.appendChild(listItem);
            });

            // Display the modal with the populated order details
            document.getElementById('orderDetailsModal').style.display = "block"; // Show modal
        }
    </script>
</body>
</html>
