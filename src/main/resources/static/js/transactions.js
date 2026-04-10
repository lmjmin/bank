window.onload = function () {
    loadTransactions();
}

function loadTransactions() {
    fetch('/api/banking/transactions', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
    })
    .then(res => res.json())
    .then(data => {
        const table = document.getElementById("transactionTable");
        table.innerHTML = "";

        data.forEach(tx => {
            const sender = tx.senderAccount ? tx.senderAccount.accountNumber : "-";
            const receiver = tx.receiverAccount ? tx.receiverAccount.accountNumber : "-";
			if(tx.type == 'TRANSFER'){
							tx.type = '송금';
						}else{
							tx.type = '입금';
						}
            const row = `
                <tr>
                    <td>${sender}</td>
                    <td>${receiver}</td>
                    <td>${tx.amount}</td>
                    <td>${tx.type}</td>
                    <td>${tx.createdAt}</td>
                </tr>
            `;
            table.innerHTML += row;
        });
    })
    .catch(err => {
        console.error(err);
    });
}