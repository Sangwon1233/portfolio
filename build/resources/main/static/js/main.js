document.addEventListener("DOMContentLoaded", () => {
    const loginModal = document.getElementById("loginModal");
    const openLoginBtn = document.getElementById("openLoginBtn");
    const loginResultDiv = document.getElementById("loginResult");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    //모달창 닫기 추가
    const closeLoginModalBtnText = document.getElementById("closeLoginModalBtnText");
    if (closeLoginModalBtnText) {
        closeLoginModalBtnText.addEventListener("click", () => {
            loginModal.style.display = "none";
        });
    }

    // ▶ 모달 열기 버튼 클릭 이벤트
    if (openLoginBtn) {
        openLoginBtn.addEventListener("click", () => {
            loginModal.style.display = "flex";
            loginResultDiv.innerText = "";
            loginResultDiv.className = "";
            usernameInput.value = "";
            passwordInput.value = "";
        });
    }

    // ▶ 모달 바깥 클릭 시 닫기
    if (loginModal) {
        loginModal.addEventListener("click", (e) => {
            if (e.target === loginModal) {
                loginModal.style.display = "none";
            }
        });
    }

    // ▶ 로그인 버튼 클릭 시 API 호출 (전역 함수 등록)
    window.submitLogin = function () {
        fetch("/portfolio/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: usernameInput.value,
                password: passwordInput.value
            }),
            credentials: "include"
        })
        .then(res => res.json().then(data => ({ status: res.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                loginResultDiv.innerText = "로그인 성공!";
                loginResultDiv.className = "success";

                setTimeout(() => {
                    loginModal.style.display = "none";
                    if (openLoginBtn) openLoginBtn.style.display = "none";
                    location.reload(); // 페이지 새로고침으로 로그인 반영
                }, 1000);
            } else {
                loginResultDiv.innerText = `로그인 실패: ${body.message || '아이디 또는 비밀번호가 올바르지 않습니다.'}`;
                loginResultDiv.className = "error";
            }
        })
        .catch(error => {
            console.error("로그인 요청 실패:", error);
            loginResultDiv.innerText = "오류 발생! 다시 시도해주세요.";
            loginResultDiv.className = "error";
        });
    };

    // 글쓰기 버튼 처리
    document.querySelectorAll(".write-button").forEach(button => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            const url = button.getAttribute("href") || button.dataset.href;
            fetch(url, {
                method: "GET",
                credentials: "include"
            })
            .then(res => {
                if (res.status === 401) {
                    alert("글쓰기 권한이 없습니다. 로그인 후 이용해주세요.");
                    // if (loginModal) loginModal.style.display = "flex";//권한 없을시 로그인 모달 창 오픈
                } else if (res.redirected) {
                    // 권한이 있지만 리다이렉트 응답인 경우
                    window.location.href = res.url;
                } else {
                    // 권한이 있을 경우 이동
                    window.location.href = url;
                }
            })
            .catch(err => {
                console.error("글쓰기 접근 중 오류:", err);
                alert("알 수 없는 오류가 발생했습니다.");
            });
        });
    });
});
