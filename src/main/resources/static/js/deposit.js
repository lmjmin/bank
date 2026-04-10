window.onload = function () {
    loadAccount();
}

// 계좌 불러오기
function loadAccount() {
    fetch('/api/banking/my-account', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("accountNumber").innerText = data.accountNumber;
    });
}

// 입금
function deposit() {
    const accountNumber = document.getElementById("accountNumber").innerText;
    const amount = document.getElementById("amount").value;

    fetch('/api/banking/deposit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
            accountNumber: accountNumber,
            amount: amount
        })
    })
    .then(res => res.text())
    .then(data => {
       alert("입금 완료 되었습니다.");
		window.location.href = "/bank/main";
    })
    .catch(err => {
        console.error(err);
        document.getElementById("result").innerText = "입금 실패";
    });
}