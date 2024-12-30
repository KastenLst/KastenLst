if (getCookie("accept") === "false") {
    alert("You need to accept the cookies to use the Settings.");
    let element = document.createElement("a");
    element.setAttribute("href", "/HTML/cookiecoice.html");
    element.click();
}

switch (getCookie("backMus")) {
    case "hard":
        document.getElementById("backgroundmusselector").selectedIndex = 1;
        break;
    case "soft":
        document.getElementById("backgroundmusselector").selectedIndex = 2;
        break;
}

function saveBgMus() {
    switch (document.getElementById("backgroundmusselector").selectedIndex) {
        case 0:
            document.cookie = "backMus=none; " + document.cookie;
            break;
        case 1:
            document.cookie = "backMus=hard; " + document.cookie;
            break;
        case 2:
            document.cookie = "backMus=soft; " + document.cookie;
            break;
    }
    location.reload();
}