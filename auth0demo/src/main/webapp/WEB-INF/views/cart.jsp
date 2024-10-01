<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cart</title>
    <script>
        window.onload = function() {
            fetchCartItems();
        };

        function fetchCartItems() {
            console.log('Fetching cart items...'); // Debugging log
            fetch('http://localhost:8081/api/cart/user?userId=user123') // Use the correct endpoint
                .then(response => {
                    console.log('Response status:', response.status); // Debugging log
                    if (!response.ok) {
                        throw new Error('Network response was not ok ' + response.statusText);
                    }
                    return response.json();
                })
                .then(cartItems => {
                    console.log('Fetched cart items:', cartItems); // Debugging log
                    const cartItemsContainer = document.querySelector('#cartItemsList');
                    cartItemsContainer.innerHTML = ''; // Clear previous cart items

                    let grandTotal = 0; // Initialize grand total

                    cartItems.forEach(item => {
                        const cartItemRow = document.createElement('div');
                        cartItemRow.classList.add('cart-item-row');
                        cartItemRow.innerHTML = `
                            <div class="cart-item">
                                <h3>Product ID: \${item.productId}</h3>
                                <div class="cart-item-price">&#8377;\${item.unitPrice.toFixed(2)}</div>
                                <div class="cart-item-quantity">Quantity: <span>\${item.quantity}</span></div>
                                <div class="cart-item-subtotal">Subtotal: &#8377;<span>\${item.totalPrice.toFixed(2)}</span></div>
                                <button class="remove-btn" onclick="deleteCartItem(\${item.cartId})">Remove</button>
                                <button class="move-btn" onclick="moveToFavorites(\${item.cartId})">Move to Favorites</button>
                            </div>
                        `;
                        cartItemsContainer.appendChild(cartItemRow);

                        // Add subtotal to grand total
                        grandTotal += item.totalPrice; 
                    });

                    // Display grand total
                    const grandTotalContainer = document.querySelector('#grandTotal');
                    grandTotalContainer.innerHTML = Grand Total: &#8377;<span>\${grandTotal.toFixed(2)}</span>;
                })
                .catch(error => console.error('Error fetching cart items:', error));
        }

		function deleteCartItem(cartId) {
		    console.log('Deleting cart item with ID:', cartId);
		    fetch(http://localhost:8081/api/cart/${cartId}, {
		        method: 'DELETE'
		    })
		    .then(response => {
		        if (!response.ok) {
		            throw new Error('Failed to delete item from cart');
		        }
		        console.log('Item deleted successfully');
		        fetchCartItems(); // Refresh the cart items
		    })
		    .catch(error => console.error('Error deleting cart item:', error));
		}

        function moveToFavorites(cartId) {
            fetch(http://localhost:8081/api/cart/favourite/${cartId}, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    fetchCartItems(); // Refresh cart items after moving
                } else {
                    alert('Error moving item to favorites. Please try again.');
                }
            });
        }
    </script>
</head>
<body>
    <h1>Your Cart</h1>
    <div id="cartItemsList"></div> <!-- Container for cart items -->
    <div id="grandTotal"></div> <!-- Container for grand total -->
</body>
</html>