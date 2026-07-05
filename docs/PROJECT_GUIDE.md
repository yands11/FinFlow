# FinFlow 프로젝트 작업 가이드

> 이 문서는 다른 AI 코딩 도구(Claude Code, Aider, Antigravity 등)에게 프로젝트 컨텍스트를 빠르게 설명하기 위한 요약 가이드입니다. 새 세션을 시작할 때 이 문서를 먼저 붙여넣거나 참고시키세요.

---

## 1. 프로젝트 개요

- **프로젝트명**: FinFlow
- **개발자**: 안드로이드 개발자 (Swift/iOS 경험 적음)
- **목표**: 기존 Android 앱을 Kotlin Multiplatform(KMP) 기반으로 iOS까지 확장
- **현재 상태**: Android 앱은 기본 기능 구현 완료. iOS 타겟은 빌드 환경 세팅 진행 중

---

## 2. 기술 스택

| 영역 | 선택 |
|---|---|
| 아키텍처 | Kotlin Multiplatform (KMP) |
| UI 프레임워크 | **Compose Multiplatform** (Android/iOS UI 코드 공유) |
| 백엔드/DB | **Supabase** (무료 티어) |
| 기존 코드 | Android 네이티브로 기본 기능 구현되어 있음 → shared 모듈로 로직 이전 예정 |

### 왜 Compose Multiplatform인가
- 개발자가 안드로이드 개발자라 Jetpack Compose 지식(`@Composable`, `remember`, `State`, `Modifier`)을 거의 그대로 재사용 가능
- SwiftUI를 새로 배우는 대신 UI 코드 자체를 공유해서 개발 속도 확보
- **나중에 iOS만 SwiftUI로 교체하는 것도 가능** — 단, shared 모듈에 UI 관련 코드를 섞지 않고 비즈니스 로직(ViewModel, 데이터, 네트워크)과 UI 레이어를 최대한 깔끔히 분리해둘 것

### 왜 Supabase인가
- 무료 티어로 개인 프로젝트 규모는 충분히 커버 (500MB DB, 1GB 파일 스토리지, 월 5GB 대역폭, MAU 5만 명)
- **주의**: 7일간 API 요청 없으면 프로젝트 자동 일시정지됨 → 데모/시연 전엔 미리 확인, 필요시 GitHub Actions나 Uptime Robot으로 주기적 ping 설정 고려
- 무료 티어엔 자동 백업 없음 → 중요 데이터는 가끔 수동 백업 권장

---

## 3. 남은 작업 범위 (예상)

1. Android 코드에서 비즈니스 로직을 `shared` 모듈로 이전
2. Compose Multiplatform으로 iOS UI 작성 (Android UI 재사용)
3. iOS 빌드 환경 세팅 (Xcode 서명, 시뮬레이터 등)
4. Supabase 연동 (Auth, DB 읽기/쓰기)
5. 양쪽 플랫폼 테스트 및 마무리