
## TnkAdAnalytics

오퍼월 화면에서 메뉴, 배너, 광고아이템 등을 클릭했을 경우 이벤트를 기록하기 위한 기능입니다.

아래와 같이 리스너를 설정 하시면 해당 이벤트 발생시 onEvent메소드가 호출됩니다.

샘플 코드는 로그를 출력하는 예제입니다.

로깅은 원하시는 경우 google analytics 등을 사용하여 필요한 항목을 기록 하도록 구현 하시기 바랍니다.

```kotlin
// 오퍼월 이벤트 리스너 설정
TnkAdAnalytics.tnkAdEVentListener = object : TnkAdAnalytics.TnkAdEVentListener {
    override fun onEvent(event: String, params: HashMap<String, String>) {
        when(event) {
            // 카테고리 선택 - 카테고리 아이디, 카테고리명
            TnkAdAnalytics.Event.SELECT_CATEGORY -> Log.d("카테고리 선택", "onEvent: $event, $params")       
            // 필터 선택 - 필터 아이디, 필터명
            TnkAdAnalytics.Event.SELECT_FILTER -> Log.d("필터 선택", "onEvent: $event, $params")         
            // 기타 상단영역 버튼 클릭 - 메뉴 이름(cps_search, cps_my, offerwall_my)
            TnkAdAnalytics.Event.CLICK_MENU -> Log.d("메뉴 선택", "onEvent: $event, $params")            
            // CPS상품 검색 - "cps_search", 유저가 검색한 키워드
            TnkAdAnalytics.Event.SEARCH_CPS -> Log.d("cps 검색", "onEvent: $event, $params")            
            // 배너 클릭 - app id, app name(광고명)
            TnkAdAnalytics.Event.CLICK_BANNER -> Log.d("배너 클릭", "onEvent: $event, $params")        
            // 광고 아이템 클릭 - app id, app name(광고명)
            TnkAdAnalytics.Event.CLICK_AD -> Log.d("광고 클릭", "onEvent: $event, $params")            
            // 광고 상세화면에서 참여 클릭 - app id, app name(광고명)
            TnkAdAnalytics.Event.JOIN_AD -> Log.d("광고 상세에서 참여 클릭", "onEvent: $event, $params")  
            // 광고 상세화면 닫음 - app id, app name(광고명)
            TnkAdAnalytics.Event.AD_DETAIL_CLOSE -> Log.d("광고 상세화면 닫음", "onEvent: $event, $params")            
            // 오퍼월 액티비티 종료 
            TnkAdAnalytics.Event.ACTIVITY_FINISH -> Log.d("오퍼월 액티비티 종료됨", "onEvent: $event, $params")
            else -> Log.d("tnkAdEVentListener", "onEvent: $event, $params")
        }
    }
}
```

