## 구현 완료 기능
- 회원가입 및 BCrypt 비밀번호 암호화
- 로그인 및 JWT Access Token 발급
- JwtAuthenticationFilter를 통한 토큰 검증
- 회원 가입 시 기본 계좌 자동 생성
- 입금 기능
- 송금 기능 (@Transactional 적용, 잔액 부족 시 RuntimeException 발생)
- 내 계좌 조회
- 최근 거래 내역 5건 조회
- Controller / Service / Repository 계층 분리
- DTO / Entity 분리

## 미구현 기능
- 관리자 API (전체 회원 목록 조회, 계좌 동결/해제, 전체 잔액 총합 조회)
- 공개 공지사항 조회
- SecurityFilterChain에서 경로별 권한 제한 완성
- 401 / 403 상태코드 명세에 맞는 예외 처리
- 회원가입 시 역할 입력 반영

## 보완이 필요한 부분
- 현재 SecurityConfig가 permitAll() 상태여서 실제 권한 제어가 동작하지 않음
- 입금/송금 시 로그인한 사용자의 본인 계좌인지 확인하는 로직이 필요함
- 예외 처리가 RuntimeException 위주라 공통 예외 처리 클래스 추가 필요
