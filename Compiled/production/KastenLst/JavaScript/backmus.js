if (getCookie("backMus") != null) {
    var bgaudio = document.createElement("audio");
    bgaudio.loop = true;
    bgaudio.volume = 0.06;
    if (getCookie("backMus") === "soft") {
        bgaudio.innerHTML = '<source src=\"/media/Backmus-soft.mp3\" type=\"audio/mp3\">';
    } else if (getCookie("backMus") === "hard") {
        bgaudio.innerHTML = '<source src=\"/media/Backmus-hard.mp3\" type=\"audio/mp3\">';
    }
}