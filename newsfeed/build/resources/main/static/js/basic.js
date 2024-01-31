let host = 'http://' + window.location.host;

$(document).ready(function () {
    const auth = getToken();
    if(auth === '') {
        $('#login-true').show();
        $('#login-false').hide();
 //      window.location.href = host + "/api/user/login-page";
    } else {
        $('#login-true').show();
        $('#login-false').hide();
    }

    // user-info
    $.ajax({
        type: 'GET',
        url: `/api/user-info`,
        contentType: 'application/json',
        headers: {
            'Authorization': `Bearer ${auth}`   // 토큰을 요청 헤더에 추가
        }
    })
        .done(function (res, status, xhr) {
            const username = res.username;
            const isAdmin = !!res.admin;

            if (!username) {
                window.location.href = '/api/user/login-page';
                return;
            }

            $('#username').text(username);
            if (isAdmin) {
                $('#admin').text(true);
                showProduct(true);
            } else {
                showProduct();
            }
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });
})

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', { path: '/' });
    window.location.href = host + "/api/user/login-page";
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    return auth;
}