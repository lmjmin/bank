window.onload = function () {
    loadAccount();
    loadTransactions();
}

// 내 계좌 조회
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
        document.getElementById("balance").innerText = data.balance;
    })
    .catch(err => {
        console.error(err);
        alert("계좌 정보를 불러오지 못함");
    });
}

// 최근 거래 조회
function loadTransactions() {
    fetch('/api/banking/transactions', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
    })
    .then(res => res.json())
    .then(data => {
        const list = document.getElementById("transactionList");
        list.innerHTML = "";

        data.forEach(tx => {
            const li = document.createElement("li");
			if(tx.type == 'TRANSFER'){
				tx.type = '송금';
			}else{
				tx.type = '입금';
			}
			
			
            li.innerText = 
                "금액: " + tx.amount + 
                " | 타입: " + tx.type + 
                " | 날짜: " + tx.createdAt;

            list.appendChild(li);
        });
    })
    .catch(err => {
        console.error(err);
        alert("거래내역 불러오기 실패");
    });
}