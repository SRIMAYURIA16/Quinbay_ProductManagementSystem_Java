let products = JSON.parse(localStorage.getItem('products')) || [];
let cartProducts = JSON.parse(localStorage.getItem('cartProducts')) || [];

document.addEventListener('DOMContentLoaded', () => {
    updateProductList();
    updateCartList();
    populateBrandDropdown();
});
function populateBrandDropdown() {
    const brandSelect = document.getElementById('brand-select');
    let uniqueBrands = [...new Set(products.map(product => product.brand))];
    uniqueBrands.sort();
    uniqueBrands.forEach(brand => {
        const option = document.createElement('option');
        option.value = brand;
        option.textContent = brand;
        brandSelect.appendChild(option);
    });
}
function checkFields(productName,productBrand,productPrice,productRAM,productROM,productQuantity,productImageURL){
    return productName && productBrand && productPrice && productRAM && productROM && productQuantity && productImageURL;
}
function addProduct(event) {
    event.preventDefault();
    const productName = document.getElementById('pname').value.trim();
    const productBrand = document.getElementById('pbrand').value.trim();
    const productPrice = parseFloat(document.getElementById('pprice').value.trim());
    const productRAM = document.getElementById('ram').value.trim();
    const productROM = document.getElementById('rom').value.trim();
    const productQuantity = parseInt(document.getElementById('quantity').value.trim());
    const productImageURL = document.getElementById('url').value.trim();
    if(!checkFields(productName,productBrand,productPrice,productRAM,productROM,productQuantity,productImageURL)){
        alert('Please fill in all fields.');
        return;
    }
    if (isNaN(productPrice) || isNaN(productQuantity)) {
        alert('Price and quantity must be valid numbers.');
        return;
    }
    const product = {
        name: productName,
        brand: productBrand,
        price: productPrice,
        ram: productRAM,
        rom: productROM,
        quantity: productQuantity,
        imageURL: productImageURL
    };
    products.push(product);
    localStorage.setItem('products', JSON.stringify(products));
    document.getElementById('productForm').reset();
    updateProductList();
}

function handleSearch(event) {
    const searchQuery = event.target.value.toLowerCase().trim(); 
    const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(searchQuery) ||
        product.brand.toLowerCase().includes(searchQuery)
    );
    updateProductList(searchQuery, filteredProducts);
}

function updateProductList(searchQuery = '', filteredProducts = []) {
    const productList = document.getElementById('productList');
    productList.innerHTML = '';
    const productsToDisplay = filteredProducts.length ? filteredProducts : products;
    productsToDisplay.forEach((product) => {
        const productBox = document.createElement('div');
        productBox.classList.add('product-box');
        if (product.imageURL) {
            const productImage = document.createElement('img');
            productImage.src = product.imageURL;
            productImage.alt = product.name;
            productImage.classList.add('product-image');
            productBox.appendChild(productImage);
        }
        const cartProduct = cartProducts.find(cartProduct => cartProduct.name === product.name && cartProduct.brand === product.brand);
        const availableQuantity = cartProduct ? product.quantity - cartProduct.cartQuantity : product.quantity;
        productBox.innerHTML += `
            <p><b>${product.name}</b></p>
            <p>${product.brand}</p>
            <h4>₹${product.price}</h4>
            <p>RAM: ${product.ram}</p>
            <p>ROM: ${product.rom}</p>
            <p>Available Quantity: ${availableQuantity}</p>
            <input type="number" min="1" value="1" class="quantity-input" id="quantity-${product.name}">
            <button class="add-to-cart-button" data-name="${product.name}" data-brand="${product.brand}">Add to Cart</button>`;
        productList.appendChild(productBox);
    });
    document.querySelectorAll('.add-to-cart-button').forEach(button => {
        button.addEventListener('click', addToCart);
    });
}
function addToCart(event) {
    const productName = event.target.dataset.name;
    const productBrand = event.target.dataset.brand;
    const quantityInput = document.getElementById(`quantity-${productName}`);
    const quantity = parseInt(quantityInput.value);
    if (quantity && quantity > 0) {
        const product = products.find(product => product.name === productName && product.brand === productBrand);
        const cartProduct = cartProducts.find(cartProduct => cartProduct.name === productName && cartProduct.brand === productBrand);
        const availableQuantity = cartProduct ? product.quantity - cartProduct.cartQuantity : product.quantity;

        if (quantity > availableQuantity) {
            alert(`Only ${availableQuantity} ${productName} available.`);
            return;
        }
        const cartProductIndex = cartProducts.findIndex(cartProduct => cartProduct.name === productName && cartProduct.brand === productBrand);
        if (cartProductIndex !== -1) {
            cartProducts[cartProductIndex].cartQuantity += quantity;
        } 
        else {
            const cartProduct = {
                ...product,
                cartQuantity: quantity
            };
            cartProducts.push(cartProduct);
        }
        localStorage.setItem('cartProducts', JSON.stringify(cartProducts));
        updateCartList();
    } else {
        alert("Please enter a valid quantity.");
    }
}

