# Unity Plugin Guide — 이전되었습니다

Unity 플러그인 가이드는 아래 저장소로 옮겨졌습니다.

### 👉 **[tnkfactory/tnk_rwd_unity](https://github.com/tnkfactory/tnk_rwd_unity)**

플러그인 다운로드(`tnk_rwd_package.unitypackage`), 설정 가이드, 샘플 프로젝트가
모두 그곳에 있습니다.

---

## 이 문서에 있던 내용을 찾고 계신가요

기존 문서는 **2023년 버전 플러그인** 기준이었고 지금 배포본과 맞지 않습니다.
아래 항목은 특히 달라졌으니 새 가이드를 확인해 주세요.

| 예전 문서 | 지금 |
|-----------|------|
| `TnkAd.Plugin.Instance.*` | **`TnkAd.RwdPlugin2.Instance.*`** 로 바뀌었습니다 |
| Android 전용 | **iOS 도 지원**합니다 |
| maven 저장소를 `baseProjectTemplate.gradle` 에 추가 | **`settingsTemplate.gradle`** 에 추가해야 합니다. Unity 2022.3 이상(**Unity 6 포함**)에서는 예전 위치에 넣으면 빌드가 실패합니다 |
| `com.unity3d.player.UnityPlayerNativeActivity` 를 직접 선언 | Unity 5 에서 제거된 클래스입니다. Unity 가 생성한 매니페스트를 그대로 쓰세요 |
| `showAdListTab()`, `showAdList(title, AdListType, TemplateStyle)`, `prepareInterstitialAdForPPI()`, `showInterstitialAdForPPI()` | **배포된 플러그인에 구현되어 있지 않습니다.** 호출하면 예외가 발생합니다. 사용 가능한 API 는 새 가이드를 봐주세요 |

문의: [platform@tnkfactory.com](mailto:platform@tnkfactory.com)
