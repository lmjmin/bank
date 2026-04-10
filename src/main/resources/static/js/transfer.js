window.onload = function () {
    loadAccount();
}

// 내 계좌 불러오기
function loadAccount() {
    fetch('/api/banking/my-account', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("senderAccount").innerText = data.accountNumber;
    });
}

// 송금
function transfer() {
    const sender = document.getElementById("senderAccount").innerText;
    const receiver = document.getElementById("receiverAccountNumber").value;
    const amount = document.getElementById("amount").value;

	fetch('/api/banking/transfer', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json',
	        'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
	    },
	    body: JSON.stringify({
	        senderAccountNumber: sender,
	        receiverAccountNumber: receiver,
	        amount: amount
	    })
	})
	.then(res => {
	    if (!res.ok) {
	        return res.text().then(err => { throw new Error(err); });
	    }
	    return res.text();
	})
	.then(data => {
		alert("송금 완료 되었습니다.");
	    document.getElementById("result").innerText = data;
	    window.location.href = "/bank/main";
	})
	.catch(err => {
	    console.error(err);
	    document.getElementById("result").innerText = err.message;
	});
}