function login(){
	const username = document.querySelector("#username").value;
	const password = document.querySelector("#password").value;
	
	fetch('/api/auth/login', {
		  method: 'POST',
		  headers: {
		    'Content-Type': 'application/json'
		  },
		  body: JSON.stringify({
		    username: username,
		    password: password
		  })
		})
		.then(response => response.json())
		.then(data => {
			console.log('Success: ',data);
			localStorage.setItem('accessToken', data.token);

			document.getElementById("result").innerText = data.message;		
			
		})
		.catch(error => {
			console.error('로그인 실패:', error);
			document.getElementById("result").innerText = "로그인 요청 중 오류 발생";
		});
	
}
