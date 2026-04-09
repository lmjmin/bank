function signup(){
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#password").value;
    const name = document.querySelector("#name").value;

    fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password, name })
    })
    .then(response => {
        if(!response.ok){
            return response.text().then(err => { throw err });
        }
        return response.text();
    })
    .then(data => {
        document.getElementById("result").innerText = data;
		window.location.href = "/loginForm";
    })
    .catch(error => {
        document.getElementById("result").innerText = error;
    });
}

function checkUsername() {
    const username = document.querySelector("#username").value;

    fetch(`/api/auth/check?username=${username}`)
    .then(res => res.text())
    .then(data => {
        document.getElementById("result").innerText = data;
		
    })
}