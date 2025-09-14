// JWT 토큰 테스트 함수
function testUserAPI() {
    fetch('/api/user/test', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.text())
    .then(data => {
        console.log('User API Response:', data);
        alert('User API 테스트 성공!\n' + data);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('User API 테스트 실패: ' + error.message);
    });
}

function testAdminAPI() {
    fetch('/api/admin/stats', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log('Admin API Response:', data);
        alert('Admin API 테스트 성공!\n' + JSON.stringify(data, null, 2));
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Admin API 테스트 실패: ' + error.message);
    });
}

// 폼 유효성 검사
function validateSignupForm() {
    const password = document.getElementById('password').value;
    const phoneNumber = document.getElementById('phoneNumber').value;

    if (password.length < 6) {
        alert('비밀번호는 최소 6자 이상이어야 합니다.');
        return false;
    }

    const phoneRegex = /^010-\d{4}-\d{4}$/;
    if (!phoneRegex.test(phoneNumber)) {
        alert('전화번호 형식이 올바르지 않습니다. (010-1234-5678)');
        return false;
    }

    return true;
}

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    // 회원가입 폼 유효성 검사
    const signupForm = document.querySelector('form[action*="signup"]');
    if (signupForm) {
        signupForm.addEventListener('submit', function(e) {
            if (!validateSignupForm()) {
                e.preventDefault();
            }
        });
    }

    // 전화번호 자동 포맷팅
    const phoneInput = document.getElementById('phoneNumber');
    if (phoneInput) {
        phoneInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 3 && value.length <= 7) {
                value = value.replace(/(\d{3})(\d{0,4})/, '$1-$2');
            } else if (value.length > 7) {
                value = value.replace(/(\d{3})(\d{4})(\d{0,4})/, '$1-$2-$3');
            }
            e.target.value = value;
        });
    }

    // 알림 메시지 자동 사라지기
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert && alert.classList.contains('show')) {
                alert.classList.remove('show');
            }
        }, 5000);
    });
});