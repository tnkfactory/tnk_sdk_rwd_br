# TNK Ad SDK for Web

TNK WEB SDK는 **TNK PUB** 플랫폼(TNK SSP를 통해 물량 제공)에서 웹 환경에서의 광고 수익화를 위한 환경을 제공하고 있습니다.

## 시작하기

### 0. 지원버전
- Chrome, Edge, Firefox, Safari 등 모던 브라우저
- IE 미지원

### 1. TNK Web SDK 라이브러리 로드
- tnkads를 호출하는 스크립트 위에 아래와 같이 `<script>` 태그를 추가하여 SDK를 로드합니다.
- SDK **페이지 로드시 최초 1회만** 로드 되어야 합니다.
```html
<script async src="https://cdn4.tnkfactory.com/adx/websdk/opv2/tnk_ad_sdk.min.js"></script>
```

### 2. TNK Web SDK 명령어 대기열 생성하기
- tnkads를 초기화 하고 명령어 대기열을 생성합니다.
```html
 <script async src="https://cdn4.tnkfactory.com/adx/websdk/opv2/tnk_ad_sdk.min.js"></script>
 <script type="text/javascript">

    window.tnkads = window.tnkads || function() {
        (window.tnkads.cmd = window.tnkads.cmd || []).push(arguments);
    };

</script>
``` 

> [!WARNING]
> WeB SDK가 로딩되기 이전에 window.tnkads의 메소드는 존재하지 않기 때문에 window.tnkads의 모든 메소드는 tnkads.cmd 명령어 대기열을 통해 실행되어야 합니다.

### 3. 광고 슬롯 설정
- setAdSlotConfig 함수로 공통 설정값을 셋팅합니다.

| Feature | Required | Detail |
| --- | :---: | -- |
| inven_id | O | TNK PUB 인벤토리 ID |
| adid | X | ADID/IDFA |
| request_timeout | X | 요청 중단 시간(밀리세컨드, 기본값 6,000 ms) |
| env | X | 광고 테스트 환경 설정(기본값 OP) |


```html
<script async src="https://cdn4.tnkfactory.com/adx/websdk/opv2/tnk_ad_sdk.min.js"></script>
<script type="text/javascript">

    window.tnkads = window.tnkads || function() {
        (window.tnkads.cmd = window.tnkads.cmd || []).push(arguments);
    };

    // 큐에 직접 작업(setAdSlotConfig)을 푸시
    window.tnkads.cmd = window.tnkads.cmd || [];
    window.tnkads.cmd.push(function() {
        var config = {
            inven_id: '[인벤토리ID]',
            adid: '[ADID/IDFA]',
            request_timeout: 6000,
            env: '[DEV/OP]'
        };

        window.tnkads.setAdSlotConfig(config);
    });
</script>

```

### 4. 광고 노출 영역 지정 설정
- 광고가 노출되는 영역에 `ins` 태그를 추가합니다. 
```html
<ins class="tnkads"
     data-ad-form="[배너광고(banner)/전면광고(interstitial)]"
     data-ad-plcmt-nm="[플레이스먼트 이름]"
     data-ad-onfail="[광고 노출 실패 시 콜백 함수 이름]"  
     data-ad-template="[템플릿 이름]">
</ins>
```
- data-ad-form : 광고 타입으로 배너(banner)와 전면 광고(interstitial)를 설정할 수 있습니다.
> [!INFORMATION] 광고 영역의 너비는 부모 태그의 너비로 잡기 때문에 전체 너비를 사용하는 태그 아래에 넣는 것을 권장합니다. 특히, 전면 광고는 `<body>` 태그 아래에 넣는 것을 권장합니다.   
- data-ad-plcmt-nm : 광고 슬롯에서 설정한 인벤토리의 플레이스먼트명을 입력합니다. 해당 플레이스먼트의 설정 값이 적용됩니다.  
- data-ad-onfail : 광고 요청이 실패했을 경우 호출하는 함수이름을 입력합니다.
- data-ad-template : 광고 템플릿 이름을 입력합니다. 미입력 시 디폴드 값이 설정됩니다.  

### 5. 광고 요청 실패 콜백함수 설정
- 광고 요청이 실패 했을 경우 `data-ad-onfail` 에 설정된 함수명을 호출합니다.

