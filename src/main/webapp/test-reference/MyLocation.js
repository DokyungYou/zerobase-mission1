navigator.geolocation.getCurrentPosition(showYourLocation, showErrorMsg); 

function showYourLocation(position) {  // 성공했을때 실행
    var userLat = position.coords.latitude;
    var userLng = position.coords.longitude;
}

function showErrorMsg(error) { // 실패했을때 실행
    switch(error.code) {
        case error.PERMISSION_DENIED:
        loc.innerHTML = "이 문장은 사용자가 Geolocation API의 사용 요청을 거부했을 때 나타납니다!"
        break;

        case error.POSITION_UNAVAILABLE:
        loc.innerHTML = "이 문장은 가져온 위치 정보를 사용할 수 없을 때 나타납니다!"
        break;

        case error.TIMEOUT:
        loc.innerHTML = "이 문장은 위치 정보를 가져오기 위한 요청이 허용 시간을 초과했을 때 나타납니다!"
        break;

        case error.UNKNOWN_ERROR:
        loc.innerHTML = "이 문장은 알 수 없는 오류가 발생했을 때 나타납니다!"
        break;
    }
}