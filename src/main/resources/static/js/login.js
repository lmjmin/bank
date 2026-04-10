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
		.then(response => 		 {
		    if (!response.ok) {  
		        throw new Error("로그인 실패");
		    }
		    return response.json();
		})
		.then(data => {
			console.log('Success: ',data);
			localStorage.setItem('accessToken', data.token);

			document.getElementById("result").innerText = data.message;	
			window.location.href = "/bank/main";	
			
		})
		.catch(error => {
			console.error('로그인 실패:', error);
			document.getElementById("result").innerText = "아이디나 비밀번호가 틀렸습니다.";
		});
	
}