```html
    // 광고 ins 태그의  data-ad-onfail에 지정된 함수명과 동일해야 함. 
    function callBackBannerFunc(retCode, adElement) {
      console.log(`광고 실패 콜백 함수. 실패 코드: ${retCode}`);
      
      // ERROR CODE 
      // E0 : 서버로부터 응답을 받지 못한 경우(네트워크 오류, 서버 장애 등)
      // E1 : 요청한 인벤토리(config: inven_id, ins: plcmt-nm)가 존재하지 않습니다.
      // E2 : 전달받은 광고가 없다.(No AD)
      // E3 : 요청한 지면타입(ins: ad-form)과 서버에 저장된 지면 타입이 다르다.
      // E4 : 매체 상태가 테스트인데 테스트 기기로 등록되어 있지 않다.(매체 상태가 테스트이면 테스트 기기로 등록한 adid를 config에 설정해 주셔야 합니다.)
      // E11 : 인증코드 불일치
      // E13 : 광고 이력이 존재하지 않는다.
      // E16 : 필수 매개변수 값이 없거나 유효한 매개변수 값이 아니다.
      // E21 : 인증이 거부되었거나 인증 기간이 만료되었다(TNK 담당자에게 확인 요청 필요).
      // E22 : config: request_timeout 초과됨

      console.log("대상 Ad Element:", adElement);

      // <ins> 태그 HTML 코드 삽입 예시
      if (adElement) {
        // 1. 기존의 내용을 모두 삭제
        adElement.innerHTML = '';

        // 2. HTML 코드를 삽입합니다.
        // 예시: <span>No AD</span>
        const noAdHtml = `
            <span style="display: block; text-align: center; padding: 10px; color: #555; border: 1px dashed #ccc;">
            No AD Available (Code: ${retCode})
            </span>
            `;

          adElement.innerHTML = noAdHtml; // **innerHTML을 사용하여 HTML 코드를 삽입**
          
        // *참고: 텍스트만 삽입하려면*
        // adElement.textContent = `광고 없음 (${retCode})`;
      }
    }
```

## 예제 샘플
```html
<!DOCTYPE html>
<html>
	<head>
	  <meta charset="utf-8" />
	  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
	  <title>TNK Web SDK</title>
	</head>
	<body>
		<header>
			<!-- Your header section -->
		</header>
		
		<main>
			<div class="flex flex-col lg:flex-row">
				<section class="w-full lg:w-3/4 lg:pr-4">
				
				<!-- Your Content Section -->

				<!-- TNK Ad Banner SAMPLE -->
				<div id="tnk_banner_sample_slot">
					<span>TNK Advertisement Banner</span>
					<ins class="tnkads"
					  data-ad-form="banner"
					  data-ad-plcmt-nm="TEST_BANNER"
					  data-ad-onfail="callBackBannerFunc"  
					  data-ad-template="default">
					</ins>
				</div>
				  
				</section>
			</div>
		</main>

		<script async src="https://cdn4.tnkfactory.com/adx/websdk/opv2/tnk_ad_sdk.min.js"></script>
		<script type="text/javascript">

			window.tnkads = window.tnkads || function() {
				(window.tnkads.cmd = window.tnkads.cmd || []).push(arguments);
			};

			window.tnkads.cmd = window.tnkads.cmd || [];
			window.tnkads.cmd.push(function() {
				var config = {
					inven_id: '00e0103070113800008311050a0b030a',  // 필수
					adid: '00000000000000000000000000000000',      // 선택: 값을 셋팅하지 않을 시 라인을 입력하지 않아도 됨
					request_timeout: 9000,                         // 선택: 값 입력하지 않아도 됨, 기본 6000 ms
					env: 'DEV'                                     // 선택: 값 입력하지 않아도 됨, 기본 라이브 환경
				};

				window.tnkads.setAdSlotConfig(config);
			});

		// 광고 실패 콜백 함수(ins 태그의 data-ad-onfail 설정값과 동일해야 함)
		function callBackBannerFunc(retCode, adElement) {
		  console.log(`광고 실패 콜백 함수. 실패 코드: ${retCode}`);	  
		}

	  </script>
	</body>
</html>

```