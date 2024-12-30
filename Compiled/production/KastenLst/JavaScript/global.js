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

if (getCookie("accept") == null) {
    let element = document.createElement("a");
    element.setAttribute("href", "/HTML/cookiecoice.html");
    element.click();
}

let vid = document.getElementById('bvid');
vid.loop = true;
onclick = function() {
    onclick = null;
    vid.play();
    if (bgaudio != null) {
        bgaudio.play();
    }
};