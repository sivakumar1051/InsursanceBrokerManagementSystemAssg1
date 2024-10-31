// Function to get query parameters
function getQueryParams() {
    const params = {};
    const queryString = window.location.search.slice(1);
    const pairs = queryString.split('&');

    for (let pair of pairs) {
        const [key, value] = pair.split('=');
        params[decodeURIComponent(key)] = decodeURIComponent(value || '');
    }
    return params;
}

// Display error message if present in the URL
const params = getQueryParams();
if (params.error) {
    const errorMessageDiv = document.getElementById('error-message');
    errorMessageDiv.textContent = params.error;
    errorMessageDiv.style.display = 'block'; // Show the error message
}