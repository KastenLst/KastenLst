function getCookie(name) {
    const cookies = document.cookie.split('; ');
    for (let cookie of cookies) {
        const [key, value] = cookie.split('=');
        if (key === name) {
            return value;
        }
    }
    return null;
}

if (getCookie("accept") != null) {
    let element = document.createElement("a");
    element.setAttribute("href", "/HTML/index.html");
    element.click();
}

function acceptCookies() {
    document.cookie = "accept=true; max-age=9999999999; path=/";
    let element = document.createElement("a");
    element.setAttribute("href", "/HTML/index.html");
    element.click();
}

function declineCookies() {
    document.cookie = "accept=false; max-age=9999999999; path=/";
    let element = document.createElement("a");
    element.setAttribute("href", "/HTML/index.html");
    element.click();
}