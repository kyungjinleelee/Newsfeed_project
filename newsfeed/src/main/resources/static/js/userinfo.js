const host = 'http://' + window.location.host;
let targetId;
$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/api/user/login-page';
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/api/user-info`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const username = res.username;
            const isAdmin = !!res.admin;

            if (!username) {    // 사용자 명이 없으면 로그인 페이지로 리디렉션
                window.location.href = host + '/api/user/login-page';
                return;
            }

            $('#name').text(name);  // html에 사용자 정보 표시
            if (isAdmin) {
                //    $('#admin').text(true);
                //    showUserinfo(true);
            } else {
                showUserinfo();     // 관리자 아닌 경우 기본 사용자 정보만 표시
            }
        })
        .fail(function (jqXHR, textStatus) {
            logout();       // AJAX 요청 실패 시 로그아웃
        });

    // id 가 query 인 녀석 위에서 엔터를 누르면 execSearch() 함수를 실행하라는 뜻입니다.
    $('#query').on('keypress', function (e) {
        if (e.key == 'Enter') {
            execSearch();
        }
    });
    $('#close').on('click', function () {
        $('#container').removeClass('active');
    })
    $('#close2').on('click', function () {
        $('#container2').removeClass('active');
    })
    $('.nav div.nav-see').on('click', function () {
        $('div.nav-see').addClass('active');
        $('div.nav-search').removeClass('active');

        $('#see-area').show();
        $('#search-area').hide();
    })
    $('.nav div.nav-search').on('click', function () {
        $('div.nav-see').removeClass('active');
        $('div.nav-search').addClass('active');

        $('#see-area').hide();
        $('#search-area').show();
    })

    $('#see-area').show();
    $('#search-area').hide();
})
function addHTML(username, name, email, isAdmin) {
    $('#username').text('Username: ' + username);
    $('#name').text('Name: ' + name);
    $('#email').text('Email: ' + email);
    $('#isAdmin').text('Is Admin: ' + isAdmin);
}
function showUserinfo(isAdmin = false) {
    /**
     * 관심상품 목록: #product-container
     * 검색결과 목록: #search-result-box
     * 관심상품 HTML 만드는 함수: addProductItem
     */
        // ADMIN 계정
    let dataSource = isAdmin ? `/api/admin/user-info` : `/api/user-info`;


    $.ajax({
        type: 'GET',
        url: dataSource,
        contentType: 'application/json',
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let userInfo = response[i];
                let username = userInfo['username'];
            //    let name = userInfo['name'];
            //    let email = userInfo['email']
                let isAdmin = userInfo['isAdmin'];

            //    addHTML(username, name, email, isAdmin);
                addHTML(username, isAdmin);
            }
        },
        error(error, status, request) {
            if (error.status === 403) {
                $('html').html(error.responseText);
                return;
            }
            logout();
        }
    });
}