// main.js
document.addEventListener("DOMContentLoaded", () => {
    const loginModal = document.getElementById("loginModal");
    const openLoginBtn = document.getElementById("openLoginBtn");
    const loginResultDiv = document.getElementById("loginResult");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    // 모달 열기 버튼 클릭 이벤트
    openLoginBtn.addEventListener("click", () => {
        loginModal.style.display = "flex"; // 모달을 'flex'로 설정하여 표시하고 중앙 정렬
        // 모달을 열 때 이전 로그인 결과 메시지 및 입력 필드 초기화
        loginResultDiv.innerText = "";
        loginResultDiv.className = "";
        usernameInput.value = "";
        passwordInput.value = "";
    });

    // 모달 바깥 클릭 시 닫기
    loginModal.addEventListener("click", (e) => {
        if (e.target === loginModal) {
            loginModal.style.display = "none"; // 모달 닫기
        }
    });

    // 로그인 처리 함수 (submitLogin이라는 이름으로 전역에 노출)
    window.submitLogin = function() {
        fetch("/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: usernameInput.value,
                password: passwordInput.value
            })
        })
        .then(res => res.json().then(data => ({ status: res.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                loginResultDiv.innerText = "✅ 로그인 성공!";
                loginResultDiv.className = "success";

                setTimeout(() => {
                    loginModal.style.display = "none";
                    openLoginBtn.style.display = "none"; // 로그인 버튼 숨기기
                    // 로그인 성공 시 관리자 페이지로 리다이렉트 (필요하다면 주석 해제)
                    // window.location.href = "/admin/dashboard";
                }, 1000);
            } else {
                loginResultDiv.innerText = `❌ 로그인 실패: ${body.message || '아이디 또는 비밀번호가 올바르지 않습니다.'}`;
                loginResultDiv.className = "error";
            }
        })
        .catch(error => {
            console.error("로그인 요청 실패:", error);
            loginResultDiv.innerText = "🚨 오류 발생! 다시 시도해주세요.";
            loginResultDiv.className = "error";
        });
    };
});