function updateCartList() {
    const cartList = document.getElementById('cartList');
    if (!cartList) return; 
    cartList.innerHTML = '';
    cartProducts.forEach((product) => {
        const cartItem = document.createElement('div');
        cartItem.classList.add('cart-item');
        if (product.imageURL) {
            const productImage = document.createElement('img');
            productImage.src = product.imageURL;
            productImage.alt = product.name;
            productImage.classList.add('product-image');
            cartItem.appendChild(productImage);
        }
        cartItem.innerHTML += `
            <p><b>${product.name}</b></p>
            <p>${product.brand}</p>
            <h4>₹${product.price}</h4>
            <p>RAM: ${product.ram}</p>
            <p>ROM: ${product.rom}</p>
            <p>Quantity in Cart: ${product.cartQuantity}</p>
        `;
        const removeButton = document.createElement('button');
        removeButton.classList.add('remove-from-cart-button');
        removeButton.textContent = 'Remove from Cart';
        removeButton.dataset.name = product.name;
        removeButton.dataset.brand = product.brand;
        cartItem.appendChild(removeButton);
        cartList.appendChild(cartItem);
    });

    const checkoutButton = document.getElementById('checkoutBtn');
    checkoutButton.addEventListener('click', handleCheckout);
    document.querySelectorAll('.remove-from-cart-button').forEach(button => {
        button.addEventListener('click', removeFromCart);
    });
}

function handleCheckout() {
    cartProducts.forEach(cartProduct => {
        const product = products.find(product => product.name === cartProduct.name && product.brand === cartProduct.brand);
        if (product) {
            product.quantity -= cartProduct.cartQuantity;
        }
    });
    cartProducts = [];
    localStorage.setItem('cartProducts', JSON.stringify(cartProducts));
    updateProductList();
    updateCartList();
}

function applyFilters() {
    const inStockOnly = document.getElementById('in-stock-checkbox').checked;
    const minPrice = parseFloat(document.getElementById('min-price').value);
    const maxPrice = parseFloat(document.getElementById('max-price').value);
    const selectedBrand = document.getElementById('brand-select').value;
    let filteredProducts = products.filter(product => {
        if (inStockOnly && product.quantity <= 0) {
            return false;
        }
        if ((minPrice && product.price < minPrice) || (maxPrice && product.price > maxPrice)) {
            return false;
        }
        if (selectedBrand && selectedBrand !== "" && product.brand !== selectedBrand) {
            return false;
        }
        return true;
    });
    updateProductList('', filteredProducts);
}
function removeFromCart(event) {
    const productName = event.target.dataset.name;
    const productBrand = event.target.dataset.brand;
    const cartProductIndex = cartProducts.findIndex(cartProduct =>
        cartProduct.name === productName && cartProduct.brand === productBrand
    );
    if (cartProductIndex !== -1) {
        cartProducts.splice(cartProductIndex, 1); 
        localStorage.setItem('cartProducts', JSON.stringify(cartProducts)); 
        updateCartList(); 
        updateProductList(); 
    }
}
document.getElementById('productForm').addEventListener('submit', addProduct);
document.getElementById('applyFilters').addEventListener('click', applyFilters);
document.querySelector('.search-bar input').addEventListener('input', handleSearch);