// Function to get query parameters from the URL
function getQueryParameter(name) {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(name);
}

// Function to display error message if present
window.onload = function() {
	const errorMessage = getQueryParameter('error');
	if (errorMessage) {
		const errorElement = document.getElementById('error-message');
		errorElement.textContent = errorMessage;
		errorElement.style.color = 'red';
	}
};